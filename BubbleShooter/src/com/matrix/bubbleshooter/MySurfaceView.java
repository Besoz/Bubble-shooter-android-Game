package com.matrix.bubbleshooter;
 
import java.util.ArrayList;
import java.util.Random;
 
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.SoundPool;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
 
 
 
public class MySurfaceView extends SurfaceView implements Runnable
{
 
	SurfaceHolder myHolder;
	public boolean hanged;
	Thread myThread = null;
	public static boolean isRunning = false , isMoving = false , isExit = false;
	public float fX,fY,aniX,aniY,cx,cy,dx,dy;
	public Bitmap ball,currBall,nexBall,viBall;
	public Bitmap red , yellow,blue,green;
	Bitmap gameover,copy,youWin,copyWin;
	public static boolean initializer = true;
	public static ArrayList< ArrayList<Integer> > blocks;
	public static Random randomBalls;
	public int  currentBall,nextBall,viewBall;
	float canvasHeight;
	float canvasWidth;
	boolean[][] visited;
	public int scaledWidth,directionSign ,directionSignY,maxRows,surrounding;
	float ballHeight,ballWidth;
	public Bitmap gameBackGround;
 
	boolean gameOverZoom;  // Initially , if he lose gameOver = true; to stop scaling set false;
	int finalWidth;
	int startWidth;
	int finalHieght;
	int startHieght;
	int aniWidth ;
	int aniHieght ;
	public boolean touch = true;
	int ballNum;
	public static SoundPool sp;
	int shoot;
	int fall;
	int stick;
	int winSound;
	int loseSound;
	boolean youWinBoolean ,youLoseBoolean=true;
	int score;
	Paint textPaint;
	int rowsNum;
	float deltaY,deltaX;
	int newLevelConstant;
	int newLevelCounter;
 
	public MySurfaceView(Context context) 
	{
		super(context);
		textPaint=new Paint();
		textPaint.setARGB(75, 254, 10, 50);
		textPaint.setTextSize(25);
		score=0;
		gameBackGround = BitmapFactory.decodeResource(getResources(),R.drawable.bg_game);
		sp= new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
 
		shoot=sp.load(context, R.raw.pop, 1);
		fall=sp.load(context, R.raw.bubble, 1);
		stick=sp.load(context, R.raw.thip, 1);
		winSound=sp.load(context, R.raw.uno_win, 1);
		loseSound=sp.load(context, R.raw.game_over, 1);
 
		myHolder = getHolder();
		fX = fY = aniX = aniY = aniWidth = aniHieght =  0;
		directionSign = 1;
		directionSignY=1;
		randomBalls = new Random();
		surrounding = 0;
 
 
		red = BitmapFactory.decodeResource(getResources(),R.drawable.red);
		yellow = BitmapFactory.decodeResource(getResources(),R.drawable.yellow);
		green = BitmapFactory.decodeResource(getResources(),R.drawable.green);
		blue = BitmapFactory.decodeResource(getResources(),R.drawable.blue);
 
		ball = BitmapFactory.decodeResource(getResources(),R.drawable.red);
 
		nextBall = randomBalls.nextInt(4)+1;
		viewBall = randomBalls.nextInt(4)+1;
		theBallColor();
 
	}
	
	public void theBallColor()
	{
		switch(nextBall)
	     {
		      case 1:
		      {
		    	  nexBall = red;
		    	  break;
		      }
		      case 2:
		      {
		    	  nexBall = blue;
		    	  break;
		      }
		      case 3:
		      {
		    	  nexBall = yellow;
		    	  break;
		      }
		      case 4:
		      {
		    	  nexBall = green;
		    	  break;
		      }
	     }
		switch(viewBall)
	     {
		      case 1:
		      {
		    	  viBall = red;
		    	  break;
		      }
		      case 2:
		      {
		    	  viBall = blue;
		    	  break;
		      }
		      case 3:
		      {
		    	  viBall = yellow;
		    	  break;
		      }
		      case 4:
		      {
		    	  viBall = green;
		    	  break;
		      }
	     }
		
	}
 
	
 
