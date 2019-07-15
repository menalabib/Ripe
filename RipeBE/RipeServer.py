import os
from flask import Flask, flash, request, redirect, url_for, jsonify
from werkzeug.utils import secure_filename
from azure.storage.blob import BlockBlobService, PublicAccess
from pymongo import MongoClient
from InvalidUsage import InvalidUsage

# local constants
UPLOAD_FOLDER = './uploaded_pics'
DOWNLOAD_FOLDER = './downloaded_pics'

# db constants
client = MongoClient('mongodb://localhost:27017/')
db = client.local

# flask configurations
app = Flask(__name__)
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER

# blob constants
BLOB_ACCOUNT_KEY = 'ZbG1DUXzpTAJfZJM2s3TlifmUEI/gj/pw5acLv0Ht0uqniOVYYB41r0tAulZB53+NtXDCUruUFplXtfdqQE30w=='
CONTAINER_NAME = 'usercontent'
ACCOUNT_NAME = 'ripeblob2'

# content constants
TITLE = 'title'
UUID = 'uid'
TAGS = 'tags'
UPLOADED_BY = 'uploaded_by'
UPVOTES = 'upvotes'
DOWNVOTES = 'downvotes'
VIEWS = 'views'

# user constants

@app.errorhandler(InvalidUsage)
def handle_invalid_usage(error):
    response = jsonify(error.to_dict())
    response.status_code = error.status_code
    return response

@app.route('/post_content', methods=['POST'])
def post_content():
    if not os.path.exists(UPLOAD_FOLDER):
        os.makedirs(UPLOAD_FOLDER)

    file = request.files['file']
    filename = secure_filename(file.filename)
    file.save(os.path.join(app.config['UPLOAD_FOLDER'], filename))

    # Create the BlockBlockService that is used to call the Blob service for the storage account.
    block_blob_service = BlockBlobService(account_name=ACCOUNT_NAME, account_key=BLOB_ACCOUNT_KEY)

    # Create a container called 'usercontent'.
   
    block_blob_service.create_container(CONTAINER_NAME)

    # Set the permission so the blobs are public.
    block_blob_service.set_container_acl(CONTAINER_NAME, public_access=PublicAccess.Container)

    # Upload the created file, use local_file_name for the blob name.
    block_blob_service.create_blob_from_path(CONTAINER_NAME, request.json[UUID], os.path.join(UPLOAD_FOLDER, filename))

    content = {
        TITLE: request.body[TITLE],
        UUID: request.body[UUID],
        TAGS: request.body[TAGS],
        UPLOADED_BY: request.body[UPLOADED_BY],
        UPVOTES: request.body[UPVOTES],
        DOWNVOTES: request.body[DOWNVOTES],
        VIEWS: request.body[VIEWS],
    }

    post_id = db.insert_one(content)

    # List the blobs in the container.
    print("\nList blobs in the container")
    generator = block_blob_service.list_blobs(CONTAINER_NAME)
    for blob in generator:
        print("\t Blob name: " + blob.name)

    return post_id


@app.route('/create_user/', methods=['POST'])
def create_user():
    if db.find_one({'id': request.json['id']}) is not None:
        user = {
            'id': request.json['id'],
            'name': request.json['name'],
            'email': request.json['email'],
            'videos_uploaded': [],
            'score': 0,
            'saved_videos': [],
        }
        return db.insert_one(user)
    return InvalidUsage('User Already Exists', status_code=410)

## Interact with content
@app.route('/get_content/<content_uuid>', methods=['GET'])
def get_content(content_uuid):
    content = [cont for cont in db if (cont['id'] == content_uuid)]

    # Create the BaaseBlockService that is used to call the Blob service for the storage account.
    block_blob_service = BaseBlobService(account_name='ripeblob', account_key=BLOB_ACCOUNT_KEY)

    if not os.path.exists(DOWNLOAD_FOLDER):
        os.makedirs(DOWNLOAD_FOLDER)

    block_blob_service.get_blob_to_path(CONTAINER_NAME, request.json[UUID], DOWNLOAD_FOLDER)
    
    

@app.route('/update_content_view/<content_uid>', methods=['PUT'])
def update_content_views(content_uid):
    content = [cont for cont in db if (cont['id'] == content_uid)]
    if 'action' in request.json:
        content[VIEWS] += 1
        if request.json['action'] == 0:
            content[DOWNVOTES] += 1
        elif request.json['action'] == 1:
            content[UPVOTES] += 1

        return "Content Updated!"

    return InvalidUsage('Cannot find action to update...', status_code=410)
