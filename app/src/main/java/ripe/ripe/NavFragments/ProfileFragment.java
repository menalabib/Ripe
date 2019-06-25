package ripe.ripe.NavFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import ripe.ripe.R;
import ripe.ripe.GridImageAdapter;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        mPosts = (TextView) view.findViewById(R.id.videos_num);
        mFollowers = (TextView) view.findViewById(R.id.score_num);
        mFollowing = (TextView) view.findViewById(R.id.views_num);
        gridView = (GridView) view.findViewById(R.id.gridView);
        mContext = getActivity();

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

        GridImageAdapter adapter = new GridImageAdapter(this.getContext());
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
}
