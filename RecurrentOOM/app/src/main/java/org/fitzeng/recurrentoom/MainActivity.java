package org.fitzeng.recurrentoom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnStaticContext = (Button) findViewById(R.id.staticContext);
        Button btnStaticView = (Button) findViewById(R.id.staticView);
        Button btnSingleton = (Button) findViewById(R.id.singleton);
        Button btnAnimation = (Button) findViewById(R.id.animation);

        btnStaticContext.setOnClickListener(this);
        btnStaticView.setOnClickListener(this);
        btnSingleton.setOnClickListener(this);
        btnAnimation.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.staticContext: {
                Intent i = new Intent(this, StaticContextOOM.class);
                startActivity(i);
                break;
            }
            case R.id.staticView: {
                Intent i = new Intent(this, StaticViewOOM.class);
                startActivity(i);
                break;
            }
            case R.id.singleton: {
                Intent i = new Intent(this, SingletonOOM.class);
                startActivity(i);
                break;
            }
            case R.id.animation: {
                Intent i = new Intent(this, AnimationOOM.class);
                startActivity(i);
                break;
            }
            default:
                break;
        }
    }
}
