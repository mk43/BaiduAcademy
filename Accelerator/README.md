<!-- MarkdownTOC -->

- [BaiduAcademy](#baiduacademy)
	- [NO.6 加速器](#no6-加速器)
		- [任务描述](#任务描述)
		- [任务演示](#任务演示)
		- [实现过程](#实现过程)
		- [不足](#不足)

<!-- /MarkdownTOC -->


# [BaiduAcademy](https://github.com/mk43/BaiduAcademy)

## [NO.6 加速器](http://ife.baidu.com/course/detail/id/77)

### 任务描述

---

点击"+0.01"按钮,文本框会在现有数字上进行加0.01， 长按会有增值加速的效果，同理，点击"-0.01"按钮,文本框会在现有数字上进行减0.01， 长按会有减值加速的效果。

### 任务演示

---

![](https://github.com/mk43/blogResource/blob/master/BaiduAcademy/Accelerator/AcceleratorDemo.gif)

### 实现过程

---

这是一个比较简单的题目，只要考虑怎么实现加速效果就可以了。

- 实现UI

![](https://github.com/mk43/blogResource/blob/master/BaiduAcademy/Accelerator/UI.png)

直接上代码

```xml
<LinearLayout
    android:orientation="horizontal"
    android:layout_width="match_parent"
    android:layout_height="wrap_content">
    <TextView
        android:text="@string/number"
        android:textSize="30sp"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content" />
    <TextView
        android:id="@+id/number"
        android:text="@string/Number_100_00"
        android:textSize="30sp"
        android:layout_marginStart="20dp"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
</LinearLayout>

<LinearLayout
    android:orientation="horizontal"
    android:layout_width="match_parent"
    android:layout_height="wrap_content">
    <Button
        android:id="@+id/btnAdd"
        android:text="@string/add_0_01"
        android:textAppearance="?android:attr/textAppearanceLarge"
        android:layout_weight="1"
        android:layout_width="0dp"
        android:layout_height="wrap_content" />
    <Button
        android:id="@+id/btnReduce"
        android:text="@string/reduce_0_01"
        android:textAppearance="?android:attr/textAppearanceLarge"
        android:layout_weight="1"
        android:layout_width="0dp"
        android:layout_height="wrap_content" />
</LinearLayout>
```

- 实现点击效果
以加为例

处理点击事件：

```java
case R.id.btnAdd: {
    switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN: {
            isClick = true;
            break;
        }
        case MotionEvent.ACTION_UP: {
            if (isClick) {
                handler.sendEmptyMessage(ADD_0_01);
            }
            break;
        }
    }

    break;
}
```

实现滑动事件

```java
case MotionEvent.ACTION_MOVE: {
    // Watch interval time
    intervalTime = event.getEventTime() - event.getDownTime();
    if (intervalTime > 200) {
        isClick = false;
        isAccAdd = true;
        if (timer == null) {
            startTimer();
        }
    }
    break;
}
case MotionEvent.ACTION_UP: {
    if (timer != null) {
        timer.cancel();
        timer = null;
    }
}
```

其中最关键的就是设置计时器和handle信息。

```java
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
```

一个很简单的加速器就做好了

### 不足

---

其实这个加速器有一个很大的缺陷，滑动事件和长按事件可以通过判断坐标来区分，但是这个touch监听在 MotionEvent.ACTION_MOVE的时候才会触发，也就是如果一直点在一个不动， MOVE动作是不会触发的。但是在实际手机上不可能完全按在一个像素点保持不动的。但是如果考虑极端情况，我们可以设置OnLongClickListener来实现监听。

```java
btnAdd.setOnLongClickListener(this);
btnReduce.setOnLongClickListener(this);
```

还有一个问题就是加速是有上限的，Android是16ms刷新一次屏幕，所以最多在1s内可以刷新60多次，尽量控制在这个数字以内，所以如果长按时间过长，我们可以设置刷新值为一个定值。