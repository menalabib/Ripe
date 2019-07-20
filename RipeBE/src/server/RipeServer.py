import os

from azure.storage.blob import BlockBlobService, PublicAccess
from flask import Flask, request, jsonify
from pymongo import MongoClient
from random import shuffle
from werkzeug.exceptions import BadRequest
from werkzeug.utils import secure_filename

from server import constants
from server.InvalidUsage import InvalidUsage

# data constants
TITLE = 'title'
UID = 'uid'
UUID = 'uuid'
TAGS = 'tags'
UPLOADED_BY = 'uploaded_by'
UPVOTES = 'upvotes'
DOWNVOTES = 'downvotes'
VIEWS = 'views'
IS_VIDEO = 'is_video'
SCORE = 'score'
ACTION = 'action'
NAME = 'name'
EMAIL = 'email'
CONTENT_UPLOADED = 'content_uploaded'
SAVED_CONTENT = 'saved_content'
VIEWED_CONTENT = 'viewed_content'
TOP_TEN = 'top_ten'
FULL_LEADERBOARD = 'full_leaderboard'
MEMBERS = 'members'
GROUPS = 'groups'
GALLERY = 'gallery'

# db constants
client = MongoClient('127.0.0.1', 27017, connect=False)
db = client['local']
content_collection = db['content']
user_collection = db['users']
leaderboard_collection = db['leaderboard']
group_collection = db['groups']
group_content_collection = db['group_content']

# flask configurations
app = Flask(__name__)
app.config['UPLOAD_FOLDER'] = constants.paths['UPLOAD_FOLDER']


# user constants
@app.errorhandler(InvalidUsage)
def handle_invalid_usage(error):
    response = jsonify(error.to_dict())
    response.status_code = error.status_code
    return response


@app.route('/post_content', methods=['POST'])
def post_content():
    if not os.path.exists(constants.paths['UPLOAD_FOLDER']):
        os.makedirs(constants.paths['UPLOAD_FOLDER'])

    file = request.files['file']
    filename = secure_filename(file.filename)
    file.save(os.path.join(app.config['UPLOAD_FOLDER'], filename))
    tags = request.form[TAGS].split(",")
    user = request.form[UPLOADED_BY]

    content = {
        TITLE: request.form[TITLE],
        TAGS: tags,
        UPLOADED_BY: user,
        UPVOTES: 0,
        DOWNVOTES: 0,
        VIEWS: 0,
        IS_VIDEO: int(request.form[IS_VIDEO]),
    }
    post_id = content_collection.insert_one(content).inserted_id

    content_collection.update_one({'_id': post_id}, {'$set': {UID: str(post_id)}})
    user_collection.update_one({UUID: user}, {'$push': {CONTENT_UPLOADED: str(post_id)}})

    # Create the BlockBlockService that is used to call the Blob service for the storage account.
    block_blob_service = BlockBlobService(account_name=constants.blob_consts['ACCOUNT_NAME'],
                                          account_key=constants.blob_consts['BLOB_ACCOUNT_KEY'])

    # Create a container called 'usercontent'.
    block_blob_service.create_container(constants.blob_consts['CONTAINER_NAME'])

    # Set the permission so the blobs are public.
    block_blob_service.set_container_acl(constants.blob_consts['CONTAINER_NAME'], public_access=PublicAccess.Container)

    # Upload the created file, use local_file_name for the blob name.
    block_blob_service.create_blob_from_path(constants.blob_consts['CONTAINER_NAME'], str(post_id),
                                             os.path.join(constants.paths['UPLOAD_FOLDER'], filename))

    # List the blobs in the container.
    # print("\nList blobs in the container")
    # generator = block_blob_service.list_blobs(constants.blob_consts['CONTAINER_NAME'])
    # for blob in generator:
    #     print("\t Blob name: " + blob.name)

    print(post_id)
    return jsonify(str(post_id))


@app.route('/create_user', methods=['POST'])
def create_user():
    email = request.form[EMAIL]
    user = user_collection.find_one({EMAIL: email})
    if user is not None:
        return jsonify(user[UUID])
    user = {
        NAME: request.form[NAME],
        EMAIL: email,
        CONTENT_UPLOADED: [],
        SCORE: 0,
        SAVED_CONTENT: [],
        VIEWED_CONTENT: [],
        TAGS: [],
        GROUPS: [],
    }
    post_id = user_collection.insert(user)
    user_collection.update_one({EMAIL: request.form[EMAIL]}, {'$set': {UUID: str(post_id)}})

    return jsonify(str(post_id))


