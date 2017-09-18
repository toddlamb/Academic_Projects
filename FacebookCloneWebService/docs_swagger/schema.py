from urllib.parse import urljoin
from openapi_codec import encode
from rest_framework.permissions import AllowAny
from rest_framework.renderers import CoreJSONRenderer
from rest_framework.schemas import *
from rest_framework.views import APIView
from rest_framework_swagger.renderers import *
from docs_swagger.util import ERROR_CODES, generate_descriptions

"""
ref = https://stackoverflow.com/questions/40175410/how-to-generate-list-of-response-messages-in-django-rest-swagger
"""


def custom_field_to_schema(field):
    title = force_text(field.label) if field.label else ''
    description = force_text(field.help_text) if field.help_text else ''

    if isinstance(field, serializers.ListSerializer):
        child_schema = custom_field_to_schema(field.child)
        return coreschema.Array(
            items=child_schema,
            title=title,
            description=description
        )
    elif isinstance(field, serializers.Serializer):
        return coreschema.Object(
            properties=OrderedDict([
                (key, custom_field_to_schema(value))
                for key, value
                in field.fields.items()
            ]),
            title=title,
            description=description
        )
    elif isinstance(field, serializers.ManyRelatedField):
        return coreschema.Array(
            items=coreschema.String(),
            title=title,
            description=description
        )
    elif isinstance(field, serializers.PrimaryKeyRelatedField):
        return coreschema.Integer(title=title, description=description)
    elif isinstance(field, serializers.RelatedField):
        return coreschema.String(title=title, description=description)
    elif isinstance(field, serializers.MultipleChoiceField):
        return coreschema.Array(
            items=coreschema.Enum(enum=list(field.choices.keys())),
            title=title,
            description=description
        )
    elif isinstance(field, serializers.ChoiceField):
        return coreschema.Enum(
            enum=list(field.choices.keys()),
            title=title,
            description=description
        )
    elif isinstance(field, serializers.CharField):
        return coreschema.String(title=title, description=description)
    elif isinstance(field, serializers.BooleanField):
        return coreschema.Boolean(title=title, description=description)
    elif isinstance(field, (serializers.DecimalField, serializers.FloatField)):
        return coreschema.Number(title=title, description=description)
    elif isinstance(field, serializers.IntegerField):
        return coreschema.Integer(title=title, description=description)

    if field.style.get('base_template') == 'textarea.html':
        return coreschema.String(
            title=title,
            description=description,
            format='textarea'
        )
    # Changed to default of integer
    return coreschema.Integer(title=title, description=description)


class SwaggerSchemaView(APIView):
    permission_classes = [AllowAny]
    renderer_classes = [
        OpenAPIRenderer,
        SwaggerUIRenderer,
        CoreJSONRenderer,
    ]

    def get(self, request):
        generator = CustomSchemaGenerator()
        schema = generator.get_schema(request=request)

        return Response(schema)


def _custom_get_responses(link):
    detail = False
    if '{id}' in link.url:
        detail = True
    return link._responses_docs.get(
        '{}_{}'.format(link.action, 'list' if not detail else 'detail'),
        link._responses_docs
    )


# Very nasty; Monkey patching;
encode._get_responses = _custom_get_responses


class CustomSchemaGenerator(SchemaGenerator):
    def get_path_fields(self, path, method, view):
        """
               Return a list of `coreapi.Field` instances corresponding to any
               templated path variables.
               """
        model = getattr(getattr(view, 'queryset', None), 'model', None)
        fields = []

        for variable in uritemplate.variables(path):
            title = ''
            description = ''
            # Custom replace string with integer
            schema_cls = coreschema.Integer
            kwargs = {}
            if model is not None:
                # Attempt to infer a field description if possible.
                try:
                    model_field = model._meta.get_field(variable)
                except:
                    model_field = None

                if model_field is not None and model_field.verbose_name:
                    title = force_text(model_field.verbose_name)

                if model_field is not None and model_field.help_text:
                    description = force_text(model_field.help_text)
                elif model_field is not None and model_field.primary_key:
                    description = get_pk_description(model, model_field)

                if hasattr(view, 'lookup_value_regex') and view.lookup_field == variable:
                    kwargs['pattern'] = view.lookup_value_regex
                elif isinstance(model_field, models.AutoField):
                    schema_cls = coreschema.Integer

            field = coreapi.Field(
                name=variable,
                location='path',
                required=True,
                schema=schema_cls(title=title, description=description, **kwargs)
            )
            fields.append(field)

        return fields

    def get_serializer_fields(self, path, method, view):
        """
        Return a list of `coreapi.Field` instances corresponding to any
        request body input, as determined by the serializer class.
        """
        if method not in ('PUT', 'PATCH', 'POST'):
            return []

        if not hasattr(view, 'get_serializer'):
            return []

        serializer = view.get_serializer()

        if isinstance(serializer, serializers.ListSerializer):
            return [
                coreapi.Field(
                    name='data',
                    location='body',
                    required=True,
                    schema=coreschema.Array()
                )
            ]

        if not isinstance(serializer, serializers.Serializer):
            return []

        fields = []
        for field in serializer.fields.values():
            if field.read_only or isinstance(field, serializers.HiddenField):
                continue

            required = field.required and method != 'PATCH'
            field = coreapi.Field(
                name=field.field_name,
                location='form',
                required=required,
                schema=custom_field_to_schema(field),
            )
            fields.append(field)

        return fields

    def get_link(self, path, method, view):
        """
        Return a `coreapi.Link` instance for the given endpoint.
        """
        fields = self.get_path_fields(path, method, view)
        fields += self.get_serializer_fields(path, method, view)
        fields += self.get_pagination_fields(path, method, view)
        fields += self.get_filter_fields(path, method, view)
        if fields and any([field.location in ('form', 'body') for field in fields]):
            encoding = self.get_encoding(path, method, view)
        else:
            encoding = None

        description = self.get_description(path, method, view)

        if self.url and path.startswith('/'):
            path = path[1:]

        # CUSTOM
        data_link = coreapi.Link(
            url=urljoin(self.url, path),
            action=method.lower(),
            encoding=encoding,
            fields=fields,
            description=description
        )

        data_link._responses_docs = self.get_response_docs(path, method, view)

        return data_link

    def get_response_docs(self, path, method, view):
        if method in ('PUT', 'PATCH', 'GET'):
            response = '200'
        elif method == 'POST':
            response = '201'
        else:
            response = '204'
        if hasattr(view, 'responses_docs'):
            return view.responses_docs
        else:
            result = {response: {'description': 'Transaction Successful'}}
            for code in ERROR_CODES:
                result[code] = {'description': generate_descriptions(code)}
            return result
