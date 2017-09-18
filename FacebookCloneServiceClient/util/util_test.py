from unittest import TestCase
from util.urls import url_getters
from util.functions import get_random_str
import requests
from util.consts import JSON_HEADERS_POST, T_STR_LEN, SUCCESS


class TestCaseUserBase(TestCase):
    def test_success_register_request_and_login(self):
        url_getter = url_getters['user']['register']
        url = url_getter()
        t_str = 'test_user_' + get_random_str(T_STR_LEN)
        data = {
            'username': t_str,
            'password': t_str,
            'firstName': t_str,
            'lastName': t_str
        }
        r = requests.post(url, json=data, headers=JSON_HEADERS_POST)
        # assert return code matched docs
        self.assertEqual(r.status_code, 201)
        url_getter = url_getters['user']['login']
        login_data = {
            'username': t_str,
            'password': t_str
        }
        url = url_getter()
        resp = requests.post(url, data=login_data)
        self.assertEqual(SUCCESS, resp.status_code)
        cookie_dict = resp.cookies
        return r, t_str, cookie_dict

    def login(self, username, password):
        url_getter = url_getters['user']['login']
        login_data = {
            'username': username,
            'password': password
        }
        url = url_getter()
        resp = requests.post(url, data=login_data)
        self.assertEqual(SUCCESS, resp.status_code)
        cookie_dict = resp.cookies
        return cookie_dict

    """
    def test_stuff(self):
        url_getter = url_getters['group']['get_list']
        url = url_getter('test')
        r, t_str, cookie_dict = self.test_success_register_request_and_login()
        resp = requests.get(url, cookies=cookie_dict)
        self.assertEqual(404, resp.status_code)
    """
