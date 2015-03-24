import java.lang.reflect.*;
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
	private int type;
	private int genre;
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
	public void reduceType(){this.type--;}
	public void setType(int type){this.type=type;}
	public void setPos(Pos pos){this.pos=pos;}
	/*Brick()
	{
		this.type=1;
		this.pos = new Pos(0,0);
		this.xVel=maxBrickVel;
	}
	Brick(int x,int y)
	{
		this.type=1;
		this.pos = new Pos(x,y);
	}*/
	Brick(int type,int x,int y, double xVel)
	{
		this.type=type;
		this.pos = new Pos(x,y);
		this.xVel=xVel;
	}
}

class Player
{
	private int lives=3;
	private int score=0;
	private Pos pos;
	private int width=Breakout.playerWidth;//will be used when powerups are added(that change the width) and 2 players can play 
	private int height=Breakout.playerHeight;
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
	public double getX(){return pos.getX();}
	public double getY(){return pos.getY();}
	public void setX(int x){pos.setX(x);}
	public int getWidth(){return width;}
	public int getHeight(){return height;}
	Player()
	{
		pos = new Pos(0,Breakout.height-30);
		lives=3;
	}
}
class Ball
{
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
	
	Ball()
	{
		pos = new Pos(0,0);
		xVel=0;
		yVel=0;
	}
	Ball(int x,int y, double xVel, double yVel)
	{
		pos=new Pos(x,y);
		this.xVel=xVel;
		this.yVel=yVel;
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
	private int radius;
	public Pos getPos(){return pos;}
	Particle(Pos pos,int radius,double xVel, double yVel, int halfLife)
	{
		this.pos=pos;
		this.radius=radius;
		this.xVel=xVel;
		this.yVel=yVel;
		this.halfLife=halfLife;
	}
	public void move()
	{
		pos.move(xVel,yVel);
		
	}
	public int getRadius(){return radius;}
	public int getHalfLife(){return halfLife;}
	public int getLife(){return life;}
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
	static boolean twoPlayers=false;
	static boolean isBasic=true;
	static boolean isLoaded=false;
	static int width=800;
	static int height=600;
	static int targetFPS=60;
	static int ballRadius=40;
	static int brickWidth=143;
	static int brickHeight=90;
	static int playerWidth=70;
	static int playerHeight=10;
	static int level=1;
	static double velOffset=10;
	static double maxBallVel=1.0*velOffset;
	static double maxBrickVel=0.2*velOffset;
	static GameState gameState=GameState.startGame;
	static ArrayList<Ball> ball = new ArrayList<Ball>();
	static ArrayList<Brick> brick = new ArrayList<Brick>();
	static ArrayList<Player> player = new ArrayList<Player>();
	static ArrayList<Button> button = new ArrayList<Button>();
	static ArrayList<Particle> particle = new ArrayList<Particle>();
	static Image playerImage;
	static Image[] brickImage;
	static Image ballImage;
	static Image backgroundImage;
	static Image[] buttonImage;
	ImageBuffer buf = new ImageBuffer(3,3);
	Image test;
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
		Input input=gc.getInput();
		boolean mouseDown = input.isMousePressed(0);
		boolean pDown=input.isKeyPressed(input.KEY_P);
		boolean kb1down=input.isKeyPressed(input.KEY_1);
		int mouseX=input.getMouseX();
		int mouseY=input.getMouseY();
		Player tempPlayer=player.get(0);
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
		if(kb1down)
		{
			createBall(mouseX,mouseY,-1*maxBallVel);
		}
		if(input.isKeyPressed(input.KEY_2))
		{
			createParticle(mouseX,mouseY,6,1);
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
			if(mouseX>width-playerWidth)
				tempPlayer.setX(width-playerWidth);
			else
				tempPlayer.setX(mouseX);
			for(int j=0;j<ball.size();j++)
			{
				Ball tempBall=ball.get(j);
				tempBall.move();
				double x=tempBall.getX();
				double y=tempBall.getY();
				if(x<=10 || x>=width-10)
					tempBall.reverseX();
				if(y>=height-10)
				{
					Random rand = new Random();
					rand.setSeed(System.currentTimeMillis());
					if(ball.size()==1)
					{
						gameState=GameState.paused;
						tempPlayer.decLives(1);
						if(tempPlayer.getLives()<=0)
						{
							gameState=GameState.gameOver;
							break;
						}
					}
					if(ball.size()==1)
					{
						tempBall.setPos(tempPlayer.getX()+playerWidth/2-ballRadius/2,tempPlayer.getY()-ballRadius);
						double yVel=rand.nextDouble()*maxBallVel;
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
						ball.remove(j);
						continue;
					}
				}
				if(y<=10)
					tempBall.reverseY();
				for(int k=brick.size()-1;k>=0;k--)
				{
					Brick tempBrick=brick.get(k);
					boolean collide=checkCollision(tempBrick,tempBall);
					if(collide==true)
					{
						createParticle(tempBrick.getX() + (brickWidth/2),tempBrick.getY() + (brickHeight/2),15,1);
						tempBrick.reduceType();
						tempBall.reverseY();
						tempPlayer.incScore(1);
						if(tempBrick.getType()==0)
						{
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
			if(brick.size()==0)
				createGame(++level);
			boolean changeXVel=false;
			for(int k=0;k<brick.size();k++)
			{
				Brick tempBrick=brick.get(k);
				tempBrick.incX(tempBrick.getXVel());
				if(tempBrick.getX()<10 || tempBrick.getX()>width-10-brickWidth)
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
			for(int k=0;k<particle.size();k++)
			{
				Particle tempPart=particle.get(k);
				tempPart.incLife();
				if(tempPart.isDead())
				{
					particle.remove(k);
					continue;
				}
				
				int radius=tempPart.getRadius();
				double xOffset=tempPart.getXOffset();
				double yOffset=tempPart.getYOffset();
				if(xOffset+radius>=width || xOffset+radius<=0)
					particle.remove(k);
				if(yOffset+radius>=height || yOffset+radius<=0)
					particle.remove(k);
			}
		}
	}
	@Override public void render(GameContainer gc, Graphics g) throws SlickException 
	{
		//start screen
		if(!isLoaded)
		{
			
			buf.setRGBA(0, 0, 255, 255, 255, 0);
			buf.setRGBA(0, 1, 255, 255, 255, 255);
			buf.setRGBA(0, 2, 255, 255, 255, 0);
			buf.setRGBA(1, 0, 255, 255, 255, 255);
			buf.setRGBA(1, 1, 255, 255, 255, 0);
			buf.setRGBA(1, 2, 255, 255, 255, 255);
			buf.setRGBA(2, 0, 255, 255, 255, 0);
			buf.setRGBA(2, 1, 255, 255, 255, 255);
			buf.setRGBA(2, 2, 255, 255, 255, 0);
			test = new Image(buf);
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
					if(type>=1)
					{
						g.setColor(new Color((type*55)%255,(type*130)%255,(type*225)%255,255));
						
						g.drawRect((int)tempBrick.getX(),(int)tempBrick.getY(),brickWidth,brickHeight);
					}
				}
				for(int i=0;i<ball.size();i++)
				{
					g.drawOval((int)ball.get(i).getX(),(int)ball.get(i).getY(),ballRadius,ballRadius);
				}
				for(int i=0;i<player.size();i++)
				{
					g.drawRect((int)player.get(i).getX(),(int)player.get(i).getY(),playerWidth,playerHeight);
				}
				for(int i=0;i<particle.size();i++)
				{
					Particle tempPart = particle.get(i);
					test.startUse();
					test.drawEmbedded((int)tempPart.getXOffset(), (int)tempPart.getYOffset(), 3, 3);		
					test.endUse();
				}
			}
			else
			{
				
				for(int i=0;i<brick.size();i++)
				{
					Brick tempBrick=brick.get(i);
					if(tempBrick.getType()>=1)
						g.drawImage(brickImage[tempBrick.getType()-1],(float)tempBrick.getX(),(float)tempBrick.getY());
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
		player.add(new Player());
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
	public static void createBall(int x,int y,double yVel)
	{
		Ball temp = new Ball();
		temp.setPos(x,y);
		temp.setXVel(0);
		temp.setYVel(yVel);
		ball.add(temp);
	}
	public static void createParticle(double x, double y,int n, int formation)
	{
		switch(formation)
		{
			case(1):
			{
				double angle=Math.PI*2;
				angle/=n;
				double Vel=1;
				double xVel=0;
				double yVel=0;
				for(int i=0;i<n;i++)
				{
					xVel=Math.sin(angle*i)/Vel;
					yVel=Math.cos(angle*i)/Vel;
					particle.add(new Particle(new Pos(x,y),3,xVel,yVel,300));
				}
			}
		
		}
	}
	public static void createGame(int Level)
	{
		int numX=(width-30) / (brickWidth+30);
		int numY=(height-250) / (brickHeight+10);
		int num=numX*numY;
		brick.clear();
		for(int i=0;i<num;i++)
		{
			int type=Level;
			if(Level>8)
				type=8;
			brick.add(new Brick(type,50+(i%numX)*(brickWidth+20),50+(i/numX)*(brickHeight+10),maxBrickVel));
		}
	}
	public static void createGame()
	{
		if(isBasic)
			createGame(50,10,1,0);
		else
			createGame(12,3,1,0);
	}
	public static void createGame(int numBricks,int bricksOnLine, int type, int formation)
	{
		ball.add(new Ball(width/2,height-80,0,maxBallVel*-1));
		for(int i=0;i<numBricks;i++)
		{
			brick.add(new Brick(type,50+(i%bricksOnLine)*(brickWidth+20),50+(i/bricksOnLine)*(brickHeight+10),maxBrickVel));
		}
		
	}
	public static void startGame()
	{
		for(int i=0;i<3;i++)
		{
			button.get(i).setVisibility(false);
			button.get(i).setActive(false);
		}
		
		createGame();
		gameState=GameState.inGame;
	}
	public static void checkCollision(Player a, Ball b)
	{
		int x1=(int)a.getX();
		int x2=x1+a.getWidth();
		int y1=(int)a.getY();
		int y2=y1+a.getHeight();
		int collideX=0;
		double newVelX=0;
		double newVelY=0;
		if(x1 < b.getX() + ballRadius && x2 > b.getX())
			if(y1<b.getY()+ballRadius && y2>b.getY())
			{
				collideX=(int)b.getX()+(ballRadius/2) - x1;
				newVelX=(collideX%(a.getWidth()/2))/(double)a.getWidth()*maxBallVel;
				newVelY=Math.sqrt((maxBallVel*maxBallVel)-(newVelX*newVelX));
				newVelY*=-1;
				if(collideX/maxBallVel<a.getWidth()/2)
					newVelX*=-1;
				b.setXVel(newVelX);
				b.setYVel(newVelY);
			}
	}
	public boolean checkCollision(Brick a, Ball b)
	{
		int x1=(int)a.getX();
		int x2=x1+brickWidth;
		int y1=(int)a.getY();
		int y2=y1+brickHeight;
		if(x1 < b.getX() + ballRadius && x2 > b.getX())
			if(y1<b.getY()+ballRadius && y2>b.getY())
				return true;
		return false;
	}
	public boolean checkCollision(Button a,int x,int y)
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
	public double distY(Brick a, Ball b)
	{
		double dist=0;
		double y1=b.getY();
		double y2=a.getY();
		dist=Math.abs(y1-y2);
		return dist;
	}
}

/* The following comments are reminders/thoughts for me
GAME LOGIC:
Move ball
Move bricks
Move player : MOUSE OR KEYBOARD?

brick: 
type = life count; getting hit changes type, changes texture?(probably)

OBJECT WIDTH/HEIGHT CONSTANT?



 Possible goals:
 randomized maps (follows pattern) aka formations
 temporary powerups, permanent powerups
 2 players?
 */
 
/*

WALLS, WHERE, EDGE OF SCREEN? x=0 && x=width or x=10 etc?
HOW IT OPERATES?


 */
