package com.bravo.bravomerchant.util;

import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.widget.Toast;

public class NFCHandler {
	public static String readFromDevice(Intent intent) {
		Parcelable[] rawNFCMsg = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        // only one message sent during the beam
        NdefMessage msg = (NdefMessage) rawNFCMsg[0];
        // record 0 contains the MIME type, record 1 is the AAR, if present
        return new String(msg.getRecords()[0].getPayload());
	}
	
	public static void checkNFCAvailability(Context context) {
		// Check for available NFC Adapter
        NfcAdapter mNfcAdapter = NfcAdapter.getDefaultAdapter(context);
        if (mNfcAdapter == null) {
            Toast.makeText(context, "NFC is not available", Toast.LENGTH_LONG).show();
        }
	}

}
