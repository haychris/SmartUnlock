package hay.chris.smartunlock;

import android.app.Activity;
import android.app.admin.DeviceAdminReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
}
