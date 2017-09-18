from util.urls import url_getters
from util.functions import get_random_str
import requests
import json
from util.consts import JSON_HEADERS_GET, JSON_HEADERS_POST, T_STR_LEN
from util.util_test import TestCaseUserBase

def json_req(data):
    return json.dumps(data)


DNE_USER_ID = 10000


class TestCaseLoginAndRegister(TestCaseUserBase):
    def setUp(self):
        r, t_str, cookie_dict = self.test_success_register_request_and_login()
        self.testUserStr = t_str
        self.testUserCookies = cookie_dict
        r_data = r.json()
        self.testUserId = r_data['id']

    def test_get_user(self):
        url_getter = url_getters['user']['get_member']
        url = url_getter(self.testUserId)
        r = requests.get(url, headers=JSON_HEADERS_GET, cookies=self.testUserCookies)
        self.assertTrue(r.status_code, 200)

    def test_get_user_dne(self):
        url_getter = url_getters['user']['get_member']
        url = url_getter(DNE_USER_ID)
        r = requests.get(url, headers=JSON_HEADERS_GET, cookies=self.testUserCookies)
        self.assertTrue(r.status_code, 404)

    def test_login(self):
        # tested for every test ran
        pass

    def test_login_invalid(self):
        url_getter = url_getters['user']['login']
        login_data = {
            'username': self.testUserStr,
            'password': 'foo'
        }
        url = url_getter()
        r = requests.post(url, data=login_data)
        self.assertEqual(401, r.status_code)

    def test_duplicate_username(self):
        url_getter = url_getters['user']['register']
        url = url_getter()
        t_str = 'test_user_' + get_random_str(T_STR_LEN)
        data = {
            'username': self.testUserStr,
            'password': t_str,
            'firstName': t_str,
            'lastName': t_str
        }
        r = requests.post(url, json=data, headers=JSON_HEADERS_POST)
        # assert return code matched docs
        self.assertTrue(400 <= r.status_code <= 499)


class TestCaseProfile(TestCaseUserBase):
    # lets test not passing a password
    def test_profile(self):
        r, t_str, cookie_dict = self.test_success_register_request_and_login()
        self.testUserCookies = cookie_dict
        r_data = r.json()
        # check to see we can actually get a profile for the user
        # user_id = r_data['id']

        # check the profile endpoint to make sure user exists with right data
        url_getter = url_getters['user']['profile']
        url = url_getter()
        r = requests.get(url, headers=JSON_HEADERS_GET, cookies=self.testUserCookies)
        self.assertEqual(r.status_code, 200)
        self.assertEqual(r_data['username'], t_str)
        r_data = r.json()
        self.assertEqual(r_data['firstName'], t_str)
        self.assertEqual(r_data['lastName'], t_str)





