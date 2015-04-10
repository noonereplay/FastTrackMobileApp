package com.kmutnb.fasttrack;

import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment
 * must implement the {@link ProfileFragment.OnFragmentInteractionListener}
 * interface to handle interaction events. Use the
 * {@link ProfileFragment#newInstance} factory method to create an instance of
 * this fragment.
 * 
 */
public class ProfileFragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	private static String username;
	private static String profile_id;
	private ProfileItemAdapter adapter;
	//private OnFragmentInteractionListener mListener;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @param param1
	 *            Parameter 1.
	 * @param param2
	 *            Parameter 2.
	 * @return A new instance of fragment ProfileFragment.
	 */
	// TODO: Rename and change types and number of parameters
	ProfileItem avg_speed = new ProfileItem("Average Speed","... km/hr");
	ProfileItem max_speed = new ProfileItem("Max Speed","... km/hr");
	ProfileItem over_speed_times = new ProfileItem("Over Speed","... times");
	ProfileItem avg_distance = new ProfileItem("Average Distance","... km");

	public static ProfileFragment newInstance(String param1, String param2) {
		ProfileFragment fragment = new ProfileFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	public ProfileFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
			username = getArguments().getString("username");
			profile_id = getArguments().getString("profile_id");
		}
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		final View view = inflater.inflate(R.layout.fragment_profile, container, false);
        
		AsyncHttpClient client = new AsyncHttpClient();
        client.get(getActivity().getBaseContext(), "http://122.155.197.49/FastTrackHostApp/history/detail/"+profile_id, new JsonHttpResponseHandler(){
        	
        	@Override
        	public void onSuccess(int statusCode, Header[] headers,
        			JSONObject response) {
                try {
                	avg_speed.setItem_value(response.getDouble("avg_speed")+" km/hr");
                	max_speed.setItem_value(response.getDouble("max_speed")+" km/hr");
                	over_speed_times.setItem_value(response.getInt("over_speed_times")+" times");
                	avg_distance.setItem_value(response.getDouble("avg_distance")+" km");
        			/*
	                items.add(new ProfileItem("Max Speed",response.getDouble("max_speed")+" km/hr"));)
					items.add(new ProfileItem("Average Speed",response.getDouble("avg_speed")+" km/hr"));
	                items.add(new ProfileItem("Max Speed",response.getDouble("max_speed")+" km/hr"));
	                items.add(new ProfileItem("Over Speed",response.getInt("over_speed_times")+" times"));
	                items.add(new ProfileItem("Average Distance",response.getDouble("avg_distance")+" km"));*/
	                //adapter.set
                	adapter = new ProfileItemAdapter(getActivity(), generateData());
            		ListView listView = (ListView) view.findViewById(R.id.profile_item_list);
            		listView.setAdapter(adapter);
            		Log.w("FastTrack", response.toString());
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

        	}
        	
        	@Override
        	public void onFailure(int statusCode, Header[] headers,
        			String responseString, Throwable throwable) {
        		Log.w("FastTrack", responseString);
        	}
        });
        
		

		
		return view;
	}
/*
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
*/
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

	
	private ArrayList<ProfileItem> generateData(){
        final ArrayList<ProfileItem> items = new ArrayList<ProfileItem>();
        

        
    	items.add(avg_speed);
    	items.add(max_speed);
    	items.add(over_speed_times);
    	items.add(avg_distance);
 
        return items;
    }
}
