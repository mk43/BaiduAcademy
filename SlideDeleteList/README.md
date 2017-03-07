<!-- MarkdownTOC -->

- [BaiduAcademy](#baiduacademy)
    - [NO.7 自定义滑动删除列表](#no7-自定义滑动删除列表)
        - [任务描述](#任务描述)
        - [实现效果](#实现效果)
        - [实现过程](#实现过程)

<!-- /MarkdownTOC -->


# [BaiduAcademy](https://github.com/mk43/BaiduAcademy)

## [NO.7 自定义滑动删除列表](http://ife.baidu.com/course/detail/id/78)

### 任务描述

---

实现一个可以滑动删除的ListView列表，列表中的每一行可以通过从左向右滑动显示出删除按钮，点击该按钮之后删除该行

### 实现效果

---

![](https://github.com/mk43/blogResource/blob/master/BaiduAcademy/SlideDeleteList/slideDeleteListDemo.gif)

### 实现过程

---

最开始还是要设计layout，这决定着你的代码写法。

- layout
  + activity_listview
  + listitem
  + content_view
  + menu_view

我们可以把每一个Item设为一个布局控件，也就是listitem

```xml
<merge xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="60dp">
    <LinearLayout
        android:id="@+id/content"
        android:orientation="horizontal"
        android:layout_width="match_parent"
        android:layout_height="match_parent" >
    </LinearLayout>

    <LinearLayout
        android:id="@+id/menu"
        android:orientation="horizontal"
        android:layout_width="80dp"
        android:layout_height="match_parent" >
    </LinearLayout>
</merge>
```

接着分别连个布局设置content和menu

```xml
content_view
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <TextView
        android:id="@+id/textView"
        android:text="@string/init_text"
        android:textAppearance="?android:attr/textAppearanceLarge"
        android:gravity="center_vertical"
        android:layout_marginLeft="20dp"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
</RelativeLayout>

menu_view
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <Button
        android:id="@+id/delete"
        android:text="@string/delete"
        android:textAllCaps="false"
        android:textAppearance="?android:attr/textAppearanceLarge"
        android:background="@color/colorAccent"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</RelativeLayout>
```

接下来就很清楚了。利用listview添加控件listitem作为适配器的布局。

- ViewHolder缓存加载

避免重复加载控件，采用缓存机制是比较合理的
下面我们看看adapter的getView()

```java
public View getView(final int position, View convertView, ViewGroup parent) {

    ViewHolder holder;
    SlideItemView slideItemView = (SlideItemView) convertView;

    if (convertView == null) {
        // get content and menu view
        View contentView = inflater.inflate(R.layout.content_view, null);
        View menuView = inflater.inflate(R.layout.menu_view, null);
        // add content and menu to slideItemView
        slideItemView = new SlideItemView(context);
        slideItemView.setContentView(contentView);
        slideItemView.setMenuView(menuView);
        // setting widgets
        holder = new ViewHolder(contentView, menuView);
        // setting slide listener
        slideItemView.setOnSlideListener((SlideItemView.OnSlideListener) context);
        slideItemView.setTag(holder);
    } else {
        holder = (ViewHolder) convertView.getTag();
    }
    // init widgets
    ItemMsg itemMsg = itemMsgList.get(position);
    itemMsg.setSlideItemView(slideItemView);
    itemMsg.getSlideItemView().shrink();

    holder.text.setText(itemMsg.getContent());
    holder.btnDel.setText(R.string.delete);
    holder.btnDel.setOnClickListener((View.OnClickListener) context);

    return slideItemView;
}

private final class ViewHolder {
    TextView text;
    Button btnDel;
    ViewHolder(View content, View menu) {
        text = (TextView) content.findViewById(R.id.textView);
        btnDel = (Button) menu.findViewById(R.id.delete);
    }
}
```

代码基本都有注释，逻辑比较清晰，主要是对适配器的控件初始化。注意返回的是slideItemView，同时也设置了监听，所以此处是加载两个布局（content,menu）合成一个slideItemView，这样便于以后的布局更改。也方便视图的添加和控件的监测。

- 构造slideItemView

部分函数

```java
private void initView() {
    Context context = getContext();
    scroller = new Scroller(context);
    // inflate layout
    View.inflate(context, R.layout.listitem, this);
    contentView = (LinearLayout) findViewById(R.id.content);
    menuView = (LinearLayout) findViewById(R.id.menu);
}

public void setContentView(View content) {
    contentView.addView(content);
}

public void setMenuView(View menu) {
    menuView.addView(menu);
}
```

- 滑动监听

这个才是重点难点
代码如下

```java
public boolean onTouchEvent(MotionEvent event) {
    // Get event start coordinate
    int x = (int) event.getX();
    int y = (int) event.getY();
    int scrollX = getScrollX();

    switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN: {
            // if another event unfinished, then finished
            if (!scroller.isFinished()) {
                scroller.abortAnimation();
            }
            // get menu width
            btnWidth = menuView.getMeasuredWidth();
            if (onSlideListener != null) {
                onSlideListener.onSlide(this, OnSlideListener.STATUS_SLIDE_SCROLL);
            }
            // represent the event was handled or consumed, see the resource code
            return true;
        }
        case MotionEvent.ACTION_MOVE: {
            // get offset
            int offsetX = x - lastX;
            int offsetY = y - lastY;
            // if offsetX miner than offsetY or offsetY more than 20, then cancel this event
            if (offsetY > 20) {
                break;
            }
            int newScrollX = scrollX - offsetX;
            if (offsetX != 0) {
                if (newScrollX < 0) {
                    newScrollX = 0;
                } else if (newScrollX > btnWidth){
                    newScrollX = btnWidth;
                }
                scrollTo(newScrollX, 0);
            }
            break;
        }
        case MotionEvent.ACTION_UP:
        case MotionEvent.ACTION_CANCEL: {
            int newScroll = 0;
            // more than half of button width, scroll out. otherwise scroll in.
            if (scrollX - btnWidth * 0.7 > 0) {
                newScroll = btnWidth;
            }
            smoothScrollTo(newScroll);
            // set item slide status
            if (onSlideListener != null) {
                onSlideListener.onSlide(this, newScroll == 0 ? OnSlideListener.STATUS_SLIDE_OFF
                        : OnSlideListener.STATUS_SLIDE_ON);
            }
            break;
        }
    }
    lastX = x;
    lastY = y;
    return super.onTouchEvent(event);
}

private void smoothScrollTo(int x) {
    int scrollX = getScrollX();
    int offsetX = x - scrollX;
    scroller.startScroll(scrollX, 0, offsetX, 0, Math.abs(offsetX));
    invalidate();
}

@Override
public void computeScroll() {
    if (scroller.computeScrollOffset()) {
        scrollTo(scroller.getCurrX(), scroller.getCurrY());
        postInvalidate();
    }
}

public void shrink() {
    if (getScaleX() != 0) {
        this.smoothScrollTo(0);
    }
}
```

参考
- [列表滑动删除效果](http://blog.csdn.net/singwhatiwanna/article/details/17515543)
- [Android触摸屏事件派发机制详解与源码分析一(View篇)](http://blog.csdn.net/yanbober/article/details/45887547)
- [android中的dispatchTouchEvent、onInterceptTouchEvent和onTouchEvent](http://www.jianshu.com/p/35a8309b9597#)

后两篇主要是讲Android中的事件分发，也就是为什么在MotionEvent.ACTION_DOWN中return true才能实现滑动效果。看了源码我们知道true代表这个控件的函数消耗这个事件，不会再分发，所以我们后面的处理事件的代码才能够执行。


- 设置接口

```java
interface OnSlideListener {

    int STATUS_SLIDE_OFF = 0;
    int STATUS_SLIDE_ON = 1;
    int STATUS_SLIDE_SCROLL = 2;

    void onSlide(View view, int status);
}

public void setOnSlideListener(OnSlideListener onSlideListener) {
    this.onSlideListener = onSlideListener;
}

@Override
public void onSlide(View view, int status) {
    // if last slide item is on, close it
    if (mLastSlideViewWithStatusOn != null && mLastSlideViewWithStatusOn != view) {
        mLastSlideViewWithStatusOn.shrink();
    }
    // if a new item slide out, mark it
    if (status == STATUS_SLIDE_ON) {
        mLastSlideViewWithStatusOn = (SlideItemView) view;
    }
}
```

这个接口的主要目的是对这个ListView中Item的管理，保证当前只有一个侧滑出来。

