package org.fitzeng.recurrentoom;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class SingletonOOM extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp);

        SingletonDemo.getInstance(this);
    }
}

class SingletonDemo {

    private static volatile SingletonDemo instance;

    private Context mContext;

    private SingletonDemo(Context context) {
        this.mContext = context;
    }

    static void getInstance(Context context) {
        if (instance == null) {
            synchronized (SingletonDemo.class) {
                if (instance == null) {
                    instance = new SingletonDemo(context);
                }
            }
        }
    }
}
