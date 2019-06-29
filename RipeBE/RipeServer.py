import os
from flask import Flask, flash, request, redirect, url_for, jsonify
from werkzeug.utils import secure_filename
from azure.storage.blob import BlockBlobService, PublicAccess
from pymongo import MongoClient
from InvalidUsage import InvalidUsage

UPLOAD_FOLDER = './uploaded_pics'
client = MongoClient('mongodb://localhost:27017/')
db = client.local


@app.errorhandler(InvalidUsage)
def handle_invalid_usage(error):
    response = jsonify(error.to_dict())
    response.status_code = error.status_code
    return response


app = Flask(__name__)
app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER
BLOB_ACCOUNT_KEY = 'ZbG1DUXzpTAJfZJM2s3TlifmUEI/gj/pw5acLv0Ht0uqniOVYYB41r0tAulZB53+NtXDCUruUFplXtfdqQE30w=='


@app.route('/post_content', methods=['POST'])
def post_content():
    if not os.path.exists(UPLOAD_FOLDER):
        os.makedirs(UPLOAD_FOLDER)

    file = request.files['file']
    filename = secure_filename(file.filename)
    file.save(os.path.join(app.config['UPLOAD_FOLDER'], filename))

    # Create the BlockBlockService that is used to call the Blob service for the storage account.
    block_blob_service = BlockBlobService(account_name='ripeblob', account_key=BLOB_ACCOUNT_KEY)

    # Create a container called 'quickstartblobs'.
    container_name = 'usercontent'
    block_blob_service.create_container(container_name)

    # Set the permission so the blobs are public.
    block_blob_service.set_container_acl(container_name, public_access=PublicAccess.Container)

    # Upload the created file, use local_file_name for the blob name.
    block_blob_service.create_blob_from_path(container_name, request.json['uid'], os.path.join(UPLOAD_FOLDER, filename))

    content = {
        'title': request.json['title'],
        'uid': request.json['uid'],
        'tags': request.json['tags'],
        'uploaded_by': request.json['uploaded_by'],
        'upvotes': request.json['upvotes'],
        'downvotes': request.json['downvotes'],
        'views': request.json['views'],
    }

    post_id = db.insert_one(content)

    # List the blobs in the container.
    print("\nList blobs in the container")
    generator = block_blob_service.list_blobs(container_name)
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


@app.route('/update_content_view/<content_uid>', methods=['PUT'])
def update_content_view(content_uid):
    content = [cont for cont in db if (cont['id'] == content_uid)]
    if 'action' in request.json:
        content['views'] += 1
        if request.json['action'] == 0:
            content['downvotes'] += 1
        elif request.json['action'] == 1:
            content['upvotes'] += 1

        return "Content Updated!"

    return InvalidUsage('Cannot find action to update...', status_code=410)
