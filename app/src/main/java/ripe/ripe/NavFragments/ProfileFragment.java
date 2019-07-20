package ripe.ripe.NavFragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
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

import com.bumptech.glide.Glide;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import ripe.ripe.APIUtils.Preference;
import ripe.ripe.APIUtils.RipeContent;
import ripe.ripe.APIUtils.RipeContentService;
import ripe.ripe.APIUtils.RipeUser;
import ripe.ripe.APIUtils.RipeUserService;
import ripe.ripe.R;
import ripe.ripe.Utils.GridImageAdapter;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private static final int NUM_GRID_COLUMNS = 3;

    //widgets
    private TextView mPosts, mScore, mUsername;
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

        mUsername = (TextView) view.findViewById(R.id.username);
        mPosts = (TextView) view.findViewById(R.id.posts);
        mScore = (TextView) view.findViewById(R.id.score_num);

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
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    private void setupGridView(){
        Log.d(TAG, "setupGridView: Setting up image grid.");

        String uuid = Preference.getSharedPreferenceString(getActivity().getApplicationContext(), "userId", "oops");

        RipeUserService userService = new RipeUserService();
        userService.getUserById(uuid, new RipeUserService.GetUserCallback() {
            @Override
            public void populateUser(final RipeUser ripeUsers) {
                mUsername.setText(ripeUsers.name);
                mPosts.setText(Integer.toString(ripeUsers.content_uploaded.length));
                mScore.setText(Integer.toString(ripeUsers.score));

                //setup our image grid
                int gridWidth = getResources().getDisplayMetrics().widthPixels;
                int imageWidth = gridWidth/NUM_GRID_COLUMNS;
                gridView.setColumnWidth(imageWidth);

                GridImageAdapter adapter = new GridImageAdapter(getContext(),ripeUsers.content_uploaded);
                gridView.setAdapter(adapter);
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        createCard(ripeUsers.content_uploaded[position]);
                        Log.e("IMAGE SELECTED", "Parent: " + parent + " View " + view + " Position " + position + " id " + id);
                    }
                });
            }
        });
    }

    public void createCard(final String url) {
        LayoutInflater inflate = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View containerView = inflate.inflate(R.layout.card_view, parentView, false);
        final RelativeLayout relativeLayoutContainer = containerView.findViewById(R.id.relative_container);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        containerView.setLayoutParams(layoutParams);

        final TextView titleTextView = containerView.findViewById(R.id.title);
        final ImageView imageView = containerView.findViewById(R.id.image_view);
        final VideoView videoView = containerView.findViewById(R.id.video_view);

        final ImageView likeIcon = containerView.findViewById(R.id.like_logo);
        final ImageView dislikeIcon = containerView.findViewById(R.id.dislike_logo);

        final ProgressBar progressBar = containerView.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        likeIcon.setImageAlpha(0);
        dislikeIcon.setImageAlpha(0);

        final RipeContentService service = new RipeContentService();

        service.getContentById(url, new RipeContentService.GetContentByIdCallback() {
            @Override
            public void populateContent(RipeContent content) {
                titleTextView.setText(content.title);
                if(content.is_video.equals("1")) {
                    videoView.setAlpha(1);
                    imageView.setAlpha(0);

                    File file = new File(getContext().getFilesDir(),"image.mp4");
                    relativeLayoutContainer.setBackgroundColor(getResources().getColor(R.color.black));
                    videoView.setVideoPath(RipeContentService.createBlobURL(url));
                    videoView.start();
                    videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            mediaPlayer.setLooping(true);
                            mediaPlayer.setVolume(0,0);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }
                else {
                    videoView.setAlpha(0);
                    imageView.setAlpha(255);

                    imageView.setBackgroundColor(getResources().getColor(R.color.black));
                    Glide.with(getContext()).load(RipeContentService.createBlobURL(url)).into(imageView);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });

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
                            Likes = 2;
                        } else if (x_cord <= screenCenter * 0.5) {
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
                        } else if (Likes == 2) {
                            // Liked content
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
