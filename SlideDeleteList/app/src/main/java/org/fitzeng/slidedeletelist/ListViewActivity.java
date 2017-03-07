package org.fitzeng.slidedeletelist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ListViewActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener, SlideItemView.OnSlideListener, View.OnClickListener{

    private ListView listView;
    private ListViewAdapter adapter;
    private List<ItemMsg> itemMsgList;
    private SlideItemView mLastSlideViewWithStatusOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        initView();
    }

    private void initView() {
        // add data
        listView = (ListView) findViewById(R.id.listView);
        itemMsgList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            ItemMsg msg = new ItemMsg();
            msg.setContent("Item " + i);
            itemMsgList.add(msg);
        }
        // set adapter and listener
        adapter = new ListViewAdapter(this, itemMsgList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.delete) {
            int position = listView.getPositionForView(v);
            if (position != ListView.INVALID_POSITION) {
                itemMsgList.remove(position);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // setting item click event
    }
}
