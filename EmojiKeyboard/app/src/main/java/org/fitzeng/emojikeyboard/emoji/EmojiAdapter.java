package org.fitzeng.emojikeyboard.emoji;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.List;


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
        layout.setTag(smileList.get(position));
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
