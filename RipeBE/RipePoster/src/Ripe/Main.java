package Ripe;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;

public class Main {

    private void uploadPic() throws InvalidKeyException, MalformedURLException, IOException {
        // upload pic
//        File file = new File(getClass().getClassLoader().getResource("test.gif").getFile());
//        RipeContent ripeContent = new RipeContent("title", "2", "ur mom", "Raymond", 10, 15, 1000, file);
        RipeContentService ripeContentService = new RipeContentService(new URL("http://10.92.1.68:5000/"));
        //upvote
        //ripeContentService.updateContentViews("user12345", "content123", 1);
        //downvote
//        ripeContentService.updateContentViews("user12345", "content123", 0);
//        ripeContentService.uploadContent(ripeContent);

        // get content from user id
//        RipeContentService ripeContentService = new RipeContentService(new URL("http://10.92.1.68:5000/"));
//        RipeUser ru = new RipeUser(null, null, null, 0, null , null, "user12345");
//        ripeContentService.getContentForUser(ru);

        // get leaderboard
//        RipeUserService ripeUserService = new RipeUserService(new URL("http://10.92.1.68:5000/"));
//        ripeUserService.getLeaderboard();

        // user interactions
//        RipeUserService ripeUserService = new RipeUserService(new URL("http://10.92.1.58:5000/"));
//        RipeUserService ripeUserService = new RipeUserService(new URL("http://10.92.1.68:5000/"));
//        RipeUser ripeUser = new RipeUser("RayRay", "ray@gmail.com", null, null, null, null, "");
//        ripeUserService.createUser(ripeUser);
        //System.out.println(ripeUser.uuid);

        // get user by id
//        RipeUserService ripeUserService = new RipeUserService(new URL("http://10.92.1.68:5000/"));
//        ripeUserService.getUserById("user12345");

    }


    public static void main(String args[]) throws Exception {
        Main main = new Main();
        main.uploadPic();
    }

}
