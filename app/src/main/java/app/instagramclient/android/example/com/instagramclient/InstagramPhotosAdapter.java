package app.instagramclient.android.example.com.instagramclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class InstagramPhotosAdapter extends ArrayAdapter<InstagramPhoto> {

    //what data do we need from the activity?
    //We need to pass in the Context, Data Source
    public InstagramPhotosAdapter(Context context, List<InstagramPhoto> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    //what our item looks like
    //use the template to display each photo
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //get the data item for this position.
        InstagramPhoto photo = getItem(position);

        //check if we are using a recycled view, if not we need to inflate the view
        if (convertView == null) {

            //create a new view from template
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photo, parent, false);

        }

        //lookup the views for populating the data, for example: (caption, image)
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        ImageView ivPhoto = (ImageView) convertView.findViewById(R.id.ivPhoto);

        //insert the model data into each of the view items (caption, image)

        //inserts the caption data from the model class into the TextView
        tvCaption.setText(photo.caption);

        //clear out the ImageView first
        ivPhoto.setImageResource(0);
        //insert the image url into the ImageView using picasso
        Picasso.with(getContext()).load(photo.imageUrl).into(ivPhoto);

        //Return the created item as a view
        return convertView;
    }
}