	public void initialize(Canvas canvas)
	 {
		ballNum=0;
		if(!mainMenu.gameSong.isPlaying() &&splash.globalSound) mainMenu.gameSong.start();
		score=0;
		youLoseBoolean=true;
		youWinBoolean=true;
		 touch=true;
		youWin = BitmapFactory.decodeResource(getResources(),R.drawable.youwin);
		copyWin = youWin;
		gameover=BitmapFactory.decodeResource(getResources(),R.drawable.gameover);
		copy = gameover;
	   gameOverZoom = true; // Initially , if he lose gameOver = true; to stop scalling set false;
	    touch=true;
	   finalWidth = gameover.getWidth();
	   startWidth = gameover.getWidth()/30 ; 
 
	   finalHieght = gameover.getHeight() ;
	   startHieght = gameover.getHeight()/30;
 
	   aniWidth = 0;
	   aniHieght = 0;
	   surrounding = 0;
 
	  blocks =  new ArrayList< ArrayList<Integer> >();
 
	  SharedPreferences getData = PreferenceManager.getDefaultSharedPreferences(getContext());
			  String rowsStr = getData.getString("list", "2");
 
			  if (rowsStr.contentEquals("1")) {
			   rowsNum = 3;
			   newLevelConstant=20;
			  } else if (rowsStr.contentEquals("2")) {
			   rowsNum = 5;
			   newLevelConstant=19;
			  } else if (rowsStr.contentEquals("3")) {
			   rowsNum = 6;
			   newLevelConstant=18;
			  } else if (rowsStr.contentEquals("4")) {
			   rowsNum = 7;
			   newLevelConstant=17;
			  } else if (rowsStr.contentEquals("5")) {
			   rowsNum = 8;
			   newLevelConstant=15;
			  }
			  newLevelCounter=newLevelConstant;
			  maxRows=(int) ((canvasHeight-3*ball.getHeight())/ball.getHeight());
 
			  for (int i = 0; i < maxRows + 3; i++) {
				   ArrayList<Integer> newArrayList = new ArrayList<Integer>();
				   blocks.add(newArrayList);
				  }
 
				  for (int i = 0; i < maxRows + 3; i++) {
				   for (int j = 0; j < canvas.getWidth(); j += ball.getWidth()) {
				    blocks.get(i).add(0);
				   }
				  }
 
				  for (int i = 0; i < rowsNum; i++) {
				   for (int j = 0; j < blocks.get(0).size(); j++) {
				    if (i % 2 != 0 && j == 0)
				     blocks.get(i).set(j, 0);
				    else{
				     blocks.get(i).set(j, randomBalls.nextInt(4) + 1);
				    // blocks.get(i).set(j,2+ 1);
				    ballNum++;}
				    // else blocks.get(i).add(2);
				   }
				  }
	  initializer = false;
	  isExit = false;
 
	 // ballNum=blocks.size()*blocks.get(0).size()-(blocks.size()/2);
	 }
 
	 public void addTwoRows() 
	 {
 
		  for (int i = maxRows + 1; i > 1; i--) 
		  {
		   for (int j = 0; j < blocks.get(0).size(); j++) 
		   {
			   blocks.get(i).set(j, blocks.get(i - 2).get(j));
		   }
		  }
 
		  for (int i = 0; i < 2; i++) 
		  {
		   for (int j = 0; j < blocks.get(0).size(); j++) 
		   {
			   if (i % 2 != 0 && j == 0)
				   blocks.get(i).set(j, 0);
			   else
			   {
				   blocks.get(i).set(j, randomBalls.nextInt(4) + 1);
				   ballNum++;
			   }
		   }
		  }
 
	 }
 
	public void scale(Canvas canvas){
		scaledWidth = (int) canvas.getWidth()/10; // = scaled height
		red = Bitmap.createScaledBitmap(red, scaledWidth, scaledWidth, false);
		yellow = Bitmap.createScaledBitmap(yellow, scaledWidth, scaledWidth, false);
		blue = Bitmap.createScaledBitmap(blue, scaledWidth, scaledWidth, false);
		green = Bitmap.createScaledBitmap(green, scaledWidth, scaledWidth, false);
		ball = Bitmap.createScaledBitmap(ball, scaledWidth, scaledWidth, false);
		nexBall = Bitmap.createScaledBitmap(nexBall, scaledWidth, scaledWidth, false);
		viBall = Bitmap.createScaledBitmap(viBall, scaledWidth, scaledWidth, false);
		gameBackGround = Bitmap.createScaledBitmap(gameBackGround, (int)canvasWidth,(int)canvasHeight , false);
 
	}
 
