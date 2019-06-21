package ripe.ripe;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class FeedActivity extends Activity {
    int ctr;
    int windowWidth;
    int windowHeight;
    int screenCenter;
    int x_cord, y_cord, x, y;
    int Likes = 0;
    public RelativeLayout parentView;
    private Context context;
    ArrayList<UserDataModel> userDataModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_feed);

        context = FeedActivity.this;
        parentView = findViewById(R.id.main_layout);

        // Set up window values
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        windowWidth = size.x;
        windowHeight = size.y;
        screenCenter = windowWidth / 2;

        userDataModelArrayList = new ArrayList<>();
        ctr = 0;
        getArrayData();

        // Load first four cards
        for (int i = 0; i < 4; i++) {
            createCard();
        }
    }

    public void createCard() {
        LayoutInflater inflate = (LayoutInflater) FeedActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View containerView = inflate.inflate(R.layout.card_view, parentView, false);
        RelativeLayout relativeLayoutContainer = containerView.findViewById(R.id.relative_container);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        containerView.setLayoutParams(layoutParams);

        TextView titleTextView = containerView.findViewById(R.id.title);
        ImageView imageView = containerView.findViewById(R.id.image_view);
        VideoView videoView = containerView.findViewById(R.id.video_view);

        final ImageView likeIcon = containerView.findViewById(R.id.like_logo);
        final ImageView dislikeIcon = containerView.findViewById(R.id.dislike_logo);
        likeIcon.setImageAlpha(0);
        dislikeIcon.setImageAlpha(0);

        // Set card contents
        titleTextView.setText(userDataModelArrayList.get(ctr).getTitle());

        // TODO: deal with videos
        if (userDataModelArrayList.get(ctr).isVideo) {
//            String uriPath = "file:///assets/boston.mp4";
//            Uri uri = Uri.parse(uriPath);
//            videoView.setVideoURI(uri);
//            videoView.requestFocus();
//            videoView.start();
        }
        else {
            videoView.setAlpha(0);
            imageView.setBackgroundColor(getResources().getColor(R.color.black));
            imageView.setImageBitmap(userDataModelArrayList.get(ctr).getPhoto());
        }

        // Touch listener on the image layout to swipe image right or left.
        //noinspection AndroidLintClickableViewAccessibility
        relativeLayoutContainer.setOnTouchListener(new View.OnTouchListener() {
            @SuppressWarnings("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                x_cord = (int) event.getRawX();
                y_cord = (int) event.getRawY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        x = (int) event.getX();
                        y = (int) event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        containerView.setX(event.getRawX() - (float) windowWidth/2);
                        containerView.setY(event.getRawY() - (float) windowHeight/2);
                        containerView.setRotation((float) ((x_cord - screenCenter) * (Math.PI / 128)));

                        if (x_cord >= screenCenter*1.5) {
                            likeIcon.setImageAlpha(255);
                            Likes = 2;
                        } else if (x_cord <= screenCenter*0.5) {
                            dislikeIcon.setImageAlpha(255);
                            Likes = 1;
                        } else {
                            Likes = 0;
                            dislikeIcon.setImageAlpha(0);
                            likeIcon.setImageAlpha(0);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (Likes == 0) {
                            Toast.makeText(context, "NOTHING", Toast.LENGTH_SHORT).show();
                            containerView.setX(0);
                            containerView.setY(0);
                            containerView.setRotation(0);
                        } else if (Likes == 1) {
                            Toast.makeText(context, "DISLIKED", Toast.LENGTH_SHORT).show();
                            parentView.removeView(containerView);
                            createCard();
                        } else if (Likes == 2) {
                            // TODO: get rid of toasts
                            Toast.makeText(context, "LIKED", Toast.LENGTH_SHORT).show();
                            parentView.removeView(containerView);
                            createCard();
                        }
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
        parentView.addView(containerView, 0);
        ctr = (ctr+1)%(userDataModelArrayList.size());
    }

    private void getArrayData() {
        Bitmap icon8 = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.login_logo);
        UserDataModel card8 = new UserDataModel();
        card8.setName("Ripe8");
        card8.setTotalLikes(100);
        card8.setPhoto(icon8);
        card8.setIsVideo(false);
        userDataModelArrayList.add(card8);

        Bitmap icon7 = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.login_logo);
        UserDataModel card7 = new UserDataModel();
        card7.setName("Ripe7");
        card7.setTotalLikes(100);
        card7.setPhoto(icon7);
        card7.setIsVideo(false);
        userDataModelArrayList.add(card7);

        Bitmap icon6 = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.login_logo);
        UserDataModel card6 = new UserDataModel();
        card6.setName("Ripe6");
        card6.setTotalLikes(100);
        card6.setPhoto(icon6);
        card6.setIsVideo(false);
        userDataModelArrayList.add(card6);

        Bitmap icon5 = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.login_logo);
        UserDataModel card5 = new UserDataModel();
        card5.setName("Ripe5");
        card5.setTotalLikes(100);
        card5.setPhoto(icon5);
        card5.setIsVideo(false);
        userDataModelArrayList.add(card5);

        Bitmap icon4 = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.login_logo);
        UserDataModel card4 = new UserDataModel();
        card4.setName("Ripe4");
        card4.setTotalLikes(100);
        card4.setPhoto(icon4);
        card4.setIsVideo(false);
        userDataModelArrayList.add(card4);

        Bitmap icon3 = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.login_logo);
        UserDataModel card3 = new UserDataModel();
        card3.setName("Ripe3");
        card3.setTotalLikes(100);
        card3.setPhoto(icon3);
        card3.setIsVideo(false);
        userDataModelArrayList.add(card3);

        Bitmap icon2 = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.login_logo);
        UserDataModel card2 = new UserDataModel();
        card2.setName("Ripe2");
        card2.setTotalLikes(100);
        card2.setPhoto(icon2);
        card2.setIsVideo(false);
        userDataModelArrayList.add(card2);

        Bitmap icon1 = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.login_logo);
        UserDataModel card1 = new UserDataModel();
        card1.setName("Ripe1");
        card1.setTotalLikes(100);
        card1.setPhoto(icon1);
        card1.setIsVideo(false);
        userDataModelArrayList.add(card1);
    }
}