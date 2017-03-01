package org.fitzeng.emojikeyboard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.fitzeng.emojikeyboard.simpleemojikeyboard.SimpleEmojiKeyboardActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Simple Emoji Keyboard Demo
        Button btnSimpleEmojiKeyboard = (Button) findViewById(R.id.btnSimpleEmojiKeyboard);
        btnSimpleEmojiKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SimpleEmojiKeyboardActivity.class);
                startActivity(intent);
            }
        });


    }
}
