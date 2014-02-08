package com.bravo.bravoclient.adapters;

import java.util.HashMap;
import java.util.List;

import com.bravo.bravoclient.model.Card;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class CardsListAdapter extends ArrayAdapter<Card>{

	private HashMap<Card, Integer> mIdMap = new HashMap<Card, Integer>();

	public CardsListAdapter(Context context, int resource,
			int textViewResourceId, List<Card> objects) {
		super(context, resource, textViewResourceId, objects);
		for(int i = 0; i < objects.size(); i++) {
			mIdMap.put(objects.get(i), i);
		}
	}
	
	@Override
	public long getItemId(int position) {
		Card item = getItem(position);
		return mIdMap.get(item);
	}
}
