from .consts import TO_TEST_URL


class _InvalidRelativeURLException(Exception):
    pass


def _make_url(relative_url):
    if relative_url and relative_url[0] != '/':
        raise _InvalidRelativeURLException('Must be relative URL')
    full_url = '{}{}'.format(TO_TEST_URL, relative_url)
    return full_url


url_getters = {
    'user': {
        'register': lambda: _make_url('/signup'),
        'profile': lambda: _make_url('/profile'),
        'login': lambda: _make_url('/login'),
        'get_member': lambda uid: _make_url('/members/login/{}'.format(uid))
    },
    'friend': {
        'get_friends': lambda member_id: _make_url('/members/{}/friends'.format(member_id)),
        'add_friends': lambda username: _make_url(
            '/friends?username={}'.format(username))
    },
    'feed': {
        'feeds': lambda: _make_url('/feeds')
    },
    'post': {
        'getAllPost': lambda: _make_url('/posts'),
        'create': lambda: _make_url('/post'),
        'delete': lambda pid: _make_url('/post/{}'.format(pid)),
        'getPostById': lambda pid: _make_url('/post/{}'.format(pid)),
        'patch': lambda pid: _make_url('/post/{}'.format(pid)),
        'getViewers': lambda pid: _make_url('/post/{}/viewers'.format(pid)),
        'getViewGroup': lambda pid: _make_url('/post/{}/viewing_groups'.format(pid)),
        'getCurPost': lambda: _make_url('/post')
    },
    'group': {
        'group_by_name': lambda group_name: _make_url('/group?groupName={}'.format(group_name)),
        'group_list': lambda: _make_url('/group'),
        'group_detail': lambda group_id: _make_url('/group/{}'.format(group_id)),
        'delete_group_member': lambda group_id, username: _make_url(
            '/group/{}/remove?username={}'.format(group_id, username)),
        'add_group_member': lambda group_id, username: _make_url('/group/{}?username={}'.format(group_id, username)),
        'add_group_members': lambda group_id: _make_url('/group/{}/add_all'.format(group_id)),
        'get_group_members': lambda group_id: _make_url('/group/{}/members'.format(group_id)),
        'get_list_of_groups': lambda: _make_url('/groups'),
        'get_all_groups': lambda: _make_url('/admin/groups')
    }
}
