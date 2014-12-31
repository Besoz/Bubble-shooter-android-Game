package com.matrix.bubbleshooter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class splash extends Activity {
	static MediaPlayer ourSong;
	SharedPreferences saving;
	public static boolean globalSound;

	@Override
	protected void onCreate(Bundle makingBackgroud) {
		// TODO Auto-generated method stub
		super.onCreate(makingBackgroud);
		
		 SharedPreferences getData = PreferenceManager
				    .getDefaultSharedPreferences(getBaseContext());
				  globalSound=getData.getBoolean("sound_control", true);
		
		setContentView(R.layout.splash_xml);

		saving = getSharedPreferences("splash_sound", 0);
		ourSong = MediaPlayer.create(splash.this, R.raw.bubbly);
		saving = getSharedPreferences("splash_sound", 0);

		if (saving.getBoolean("sound", true) == true)  {
			if(globalSound) ourSong.start();
			else {ourSong.start(); 
				ourSong.pause();
			}
		}

		ourSong.setLooping(true);
		Thread timer = new Thread() {
			public void run() {
				try {
					sleep(4000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					if (!splash.this.isFinishing()) {
						Intent openMainActivity = new Intent("android.intent.action.MAINMENU");
						startActivity(openMainActivity);
					}
				}
			}
		};
		timer.start();
	
		
	
	
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		finish();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		ourSong.release();
		finish();

	}
}
