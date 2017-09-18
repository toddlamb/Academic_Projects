from django.conf.urls import url, include
from . import views
from rest_framework.routers import DefaultRouter

# Create a router and register our viewsets with it.
router = DefaultRouter()
router.register(r'', views.FeedItemViewSet, base_name='feed_item')

# The API URLs are now determined automatically by the router.
# Additionally, we include the login URLs for the browsable API.
urlpatterns = [
    url(r'^', include(router.urls)),
    # url(r'internal_feed/(?P<pk>[0-9]+)', views.FeedView.as_view(), name='feed_view')
]
