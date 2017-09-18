from docs_swagger.util import *
from unittest import TestCase


class DocGenerationTesting(TestCase):
    def setUp(self):
        self.my_fields = {'id': 'integer', 'name': 'string'}

    def test_fields(self):
        self.assertEqual(
            {
                'id': {
                    'type': 'integer'
                },
                'name': {
                    'type': 'string'
                }
            }, generate_fields(self.my_fields)
        )

    def test_object(self):
        self.assertEqual(
            {
                'type': 'object',
                'properties': generate_fields({'id': 'integer', 'name': 'string'})
            },
            generate_object(self.my_fields)
        )

    def test_schema(self):
        self.assertEqual(
            {
                'type': 'array',
                'items': {
                    'type': 'object',
                    'properties': {
                        'id': {
                            'type': 'integer'
                        },
                        'name': {
                            'type': 'string'
                        }
                    }
                }
            },
            generate_schema('get_list', self.my_fields)

        )
