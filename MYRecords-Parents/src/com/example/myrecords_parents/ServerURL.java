package com.example.myrecords_parents;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ServerURL extends Activity {
	Button save;
	EditText edt;
	String url;
	SharedPreferences spref;
	TextView currentserverurl;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_server_url);
		ActionBar bar = getActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#1f56af")));	
		save = (Button)findViewById(R.id.button1);
		edt =(EditText)findViewById(R.id.editText1);
		spref=(SharedPreferences)PreferenceManager.getDefaultSharedPreferences(this);
		currentserverurl=(TextView)findViewById(R.id.textView2);
		currentserverurl.setText("Current URL : "+spref.getString("ServerURL",""));
		save.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				url=edt.getText().toString();
				SharedPreferences.Editor spe = spref.edit();
				spe.putString("ServerURL",url);
				spe.commit();
				Toast.makeText(getApplicationContext(),"Server URL : "+ url +" Saved", Toast.LENGTH_SHORT).show();
				currentserverurl.setText("Current URL : "+spref.getString("ServerURL",""));
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.server_url, menu);
		return true;
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		 if(keyCode == KeyEvent.KEYCODE_BACK)
		    { Intent i = new Intent(ServerURL.this, MainActivity.class );
			i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			startActivity(i);
			overridePendingTransition(R.anim.push_left_in,android.R.anim.fade_out);
			finish();
			
		    return true;
	    }
	return false;
	}
}
