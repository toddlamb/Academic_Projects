from http import HTTPStatus

VIEWS = {'get_list': HTTPStatus.OK.value, 'get_detail': HTTPStatus.OK.value, 'post_list': HTTPStatus.CREATED.value,
         'put_detail': HTTPStatus.OK.value, 'patch_detail': HTTPStatus.OK.value,
         'delete_detail': HTTPStatus.NO_CONTENT.value}

ERROR_CODES = [HTTPStatus.UNAUTHORIZED.value, HTTPStatus.BAD_REQUEST.value, HTTPStatus.NOT_FOUND.value]


def generate_response_docs(field_items):
    result = {}
    for view in VIEWS:
        description = {'description': generate_descriptions(VIEWS[view])}
        if VIEWS[view] == 200 or 201:
            schema = {'schema': generate_schema(view, field_items)}
        result[view] = {VIEWS[view]: description}
        if schema is not None:
            success_response = result[view]
            response_code = success_response[VIEWS[view]]
            response_code['schema'] = generate_schema(view, field_items)
        status_codes = result[view]
        for error in ERROR_CODES:
            status_codes[error] = {'description': generate_descriptions(error)}
    return result


def generate_descriptions(status):
    descriptions = {
        200: 'Transaction Successful',
        201: 'Creation Successful',
        204: 'Deletion Successful',
        400: 'Bad Request: Please, review input data',
        401: 'User is not authenticated',
        404: 'No record(s) could be found for the given request'
    }
    return descriptions[status]


# Generates schema dict for response


def generate_schema(view, field_items):
    result = {}
    if 'list' in view:
        result['type'] = 'array'
        result['items'] = generate_object(field_items)
    else:
        result = generate_object(field_items)
    return result


# Generates Response Object example


def generate_object(field_items):
    return {
        'type': 'object',
        'properties': generate_fields(field_items)
    }


# Generate response example fields


def generate_fields(field_items):
    fields = {}
    for field in field_items:
        fields[field] = {'type': field_items[field]}
    return fields



