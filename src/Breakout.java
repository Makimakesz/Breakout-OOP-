import java.io.*;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
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
	public final int ID;	
	private int lives=3;
	private int score=0;
	private Pos pos;
	private int width=Breakout.playerWidth;//will be used when powerups are added(that change the width) and 2 players can play 
	private int height=Breakout.playerHeight;
	private int numBalls=0;
	private int moveLeft=-1;
	private int moveRight=-1;
	private Color color;
	private double xVel=1.5;
	public int getScore(){return score;}
	public void incScore(int score){this.score+=score;Breakout.totalScore[ID]+=score;}
	public void decLives(int lives){this.lives-=lives;}
	public void incLives(int lives){this.lives+=lives;}//can be done with just 1 method but this looks nicer
	public int getLives(){return lives;}
	public ArrayList<Ball> ball = new ArrayList<Ball>();
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
	public void incWidth(int dWidth){this.width+=dWidth;}
	public void setKeys(int left, int right){moveLeft=left;moveRight=right;}
	public int getLeft(){return moveLeft;}
	public int getRight(){return moveRight;}
	public void setColor(Color color){this.color=color;}
	public Color getColor(){return color;}
	Player(int id)
	{
		ID=id;
		pos = new Pos(0,Breakout.height-30);
	}
	Player(int id, double x, double y)
	{
		ID=id;
		pos = new Pos(x,y);
	}
	Player(int id, double x)
	{
		ID=id;
		pos = new Pos(x,Breakout.height-30);
	}
}
class Ball
{
	//private final int owner;
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
	//public int getOwner(){return owner;}
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
	Ball(int x,int y, double xVel, double yVel)//, int owner)
	{
		pos=new Pos(x,y);
		this.xVel=xVel;
		this.yVel=yVel;
		//this.owner=owner;
	}
}
class Powerup
{
	private Pos pos;
	private int type;
	private double yVel=0.3;
	private int radius=30;
	private String methodName;
	private Class[] types;
	private Object[] args;
	private int ownerIndex=0;
	public double getX(){return pos.getX();}
	public double getY(){return pos.getY();}
	public int getRadius(){return radius;}
	public int getType(){return type;}
	public void move()
	{
		pos.move(0, yVel);
	}
	public void invoke(int owner)
	{
		args[ownerIndex]=owner;
		Breakout.invoke(methodName, types, args);
	}
	Powerup(int type, double x, double y)
	{
		this.type=type;
		this.pos=new Pos(x,y);
		initInvoke();
	}
	private void initInvoke()
	{//0: add life 1: add ball 2: increase width
		switch(type)
		{
			case(0):
			{
				methodName="addLife";
				types=new Class[]{int.class,int.class};
				args=new Object[]{0,1};
				ownerIndex=0;
				break;
			}
			case(1):
			{
				methodName="createBall";
				types=new Class[]{int.class};
				args=new Object[]{0};
				ownerIndex=0;
				break;
			}
			case(2):
			{
				methodName="incWidth";
				types=new Class[]{int.class,int.class};
				args=new Object[]{0,Breakout.incWidthAmount};
				ownerIndex=0;
				break;
			}
		}
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
	private int imageNum;
	private ArrayList<GameState> visibleStates = new ArrayList<GameState>();
	private Class[] types;
	private Object[] args;
	public Pos getPos(){return pos;}
	public int getWidth(){return width;}
	public int getHeight(){return height;}
	public boolean getActive(){return isActive;}
	public boolean getVisible(){return isVisible;}
	public void setState(GameState gs)
	{
		isActive=false;
		isVisible=false;
		for(int i=0;i<visibleStates.size();i++)
		{
			if(gs==visibleStates.get(i))
			{
				isActive=true;
				isVisible=true;
				return;
			}
		}
	}
	public void setStates(GameState... gs)
	{
		visibleStates.clear();
		for(int i=0;i < gs.length;i++)
		{
			visibleStates.add(gs[i]);
		}
	}
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
		Breakout.invoke(methodName,types,args);
	}
	public void invoke(Object[] args)
	{
		this.args=args;
		this.invoke();
	}
	public int getImageNumber(){return imageNum;}
	Button(String methodName,Class[] types,Object[] args,int x,int y, int width,int height,boolean isVisible,boolean isActive, int image)
	{
		this.methodName=methodName;
		this.types=types;
		this.args=args;
		this.pos = new Pos(x,y);
		this.width=width;
		this.height=height;
		this.isVisible=isVisible;
		this.isActive=isActive;
		this.imageNum=image;
	}
	Button(String methodName,Class[] types,Object[] args,int x,int y, int width,int height,int image)
	{
		this(methodName,types,args,x,y,width,height,true,true, image);
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
		if(type==0 || type==1)
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
	/*public double getXOffset()
	{
		return pos.getX() + life*xVel;
	}
	public double getYOffset()
	{
		return pos.getY() + life*yVel;
	}*/
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
enum GameMode
{
	arcade,competition
}
public class Breakout extends BasicGame {
	static boolean isBasic=true;
	static boolean isLoaded=false;
	static int width=800;
	static int height=600;
	static int targetFPS=60;
	static int ballRadius=20;
	static int brickWidth=50;
	static int brickHeight=10;
	static int playerWidth=100;
	static int playerHeight=10;
	static int playerSpeed=15;
	static int wallSpace=ballRadius+10;
	static int level=1;
	static int frameCount=0;
	static int tempFrame=0;
	static int randSeed=1;
	static int numPlayers=1;
	static int totalScore[]={0,0};
	static int highScore=0;
	static int keysLeft[]={30,203};
	static int keysRight[]={32,205};
	static int powerupChance=15;
	static int incWidthAmount=10;
	static double velOffset=1;
	static double gameOffset=10;
	static double maxBallVel=1.0*velOffset;
	static double maxBrickVel=0.2*velOffset;
	static Color playerColors[]={new Color(95,148,232,255),new Color(27,202,91,255)};
	static Color powerColor[]={Color.white,Color.yellow,Color.magenta};
	static GameState gameState=GameState.startGame;
	static GameMode gameMode=GameMode.arcade;
	
	static ArrayList<Brick> brick = new ArrayList<Brick>();
	static ArrayList<Player> player = new ArrayList<Player>();
	static ArrayList<Button> button = new ArrayList<Button>();
	static ArrayList<Particle> particle = new ArrayList<Particle>();
	static ArrayList<Powerup> powerup = new ArrayList<Powerup>();
	static ArrayList<ImageBuffer> bufParticle = new ArrayList<ImageBuffer>();
	static ArrayList<Image> imageParticle = new ArrayList<Image>();
	static Image[] playerImage = new Image[2];
	static Image[] brickImage;
	static Image ballImage;
	static Image backgroundImage;
	static Image[] buttonImage;
	static Image[] powerupImage = new Image[3];
	public static Class[] getClass(Class... cls){return cls;}
	public static Object[] getObj(Object... obj){return obj;}
	public Breakout(String gameName)
	{
		super(gameName);
	}
	@Override public void init(GameContainer gc) throws SlickException
	{
	}
	@Override public void update(GameContainer gc, int i) throws SlickException 
	{
		frameCount++;
		Input input=gc.getInput();
		boolean mouseDown = input.isMousePressed(0);
		boolean pDown=input.isKeyPressed(input.KEY_P);
		boolean spaceDown=input.isKeyPressed(input.KEY_SPACE);
		/*for(int v = 0;v<player.size();v++)
		{
			if(gameMode==GameMode.arcade)
			{
			if(totalScore>highScore)
			{
				highScore=totalScore;
				saveData();
			}
			}
		}*/
		int sumScore=0;
		if(gameMode==GameMode.arcade)
		{
			sumScore=totalScore[0]+totalScore[1];//totalScore[1]=0 if numPlayers=1
		}
		else if(gameMode==GameMode.competition)
		{
			sumScore=(totalScore[0]>totalScore[1])?totalScore[0]:totalScore[1];
		}
		if(sumScore>highScore)
		{
			highScore=sumScore;
			saveData();
		}
		int mouseX=input.getMouseX();
		int mouseY=input.getMouseY();
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
		if(input.isKeyPressed(input.KEY_1))//the following are for testing purposes
		{
			createBall(50,50,1,0,0);
		}
		if(input.isKeyPressed(input.KEY_2))
		{
			createBall(50,50,1,0,1);
		}
		if(input.isKeyPressed(input.KEY_4))
		{
			createGame(++level);
		}
		if(input.isKeyPressed(input.KEY_5))
		{
			powerup.add(new Powerup(2,300,300));
		}
		if(gameState==GameState.inGame)
		{
			for(int w=0;w<player.size();w++)
			{
				Player tempPlayer=player.get(w);
				if(input.isKeyDown(tempPlayer.getLeft()))
				{	
					if(tempPlayer.getX()>playerSpeed)
						tempPlayer.moveX(-playerSpeed);
					else
						tempPlayer.setX(0);
				}
				if(input.isKeyDown(tempPlayer.getRight()))
				{	
					if(tempPlayer.getX()<width-playerSpeed-tempPlayer.getWidth())
						tempPlayer.moveX(playerSpeed);
					else
						tempPlayer.setX(width-tempPlayer.getWidth());
				}
			}
		}
		if((mouseDown||pDown||spaceDown) && gameState==GameState.gameOver)
		{
			gc.exit();
		}
		if(pDown && gameState==GameState.paused)
			changeState(GameState.inGame);
		else if(pDown && gameState==GameState.inGame)
			changeState(GameState.paused);
		if(gameState==GameState.paused)
			if(mouseDown || spaceDown)
			{
				changeState(GameState.inGame);
			}
		if(gameState==GameState.inGame)
		{
			for(int z=0;z<gameOffset;z++)
			{
				for(int w=0;w<player.size();w++)
				{
					Player tmpPlayer=player.get(w);
					for(int j=0;j<tmpPlayer.ball.size();j++)
					{
						Ball tempBall=tmpPlayer.ball.get(j);
						tempBall.move();
						double x=tempBall.getX();
						double y=tempBall.getY();
						if(x<=10 || x>=width-10)
							tempBall.reverseX();
						if(y>=height-10)
						{
							if(tmpPlayer.getBalls()==1)
							{
								tmpPlayer.decLives(1);
								//if(tmpPlayer.ball.size()==1)
								if(totalBalls()==1)
									changeState(GameState.paused);
								
								if(tmpPlayer.getLives()<=0)
								{
									tmpPlayer.ball.remove(j);
									player.remove(w);
									if(player.size()==0)
									{
										changeState(GameState.gameOver);
									}
									break;
								}
								tempBall.setPos(tmpPlayer.getX()+tmpPlayer.getWidth()/2-ballRadius/2,tmpPlayer.getY()-ballRadius);
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
								tmpPlayer.decBall();
								tmpPlayer.ball.remove(j);
								continue;
							}
							/*if(tmpPlayer.getLives()==0)
							{
								tmpPlayer.decBall();
								tmpPlayer.ball.remove(j);
								player.remove(w);	
								continue;
							}*/
							
						}
						if(y<=10)
							tempBall.reverseY();
						
						for(int k=brick.size()-1;k>=0;k--)
						{
							Brick tempBrick=brick.get(k);
							int collide=checkCollision(tempBrick,tempBall);
							if(collide!=-1)
							{
								double brickX=tempBrick.getX();
								double brickY=tempBrick.getY();
								
								if(collide==3 || collide==4)
									tempBall.reverseY();
								else
									tempBall.reverseX();
								tempBall.move();
								if(tempBrick.getType()==1)
									break;
								tmpPlayer.incScore(1);
								tempBrick.reduceLife();
								if(tempBrick.getLife()==0)
								{
									createParticle(brickX + (brickWidth/2),brickY + (brickHeight/2),45,0);
									brick.remove(k);
								}
								int dropPowerup=Rand(0,powerupChance);
								if(dropPowerup==0)
								{
									int powerType=0;//randomize that later
									int powerRand=Rand(0,100);
									if(powerRand<=50)
										powerType=0;
									else if(powerRand>50&&powerRand<=80)
										powerType=1;
									else
										powerType=2;
									powerup.add(new Powerup(powerType,brickX+(brickWidth/2),brickY));
								}
								break;
							}
						}
						for(int k=0;k<player.size();k++)
						{
							//if(k!=w)
								if(checkCollision(player.get(k),tempBall))
									break;
						}
					}
					for(int k=powerup.size()-1;k>=0;k--)
					{
						Powerup tempPower=powerup.get(k);
						boolean collide=checkCollision(tempPower,tmpPlayer);
						if(collide)
						{
							tempPower.invoke(w);
							powerup.remove(k);
						}
						
					}
				}
				for(int k=powerup.size()-1;k>=0;k--)
				{
					Powerup tempPower=powerup.get(k);
					tempPower.move();
					if(tempPower.getY()>=height)
						powerup.remove(k);
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
					tempPart.move();
					if(tempPart.isDead())
					{
						particle.remove(k);
						continue;
					}
					
					int Width=tempPart.getWidth();
					int Height=tempPart.getHeight();
					double x=tempPart.getX();
					double y=tempPart.getY();
					if(x+Width>width || x+Width<0)
					{
						particle.remove(k);
						continue;
					}
					if(y+Height>height || y+Height<0)
						particle.remove(k);
				}
				
		}
		}
	}
	public void buttonBorder(Graphics g,int buttonA,int buttonB) throws SlickException
	{
		Button tempButton;
		g.setLineWidth(3);
		g.setColor(Color.red);
		tempButton=button.get(buttonA);
		g.drawRect((float)tempButton.getPos().getX(),(float)tempButton.getPos().getY(), tempButton.getWidth(), tempButton.getHeight());
		g.setLineWidth(1);
		g.setColor(Color.gray);
		tempButton=button.get(buttonB);
		g.drawRect((float)tempButton.getPos().getX(),(float)tempButton.getPos().getY(), tempButton.getWidth(), tempButton.getHeight());
	}
	public void buttonBorder(Graphics g, int buttonA) throws SlickException
	{
		Button tempButton;
		g.setLineWidth(3);
		g.setColor(Color.red);
		tempButton=button.get(buttonA);
		g.drawRect((float)tempButton.getPos().getX(),(float)tempButton.getPos().getY(), tempButton.getWidth(), tempButton.getHeight());
	}
	@Override public void render(GameContainer gc, Graphics g) throws SlickException 
	{
		//start screen
		if(!isLoaded)
		{
			ImageBuffer bufParticle1 = new ImageBuffer(3,3);
			ImageBuffer bufParticle2 = new ImageBuffer(5,1);
			//ImageBuffer bufParticle2 = new ImageBuffer(2,3);
			int[] colors1={255,255,255};
			int[] colors2={255,255,255};
			for(int i=0;i<3;i++)
			{
				for(int j=0;j<3;j++)
				{
					bufParticle1.setRGBA(i, j, colors1[0], colors1[1], colors1[2], ((i+j))%2*255);
				}
			}
			for(int i=0;i<5;i++)
				bufParticle2.setRGBA(i, 0, colors2[0], colors2[1], colors2[2], 255);
			imageParticle.add(new Image(bufParticle1));
			imageParticle.add(new Image(bufParticle2));
			try
			{
				buttonImage = new Image[7];
				buttonImage[0]=new Image("data/start.png");
				buttonImage[1]=new Image("data/gfx.png");
				buttonImage[2]=new Image("data/basic.png");
				buttonImage[3]=new Image("data/players1.png");
				buttonImage[4]=new Image("data/players2.png");
				buttonImage[5]=new Image("data/arcade.png");
				buttonImage[6]=new Image("data/competition.png");
				
				int playerCount=2;
				int brickCount=9;
				int powerupCount=3;
				for(int i=0;i<playerCount;i++)
				{
					playerImage[i] = new Image("data/player" + (i+1) + ".png");
				}
				ballImage = new Image("data/ball.png");
				backgroundImage = new Image("data/bg.png");
				brickImage=new Image[brickCount];
				for(int i=0;i<brickCount;i++)
				{
					brickImage[i]=new Image("data/brick"+(i+1)+".png");
				}
				for(int i=0;i<powerupCount;i++)
				{
					powerupImage[i]=new Image("data/powerup"+(i+1)+".png");
				}
			}
			catch(Throwable e)
			{
				System.out.println(e);
			}
			isLoaded=true;
		}
		if(isBasic)
			g.setBackground(Color.black);
		//else
		//	g.drawImage(backgroundImage, 0, 0);
		for(int i=0;i<button.size();i++)
		{
			Button tempButton=button.get(i);
			if(tempButton.getVisible())	
			{
				g.drawImage(buttonImage[tempButton.getImageNumber()],(float)tempButton.getPos().getX(),(float)tempButton.getPos().getY());
				
			}
		}
		if(gameState==GameState.startGame)
		{
			g.setColor(Color.white);
			g.drawString("Player movement keys are:",30,170);
			g.drawString("Player 1\nLeft: 'a'\nRight: 'd'\n\nPause: 'p'",30,200);
			g.drawString("Player 2\nLeft: 'left arrow key'\nRight: 'right arrow key'" , 170, 200);
			g.drawString("The idea of the game is to get the highest total score\nor to compete with a friend in the compete mode\n",30,350);
			
			buttonBorder(g,0);
			if(numPlayers==1)
			{
				buttonBorder(g,3,4);
			}
			else
			{
				buttonBorder(g,4,3);
				if(gameMode==GameMode.arcade)
				{
					buttonBorder(g,5,6);
				}
				else if(gameMode==GameMode.competition)
				{
					buttonBorder(g,6,5);
				}
			}
			if(isBasic)
			{
				buttonBorder(g,2,1);
			}
			else
			{
				buttonBorder(g,1,2);
			}
		}
		//game
		if(gameState==GameState.inGame || gameState==GameState.paused)
		{
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
				
				for(int i=0;i<player.size();i++)
				{
					Player tmpPlayer=player.get(i);
					g.setColor(tmpPlayer.getColor());
					g.drawRect((int)tmpPlayer.getX(),(int)tmpPlayer.getY(),tmpPlayer.getWidth(),tmpPlayer.getHeight());
					for(int k=0;k<tmpPlayer.ball.size();k++)
					{
						Ball tempBall=tmpPlayer.ball.get(k);
						//g.setColor(new Color(255,0,0,255));
						
						g.drawOval((int)tempBall.getX(),(int)tempBall.getY(),ballRadius,ballRadius);
					}
				}
				
				for(int i=0;i<powerup.size();i++)
				{
					Powerup tempPower=powerup.get(i);
					g.setColor(powerColor[tempPower.getType()]);
					g.drawOval((int)tempPower.getX(),(int)tempPower.getY(),tempPower.getRadius(),tempPower.getRadius());
				}
				
			}
			else
			{
				for(int i=0;i<powerup.size();i++)
				{
					Powerup tempPower=powerup.get(i);
					powerupImage[tempPower.getType()].draw((int)tempPower.getX(),(int)tempPower.getY());
				}
				for(int i=0;i<brick.size();i++)
				{
					Brick tempBrick=brick.get(i);
					if(tempBrick.getType()==0)
						brickImage[tempBrick.getLife()].draw((float)tempBrick.getX(),(float)tempBrick.getY(),50,10);
						//g.drawImage(brickImage[tempBrick.getType()-1],(float)tempBrick.getX(),(float)tempBrick.getY());
					//brickImage[0].
					//g.drawImage
				}
				
				for(int i=0;i<player.size();i++)
				{
					Player tmpPlayer=player.get(i);
					Color col = tmpPlayer.getColor();
					playerImage[i].draw((float)tmpPlayer.getX(), (float)tmpPlayer.getY(),(float)tmpPlayer.getWidth(),playerHeight);
					//g.drawIm
					//g.drawImage(playerImage, (float)tmpPlayer.getX(), (float)tmpPlayer.getY(),(float)tmpPlayer.getWidth(),playerHeight);
					for(int k=0;k<tmpPlayer.ball.size();k++)
					{
						Ball tempBall=tmpPlayer.ball.get(k);
						if(gameState==GameState.inGame)
						{
							for(int l=1;l<20;l++)//TRAIL MAYBE TEMP
							{
								ballImage.setAlpha((float) (1.0/Math.ceil(l/2)));//trail
								g.drawImage(ballImage, (float)(tempBall.getX()-l*tempBall.getXVel()), (float)(tempBall.getY()-l*tempBall.getYVel()),col);
							}
						}
						ballImage.setAlpha((float)1.0);
						g.drawImage(ballImage, (float)tempBall.getX(), (float)tempBall.getY(),col);
					}
				}
			}
			g.setColor(Color.white);
			g.drawString("Highscore: " + highScore,width-150,10);
			for(int k=0;k<player.size();k++)
			{
				Player tempPlayer=player.get(k);
				g.setColor(Color.white);
				g.drawString("Player " + (k+1) + "\nScore: " + tempPlayer.getScore() + "\nLives: " + tempPlayer.getLives(), k*100+10,10);
			}
			for(int i=0;i<particle.size();i++)
			{
				Particle tempPart = particle.get(i);
				int type=tempPart.getType();
				if(imageParticle.size()>=type)
				{
					Image tempImage=imageParticle.get(type);
					tempImage.startUse();
					tempImage.drawEmbedded((int)tempPart.getX(), (int)tempPart.getY(), tempPart.getWidth(), tempPart.getHeight());		
					tempImage.endUse();
				}
			}
		}//pause
		if(gameState==GameState.paused)
		{
			g.drawString("Paused",width/2-50,(int)(height/1.5));
		}
		if(gameState==GameState.gameOver)
		{
			int textX=width/2-80;
			int textY=(int)(height/1.5);
			g.drawString("Game over", textX,textY);
			if(gameMode==GameMode.arcade)
			{
				int TotalScore=totalScore[0]+totalScore[1];
				g.drawString("Total Score: " + TotalScore,textX,textY+40);
			}
			else if(gameMode==GameMode.competition)
			{
				int winner = (totalScore[0]>totalScore[1])?0:1;
				g.drawString("Player " + (winner+1) + " wins!\nScore: " + totalScore[winner],textX , textY+40 );
			}
		}
	}
	public static void Init()
	{
		player.add(new Player(0,width/2-width/4));
		player.add(new Player(1,width/2+width/4));
		for(int i=0;i<2;i++)
		{
			Player tmpPlayer=player.get(i);
			tmpPlayer.setKeys(keysLeft[i], keysRight[i]);
			tmpPlayer.setColor(playerColors[i]);
		}
		Button startGame = new Button("startGame",getClass((Class[])null),getObj((Object[])null),300,500,200,50,0);
		Button textures1=new Button("setGraphics",getClass(boolean.class),getObj(true),30,30,200,50,1);
		Button textures2=new Button("setGraphics",getClass(boolean.class),getObj(false),260,30,200,50,2);
		Button player1=new Button("setPlayers",getClass(int.class),getObj((int)1),30,100,200,50,3);
		Button player2=new Button("setPlayers",getClass(int.class),getObj((int)2),260,100,200,50,4);
		Button arcade=new Button("setMode",getClass(GameMode.class),getObj(GameMode.arcade),490,30,200,50,5);
		Button competition=new Button("setMode",getClass(GameMode.class),getObj(GameMode.competition),490,100,200,50,6);
		startGame.setStates(GameState.startGame);
		textures1.setStates(GameState.startGame);
		textures2.setStates(GameState.startGame);
		player1.setStates(GameState.startGame);
		player2.setStates(GameState.startGame);
		button.add(startGame);
		button.add(textures1);
		button.add(textures2);
		button.add(player1);
		button.add(player2);
		button.add(arcade);//no states
		button.add(competition);
		setGraphics(true);
		changeState(GameState.startGame);
		readData();
	}
	public static void setPlayers(int count)
	{
		numPlayers=count;
		if(count==2)
		{
			button.get(5).setActive(true);
			button.get(5).setVisibility(true);
			button.get(6).setActive(true);
			button.get(6).setVisibility(true);
		}
		else
		{
			button.get(5).setActive(false);
			button.get(5).setVisibility(false);
			button.get(6).setActive(false);
			button.get(6).setVisibility(false);
		}
	}
	public static void setMode(GameMode mode)
	{
		gameMode=mode;
	}
	public static void changeState(GameState gs)
	{
		gameState=gs;
		for(int i=0;i<button.size();i++)
		{
			button.get(i).setState(gs);
		}
	}
	public static void setGraphics(boolean basic)
	{
		isBasic=!basic;
	}
	public static void main(String[] args) 
	{
		try
		{
			AppGameContainer appgc;
			appgc = new AppGameContainer(new Breakout("Breakout"));
			appgc.setDisplayMode(width,height,false);
			appgc.setTargetFrameRate(targetFPS);
			appgc.setShowFPS(false);
			Init();
			appgc.start();
			System.out.println("Test");
		}
		catch (SlickException ex)
		{
			Logger.getLogger(Breakout.class.getName()).log(Level.SEVERE,null,ex);
		}
	}
	public static void deleteGame()//probably not needed
	{
		
	}
	public static void createBall(int owner)
	{
		Player tmpPlayer=player.get(owner);
		tmpPlayer.incBall();
		tmpPlayer.ball.add(new Ball((int)tmpPlayer.getX(),(int)tmpPlayer.getY(),0,-1));
	}
	public static void createBall(int x,int y,double xVel, double yVel, int owner)
	{
		Player tmpPlayer=player.get(owner);
		tmpPlayer.incBall();
		tmpPlayer.ball.add(new Ball(x,y,xVel,yVel));
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
				double velOffset=1;
				for(int i=0;i<n;i++)
				{
					xVel=velOffset*Math.sin(angle*i)/Vel;
					yVel=velOffset*Math.cos(angle*i)/Vel;
					particle.add(new Particle(new Pos(x,y),3,3,xVel,yVel,100,0));
				}
				break;
			}
			case(1):
			{
				double xVel=3;
				double yVel=0;
				int step = 2;
				int c=0;
				for(int i=0;i<n;i++)
				{
					particle.add(new Particle(new Pos(x,y+(step*c)),50,1,xVel,yVel,500,1));
					particle.add(new Particle(new Pos(x,y+(step*c)),50,1,-1*xVel,yVel,500,1));
					step*=-1;
					if(i%2==0)
						c++;
				}
				break;
			}
		}
	}
	public static void createGame(int Level)
	{
		//randomize the kind of layout
		for(int i = 0;i<player.size();i++)
		{
			Player tempPlayer=player.get(i);
			int n = tempPlayer.ball.size();
			if(n==0)
			{
				createBall((int)tempPlayer.getX()+tempPlayer.getWidth()/2-ballRadius/2,(int)tempPlayer.getY()-ballRadius-1,0,maxBallVel*-1,i);
				//tempPlayer.incBall();
				//tempPlayer.ball.add(new Ball((int)tempPlayer.getX()+tempPlayer.getWidth()/2-ballRadius/2,(int)tempPlayer.getY()-ballRadius-1,0,maxBallVel*-1,i));
			}
			else if(n==1)
			{
				Ball tempBall=tempPlayer.ball.get(0);
				tempBall.setPos(tempPlayer.getX()+tempPlayer.getWidth()/2-ballRadius/2,tempPlayer.getY()-ballRadius-1);
				tempBall.setXVel(0);
				tempBall.setYVel(-1);
			}
			else
			{
				double angle=(Math.PI/2)/(n-1);
				double Angle=0;
				for(int k=0;k<n;k++)
				{
					Ball tempBall=tempPlayer.ball.get(k);
					tempBall.setPos(tempPlayer.getX()+tempPlayer.getWidth()/2-ballRadius/2,tempPlayer.getY()-ballRadius-1);
					Angle=angle*k+Math.PI/4;
					double xVel=maxBallVel*Math.cos(Angle);
					double yVel=maxBallVel*Math.sin(Angle)*-1;
					tempBall.setXVel(xVel);
					tempBall.setYVel(yVel);
				}
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
			//type=Rand(0,9)%2;
			brick.add(new Brick(type,wallSpace+10+(i%numX)*(brickWidth+20),50+(i/numX)*(brickHeight+10),maxBrickVel,life));
		}
	}
	public static void startGame()
	{
		if(numPlayers==1)
		{
			player.remove(1);
			gameMode=GameMode.arcade;
		}
		else
		{
			player.get(0).setWidth((int)(playerWidth*0.9));
			player.get(1).setWidth((int)(playerWidth*0.9));
			
		}
		createGame(level);
		changeState(GameState.inGame);
	}
	public static boolean checkCollision(Player a, Ball b)
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
				return true;
			}
		return false;
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
	public static boolean checkCollision(Powerup b, Player a)
	{
		int x1=(int)a.getX();
		int x2=x1+a.getWidth();
		int y1=(int)a.getY();
		int y2=y1+a.getHeight();
		int X1=(int)b.getX();
		int Y1=(int)b.getY();
		int radius=b.getRadius();
		if(x1 <X1 + radius && x2 > X1)
			if(y1<Y1+radius && y2>Y1)
			{
				return true;
			}
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
	public static int totalBalls()
	{
		int count=0;
		for(int i=0;i<player.size();i++)
		{
			Player tmpPlayer = player.get(i);
			count+=tmpPlayer.getBalls();
		}
		return count;
	}
	public static void invoke(String methodName,Class[] types, Object[] args)
	{
		try
		{
			Class cls = Class.forName("Breakout");
			Method m=cls.getMethod(methodName, types);
			Breakout methodObject = new Breakout("invoker");
			m.invoke(methodObject, args);
		}
		catch(Throwable e)
		{
			System.out.println(e);
		}
	}
	public static void addLife(int owner, int amount)
	{
		player.get(owner).incLives(amount);
	}
	public static void incWidth(int owner, int amount)
	{
		Player tempPlayer=player.get(owner);
		createParticle(tempPlayer.getX()+tempPlayer.getWidth()/2,tempPlayer.getY()+tempPlayer.getHeight()/2,3,1);
		tempPlayer.incWidth(amount);
		
	}
	public static void readData()
	{
		try 
		{
			Scanner input = new Scanner(new FileReader("data.txt"));
			String in="";
			String parse[];
			while(input.hasNextLine())
			{
				in=input.nextLine();
				if(in.contains("="))
				{
					parse=in.split("=");
					if(parse.length==2)
					{
						if(parse[0].equals("highscore"))
						{
							highScore=Integer.parseInt(parse[1]);
						}
					}
				}
			}
			input.close();
		} 
		catch (FileNotFoundException e) {
			
			highScore=0;
			e.printStackTrace();
		}
	}
	public static void saveData()
	{
		try {
            File file = new File("data.txt");
            BufferedWriter output = new BufferedWriter(new FileWriter(file));
            output.write("highscore="+highScore);
            output.close();
        } catch ( IOException e ) {
            e.printStackTrace();
        }
	}
}

/* 

[50%] start menu
[+] player count
[?] highscore
[+] better graphics

[ ] randomized maps (follows pattern) aka formations
[+] temporary powerups (addBall? lives?)
[+] permanent powerups
[+] 2 players: movement "ad", "left right"
[+] 1st player data: left screen, 2nd player data: right screen : player.id!!

[+] POWERUPS: Activate via button:
[+] increasePlayerWidth
[+] addLives
[+] addBall - already exists 
[+] Graphics

TEXT FILES: 
[ ] level data read
[+] highscore write
 */