	public void pause() throws InterruptedException
	{
		isRunning = false;
		myThread.join();
		myThread = null;
	}
 
	public void resume()
	{
		gameover=BitmapFactory.decodeResource(getResources(),R.drawable.gameover);
		youWin = BitmapFactory.decodeResource(getResources(),R.drawable.youwin);
		
		  SharedPreferences getData = PreferenceManager.getDefaultSharedPreferences(getContext());
		  String rowsStr = getData.getString("list", "2");

		  if (rowsStr.contentEquals("1")) {
		   rowsNum = 3;
		   newLevelConstant=20;
		  } else if (rowsStr.contentEquals("2")) {
		   rowsNum = 5;
		   newLevelConstant=19;
		  } else if (rowsStr.contentEquals("3")) {
		   rowsNum = 6;
		   newLevelConstant=18;
		  } else if (rowsStr.contentEquals("4")) {
		   rowsNum = 7;
		   newLevelConstant=17;
		  } else if (rowsStr.contentEquals("5")) {
		   rowsNum = 8;
		   newLevelConstant=15;
		  }
		  
		isRunning = true;
		myThread = new Thread(this);
		myThread.start();
	}
 
 
	@Override
	public void run() 
	{ 
	Paint paint = new Paint();
	 paint.setColor(Color.DKGRAY);
	 paint.setStrokeWidth(7);
	 theBallColor();
	  while(isRunning)
	  {
 
	   if(!myHolder.getSurface().isValid()) continue;
 
	   Canvas  canvas = myHolder.lockCanvas();
	   canvas.drawColor(Color.BLACK);
 
 
	  canvasHeight = canvas.getHeight();
	  canvasWidth = canvas.getWidth();
 
	  scale(canvas);
	  canvas.drawBitmap(gameBackGround, 0, 0, null);
	   if(initializer || isExit) initialize(canvas);
 
	   ballHeight = ball.getHeight();
	   ballWidth = ball.getWidth();
	   canvas.drawLine(0, canvasHeight-ballHeight*3, canvas.getWidth(), canvasHeight-ballHeight*3, paint);
 
	   for(int i = 0 ; i < blocks.size() ; i++)
	   {
	    int k = 0 ;
	    for (int j = 0; j < canvasWidth; j+=ballWidth , k++) 
	    {
 
		     switch(blocks.get(i).get(k))
		     {
			      case 0: continue;
			      case 1:
			      {
			    	  ball = red;
			    	  break;
			      }
			      case 2:
			      {
			    	  ball = blue;
			    	  break;
			      }
			      case 3:
			      {
			    	  ball = yellow;
			    	  break;
			      }
			      case 4:
			      {
			    	  ball = green;
			    	  break;
			      }
		     }
 
		     if(i%2 == 0)
		     canvas.drawBitmap(ball, j , i*ballHeight, null);
		     else
		     canvas.drawBitmap(ball, j-ballWidth/2 , i*ballHeight, null);
 
		    }
	   }
 
	   canvas.drawBitmap(nexBall, canvasWidth/2-ballWidth/2, canvasHeight-ballHeight, null);
 
	   canvas.drawText("Next Ball:",ballWidth/2, canvasHeight-ballHeight/3, textPaint);
	   canvas.drawBitmap(viBall, 3*ballWidth, canvasHeight-ballHeight, null);
 
 
	   canvas.drawText("Score: "+score, canvasWidth-3*ballWidth, canvasHeight-ballHeight, textPaint);
 
	   if(isMoving)
	   {		   
		   cx = (canvasWidth/2-ballWidth/2) + aniX;
		   cy = (canvasHeight-ballHeight) + aniY;
 
		   canvas.drawBitmap(currBall,cx,cy, null); 
 
		   // do magic
		   doMagic();
		// Collided at left and right
		   if(dx-ballWidth/2 <= 0 || dx + ballWidth/2 >= canvasWidth) directionSign *= -1;	
 
		   if((fY - (canvasHeight-ballHeight)>0)){
			   directionSignY = -1;	
		   }else{
			   directionSignY = +1;
		   }
 
		 //  MainActivity.ballMoving = false;
 
		     deltaY= canvasHeight;
		     deltaX = deltaY / (directionSignY*(fY - (canvasHeight-ballHeight) ) / (fX - (canvasWidth/2-ballWidth/2)));
		     aniX= aniX + directionSignY*directionSign*(-deltaX )/30;
		     aniY=aniY+ (-deltaY )/30;
 
		   //aniX= aniX + directionSign*(fX - (canvasWidth/2-ballWidth/2) )/30;
		  // aniY=aniY+ directionSignY*(fY - (canvasHeight-ballHeight) )/30;
	   }
	   else
	   {
		   MainActivity.ballMoving = false;
	   }
 
 
	   if (!checkLose())
	   {	
		   if(loseSound !=0 && youLoseBoolean==true &&splash.globalSound){
			   mainMenu.gameSong.pause();
			   sp.play(loseSound, 1, 1, 0, 0, 1);
			   youLoseBoolean=false;
		   }
		   touch = false;
		   isMoving = false;
 
		   if(gameOverZoom)
		   {
			   zoomGameOver(false);
		   }
		   drawGameOver(canvas,gameover);
	   }
 
 
	   if(ballNum <= 0)
	   {
		   if(winSound !=0 && youWinBoolean==true&&splash.globalSound){
			   mainMenu.gameSong.pause();
			   sp.play(winSound, 1, 1, 0, 0, 1);
			   youWinBoolean=false;
		   }
		   touch = false;
		   isMoving = false;
		   if(gameOverZoom)
		   {
			   zoomGameOver(true);
		   }
		   drawGameOver(canvas,youWin);
	    }
 
 
	   myHolder.unlockCanvasAndPost(canvas);
 
 
	   try {myThread.sleep(16);}
	   catch (InterruptedException e) 
	   {e.printStackTrace();}
 
		}
	}
 
