package Ripe;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

public class Main {

    private void uploadPic() throws Exception {
        String host = "http://10.32.12.22:5000";
        // upload pic
        ArrayList<String> tags = new ArrayList<>();
        tags.add("funny");
        tags.add("joke");
        File file = new File(getClass().getClassLoader().getResource("test.gif").getFile());
        RipeContent ripeContent = new RipeContent("title", "2", tags, "Raymond", 10, 15, 1000, file);
        RipeContentService ripeContentService = new RipeContentService(new URL(host));
        //upvote
        //ripeContentService.updateContentViews("user12345", "content123", 1);
        //downvote
//        ripeContentService.updateContentViews("user12345", "content123", 0);
        //ripeContentService.uploadContent(ripeContent);

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

        // GROUPS

        RipeGroupService ripeGroupService = new RipeGroupService(new URL(host));
//        ripeGroupService.joinGroup("user6789","7daad");
        //ripeGroupService.joinGroup();
        //ripeGroupService.postToGroup("7daad", file);
        //ripeGroupService.getGroupContent("user12345", "7daad");
        //ripeGroupService.updateGroupContentView("user12345", "content456", 1 );
        ripeGroupService.getGallery("7daad");

    }


    public static void main(String args[]) throws Exception {
        Main main = new Main();
        main.uploadPic();
    }

}
