from util.urls import url_getters
import requests
from util.util_test import TestCaseUserBase
from util.consts import *
from util.functions import get_random_str


class TestPost(TestCaseUserBase):

    def setUp(self):
        r, t_str, cookie_dict = self.test_success_register_request_and_login()
        self.testUserStr = t_str
        self.testUserCookies = cookie_dict
        r_data = r.json()
        self.testUserId = r_data['id']


    # Test function: return all the posts
    def test_get_post(self):
        url_getter = url_getters['post']['getAllPost']
        url = url_getter()
        resp = requests.get(url, cookies=self.testUserCookies)
        self.assertEqual(resp.status_code, 200)


    # Test function: create a post that belong to the current logged in member
    def test_create_post(self):
        url_getter = url_getters['post']['create']
        postContent = {"content": get_random_str(POST_STR_LENGTH)}
        url = url_getter()
        resp = requests.post(url, json=postContent, headers=JSON_HEADERS_POST, cookies=self.testUserCookies)
        self.assertEqual(resp.status_code, 201)
        self.post_id = resp.json()['id']


    # Test function: detele a post from current logged in member by post id
    def test_delete_post(self):
        self.test_create_post()
        url_getter = url_getters['post']['delete']
        url = url_getter(self.post_id)
        resp = requests.delete(url, cookies=self.testUserCookies)
        self.assertEqual(200, resp.status_code)
        resp = requests.get(url, cookies=self.testUserCookies)
        self.assertEqual(404, resp.status_code)


    # Test function: get a post by both owner id and post id in case a member modify post that is not belong to him
    def test_get_postId(self):
        self.test_create_post()
        url_getter = url_getters['post']['getPostById']
        url = url_getter(self.post_id)
        resp = requests.get(url, cookies=self.testUserCookies)
        self.assertEqual(200, resp.status_code)


    # Test function: grant access of specific post to group and member
    def test_grant_post(self):
        self.test_create_post()
        url_getter = url_getters['post']['patch']
        url = url_getter(self.post_id)
        data = {
            "groupNames": ["string"],
            "usernames": ["string"]
        }
        resp = requests.get(url, json= data, cookies=self.testUserCookies)
        self.assertEqual(200, resp.status_code)


    # Test function: get all post viewers from current logged in member by post id
    def test_get_post_viewers(self):
        self.test_create_post()
        url_getter = url_getters['post']['getViewers']
        url = url_getter(self.post_id)
        resp = requests.get(url, cookies=self.testUserCookies)
        self.assertEqual(200, resp.status_code)

    # Test function: get all post viewing groups from current logged in member by post id
    def test_getViewGroups(self):
        self.test_create_post()
        url_getter = url_getters['post']['getViewGroup']
        url = url_getter(self.post_id)
        resp = requests.get(url, cookies=self.testUserCookies)
        self.assertEqual(200, resp.status_code)


    # Test function: return all the post owned by current logged in member
    # failing
    def test_getCurPosts(self):
        url_getter = url_getters['post']['getCurPost']
        url = url_getter()
        resp = requests.get(url, headers=JSON_HEADERS_GET, cookies=self.testUserCookies)
        self.assertEqual(resp.status_code, 405)