	public void zoomGameOver(boolean image)
	{
		if(!image){
			gameover=copy;
			gameover = Bitmap.createScaledBitmap(gameover,startWidth + aniWidth,startHieght + aniHieght , false);
 
			if(gameover.getWidth() >= finalWidth)
			{
				gameOverZoom = false; // stop looping
				return;
			}
			aniWidth += (finalWidth - startWidth)/10;
			aniHieght += (finalHieght - startHieght)/10;
		}
		else
		{
			youWin=copyWin;
			youWin = Bitmap.createScaledBitmap(youWin,startWidth + aniWidth,startHieght + aniHieght , false);
 
		     if(youWin.getWidth() >= finalWidth)
		     {
		        gameOverZoom = false; // stop looping
		        return;
		     }
		     aniWidth += (finalWidth - startWidth)/10;
		     aniHieght += (finalHieght - startHieght)/10;
		}
	}
 
 
 
	public boolean checkLose()
	{
		if(blocks.size() <= maxRows ) return true;
 
	    for (int i = 0 ; i < blocks.get(0).size() ; i ++)
	    {
           if (blocks.get(maxRows).get(i) != 0 )   return false;
        }
	   return true;
	}
 
	public void doMagic() 
	{
		dx = cx + ball.getWidth()/2; // center of shoot ball x 
		dy = cy + ball.getHeight()/2; // center of shoot ball y
 
		if(dy - ball.getWidth()/2 <= blocks.size()*ball.getHeight())
		{
			float levelEdge;
			for (int level = blocks.size() ; level > 0 ; level--) 
			{
				levelEdge = level * ball.getHeight();
				if(dy - ball.getWidth()/2 <= levelEdge)
				{
					float centralLevelEdge = levelEdge - ball.getHeight()/2; // y of center
					float verticalEdge;// x of center
					float distance; // distance between the 2 centers
					for (int vertical = 0; vertical < blocks.get(0).size(); vertical++) 
					{
						if( blocks.get(level-1).get(vertical) != 0 )// there is a ball here
						{
							if( (level-1) % 2 == 0) verticalEdge = ball.getWidth()/2 + (vertical*ball.getWidth()); //even level
							else verticalEdge = vertical*ball.getWidth(); // odd level
 
							distance = (float) Math.sqrt( Math.pow((float) (dx-verticalEdge), 2) + Math.pow((float)(dy-centralLevelEdge), 2));
 
							if(distance <= ball.getHeight() ) // interaction between balls (the 2 balls touch each other)
							{
								if(dx > verticalEdge || ( dx == verticalEdge && (canvasWidth/2-ball.getWidth()/2) <= verticalEdge))//from right
								{
									if(dy > centralLevelEdge)//Down right
									{
										int index = -1;
 
										if( (level-1)%2==0)
										{
											index = vertical+1; // index to place ball if level is even
											if(index >= blocks.get(0).size())
											{
//												isMoving = false; // stop moving ball
//												return; // don't put the ball
												index--;
											}
										}
										else index = vertical; // if level is odd
 
										//there is a level
 
											 blocks.get(level).set(index,currentBall);
											 isMoving = false;
 
 
 
											 ballNum++;
 
											 surrounding = 0;
											 visited = new boolean[blocks.size()][blocks.get(0).size()]; // Initially false
											fallDownCheck(level,index,currentBall);
											Log.d("debug","Surrounding = "+ surrounding);
											if(surrounding>=3)
											{ 
												Log.d("debug","Entered");
												 fallDown(level,index,currentBall);
												 findFallingBlocks();
											}else{
												 if(stick !=0&&splash.globalSound) sp.play(stick, 1, 1, 0, 0, 1);
												 score-=5;
											 }
 
												newLevelCounter--;
									           if (newLevelCounter==0)
									           {
									        	   newLevelCounter=newLevelConstant;
									        	   addTwoRows();
									           }
 
											 return;
									}
								}
								else  // from left or bottom to left
								{
									if(dy > centralLevelEdge) // Down Left
									{
										int index = -1;
 
										if((level-1)%2==0)	index = vertical; // index to place ball if level is even
										else
										{
											index = vertical-1; // if level is odd
											if(index < 0)
											{
//												// don't put the ball
////												isMoving = false;
////												return;
												index = 0;
											}
										}
 
 
										if(level%2 != 0 && index == 0 )
										{
//											isMoving = false;
//											return;
											index = 1;
										}
 
											 blocks.get(level).set(index,currentBall);
											 isMoving = false;
 
 
											 ballNum++;
											 surrounding = 0;
											 visited = new boolean[blocks.size()][blocks.get(0).size()]; // Initially false
											fallDownCheck(level,index,currentBall);
											Log.d("debug","Surrounding = "+ surrounding);
											if(surrounding>=3)
											 {
												Log.d("debug","Entered");
												 fallDown(level,index,currentBall);
												 findFallingBlocks();
											 }else{
												 if(stick !=0&&splash.globalSound) sp.play(stick, 1, 1, 0, 0, 1);
												 score-=5;
											 }
 
											   newLevelCounter--;
									           if (newLevelCounter==0)
									           {
									        	   newLevelCounter=newLevelConstant;
									        	   addTwoRows();
									           }
 
											 return;
 
 
									}
								}
							}
						}
					}
				}
				else	return; // get out if we don't reach the next levels
			}
		}
 
		if(cy <= 5)
		{
			for(int i = 0 ; i < blocks.get(0).size();i++)
			{
				if(cx+ball.getWidth()/2 <= i*ball.getWidth() + ball.getWidth())
				{
					blocks.get(0).set(i, currentBall);
					isMoving  = false;
 
 
 
					ballNum++;
					surrounding = 0;
					visited = new boolean[blocks.size()][blocks.get(0).size()]; // Initially false
					fallDownCheck(0,i,currentBall);
					if(surrounding>=3)
					{
						fallDown(0,i,currentBall);
						findFallingBlocks();
					}else{
						 if(stick !=0&&splash.globalSound) sp.play(stick, 1, 1, 0, 0, 1);
						 score-=5;
					 }
					   newLevelCounter--;
			           if (newLevelCounter==0)
			           {
			        	   newLevelCounter=newLevelConstant;
			        	   addTwoRows();
			           }
 
					return;
				}
			}
		}
	}
 
