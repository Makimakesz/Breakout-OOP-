import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;

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
	private double xVel=0;
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
enum GameState
{
	startGame,inGame,paused, gameOver
}
public class Breakout extends BasicGame {
	static boolean twoPlayers=false;
	static int width=800;
	static int height=640;
	static int targetFPS=600;
	static int ballRadius=20;
	static int brickWidth=40;
	static int brickHeight=10;
	static int playerWidth=100;
	static int playerHeight=10;
	static GameState gameState=GameState.startGame;
	static Ball ball[];
	static Brick brick[];
	static Player player[];
	public Breakout(String gameName)
	{
		super(gameName);
	}
	@Override public void init(GameContainer gc) throws SlickException{}
	@Override public void update(GameContainer gc, int i) throws SlickException 
	{
		Input input=gc.getInput();
		boolean mouseDown = input.isMousePressed(0);
		boolean pDown=input.isKeyPressed(input.KEY_P);
		if(pDown && gameState!=GameState.inGame)
			gameState=GameState.inGame;
		else if(pDown && gameState==GameState.inGame)
			gameState=GameState.paused;
		if(gameState==GameState.paused || gameState==GameState.startGame)
			if(mouseDown)
			{
				int mouseX=input.getMouseX();
				int mouseY=input.getMouseY();
				gameState=GameState.inGame;
			}
		if(gameState==GameState.inGame)
		{
			int mouseX=input.getMouseX();
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
					ball[j].setPos(player[0].getX()+playerWidth/2-ballRadius/2,player[0].getY()-1-playerHeight*2);
					double yVel=rand.nextDouble()*(-1);
					yVel*=0.7;
					yVel+=0.3;
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
		if(gameState==GameState.startGame)
		{
				
		}
		//game
		if(gameState==GameState.inGame || gameState==GameState.paused)
		{
			g.drawString("Score: " + player[0].getScore() + "\nLives: " + player[0].getLives(), 10,30);
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
		if(gameState==GameState.paused)
		{
			g.drawString("Paused",width/2,height/2);
		}
	}
	public static void main(String[] args) 
	{
		if(args.length==1)
			if(args[0]=="2")
				twoPlayers=true;
		createGame();
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
	public static void deleteGame(){}
	public static void createGame()
	{
		createGame(50,10,2,0);
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
			brick[i]=new Brick(type,100+(i%bricksOnLine)*50,100+(i/bricksOnLine)*50);
		}
		player = new Player[1];
		player[0]=new Player();
	}
	
	public void checkCollision(Player a, Ball b)
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
		int x1=(int)a.getX();
		int x2=x1+brickWidth;
		int y1=(int)a.getY();
		int y2=y1+brickHeight;
		if(x1 < b.getX() + ballRadius && x2 > b.getX())
			if(y1<b.getY()+ballRadius && y2>b.getY())
				return true;
		return false;
	}
}

/*
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

