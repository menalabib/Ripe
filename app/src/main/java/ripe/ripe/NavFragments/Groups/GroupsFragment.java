package ripe.ripe.NavFragments.Groups;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import ripe.ripe.APIUtils.Preference;
import ripe.ripe.APIUtils.RipeContent;
import ripe.ripe.APIUtils.RipeGroupService;
import ripe.ripe.APIUtils.RipeUser;
import ripe.ripe.APIUtils.RipeUserService;
import ripe.ripe.NavFragments.UploadFlow.Up.ShareFragment;
import ripe.ripe.R;

public class GroupsFragment extends Fragment {

    private int CAMERA_REQUEST_CODE = 5;

    View view;
    TableLayout tableLayout;
    TextView rowLabel;
    ImageButton addContentButton;
    TextView newGroupTV;
    String uuid;
    String selectedGroup;

    RipeGroupService groupService;

    private OnFragmentInteractionListener mListener;

    public GroupsFragment() {
        // Required empty public constructor
    }

    public static GroupsFragment newInstance(String param1, String param2) {
        GroupsFragment fragment = new GroupsFragment();
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
        view = inflater.inflate(R.layout.fragment_groups, container, false);
        newGroupTV = (TextView) view.findViewById(R.id.newGroup);
        tableLayout = (TableLayout) view.findViewById(R.id.tableLayout);

        uuid = Preference.getSharedPreferenceString(getActivity().getApplicationContext(), "userId", "oops");

        newGroupTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] options = {"Create New Group", "Join a Group"};

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Groups:");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]
                        if (which == 0) {
                            // Create a new group
                            createGroup();
                        }
                        else if (which == 1) {
                            // Join a group
                            inputGroupDialog();
                        }
                        Log.d("ZUHEIR", "" + which);
                    }
                });
                builder.show();
            }
        });
        updateTable();
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void inputGroupDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("What is the group code?");

        // Set up the input
        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Join group
                if (input.getText().length() > 0) {
                    RipeGroupService groupService = new RipeGroupService();
                    groupService.joinGroup(uuid, input.getText().toString(), new RipeGroupService.JoinGroupCallback() {
                        @Override
                        public void finishJoiningGroup() {
                            updateTable();
                        }
                    });
                }
                else {
                    inputGroupDialog();
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();

        dialog.show();

        Button pos = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        pos.setTextColor(Color.rgb(255,255,255));

        Button neg = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        neg.setTextColor(Color.rgb(255,255, 255));
    }

    private void createGroup() {
        groupService = new RipeGroupService();
        groupService.createGroup(uuid, new RipeGroupService.CreateGroupCallback() {
            @Override
            public void setUpGroup(String groupId) {
                updateTable();
            }
        });
    }

    private void updateTable() {
        tableLayout.removeAllViews();

        String uuid = Preference.getSharedPreferenceString(getActivity().getApplicationContext(), "userId", "oops");

        RipeUserService userService = new RipeUserService();
        userService.getUserById(uuid, new RipeUserService.GetUserCallback() {
            @Override
            public void populateUser(RipeUser ripeUsers) {
                for (final String groupID : ripeUsers.groups) {
                    TableRow row = (TableRow) LayoutInflater.from(getContext()).inflate(R.layout.group_table_row, null);
                    rowLabel = row.findViewById(R.id.label);
                    rowLabel.setText(groupID);
                    final String rowGroupId = groupID;
                    addContentButton = row.findViewById(R.id.addContent);
                    addContentButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                selectedGroup = rowGroupId;
                                startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
                            }
                            else {
                                Intent inte = new Intent(getActivity(), ShareFragment.class);
                                inte.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(inte);
                            }
                        }
                    });

                    row.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), SwipeGroupActivity.class);
                            intent.putExtra("group_id", rowGroupId);
                            startActivity(intent);
                        }
                    });

                    tableLayout.addView(row);
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAMERA_REQUEST_CODE) {
            Bitmap bitmap = null;
            try {
                if (data.getExtras().get("data") != null) {
                    RipeGroupService groupService = new RipeGroupService();
                    bitmap = (Bitmap) data.getExtras().get("data");

                    Intent intent = new Intent(getActivity(), SwipeGroupActivity.class);
                    intent.putExtra("group_id", selectedGroup);

                    File filesDir = getActivity().getApplicationContext().getFilesDir();
                    File imageFile = new File(filesDir, "image.jpg");

                    OutputStream os;
                    try {
                        os = new FileOutputStream(imageFile);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                        os.flush();
                        os.close();

                        groupService.postToGroup(selectedGroup,imageFile);
                    } catch (Exception e) {
                        Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
                    }
                    startActivity(intent);
                }
            } catch (NullPointerException e) { }
        }
    }
}