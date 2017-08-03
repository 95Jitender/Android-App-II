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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SelectSemYear extends Activity {
	SharedPreferences spref;
	int loginval,flag=0;
	ListView lv;
	String rollno,result="",record1="",serverurl;
	TextView txt;
	FetchSemAdapter adapter;
	ArrayList<String> semisteral= new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_sem_year);
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1f56af")));
		try{Bundle b= getIntent().getExtras();
		rollno=b.getString("rollno");
		spref=(SharedPreferences)PreferenceManager.getDefaultSharedPreferences(this);
		serverurl=spref.getString("ServerURL","val").trim();}
		catch(Exception e){
			Toast.makeText(SelectSemYear.this,"Error :"+e, Toast.LENGTH_LONG).show();
		}
		lv=(ListView)findViewById(R.id.listView1);
		txt=(TextView)findViewById(R.id.textView3);
		System.out.println(rollno);
		if(isNetworkAvailable()==true){
		new FetchSem().execute();
		}
		else
		{
			lv.setAdapter(null);
			txt.setText("No Internet Connection. \n Connect to Internet and then try.");
		}
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.select_sem_year, menu);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item)
	{	Intent i;
		switch(item.getItemId())
		{
		case R.id.item1 : 
			loginval=0;
			spref=(SharedPreferences)PreferenceManager.getDefaultSharedPreferences(this);
			SharedPreferences.Editor spe = spref.edit();
			spe.putInt("login",loginval);
			spe.commit();
			i = new Intent(SelectSemYear.this,MainActivity.class );
			i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(i);
			overridePendingTransition(R.anim.push_left_in,android.R.anim.fade_out);
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
		
	}
	class FetchSem extends AsyncTask<Void, Void, Void>
	{
	ProgressDialog pb;
     int flag=0; 
      @Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pb = new ProgressDialog(SelectSemYear.this);
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
			if(flag==1){ Toast.makeText(SelectSemYear.this, "Connection Timeout.. Try Again Later.", Toast.LENGTH_LONG).show();
			//lv.setAdapter(adapter);	
			lv.setAdapter(null);
			}
			else if(flag==2) { //Toast.makeText(SearchServer.this, "Server Not Responding..", Toast.LENGTH_LONG).show();
			//lv.setAdapter(adapter);
			lv.setAdapter(null);
			txt.setText("No Results Found.");
			}
			else {
			//lv.setAdapter(adapter);
				
			txt.setText("");
			adapter = new FetchSemAdapter(SelectSemYear.this,semisteral);
			lv.setAdapter(adapter);
		    lv.smoothScrollToPosition(adapter.getCount());
		    lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View v, int pos,
						long id) {
					// TODO Auto-generated method stub
					Toast.makeText(getApplicationContext(),"You Clicked on : "+ semisteral.get(pos), Toast.LENGTH_LONG).show();
					Intent i= new Intent(SelectSemYear.this,StudentInfo.class);
					i.putExtra("rollno",rollno);
					i.putExtra("semister",semisteral.get(pos));
					i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
					startActivity(i);
					overridePendingTransition(R.anim.push_left_in,android.R.anim.fade_out);
					finish();
					/*Animation anim = AnimationUtils.loadAnimation(
		     	                getActivity(),R.anim.fade_in
		     	            );
		     			anim.setDuration(500);
		     	    i.startAnimation(anim);*/
					
				}
			});
				
			}
		}
    	public void getData(){
    		InputStream isr = null;
    		try{
    			HttpClient httpClient = new DefaultHttpClient();
    			HttpParams params = httpClient.getParams();
    			HttpConnectionParams.setConnectionTimeout(params,5000);
    			HttpConnectionParams.setSoTimeout(params,5000);
    			HttpPost httppost = new HttpPost("http://"+serverurl+"/myrecordsparents/fetchsem.php?rollno="+rollno.replaceAll(" ","+"));
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
    				
    		
    		}catch(Exception e){
    			System.out.println("Error"+e);
    		}
    		try {
    			JSONArray j = new JSONArray(result);
    			for(int i=0;i<j.length();i++){
    				JSONObject json = j.getJSONObject(i);
    				record1 ="Semister : " +json.getString("semister");
    				 /*nameal.add(json.getString("name"));*/
    				// rollal.add(json.getString("rollno"));
    				/* classnameal.add(json.getString("class"));
    			     subjectal.add(json.getString("subject"));*/
    				 semisteral.add(json.getString("semister"));
    				/* totallectal.add(json.getString("totallect"));
    				 totalattal.add(json.getString("attendance"));
    				 remarksal.add(json.getString("remarks"));
    				 prratingal.add(json.getString("performancerating"));
    				 brratingal.add(json.getString("behaviourrating"));
    				 teachernameal.add(json.getString("teachername"));
    				 updatedateal.add(json.getString("updatedate"));*/
    				 System.out.println(record1);
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
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
	    if(keyCode == KeyEvent.KEYCODE_BACK)
	    {
	    	if(flag==0){
	    		Toast.makeText(getApplicationContext(), "Press Again To Exit", Toast.LENGTH_SHORT).show();
	    		flag=flag+1;
	    	}
	    	else{
            finish();          
            moveTaskToBack(true);
		    return true;
	    	}
	    }
	return false;
	}
}
