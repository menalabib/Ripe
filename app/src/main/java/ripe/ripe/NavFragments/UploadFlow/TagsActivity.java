package ripe.ripe.NavFragments.UploadFlow;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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

import java.util.ArrayList;

import ripe.ripe.NavActivity;
import ripe.ripe.R;

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

        uploadTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Upload tags + content
                if (tags.size() > 0) {
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

        if (getIntent().hasExtra(getString(R.string.selected_image))) {
            Log.d("ZUHEIR", getIntent().getStringExtra(getString(R.string.selected_image)));
        }
        else if (getIntent().hasExtra(getString(R.string.selected_bitmap))) {
            Log.d("ZUHEIR", ((Bitmap) getIntent().getParcelableExtra(getString(R.string.selected_bitmap))).toString());
        }
        else if (getIntent().hasExtra(getString(R.string.selected_video))) {
            Log.d("ZUHEIR", ((Uri) getIntent().getParcelableExtra(getString(R.string.selected_video))).toString());
        }

    }
}
