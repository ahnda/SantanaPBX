package com.roguepanda.santanapbx;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;


public class SantanaPBXActivity extends Activity {
	public final static String SECRET_ID = "com.roguepanda.santanapbx.SECRET_ID";
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        NumberPicker secretId = (NumberPicker)findViewById(R.id.secretId);
        secretId.setMinValue(00);
        secretId.setMaxValue(99);
        secretId.setValue(01);
        
    }
    
    public void startPhreaking(View view) {
    	Intent intent = new Intent(this, PhreakPBXActivity.class);
    	NumberPicker secretId = (NumberPicker)findViewById(R.id.secretId);
    	int sid = secretId.getValue();
    	intent.putExtra(SECRET_ID, sid);
    	startActivity(intent);
    }
}