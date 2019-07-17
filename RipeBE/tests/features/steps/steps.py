import os
from io import BytesIO

from behave import *
from nose.tools import assert_equals, assert_not_equals

from server.RipeServer import user_collection

USER_INFO = {
    'id': 'user12345',
    'name': 'John Doe',
    'email': 'jdoe@email.com',
    'videos_uploaded': [],
    'score': 0,
    'saved_videos': [],
    'tags': [],
}

CONTENT_INFO = {
    'uid': 'content123',
    'title': 'Meme1',
    'uploaded_by': 'user12345',
    'upvotes': 0,
    'downvotes': 0,
    'views': 0
}


@given('The user does not exist and some user information')
def step_impl(context):
    user_collection.delete_one(USER_INFO)
    context.user_info = USER_INFO


@when('I try to create a new user')
def step_impl(context):
    context.response = context.app.post('/create_user', data=context.user_info)


@then('I should get a 200 response')
def step_imp(context):
    assert_equals(context.response.status_code, 200)


@step("the user should be in the mongoDB")
def step_impl(context):
    user = user_collection.find_one(USER_INFO)
    assert_not_equals(user, None)


@given("The file exists along with content data")
def step_impl(context):
    if os.path.exists('test.jpg'):
        print(os.path)
        content = (BytesIO(b'my file contents'), "test.jpg")
        context.content_info = CONTENT_INFO
        context.content_info['file'] = content


@when("I try to upload content")
def step_impl(context):
    context.response = context.app.post('/post_content', data=context.content_info)


@step("the content data should be in the mongoDB")
def step_impl(context):
    pass
