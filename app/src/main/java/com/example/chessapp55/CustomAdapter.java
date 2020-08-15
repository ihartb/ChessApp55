package com.example.chessapp55;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ChessModel.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class CustomAdapter extends ArrayAdapter<SavedGames> {
    private Activity activity;
    private ArrayList<SavedGames> games;
    private static LayoutInflater inflater = null;

    public CustomAdapter(Activity activity, int textViewResourceId, ArrayList<SavedGames> _games) {
        super(activity, textViewResourceId, _games);
        try {
            this.activity = activity;
            this.games = _games;

            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        } catch (Exception e) {

        }
    }

    public int getCount() {
        return games.size();
    }

    public SavedGames getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        public TextView display_name;
        public TextView display_date;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;
        try {
            if (convertView == null) {
                vi = inflater.inflate(R.layout.customlayout, null);
                holder = new ViewHolder();
                holder.display_name = (TextView) vi.findViewById(R.id.nameText);
                holder.display_date = (TextView) vi.findViewById(R.id.dateText);


                vi.setTag(holder);
            } else {
                holder = (ViewHolder) vi.getTag();
            }
            SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);


            holder.display_name.setText(games.get(position).nombre);
            holder.display_date.setText(sdf.format(games.get(position).date.getTime()));

        } catch (Exception e) {



        }
        return vi;
    }
}
