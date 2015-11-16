package group9.android_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Fetes on 2015-11-16.
 */
public class CustomUsersAdapter extends ArrayAdapter<User> {
    public CustomUsersAdapter(Context context, ArrayList<User> users) {
        super(context,0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        User user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user, parent, false);
        }
        // Lookup view for data population
        TextView tvFirstname = (TextView) convertView.findViewById(R.id.tvFirstname);
        TextView tvLastname = (TextView) convertView.findViewById(R.id.tvLastname);
        TextView tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
        // Populate the data into the template view using the data object
        tvFirstname.setText(user.firstname);
        tvLastname.setText(user.lastname);
        tvUsername.setText(user.username);
        // Return the completed view to render on screen
        return convertView;
    }
}
