package org.fitzeng.slidedeletelist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

class ListViewAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context context;
    private List<ItemMsg> itemMsgList;

    ListViewAdapter(Context context, List<ItemMsg> itemMsgList) {
        this.itemMsgList = itemMsgList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override

    public int getCount() {
        return itemMsgList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemMsgList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
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
}
