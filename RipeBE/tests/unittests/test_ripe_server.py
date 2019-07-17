import ast

from server.RipeServer import app


class TestRipeServer:
    def __init__(self):
        self.app = app.test_client()

    def test_get_content_for_user(self):
        response = self.app.get('/get_content_for_user/user12345')
        content = response.data.decode("utf-8")
        content_list = ast.literal_eval(content)
        print(content_list)
        assert response.status_code is 200

    def test_update_content_view(self):
        response = self.app.put('/update_content_view/user6789/content123', data={'action': 1})
        print(response)
        assert response.status_code is 200

    def test_get_user_info(self):
        response = self.app.get('/get_user_by_id/user12345')
        user = response.data.decode("utf-8")
        print(user)
        assert response.status_code is 200

    def test_get_leaderboard(self):
        response = self.app.get('/get_leaderboard')
        leaderboard = response.data.decode('utf-8')
        print(leaderboard)
        assert response.status_code is 200
