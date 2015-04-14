import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.ImageBuffer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.renderer.Renderer;

class Pos
{
	private double x;
	private double y;
	public double getX()
	{
		return x;
	}
	public double getY()
	{
		return y;
	}
	public void setX(double x)
	{
		this.x=x;
	}
	public void setY(double y)
	{
		this.y=y;
	}
	Pos()
	{
		x=0;
		y=0;
	}
	Pos(double x,double y)
	{
		this.x=x;
		this.y=y;
	}	
	public void move(double dx, double dy)
	{
		x+=dx;
		y+=dy;
	}
}
class Brick
{
	private int life;
	private int type;
	private Pos pos;
	private double xVel=0.2;
	public double getXVel(){return xVel;}
	public void setXVel(double xVel){this.xVel=xVel;}
	public Pos getPos()
	{
		return pos;
	}
	public double getX()
	{
		return pos.getX();
	}
	public double getY()
	{
		return pos.getY();
	}
	public void incX(double x){pos.setX(pos.getX()+x);}
	public int getType(){return type;}
	public void setType(int type){this.type=type;}
	public void setPos(Pos pos){this.pos=pos;}
	public int getLife(){return life;}
	public void reduceLife(){life--;}
	Brick(int type,int x,int y, double xVel,int life)
	{
		this.type=type;
		this.pos = new Pos(x,y);
		this.xVel=xVel;
		this.life=life;
	}
}

class Player
{
	private int lives=3;
	private int score=0;
	private Pos pos;
	private int width=Breakout.playerWidth;//will be used when powerups are added(that change the width) and 2 players can play 
	private int height=Breakout.playerHeight;
	private int numBalls=0;
	private double xVel=1;
	public int getScore(){return score;}
	public void incScore(int score){this.score+=score;}
	public void decLives(int lives){this.lives-=lives;}
	public void incLives(int lives){this.lives+=lives;}//can be done with just 1 method but this looks nicer
	public int getLives(){return lives;}
	public void setPos(int x,int y)
	{
		pos.setX(x);
		pos.setY(y);
	}
	public void move(double dx,double dy)
	{
		pos.move(dx,dy);
	}
	public void moveX(double dx)
	{
		pos.setX(pos.getX()+dx);
	}
	public void moveX()
	{
		pos.setX(pos.getX()+xVel);
	}
	public void decBall(){numBalls--;}
	public void incBall(){numBalls++;}
	public int getBalls(){return numBalls;}
	public double getX(){return pos.getX();}
	public double getY(){return pos.getY();}
	public void setX(int x){pos.setX(x);}
	public void setXVel(int xVel){this.xVel=xVel;}
	public double getXVel(){return xVel;}
	public int getWidth(){return width;}
	public int getHeight(){return height;}
	public void setWidth(int width){this.width=width;}
	public void setHeight(int height){this.height=height;}
	Player()
	{
		pos = new Pos(0,Breakout.height-30);
	}
	Player(double x, double y)
	{
		pos = new Pos(x,y);
	}
	Player(double x)
	{
		pos = new Pos(x,Breakout.height-30);
	}
}
class Ball
{
	private final int owner;
	private Pos pos;
	private double xVel;
	private double yVel;
	public double getX() {return pos.getX();};
	public double getY() {return pos.getY();};
	public void setPos(Pos pos)
	{
		this.pos.setX(pos.getX());
		this.pos.setY(pos.getY());
	}
	public void setPos(double d,double e)
	{
		this.pos.setX(d);
		this.pos.setY(e);
	}
	public void move()
	{
		pos.move(xVel,yVel);
	}
	public int getOwner(){return owner;}
	public double getXVel(){return xVel;}
	public double getYVel(){return yVel;}
	public void setXVel(double vel)
	{
		xVel=vel;
	}
	public void setYVel(double vel)
	{
		yVel=vel;
	}
	public void reverseX(){xVel*=(-1);}
	public void reverseY(){yVel*=(-1);}
	Ball(int x,int y, double xVel, double yVel, int owner)
	{
		pos=new Pos(x,y);
		this.xVel=xVel;
		this.yVel=yVel;
		this.owner=owner;
	}
}