	public void fallDown(int x, int y, int type) // x--> row , y--> col
	{
	    if( x>=blocks.size() || x<0  || y>= blocks.get(0).size() || y<0 || blocks.get(x).get(y)!=type ) return;
 
	    blocks.get(x).set(y,0);
	    ballNum--;
	    score+=5;
	    if(fall !=0 &&splash.globalSound){
	    	sp.play(fall, 1, 1, 0, 0, 1);
 
	    try {myThread.sleep(10);}
		   catch (InterruptedException e) 
		   {e.printStackTrace();}
	    }
	    if (x%2==0) // even
	    {
	        fallDown(x,y+1,type);
	        fallDown(x,y-1,type);
 
	        fallDown(x-1,y,type );
	        fallDown(x-1,y+1,type);
 
	        fallDown(x+1,y,type);
	        fallDown(x+1,y+1,type);
	    }
	    else
	    {
	        fallDown(x,y+1,type);
	        fallDown(x,y-1,type);
 
	        fallDown(x-1,y,type );
	        fallDown(x-1,y-1,type);
 
	        fallDown(x+1,y,type);
	        fallDown(x+1,y-1,type);
	    }
	}
	public void fallDownCheck(int x, int y, int type)
	{
	    if( x>=blocks.size() || x<0  || y>= blocks.get(0).size() || y<0 || blocks.get(x).get(y)!=type || visited[x][y] || surrounding >= 3) return;
 
	    surrounding++;
	    Log.d("debug","in Surrounding = "+surrounding);
	    visited[x][y] = true;
 
	    if (x%2==0) // even
	    {
	    	fallDownCheck(x,y+1,type);
	    	fallDownCheck(x,y-1,type);
 
	    	fallDownCheck(x-1,y,type );
	    	fallDownCheck(x-1,y+1,type);
 
	    	fallDownCheck(x+1,y,type);
	    	fallDownCheck(x+1,y+1,type);
	    }
	    else //odd
	    {
	    	fallDownCheck(x,y+1,type);
	    	fallDownCheck(x,y-1,type);
 
	    	fallDownCheck(x-1,y,type );
	    	fallDownCheck(x-1,y-1,type);
 
	    	fallDownCheck(x+1,y,type);
	    	fallDownCheck(x+1,y-1,type);
	    }
	}
 
