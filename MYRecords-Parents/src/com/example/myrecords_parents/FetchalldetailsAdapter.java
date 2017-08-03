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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

public class FetchalldetailsAdapter extends ArrayAdapter<String> {
	Context context;
	ArrayList<String> nameal;
	ArrayList<String> batchal;
	ArrayList<String> rollal;
	ArrayList<String> classnameal;
	ArrayList<String> subjectal;
	ArrayList<String> semisteral;
	ArrayList<String> totallectal;
	ArrayList<String> totalattal;
	ArrayList<String> remarksal;
	ArrayList<String> prratingal;
	ArrayList<String> brratingal;
	ArrayList<String> teachernameal;
	ArrayList<String> updatedateal;
	
	public FetchalldetailsAdapter(Context context,	ArrayList<String> nameal, ArrayList<String> rollal,ArrayList<String> classnameal,
	ArrayList<String> subjectal, ArrayList<String> batchal, ArrayList<String> semisteral, ArrayList<String> totallectal, ArrayList<String> totalattal,
	ArrayList<String> remarksal, ArrayList<String> prratingal, ArrayList<String> brratingal, ArrayList<String> teachernameal,ArrayList<String> updatedateal) {
		super(context,R.layout.fetchalldetailsrow,nameal);
		/*super(context,R.layout.serversearchrow, nameal,  rollal, classnameal, subjectal, semisteral, totallectal, totalattal,
		remarksal , prratingal, brratingal, teachernameal, updatedateal);*/
		// TODO Auto-generated constructor stub
		 this.context = context;
		 this.nameal = nameal ;
		 this.rollal= rollal;
		 this.batchal = batchal ;
		 this.classnameal =classnameal;
		 this.subjectal =subjectal;
		 this.semisteral=semisteral;
		 this.totallectal=totallectal;
		 this.totalattal=totalattal;
		 this.remarksal =remarksal ;
		 this.prratingal= prratingal;
		 this.brratingal =brratingal;
		 this.teachernameal =teachernameal;
		 this.updatedateal =updatedateal;
		 
	}

	 public View getView(int position, View convertView, ViewGroup parent) {
		 // TODO Auto-generated method stub
		 
		LayoutInflater inflater = ((Activity)context).getLayoutInflater();
	    convertView = inflater.inflate(R.layout.fetchalldetailsrow, parent, false); 
	    convertView.setMinimumHeight(parent.getMeasuredHeight());
		final TextView name = (TextView) convertView.findViewById(R.id.textView1);
		final TextView rollno = (TextView) convertView.findViewById(R.id.textView2);
		final TextView classname = (TextView) convertView.findViewById(R.id.textView3);
		final TextView subject = (TextView) convertView.findViewById(R.id.textView4);
		final TextView semister = (TextView) convertView.findViewById(R.id.textView5);
		final TextView totallect = (TextView) convertView.findViewById(R.id.textView6);
		final TextView totalatt = (TextView) convertView.findViewById(R.id.textView7);
		final TextView remarks = (TextView) convertView.findViewById(R.id.textView8);
		final TextView prrating = (TextView) convertView.findViewById(R.id.textView9);
		final TextView brrating = (TextView) convertView.findViewById(R.id.textView10);
		final TextView teachername = (TextView) convertView.findViewById(R.id.textView11);
		final TextView updatedate = (TextView) convertView.findViewById(R.id.textView12);
		 BarChart chart = (BarChart)convertView.findViewById(R.id.chart);
		 chart.setMinimumHeight((parent.getMeasuredHeight()/2));
		 float brating=0,prating=0;
		 String remarksnew="";
		if(remarksal.get(position).contains("null")||remarksal.get(position).trim().equals(""))
		{ remarks .setText("Remarks : No Remarks."); }
		else { remarks .setText("Remarks : "+remarksal.get(position)); }
		
		if(prratingal.get(position).contains("null")||prratingal.get(position).trim().equals(""))
		{
		/* prrating.setText("Performance Rating : No Rating.");*/
		 prating=0;}
		else {
		/* prrating.setText("Performance Rating : "+prratingal.get(position));*/
		 prating=Float.parseFloat(prratingal.get(position));
		}
		
		if(brratingal.get(position).contains("null")||brratingal.get(position).trim().equals(""))
		{/* brrating .setText("Behaviour Rating : No Rating. ");*/
		brating=0;}
		else { /*brrating .setText("Behaviour Rating : "+brratingal.get(position));*/
		brating=Float.parseFloat(brratingal.get(position));}
		
		 name.setText("Name : "+nameal.get(position));
		 rollno .setText("Roll No. : "+rollal.get(position));
		 classname .setText("Classname : " +classnameal.get(position)+ " Batch : "+batchal.get(position));
		 subject .setText("Subject : " +subjectal.get(position)+" Semister : " +semisteral.get(position));
		// semister .setText();
		 totallect.setText("Attendance : "+totalattal.get(position)+"/"+totallectal.get(position));
		 //totalatt .setText();
		 teachername .setText("Teacher Name : "+teachernameal.get(position));
		 updatedate .setText("Update Date : "+updatedateal.get(position));
		
		 ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
	 		entries.add(new BarEntry(10.0f, 0));
	 		entries.add(new BarEntry(prating, 1));
	 		entries.add(new BarEntry(brating, 2));
	 	BarDataSet dataset = new BarDataSet(entries, "Maximum Rating ,Performance Rating , Behaviour Rating");
	 		ArrayList<String> labels = new ArrayList<String>();
	 		labels.add("Maximum");
	 		labels.add("Performance"); 
	 		labels.add("Behaviour"); 
	  
	 		BarData data = new BarData(labels, dataset);
	 		chart.setData(data);
	 		chart.setDescription(nameal.get(position)+" - Rating");
	 		dataset.setColors(ColorTemplate.COLORFUL_COLORS);
	 	    chart.animateXY(2000, 2000);
	 	    chart.invalidate();
		
		 Animation anim = AnimationUtils.loadAnimation(
	            context,R.anim.push_left_in
	        );
		anim.setDuration(500);
		convertView.startAnimation(anim);
		  return convertView;
		 }

}
