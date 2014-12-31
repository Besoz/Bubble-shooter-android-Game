package com.matrix.bubbleshooter;
 
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.Touch;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
 
public class MainActivity extends Activity implements OnTouchListener{
 
        MySurfaceView myView;
        public static String fileName ="myData"; // file to put data
        SharedPreferences toSave;
        public static boolean ballMoving;
 
 
        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
                super.onCreate(savedInstanceState);
                myView = new MySurfaceView(this);
                myView.setOnTouchListener(this);
                setContentView(myView);
                toSave = getSharedPreferences(fileName, 0);
                ballMoving = false;
        }
 
 
 
        @Override
        protected void onPause()
        {
                super.onPause();
 
                if(mainMenu.gameSong.isPlaying() ) mainMenu.gameSong.pause();
 
                Log.d("debug","da5al fe onPause");
                SharedPreferences.Editor editor = toSave.edit();
                editor.putBoolean("touch", myView.touch);
                editor.putInt("newLevelCounter", myView.newLevelCounter);
                editor.putInt("maxRows", myView.maxRows);
                editor.putInt("ballNum", myView.ballNum);
                editor.putInt("nextBall", myView.nextBall);
                editor.putInt("viewBall", myView.viewBall);
                editor.putBoolean("youWinBoolean", myView.youWinBoolean);
                editor.putBoolean("youLoseBoolean",myView.youLoseBoolean);
                editor.putInt("score", myView.score);
                editor.commit();
 
                try
                {
                        myView.pause();
                }
                catch (InterruptedException e)
                {
                        e.printStackTrace();
                }
 
 
        }
 
        @Override
        protected void onResume()
        {
                Log.d("debug","da5al fe onResume");
                super.onResume();
 
                if(splash.globalSound ) mainMenu.gameSong.start();
 
                toSave = getSharedPreferences(fileName, 0);
                myView.touch=true;// toSave.getBoolean("touch", true);
                myView.maxRows = toSave.getInt("maxRows", 12);
                myView.ballNum = toSave.getInt("ballNum", 999);
                myView.newLevelCounter = toSave.getInt("newLevelCounter", 5);
                myView.nextBall = toSave.getInt("nextBall", 2);
                myView.viewBall = toSave.getInt("viewBall", 3);
                myView.youWinBoolean=toSave.getBoolean("youWinBoolean", true);
                myView.youLoseBoolean=toSave.getBoolean("youLoseBoolean", true);
                myView.score=toSave.getInt("score", 0);
                myView.resume();
        }
 
        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
 
                if(myView.touch)
                {
 
 
                	if(!ballMoving && event.getY() < (myView.canvasHeight - myView.ballHeight))
                	{
                		ballMoving = true;
	                	if(myView.shoot !=0&&splash.globalSound) myView.sp.play(myView.shoot, 1, 1, 0, 0, 1);
	                	myView.directionSign = 1;
 
	                	myView.aniX = myView.aniY = 0;
	                	myView.fX = event.getX()-(myView.ball.getWidth()/2);
	                	myView.fY = event.getY()-(myView.ball.getHeight()/2);
	                	
 
	                	myView.currentBall = myView.nextBall;
	                	myView.currBall = myView.nexBall;
 
	                	myView.nextBall = myView.viewBall;
	                	myView.nexBall = myView.viBall;
 
	                	myView.viewBall = MySurfaceView.randomBalls.nextInt(4)+1;
		                switch(myView.viewBall)
		                {
		                      case 1:
		                      {
		                          myView.viBall = myView.red;
		                          break;
		                      }
		                      case 2:
		                      {
		                          myView.viBall = myView.blue;
		                          break;
		                      }
		                      case 3:
		                      {
		                          myView.viBall = myView.yellow;
		                          break;
		                      }
		                      case 4:
		                      {
		                          myView.viBall = myView.green;
		                          break;
		                      }
		                }
 
		                myView.isMoving = true;
 
                	}
                }
                else
                {
                        openOptionsMenu();
                }
 
                return false;
        }
 
 
 
        @Override
        public boolean onCreateOptionsMenu(Menu menu)
        {
                // Inflate the menu; this adds items to the action bar if it is present.
                  getMenuInflater().inflate(R.menu.main, menu);
                  return true;
         }
         @Override
        public boolean onOptionsItemSelected(MenuItem item)
        {
 
                  switch(item.getItemId()){
                  case R.id.new_game:
                          myView.initializer=true;
                     break;
                  case R.id.exit_game:
 
                          myView.initializer=true;
 
                          MainActivity.this.finish();
 
                   break;
                  }
                  return true;
        }
 
        @Override
        public void onBackPressed() {
                // TODO Auto-generated method stub
 
                if(myView.touch==false){
                        myView.initializer=true;
                }
                super.onBackPressed();
        }
 
        @Override
        protected void onDestroy() {
                // TODO Auto-generated method stub
                super.onDestroy();
 
                Log.d("debug","da5al fe onDestroy");
                SharedPreferences.Editor editor = toSave.edit();
                editor.putBoolean("touch", myView.touch);
                editor.putInt("maxRows", myView.maxRows);
                editor.putInt("newLevelCounter", myView.newLevelCounter);
                editor.putInt("ballNum", myView.ballNum);
                editor.putBoolean("youWinBoolean", myView.youWinBoolean);
                editor.putBoolean("youLoseBoolean",myView.youLoseBoolean);
                editor.putInt("nextBall", myView.nextBall);
                editor.putInt("viewBall", myView.viewBall);
                editor.putInt("score", myView.score);
                editor.commit();
        }
 
 
}