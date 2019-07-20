package ripe.ripe.NavFragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import ripe.ripe.APIUtils.Preference;
import ripe.ripe.APIUtils.RipeContentService;
import ripe.ripe.APIUtils.RipeUser;
import ripe.ripe.R;
import ripe.ripe.Utils.UserDataModel;

public class FeedFragment extends Fragment {
    int windowWidth;
    int windowHeight;
    int screenCenter;
    int x_cord, y_cord, x, y;
    int Likes = 0;
    public RelativeLayout parentView;
    private Context context;
    ArrayList<UserDataModel> userDataModelArrayList;
    private OnFragmentInteractionListener mListener;

    private String uuid;

    public FeedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        context = this.getContext();
        parentView = view.findViewById(R.id.main_layout);

        // Set up screen size values
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        windowHeight = displaymetrics.heightPixels;
        windowWidth = displaymetrics.widthPixels;
        screenCenter = windowWidth / 2;

        userDataModelArrayList = new ArrayList<>();
//        getArrayData();

        uuid = Preference.getSharedPreferenceString(getActivity().getApplicationContext(), "userId", "oops");

        // Load first four cards
        for (int i = 0; i < 3; i++) {
            createCard();
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public void createCard() {
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
        service.getContentForUser(uuid, new RipeContentService.RipeCallback() {
            @Override
            public void setContent(String contentId, String title, boolean isVideo) {
                // Set card contents

                if (!contentId.equals("No Content Available")) {
                    titleTextView.setText(title);
                    relativeLayoutContainer.setTag(contentId);

                    // TODO: deal with videos
                    if (isVideo) {
                        videoView.setAlpha(1);
                        imageView.setAlpha(0);

                        File file = new File(getContext().getFilesDir(),"image.mp4");
                        relativeLayoutContainer.setBackgroundColor(getResources().getColor(R.color.black));
                        videoView.setVideoPath(RipeContentService.createBlobURL(contentId));
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
                        Glide.with(getContext()).load(RipeContentService.createBlobURL(contentId)).into(imageView);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
                else {
                    imageView.setAlpha(0);
                    videoView.setAlpha(0);
                    relativeLayoutContainer.setBackgroundColor(getResources().getColor(R.color.black));
                    titleTextView.setText("No content available, swipe back later");
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
                            service.updateContentViews(uuid, (String) v.getTag(), 0);
                            createCard();
                        } else if (Likes == 2) {
                            // Liked content
                            parentView.removeView(containerView);
                            service.updateContentViews(uuid, (String) v.getTag(), 1);
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
    }

    private static void downloadFile(String url, File outputFile) {
        try {
            URL u = new URL(url);
            URLConnection conn = u.openConnection();
            int contentLength = conn.getContentLength();

            DataInputStream stream = new DataInputStream(u.openStream());

            byte[] buffer = new byte[contentLength];
            stream.readFully(buffer);
            stream.close();

            DataOutputStream fos = new DataOutputStream(new FileOutputStream(outputFile));
            fos.write(buffer);
            fos.flush();
            fos.close();
        } catch(FileNotFoundException e) {
            return; // swallow a 404
        } catch (IOException e) {
            return; // swallow a 404
        }
    }
}
