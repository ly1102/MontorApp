package com.cuit.monitorapp.main.cuit.monitorapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.cuit.monitorapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by syche on 2018/5/18.
 */

public class FollowListAdapter extends ArrayAdapter<AVObject> {
    private final int ITEM_LAYOUT_ID;
    public FollowListAdapter(@NonNull Context context, int resource, @NonNull List<AVObject> objects) {
        super(context, resource, objects);
        ITEM_LAYOUT_ID = resource;
    }

    @NonNull
    public View getView(int position, View convert, ViewGroup parent) {
        FollowViewHolder viewHolder;
        if (convert == null) {
            convert = LayoutInflater.from(getContext()).inflate(R.layout.main_list_item, parent, false);
            viewHolder = new FollowViewHolder();
            viewHolder.setName((TextView) convert.findViewById(R.id.name));
            viewHolder.setGender((TextView) convert.findViewById(R.id.gender));
            viewHolder.setCreateDate((TextView) convert.findViewById(R.id.date));
            convert.setTag(viewHolder);
        } else {
            viewHolder = (FollowViewHolder) convert.getTag();
        }
        AVObject follow = getItem(position);
        assert follow != null;
        viewHolder.getName().setText(follow.getString("name"));
        viewHolder.getGender().setText(follow.getString("gender"));
        DateFormat dateFormat =  new SimpleDateFormat("yyyy-MM-dd");
        viewHolder.getCreateDate().setText(dateFormat.format(follow.getCreatedAt()));
        return convert;
    }
}
