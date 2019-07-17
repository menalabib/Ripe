import os
from server import constants
from azure.storage.blob import BlockBlobService, PublicAccess
from azure.storage.blob.baseblobservice import BaseBlobService
from flask import Flask, request, jsonify
from pymongo import MongoClient
from werkzeug.utils import secure_filename

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
SCORE = 'score'
ACTION = 'action'
NAME = 'name'
EMAIL = 'email'
CONTENT_UPLOADED = 'content_uploaded'
SAVED_CONTENT = 'saved_content'
VIEWED_CONTENT = 'viewed_content'
TOP_TEN = 'top_ten'
FULL_LEADERBOARD = 'full_leaderboard'

# db constants
client = MongoClient('mongodb://localhost:27017/', connect=False)
db = client['local']
content_collection = db['content']
user_collection = db['users']
leaderboard_collection = db['leaderboard']

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

    # Create the BlockBlockService that is used to call the Blob service for the storage account.
    block_blob_service = BlockBlobService(account_name=constants.blob_consts['ACCOUNT_NAME'],
                                          account_key=constants.blob_consts['BLOB_ACCOUNT_KEY'])

    # Create a container called 'usercontent'.
    block_blob_service.create_container(constants.blob_consts['CONTAINER_NAME'])

    # Set the permission so the blobs are public.
    block_blob_service.set_container_acl(constants.blob_consts['CONTAINER_NAME'], public_access=PublicAccess.Container)

    uuid = request.form[UID]

    # Upload the created file, use local_file_name for the blob name.
    block_blob_service.create_blob_from_path(constants.blob_consts['CONTAINER_NAME'], uuid,
                                             os.path.join(constants.paths['UPLOAD_FOLDER'], filename))

    content = {
        TITLE: request.form[TITLE],
        UID: request.form[UID],
        TAGS: request.form[TAGS],
        UPLOADED_BY: request.form[UPLOADED_BY],
        UPVOTES: request.form[UPVOTES],
        DOWNVOTES: request.form[DOWNVOTES],
        VIEWS: request.form[VIEWS],
    }

    object_id = content_collection.insert(content)

    # List the blobs in the container.
    print("\nList blobs in the container")
    generator = block_blob_service.list_blobs(constants.blob_consts['CONTAINER_NAME'])
    for blob in generator:
        print("\t Blob name: " + blob.name)

    return str(object_id)


@app.route('/create_user', methods=['POST'])
def create_user():
    user_id = request.form['id']
    if user_collection.find_one({'id': user_id}) is None:
        user = {
            UUID: request.form[UUID],
            NAME: request.form[NAME],
            EMAIL: request.form[EMAIL],
            CONTENT_UPLOADED: [],
            SCORE: 0,
            SAVED_CONTENT: [],
            VIEWED_CONTENT: [],
            TAGS: [],
        }
        post_id = user_collection.insert(user)

        print(post_id)
        return "User Added Successfully"
    return InvalidUsage('User Already Exists', status_code=410)


# get content for user
@app.route('/get_content_for_user/<user_uid>', methods=['GET'])
def get_content_for_user(user_uid):
    user_dict = user_collection.find_one({UID: user_uid})

    user_tags = user_dict['tags']
    user_content = []
    return_list = []

    for tag in user_tags:
        user_content += list(content_collection.find({TAGS: tag}))

    for cont in user_content:
        if str(cont.get('_id')) not in return_list:
            print(cont[TAGS])
            return_list.append(str(cont.get('_id')))

    return jsonify(return_list)


@app.route('/update_content_view/<user_uid>/<content_uid>', methods=['PUT'])
def update_content_views(user_uid, content_uid):
    if ACTION in request.form:
        content_collection.update_one({UID: content_uid}, {'$inc': {VIEWS: 1}})
        user_collection.update_one({UUID: user_uid}, {'$push': {VIEWED_CONTENT: content_uid}})
        if request.form[ACTION] == '0':
            content_collection.update_one({UID: content_uid}, {'$inc': {DOWNVOTES: 1}})
        elif request.form[ACTION] == '1':
            content_collection.update_one({UID: content_uid}, {'$inc': {UPVOTES: 1}})
            user_collection.update_one({CONTENT_UPLOADED: content_uid}, {'$inc': {SCORE: 1}})
            user_collection.update_one({UUID: user_uid}, {'$push': {SAVED_CONTENT: content_uid}})

        return "Content Updated!"

    return InvalidUsage('Cannot find action to update...', status_code=410)


@app.route('/get_leaderboard', methods=['GET'])
def get_leaderboard():
    leaderboard = leaderboard_collection.find_one()
    top_ten = leaderboard[TOP_TEN]
    full_leaderboard = leaderboard[FULL_LEADERBOARD]
    # TODO: TEST
    return jsonify({TOP_TEN: top_ten, FULL_LEADERBOARD: full_leaderboard})


# Interact with content
@app.route('/get_content_by_id/<content_uuid>', methods=['GET'])
def get_content(content_uuid):
    content = [cont for cont in content_collection if (cont['id'] == content_uuid)]

    # Create the BaaseBlockService that is used to call the Blob service for the storage account.
    block_blob_service = BaseBlobService(account_name='ripeblob', account_key=constants.blob_consts['BLOB_ACCOUNT_KEY'])

    if not os.path.exists(constants.paths['DOWNLOAD_FOLDER']):
        os.makedirs(constants.paths['DOWNLOAD_FOLDER'])

    uid = request.form[UID]
    block_blob_service.get_blob_to_path(constants.blob_consts['CONTAINER_NAME'], uid,
                                        constants.paths['DOWNLOAD_FOLDER'])


if __name__ == "__main__":
    app.run(host='192.168.43.25')
