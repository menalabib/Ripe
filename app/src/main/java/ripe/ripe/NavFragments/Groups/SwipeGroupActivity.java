package ripe.ripe.NavFragments.Groups;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.ArrayList;

import ripe.ripe.R;
import ripe.ripe.Utils.UserDataModel;

public class SwipeGroupActivity extends AppCompatActivity {

    public RelativeLayout parentView;
    ArrayList<UserDataModel> userDataModelArrayList;
    int ctr;
    int swipedOn;
    int windowWidth;
    int windowHeight;
    int screenCenter;
    int x_cord, y_cord, x, y;
    int Likes = 0;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try { this.getSupportActionBar().hide(); }
        catch (NullPointerException e){}

        setContentView(R.layout.activity_swipe_group);

        parentView = findViewById(R.id.relLayout1);
        userDataModelArrayList = new ArrayList<>();

        // Set up screen size values
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        windowHeight = displaymetrics.heightPixels;
        windowWidth = displaymetrics.widthPixels;
        screenCenter = windowWidth / 2;
        ctr = 0;
        swipedOn = 0;
        context = this;

        getArrayData();
        for (int i = 0; i < 3; i++) {
            createCard();
        }
    }

    public void createCard() {
        LayoutInflater inflate = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View containerView = inflate.inflate(R.layout.card_view, parentView, false);
        RelativeLayout relativeLayoutContainer = containerView.findViewById(R.id.relative_container);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
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
        if (userDataModelArrayList.get(ctr).getIsVideo()) {

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
                        containerView.setX(x_cord - (float) windowWidth/2);
                        containerView.setY(y_cord - (float) windowHeight/2);
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
                            containerView.setX(0);
                            containerView.setY(0);
                            containerView.setRotation(0);
                        } else if (Likes == 1) {
                            parentView.removeView(containerView);
                            swipedOn++;
                        } else if (Likes == 2) {
                            parentView.removeView(containerView);
                            swipedOn++;
                        }
                        // TODO: switch num
                        if(swipedOn == 3) {
                            Intent intent = new Intent(SwipeGroupActivity.this, GroupGalleryActivity.class);
                            startActivity(intent);
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
        Bitmap icon9 = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.yeet);
        UserDataModel card9 = new UserDataModel();
        card9.setName("YEEEEET");
        card9.setTotalLikes(100);
        card9.setPhoto(icon9);
        card9.setIsVideo(false);
        userDataModelArrayList.add(card9);

        Bitmap icon8 = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.ta);
        UserDataModel card8 = new UserDataModel();
        card8.setName("TA");
        card8.setTotalLikes(100);
        card8.setPhoto(icon8);
        card8.setIsVideo(false);
        userDataModelArrayList.add(card8);

        Bitmap icon7 = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.oil);
        UserDataModel card7 = new UserDataModel();
        card7.setName("Drumpf");
        card7.setTotalLikes(100);
        card7.setPhoto(icon7);
        card7.setIsVideo(false);
        userDataModelArrayList.add(card7);

        Bitmap icon6 = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.home);
        UserDataModel card6 = new UserDataModel();
        card6.setName("Water Water Water");
        card6.setTotalLikes(100);
        card6.setPhoto(icon6);
        card6.setIsVideo(false);
        userDataModelArrayList.add(card6);

        Bitmap icon5 = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.help);
        UserDataModel card5 = new UserDataModel();
        card5.setName("Ouch");
        card5.setTotalLikes(100);
        card5.setPhoto(icon5);
        card5.setIsVideo(false);
        userDataModelArrayList.add(card5);

        Bitmap icon4 = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.fast);
        UserDataModel card4 = new UserDataModel();
        card4.setName("Speedy");
        card4.setTotalLikes(100);
        card4.setPhoto(icon4);
        card4.setIsVideo(false);
        userDataModelArrayList.add(card4);

        Bitmap icon3 = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.exam);
        UserDataModel card3 = new UserDataModel();
        card3.setName("Finals");
        card3.setTotalLikes(100);
        card3.setPhoto(icon3);
        card3.setIsVideo(false);
        userDataModelArrayList.add(card3);

        Bitmap icon2 = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.coop);
        UserDataModel card2 = new UserDataModel();
        card2.setName("Hire me pls");
        card2.setTotalLikes(100);
        card2.setPhoto(icon2);
        card2.setIsVideo(false);
        userDataModelArrayList.add(card2);

        Bitmap icon1 = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.apple);
        UserDataModel card1 = new UserDataModel();
        card1.setName("Apple");
        card1.setTotalLikes(100);
        card1.setPhoto(icon1);
        card1.setIsVideo(false);
        userDataModelArrayList.add(card1);
    }

}
