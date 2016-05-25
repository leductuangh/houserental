package com.example.houserental.function;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.houserental.R;
import com.example.houserental.function.model.DAOManager;

import java.util.Calendar;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import core.data.DataSaver;
import core.util.Constant;
import core.util.DLog;
import core.util.notification.NotificationController;

/**
 * Created by leductuan on 5/23/16.
 */
public class ReminderReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && intent.getAction().equals(Constant.REMINDER_ACTION)) {
            TimeZone.setDefault(new SimpleTimeZone(7, "Asia/Bangkok"));
            Calendar now = Calendar.getInstance();
            int dayCountInMonth = HouseRentalUtils.dayCountOfMonth(now.get(Calendar.MONTH), now.get(Calendar.YEAR));
            int currentDateOfMonth = now.get(Calendar.DAY_OF_MONTH);
            int dayLeft = dayCountInMonth - currentDateOfMonth;
            if (dayLeft >= 0 && dayLeft <= 3) {
                // push end of month
                String message = String.format(context.getResources().getString(R.string.reminder_push_day_left_message), dayLeft, (now.get(Calendar.MONTH) + 1));
                if (dayLeft == 0) {
                    message = String.format(context.getResources().getString(R.string.reminder_push_end_of_month_message), (now.get(Calendar.MONTH) + 1));
                }
                NotificationController.sendNotification(DataSaver.getInstance().nextNotificationId(), context.getResources().getString(R.string.reminder_push_end_of_month_title), message, new Intent(HouseRentalApplication.getContext(), MainActivity.class));
            }

            if (currentDateOfMonth == 3 || currentDateOfMonth == 4 || currentDateOfMonth == 5) {
                int unPaidRoomCount = DAOManager.getUnPaidRoomInMonth(now);
                if (unPaidRoomCount > 0) {
                    // push unpaid rooms of month
                    String message = String.format(context.getResources().getString(R.string.reminder_unpaid_room_message), unPaidRoomCount, (now.get(Calendar.MONTH) + 1));
                    NotificationController.sendNotification(DataSaver.getInstance().nextNotificationId(), context.getString(R.string.reminder_unpaid_room_title), message, new Intent(HouseRentalApplication.getContext(), MainActivity.class));
                }
            }
            DLog.d("ReminderReceiver", "receive broadcast");
        }
    }
}
