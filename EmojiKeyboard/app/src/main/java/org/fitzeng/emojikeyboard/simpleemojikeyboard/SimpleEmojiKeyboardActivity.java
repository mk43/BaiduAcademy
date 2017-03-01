package org.fitzeng.emojikeyboard.simpleemojikeyboard;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import org.fitzeng.emojikeyboard.R;

public class SimpleEmojiKeyboardActivity extends AppCompatActivity {

    private SimpleEmojiEditLayout simpleEmojiEditLayout;
    private ImageView touchIcon;
    private ImageView btndelete;
    private SimpleEmojiKeyboardLayout simpleEmojiKeyboardLayout;
    private GridView gridView;

    private Editable content;
    private int location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_emoji_keyboard);

        init();

        clickListener();

    }

    /**
     * Click listener list
     */
    private void clickListener() {
        // Click icon to make keyboard hide or appear
        touchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (simpleEmojiKeyboardLayout.getVisibility() == View.GONE) {
                    simpleEmojiKeyboardLayout.setVisibility(View.VISIBLE);
                    simpleEmojiKeyboardLayout.slipUp();
                } else {
                    simpleEmojiKeyboardLayout.setVisibility(View.GONE);
                }
            }
        });

        // Make keyboard appear when click to edit
        simpleEmojiEditLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (simpleEmojiKeyboardLayout.getVisibility() == View.GONE) {
                    simpleEmojiKeyboardLayout.slipUp();
                    simpleEmojiKeyboardLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        // Called by click an emoji
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Calc lineHeight for ensure icon size
                int lineHeight = simpleEmojiEditLayout.getLineHeight();
                // Create a bitmap
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), (int) id);
                // Set width and height
                bitmap = Bitmap.createScaledBitmap(bitmap, lineHeight - 10, lineHeight - 10, true);
                //
                ImageSpan imageSpan = new ImageSpan(SimpleEmojiKeyboardActivity.this, bitmap);
                SpannableString spannableString = new SpannableString("e");
                spannableString.setSpan(imageSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                // Get texts from textView
                content = simpleEmojiEditLayout.getText();
                // Get cursor location
                location = simpleEmojiEditLayout.getSelectionStart();
                // Insert text
                content.insert(location, spannableString);
            }
        });

        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCharOrIcon();
            }
        });
    }

    /**
     * Delete func
     */
    private void deleteCharOrIcon() {
        content = simpleEmojiEditLayout.getText();
        location = simpleEmojiEditLayout.getSelectionStart();
        if (location != 0) {
            content.delete(location - 1, location);
        } else {
            Toast.makeText(SimpleEmojiKeyboardActivity.this, "Nothing to delete", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Init widget
     */
    private void init() {
        simpleEmojiEditLayout = (SimpleEmojiEditLayout) findViewById(R.id.etSimpleEmojiEdit);
        touchIcon = (ImageView) findViewById(R.id.touchIcon);
        btndelete = (ImageView) findViewById(R.id.btnDelete);
        simpleEmojiKeyboardLayout = (SimpleEmojiKeyboardLayout) findViewById(R.id.simpleEmojiKeyboardLayout);
        gridView = (GridView) findViewById(R.id.gridView);

        simpleEmojiKeyboardLayout.setVisibility(View.GONE);
    }
}
