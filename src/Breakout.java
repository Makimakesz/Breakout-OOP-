import java.lang.reflect.*;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Font;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

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
}
class Brick
{
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
	public void reduceType(){this.type--;}
	public void setType(int type){this.type=type;}
	public void setPos(Pos pos){this.pos=pos;}
	Brick()
	{
		this.type=1;
		this.xVel=1;
		this.pos = new Pos(0,0);
	}
	Brick(int x,int y)
	{
		this.type=1;
		this.xVel=0.2;
		this.pos = new Pos(x,y);
	}
	Brick(int type,int x,int y)
	{
		this.type=type;
		this.pos = new Pos(x,y);
		this.xVel=0.2;
	}
}

class Player
{
	private String name; //will probably be unused, can be asked but nothing will be done with it
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
	public void incPos(double x,double y)
	{
		this.pos.setX(this.pos.getX()+x);
		this.pos.setY(this.pos.getY()+y);
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
	static int targetFPS=600;
	static int ballRadius=40;
	static int brickWidth=143;
	static int brickHeight=90;
	static int playerWidth=132;
	static int playerHeight=64;
	static int level=0;
	static GameState gameState=GameState.startGame;
	static Ball ball[];
	static Brick brick[];
	static Player player[];
	static Button button[];
	/*static Texture playerTexture;
	static Texture[] brickTexture;
	static Texture ballTexture;
	static Texture backgroundTexture;*/
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
		System.out.println("init");
	}
	@Override public void update(GameContainer gc, int i) throws SlickException 
	{
		Input input=gc.getInput();
		boolean mouseDown = input.isMousePressed(0);
		boolean pDown=input.isKeyPressed(input.KEY_P);
		int mouseX=input.getMouseX();
		int mouseY=input.getMouseY();
		if(mouseDown)
		{
			for(int k=0;k<button.length;k++)
			{
				if(button[k].getActive())
					if(checkCollision(button[k],mouseX,mouseY))
					{
						button[k].invoke();
					}
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
			if(mouseX>width-playerWidth)
				player[0].setX(width-playerWidth);
			else
				player[0].setX(mouseX);
			for(int j=0;j<ball.length;j++)
			{
				ball[j].incPos(ball[j].getXVel(), ball[j].getYVel());
				double x=ball[j].getX();
				double y=ball[j].getY();
				if(x<=10 || x>=width-10)
					ball[j].reverseX();
				if(y>=height-10)
				{
					Random rand = new Random();
					rand.setSeed(System.currentTimeMillis());
					player[0].decLives(1);
					if(player[0].getLives()<=0)
					{
						gameState=GameState.gameOver;
						break;
					}
					ball[j].setPos(player[0].getX()+playerWidth/2-ballRadius/2,player[0].getY()-ballRadius);
					double yVel=rand.nextDouble();
					yVel*=0.7;
					yVel+=0.3;
					yVel*=(-1);
					ball[j].setYVel(yVel);
					ball[j].setXVel(1.0-(yVel*yVel));
					gameState=GameState.paused;
				}
					//delete ball, remove life, create new ball
				if(y<=10)
					ball[j].reverseY();
				for(int k=0;k<brick.length;k++)
				{
					if(brick[k].getType()>=1)
					{
						boolean collide=checkCollision(brick[k],ball[j]);
						if(collide==true)
						{
							brick[k].reduceType();
							ball[j].reverseY();
							player[0].incScore(1);
							boolean startNew=true;
							for(int l=0;l<brick.length;l++)
							{
								if(brick[l].getType()>0)
								{
									startNew=false;
									break;
								}
							}
							if(startNew)
							{
								createGame(++level);
							}
							break;
						}
					}
				}
				boolean changeXVel=false;
				for(int k=0;k<brick.length;k++)
				{
					if(brick[k].getType()>0)//List1(for future)
					{
						brick[k].incX(brick[k].getXVel());
						if(brick[k].getX()<10 || brick[k].getX()>width-10-brickWidth)
							changeXVel=true;
					}
				}
				if(changeXVel)
				{
					for(int k=0;k<brick.length;k++)
						if(brick[k].getType()>0)//List1
							brick[k].setXVel(brick[k].getXVel()*(-1));
				}
				for(int k=0;k<player.length;k++)
				{
					checkCollision(player[k],ball[j]);
				}
				
			}
		}
	}
	@Override public void render(GameContainer gc, Graphics g) throws SlickException 
	{
		//start screen
		if(!isLoaded)
		{
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
		for(int i=0;i<button.length;i++)
		{
			if(button[i].getVisible())
			{
				g.drawImage(buttonImage[i],(float)button[i].getPos().getX(),(float)button[i].getPos().getY());
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
			g.drawString("Score: " + player[0].getScore() + "\nLives: " + player[0].getLives(), 10,30);
			if(isBasic)
			{
				for(int i=0;i<brick.length;i++)
				{
					if(brick[i].getType()>=1)
						g.drawRect((int)brick[i].getX(),(int)brick[i].getY(),brickWidth,brickHeight);
				}
				for(int i=0;i<ball.length;i++)
				{
					g.drawOval((int)ball[i].getX(),(int)ball[i].getY(),ballRadius,ballRadius);
				}
				for(int i=0;i<player.length;i++)
				{
					g.drawRect((int)player[i].getX(),(int)player[i].getY(),playerWidth,playerHeight);
				}
			}
			else
			{
				
				for(int i=0;i<brick.length;i++)
				{
					if(brick[i].getType()>=1)
						//g.drawRect((int)brick[i].getX(),(int)brick[i].getY(),brickWidth,brickHeight);
						g.drawImage(brickImage[brick[i].getType()-1],(float)brick[i].getX(),(float)brick[i].getY());
				}
				for(int i=0;i<ball.length;i++)
				{
					g.drawImage(ballImage, (float)ball[i].getX(), (float)ball[i].getY());
				}
				for(int i=0;i<player.length;i++)
				{
					g.drawImage(playerImage, (float)player[i].getX(), (float)player[i].getY());
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
			g.drawString("Score: " + player[0].getScore(),width/2-50,(int)(height/1.5)+40);
		}
	}
	public static void Init()
	{
		button = new Button[4];
		button[0]=new Button("startGame",getClass(null),getObj(null),250,300,300,100);
		button[1]=new Button("setTextures",getClass(String.class),getObj("gfx1"),100,100,300,100);
		button[2]=new Button("setTextures",getClass(boolean.class),getObj(true),450,100,300,100);
		button[3]=new Button("setTextures",getClass(String.class),getObj("gfx2"),100,100,50,50,false,false);
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
			/*playerTexture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(folder+"/player.png"));
			ballTexture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(folder+"/ball.png"));
			backgroundTexture = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream(folder+"/bg.png"));
			brickTexture=new Texture[8];
			for(int i=0;i<8;i++)
			{
				brickTexture[i] = TextureLoader.getTexture("PNG",ResourceLoader.getResourceAsStream(folder+"/"+(i+1)+".png"));
			}*/
			playerImage = new Image(folder+"/player.png");
			ballImage = new Image(folder+"/ball.png");
			backgroundImage = new Image(folder+"/bg.png");
			brickImage=new Image[8];
			/*buttonImage = new Image[4];
			buttonImage[0]=new Image(folder+"/start.png");
			buttonImage[1]=ballImage;
			buttonImage[2]=brickImage[0];*/
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
	public static void createGame(int Level)
	{
		int numX=(width-30) / (brickWidth+30);
		int numY=(height-250) / (brickHeight+10);
		int num=numX*numY;
		if(brick.length<num)
			num=brick.length;
		for(int i=0;i<num;i++)
		{
			brick[i].setPos(new Pos(50+(i%numX)*(brickWidth+20),50+(i/numX)*(brickHeight+10)));
			brick[i].setXVel(0.2);
			if(Level<8)
			
				brick[i].setType(Level);
			else
				brick[i].setType(8);
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
		//set all the bricks and shit
		//
		ball = new Ball[1];//ArrayList in future for multiple balls
		ball[0] = new Ball();
		ball[0].setPos(width/2,height-80);
		ball[0].setXVel(0);
		ball[0].setYVel(-1);
		brick = new Brick[numBricks];
		for(int i=0;i<numBricks;i++)
		{
			brick[i]=new Brick(type,50+(i%bricksOnLine)*(brickWidth+20),50+(i/bricksOnLine)*(brickHeight+10));
		}
		player = new Player[1];
		player[0]=new Player();
	}
	public static void startGame()
	{
		for(int i=0;i<3;i++)
		{
			button[i].setVisibility(false);
			button[i].setActive(false);
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
				newVelX=(collideX%(a.getWidth()/2))/(double)a.getWidth();
				newVelY=Math.sqrt(1-(newVelX*newVelX));
				newVelY*=-1;
				if(collideX<a.getWidth()/2)
					newVelX*=-1;
				b.setXVel(newVelX);
				b.setYVel(newVelY);
			}
	}
	public boolean checkCollision(Brick a, Ball b)
	{
		if(a.getType()<=0)
			return false;
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
 multiple balls? <-- gotta arraylist that shit in that case
 in fact i could arraylist everything and get rid of the type<=0 with bricks
 
 */
 
/*

WALLS, WHERE, EDGE OF SCREEN? x=0 && x=width or x=10 etc?
HOW IT OPERATES?


 */

//test release github