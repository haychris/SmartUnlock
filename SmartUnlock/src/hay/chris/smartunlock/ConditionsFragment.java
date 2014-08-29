package hay.chris.smartunlock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment
 * must implement the {@link ConditionsFragment.OnFragmentInteractionListener}
 * interface to handle interaction events. Use the
 * {@link ConditionsFragment#newInstance} factory method to create an instance
 * of this fragment.
 * 
 */
public class ConditionsFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	private OnFragmentInteractionListener mListener;
	private Context fragContext;
	private Activity currentActivity;
	private ConditionAdapter cAdapter;

	private class ConditionAdapter extends ArrayAdapter<ConditionObject> {

		private Context mContext;
		public ConditionAdapter(Context context, int resource, int textViewResourceId, ArrayList<ConditionObject> data) {
			super(context, resource, textViewResourceId, data);
			mContext = context;
		}
		public ConditionAdapter(Context context, int resource, ArrayList<ConditionObject> data) {
			super(context, resource, data);
			mContext = context;
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = currentActivity.getLayoutInflater().inflate(R.layout.condition_card, parent, false);
			}
			TextView title = (TextView) ((ViewGroup) convertView).getChildAt(0);
			TextView description = (TextView) ((ViewGroup) convertView).getChildAt(1);
			ImageView image = (ImageView) ((ViewGroup) convertView).getChildAt(2);
			final int pos = position;
			
			ConditionObject condition = getItem(position);
			final String conditionName = condition.getName();
			int type = condition.getTypeInt();
			switch(type){
				case 0: 
					image.setImageResource(R.drawable.ic_timer_icon_enabled); 
					image.setOnClickListener(new OnClickListener() {
												private boolean enabled = true;
												public void onClick(View v) {
													Drawable current = ((ImageView) v).getDrawable();
													if (enabled) {
														((ImageView) v).setImageResource(R.drawable.ic_timer_icon_disabled);
														enabled = false;
														activateCondition(conditionName);
													} else {
														((ImageView) v).setImageResource(R.drawable.ic_timer_icon_enabled);
														enabled = true;
														disableCondition(conditionName);
													}
												}
											});
					title.setText(R.string.title_timer);
					ConditionObject.TimerObject data = (ConditionObject.TimerObject) condition.getData();
					switch(data.radio) {
						case 0: 
							String timeTypeName;
							int setTimeAmount = data.time; //length of time in milliseconds
							switch(data.timeType){
								case 0: timeTypeName = " hours "; break;
								case 1: timeTypeName = " minutes "; break;
								case 2: timeTypeName = " seconds "; break;
								default: timeTypeName = ""; break;
							}
							switch(data.timeType){
								case 0: setTimeAmount *= 60;
								case 1: setTimeAmount *= 60;
								case 2: setTimeAmount *= 1000;
								break;
							}
							description.setText("Disable lock for " + data.time + timeTypeName + "after successful login.");
						break;
						case 1:
						break;
						case 2: 
							description.setText("Always disable lock.");
						break;
						case 3:
							description.setText("Never disable lock.");
						break;
					}
				break;
				case 1:
					image.setImageResource(R.drawable.ic_bluetooth_icon);
					title.setText(R.string.title_bluetooth);
					break;
				case 2:
					image.setImageResource(R.drawable.ic_wifi_icon);
					title.setText(R.string.title_wifi);
					break;
				case 3:
					image.setImageResource(R.drawable.ic_location_icon);
					title.setText(R.string.title_location);
					break;
			}
			return convertView;
		}
		protected void disableCondition(String name) {
			// TODO Auto-generated method stub
			
		}
		public String getSetName(String name){
			return name;
		}
		protected void activateCondition(String name) {
			// TODO Auto-generated method stub
			ConditionObject condition = ConditionObject.loadThatCondition(mContext, name);
			String curSetName = getSetName(name);
			int type = condition.getTypeInt();
			switch(type){
			case 0: 
				ConditionObject.TimerObject data = (ConditionObject.TimerObject) condition.getData();
				switch(data.radio) {
					case 0: 
						SharedPreferences prefs = mContext.getSharedPreferences("hay.chris.smartunlock.condition_storage", 0);
						SharedPreferences.Editor ed = prefs.edit();
						//get list of all condition sets that are active
						Set<String> activeConditionSets = prefs.getStringSet("active_condition_sets", new HashSet<String>());
						//get list of all active conditions within this set
						Set<String> curSetConditions = prefs.getStringSet(curSetName, new HashSet<String>());
						//add this condition to its set
						curSetConditions.add(name);
						//add this set to the list of active sets
						activeConditionSets.add(curSetName);
						ed.commit();
					break;
					case 1:
					break;
					case 2: 
					break;
					case 3:
					break;
				}
			break;
			case 1:
				break;
			case 2:
				break;
			case 3:
				break;
		}
		}
		
	}
	
	
	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment ConditionsFragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static ConditionsFragment newInstance(String param1, String param2) {
		ConditionsFragment fragment = new ConditionsFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	public ConditionsFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		currentActivity = getActivity();
		fragContext = getActivity().getApplicationContext();
		removeAllConditions();
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}
	public void refreshConditions(){
		Log.e("test", "refreshing");
		SharedPreferences prefs = fragContext.getSharedPreferences("hay.chris.smartunlock.condition_storage", 0);
	    int currentConditionSize = prefs.getInt("total_condition_storage_size", 0);
		if (cAdapter != null && cAdapter.getCount() != currentConditionSize) {
			ConditionObject[] newData = new ConditionObject[currentConditionSize - cAdapter.getCount()];
			for (int i = cAdapter.getCount(); i < currentConditionSize; i++){
				newData[i - cAdapter.getCount()] = (ConditionObject.loadThatCondition(fragContext, "condition_storage" + i));
			}
			ArrayList<ConditionObject> lst = new ArrayList<ConditionObject>();
			lst.addAll(Arrays.asList(newData));
			Log.e("test", "newData" + newData.length);
			cAdapter.addAll(lst);
		}
		((ListView) getView().findViewById(R.id.conditions_listview)).invalidateViews();

	}
	public void removeCondition(int position){
		
	}
	public void removeAllConditions() {
		SharedPreferences prefs = fragContext.getSharedPreferences("hay.chris.smartunlock.condition_storage", 0);
		SharedPreferences.Editor ed = prefs.edit();
	    int currentConditionSize = prefs.getInt("total_condition_storage_size", 0);
	    for (int i = 0; i < currentConditionSize; i++)
	    	ed.remove("condition_storage" + i);
	    ed.putInt("total_condition_storage_size", 0);
	    ed.commit();
	}
	public void onResume(){
		super.onResume();
		refreshConditions();
				
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		ViewGroup view =  (ViewGroup) inflater.inflate(R.layout.fragment_conditions, container, false); 
		ListView list = (ListView) view.findViewById(R.id.conditions_listview);
		
		SharedPreferences prefs = fragContext.getSharedPreferences("hay.chris.smartunlock.condition_storage", 0);
	    int currentConditionSize = prefs.getInt("total_condition_storage_size", 0);
	    if (cAdapter == null){
		    ConditionObject[] data = new ConditionObject[currentConditionSize];
		    for (int i = 0; i < currentConditionSize; i++){
		    	data[i] = ConditionObject.loadThatCondition(fragContext, "condition_storage" + i);
		    }
		    ArrayList<ConditionObject> lst = new ArrayList<ConditionObject>();
		    lst.addAll(Arrays.asList(data));
		    cAdapter = new ConditionAdapter(fragContext, R.layout.condition_card, lst);
		    cAdapter.setNotifyOnChange(true);
	    } else {
	    	refreshConditions();
	    	Log.e("test", "refresh attempt");
	    }
	    if (list != null)
	    	list.setAdapter(cAdapter);
	    
//	    View returnView = null;
//	    for (int i = 0; i < currentConditionSize; i++) {
//			returnView = inflater.inflate(R.layout.condition_card, view, true);
//	    }
		return view;
		//return inflater.inflate(R.layout.fragment_conditions, container, false);
	}
	
	
	
	
	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() { 
		super.onDetach();
		mListener = null;  
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated to
	 * the activity and potentially other fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		public void onFragmentInteraction(Uri uri);
	}

}
