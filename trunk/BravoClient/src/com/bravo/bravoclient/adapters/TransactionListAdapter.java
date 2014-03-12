package com.bravo.bravoclient.adapters;

import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;

import com.bravo.bravoclient.model.Transaction;

import android.content.Context;
import android.widget.ArrayAdapter;

public class TransactionListAdapter extends ArrayAdapter<Transaction>{

	private HashMap<Transaction, Integer> mIdMap = new HashMap<Transaction, Integer>();

	public TransactionListAdapter(Context context, int resource,
			int textViewResourceId, List<Transaction> objects) {
		super(context, resource, textViewResourceId, objects);
		for(int i = 0; i < objects.size(); i++) {
			mIdMap.put(objects.get(i), i);
		}
	}
	
	@Override
	public long getItemId(int position) {
		Transaction item = getItem(position);
		return mIdMap.get(item);
	}
}
