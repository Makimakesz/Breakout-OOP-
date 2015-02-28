class Pos
{
	private int x;
	private int y;
	public int getX()
	{
		return x;
	}
	public int getY()
	{
		return y;
	}
	public void setX(int x)
	{
		this.x=x;
	}
	public void setY(int y)
	{
		this.y=y;
	}
	Pos()
	{
		x=0;
		y=0;
	}
	Pos(int x,int y)
	{
		this.x=x;
		this.y=y;
	}	
}
class Brick
{
	private int type;
	private Pos pos;
	private double xVel;
	private double yVel;
}
class Player
{
	private String name; //will probably be unused, can be asked but nothing will be done with it
	private int lives=3;
	private Pos pos;
	private int width;
}
class Ball
{
	private Pos pos;
	private double xVel;
	private double yVel;
}
public class Breakout {
	static boolean twoPlayers=false;
	static int width=800;
	static int height=640;
	
	public static void main(String[] args) 
	{
		if(args.length==1)
			if(args[0]=="2")
				twoPlayers=true;
		
		//DO GAME LOGIC
		//DRAW SHIT
		//KEYBOARD INPUT SOMEWHERE?
	}
	public static void draw()
	{
		//all drawing goes here
	}
	public static void createGame()
	{
		//set all the bricks and shit
		//
	}
	public static void game()
	{
		//all game logic goes here
	}
}

/*
GAME LOGIC:
Move ball
Move bricks
Move player : MOUSE OR KEYBOARD?

brick: 
type = life count; getting hit changes type, changes texture?(probably)



 Possible goals:
 randomized maps (follows pattern)
 
 */
 
/*

WALLS, WHERE, EDGE OF SCREEN? x=0 && x=width
HOW IT OPERATES?


 */

