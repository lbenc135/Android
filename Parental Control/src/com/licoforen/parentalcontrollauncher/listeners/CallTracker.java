package com.licoforen.parentalcontrollauncher.listeners;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.licoforen.parentalcontrollauncher.Helpers.ResourceLoader;

public class CallTracker extends BroadcastReceiver {

	private static boolean inCall = false;
	TelephonyManager tmgr = null;

	@SuppressLint("CommitPrefEdits")
	@Override
	public void onReceive(Context context, Intent intent) {

		if (tmgr == null) {
			try {
				tmgr = (TelephonyManager) context
						.getSystemService(Context.TELEPHONY_SERVICE);

				tmgr.listen(new MyPhoneStateListener(),
						PhoneStateListener.LISTEN_CALL_STATE);

			} catch (Exception e) {
			}
		}

		if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
			ResourceLoader.addToLog("Outgoing call to number: \""
					+ intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER) + "\"");
		}
	}

	private class MyPhoneStateListener extends PhoneStateListener {
		public void onCallStateChanged(int state, String incomingNumber) {

			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				if (incomingNumber == null)
					incomingNumber = "Hidden number";
				if (!inCall) {
					ResourceLoader.addToLog("Incoming call from number: \""
							+ incomingNumber + "\"");
					inCall = true;
				}
				break;
			case TelephonyManager.CALL_STATE_IDLE:
				if(inCall)
					inCall = false;
				break;
			}
		}
	}
}
