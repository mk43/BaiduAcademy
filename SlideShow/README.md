<!-- MarkdownTOC -->

- [BaiduAcademy](#baiduacademy)
	- [NO.5 实现一个自动无线循环轮播图](#no5-实现一个自动无线循环轮播图)
		- [任务描述](#任务描述)
		- [实现效果](#实现效果)
		- [实现过程](#实现过程)
		- [不足](#不足)

<!-- /MarkdownTOC -->


# [BaiduAcademy](https://github.com/mk43/BaiduAcademy)

## [NO.5 实现一个自动无线循环轮播图](http://ife.baidu.com/course/detail/id/75)

### 任务描述

---

每隔3秒轮播图自动滚动
触摸改轮播图的时候，轮播图停止自动滚动
轮播图能够循环滚动，并且第一幅图向左滚动的效果和其他图片滚动的效果要求一致。
同理，最后一幅图向右滚动的效果也要求和其他图片的效果一致
demo的轮播图至少包含3幅图

### 实现效果

---

![](https://github.com/mk43/blogResource/blob/master/BaiduAcademy/SlideShow/SlideShowDemo.gif)

### 实现过程

---
- 界面设计

首先要知道我们的UI界面是怎么样的，此处的设计如下图所示

![](https://github.com/mk43/blogResource/blob/master/BaiduAcademy/SlideShow/UI.png)

代码：

```xml
<android.support.v4.view.ViewPager
    android:id="@+id/vpWallpaper"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

</android.support.v4.view.ViewPager>

<LinearLayout
    android:id="@+id/dotLayout"
    android:gravity="center"
    android:orientation="horizontal"
    android:layout_alignParentBottom="true"
    android:layout_marginBottom="28dp"
    android:layout_centerInParent="true"
    android:layout_width="match_parent"
    android:layout_height="50dp">
</LinearLayout>
```
- 绘制UI(先设计滚动的页面，在设计指示器)

要给ViewPager添加图片，就要设置适配器和资源

```java
/**
 * Set ViewPager Fragment
 */
public static class ViewPagerFragment extends Fragment {
    private ImageView image;
    private int id;

    public ViewPagerFragment(int id) {
        this.id = id;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_adapter, container, false);
        image  = (ImageView) rootView.findViewById(R.id.image);
        image.setImageResource(id);
        return rootView;
    }
}

// fragement layout(fragment_adapter)
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="horizontal"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <ImageView
        android:id="@+id/image"
        android:scaleType="fitXY"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:contentDescription="@string/image" />
</LinearLayout>

/**
 * Set ViewPager Adapter
 */
private class ViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList = new ArrayList<>();

    // Add pages to ViewPager
    private void addPage(Fragment fragment) {
        fragmentList.add(fragment);
    }

    ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
```

- 添加资源

```java
private static final int[] srcId = {
        R.drawable.pic0,
        R.drawable.pic1,
        R.drawable.pic2,
        R.drawable.pic3,
        R.drawable.pic4,
        R.drawable.pic5,
        R.drawable.pic6
};


ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
// Add Fragments
for (int i = 0; i < 7; i++) {
    ViewPagerFragment fragment = new ViewPagerFragment(srcId[i]);
    adapter.addPage(fragment);
}
viewPager.setAdapter(adapter);
```

- 设置定时效果

这里就是难点，要想实现两端的平缓循环滚动可以在两端添加一个cache page，[ 0 ( 1 2 3 4 0) 1]，只显示（）中的页，但是一旦出现cache page立马进行跳转。这样就能实现循环。
接下来就是设置计时器了。

```java
/**
 * Set page change timer
 */
@RequiresApi(api = Build.VERSION_CODES.KITKAT)
private void setTimer() {
    timer = new Timer();
    timer.schedule(new TimerTask() {
        @Override
        public void run() {
            handler.sendEmptyMessage(viewPager.getCurrentItem());
        }
    }, 2000, 2000);
}

private Handler handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5: {
                viewPager.setCurrentItem(msg.what + 1, true);
                break;
            }
            // To make start or end slip smoothly
            case 0: {
                viewPager.setCurrentItem(5, false);
                viewPager.setCurrentItem(6, true);
                break;
            }
            case 6: {
                viewPager.setCurrentItem(1, false);
                viewPager.setCurrentItem(2, true);
                break;
            }
            default:
                break;
        }
    }
};
```

- 设置平缓的滑动效果
但是这种效果不能满足要求,w我们可以设计一个滑动延时

```java
/**
 * Set Duration
 */
@RequiresApi(api = Build.VERSION_CODES.KITKAT)
private void setDuration() {
    try {
        Field field = ViewPager.class.getDeclaredField("mScroller");
        field.setAccessible(true);
        FixSpeedScroller scroller = new FixSpeedScroller(this, new AccelerateInterpolator());
        scroller.setDuration(500);
        field.set(viewPager, scroller);
    } catch (NoSuchFieldException | IllegalAccessException e) {
        e.printStackTrace();
    }
}


/**
 * Set scroller duration, make it smoothly
 */
class FixSpeedScroller extends Scroller {

    private int _duration = 1000;

    FixSpeedScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, _duration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, _duration);
    }

    public void setDuration(int duration) {
        _duration = duration;
    }
}
```

基本效果就已经实现了，但是如果有触摸事件发生，我们要另行处理

- 添加点击监听

```java
viewPager.setOnTouchListener(new View.OnTouchListener() {
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // Cancel the timer
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        // when slip out cache view(page0, page6), set an another same page instantly
        if (viewPager.getCurrentItem() == 0) {
            viewPager.setCurrentItem(5, false);
        }
        if (viewPager.getCurrentItem() == 6) {
            viewPager.setCurrentItem(1, false);
        }
        return false;
    }
});
```

在滑动监听中恢复计时

```java
viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onPageSelected(int position) {
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onPageScrollStateChanged(int state) {
        // if this page was showed by figure slip, then set timer to continue auto slip for next page
        if (timer == null) {
            setTimer();
        }
    }
});
```

ViewPager的效果已经实现。下面开始实现指示器

- 指示器UI

利用两张图片实现选中和没选中效果，每张图片添加进一个LinearLayout，把所有的LinearLayout排列经另一个LinearLayout就可以实现效果

值得注意的是，在添加视图时，要加判断

```java
if (viewSelected.getParent() != null) {
    ((ViewGroup) viewSelected.getParent()).removeView(viewSelected);
}

```

否则在有父布局的情况下添加不进别的视图。

```java
/**
 * Create a dot view for an select view
 * @return view
 */
private View createViewSelected() {
    LinearLayout view = new LinearLayout(this);

    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    lp.setMargins(10, 0, 10, 0);
    view.setLayoutParams(lp);

    ImageView imageView = new ImageView(this);
    imageView.setImageResource(R.drawable.dot_selected);
    imageView.setLayoutParams(new LinearLayout.LayoutParams(30, 30));
    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
    view.addView(imageView);
    return view;
}

/**
 * Create a dot view for an unselected view
 * @return view
 */
private View createViewUnselected() {
    LinearLayout view = new LinearLayout(this);

    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    lp.setMargins(10, 0, 10, 0);
    view.setLayoutParams(lp);

    ImageView imageView = new ImageView(this);
    imageView.setImageResource(R.drawable.dot_unselect);
    imageView.setLayoutParams(new LinearLayout.LayoutParams(30, 30));
    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
    view.addView(imageView);
    return view;
}

/**
 * Refresh the Dot View when sliped
 * @param selectId  current Id
 */
private void refreshDotView(int selectId) {
    dotLayout.removeAllViews();
    for (int i = 1; i < 6; i++) {
        if (selectId != i) {
            View viewUnselect = createViewUnselected();
            if (viewUnselect.getParent() != null) {
                ((ViewGroup) viewUnselect.getParent()).removeView(viewUnselect);
            }
            dotLayout.addView(viewUnselect);
        } else {
            View viewSelected = createViewSelected();
            if (viewSelected.getParent() != null) {
                ((ViewGroup) viewSelected.getParent()).removeView(viewSelected);
            }
            dotLayout.addView(viewSelected);
        }
    }
}

```

- 实现指示器功能

UI实现了之后就是实现指示功能
就是直接在PageChange监听处加码就可以了，逻辑很简单，不过注意在恶劣环境下有可能会滑到cache page，这是传入的page ID要进行相应的变换

```java
// Delay to change dotview
Timer timer = new Timer();
timer.schedule(new TimerTask() {
    @Override
    public void run() {
        handler.sendEmptyMessage(DOT_VIEW_CHANGE_SELECTED);
    }
}, 500);


// Refresh the dotView when page was loaded
case DOT_VIEW_CHANGE_SELECTED:
    int currentId = viewPager.getCurrentItem();
    switch (currentId) {
        case 0: {
            refreshDotView(5);
            break;
        }
        case 1:
        case 2:
        case 3:
        case 4:
        case 5: {
            refreshDotView(currentId);
            break;
        }
        case 6: {
            refreshDotView(1);
            break;
        }
        default:
            break;
    }
```

- onCreate()调用顺序如下

```java
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_wallpaper);

    init();

    listener();
    setTimer();
    setDuration();
}
```

### 不足

不足比较多，在极端的操作下不流畅，比如在设置的延时中滑动次数过多会造成切换不自然，在手指滑动时更好的做法是直接监听手机的移动速度进行相关图片的移动。还有就是耗内存，内存溢出的话，可以自己在Manifest文件加两句代码

```xml
android:hardwareAccelerated="false"
android:largeHeap="true"
```