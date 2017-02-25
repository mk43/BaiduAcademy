# [BaiduAcademy](https://github.com/mk43/BaiduAcademy)

## [NO.1 复现ANR](http://ife.baidu.com/course/detail/id/70)

### 任务描述
#### 写出不少于3种常见ANR错误的Android代码
---
- 主线程复现ANR
- BroadcastReceiver复现ANR
- Service复现ANR

### 1.什么是ANR?
---
ANR(Application Not Responding),应用程序无响应. 参考[ANR完全解析](https://blog.saymagic.tech/2014/03/25/anr-analyze.html)

### 2.ANR的三种类型
--- 
- KeyDispatchTimeout(5 seconds) 主要类型按键或触摸事件在特定时间内无响应
- BroadcastTimeout(10 seconds) BroadcastReceiver在特定时间内无法处理完成
- ServiceTimeout(20 seconds) 小概率类型 Service在特定的时间内无法处理完成

### 3.复现ANR

#### I 主线程复现ANR
--- 
- 代码
```java
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
```
- 结果

![](https://github.com/mk43/blogResource/blob/master/BaiduAcademy/RecurrentANR/ANR.png)

#### II BroadcastReceiver复现ANR
- 代码
```java
public class BroadcastANR extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Looper myLooper = Looper.myLooper();
        Looper mainLooper = Looper.getMainLooper();
        Log.i("BroadcastANR", "myLooper=" + myLooper + ";   mainLooper=" + mainLooper);
        try {
            Thread.sleep(80 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

#### III Service复现ANR
- 代码
```java
public class ServiceANR extends Service {

    @Override
    public void onCreate() {
        Looper myLooper = Looper.myLooper();
        Looper mainLooper = Looper.getMainLooper();
        Log.i("ServiceANR", "myLooper=" + myLooper + ";   mainLooper=" + mainLooper);
        try {
            Thread.sleep(60 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
```

### 4.避免ANR
要想避免ANR，首先要分析为什么会发生ANR，主要是在一些不能耗时操作的函数中处理代码的时间过长，所以把耗时长的程序写入其他子进程中就可以避免。[ANR机制以及问题分析](https://duanqz.github.io/2015-10-12-ANR-Analysis#service)写的很详细。

![](https://github.com/mk43/blogResource/blob/master/BaiduAcademy/RecurrentANR/ReANR.png)

首先我们看看Log

---
```
MainThreadANR: 
I/MainThreadANR: myLooper=Looper (main, tid 1) {13467e36};   mainLooper=Looper (main, tid 1) {13467e36}

BroadcastANR:
I/BroadcastANR: myLooper=Looper (main, tid 1) {33c6137};   mainLooper=Looper (main, tid 1) {33c6137}

ServiceANR:
I/ServiceANR: myLooper=Looper (main, tid 1) {303e756a};   mainLooper=Looper (main, tid 1) {303e756a}
```
前面的三个ANR都有一个特点，那就是当前的线程都是主线程，细心点会发现其实还有一句丢帧Log打印

---
```
Skipped 1204 frames!  The application may be doing too much work on its main thread.
```

我实现下通过在主线程中创建一个新线程的方法来避免ANR
- 代码
```java
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
```
下面我们来对比下和主线程中直接处理的Log有什么不同
```
MainThreadNotANR:
点击按钮MainThreadNotANR
I/MainThreadNotANR: mainThreadNotANR() Done
I/MainThreadNotANR: myLooper=null;   mainLooper=Looper (main, tid 1) {303e756a}
I/MainThreadNotANR: ThreadRun() Done
W/art: Suspending all threads took: 6.032ms

MainThreadANR:
点击按钮MainThreadANR
I/MainThreadANR: myLooper=Looper (main, tid 1) {303e756a};   mainLooper=Looper (main, tid 1) {303e756a}
I/MainThreadANR: mainThreadANR() Done
再多次点击按钮MainThreadANR
I/Choreographer: Skipped 1200 frames!  The application may be doing too much work on its main thread.
I/MainThreadANR: myLooper=Looper (main, tid 1) {303e756a};   mainLooper=Looper (main, tid 1) {303e756a}
I/art: Thread[5,tid=2037,WaitingInMainSignalCatcherLoop,Thread*=0xef70d400,peer=0x12c00080,"Signal Catcher"]: reacting to signal 3
I/art: Wrote stack traces to '/data/anr/traces.txt'
```
从Log中可以看出MainThreadNotANR中先打印mainThreadNotANR() Done后打印ThreadRun() Done说明代码没在MainThreadNotANR这个线程中运行，从myLooper=null;   mainLooper=Looper (main, tid 1) {303e756a}可以验证我们的猜想。
而MainThreadANR中就很明显，各种丢帧奔溃。不过I/art: Wrote stack traces to '/data/anr/traces.txt'告诉我们可以在/data/anr/traces.txt中追踪奔溃的缘由
```
traces.txt

DALVIK THREADS (18):
"main" prio=5 tid=1 Sleeping
  | group="main" sCount=1 dsCount=0 obj=0x73581970 self=0xf4306800
  | sysTid=2030 nice=0 cgrp=apps sched=0/0 handle=0xf7761160
  | state=S schedstat=( 545706267 72336573 314 ) utm=30 stm=23 core=3 HZ=100
  | stack=0xff7c7000-0xff7c9000 stackSize=8MB
  | held mutexes=
  at java.lang.Thread.sleep!(Native method)
  - sleeping on <0x2ce90c0d> (a java.lang.Object)
  at java.lang.Thread.sleep(Thread.java:1031)
  - locked <0x2ce90c0d> (a java.lang.Object)
  at java.lang.Thread.sleep(Thread.java:985)
  at org.fitzeng.recurrentanr.MainActivity.mainThreadANR(MainActivity.java:47)
  at org.fitzeng.recurrentanr.MainActivity.onClick(MainActivity.java:81)
  at android.view.View.performClick(View.java:4756)
  at android.view.View$PerformClick.run(View.java:19749)
  at android.os.Handler.handleCallback(Handler.java:739)
  at android.os.Handler.dispatchMessage(Handler.java:95)
  at android.os.Looper.loop(Looper.java:135)
  at android.app.ActivityThread.main(ActivityThread.java:5221)
  at java.lang.reflect.Method.invoke!(Native method)
  at java.lang.reflect.Method.invoke(Method.java:372)
  at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:899)
  at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:694)
```
这是定位ANR的有效手段。








Tips: 作为一个Android小白，自然有很多不知道不理解的地方。如果文中有错或者有哪些值得改进的地方，欢迎大家提意见，我很开心能和大家一起交流学习，共同进步。

多谢阅读