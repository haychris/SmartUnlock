package hay.chris.smartunlock;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

public class CreateConditionActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_condition);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new CreateConditionsFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_condition, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void cardClickTimer(View view){
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.container, new TimerFragment());
		ft.addToBackStack(null);
		ft.commit();
		
	}
	public void cardClickBluetooth(View view){
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.container, new TimerFragment());
		ft.addToBackStack(null);
		ft.commit();	
	}
	public void cardClickWifi(View view){
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.container, new TimerFragment());
		ft.addToBackStack(null);
		ft.commit();	
	}
	public void cardClickLocation(View view){
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.container, new TimerFragment());
		ft.addToBackStack(null);
		ft.commit();	
	}
	
	public static class CreateConditionsFragment extends Fragment {

		public CreateConditionsFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_create_conditions, container, false);
			return rootView;
		}
	}
	public static class TimerFragment extends Fragment {

		public TimerFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(
					R.layout.fragment_time, container, false);
			return rootView;
		}
	}
}
