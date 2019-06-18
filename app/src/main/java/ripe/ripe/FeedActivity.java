package ripe.ripe;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Collections;

public class FeedActivity extends Activity {
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
        titleTextView.setText(userDataModelArrayList.get(0).getTitle());
        imageView.setImageBitmap(userDataModelArrayList.get(0).getPhoto());
        // TODO: deal with videos
//        videoView.setVideoURI("");

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
        parentView.addView(containerView);
    }

    private void getArrayData() {
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.login_logo);
        UserDataModel model9 = new UserDataModel();
        model9.setName("Ripe");
        model9.setTotalLikes(100);
        model9.setPhoto(icon);
        userDataModelArrayList.add(model9);
        Collections.reverse(userDataModelArrayList);
    }
}