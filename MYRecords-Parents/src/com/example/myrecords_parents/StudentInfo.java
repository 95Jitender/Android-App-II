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
import com.example.myrecords_parents.SelectSemYear.FetchSem;
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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class StudentInfo extends Activity {
	String result="",searchitem="",record1="",serverurl;
	EditText edt1;
	Button btn;
	ListView lv;
	TextView txt,txt2;
	ArrayAdapter<String> adp;
	FetchalldetailsAdapter adapter;
	ArrayList<String> nameal = new ArrayList<String>();
	ArrayList<String> batchal = new ArrayList<String>();
	ArrayList<String> rollal= new ArrayList<String>();
	ArrayList<String> classnameal= new ArrayList<String>();
	ArrayList<String> subjectal= new ArrayList<String>();
	ArrayList<String> semisteral= new ArrayList<String>();
	ArrayList<String> totallectal= new ArrayList<String>();
	ArrayList<String> totalattal= new ArrayList<String>();
	ArrayList<String> remarksal= new ArrayList<String>();
	ArrayList<String> prratingal= new ArrayList<String>();
	ArrayList<String> brratingal= new ArrayList<String>();
	ArrayList<String> teachernameal= new ArrayList<String>();
	ArrayList<String> updatedateal= new ArrayList<String>();
	int output;
	SharedPreferences spref;
	String rollno,semister;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_student_info);
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1f56af")));
		spref=(SharedPreferences)PreferenceManager.getDefaultSharedPreferences(this);
		serverurl=spref.getString("ServerURL","val").trim();
		try{Bundle b= getIntent().getExtras();
		rollno=b.getString("rollno");
		semister=b.getString("semister");
		
		}
		catch(Exception e){
			Toast.makeText(StudentInfo.this,"Error :"+e, Toast.LENGTH_LONG).show();
		}
		lv=(ListView)findViewById(R.id.listView1);
		txt=(TextView)findViewById(R.id.textView3);
		txt2=(TextView)findViewById(R.id.textView2);
		System.out.println(rollno+" "+semister);
		txt2.setText("Semister : "+ semister);
		if(isNetworkAvailable()==true){
			new Fetchalldetails().execute();
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
		getMenuInflater().inflate(R.menu.student_info, menu);
		return true;
	}

	class Fetchalldetails extends AsyncTask<Void, Void, Void>
	{
	ProgressDialog pb;
     int flag=0; 
      @Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pb = new ProgressDialog(StudentInfo.this);
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
			if(flag==1){ Toast.makeText(StudentInfo.this, "Connection Timeout.. Try Again Later.", Toast.LENGTH_LONG).show();
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
			adapter = new FetchalldetailsAdapter(StudentInfo.this,nameal, rollal, classnameal, subjectal,batchal, semisteral, totallectal, totalattal,
						remarksal , prratingal, brratingal, teachernameal, updatedateal);
			lv.setAdapter(adapter);
		    lv.smoothScrollToPosition(adapter.getCount());
		    lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View v, int pos,
						long id) {
					// TODO Auto-generated method stub
					Toast.makeText(getApplicationContext(),"You Clicked on : "+nameal.get(pos), Toast.LENGTH_LONG).show();
					Intent i= new Intent(StudentInfo.this,SubjectInfo.class);
					i.putExtra("rollno",rollno);
					i.putExtra("semister",semisteral.get(pos));
					i.putExtra("name",nameal.get(pos));
					i.putExtra("subject",subjectal.get(pos));
					i.putExtra("batch",batchal.get(pos));
					i.putExtra("teachername",teachernameal.get(pos));
					i.putExtra("classname",classnameal.get(pos));
					i.putExtra("studentname",nameal.get(pos));
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
    			HttpPost httppost = new HttpPost("http://"+serverurl+"/myrecordsparents/fetchalldata.php?rollno="+rollno+"&semister="+semister);
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
    				 nameal.add(json.getString("name"));
    				 batchal.add(json.getString("batch"));
    				 rollal.add(json.getString("rollno"));
    				 classnameal.add(json.getString("class"));
    			     subjectal.add(json.getString("subject"));
    				 semisteral.add(json.getString("semister"));
    				 totallectal.add(json.getString("totallect"));
    				 totalattal.add(json.getString("attendance"));
    				 remarksal.add(json.getString("remarks"));
    				 prratingal.add(json.getString("performancerating"));
    				 brratingal.add(json.getString("behaviourrating"));
    				 teachernameal.add(json.getString("teachername"));
    				 updatedateal.add(json.getString("updatedate"));
    				// System.out.println(record1);
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
	    { Intent i = new Intent(StudentInfo.this, SelectSemYear.class );
	    i.putExtra("rollno",rollno);
		i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivity(i);
		overridePendingTransition(R.anim.push_left_in,android.R.anim.fade_out);
		finish();
		
	    return true;
    }
return false;
}


}