class Button
{
	private Pos pos;
	private int width;
	private int height;
	private boolean isVisible;
	private boolean isActive;
	private String methodName;
	private Class[] types;
	private Object[] args;
	public Pos getPos(){return pos;}
	public int getWidth(){return width;}
	public int getHeight(){return height;}
	public boolean getActive(){return isActive;}
	public boolean getVisible(){return isVisible;}
	public void setVisibility(boolean vis)
	{
		isVisible=vis;
	}
	public void setActive(boolean active)
	{
		isActive=active;
	}
	public void invoke()
	{
		try
		{
			Class cls = Class.forName("Breakout");
			Method m=cls.getMethod(methodName, types);
			Breakout methodObject = new Breakout("test");
			m.invoke(methodObject, args);
		}
		catch(Throwable e)
		{
			System.out.println(e);
		}
	}
	public void invoke(Object[] args)
	{
		this.args=args;
		this.invoke();
	}
	Button(String methodName,Class[] types,Object[] args,int x,int y, int width,int height,boolean isVisible,boolean isActive)
	{
		this.methodName=methodName;
		this.types=types;
		this.args=args;
		this.pos = new Pos(x,y);
		this.width=width;
		this.height=height;
		this.isVisible=isVisible;
		this.isActive=isActive;
	}
	Button(String methodName,Class[] types,Object[] args,int x,int y, int width,int height)
	{
		this(methodName,types,args,x,y,width,height,true,true);
	}
}	
class Particle
{
	private Pos pos;
	private double xVel;
	private double yVel;
	private int halfLife;
	private int life=0;
	private double Dist;//can be used
	private int width;
	private int height;
	private int type;
	public Pos getPos(){return pos;}
	Particle(Pos pos,int width,int height,double xVel, double yVel, int halfLife,int type)
	{
		this.pos=pos;
		this.width=width;
		this.height=height;
		this.xVel=xVel;
		this.yVel=yVel;
		this.halfLife=halfLife;
		this.type=type;
	}
	public void move()
	{
		pos.move(xVel,yVel);
		
	}
	public int getWidth(){return width;}
	public int getHeight(){return height;}
	public int getHalfLife(){return halfLife;}
	public int getLife(){return life;}
	public int getType(){return type;}
	public void incLife()
	{
		life++;
	}
	public double getX(){return pos.getX();}
	public double getY(){return pos.getY();}
	public double getXVel(){return xVel;}
	public double getYVel(){return yVel;}
	public double getXOffset()
	{
		return pos.getX() + life*xVel;
	}
	public double getYOffset()
	{
		return pos.getY() + life*yVel;
	}
	public boolean isDead()
	{
		if(life>=halfLife)
			return true;
		return false;
	}
	
}
enum GameState
{
	startGame,inGame,paused, gameOver
}
public class Breakout extends BasicGame {
	static boolean isBasic=true;
	static boolean isLoaded=false;
	static int width=800;
	static int height=600;
	static int targetFPS=60;
	static int ballRadius=40;
	static int brickWidth=50;
	static int brickHeight=10;
	static int playerWidth=70;
	static int playerHeight=10;
	static int wallSpace=ballRadius+10;
	static int level=1;
	static int frameCount=0;
	static int tempFrame=0;
	static int randSeed=1;
	static int numPlayers=2;
	static double velOffset=1;
	static double gameOffset=10;
	static double maxBallVel=1.0*velOffset;
	static double maxBrickVel=0.2*velOffset;
	static GameState gameState=GameState.startGame;
	static ArrayList<Ball> ball = new ArrayList<Ball>();
	static ArrayList<Brick> brick = new ArrayList<Brick>();
	static ArrayList<Player> player = new ArrayList<Player>();
	static ArrayList<Button> button = new ArrayList<Button>();
	static ArrayList<Particle> particle = new ArrayList<Particle>();
	static ArrayList<ImageBuffer> bufParticle = new ArrayList<ImageBuffer>();
	static ArrayList<Image> imageParticle = new ArrayList<Image>();
	static Image playerImage;
	static Image[] brickImage;
	static Image ballImage;
	static Image backgroundImage;
	static Image[] buttonImage;
	public static Class[] getClass(Class... cls){return cls;}
	public static Object[] getObj(Object... obj){return obj;}
	public Breakout(String gameName)
	{
		super(gameName);
	}
	@Override public void init(GameContainer gc) throws SlickException
	{
		//Renderer.setRenderer(Renderer.VERTEX_ARRAY_RENDERER);
		System.out.println("init");

	}
	@Override public void update(GameContainer gc, int i) throws SlickException 
	{
		frameCount++;
		Input input=gc.getInput();
		boolean mouseDown = input.isMousePressed(0);
		boolean pDown=input.isKeyPressed(input.KEY_P);
		int mouseX=input.getMouseX();
		int mouseY=input.getMouseY();
		Player Player1=player.get(0);
		Player Player2;
		if(player.size()>=2)
			Player2=player.get(1);
		if(mouseDown)
		{
			for(int k=0;k<button.size();k++)
			{
				Button tempButton=button.get(k);
				if(tempButton.getActive())
					if(checkCollision(tempButton,mouseX,mouseY))
					{
						tempButton.invoke();
					}
			}
		}
		if(input.isKeyPressed(input.KEY_4))
		{
			createGame(++level);
		}
		int playerSpeed=10;
		if(input.isKeyDown(input.KEY_A))
		{	
			if(Player1.getX()>playerSpeed)
				Player1.moveX(-playerSpeed);
			else
				Player1.setX(0);
		}
		if(input.isKeyDown(input.KEY_D))
		{	
			if(Player1.getX()<width-playerSpeed-Player1.getWidth())
				Player1.moveX(playerSpeed);
			else
				Player1.setX(width-Player1.getWidth());
		}
		if(player.size()>1)
		{
			Player2=player.get(1);
			if(input.isKeyDown(input.KEY_LEFT))
			{	
				if(Player2.getX()>playerSpeed)
					Player2.moveX(-playerSpeed);
				else
					Player2.setX(0);
			}
			if(input.isKeyDown(input.KEY_RIGHT))
			{	
				if(Player2.getX()<width-playerSpeed-Player2.getWidth())
					Player2.moveX(playerSpeed);
				else
					Player2.setX(width-Player2.getWidth());
			}
		}
		if(mouseDown && gameState==GameState.gameOver)
		{
			gc.exit();
		}
		if(pDown && gameState==GameState.paused)
			gameState=GameState.inGame;
		else if(pDown && gameState==GameState.inGame)
			gameState=GameState.paused;
		if(gameState==GameState.paused)
			if(mouseDown)
			{
				
				gameState=GameState.inGame;
			}
		
		if(gameState==GameState.inGame)
		{
			for(int z=0;z<gameOffset;z++)
			{
			for(int j=0;j<ball.size();j++)
			{
				Ball tempBall=ball.get(j);
				tempBall.move();
				double x=tempBall.getX();
				double y=tempBall.getY();
				Player tmpPlayer=player.get(tempBall.getOwner());
				if(x<=10 || x>=width-10)
					tempBall.reverseX();
				if(y>=height-10)
				{
					if(tmpPlayer.getBalls()==1)
					{
						tmpPlayer.decLives(1);
						if(ball.size()==1)
							gameState=GameState.paused;
						
						if(tmpPlayer.getLives()<=0)
						{
							player.remove(tempBall.getOwner());
							ball.remove(j);
							if(player.size()==0)
							{
								gameState=GameState.gameOver;
							}
							break;
						}
						
						
					}
					int owner=tempBall.getOwner();
					if(tmpPlayer.getBalls()==1)
					{
						Player tempPlayer=player.get(tempBall.getOwner());
						tempBall.setPos(tempPlayer.getX()+tempPlayer.getWidth()/2-ballRadius/2,tempPlayer.getY()-ballRadius);
						//double yVel=rand.nextDouble()*maxBallVel;
						double yVel=Rand(0.3*maxBallVel,maxBallVel*0.8);
						double xVel;
						yVel*=0.7;
						yVel+=0.3*maxBallVel;
						yVel*=(-1);
						xVel=Math.sqrt((maxBallVel*maxBallVel)-(yVel*yVel));
						tempBall.setYVel(yVel);
						tempBall.setXVel(xVel);
					}
					else
					{
						player.get(tempBall.getOwner()).decBall();
						ball.remove(j);
						continue;
					}
					if(player.get(tempBall.getOwner()).getLives()==0)
					{
						player.get(tempBall.getOwner()).decBall();
						player.remove(tempBall.getOwner());
						ball.remove(j);
						continue;
					}
					
				}
				if(y<=10)
					tempBall.reverseY();
				
				for(int k=brick.size()-1;k>=0;k--)
				{
					Brick tempBrick=brick.get(k);
					int collide=checkCollision(tempBrick,tempBall);
					if(collide!=-1)
					{
						if(collide==3 || collide==4)
							tempBall.reverseY();
						else
							tempBall.reverseX();
						tempBall.move();
						if(tempBrick.getType()==1)
							break;
						player.get(tempBall.getOwner()).incScore(1);
						tempBrick.reduceLife();
						if(tempBrick.getLife()==0)
						{
							createParticle(tempBrick.getX() + (brickWidth/2),tempBrick.getY() + (brickHeight/2),45,0);
							brick.remove(k);
						}
						break;
					}
				}
				
				for(int k=0;k<player.size();k++)
				{
					checkCollision(player.get(k),ball.get(j));
				}
			}
			boolean changeXVel=false;
			for(int k=0;k<brick.size();k++)
			{
				Brick tempBrick=brick.get(k);
				tempBrick.incX(tempBrick.getXVel());
				if(tempBrick.getX()<=wallSpace || tempBrick.getX()+brickWidth>width-wallSpace)
					changeXVel=true;
			}
			if(changeXVel)
			{
				for(int k=0;k<brick.size();k++)
				{
					Brick tempBrick=brick.get(k);
					tempBrick.setXVel(tempBrick.getXVel()*(-1));
				}
			}
			boolean create=false;
			if(brick.size()==0)
				createGame(++level);
			else
			{
				create=true;
				for(int k=0;k<brick.size();k++)
				{
					Brick tempBrick=brick.get(k);
					if(tempBrick.getType()==0)
					{
						create=false;
						break;
					}
				}
				if(create)
					createGame(++level);
			}
			for(int k=particle.size()-1;k>=0;k--)
			{
				Particle tempPart=particle.get(k);
				tempPart.incLife();
				if(tempPart.isDead())
				{
					particle.remove(k);
					continue;
				}
				
				int Width=tempPart.getWidth();
				int Height=tempPart.getHeight();
				double xOffset=tempPart.getXOffset();
				double yOffset=tempPart.getYOffset();
				if(xOffset+Width>width || xOffset+Width<0)
				{
					particle.remove(k);
					continue;
				}
				if(yOffset+Height>height || yOffset+Height<0)
					particle.remove(k);
			}
		}
		}
	}
	@Override public void render(GameContainer gc, Graphics g) throws SlickException 
	{
		//start screen
		if(!isLoaded)
		{
			ImageBuffer bufParticle1 = new ImageBuffer(3,3);
			//ImageBuffer bufParticle2 = new ImageBuffer(2,3);
			int[] colors1={255,255,255};
			//int[] colors2={0,255,0};
			bufParticle1.setRGBA(0, 0, colors1[0], colors1[1], colors1[2], 0);
			bufParticle1.setRGBA(0, 1, colors1[0], colors1[1], colors1[2], 255);
			bufParticle1.setRGBA(0, 2, colors1[0], colors1[1], colors1[2], 0);
			bufParticle1.setRGBA(1, 0, colors1[0], colors1[1], colors1[2], 255);
			bufParticle1.setRGBA(1, 1, colors1[0], colors1[1], colors1[2], 0);
			bufParticle1.setRGBA(1, 2, colors1[0], colors1[1], colors1[2], 255);
			bufParticle1.setRGBA(2, 0, colors1[0], colors1[1], colors1[2], 0);
			bufParticle1.setRGBA(2, 1, colors1[0], colors1[1], colors1[2], 255);
			bufParticle1.setRGBA(2, 2, colors1[0], colors1[1], colors1[2], 0);
			imageParticle.add(new Image(bufParticle1));
			try
			{
				buttonImage = new Image[4];
				buttonImage[0]=new Image("gfx1/start2.png");
				buttonImage[1]=new Image("gfx1/gfx.png");
				buttonImage[2]=new Image("gfx1/basic.png");
				buttonImage[3]=buttonImage[0];
			}
			catch(Throwable e)
			{
				System.out.println(e);
			}
			isLoaded=true;
		}
		if(isBasic)
			g.setBackground(Color.black);
		else
			g.drawImage(backgroundImage, 0, 0);
		for(int i=0;i<button.size();i++)
		{
			Button tempButton=button.get(i);
			if(tempButton.getVisible())
			{
				g.drawImage(buttonImage[i],(float)tempButton.getPos().getX(),(float)tempButton.getPos().getY());
			}
		}
		if(gameState==GameState.startGame)
		{
				/*Image image;
				Texture text;
				text=TextureLoader.*/
		}
		//game
		if(gameState==GameState.inGame || gameState==GameState.paused)
		{
			Player tempPlayer=player.get(0);
			g.setColor(Color.white);
			g.drawString("Score: " + tempPlayer.getScore() + "\nLives: " + tempPlayer.getLives(), 10,30);
			g.drawString("Particle count: " + particle.size(),500,30);
			if(isBasic)
			{
				for(int i=0;i<brick.size();i++)
				{
					Brick tempBrick=brick.get(i);
					int type = tempBrick.getType();
					if(type==0)
					{
						int life = tempBrick.getLife();
						g.setColor(new Color((life*55)%255,(life*130)%255,(life*225)%255,255));
					}
					if(type==1)
					{
						g.setColor(new Color(100,100,100,255));
						g.fillRect((int)tempBrick.getX(),(int)tempBrick.getY(),brickWidth,brickHeight);
					}
					g.drawRect((int)tempBrick.getX(),(int)tempBrick.getY(),brickWidth,brickHeight);
				}
				for(int i=0;i<ball.size();i++)
				{
					g.setColor(new Color(255,0,0,255));
					g.drawOval((int)ball.get(i).getX(),(int)ball.get(i).getY(),ballRadius,ballRadius);
				}
				for(int i=0;i<player.size();i++)
				{
					if(i==0)
						g.setColor(Color.blue);
					if(i==1)
						g.setColor(Color.green);
					Player tmpPlayer=player.get(i);
					g.drawRect((int)tmpPlayer.getX(),(int)tmpPlayer.getY(),tmpPlayer.getWidth(),tmpPlayer.getHeight());
				}
				for(int i=0;i<particle.size();i++)
				{
					Particle tempPart = particle.get(i);
					int type=tempPart.getType();
					if(imageParticle.size()>=type)
					{
						Image tempImage=imageParticle.get(type);
						tempImage.startUse();
						tempImage.drawEmbedded((int)tempPart.getXOffset(), (int)tempPart.getYOffset(), tempPart.getWidth(), tempPart.getHeight());		
						tempImage.endUse();
					}
				}
			}
			else
			{
				
				for(int i=0;i<brick.size();i++)
				{
					Brick tempBrick=brick.get(i);
					if(tempBrick.getType()>0)
						brickImage[tempBrick.getType()-1].draw((float)tempBrick.getX(),(float)tempBrick.getY(),50,10);
						//g.drawImage(brickImage[tempBrick.getType()-1],(float)tempBrick.getX(),(float)tempBrick.getY());
					//brickImage[0].
					//g.drawImage
				}
				for(int i=0;i<ball.size();i++)
				{
					g.drawImage(ballImage, (float)ball.get(i).getX(), (float)ball.get(i).getY());
				}
				for(int i=0;i<player.size();i++)
				{
					g.drawImage(playerImage, (float)player.get(i).getX(), (float)player.get(i).getY());
				}
			}
		}//pause
		if(gameState==GameState.paused)
		{
			g.drawString("Paused",width/2-50,(int)(height/1.5));
		}
		if(gameState==GameState.gameOver)
		{
			g.drawString("Game over", width/2-50,(int)(height/1.5));
			g.drawString("Score: " + player.get(0).getScore(),width/2-50,(int)(height/1.5)+40);
		}
	}
	public static void Init()
	{
		player.add(new Player(width/2-width/4));
		player.add(new Player(width/2+width/4));
		
		button.add(new Button("startGame",getClass((Class[])null),getObj((Object[])null),250,300,300,100));
		button.add(new Button("setTextures",getClass(String.class),getObj("gfx1"),100,100,300,100));
		button.add(new Button("setTextures",getClass(boolean.class),getObj(true),450,100,300,100));
		button.add(new Button("setTextures",getClass(String.class),getObj("gfx2"),100,100,50,50,false,false));
		setTextures(true);
	}
	public static void setTextures(String folder)
	{
		isBasic=false;
		ballRadius=40;
		brickWidth=143;
		brickHeight=90;
		playerWidth=132;
		playerHeight=64;
		try
		{
			playerImage = new Image(folder+"/player.png");
			ballImage = new Image(folder+"/ball.png");
			backgroundImage = new Image(folder+"/bg.png");
			brickImage=new Image[8];
			for(int i=0;i<8;i++)
			{
				brickImage[i]=new Image(folder+"/"+(i+1)+".png");
			}
		}
		catch(Throwable e)
		{
			System.out.println(e);
		}
	}
	public static void setTextures(boolean basic)
	{
		isBasic=basic;
		ballRadius=20;
		brickWidth=50;
		brickHeight=10;
		playerWidth=70;
		playerHeight=10;
	}
	public static void main(String[] args) 
	{
		Init();
		// MENU OPTION IN START SCREEN TWO PLAYERS (future)
		
		try
		{
			AppGameContainer appgc;
			appgc = new AppGameContainer(new Breakout("Breakout"));
			appgc.setDisplayMode(width,height,false);
			appgc.setTargetFrameRate(targetFPS);
			appgc.start();
		}
		catch (SlickException ex)
		{
			Logger.getLogger(Breakout.class.getName()).log(Level.SEVERE,null,ex);
		}
	}
	public static void deleteGame()//probably not needed
	{
		
	}
	public static void createBall(int x,int y,double xVel, double yVel,int owner)
	{
		ball.add(new Ball(x,y,xVel,yVel,owner));
	}
	public static void createParticle(double x, double y,int n, int formation)
	{
		switch(formation)
		{
			case(0):
			{
				double angle=Math.PI*2;
				angle/=n;
				double Vel=1;
				double xVel=0;
				double yVel=0;
				double velOffset=10;
				for(int i=0;i<n;i++)
				{
					xVel=velOffset*Math.sin(angle*i)/Vel;
					yVel=velOffset*Math.cos(angle*i)/Vel;
					particle.add(new Particle(new Pos(x,y),3,3,xVel,yVel,100,0));
				}
				break;
			}
		}
	}
	public static void createGame(int Level)
	{
		//randomize the kind of layout
		int n = ball.size();
		if(n==0)
		for(int i=0;i<player.size();i++)
		{
			Player tempPlayer=player.get(i);
			tempPlayer.incBall();
			ball.add(new Ball((int)tempPlayer.getX()+tempPlayer.getWidth()/2-ballRadius/2,(int)tempPlayer.getY()-ballRadius-1,0,maxBallVel*-1,i));
		}
		else if(n==1)
		{
			Ball tempBall=ball.get(0);
			tempBall.setPos(player.get(0).getX()+playerWidth/2-ballRadius/2,player.get(0).getY()-ballRadius-1);
			tempBall.setXVel(0);
			tempBall.setYVel(-1);
		}
		else
		{
			double angle=(Math.PI/2)/(n-1);
			double Angle=0;
			for(int i=0;i<n;i++)
			{
				Ball tempBall=ball.get(i);
				tempBall.setPos(player.get(0).getX()+playerWidth/2-ballRadius/2,player.get(0).getY()-ballRadius-1);
				Angle=angle*i+Math.PI/4;
				double xVel=maxBallVel*Math.cos(Angle);
				double yVel=maxBallVel*Math.sin(Angle)*-1;
				tempBall.setXVel(xVel);
				tempBall.setYVel(yVel);
			}
		}
		int numX=9;
		int numY=6;
		int num=numX*numY;
		brick.clear();
		int type=0;
		int life=Level;
		if(Level>8)
			life=8;
		for(int i=0;i<num;i++)
		{
			type=Rand(0,9)%2;
			brick.add(new Brick(type,wallSpace+10+(i%numX)*(brickWidth+20),50+(i/numX)*(brickHeight+10),maxBrickVel,life));
		}
	}
	public static void startGame()
	{
		for(int i=0;i<3;i++)
		{
			button.get(i).setVisibility(false);
			button.get(i).setActive(false);
		}
		if(numPlayers==1)
			player.remove(1);
		else
		{
			player.get(0).setWidth((int)(playerWidth/1.5));
			player.get(1).setWidth((int)(playerWidth/1.5));
			
		}
		createGame(level);
		gameState=GameState.inGame;
	}
	public static void checkCollision(Player a, Ball b)
	{
		int x1=(int)a.getX();
		int x2=x1+a.getWidth();
		int y1=(int)a.getY();
		int y2=y1+a.getHeight();
		int collideX=0;
		int X1=(int)b.getX();
		if(x1 <X1 + ballRadius && x2 > X1)
			if(y1<b.getY()+ballRadius && y2>b.getY())
			{
				collideX=X1 - x1;
				double angle=((Math.PI/4*3)-(Math.PI/4))*(double)collideX/a.getWidth()+Math.PI/4;
				b.setXVel(maxBallVel*Math.cos(angle)*-1);
				b.setYVel(maxBallVel*Math.sin(angle)*-1);
			}
	}
	public static int checkCollision(Brick a, Ball b)
	{
		int x1=(int)a.getX();
		int x2=x1+brickWidth;
		int y1=(int)a.getY();
		int y2=y1+brickHeight;
		int X1=(int)b.getX();
		int X2=X1+ballRadius;
		int Y1=(int)b.getY();
		int Y2=Y1+ballRadius;
		if(x1 <= X2 && x2 >= X1)
			if(y1<=Y2 && y2>=Y1)
			{
				double sides[]={0,0,0,0};
				sides[0]=dist(x1,X2);
				sides[1]=dist(x2,X1);
				sides[2]=dist(y1,Y2);
				sides[3]=dist(y2,Y1);
				for(int i=0;i<4;i++)
				{
					if(sides[i]==0)
						return i+1;
				}
				return 0;
			}
		return -1;
	}
	public static boolean checkCollision(Button a,int x,int y)
	{
		int X1=(int)a.getPos().getX();
		int Y1=(int)a.getPos().getY();
		int X2=X1+a.getWidth();
		int Y2=Y1+a.getHeight();
		if(x>X1 && x<X2)
			if(y>Y1&&y<Y2)
				return true;
		return false;
	}
	public static double dist(double x1, double x2)
	{
		return Math.abs(x1-x2);
	}
	public static double distY(Brick a, Ball b)//possible future optimization
	{
		double dist=0;
		double y1=b.getY();
		double y2=a.getY();
		dist=Math.abs(y1-y2);
		return dist;
	}
	public static int Rand(int min, int max)
	{
		Random rand = new Random();
		rand.setSeed(System.currentTimeMillis()+(++randSeed));
		return rand.nextInt(max-min)+min;
	}
	public static double Rand(double min, double max)
	{
		Random rand = new Random();
		rand.setSeed(System.currentTimeMillis());
		return rand.nextDouble()*(max-min)+min;
	}
}

/* The following comments are reminders/thoughts for me

OBJECT WIDTH/HEIGHT CONSTANT?



 Possible goals:
 randomized maps (follows pattern) aka formations
 temporary powerups, permanent powerups
 2 players: movement "ad", "left right", 1st player data: left screen, 2nd player data: right screen

POWERUPS: Activate via button:
increasePlayerWidth
addLives
addBall - already exists
ball.getOwner()

TEXT FILES: level data read, highscore write
 */
