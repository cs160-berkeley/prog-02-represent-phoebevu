package com.example.phoebevu.gorepresent;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by phoebevu on 3/10/16.
 */
public class cArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;


    public cArrayAdapter(Context context, String[] values) {
        super(context, R.layout.coms, values);
        Log.d("cARRAY ADAPT:", "VAL:" + values[0] + " & " + values[1]);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.coms, parent, false);

        // Find all fields
        TextView name = (TextView) rowView.findViewById(R.id.cName);
        TextView d = (TextView) rowView.findViewById(R.id.date);
        if (values != null) {
            name.setText(values[position]);
        }

        return rowView;
    }
}