# get content for user
@app.route('/get_content_for_user/<user_uid>', methods=['GET'])
def get_content_for_user(user_uid):
    user_dict = user_collection.find_one({UUID: user_uid})
    if user_dict is None:
        raise BadRequest('User Not Found!')

    user_tags = user_dict[TAGS]
    shuffle(user_tags)
    viewed_content = user_dict[VIEWED_CONTENT]
    content_uploaded = user_dict[CONTENT_UPLOADED]
    # user_content = []
    # return_list = []

    for tag in user_tags:
        user_content = content_collection.find_one({TAGS: tag})

        # for user_cont in user_content:
        if str(user_content.get('_id')) not in viewed_content and str(user_content.get('_id')) not in content_uploaded:
            user_collection.update_one({UUID: user_uid}, {'$push': {VIEWED_CONTENT: user_content.get(UID)}})
            return jsonify([user_content.get(UID), user_content.get(TITLE), user_content.get(IS_VIDEO)])

    # for all_cont in list(content_collection.find()):
    #     if all_cont not in viewed_content:
    #         return_list.append()
    # return jsonify(return_list)
    user_content = list(content_collection.aggregate([
        {"$match": {"tags": {"$exists": True}}},
        {"$sample": {"size": 3}}
    ]))
    for content in user_content:
        if str(content.get('_id')) not in viewed_content and str(content.get('_id')) not in content_uploaded:
            user_collection.update_one({UUID: user_uid}, {'$push': {VIEWED_CONTENT: content.get(UID)}})
            return jsonify([content.get(UID), content.get(TITLE), content.get(IS_VIDEO)])

    return jsonify(['No Content Available', '', '0'])


@app.route('/update_content_view/<user_uid>/<content_uid>', methods=['PUT'])
def update_content_views(user_uid, content_uid):
    if ACTION in request.form:
        content_dict = content_collection.find_one({UID: content_uid})
        user_dict = user_collection.find_one({UUID: user_uid})
        content_collection.update_one({UID: content_uid}, {'$inc': {VIEWS: 1}})
        if request.form[ACTION] == '0':
            content_collection.update_one({UID: content_uid}, {'$inc': {DOWNVOTES: 1}})
        elif request.form[ACTION] == '1':
            content_collection.update_one({UID: content_uid}, {'$inc': {UPVOTES: 1}})
            user_collection.update_one({CONTENT_UPLOADED: content_uid}, {'$inc': {SCORE: 1}})
            for tag in content_dict.get(TAGS):
                if tag not in user_dict.get(TAGS):
                    user_collection.update_one({UUID: user_uid}, {'$push': {TAGS: tag}})
            if content_uid not in user_dict[SAVED_CONTENT]:
                user_collection.update_one({UUID: user_uid}, {'$push': {SAVED_CONTENT: content_uid}})

        return "Content Updated!"

    raise BadRequest('Action Not Found!')


@app.route('/get_leaderboard', methods=['GET'])
def get_leaderboard():
    return_list = []
    ordered_users = user_collection.find({'$query': {}, '$orderby': {SCORE: -1}})
    for i in range(min(10, user_collection.count())):
        return_list.append((ordered_users[i][NAME], ordered_users[i][SCORE]))

    print(return_list)
    return jsonify(return_list)


@app.route('/get_user_by_id/<user_uid>', methods=['GET'])
def get_user_by_id(user_uid):
    user_dict = user_collection.find_one({UUID: user_uid})
    if user_dict is None:
        raise BadRequest('User Not Found!')
    user_dict['uuid'] = str(user_dict.get('_id'))
    del user_dict['_id']
    return jsonify(user_dict)


# Interact with content
@app.route('/get_content_by_id/<content_uid>', methods=['GET'])
def get_content_by_id(content_uid):
    content_dict = content_collection.find_one({UID: content_uid})
    if content_dict is None:
        raise BadRequest('Content Not Found!')
    content_dict[UID] = str(content_dict.get('_id'))
    del content_dict['_id']
    return jsonify(content_dict)


