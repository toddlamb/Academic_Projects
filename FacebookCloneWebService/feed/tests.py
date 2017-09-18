from django.urls import reverse
from django.test import TestCase
from rest_framework.test import APITestCase
from rest_framework import status
from .models import *
from my_user.models import User
from post.models import Post

foo_kwargs = {'pk': 0}

# commenting out until we would need to give user access to feed object
# class FeedTestCase(APITestCase):
#
#     def setUp(self):
#         """
#         Data setup for the test.
#         """
#         self.user1 = User.objects.create_user('test_user1', 'test_user1@email.com', 'password')
#         self.user1.save()
#
#     def test_create_feed(self):
#         """
#         Ensure an user can not create a feed.
#         """
#         self.client.login(username='test_user1', password='password')
#         url = reverse('api:feed:feed_view', kwargs=foo_kwargs)
#         data = {'user': User.objects.get(username='test_user1').id}
#         response = self.client.post(url, data, format='json')
#         self.assertEqual(response.status_code, status.HTTP_405_METHOD_NOT_ALLOWED)
#
#     def test_delete_feed(self):
#         """
#         Ensure an user can not delete a feed.
#         """
#         self.client.login(username='test_user1', password='password')
#         url = reverse('api:feed:feed_view', kwargs=foo_kwargs)
#         data = {'user': User.objects.get(username='test_user1').id}
#         response = self.client.delete(url, data, format='json')
#         self.assertEqual(response.status_code, status.HTTP_405_METHOD_NOT_ALLOWED)
#
#     def test_update_feed(self):
#         """
#         Ensure an user can not update a feed.
#         """
#         self.client.login(username='test_user1', password='password')
#         url = reverse('api:feed:feed_view', kwargs=foo_kwargs)
#         data = {'user': User.objects.get(username='test_user1').id}
#         response = self.client.put(url, data, format='json')
#         self.assertEqual(response.status_code, status.HTTP_405_METHOD_NOT_ALLOWED)
#
#     def test_get_with_valid_permission(self):
#         """
#         To test that an user can only see its own feed.
#         """
#         self.client.login(username='test_user1', password='password')
#         feed1 = Feed.objects.create(user=self.user1)
#         url = reverse('api:feed:feed_view', kwargs={'pk': feed1.id})
#         response = self.client.get(url)
#         self.assertEqual(response.status_code, status.HTTP_200_OK)
#         self.assertEqual(response.data, {'user': feed1.user.id})
#
#     def test_get_with_invalid_permission(self):
#         """
#         To test that an unauthorized user should not see any feed.
#         """
#         feed1 = Feed.objects.create(user=self.user1)
#         url = reverse('api:feed:feed_view', kwargs={'pk': feed1.id})
#         response = self.client.get(url)
#         self.assertEqual(response.status_code, status.HTTP_401_UNAUTHORIZED)


class FeedModelTestCase(TestCase):
    def test_feed_made_upon_user_creation(self):
        user = User.objects.create_user('test_user1', 'test_user1@email.com', 'password')
        feed_exists = Feed.objects.filter(user=user)
        self.assertTrue(feed_exists)


class FeedItemTestCase(APITestCase):

    def setUp(self):
        """
        Data setup for the test.
        """
        self.user1 = User.objects.create_user('test_user1', 'test_user1@email.com', 'password')
        self.user2 = User.objects.create_user('test_user2', 'test_user2@email.com', 'password')
        self.feed1 = Feed.objects.get(user=self.user1)
        self.post_sample = Post.objects.create(owner=self.user2, text='hello world')
        self.feeditem1 = FeedItem.objects.create(feed=self.feed1, post=self.post_sample)

    def test_create_feeditem(self):
        """
        Ensure an user can not create a feeditem.
        """
        self.client.login(username='test_user1', password='password')
        url = reverse('api:feed:feed_item-list')
        data = {'feed': self.feed1.id, 'post': self.post_sample.id}
        response = self.client.post(url, data, format='json')
        self.assertEqual(response.status_code, status.HTTP_405_METHOD_NOT_ALLOWED)

    def test_delete_feeditem(self):
        """
        Ensure an user can not delete a feeditem.
        """
        self.client.login(username='test_user1', password='password')
        url = reverse('api:feed:feed_item-detail', kwargs={'pk': self.feeditem1.id})
        data = {'feed': self.feed1.id, 'post': self.post_sample.id}
        response = self.client.delete(url, data, format='json')
        self.assertEqual(response.status_code, status.HTTP_405_METHOD_NOT_ALLOWED)

    def test_update_feeditem(self):
        """
        Ensure an user can not update a feeditem.
        """
        self.client.login(username='test_user1', password='password')
        url = reverse('api:feed:feed_item-detail', kwargs={'pk': self.feeditem1.id})
        data = {'feed': self.feed1.id, 'post': self.post_sample.id}
        response = self.client.put(url, data, format='json')
        self.assertEqual(response.status_code, status.HTTP_405_METHOD_NOT_ALLOWED)

    def test_get_feeditem_with_valid_permission(self):
        """
        To test that an user can only see its own feeditems.
        """
        self.client.login(username='test_user1', password='password')
        url = reverse('api:feed:feed_item-detail', kwargs={'pk': self.feeditem1.id})
        response = self.client.get(url)
        self.assertEqual(response.status_code, status.HTTP_200_OK)
        self.assertEqual(1, FeedItem.objects.count())
        self.assertEqual(response.data['feed'], self.feed1.id)
        self.assertEqual(
            response.data['post'],
            {
                'id': self.post_sample.id,
                'owner': self.post_sample.owner.id,
                'text': self.post_sample.text
            }
        )

    def test_get_feeditem_with_invalid_permission(self):
        """
        To test that an unauthorized user should not see any feed.
        """
        url = reverse('api:feed:feed_item-list')
        response = self.client.get(url)
        self.assertEqual(response.status_code, status.HTTP_401_UNAUTHORIZED)

