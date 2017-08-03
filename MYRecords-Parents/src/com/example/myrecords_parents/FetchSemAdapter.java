package com.example.myrecords_parents;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class FetchSemAdapter  extends ArrayAdapter<String> {
	Context context;
	ArrayList<String> semisteral;
	
	public FetchSemAdapter(Context context,ArrayList<String> semisteral) {
		super(context,R.layout.fetchsemrow,semisteral);
		/*super(context,R.layout.serversearchrow, nameal,  rollal, classnameal, subjectal, semisteral, totallectal, totalattal,
		remarksal , prratingal, brratingal, teachernameal, updatedateal);*/
		// TODO Auto-generated constructor stub
		 this.context = context;
		 this.semisteral=semisteral; 
	}

	 public View getView(int position, View convertView, ViewGroup parent) {
		 // TODO Auto-generated method stub
		 
		LayoutInflater inflater = ((Activity)context).getLayoutInflater();
	    convertView = inflater.inflate(R.layout.fetchsemrow, parent, false); 
		final TextView semister = (TextView) convertView.findViewById(R.id.textView1);
		semister .setText(" Semister : " +semisteral.get(position));		
		 Animation anim = AnimationUtils.loadAnimation(
	            context,R.anim.push_left_in
	        );
		anim.setDuration(500);
		convertView.startAnimation(anim);
		  return convertView;
		 }

}
