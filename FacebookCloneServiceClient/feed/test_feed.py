from util.urls import url_getters
import requests
from util.util_test import TestCaseUserBase


class TestGetFeeds(TestCaseUserBase):

    def setUp(self):
        r, t_str, cookie_dict = self.test_success_register_request_and_login()
        self.testUserStr = t_str
        r_data = r.json()
        self.testUserId = r_data['id']

    def test_get_feeds(self):
        url_getter = url_getters['feed']['feeds']
        url = url_getter()
        r, t_str, cookie_dict = self.test_success_register_request_and_login()
        resp = requests.get(url, cookies=cookie_dict)
        self.assertEqual(200, resp.status_code)

    def test_update_feeds(self):
        url_getter = url_getters['feed']['feeds']
        url = url_getter()
        r, t_str, cookie_dict = self.test_success_register_request_and_login()
        resp = requests.put(url, cookies=cookie_dict)
        self.assertEqual(405, resp.status_code)

    def test_delete_feeds(self):
        url_getter = url_getters['feed']['feeds']
        url = url_getter()
        r, t_str, cookie_dict = self.test_success_register_request_and_login()
        resp = requests.delete(url, cookies=cookie_dict)
        self.assertEqual(405, resp.status_code)

    def test_create_feeds(self):
        url_getter = url_getters['feed']['feeds']
        url = url_getter()
        r, t_str, cookie_dict = self.test_success_register_request_and_login()
        resp = requests.post(url, cookies=cookie_dict)
        self.assertEqual(405, resp.status_code)