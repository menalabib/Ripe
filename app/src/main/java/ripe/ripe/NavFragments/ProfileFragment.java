package ripe.ripe.NavFragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import ripe.ripe.R;
import ripe.ripe.Utils.GridImageAdapter;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private static final int NUM_GRID_COLUMNS = 3;

    //widgets
    private TextView mPosts, mFollowers, mFollowing, mDescription;
    private ImageView mProfilePhoto;
    private GridView gridView;
    private Context mContext;

    //vars
    private int mFollowersCount = 0;
    private int mFollowingCount = 0;
    private int mPostsCount = 0;

    //cardView vars
    public RelativeLayout parentView;
    int windowWidth;
    int windowHeight;
    int screenCenter;
    int x_cord, y_cord, x, y;
    int Likes = 0;
    Context context;
    ProgressBar mProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mPosts = (TextView) view.findViewById(R.id.videos_num);
        mFollowers = (TextView) view.findViewById(R.id.score_num);
        mFollowing = (TextView) view.findViewById(R.id.views_num);
        gridView = (GridView) view.findViewById(R.id.gridView);
        mContext = getActivity();

        // Set up screen size values
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        windowHeight = displaymetrics.heightPixels;
        windowWidth = displaymetrics.widthPixels;
        screenCenter = windowWidth / 2;
        context = getContext();
        parentView = view.findViewById(R.id.relLayout2);

        setupGridView();
        getFollowersCount();
        getFollowingCount();
        getPostsCount();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private void setupGridView(){
        Log.d(TAG, "setupGridView: Setting up image grid.");

        //setup our image grid
        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth/NUM_GRID_COLUMNS;
        gridView.setColumnWidth(imageWidth);

        GridImageAdapter adapter = new GridImageAdapter(this.getContext(), thumbIds);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                createCard(getResources().getDrawable(thumbIds[position]));
                Log.e("IMAGE SELECTED", "Parent: " + parent + " View " + view + " Position " + position + " id " + id);
            }
        });
    }

    private void getFollowersCount(){
        mFollowersCount = 45;
    }

    private void getFollowingCount(){
        mFollowingCount = 50;
    }

    private void getPostsCount(){
        mPostsCount = 20;
    }

    private int[] thumbIds = {
            R.drawable.leader_1_trav,
            R.drawable.leader_2_ye,
            R.drawable.leader_3_kenny,
            R.drawable.leader_4_drake,
            R.drawable.leader_5_rocky,
            R.drawable.leader_6_rashad,
            R.drawable.leader_7_skepta,
            R.drawable.like_logo,
            R.drawable.dislike_logo,
            R.drawable.apple,
            R.drawable.yeet,
            R.drawable.fast,
            R.drawable.help
    };

    public void createCard(Drawable selectedImage) {
        LayoutInflater inflate = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View containerView = inflate.inflate(R.layout.card_view, parentView, false);
        RelativeLayout relativeLayoutContainer = containerView.findViewById(R.id.relative_container);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        containerView.setLayoutParams(layoutParams);

        TextView titleTextView = containerView.findViewById(R.id.title);
        ImageView imageView = containerView.findViewById(R.id.image_view);
        VideoView videoView = containerView.findViewById(R.id.video_view);
        mProgressBar = containerView.findViewById(R.id.progressBar);

        final ImageView likeIcon = containerView.findViewById(R.id.like_logo);
        final ImageView dislikeIcon = containerView.findViewById(R.id.dislike_logo);
        likeIcon.setImageAlpha(0);
        dislikeIcon.setImageAlpha(0);

        // Set card contents
        //TODO: fix this string
        titleTextView.setText("String");

        videoView.setAlpha(0);
        imageView.setBackgroundColor(getResources().getColor(R.color.black));
        imageView.setImageDrawable(selectedImage);

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
                        dislikeIcon.setImageAlpha(0);
                        likeIcon.setImageAlpha(0);
                        if (x_cord >= screenCenter*1.5) {
                            Likes = 2;
                        } else if (x_cord <= screenCenter*0.5) {
                            Likes = 1;
                        } else {
                            Likes = 0;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (Likes == 0) {
                            containerView.setX(0);
                            containerView.setY(0);
                            containerView.setRotation(0);
                        } else if (Likes == 1) {
                            parentView.removeView(containerView);
                        } else if (Likes == 2) {
                            parentView.removeView(containerView);
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
