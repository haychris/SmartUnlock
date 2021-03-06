package hay.chris.smartunlock;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.widget.CompoundButton;


public class DeviceAdmin extends DeviceAdminReceiver {

	
	public DeviceAdmin() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void onEnabled(Context context, Intent intent){
		super.onEnabled(context, intent);
		//setResult(Activity.RESULT_OK, null, null);
//		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
//		SharedPreferences.Editor editor = settings.edit();
//		editor.putBoolean("enable_device_admin", true);
//		editor.commit();
//		PreferenceManager.findPreference("enable_device_admin");
//		CompoundButton b = (CompoundButton) findViewById();
	}

	@Override
	public void onDisabled(Context context, Intent intent){
		super.onDisabled(context, intent);
	}
	@SuppressLint("NewApi")
	@Override
	public void onPasswordSucceeded(Context context, Intent intent) {
		SharedPreferences prefs = context.getSharedPreferences("hay.chris.smartunlock.condition_storage", 0);
		Set<String> activeConditionSets = prefs.getStringSet("active_condition_sets", new HashSet<String>());
		ArrayList<ConditionObject> conditions = new ArrayList<ConditionObject>();
		//get all active sets
		for (String s : activeConditionSets){
			Set<String> curConditionSet = prefs.getStringSet(s, new HashSet<String>());
			boolean hasTimer = false;
			//get all active conditions within each set 
			for (String con : curConditionSet) {
				ConditionObject c = ConditionObject.loadThatCondition(context, con);
				if (c.getTypeInt() == 0 ) {
					ConditionObject.TimerObject data = (ConditionObject.TimerObject) c.getData();
					if (data.radio == 0) {
						conditions.add(c);
						hasTimer = true;
					}
				}
			}
			if (!hasTimer)
				activeConditionSets.remove(s);
		}
		//activeConditionSets now only contains sets with a timer in them that triggers at unlock
		//and conditions only has such timers
		for (ConditionObject c : conditions) {
			//TODO: handle multiple timers to get shortest
			Set<String> curSet = prefs.getStringSet(c.getSetName(), new HashSet<String>());
			for (String s : curSet){
				ConditionObject con = ConditionObject.loadThatCondition(context, s);
				if (con.getTypeInt() != 0){
					//TODO: validate other conditions
				}
			}
			ConditionObject.TimerObject data = (ConditionObject.TimerObject) c.getData();
			int timeInMillis = data.time;
			switch (data.timeType) {
				case 0: timeInMillis *= 60;
				case 1: timeInMillis *= 60;
				case 2: timeInMillis *= 1000;
				break;
			}
			getManager(context).resetPassword("", 0);
			//creating alarm with intent
	        AlarmManager alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
	        Intent in = new Intent(context, LockUnReceiver.class);
	        in.putExtra("toLock", true);
	        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, in, 0);
	        if (Build.VERSION.SDK_INT >= 19)
	        	alarmMgr.setWindow(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + timeInMillis, timeInMillis/10, alarmIntent);
	        else alarmMgr.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + timeInMillis, alarmIntent);
		}
	}
	@Override
	public void onPasswordChanged(Context context, Intent intent) {
		
	}
}