@app.route('/create_group/<user_uid>', methods=['POST'])
def create_group(user_uid):
    group = {
        MEMBERS: [user_uid],
        CONTENT_UPLOADED: [],
        GALLERY: []
    }
    uid = group_collection.insert_one(group).inserted_id
    group_id = str(uid)[-5:]
    group_collection.update_one({'_id': uid}, {'$set': {UID: group_id}})
    user_collection.update_one({UUID: user_uid}, {'$push': {GROUPS: group_id}})
    return jsonify(group_id)


@app.route('/join_group/<user_uid>/<group_uid>', methods=['PUT'])
def join_group(user_uid, group_uid):
    group_dict = group_collection.find_one({UID: group_uid})
    if group_dict is None:
        raise BadRequest('Invalid Group ID')
    if user_uid in group_dict[MEMBERS]:
        raise BadRequest('User already in group!')
    group_collection.update_one({UID: group_uid}, {'$push': {MEMBERS: user_uid}})
    user_collection.update_one({UUID: user_uid}, {'$push': {GROUPS: group_uid}})
    return "Group members updated!"


@app.route('/post_to_group/<group_uid>', methods=['POST'])
def post_to_group(group_uid):
    if not os.path.exists(constants.paths['UPLOAD_FOLDER']):
        os.makedirs(constants.paths['UPLOAD_FOLDER'])

    file = request.files['file']
    filename = secure_filename(file.filename)
    file.save(os.path.join(app.config['UPLOAD_FOLDER'], filename))

    content = {
        GROUPS: group_uid,
        UPVOTES: 0,
        VIEWS: 0,
    }

    post_id = group_content_collection.insert_one(content).inserted_id
    group_content_collection.update_one({'_id': post_id}, {'$set': {UID: str(post_id)}})
    group_collection.update_one({UID: group_uid}, {'$push': {CONTENT_UPLOADED: str(post_id)}})

    # Create the BlockBlockService that is used to call the Blob service for the storage account.
    block_blob_service = BlockBlobService(account_name=constants.blob_consts['ACCOUNT_NAME'],
                                          account_key=constants.blob_consts['BLOB_ACCOUNT_KEY'])
    block_blob_service.create_container(constants.blob_consts['CONTAINER_NAME'])
    block_blob_service.set_container_acl(constants.blob_consts['CONTAINER_NAME'], public_access=PublicAccess.Container)
    block_blob_service.create_blob_from_path(constants.blob_consts['CONTAINER_NAME'], str(post_id),
                                             os.path.join(constants.paths['UPLOAD_FOLDER'], filename))

    return jsonify(str(post_id))


@app.route('/get_group_content/<user_uid>/<group_uid>', methods=['GET'])
def get_group_content(user_uid, group_uid):
    return_list = []
    group_dict = group_collection.find_one({UID: group_uid})
    if group_dict is None:
        raise BadRequest('Invalid Group ID')
    user_dict = user_collection.find_one({UUID: user_uid})
    if user_dict is None:
        raise BadRequest('Invalid User ID')
    group_content = group_dict[CONTENT_UPLOADED]
    viewed_content = user_dict[VIEWED_CONTENT]
    for content in group_content:
        if content not in viewed_content:
            return_list.append(content)
    return jsonify(return_list)


@app.route('/update_group_content_views/<user_uid>/<content_uid>', methods=['PUT'])
def update_group_content_views(user_uid, content_uid):
    content_dict = group_content_collection.find_one({UID: content_uid})
    if ACTION in request.form and content_dict is not None:
        group_id = content_dict[GROUPS]
        user_collection.update_one({UUID: user_uid}, {'$push': {VIEWED_CONTENT: content_uid}})
        group_content_collection.update_one({UID: content_uid}, {'$inc': {VIEWS: 1}})
        content_dict[VIEWS] += 1
        if request.form[ACTION] == '1':
            group_content_collection.update_one({UID: content_uid}, {'$inc': {UPVOTES: 1}})
            content_dict[UPVOTES] += 1
        if (content_dict[UPVOTES]/content_dict[VIEWS]) > 0.5:
            group_collection.update_one({UID: group_id}, {'$push': {GALLERY: content_uid}})
        return "Content Updated!"
    raise BadRequest('Action Not Found!')


@app.route('/get_gallery/<group_id>', methods=['GET'])
def get_gallery(group_id):
    group_dict = group_collection.find_one({UID: group_id})
    if group_dict is None:
        raise BadRequest('Invalid Group ID')
    gallery = group_dict[GALLERY]
    return jsonify(gallery)


if __name__ == "__main__":
    app.run(host='192.168.43.25')
