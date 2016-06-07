package edu.stanford.cs247.stanfordmindfulnessapp;

/**
 * Created by peterwashington on 3/6/16.
 */

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import android.widget.ImageButton;

/**
 * Created by peterwashington on 3/6/16.
 */
public class MindfulnessListAdapter extends BaseAdapter {

    private Activity activity;
    private Context context;
    private LayoutInflater inflater;
    private ArrayList mData = new ArrayList();
    private ArrayList<String> suggestions = new ArrayList<String>();

    public MindfulnessListAdapter(Activity activity) {
        this.activity = activity;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int location) {
        return mData.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addText(final String text) {
        mData.add(text);
        suggestions.add(text);
        notifyDataSetChanged();
    }

    private void updateTheUI() {
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        int type = getItemViewType(position);
        final FragmentManager fragmentManager = activity.getFragmentManager();

        convertView = inflater.inflate(R.layout.item_activity, null);
        final Button suggestion = (Button) convertView.findViewById(R.id.suggestion);

        final ImageButton cancelButton = (ImageButton) convertView.findViewById(R.id.delete_button);

        if (UserStatus.exercises.size() < 1)
            cancelButton.setVisibility(View.INVISIBLE);

        final String feedbackString = mData.get(position).toString();
        suggestion.setText(feedbackString);

        suggestion.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (UserStatus.exercises.size() >= 1) UserStatus.exercises.remove(position);
                Intent myIntent = new Intent(activity, MainApplicationPage.class);
                activity.startActivity(myIntent);
                activity.finish();
            }
        });

        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

}
