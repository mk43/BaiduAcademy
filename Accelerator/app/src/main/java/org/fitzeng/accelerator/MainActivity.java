package org.fitzeng.accelerator;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, View.OnLongClickListener{

    private TextView textView;
    private boolean isClick;
    private Timer timer;
    private boolean isAccAdd;
    private float intervalTime;
//    private boolean isMoveLongClick;

    private static final int ADD_0_01 = 0;
    private static final int REDUCE_0_01 = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.number);
        Button btnAdd = (Button) findViewById(R.id.btnAdd);
        Button btnReduce = (Button) findViewById(R.id.btnReduce);

        btnAdd.setOnTouchListener(this);
        btnReduce.setOnTouchListener(this);

//        btnAdd.setOnLongClickListener(this);
//        btnReduce.setOnLongClickListener(this);

    }

    private void startTimer() {
        timer = new Timer();
        // Set timer speed
        int time = (int) (intervalTime / 1000 + 1);
        if (time > 5) {
            time = 5;
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (isAccAdd) {
                    handler.sendEmptyMessage(ADD_0_01);
                } else {
                    handler.sendEmptyMessage(REDUCE_0_01);
                }
            }
        }, 0, 1000/(time * 10));
    }


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            double content = Double.parseDouble(textView.getText().toString());
            switch (msg.what) {
                case ADD_0_01: {
                    content += 0.01;
                    textView.setText(new DecimalFormat("#.00").format(content));
                    break;
                }
                case REDUCE_0_01: {
                    content -= 0.01;
                    textView.setText(new DecimalFormat("#.00").format(content));
                    break;
                }
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (v.getId()) {
            case R.id.btnAdd: {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        isClick = true;
                        break;
                    }
                    case MotionEvent.ACTION_MOVE: {
                        // Watch interval time
                        intervalTime = event.getEventTime() - event.getDownTime();
                        if (intervalTime > 200) {
                            isClick = false;
                            isAccAdd = true;
//                            isMoveLongClick = true;
                            if (timer == null) {
                                startTimer();
                            }
                        }
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        if (isClick) {
                            handler.sendEmptyMessage(ADD_0_01);
                        }
                        if (timer != null) {
                            timer.cancel();
                            timer = null;
                        }
                        break;
                    }
                }

                break;
            }
            case R.id.btnReduce: {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        isClick = true;
                        break;
                    }
                    case MotionEvent.ACTION_MOVE: {
                        intervalTime = event.getEventTime() - event.getDownTime();
                        if (intervalTime > 200) {
                            isClick = false;
                            isAccAdd = false;
//                            isMoveLongClick = true;
                            if (timer == null) {
                                startTimer();
                            }
                        }
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        if (isClick) {
                            handler.sendEmptyMessage(REDUCE_0_01);
                        }
                        if (timer != null) {
                            timer.cancel();
                            timer = null;
                        }
                        break;
                    }
                    default:
                        break;
                }
                break;
            }
            default:
                break;
        }
        return false;
    }

    @Override
    public boolean onLongClick(View v) {
        return false;
    }
}