	public void fallDownNoColor(int x, int y) // x--> row , y--> col
	{
	    if( x>=blocks.size() || x<0  || y>= blocks.get(0).size() || y<0 || blocks.get(x).get(y)==0) return;
 
	    blocks.get(x).set(y,0);
	    ballNum--;
	    score+=10;
	    if(fall !=0&&splash.globalSound){ 
	    	sp.play(fall, 1, 1, 0, 0, 1);
	    try {myThread.sleep(10);}
		   catch (InterruptedException e) 
		   {e.printStackTrace();}
	    }
	    if (x%2==0) // even
	    {
	        fallDownNoColor(x,y+1);
	        fallDownNoColor(x,y-1);
 
	        fallDownNoColor(x-1,y);
	        fallDownNoColor(x-1,y+1);
 
	        fallDownNoColor(x+1,y);
	        fallDownNoColor(x+1,y+1);
	    }
	    else
	    {
	        fallDownNoColor(x,y+1);
	        fallDownNoColor(x,y-1);
 
	        fallDownNoColor(x-1,y );
	        fallDownNoColor(x-1,y-1);
 
	        fallDownNoColor(x+1,y);
	        fallDownNoColor(x+1,y-1);
	    }
	}
 
	public void findFallingBlocks()
	{
		visited = new boolean[blocks.size()][blocks.get(0).size()]; // Initially false
	    for(int i=0 ; i < blocks.size() ; i ++)
	    {
	        for (int j=0; j < blocks.get(0).size() ; j++)
	        {
	            hanged= false;
	            if (visited[i][j] == true || blocks.get(i).get(j)==0)   continue;
	            detectFall(i,j);
	            if(hanged == false)   fallDownNoColor(i,j);
	        }
	    }
	}
 
	public void detectFall(int x, int y) 
	{
	   if(x>=blocks.size() || x<0  || y>= blocks.get(0).size() || y<0 || blocks.get(x).get(y)==0 || visited[x][y]==true)         return;                                  
 
	        visited[x][y] = true;
	        if(x==0) hanged = true;
 
	        if (x%2==0) // even
	        {
	            detectFall(x,y+1);
	            detectFall(x,y-1);
 
	            detectFall(x-1,y);
	            detectFall(x-1,y+1);
 
	            detectFall(x+1,y);
	            detectFall(x+1,y+1);
	        }
	        else // odd
	        {
	            detectFall(x,y+1);
	            detectFall(x,y-1);
 
	            detectFall(x-1,y );
	            detectFall(x-1,y-1);
 
	            detectFall(x+1,y);
	            detectFall(x+1,y-1);
	        }
	}
 
	void drawGameOver(Canvas canvas,Bitmap image)
	{  
		  canvas.drawBitmap(image,canvas.getWidth()/2-image.getWidth()/2, canvas.getHeight()/2 - image.getHeight()/2  , null);
	}
 
 
 
 
}