package com.example.myrecords_parents;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.myrecords_parents.SubjectInfo.Fetchallcolumns;
import com.example.myrecords_parents.SubjectInfo.Fetchattendance;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MarksInfo extends Activity {
String rollno,semister,result,record1,subject,batch,teachername,classname,studentname,serverurl;
String[] split ;
ArrayList<String> values = new ArrayList<String>();
TextView name,roll,att,message;
SharedPreferences spref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_marks_info);
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1f56af")));
		spref=(SharedPreferences)PreferenceManager.getDefaultSharedPreferences(this);
		serverurl=spref.getString("ServerURL","val").trim();
		try{Bundle b= getIntent().getExtras();
		rollno=b.getString("rollno");
		semister=b.getString("semister");
		subject=b.getString("subject");
		batch=b.getString("batch");
		teachername=b.getString("teachername");
		classname=b.getString("classname");
		studentname=b.getString("studentname");
		}
		catch(Exception e){
			Toast.makeText(MarksInfo.this,"Error :"+e, Toast.LENGTH_LONG).show();
		}
		name=(TextView)findViewById(R.id.textView1); name.setText("Subject");
		roll=(TextView)findViewById(R.id.textView2); roll.setText(subject);
		att=(TextView)findViewById(R.id.textView3);
		message=(TextView)findViewById(R.id.textView4);
		if(isNetworkAvailable()==true){
			
			serverurl=spref.getString("ServerURL","val").trim();new Fetchallcolumns().execute();
			new Fetchmarks().execute();
			}
			else
			{
				//lv.setAdapter(null);
				//txt.setText("No Internet Connection. \n Connect to Internet and then try.");
				message.setText("No Internet Connection.");
			}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.marks_info, menu);
		return true;
	}
	class Fetchallcolumns extends AsyncTask<Void, Void, Void>
	{
	ProgressDialog pb;
     int flag=0; 
      @Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pb = new ProgressDialog(MarksInfo.this);
			pb.setMessage("Fetching Content...");
			//pb.setTitle("Please Wait...");
			pb.setCancelable(false);
			pb.show();
		}	

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			getData();
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(pb.isShowing()){pb.dismiss();}
			if(flag==1){ Toast.makeText(MarksInfo.this, "Connection Timeout.. Try Again Later.", Toast.LENGTH_LONG).show();
			//lv.setAdapter(adapter);	
			//lv.setAdapter(null);
			}
			else if(flag==2) { //Toast.makeText(SearchServer.this, "Server Not Responding..", Toast.LENGTH_LONG).show();
			//lv.setAdapter(adapter);
			//lv.setAdapter(null);
			//txt.setText("No Results Found.");
			}
			else {
			//lv.setAdapter(adapter);
				
			//txt.setText("");
			//adapter = new FetchalldetailsAdapter(SubjectInfo.this,nameal, rollal, classnameal, subjectal,batchal, semisteral, totallectal, totalattal,
			//			remarksal , prratingal, brratingal, teachernameal, updatedateal);
			//lv.setAdapter(adapter);
		   // lv.smoothScrollToPosition(adapter.getCount());
		    
				
			}
		}
    	public void getData(){
    		InputStream isr = null;
    		try{
    			HttpClient httpClient = new DefaultHttpClient();
    			HttpParams params = httpClient.getParams();
    			HttpConnectionParams.setConnectionTimeout(params,5000);
    			HttpConnectionParams.setSoTimeout(params,5000);
    			HttpPost httppost = new HttpPost("http://"+serverurl+"/myrecordsparents/fetchmarkscolumns.php?rollno="+rollno+"&semister="+semister
    					+"&subject="+subject+"&teachername="+teachername.replaceAll(" ","_")+"&batch="+batch+"&classname="+classname);
    			HttpResponse response = httpClient.execute(httppost);
    			if(response !=null)
    			{
    				System.out.println("Connection Created..!");
    				System.out.println(serverurl);
    			}
    			else{
    				System.out.println("Connection Not Created..!");
    			}
    			HttpEntity entity = response.getEntity();
    			isr = entity.getContent();
    			
    		} catch (ConnectTimeoutException e) {
    	        //Here Connection TimeOut excepion    
    		     // Toast.makeText(SearchServer.this, "Your connection timedout", Toast.LENGTH_LONG).show();
    			flag=1;
    		   }
    		catch(Exception e){
    			System.out.println("Error"+e);
    		}
    		try{
    			InputStreamReader isre = new InputStreamReader(isr,"iso-8859-1");
    			BufferedReader reader = new BufferedReader(isre,8);
    			StringBuffer sb = new StringBuffer();
    			String line = null;
    					while((line= reader.readLine())!=null)
    					{
    						sb.append(line);
    					}
    				isr.close();
    				result = sb.toString();
    				System.out.println("Success");
    				System.out.println(result);
    				//String foo = "This,that,other";
    				split = result.split(",");
    				for(int i = 0; i < split.length; i++) {
    					System.out.println(i+" : "+split[i]);
    				}
    				
    		
    		}catch(Exception e){
    			System.out.println("Error"+e);
    		}
    	}
	}

	
	class Fetchmarks extends AsyncTask<Void, Void, Void>
	{
	ProgressDialog pb;
     int flag=0; 
      @Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pb = new ProgressDialog(MarksInfo.this);
			pb.setMessage("Fetching Content...");
			//pb.setTitle("Please Wait...");
			pb.setCancelable(false);
			pb.show();
		}	

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			getData();
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if(pb.isShowing()){pb.dismiss();}
			if(flag==1){ Toast.makeText(MarksInfo.this, "Connection Timeout.. Try Again Later.", Toast.LENGTH_LONG).show();
			//lv.setAdapter(adapter);	
			//lv.setAdapter(null);
			}
			else if(flag==2) { //Toast.makeText(SearchServer.this, "Server Not Responding..", Toast.LENGTH_LONG).show();
			//lv.setAdapter(adapter);
			/*lv.setAdapter(null);
			txt.setText("No Results Found.");*/
				message.setText("No Data Available.");
			}
			else {
			//lv.setAdapter(adapter);
				try{ 
					//String[] split2 ;
				for(int j=2;j<split.length;j++){
					//split2= split[j].split("_");
					att.append(split[j]+"  -  "+values.get(j)+"\n\n");
				}
			} catch(Exception e){ System.out.println("Error : "+e);}
				
				
				
			}
		}
    	public void getData(){
    		InputStream isr = null;
    		try{
    			HttpClient httpClient = new DefaultHttpClient();
    			HttpParams params = httpClient.getParams();
    			HttpConnectionParams.setConnectionTimeout(params,5000);
    			HttpConnectionParams.setSoTimeout(params,5000);
    			HttpPost httppost = new HttpPost("http://"+serverurl+"/myrecordsparents/fetchmarks.php?rollno="+rollno+"&semister="+semister
    					+"&subject="+subject+"&teachername="+teachername.replaceAll(" ","_")+"&batch="+batch+"&classname="+classname);
    			HttpResponse response = httpClient.execute(httppost);
    			if(response !=null)
    			{
    				System.out.println("Connection Created..!");
    				System.out.println(serverurl);
    			}
    			else{
    				System.out.println("Connection Not Created..!");
    			}
    			HttpEntity entity = response.getEntity();
    			isr = entity.getContent();
    			
    		} catch (ConnectTimeoutException e) {
    	        //Here Connection TimeOut excepion    
    		     // Toast.makeText(SearchServer.this, "Your connection timedout", Toast.LENGTH_LONG).show();
    			flag=1;
    		   }
    		catch(Exception e){
    			System.out.println("Error"+e);
    		}
    		try{
    			InputStreamReader isre = new InputStreamReader(isr,"iso-8859-1");
    			BufferedReader reader = new BufferedReader(isre,8);
    			StringBuffer sb = new StringBuffer();
    			String line = null;
    					while((line= reader.readLine())!=null)
    					{
    						sb.append(line);
    					}
    				isr.close();
    				result = sb.toString();
    				System.out.println("Success");
    				System.out.println(result);
    				
    		
    		}catch(Exception e){
    			System.out.println("Error"+e);
    		}
    		try {
    			JSONArray j = new JSONArray(result);
    			for(int i=0;i<j.length();i++){
    				JSONObject json = j.getJSONObject(i);
    				//record1 ="Name : "+json.getString("name")+"\nRoll No. : " + json.getString("rollno")+"\nClass : "+json.getString("class")+"\nBatch : "+json.getString("batch")+"\nRemarks : "+json.getString("remarks")+"\nPerformance : "+json.getString("performancerating")+"\n Behaviour : "+json.getString("behaviourrating");
    				 for(int ji=0;ji<split.length;ji++) {
    				System.out.println(split[ji] +" : "+json.getString(split[ji]));
    				values.add(json.getString(split[ji]));
    				 }
    				//adp.add(record1);
    				//txt.append(record1+"\n");
    				//total = total + record1 + "\n";
    			}
    		} catch(JSONException e)
    		{
    			flag=2;
    			System.out.println("Error"+e);
    		}
    		catch(Exception e){
    			System.out.println("Error"+e);
    		}
    	}
	}

public boolean isNetworkAvailable() {
			ConnectivityManager connectivityManager  = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
		    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		    if(activeNetworkInfo != null && activeNetworkInfo.isConnected())
		    	 {return true;}
		    else {return false;}
		
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		 if(keyCode == KeyEvent.KEYCODE_BACK)
		    { Intent i = new Intent(MarksInfo.this,SubjectInfo.class );
		    i.putExtra("rollno",rollno);
		    i.putExtra("semister",semister);
			i.putExtra("studentname",studentname);
			i.putExtra("subject",subject);
			i.putExtra("batch",batch);
			i.putExtra("teachername",teachername);
			i.putExtra("classname",classname);
			i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(i);
			overridePendingTransition(R.anim.push_left_in,android.R.anim.fade_out);
			finish();
			
		    return true;
	    }
	return false;
	}

}
