package hay.chris.smartunlock;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Base64OutputStream;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.os.Build;

public class CreateConditionActivity extends Activity implements OnItemSelectedListener {

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

	private void expand(View view) {
	     //set Visible
	     view.setVisibility(View.VISIBLE);
	     
	     final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
	     final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
	     view.measure(widthSpec, heightSpec);
	     
	     ValueAnimator mAnimator = slideAnimator(0, view.getMeasuredHeight(), view);
	     mAnimator.start();
	}
	 
	private void collapse(final View view) {
	     int finalHeight = view.getHeight();

	     ValueAnimator mAnimator = slideAnimator(finalHeight, 0, view);  
	  
	     mAnimator.addListener(new Animator.AnimatorListener() {
	          @Override
	          public void onAnimationEnd(Animator animator) {
	             //Height=0, but it set visibility to GONE
	              view.setVisibility(View.GONE);
	          }

			@Override
			public void onAnimationCancel(Animator animation) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onAnimationStart(Animator animation) {
				// TODO Auto-generated method stub
				
			}
	     });
	     mAnimator.start();
	}
	private ValueAnimator slideAnimator(int start, int end, final View view) {
		  
	    ValueAnimator animator = ValueAnimator.ofInt(start, end);
	  
	    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
	         @Override
	         public void onAnimationUpdate(ValueAnimator valueAnimator) {
	            //Update Height
	            int value = (Integer) valueAnimator.getAnimatedValue();
	            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
	            layoutParams.height = value;
	            view.setLayoutParams(layoutParams);
	         }
	    });
	    return animator;
	}

	public void cardClickTimer(View view){ 
//		FragmentTransaction ft = getFragmentManager().beginTransaction();
//		ft.replace(R.id.container, new TimerFragment());
//		ft.addToBackStack(null);
//		ft.commit();
		TextView tv = (TextView) findViewById(R.id.timer_description2);
		if (tv.getVisibility() == View.GONE) {
			//tv.setVisibility(View.VISIBLE);
			expand(tv);
		} else {
			//tv.setVisibility(View.GONE);
			collapse(tv);
		}
	}  
	private View[] getTimerViews(){
		View[] views = {findViewById(R.id.text_for),
		                findViewById(R.id.timer_unlock_body),
		                findViewById(R.id.timer_editor),
		                findViewById(R.id.timer_spinner)};
		return views;
	}
	private void dismissAllViews(){
		View[] views = getTimerViews();    
		for (View v: views) {
			v.setVisibility(View.GONE);
		}
	}
	public void onRadioTimerAfterUnlock(View view) {
		dismissAllViews();
		TextView tv = (TextView) findViewById(R.id.text_for);
		EditText et = (EditText) findViewById(R.id.timer_editor);
		RelativeLayout rl = (RelativeLayout) findViewById(R.id.timer_unlock_body);
		tv.setVisibility(View.VISIBLE);
		et.setVisibility(View.VISIBLE);
		rl.setVisibility(View.VISIBLE);
		Spinner spinner = (Spinner) findViewById(R.id.timer_spinner);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.timer_spinner, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		spinner.setSelection(1);
		spinner.setVisibility(View.VISIBLE);
	}
	public void onItemSelected(AdapterView<?> parent, View view, 
            int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

	public void onRadioTimerBetweenTimes(View view) {
		dismissAllViews();
	}
	public void onRadioTimerAlways(View view) {
		dismissAllViews();
	}
	public void onRadioTimerNever(View view) {
		dismissAllViews();
	}
	public void onTimerCancel(View view) {

	}
	
	public void onTimerCreate(View view) {
		SharedPreferences prefs = CreateConditionActivity.this.getSharedPreferences("hay.chris.smartunlock.condition_storage", 0);
	    int currentConditionSize = prefs.getInt("total_condition_storage_size", 0);
		int radio = ((RadioGroup)findViewById(R.id.timer_body)).getCheckedRadioButtonId();
		String storageName = "condition_storage"+ currentConditionSize;
		ConditionObject condition = new ConditionObject(0, storageName); 

		switch (radio) {
			case R.id.radio_after_unlock:
				int time = Integer.parseInt(((EditText) findViewById(R.id.timer_editor)).getText().toString());
				int timeType = ((Spinner) findViewById(R.id.timer_spinner)).getSelectedItemPosition();
				condition.setTimer(time, timeType, 0);
				break; 
			case R.id.radio_between_times:
				break;
			case R.id.radio_always:
				condition.setTimer(2);
				break;
			case R.id.radio_never:
				condition.setTimer(3);
				break;
		}
		Log.e("test", "attempt save"); 
		condition.saveThatCondition(this, storageName);
		finish();
	}
	public void setTimeDialog(View view){
		
	}
	
	private boolean saveCondition(String[] array, int conditionType, Context context){
		SharedPreferences prefs = context.getSharedPreferences("hay.chris.smartunlock.condition_storage", 0);
	    SharedPreferences.Editor editor = prefs.edit(); 
	    int currentConditionSize = prefs.getInt("total_condition_storage_size", 0);
	    editor.putInt("total_condition_storage_size", currentConditionSize + 1);
	    editor.putInt("condition_storage"+ currentConditionSize + "_size", array.length);  
	    editor.putInt("condition_storage"+ currentConditionSize + "_type", conditionType);
	    
	   
	    for(int i=0;i<array.length;i++)  
	        editor.putString("condition_storage" + currentConditionSize + "_" + i, array[i]);  
	    return editor.commit();  
	}
	
	public void loadAllConditions(Context context){
	    SharedPreferences prefs = context.getSharedPreferences("hay.chris.smartunlock.condition_storage", 0);
	    int currentConditionSize = prefs.getInt("total_condition_storage_size", 0);

	}
	public String[] loadCondition(String arrayName, int position, Context context) {  
	    SharedPreferences prefs = context.getSharedPreferences("hay.chris.smartunlock.condition_storage", 0);
	    int arraySize = prefs.getInt("condition_storage" + position + "_size", 0);
	    int conditionType = prefs.getInt("condition_storage" + position + "_type", 0);
	    
	    String array[] = new String[arraySize];  
	    for(int i=0;i<arraySize;i++)  
	        array[i] = prefs.getString("condition_storage" + position + "_" + i, null);  
	    
	    switch (conditionType) {
	 	//timer
		    case 0:
		    	break;
		    //bluetooth
		    case 1:
		    	break;
		    //wifi
		    case 2:
		    	break;
		    //location
		    case 3:
		    	break;
	 }
	    return array;  
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
