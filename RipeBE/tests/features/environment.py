from src.server import app


def before_all(context):
    context.app = app.test_client()
    # mock_azure_blob = Mock()
    # mock_azure_blob.create_container.return_value = True
    # mock_azure_blob.set_container_acl.return_value = True
    # mock_azure_blob.create_blob_from_path.return_value = True
    # mock_azure_blob.list_blobs.return_value = []
    #
    # patcher = patch('server.RipeServer.BlockBlobService', mock_azure_blob)
    # patcher.start()
