package com.matrix.bubbleshooter;



import com.matrix.bubbleshooter.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

public class mainMenu extends Activity {

	Button start, options, help, about;
	boolean soundAfterPause;
	
	public static MediaPlayer gameSong;
	SharedPreferences savedSound;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		
		
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
		
		soundAfterPause=false;
		
		start = (Button) findViewById(R.id.bStart);
		options = (Button) findViewById(R.id.bOptions);
		help = (Button) findViewById(R.id.bHelp);
		about = (Button) findViewById(R.id.bAbout);

		savedSound = getSharedPreferences("game_sound", 0);
		gameSong = MediaPlayer.create(this, R.raw.terminal_track);
		savedSound = getSharedPreferences("game_sound", 0);

		if (savedSound.getBoolean("sound", true) == true)  {
			 gameSong.start();
			gameSong.pause();
			
		}

		gameSong.setLooping(true);
		
		start.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				soundAfterPause=false;
				Intent intent=new Intent("android.intent.action.MAINACTIVITY");///////////////////////////////
						startActivity(intent);
						//splash.ourSong.pause();
			}
		});

		options.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				soundAfterPause=false;
				Intent intent=new Intent("android.intent.action.PREFRENCES");///////////////////////////////
				startActivity(intent);
				
			}
		});
		
		help.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				soundAfterPause=true;
				Intent intent=new Intent("android.intent.action.HELP");///////////////////////////////
				startActivity(intent);
			}
		});
		
		about.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				soundAfterPause=true;
				Intent intent=new Intent("android.intent.action.ABOUT");///////////////////////////////
				startActivity(intent);
			}	
		});

	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		 SharedPreferences getData = PreferenceManager
				    .getDefaultSharedPreferences(getBaseContext());
				  splash.globalSound=getData.getBoolean("sound_control", true);
		soundAfterPause=false;
		if(!splash.ourSong.isPlaying() &&splash.globalSound==true)
		splash.ourSong.start();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(soundAfterPause==false)splash.ourSong.pause();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		splash.ourSong.release();
	}
	
	
}


