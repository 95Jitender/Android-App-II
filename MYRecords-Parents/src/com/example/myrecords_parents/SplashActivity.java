package com.example.myrecords_parents;

import com.example.myrecords_parents.MainActivity.login;

import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

public class SplashActivity extends Activity {
	public ProgressBar pb;
	public SharedPreferences spref;
	String rollno;
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.activity_splash);
	        pb=(ProgressBar)findViewById(R.id.progressBar1);
	        spref=(SharedPreferences)PreferenceManager.getDefaultSharedPreferences(this);
	       
	        new Progress().execute();
		}
		class Progress extends AsyncTask<Void,Integer,Void>
		{ 
		
			protected void onPostExecute(Void result)
			{	
				super.onPostExecute(result);
			try{ 
				int x= spref.getInt("login",0);
				rollno= spref.getString("rollno","");
				if(x==0){Intent i=new Intent();
				i.setClass(SplashActivity.this,MainActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(i);
				overridePendingTransition(R.anim.push_left_in,android.R.anim.fade_out);
				finish();
				} //end of if
					
					else{
						Intent i=new Intent();
						i.setClass(SplashActivity.this,SelectSemYear.class);
						i.putExtra("rollno",rollno);
						i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
    					startActivity(i);
    					overridePendingTransition(R.anim.push_left_in,android.R.anim.fade_out);
						finish();
						
					}
				}//end of try
				catch(Exception e){
					Toast.makeText(SplashActivity.this,"Error : "+e.toString(),Toast.LENGTH_LONG).show();
				}
			}
			
			protected void onProgressUpdate(Integer... values)
			{
				super.onProgressUpdate(values);
				pb.setProgress(values[0]);
				
			}
			protected Void doInBackground(Void... params){
				for(int i=0;i<=30;i++){
					try{
						Thread.sleep(10);
						publishProgress(i);
					}
					catch(InterruptedException e){
						e.printStackTrace();
					}//end of for
				 }
				return null;
			}
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	}

}
