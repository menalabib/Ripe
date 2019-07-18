package Ripe;

import java.io.File;
import java.net.URL;

public class Main {

    private void uploadPic() throws Exception {

        // upload pic
//        File file = new File(getClass().getClassLoader().getResource("test.gif").getFile());
//        RipeContent ripeContent = new RipeContent("title", "2", "ur mom", "Raymond", 10, 15, 1000, file);
//        RipeContentService ripeContentService = new RipeContentService(new URL("http://10.92.1.58:5000/"));
//        ripeContentService.uploadContent(ripeContent);

        // get content from user id

        RipeContentService ripeContentService = new RipeContentService(new URL("http://10.92.1.68:5000/"));
        RipeUser ru = new RipeUser(null, null, null, 0, null , null, "user12345");
        ripeContentService.getContentForUser(ru);

        // get leaderboard
//        RipeLeaderboardService ripeLeaderboardService = new RipeLeaderboardService(new URL("http://10.92.1.58:5000/"));
//        ripeLeaderboardService.getLeaderboard();

        // user interactions
//        RipeUserService ripeUserService = new RipeUserService(new URL("http://10.92.1.58:5000/"));
//        RipeUserService ripeUserService = new RipeUserService(new URL("http://10.92.1.68:5000/"));
//        RipeUser ripeUser = new RipeUser("RayRay", "ray@gmail.com", null, null, null, null, "");
//        ripeUserService.createUser(ripeUser);
        //System.out.println(ripeUser.uuid);

    }

    public static void main(String args[]) throws Exception {
        Main main = new Main();
        main.uploadPic();
    }
}
