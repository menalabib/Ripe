package ripe.ripe.NavFragments.Groups;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import ripe.ripe.APIUtils.Preference;
import ripe.ripe.APIUtils.RipeContentService;
import ripe.ripe.APIUtils.RipeGroupService;
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
    int contentCtr;
    Context context;

    String groupId;

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

        groupId = getIntent().getExtras().get("group_id").toString();

        final String uuid = Preference.getSharedPreferenceString(SwipeGroupActivity.this.getApplicationContext(), "userId", "oops");

        RipeGroupService groupService = new RipeGroupService();
        groupService.getGroupContent(uuid, groupId, new RipeGroupService.GetGroupContentCallback() {
            @Override
            public void setUpGroupContent(List<String> groupContent) {
                if(groupContent.size() > 0) {
                    contentCtr = groupContent.size();
                    for (String id : groupContent) {
                        createCard(id);
                    }
                }
                else {
                    Intent intent = new Intent(SwipeGroupActivity.this, GroupGalleryActivity.class);
                    intent.putExtra("group_id", groupId);
                    startActivity(intent);
                }
            }
        });
    }



    public void createCard(final String id) {
        LayoutInflater inflate = (LayoutInflater) SwipeGroupActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View containerView = inflate.inflate(R.layout.card_view, parentView, false);
        final RelativeLayout relativeLayoutContainer = containerView.findViewById(R.id.relative_container);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        containerView.setLayoutParams(layoutParams);

        LinearLayout titleView = containerView.findViewById(R.id.title_view);
        titleView.setAlpha(0);
        final ImageView imageView = containerView.findViewById(R.id.image_view);
        final VideoView videoView = containerView.findViewById(R.id.video_view);

        final ImageView likeIcon = containerView.findViewById(R.id.like_logo);
        final ImageView dislikeIcon = containerView.findViewById(R.id.dislike_logo);

        final ProgressBar progressBar = containerView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        likeIcon.setImageAlpha(0);
        dislikeIcon.setImageAlpha(0);

        final String uuid = Preference.getSharedPreferenceString(SwipeGroupActivity.this.getApplicationContext(), "userId", "oops");
        final RipeGroupService groupService = new RipeGroupService();

        videoView.setAlpha(0);
        imageView.setAlpha(255);

        imageView.setBackgroundColor(getResources().getColor(R.color.black));
        Glide.with(SwipeGroupActivity.this).load(RipeContentService.createBlobURL(id)).into(imageView);
        progressBar.setVisibility(View.INVISIBLE);

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
                        containerView.setX(x_cord - (float) windowWidth / 2);
                        containerView.setY(y_cord - (float) windowHeight / 2);
                        containerView.setRotation((float) ((x_cord - screenCenter) * (Math.PI / 128)));

                        if (x_cord >= screenCenter * 1.5) {
                            likeIcon.setImageAlpha(255);
                            Likes = 2;
                        } else if (x_cord <= screenCenter * 0.5) {
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
                            // Disliked content
                            parentView.removeView(containerView);
                            groupService.updateGroupContentView(uuid, id, 0);
                            ctr++;
                        } else if (Likes == 2) {
                            // Liked content
                            parentView.removeView(containerView);
                            groupService.updateGroupContentView(uuid, id, 1);
                            ctr++;
                        }
                        if (ctr == contentCtr) {
                            Intent intent = new Intent(SwipeGroupActivity.this, GroupGalleryActivity.class);
                            intent.putExtra("group_id", groupId);
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
    }
}
