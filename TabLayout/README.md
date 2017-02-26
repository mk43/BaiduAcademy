<!-- MarkdownTOC -->

- [BaiduAcademy](#baiduacademy)
    - [NO.3 实现一个TAB布局](#no3-实现一个tab布局)
        - [任务描述](#任务描述)
        - [实现效果](#实现效果)
        - [实现过程](#实现过程)
        - [不足](#不足)

<!-- /MarkdownTOC -->


# [BaiduAcademy](https://github.com/mk43/BaiduAcademy)

## [NO.3 实现一个TAB布局](http://ife.baidu.com/course/detail/id/73)

### 任务描述

---

实现常见的标签tab，并且点击tab跳转至对应页面
实现页面滑动效果

### 实现效果

---
TabLayout + ViewPager（GIF）

![](https://github.com/mk43/blogResource/blob/master/BaiduAcademy/TabLayout/TabLayoutAndViewPager.gif)

### 实现过程

---
- 绘制页面
```xml
<android.support.v4.view.ViewPager
    android:id="@+id/viewPager"
    android:scrollbars="none"
    android:layout_weight="8"
    android:layout_width="match_parent"
    android:layout_height="0dp">
</android.support.v4.view.ViewPager>

<android.support.design.widget.TabLayout
    android:id="@+id/tabLayout"
    android:layout_weight="1"
    android:layout_width="match_parent"
    android:layout_height="0dp"
    app:tabGravity="fill"
    app:tabIndicatorHeight="0dp"
    app:tabMode="fixed"
    app:tabSelectedTextColor="#FF4081"
    app:tabTextColor="#000">
</android.support.design.widget.TabLayout>
```
需要在配置文件中添加 compile 'com.android.support:design:23.2.0'

- 准备Adapter
```java
class ViewPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> fragmentTitleList = new ArrayList<>();

    ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    void addPage(Fragment fragment, String title) {
        fragmentList.add(fragment);
        fragmentTitleList.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentTitleList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitleList.get(position);
    }
}
```
- 准备Fragment布局
```java
public class ViewPagerFragment extends Fragment {

    private String content;

    public ViewPagerFragment(String content) {
        this.content = content;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_view_pager, container, false);
        TextView tvContent = (TextView) rootView.findViewById(R.id.tvContent);
        tvContent.setText(content);
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
```
```xml
<TextView
    android:id="@+id/tvContent"
    android:textSize="30sp"
    android:hint="@string/default_text"
    android:layout_gravity="center"
    android:textAlignment="center"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content" />
```
为了方便，此处所有Fragment共用一个

- 初始化布局
```java
/**
 * Init Layout, Load Fragments and draw Layouts.
 */
private void initLayout() {
    viewPager = (ViewPager) findViewById(R.id.viewPager);
    tabLayout = (TabLayout) findViewById(R.id.tabLayout);

    ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
    // Add Fragments
    for (int i = 0; i < 4; i++) {
        adapter.addPage(new ViewPagerFragment("PAGE" + i), "PAGE" + i);
    }
    viewPager.setAdapter(adapter);
    // Connect TabLayout and ViewPage
    tabLayout.setupWithViewPager(viewPager);

    // Draw Tab View
    for (int i = 0; i < 4; i++) {
        tabListView.add(tabLayout.getTabAt(i));
        if (i == 0) {
            tabListView.get(i).setIcon(R.mipmap.selected_icon);
        } else {
            tabListView.get(i).setIcon(R.mipmap.ic_launcher);
        }
        tabListView.get(i).setText("PAGE" + i);
    }
}
```
- 设置监听
```java
/**
 * Setting Tab Click Listener
 */
private void initEvents() {
    tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            // Called when the tab was selected
            tabListView.get(tab.getPosition()).setIcon(R.mipmap.selected_icon);
            viewPager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            // Called when a tab from selected switch to unselected
            tabListView.get(tab.getPosition()).setIcon(R.mipmap.ic_launcher);
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            // Called when a tab selected more than twice consecutively
        }
    });
}
```

### 不足

---

利用控件已有的性质，基本的Tab切换已经实现。但是没有自己手动实现滑动等控件的效果。实现方式有很多种，以后来补充。








Tips: 作为一个Android小白，自然有很多不知道不理解的地方。如果文中有错或者有哪些值得改进的地方，欢迎大家提意见，我很开心能和大家一起交流学习，共同进步。

多谢阅读