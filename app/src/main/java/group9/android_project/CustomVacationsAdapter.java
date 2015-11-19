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
public class CustomVacationsAdapter extends ArrayAdapter<Vacation> {
    public CustomVacationsAdapter(Context context, ArrayList<Vacation> vacations) {
        super(context,0, vacations);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Vacation vacation = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_vacation, parent, false);
        }

        // Lookup view for data population
        TextView tvVacationName = (TextView) convertView.findViewById(R.id.tvVacationname);
        // Populate the data into the template view using the data object
        tvVacationName.setText(vacation.title);



        // Return the completed view to render on screen
        return convertView;
    }
}
