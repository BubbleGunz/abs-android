package group9.android_project;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Halling- on 2015-11-20.
 */

    class CustomUsersAdapter extends ArrayAdapter<User> {
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
            Button btnDelete = (Button) convertView.findViewById(R.id.btnRemoveFriend);


            // Populate the data into the template view using the data object
            tvFirstname.setText(user.firstname);
            tvLastname.setText(user.lastname);
            tvUsername.setText(user.username);


            // Return the completed view to render on screen
            return convertView;
        }
    }

    class CustomVacationsAdapter extends ArrayAdapter<Vacation> {
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

    class CustomMemoriesAdapter extends ArrayAdapter<Memory> {
        public CustomMemoriesAdapter(Context context, ArrayList<Memory> memories) {
            super(context,0, memories);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Memory memory = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_vacation, parent, false);
            }

            // Lookup view for data population
            TextView tvVacationName = (TextView) convertView.findViewById(R.id.tvVacationname);
            // Populate the data into the template view using the data object
            tvVacationName.setText(memory.title);

            // Return the completed view to render on screen
            return convertView;
        }
    }
    class CustomMediaAdapter extends ArrayAdapter<Media> {
        public CustomMediaAdapter(Context context, ArrayList<Media> media) {
            super(context,0, media);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            Media media = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_vacation, parent, false);
            }

            // Lookup view for data population
            TextView tvVacationName = (TextView) convertView.findViewById(R.id.tvVacationname);
            ImageView ivImage = (ImageView) convertView.findViewById(R.id.ivImage);

            tvVacationName.setVisibility(View.INVISIBLE);
            // Populate the data into the template view using the data object
            tvVacationName.setText(media.container);
            ivImage.setImageBitmap(media.bitmap);


            // Return the completed view to render on screen
            return convertView;
        }
    }

