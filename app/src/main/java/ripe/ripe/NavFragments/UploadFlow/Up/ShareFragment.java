package ripe.ripe.NavFragments.UploadFlow.Up;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ripe.ripe.Utils.Permissions;
import ripe.ripe.R;
import ripe.ripe.Utils.SectionsPagerAdapter;

public class ShareFragment extends Fragment {

    private static final int VERIFY_PERMISSIONS_REQUEST = 1;
    private OnFragmentInteractionListener mListener;

    private ViewPager mViewPager;

    public int getCurrentTabNumber() {
        return mViewPager.getCurrentItem();
    }

    private void setupViewPager() {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new GalleryFragment());
        adapter.addFragment(new PhotoFragment());
        mViewPager = (ViewPager) getView().findViewById(R.id.container);
        mViewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) getView().findViewById(R.id.tabsBottom);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setText("Gallery");
        tabLayout.getTabAt(1).setText("Camera");
    }

    public ShareFragment() {
        // Required empty public constructor
    }

    public static ShareFragment newInstance(String param1, String param2) {
        ShareFragment fragment = new ShareFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_share, container, false);
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

    public boolean checkPermissionsArray(String[] permissions) {
        for (String perm : permissions) {
            if (!checkPermissions(perm)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (checkPermissionsArray(Permissions.permissions)) {
            setupViewPager();
        }
        else {
            verifyPermissions(Permissions.permissions);
        }
    }

    public boolean checkPermissions(String perm) {
        int permissionRequest = ActivityCompat.checkSelfPermission(getContext(), perm);
        if (permissionRequest != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

    public void verifyPermissions(String[] permissions) {
        getActivity().requestPermissions(permissions, VERIFY_PERMISSIONS_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (checkPermissionsArray(Permissions.permissions)) {
            setupViewPager();
        } else {
            verifyPermissions(Permissions.permissions);
        }
    }
}
