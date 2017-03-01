package org.fitzeng.emojikeyboard.simpleemojikeyboard;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
import android.widget.LinearLayout;

import org.fitzeng.emojikeyboard.R;
import org.fitzeng.emojikeyboard.emoji.EmojiAdapter;
import org.fitzeng.emojikeyboard.emoji.EmojiManager;


public class SimpleEmojiKeyboardLayout extends LinearLayout {

    private GridView gridView = new GridView(getContext());

    public SimpleEmojiKeyboardLayout(Context context) {
        super(context);
        init();
    }

    public SimpleEmojiKeyboardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SimpleEmojiKeyboardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        addEmojiToKeyBoard();
        this.addView(gridView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    private void addEmojiToKeyBoard() {
        gridView.setNumColumns(8);
        gridView.setId(R.id.gridView);
        EmojiAdapter adapter = new EmojiAdapter(getContext(), EmojiManager.getSmileList());
        gridView.setAdapter(adapter);
    }


    /**
     * keyboard slip out
     */
    public void slipUp() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.getLayoutParams();
        layoutParams.height = (int) (getMeasuredWidth() * 0.7);
        this.setLayoutParams(layoutParams);
    }
}
