package ripe.ripe.NavFragments.UploadFlow.Up;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import ripe.ripe.R;
import ripe.ripe.Utils.UniversalImageLoader;

public class TitleActivity extends AppCompatActivity {

    Intent intent;

    private String selectedImage;
    private Bitmap bitmap;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        setContentView(R.layout.activity_title);

        ImageView back = (ImageView) findViewById(R.id.ivBackArrow);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        setImage();

        final EditText titleText = (EditText) findViewById(R.id.title);

        TextView nextScreen = (TextView) findViewById(R.id.tvTags);
        final TextView warning = (TextView) findViewById(R.id.tvWarning);
        warning.setAlpha(0);
        nextScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titleText.getText().length() != 0) {
                    warning.setAlpha(0);
                    Intent intent = new Intent(TitleActivity.this, TagsActivity.class);
                    if (getIntent().hasExtra(getString(R.string.selected_image))) {
                        intent.putExtra(getString(R.string.selected_image), selectedImage);
                    }
                    else if (getIntent().hasExtra(getString(R.string.selected_bitmap))) {
                        intent.putExtra(getString(R.string.selected_bitmap), bitmap);
                    }
                    else if (getIntent().hasExtra(getString(R.string.selected_video))) {
                        intent.putExtra(getString(R.string.selected_video), uri);
                    }
                    startActivity(intent);
                }
                else {
                    warning.setAlpha(1);
                }
            }
        });
    }

    private void setImage() {
        intent = getIntent();
        ImageView imageView = (ImageView) findViewById(R.id.imgShare);

        // image selected from gallery
        if (intent.hasExtra(getString(R.string.selected_image))) {
            selectedImage = intent.getStringExtra(getString(R.string.selected_image));
            UniversalImageLoader.setImage(selectedImage, imageView, null, "file:/");
        }
        // image taken from camera
        else if (intent.hasExtra(getString(R.string.selected_bitmap))) {
            bitmap = (Bitmap) intent.getParcelableExtra(getString(R.string.selected_bitmap));
            bitmap = rotateImage(bitmap, -90);
            imageView.setImageBitmap(bitmap);
        }
        // video taken from camera
        else if (intent.hasExtra(getString(R.string.selected_video))) {
            uri = (Uri) intent.getParcelableExtra(getString(R.string.selected_video));

            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = this.getContentResolver().query(uri, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(picturePath, MediaStore.Video.Thumbnails.MICRO_KIND);
            imageView.setImageBitmap(bitmap);
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }
}
