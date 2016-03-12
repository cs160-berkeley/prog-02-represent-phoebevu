package com.example.phoebevu.gorepresent;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by phoebevu on 3/10/16.
 */
public class tabArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;
    private final String[] date;

    public tabArrayAdapter(Context context, String[] values, String[] date) {
        super(context, R.layout.bills, values);
        this.context = context;
        this.values = values;
        this.date = date;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.bills, parent, false);

        // Find all fields
        TextView name = (TextView) rowView.findViewById(R.id.bName);
        TextView d = (TextView) rowView.findViewById(R.id.date);
        if (values != null) {
            name.setText(values[position]);
        }
        if (date != null) {
            d.setText("Date: " + date[position]);
        }

        return rowView;
    }
}
