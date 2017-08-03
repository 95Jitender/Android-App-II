package com.example.myrecords_parents;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
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
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
EditText roll,pass;
Button login;
CheckBox ksignin;
String rollno,password,finalresult,result,serverurl="";
SharedPreferences spref;
int stayvalue,flag=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1f56af")));
		spref=(SharedPreferences)PreferenceManager.getDefaultSharedPreferences(this);
		serverurl=spref.getString("ServerURL","val").trim();
		roll=(EditText)findViewById(R.id.editText1);
		pass=(EditText)findViewById(R.id.editText2);
		pass.setTypeface(Typeface.DEFAULT);
		pass.setTransformationMethod(new PasswordTransformationMethod());
		login=(Button)findViewById(R.id.button1);
		ksignin=(CheckBox)findViewById(R.id.checkBox1);
		roll.setOnFocusChangeListener(new View.OnFocusChangeListener(){
			 int x=1;
		      public void onFocusChange(View v, boolean hasFocus){
		if(hasFocus){
		        if(x==1){roll.setHint("Enrollment Number"); x=2;}
		        else{roll.setHint("");}
		        	}
		else
			roll.setHint("Enrollment Number");
		      }
		    });
		 
		pass.setOnFocusChangeListener(new View.OnFocusChangeListener(){
		      public void onFocusChange(View v, boolean hasFocus){
		if(hasFocus)
		{ pass.setHint("");}
		else
			pass.setHint("Password");
		      }
		    });
		 pass.setOnKeyListener(new View.OnKeyListener() {
		        public boolean onKey(View v, int keyCode, KeyEvent event) {
		            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
		        // Toast.makeText(getActivity(),"CLICKED", Toast.LENGTH_SHORT).show();
						InputMethodManager inputManager = (InputMethodManager)
		                        getSystemService(Context.INPUT_METHOD_SERVICE); 

		inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
		                           InputMethodManager.HIDE_NOT_ALWAYS);
						
		try {
			if(isNetworkAvailable()==true)
				{
					rollno = roll.getText().toString().trim();
					password = pass.getText().toString().trim();
					if(rollno.equals(null)||rollno.equals("")||password.equals(null)||password.equals(""))
					{ Toast.makeText(getApplicationContext(), "Input All Details", Toast.LENGTH_SHORT).show(); }
					else{ serverurl=spref.getString("ServerURL","val").trim(); new login().execute();}
				}
		else    {
					Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
				}
	
			} catch (Exception e) { System.out.println("Error : "+e); }                
			return true;
		            }
		            return false;
		        }
		    });
		
		login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			try {
				if(isNetworkAvailable()==true)
					{
						rollno = roll.getText().toString().trim();
						password = pass.getText().toString().trim();
						if(rollno.equals(null)||rollno.equals("")||password.equals(null)||password.equals(""))
						{ Toast.makeText(getApplicationContext(), "Input All Details", Toast.LENGTH_SHORT).show(); }
						else{ serverurl=spref.getString("ServerURL","val").trim(); try{new login().execute();
						}catch(Exception e){System.out.println("Error : "+e);}
						
						}
					}
			else    {
						Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
					}
		
				} catch (Exception e) { System.out.println("Error : "+e); }
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem item)
	{	Intent i;
		switch(item.getItemId())
		{
		case R.id.item1 : 
			i = new Intent(MainActivity.this,ServerURL.class );
			i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(i);
			overridePendingTransition(R.anim.push_left_in,android.R.anim.fade_out);
			break;
		}
		return super.onOptionsItemSelected(item);
		
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
	public boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager  = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    if(activeNetworkInfo != null && activeNetworkInfo.isConnected())
	    	 {return true;}
	    else {return false;}
	}
	class login extends AsyncTask<Void, Void, Void>
	{
	ProgressDialog pb;
     int flag=0; 
	
	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
		data();
		 
		
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		if(pb.isShowing()){pb.dismiss();}
		if(flag==1){ Toast.makeText(MainActivity.this, "Connection Timeout.. Try Again Later.", Toast.LENGTH_LONG).show();}
		else if(flag==2) { Toast.makeText(MainActivity.this, "Server Not Responding..", Toast.LENGTH_LONG).show();}
		else{
		  if(finalresult.equals("EnrollNotFound")){
			  Toast.makeText(MainActivity.this,"No Such Student.",Toast.LENGTH_LONG).show(); 
			  finalresult="";
		  }
		  else if(finalresult.equals("PasswordMatch")) {
			  Toast.makeText(MainActivity.this,"Correct",Toast.LENGTH_LONG).show(); 
				int loginval=1;
			  if(ksignin.isChecked()==true){
				SharedPreferences.Editor spe = spref.edit();
				spe.putInt("login",loginval);
				spe.putString("rollno",rollno);
				spe.putString("password",password);
				spe.commit();
			  }
			  Intent i= new Intent(MainActivity.this,SelectSemYear.class);
				i.putExtra("rollno",rollno);
				i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(i);
				overridePendingTransition(R.anim.push_left_in,android.R.anim.fade_out);
				finish();
			  finalresult="";
		  }
		  
		  else {
			  Toast.makeText(MainActivity.this,"Password Incorrect",Toast.LENGTH_LONG).show(); 
			  finalresult="";
			  }
		}
		  
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		pb = new ProgressDialog(MainActivity.this);
		pb.setMessage("Logging In.. ");
		//pb.setTitle("Please Wait...");
		pb.setCancelable(false);
		pb.show();
	}

	public boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager  = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    if(activeNetworkInfo != null && activeNetworkInfo.isConnected())
	    	 {return true;}
	    else {return false;}
	}
	public void data(){
		
		InputStream isr = null;
		try{
			HttpClient httpClient = new DefaultHttpClient();
			HttpParams params = httpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params,5000);
			HttpConnectionParams.setSoTimeout(params,5000);
			HttpPost httppost = new HttpPost("http://"+serverurl+"/myrecords/loginverify.php?rollno="+rollno+"&password="+password);
			HttpResponse response = httpClient.execute(httppost);
			if(response !=null)
			{
				System.out.println("Connection Created..!");
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
			JSONObject j = new JSONObject(result);
			finalresult =j.getString("result");
			 System.out.println(finalresult);
		}
			catch(JSONException e)
		{
			flag=2; 
			System.out.println("Error in jason : "+e);
		}
		catch(Exception e){
			System.out.println("Error "+e);
		}
	}
	}

}
