class Brick
{
	private int type;
	private int x;
	private int y;
	private double xVel;
	private double yVel;
}
class Player
{
	private String name;
	private int lives=3;
	private int x;
	private int y;
	private int width;
}
class Ball
{
	private int x;
	private int y;
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

}

/*
GAME LOGIC:
Move ball
Move bricks
Move player : MOUSE OR KEYBOARD?

brick: 
type = life count; getting hit changes type, changes texture?(probably)
*/

/*
 Possible goals:
 randomized maps (follows pattern)
 
 
 
 */


