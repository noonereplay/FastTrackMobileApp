package com.kmutnb.fasttrack;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ProfileItemAdapter extends ArrayAdapter<ProfileItem>{


	private final Context context;
    private final List<ProfileItem>  itemsArrayList;
   
	public ProfileItemAdapter(Context context, List<ProfileItem> itemsArrayList) {
		super(context, R.layout.profile_item, itemsArrayList);

        this.context = context;
        this.itemsArrayList = itemsArrayList;
	}
	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.profile_item, parent, false);

        // 3. Get the two text view from the rowView
        TextView labelView = (TextView) rowView.findViewById(R.id.profile_item_text);
        TextView valueView = (TextView) rowView.findViewById(R.id.profile_sub_item_text);

        // 4. Set the text for textView
        labelView.setText(itemsArrayList.get(position).getItem_title());
        valueView.setText(itemsArrayList.get(position).getItem_value());

        // 5. retrn rowView
        return rowView;
    }


	
	

}
