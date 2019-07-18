package Ripe;

import java.io.File;
import Ripe.RipeContent;
import Ripe.RipePoster;
import java.net.URL;

public class Main {

    private void uploadPic() throws Exception {

        // Getting file info
        File file = new File(getClass().getClassLoader().getResource("test.gif").getFile());

        RipeContent ripeContent = new RipeContent("title", "2", "ur mom", "Raymond", 10, 15, 1000, file);
        RipePoster ripePoster = new RipePoster(new URL("http://10.92.1.58:5000/"));
        ripePoster.uploadContent(ripeContent);

    }

    public static void main(String args[]) throws Exception {
        Main main = new Main();
        main.uploadPic();
    }
}
