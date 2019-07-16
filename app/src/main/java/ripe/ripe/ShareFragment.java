package ripe.ripe;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
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
import ripe.ripe.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShareFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShareFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
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

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShareFragment.
     */
    // TODO: Rename and change types and number of parameters
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
//        setupViewPager();

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
        setupViewPager();
    }
}
