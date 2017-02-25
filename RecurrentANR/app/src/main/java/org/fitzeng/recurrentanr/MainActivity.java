package org.fitzeng.recurrentanr;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnMainThread;
    Button btnBroadcastReceiver;
    Button btnService;
    Button btnMainThreadNotANR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    /**
     * Init button and set ClickListener event
     * 初始化按钮和设置监听事件
     */
    private void init() {
        btnMainThread = (Button) findViewById(R.id.btnMainThreadANR);
        btnBroadcastReceiver = (Button) findViewById(R.id.btnBroadcastReceiverANR);
        btnService = (Button) findViewById(R.id.btnServiceANR);
        btnMainThreadNotANR = (Button) findViewById(R.id.btnMainThreadNotANR);

        btnMainThread.setOnClickListener(this);
        btnBroadcastReceiver.setOnClickListener(this);
        btnService.setOnClickListener(this);
        btnMainThreadNotANR.setOnClickListener(this);
    } // init()

    /**
     * Recurrent ANR in main Thread
     */
    private void mainThreadANR() {
        Looper myLooper = Looper.myLooper();
        Looper mainLooper = Looper.getMainLooper();
        Log.i("MainThreadANR", "myLooper=" + myLooper + ";   mainLooper=" + mainLooper);
        try {
            Thread.sleep(20 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i("MainThreadANR", "mainThreadANR() Done");
    }

    /**
     * Deal ANR in main Thread
     */
    private void mainThreadNotANR() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper myLooper = Looper.myLooper();
                Looper mainLooper = Looper.getMainLooper();
                Log.i("MainThreadNotANR", "myLooper=" + myLooper + ";   mainLooper=" + mainLooper);
                try {
                    Thread.sleep(20 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.i("MainThreadNotANR", "Thread Run() Done");
            }
        }).start();
        Log.i("MainThreadNotANR", "mainThreadNotANR() Done");
    }

    /**
     * Deal the click events
     * 处理点击事件
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnMainThreadANR: {
                mainThreadANR();
                break;
            } // case MainThreadANR
            case R.id.btnBroadcastReceiverANR: {
                Intent intent = new Intent(this, BroadcastANR.class);
                sendBroadcast(intent);
                break;
            } // case BroadcastANR
            case R.id.btnServiceANR: {
                Intent intent = new Intent(this, ServiceANR.class);
                startService(intent);
                break;
            } // case ServiceANR
            case R.id.btnMainThreadNotANR: {
                mainThreadNotANR();
            }
            default:
                break;
        } // switch
    } // onClick()
}
