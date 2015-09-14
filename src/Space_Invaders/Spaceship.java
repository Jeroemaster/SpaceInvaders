package Space_Invaders;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Vector;

public class Spaceship {


	// the x and y coordinates of the Spaceship.
	private double x;
	private static double y = 425;
	private int lives;
	
	// the game the alien is a part of.
	private Game game;
	// the horizontal movement speed of the aliens.
	private int MovementSpeed = 1;

	// the bufferedImage of the spaceship.
	private BufferedImage Spaceship;
	
	/**
	 * this method makes the ship move left by 1 as long as it hasn't reached
	 * the border
	 *
	 */
	public void moveLeft() {
		if (x > 10) {
			x -= 2;
		}
	}

	/**
	 * this method makes the ship move right by 1 as long as it hasn't reached
	 * the border
	 *
	 */
	public void moveRight() {
		if (x < 600) {
			x += 2;
		}
	}
	
	/**
	 * the constructor of the Spaceship class.
	 * 
	 * @param int x
	 * @param int y
	 * @param  Game g
	 */
	public Spaceship(Game g){
		x = 317;
		game = g;
		lives = 3;
		
		SpriteSheet ss = new SpriteSheet(g.getSpriteSheet());
		Spaceship = ss.grabImage(277, 228, 26, 16);
	}
	
	/**
	 * the method creates a new bullet on the position of the ship
	 * and returns it
	 *
	 * @return Bullet newBullet
	 */
	public Bullet shoot() {
		SpriteSheet ss = new SpriteSheet(game.getSpriteSheet());
		Bullet newBullet = new Bullet(x+10, y+2, ss);
		return newBullet;
	}
	
	public int ifHit(Vector<Bullet> alienBullets) {
		for (int i = 0; i < alienBullets.size(); i++) {
			if (alienBullets.get(i).getX() >= x && alienBullets.get(i).getX() <= x+26) {
				if (alienBullets.get(i).getY() >= y && alienBullets.get(i).getY() <= y+16) {
					return i;
				}
			}
		}
		return -1;
	}

	/**
	 * the method that returns the x position
	 *
	 * @return x position
	 */
	public double getPosX() {
		return x;
	}

	/**
	 * the method that returns the y position
	 *
	 * @return y position
	 */
	public double getPosY(){
		return y;
	}
	
	/**
	 * the method that returns the amount of lives left.
	 * 
	 * @return amount of lives
	 */
	public int getLives(){
		return lives;
	}
	
	/**
	 * the method that decreases lives with one.
	 */
	public void decreaseLives(){
		lives--;
	}
	
	/**
	 * the method used to draw the spaceship on the screen.
	 * 
	 * @param Graphics g
	 */
	public void render(Graphics g){
		g.drawImage(Spaceship,(int) x,(int) y, null);
		
		for (int i=1; i<=lives; i++){
			g.drawImage(Spaceship, 10+30*(i-1), 452, null);
		}
		
	}
	
}
