package ripe.ripe.NavFragments.UploadFlow;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import ripe.ripe.R;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PhotoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PhotoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhotoFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    Button launchCamera;
    Button captureVideo;
    private View inflatedView;

    public PhotoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PhotoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PhotoFragment newInstance(String param1, String param2) {
        PhotoFragment fragment = new PhotoFragment();
        return fragment;
    }

    private int CAMERA_REQUEST_CODE = 5;
    private int VIDEO_REQUEST_CODE = 15;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflatedView = inflater.inflate(R.layout.fragment_photo, container, false);
        launchCamera = (Button) inflatedView.findViewById(R.id.btnLaunchCamera);
        launchCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((ShareFragment)getParentFragment()).getCurrentTabNumber() == 1) {
                    if (((ShareFragment)getParentFragment()).checkPermissions(Manifest.permission.CAMERA)) {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
                    }
                    else {
                        Intent inte = new Intent(getActivity(), ShareFragment.class);
                        inte.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(inte);
                    }
                }
            }
        });

        captureVideo = inflatedView.findViewById(R.id.btnLaunchVideo);
        captureVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((ShareFragment)getParentFragment()).getCurrentTabNumber() == 1) {
                    if (((ShareFragment)getParentFragment()).checkPermissions(Manifest.permission.CAMERA)) {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        cameraIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
                        startActivityForResult(cameraIntent, VIDEO_REQUEST_CODE);
                    }
                    else {
                        Intent inte = new Intent(getActivity(), ShareFragment.class);
                        inte.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(inte);
                    }
                }
            }
        });
        return inflatedView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE) {

            Bitmap bitmap = null;
            try {
                if (data.getExtras().get("data") != null) {
                    bitmap = (Bitmap) data.getExtras().get("data");
                    Intent intent = new Intent(getActivity(), TitleActivity.class);
                    intent.putExtra(getString(R.string.selected_bitmap), bitmap);
                    startActivity(intent);
                }
            }
            catch (NullPointerException e) { }
        }
        else if (requestCode == VIDEO_REQUEST_CODE) {

            Uri videoUri = null;
            try {
                if (data.getData() != null) {
                    videoUri = data.getData();
                    if (resultCode == RESULT_OK) {
                        Intent intent = new Intent(getActivity(), TitleActivity.class);
                        intent.putExtra(getString(R.string.selected_video), videoUri);
                        startActivity(intent);
                    }
                }
            }
            catch (NullPointerException e) { }
        }
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
