<!-- MarkdownTOC -->

- [BaiduAcademy](#baiduacademy)
	- [NO.4 实现一个表情键盘控件](#no4-实现一个表情键盘控件)
		- [任务目的](#任务目的)
		- [任务描述](#任务描述)
		- [结果演示](#结果演示)
		- [实现过程](#实现过程)

<!-- /MarkdownTOC -->


# [BaiduAcademy](https://github.com/mk43/BaiduAcademy)

## [NO.4 实现一个表情键盘控件](http://ife.baidu.com/course/detail/id/74)

### 任务目的

---

- 学习android控件的封装，viewPager的使用，以及SpannableString对文字加图片的处理

### 任务描述

---

- 能进行表情的输入，删除
- 表情大于一页，且可以翻页
- 表情键盘和输入法键盘的弹出逻辑要正确

### 结果演示

---

![](https://github.com/mk43/blogResource/blob/master/BaiduAcademy/EmojiKeyBoard/shou.png)
![](https://github.com/mk43/blogResource/blob/master/BaiduAcademy/EmojiKeyBoard/EmojiDemo.gif)

### 实现过程

---

一开始，其实也不知道从哪里下手，开始在网上浏览了一些博客，大概有了解决的思路，下面就从我个人的解决思路出发实现一个一个小功能。

- step 1: 设计总体布局

采用LinearLayout基本上是上(textView)中(func Bar)下(keyboard)的设计，为了方便控制，keyboard的布局采用自定义，布局代码如下。

```xml
<org.fitzeng.emojikeyboard.simpleemojikeyboard.SimpleEmojiEditLayout
    android:id="@+id/etSimpleEmojiEdit"
    android:hint="@string/typeing_something_here"
    android:maxLines="30"
    android:textSize="24sp"
    android:gravity="top"
    android:background="@null"
    android:layout_margin="8dp"
    android:layout_weight="1"
    android:layout_width="match_parent"
    android:layout_height="0dp" />

<RelativeLayout
    android:background="@color/colorPrimary"
    android:layout_width="match_parent"
    android:layout_height="50dp">
    
    <ImageView
        android:id="@+id/touchIcon"
        android:src="@mipmap/ic_launcher"
        android:layout_centerInParent="true"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:contentDescription="@string/an_icon_button" />

    <ImageView
        android:id="@+id/btnDelete"
        android:src="@drawable/icon_delete_normal"
        android:layout_alignParentRight="true"
        android:layout_centerVertical="true"
        android:layout_width="wrap_content"
        android:layout_height="@dimen/dp40"
        android:contentDescription="@string/delete" />
</RelativeLayout>

<org.fitzeng.emojikeyboard.simpleemojikeyboard.SimpleEmojiKeyboardLayout
    android:id="@+id/simpleEmojiKeyboardLayout"
    android:visibility="gone"
    android:layout_width="match_parent"
    android:layout_height="0dp">
</org.fitzeng.emojikeyboard.simpleemojikeyboard.SimpleEmojiKeyboardLayout>
```

- step 2：大概实现布局控件

设计好控件，就进行实现，通过对func Bar的设计，实现键盘的收缩功能。

![](https://github.com/mk43/blogResource/blob/master/BaiduAcademy/EmojiKeyBoard/slipDown.png)
![](https://github.com/mk43/blogResource/blob/master/BaiduAcademy/EmojiKeyBoard/slipUp.png)

```java
// Click icon to make keyboard hide or appear
touchIcon.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if (simpleEmojiKeyboardLayout.getVisibility() == View.GONE) {
            simpleEmojiKeyboardLayout.setVisibility(View.VISIBLE);
            simpleEmojiKeyboardLayout.slipUp();
        } else {
            simpleEmojiKeyboardLayout.setVisibility(View.GONE);
        }
    }
});

// Make keyboard appear when click to edit
simpleEmojiEditLayout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if (simpleEmojiKeyboardLayout.getVisibility() == View.GONE) {
            simpleEmojiKeyboardLayout.slipUp();
            simpleEmojiKeyboardLayout.setVisibility(View.VISIBLE);
        }
    }
});

/**
 * keyboard slip out
 */
public void slipUp() {
    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.getLayoutParams();
    layoutParams.height = (int) (getMeasuredWidth() * 0.7);
    this.setLayoutParams(layoutParams);
}
```

- step 3: 封装表情加入keyboard

将emoji封装成一个对象

```java
public class Smile {

    private int resId;
    private String info;

    public Smile(int resId, String info) {
        this.resId = resId;
        this.info = info;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
```

为了方便管理，把表情资源都添加进一个Manager类

```java
public class EmojiManager {
    private static List<Smile> smileList;
    private static Context context;

    public EmojiManager(Context context) {
        this.context = context;
    }

    public static List<Smile> getSmileList() {
        if(smileList==null){
            smileList=new ArrayList<>();
            smileList.add(new Smile(R.drawable.emotion_1001,"[e]1001[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1002,"[e]1002[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1003,"[e]1003[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1004,"[e]1004[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1005,"[e]1005[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1006,"[e]1006[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1007,"[e]1007[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1008,"[e]1008[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1009,"[e]1009[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1010,"[e]1010[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1011,"[e]1011[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1012,"[e]1012[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1013,"[e]1013[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1014,"[e]1014[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1015,"[e]1015[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1016,"[e]1016[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1017,"[e]1017[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1018,"[e]1018[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1019,"[e]1019[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1020,"[e]1020[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1021,"[e]1021[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1022,"[e]1022[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1023,"[e]1023[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1024,"[e]1024[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1025,"[e]1025[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1026,"[e]1026[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1027,"[e]1027[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1028,"[e]1028[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1029,"[e]1029[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1030,"[e]1030[/e]"));

            smileList.add(new Smile(R.drawable.emotion_1031,"[e]1031[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1032,"[e]1032[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1033,"[e]1033[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1034,"[e]1034[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1035,"[e]1035[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1036,"[e]1036[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1037,"[e]1037[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1038,"[e]1038[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1039,"[e]1039[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1040,"[e]1040[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1041,"[e]1041[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1042,"[e]1042[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1043,"[e]1043[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1044,"[e]1044[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1045,"[e]1045[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1046,"[e]1046[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1047,"[e]1047[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1048,"[e]1048[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1049,"[e]1049[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1050,"[e]1050[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1051,"[e]1051[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1052,"[e]1052[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1053,"[e]1053[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1054,"[e]1054[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1055,"[e]1055[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1056,"[e]1056[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1057,"[e]1057[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1058,"[e]1058[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1059,"[e]1059[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1060,"[e]1060[/e]"));

            smileList.add(new Smile(R.drawable.emotion_1061,"[e]1061[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1062,"[e]1062[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1063,"[e]1063[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1064,"[e]1064[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1065,"[e]1065[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1066,"[e]1066[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1067,"[e]1067[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1068,"[e]1068[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1069,"[e]1069[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1070,"[e]1070[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1071,"[e]1071[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1072,"[e]1072[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1073,"[e]1073[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1074,"[e]1074[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1075,"[e]1075[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1076,"[e]1076[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1077,"[e]1077[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1078,"[e]1078[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1079,"[e]1079[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1080,"[e]1080[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1081,"[e]1081[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1082,"[e]1082[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1083,"[e]1083[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1084,"[e]1084[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1085,"[e]1085[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1086,"[e]1086[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1087,"[e]1087[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1088,"[e]1088[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1089,"[e]1089[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1090,"[e]1090[/e]"));

            smileList.add(new Smile(R.drawable.emotion_1091,"[e]1091[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1092,"[e]1092[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1093,"[e]1093[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1094,"[e]1094[/e]"));
            smileList.add(new Smile(R.drawable.emotion_1095,"[e]1095[/e]"));
        }
        return smileList;
    }
}
```

表情资源基本准备好，现在要实现的功能是把表情添加进keyboard。可以通过GridView添加资源。

但是在添加资源时，需要借助适配器，所以还要自己构造一个表情的适配器，基本实现是将一个imageView(表情)添加进LinearLayout作为一个布局元素。

```java
public class EmojiAdapter extends BaseAdapter {

    private static List<Smile> smileList;
    private static Context context;

    public EmojiAdapter(Context context, List<Smile> list) {
        EmojiAdapter.context = context;
        smileList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(smileList.get(position).getResId());
        LinearLayout layout = new LinearLayout(context);
        layout.setGravity(Gravity.CENTER);
        layout.addView(imageView, dp2px(28), dp2px(28 + 20));
        return layout;
    }

    private static int dp2px(float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public int getCount() {
        return smileList.size();
    }

    @Override
    public Object getItem(int position) {
        return smileList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return smileList.get(position).getResId();
    }
}
```

构造好了资源适配器就可以往GridView中加载资源了，再把GridView添加进键盘这个视图

```java
private void addEmojiToKeyBoard() {
    gridView.setNumColumns(8);
    gridView.setId(R.id.gridView);
    EmojiAdapter adapter = new EmojiAdapter(getContext(), EmojiManager.getSmileList());
    gridView.setAdapter(adapter);
}

private void init() {
    addEmojiToKeyBoard();
    this.addView(gridView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
}
```

- step 4：实现编辑相关功能模块

现在基本UI已经实现，接下来就是实现对emoji的点击事件监听，从而实现在EditText中显示表情元素

表情的添加，在主Activity实现监听

参考:
- [在EditText中添加QQ表情](https://www.cnblogs.com/tianzhijiexian/p/3867749.html)
- [android EditText获取光标位置并插入字符删除字符](http://blog.csdn.net/centralperk/article/details/8548075)

```java
// Called by click an emoji
gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Calc lineHeight for ensure icon size
        int lineHeight = simpleEmojiEditLayout.getLineHeight();
        // Create a bitmap
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), (int) id);
        // Set width and height
        bitmap = Bitmap.createScaledBitmap(bitmap, lineHeight - 10, lineHeight - 10, true);
        //
        ImageSpan imageSpan = new ImageSpan(SimpleEmojiKeyboardActivity.this, bitmap);
        SpannableString spannableString = new SpannableString("e");
        spannableString.setSpan(imageSpan, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Get texts from textView
        content = simpleEmojiEditLayout.getText();
        // Get cursor location
        location = simpleEmojiEditLayout.getSelectionStart();
        // Insert text
        content.insert(location, spannableString);
    }
});
```

接下来实现func Bar的delete功能

```java
btndelete.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        content = simpleEmojiEditLayout.getText();
        location = simpleEmojiEditLayout.getSelectionStart();
        if (location != 0) {
            content.delete(location - 1, location);
        } else {
            Toast.makeText(SimpleEmojiKeyboardActivity.this, "Nothing to delete", Toast.LENGTH_SHORT).show();
        }
    }
});
```

到这一个简单的表情键盘就实现了，其中还有很多小bug需要优化，最后把文件名列出来熟悉下这个过程。

+ emoji
  - Smile
  - EmojiManager
  - EmojiAdapter
+ SimpleEmojiKeyboard
  - SimpleEmojiEditLayout
  - SimpleEmojiKeyboardLayout
  - SimpleEmojiKeyboardActivity



推荐阅读：
- [Android 软键盘和emoji表情切换方案，和微信几乎一样的体验](http://www.jianshu.com/p/328784b363b6#)
- [The handler for the keyboard and panel layout conflict in Android](https://github.com/Jacksgong/JKeyboardPanelSwitch)

有机会要写一个更加完善的^_^

Tips: 作为一个Android小白，自然有很多不知道不理解的地方。如果文中有错或者有哪些值得改进的地方，欢迎大家提意见，我很开心能和大家一起交流学习，共同进步。

多谢阅读