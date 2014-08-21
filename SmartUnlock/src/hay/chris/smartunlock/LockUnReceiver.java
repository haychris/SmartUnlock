package hay.chris.smartunlock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class LockUnReceiver extends BroadcastReceiver {
	public LockUnReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		Boolean toLock = bundle.getBoolean("toLock");
		if (toLock) {
			lockDevice();
		}
		else {
			unlockDevice();
		}
		// TODO: This method is called when the BroadcastReceiver is receiving
		// an Intent broadcast.
		//throw new UnsupportedOperationException("Not yet implemented");
	}
	private void lockDevice(){
		
	}
	private void unlockDevice(){
		
	}
}
