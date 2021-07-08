package com.buyorsell.sastobook.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import com.buyorsell.sastobook.R;
import com.buyorsell.sastobook.model.Messages;

import java.util.List;

public class MessageList extends ArrayAdapter<Messages> {
    private Activity context;
    List<Messages> artists;
    public MessageList(Activity context, List<Messages> artists) {
        super(context, R.layout.single_view_user, artists);
        this.context = context;
        this.artists = artists;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.single_view_user, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.EmailTV);

        Messages artist = artists.get(position);
        textViewName.setText(artist.getEmail());

        return listViewItem;
    }

}
