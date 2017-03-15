package org.fitzeng.recurrentoom;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class AnimationOOM extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);

        TextView textView = (TextView) findViewById(R.id.text);
        textView.setVisibility(View.GONE);

        Button button = (Button) findViewById(R.id.btnAnimation);
        button.setVisibility(View.VISIBLE);

        ObjectAnimator animator = ObjectAnimator.ofFloat(button, "rotation", 0, 360).setDuration(3 * 1000);
        animator.start();
//        animator.cancel();
    }
}
