import random

import requests

from util.consts import *
from util.functions import get_random_str
from util.urls import url_getters
from util.util_test import TestCaseUserBase


class TestGroups(TestCaseUserBase):
    # Create a user and log-in
    def setUp(self):
        r, t_str, cookie_dict = self.test_success_register_request_and_login()
        self.my_cookie = cookie_dict
        self.my_user_id = r.json()['id']

    # Create a list of user's username for testing
    def generate_list_users(self, user_count):
        result = []
        for val in range(user_count):
            url_getter = url_getters['user']['register']
            url = url_getter()
            user_data = {
                'username': get_random_str(GRP_STR_LENGTH),
                'password': get_random_str(GRP_STR_LENGTH),
                'firstName': get_random_str(GRP_STR_LENGTH),
                'lastName': get_random_str(GRP_STR_LENGTH)
            }
            resp = requests.post(url, json=user_data, headers=JSON_HEADERS_POST)
            self.assertEqual(first=CREATED, second=resp.status_code)
            result.append(resp.json()['username'])
        return result

    # Create a list of user profiles where each user profile is a dict
    def generate_user_dict_list(self, user_count):
        result = []
        for val in range(user_count):
            url_getter = url_getters['user']['register']
            url = url_getter()
            user_data = {
                'username': get_random_str(GRP_STR_LENGTH),
                'password': get_random_str(GRP_STR_LENGTH),
                'firstName': get_random_str(GRP_STR_LENGTH),
                'lastName': get_random_str(GRP_STR_LENGTH)
            }
            resp = requests.post(url, json=user_data, headers=JSON_HEADERS_POST)
            self.assertEqual(first=SUCCESS, second=resp.status_code)
            result.append(resp.json())
        return result

    # Test that regular user shouldn't be able to access admin endpoint
    def test_get_groups_admin(self):
        make_url = url_getters['group']['get_all_groups']
        url = make_url()
        resp = requests.get(url, headers=JSON_HEADERS_GET, cookies=self.my_cookie)
        self.assertEqual(FORBIDDEN, resp.status_code)

    def test_create_group(self):
        make_url = url_getters['group']['group_by_name']
        group_name = get_random_str(GRP_STR_LENGTH)
        url = make_url(group_name)
        resp = requests.post(url, headers=JSON_HEADERS_POST, cookies=self.my_cookie)
        self.assertEqual(CREATED, resp.status_code)
        self.assertEqual(group_name, resp.json()['groupName'])
        self.my_group_id = resp.json()['id']
        self.my_group_name = resp.json()['groupName']

    def test_get_owned_groups(self):
        self.test_create_group()
        make_url = url_getters['group']['get_list_of_groups']
        url = make_url()
        resp = requests.get(url, headers=JSON_HEADERS_GET, cookies=self.my_cookie)
        self.assertEqual(SUCCESS, resp.status_code)
        self.assertEqual(2, len(resp.json()))
        self.assertEqual(self.my_group_id, resp.json()[1]['id'])

    # Tests admin functionality for group endpoint
    def test_get_all_groups(self):
        # Create another user
        url_getter = url_getters['user']['register']
        url = url_getter()
        user_data = {
            'username': get_random_str(GRP_STR_LENGTH),
            'password': get_random_str(GRP_STR_LENGTH),
            'firstName': get_random_str(GRP_STR_LENGTH),
            'lastName': get_random_str(GRP_STR_LENGTH)
        }
        resp = requests.post(url, json=user_data, headers=JSON_HEADERS_POST)
        self.another_user = resp.json()['id']
        # Create group for that user
        another_user_cookie = self.login(user_data['username'], user_data['password'])
        make_url = url_getters['group']['group_by_name']
        url = make_url('test_group')
        resp = requests.post(url, headers=JSON_HEADERS_POST, cookies=another_user_cookie)
        self.assertEqual(CREATED, resp.status_code)
        # Test that results contain groups of both users
        make_url = url_getters['group']['get_all_groups']
        url = make_url()
        admin_cookie = self.login('admin', 'admin')
        resp = requests.get(url, headers=JSON_HEADERS_GET, cookies=admin_cookie)
        self.assertEqual(SUCCESS, resp.status_code)
        user1 = user2 = False
        for response_item in resp.json():
            if response_item['owner']['id'] == self.my_user_id:
                user1 = True
            if response_item['owner']['id'] == self.another_user:
                user2 = True
        self.assertTrue(user1 and user2)

    def test_get_group_byName(self):
        self.test_create_group()
        make_url = url_getters['group']['group_by_name']
        url = make_url(self.my_group_name)
        resp = requests.get(url, headers=JSON_HEADERS_GET, cookies=self.my_cookie)
        self.assertEqual(SUCCESS, resp.status_code)
        resp_dict = resp.json()
        self.assertEqual(resp_dict['id'], self.my_group_id)
        self.assertEqual(resp_dict['groupName'], self.my_group_name)

    def test_get_group_byID(self):
        self.test_create_group()
        make_url = url_getters['group']['group_detail']
        url = make_url(self.my_group_id)
        resp = requests.get(url, headers=JSON_HEADERS_GET, cookies=self.my_cookie)
        self.assertEqual(SUCCESS, resp.status_code)
        resp_dict = resp.json()
        self.assertEqual(resp_dict['id'], self.my_group_id)
        self.assertEqual(resp_dict['owner']['id'], self.my_user_id)

    # Adds friends to user so they can be added to groups
    def add_Friends(self, user_list):
        make_url = url_getters['friend']['add_friends']
        for user in user_list:
            url = make_url(user)
            resp = requests.patch(url, headers=JSON_HEADERS_POST, cookies=self.my_cookie)
            self.assertEqual(SUCCESS, resp.status_code)

    def test_add_group_members(self):
        self.test_create_group()
        make_url = url_getters['group']['add_group_members']
        url = make_url(self.my_group_id)
        user_list = self.generate_list_users(5)
        self.add_Friends(user_list)
        json_params = {"usernames": user_list}
        resp = requests.patch(url, json=json_params, headers=JSON_HEADERS_POST, cookies=self.my_cookie)
        resp_dict = resp.json()
        self.assertEqual(resp_dict['id'], self.my_group_id)
        self.assertEqual(resp_dict['owner']['id'], self.my_user_id)
        member_list = resp_dict['members']
        for member in member_list:
            self.assertIn(member['username'], user_list)
        return user_list

    def test_add_group_member(self):
        self.test_create_group()
        make_url = url_getters['group']['add_group_member']
        user_list = self.generate_list_users(1)
        self.add_Friends(user_list)
        url = make_url(self.my_group_id, user_list[0])
        resp = requests.patch(url, headers=JSON_HEADERS_POST, cookies=self.my_cookie)
        self.assertEqual(SUCCESS, resp.status_code)
        self.assertEqual(resp.json()[0]['username'], user_list[0])

    def test_get_group_members(self):
        make_url = url_getters['group']['get_group_members']
        user_list = self.test_add_group_members()
        url = make_url(self.my_group_id)
        resp = requests.get(url, headers=JSON_HEADERS_GET, cookies=self.my_cookie)
        self.assertEqual(SUCCESS, resp.status_code)
        for member in resp.json():
            self.assertIn(member['username'], user_list)

    def test_delete_group_members(self):
        user_list = self.test_add_group_members()
        user = user_list[random.randint(0, 10000) % user_list.__len__()]
        user_list.remove(user)
        make_url = url_getters['group']['delete_group_member']
        url = make_url(self.my_group_id, user)
        resp = requests.patch(url, headers=JSON_HEADERS_POST, cookies=self.my_cookie)
        self.assertEqual(SUCCESS, resp.status_code)
        members = resp.json()['members']
        for member in members:
            self.assertIn(member['username'], user_list)

    def test_delete_group(self):
        self.test_create_group()
        make_url = url_getters['group']['group_detail']
        url = make_url(self.my_group_id)
        resp = requests.delete(url, cookies=self.my_cookie)
        self.assertEqual(SUCCESS, resp.status_code)
        resp = requests.get(url, cookies=self.my_cookie)
        self.assertEqual(NOT_FOUND, resp.status_code)
