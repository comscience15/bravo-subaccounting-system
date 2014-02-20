package com.bravo.bravoclient.common;

import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;

public class CommonViewHandler {
	
	/**
	 * Get the value of a edit text field
	 * @param field
	 * @return
	 */
	public static String getEditTextValue(EditText field) {
		return field == null ? "" : field.getText().toString(); 
	}
	
	/**
	 * The method is going to get the value of spinner field
	 * @param field
	 * @return
	 */
	public static String getSpinnerValue(Spinner field) {
		return field == null ? "" : field.getSelectedItem().toString();
	}
	
	public static String getNumebrPickerValue(NumberPicker numberPicker) {
		int value = numberPicker.getValue();
		if (numberPicker == null) return "";
		if (value < 10) {
			return "0" + String.valueOf(value);
		} else {
			return String.valueOf(value);
		}
	}

}
