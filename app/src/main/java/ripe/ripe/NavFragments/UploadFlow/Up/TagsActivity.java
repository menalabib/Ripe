package ripe.ripe.NavFragments.UploadFlow.Up;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import ripe.ripe.APIUtils.Preference;
import ripe.ripe.APIUtils.RipeContent;
import ripe.ripe.APIUtils.RipeContentService;
import ripe.ripe.NavActivity;
import ripe.ripe.R;
import ripe.ripe.Utils.UniversalImageLoader;

public class TagsActivity extends AppCompatActivity {

    EditText editText;
    Button addButton;
    TextView uploadTV;
    ImageView backArrowImageView;
    ChipGroup chipGroup;

    ArrayList<String> tags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_tags);

        tags = new ArrayList<>();
        editText = (EditText) findViewById(R.id.textInput_tag);
        addButton = (Button) findViewById(R.id.addBtn);
        uploadTV = (TextView) findViewById(R.id.tvUpload);
        backArrowImageView = (ImageView) findViewById(R.id.ivBackArrow);
        chipGroup = (ChipGroup) findViewById(R.id.chipGroup);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String text = editText.getText().toString();
                if (text.length() > 0 && !tags.contains(text)) {
                    tags.add(text);
                    editText.setText("");
                    final Chip chip = new Chip(TagsActivity.this);
                    chip.setText(text);
                    chip.setChipBackgroundColorResource(R.color.peach);
                    chip.setTextAppearanceResource(R.style.ChipTextStyle);
                    chip.setElevation(15);
                    chip.setCloseIconResource(R.drawable.close_tag);
                    chip.setCloseIconVisible(true);
                    chip.setOnCloseIconClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            chipGroup.removeView(chip);
                            tags.remove(text);
                        }
                    });
                    chipGroup.addView(chip);
                }
            }
        });

        final String uuid = Preference.getSharedPreferenceString(getApplicationContext(), "userId", "Ripe");

        uploadTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Upload tags + content
                if (tags.size() > 0) {
                    Intent intent = getIntent();
                    String title = (String) intent.getExtras().get("TITLE");
                    ImageView imageView = (ImageView) findViewById(R.id.imgShare);
                    RipeContentService service = new RipeContentService();

                    // image selected from gallery
                    if (intent.hasExtra(getString(R.string.selected_image))) {
                        String selectedImage = intent.getStringExtra(getString(R.string.selected_image));
                        File file = new File(selectedImage);

                        ArrayList<String> arrayList = new ArrayList<>();
                        service.uploadContent(new RipeContent("0", title, tags, uuid, file));
                    }
                    // image taken from camera
                    else if (intent.hasExtra(getString(R.string.selected_bitmap))) {
                        Bitmap bitmap = (Bitmap) intent.getParcelableExtra(getString(R.string.selected_bitmap));

                        File filesDir = getApplicationContext().getFilesDir();
                        File imageFile = new File(filesDir, "image.jpg");

                        OutputStream os;
                        try {
                            os = new FileOutputStream(imageFile);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                            os.flush();
                            os.close();

                            service.uploadContent(new RipeContent("0", title, tags, uuid, imageFile));
                        } catch (Exception e) {
                            Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
                        }
                    }
                    // video taken from camera
                    else if (intent.hasExtra(getString(R.string.selected_video))) {
                        Uri uri = (Uri) intent.getParcelableExtra(getString(R.string.selected_video));

                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = TagsActivity.this.getContentResolver().query(uri, filePathColumn, null, null, null);
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String videoPath = cursor.getString(columnIndex);
                        cursor.close();

                        File file = new File(videoPath);
                        service.uploadContent(new RipeContent("1", title, tags, uuid, file));
                    }

                    Intent i = new Intent(TagsActivity.this, NavActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            }
        });

        backArrowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }
}
