package org.fitzeng.tablayout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TabLayout and ViewPager to draw this layout
        Button btnTabLayoutAndViewPager = (Button) findViewById(R.id.btnTabLayoutViewPager);
        btnTabLayoutAndViewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TabLayoutAndViewPagerActivity.class);
                startActivity(intent);
            }
        });
    }
}
