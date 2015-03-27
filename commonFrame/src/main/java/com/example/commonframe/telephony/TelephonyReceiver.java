package com.example.commonframe.telephony;

import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import com.example.commonframe.util.DLog;

public class TelephonyReceiver extends BroadcastReceiver {

	// The receiver will be recreated whenever android feels like it. We need a
	// static variable to remember data between instantiations
	private static final String TAG = "TelephonyReceiver";
	private static int lastState = TelephonyManager.CALL_STATE_IDLE;
	private static Date callStartTime;
	private static boolean isIncoming;
	private static String savedNumber; // because the passed incoming is only
										// valid in ringing

	private void onIncomingCallStarted(String number, Date start) {
		DLog.d(TAG, "onIncomingCallStarted " + number);
	}

	private void onOutgoingCallStarted(String number, Date start) {
		DLog.d(TAG, "onOutgoingCallStarted " + number);
	}

	private void onIncomingCallEnded(String number, Date start, Date end) {
		DLog.d(TAG, "onIncomingCallEnded " + number);
	}

	private void onOutgoingCallEnded(String number, Date start, Date end) {
		DLog.d(TAG, "onOutgoingCallEnded " + number);
	}

	private void onMissedCall(String number, Date start) {
		DLog.d(TAG, "onMissedCall " + number);
	}

	private void onReceivedSMS(String sender, String message, Date time) {
		DLog.d(TAG, "onReceivedSMS " + sender);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// We listen to three intents. The new
		// incoming sms
		// outgoing call only tells us of an
		// outgoing call. We use it to get the number.

		if (intent.getAction()
				.equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
			try {
				Bundle bundle = intent.getExtras();
				if (bundle != null) {

					final Object[] pdusObj = (Object[]) bundle.get("pdus");

					for (int i = 0; i < pdusObj.length; i++) {

						SmsMessage currentMessage = SmsMessage
								.createFromPdu((byte[]) pdusObj[i]);
						String phoneNumber = currentMessage
								.getDisplayOriginatingAddress();

						String senderNum = phoneNumber;
						String message = currentMessage.getDisplayMessageBody();
						onReceivedSMS(senderNum, message, new Date());
					}
				}
			} catch (Exception e) {
			}
		} else if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
			savedNumber = intent.getExtras().getString(
					Intent.EXTRA_PHONE_NUMBER);
		} else {
			String stateStr = intent.getExtras().getString(
					TelephonyManager.EXTRA_STATE);
			String number = intent.getExtras().getString(
					TelephonyManager.EXTRA_INCOMING_NUMBER);
			int state = 0;
			if (stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
				state = TelephonyManager.CALL_STATE_IDLE;
			} else if (stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
				state = TelephonyManager.CALL_STATE_OFFHOOK;
			} else if (stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
				state = TelephonyManager.CALL_STATE_RINGING;
			}
			onCallStateChanged(state, number);
		}
	}

	// Deals with actual events

	// Incoming call- goes from IDLE to RINGING when it rings, to OFFHOOK when
	// it's answered, to IDLE when its hung up
	// Outgoing call- goes from IDLE to OFFHOOK when it dials out, to IDLE when
	// hung up
	public void onCallStateChanged(int state, String number) {
		if (lastState == state) {
			// No change, debounce extras
			return;
		}
		switch (state) {
		case TelephonyManager.CALL_STATE_RINGING:
			isIncoming = true;
			callStartTime = new Date();
			savedNumber = number;
			onIncomingCallStarted(number, callStartTime);
			break;
		case TelephonyManager.CALL_STATE_OFFHOOK:
			// Transition of ringing->offhook are pickups of incoming calls.
			// Nothing done on them
			if (lastState != TelephonyManager.CALL_STATE_RINGING) {
				isIncoming = false;
				callStartTime = new Date();
				onOutgoingCallStarted(savedNumber, callStartTime);
			}
			break;
		case TelephonyManager.CALL_STATE_IDLE:
			// Went to idle- this is the end of a call. What type depends on
			// previous state(s)
			if (lastState == TelephonyManager.CALL_STATE_RINGING) {
				// Ring but no pickup- a miss
				onMissedCall(savedNumber, callStartTime);
			} else if (isIncoming) {
				onIncomingCallEnded(savedNumber, callStartTime, new Date());
			} else {
				onOutgoingCallEnded(savedNumber, callStartTime, new Date());
			}
			break;
		}
		lastState = state;
	}
}
