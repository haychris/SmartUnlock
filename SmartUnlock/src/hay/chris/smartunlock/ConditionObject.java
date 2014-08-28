package hay.chris.smartunlock;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.Serializable;
import java.io.StreamCorruptedException;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Base64OutputStream;
import android.util.Log;

public class ConditionObject implements Serializable{
	private String name;
	private int type;
	private Object data;
	
	private class TimerObject implements Serializable{
		private int time;
		private int timeType;
		private boolean alwaysUnlocked;
		TimerObject() {}
	}
	private class BluetoothObject implements Serializable{
		BluetoothObject() {}
	}
	private class WifiObject implements Serializable{
		WifiObject() {}
	}
	private class LocationObject implements Serializable{
		LocationObject(){}
	}
	public ConditionObject(int type, String name) {
		this.type = type;
		this.name = name;
		switch(type) {
			case 0: this.data = new TimerObject();
				break;
			case 1: this.data = new BluetoothObject();
				break;
			case 2: this.data = new WifiObject();
				break;
			case 3: this.data = new LocationObject();
				break;
		}
		
	}
	public void setTimer(int time, int timeType) {
		TimerObject timer = (TimerObject) data;
		timer.time = time;
		timer.timeType = timeType;
	}
	public void setTimer(boolean alwaysUnlocked) {
		TimerObject timer = (TimerObject) data;
		timer.alwaysUnlocked = alwaysUnlocked;
	}
	public int getTypeInt(){
		return type;
	}
	public String getTypeName(){
		switch(getTypeInt()) {
			case 0: return "Timer";
			case 1: return "Bluetooth";
			case 2: return "Wifi";
			case 3: return "Location";
		}
		return "error";
	}
	public String getName(){
		return name;
	}
	public Object getData(){
		return data;
	}

	public boolean saveThatCondition(Context context, String storageName) {
		SharedPreferences prefs = context.getSharedPreferences("hay.chris.smartunlock.condition_storage", 0);
	    SharedPreferences.Editor ed = prefs.edit();
	    int currentConditionSize = prefs.getInt("total_condition_storage_size", 0);
	    ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();

	    ObjectOutputStream objectOutput;
        Log.e("test", "save" + (currentConditionSize + 1));

	    try {
	        objectOutput = new ObjectOutputStream(arrayOutputStream);
	        objectOutput.writeObject(this);
	        byte[] data = arrayOutputStream.toByteArray();
	        objectOutput.close();
	        arrayOutputStream.close();

	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        Base64OutputStream b64 = new Base64OutputStream(out, Base64.DEFAULT);
	        b64.write(data);
	        b64.close();
	        out.close();

	        ed.putString(storageName, new String(out.toByteArray()));
		    ed.putInt("total_condition_storage_size", currentConditionSize + 1);

	        ed.commit(); 
	        Log.e("test", "save" + (currentConditionSize + 1));
	    } catch (IOException e) {
	        e.printStackTrace();
	        return false;
	    }
	    return true;
	}
	public static ConditionObject loadThatCondition(Context context, String storageName) {
		SharedPreferences prefs = context.getSharedPreferences("hay.chris.smartunlock.condition_storage", 0);
		byte[] bytes = prefs.getString(storageName, "{}").getBytes();
	    if (bytes.length == 0) {
	        return null;
	    }
	    ByteArrayInputStream byteArray = new ByteArrayInputStream(bytes);
	    Base64InputStream base64InputStream = new Base64InputStream(byteArray, Base64.DEFAULT);
	    ObjectInputStream in = null;
	    ConditionObject condition = null;
	    try {
	    	in = new ObjectInputStream(base64InputStream);
			condition = (ConditionObject) in.readObject();
			byteArray.close();
			base64InputStream.close();
		    in.close();
	       Log.e("test", "load");
		} catch (OptionalDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return condition;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}