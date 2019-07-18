package ripe.ripe.NavFragments.UploadFlow;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
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

import java.util.ArrayList;

import ripe.ripe.NavActivity;
import ripe.ripe.R;
import ripe.ripe.Utils.FilePaths;
import ripe.ripe.Utils.FileSearch;
import ripe.ripe.Utils.GridImageAdap;

public class GroupGalleryActivity extends AppCompatActivity {

    ImageView backArrowIV;
    GridView gridView;
    private ArrayList<String> directories;
    private String selectedImage;

    public RelativeLayout parentView;
    int windowWidth;
    int windowHeight;
    int screenCenter;
    int x_cord, y_cord, x, y;
    int Likes = 0;
    Context context;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try { this.getSupportActionBar().hide(); }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_group_gallery);

        backArrowIV = findViewById(R.id.ivBackArrow);
        gridView = findViewById(R.id.gridView);

        backArrowIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(GroupGalleryActivity.this, NavActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });


        // Set up screen size values
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        windowHeight = displaymetrics.heightPixels;
        windowWidth = displaymetrics.widthPixels;
        screenCenter = windowWidth / 2;
        context = this;
        parentView = findViewById(R.id.relLayout2);

        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent i = new Intent(GroupGalleryActivity.this, NavActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }

    private void init() {
        FilePaths filePaths = new FilePaths();

        if(FileSearch.getDirectoryPaths(filePaths.PICTURES) != null) {
            directories = FileSearch.getDirectoryPaths(filePaths.PICTURES);
        }
        directories.add(filePaths.CAMERA);

        final ArrayList<String> directoryNames = new ArrayList<>();
        for (String dir : directories) {
            directoryNames.add(dir.substring(dir.lastIndexOf("/") + 1));
        }

        setupGridView(directories.get(0));
    }

    private void setupGridView(String selectedDirectory) {
        final ArrayList<String> imgURLS = FileSearch.getFilePaths(selectedDirectory);
        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth/3;
        gridView.setColumnWidth(imageWidth);

        GridImageAdap gridImageAdapter = new GridImageAdap(this, R.layout.layout_grid_imageview, "file:/", imgURLS);
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
        if (imgURLS.size() != 0) {
            gridView.setAdapter(gridImageAdapter);
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedImage = imgURLS.get(i);
                createCard(selectedImage);
            }
        });
    }

    public void createCard(String selectedImage) {
        LayoutInflater inflate = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        titleTextView.setText(selectedImage);

        videoView.setAlpha(0);
        imageView.setBackgroundColor(getResources().getColor(R.color.black));
        setImage(selectedImage,imageView,"file:/");

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

    private void setImage(String imgURL, ImageView imageView, String append) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));

        imageLoader.displayImage(append + imgURL, imageView, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                mProgressBar.setVisibility(View.INVISIBLE);
            }
        });
    }
}
