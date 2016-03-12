package com.example.phoebevu.gorepresent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by phoebevu on 3/8/16.
 */
public class mArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;
    private final JSONArray repList;
    private final String[] bioId;

    public mArrayAdapter(Context context, String[] values, JSONArray repList, String[] bio) {
        super(context, R.layout.list_elem, values);
        this.context = context;
        this.values = values;
        this.repList = repList;
        this.bioId = bio;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_elem, parent, false);

        // Find all the fields
        TextView name = (TextView) rowView.findViewById(R.id.name);
        TextView party = (TextView) rowView.findViewById(R.id.party);
        TextView email = (TextView) rowView.findViewById(R.id.email);
        TextView web = (TextView) rowView.findViewById(R.id.website);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        // Get info from JSON object
        if (values != null) {

            try {

                JSONObject repInfo = repList.getJSONObject(position);
                if (repInfo.getString("chamber").equalsIgnoreCase("senate")) {
                    name.setText("Sen. " + values[position]);
                } else {
                    name.setText("Rep." + values[position]);
                }

                String p = repInfo.getString("party");
                if (p.equalsIgnoreCase("D")) {party.setText("Democrat");}
                else if (p.equalsIgnoreCase("R")) {party.setText("Republican");}
                else {party.setText("Other");}
                email.setText(repInfo.getString("oc_email"));
                web.setText(repInfo.getString("website"));

                String path = "https://theunitedstates.io/images/congress/225x275/" +
                        repInfo.getString("bioguide_id") + ".jpg";
//                Picasso.with(context).load(path).placeholder(R.drawable.usericon).error(R.drawable.usericon).into(imageView);
                new Photo_politicians((ImageView) imageView).execute(path);

            } catch (Exception e) {

            }
        }



        return rowView;
    }
}
