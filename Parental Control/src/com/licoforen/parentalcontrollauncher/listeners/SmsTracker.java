package com.licoforen.parentalcontrollauncher.listeners;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.licoforen.parentalcontrollauncher.Helpers.ResourceLoader;

public class SmsTracker extends BroadcastReceiver {

	@SuppressLint("CommitPrefEdits")
	@Override
	public void onReceive(Context c, Intent intent) {
		final Bundle bundle = intent.getExtras();

		try {
			if (bundle != null) {
				final Object[] pdusObj = (Object[]) bundle.get("pdus");

				for (int i = 0; i < pdusObj.length; i++) {
					SmsMessage currentMessage = SmsMessage
							.createFromPdu((byte[]) pdusObj[i]);
					String phoneNumber = currentMessage
							.getDisplayOriginatingAddress();

					String message = currentMessage.getDisplayMessageBody();

					ResourceLoader.addToLog("SMS message from: " + phoneNumber + "\nMessage: "
							+ message);
				}
			}
		} catch (Exception e) {
		}

	}
}