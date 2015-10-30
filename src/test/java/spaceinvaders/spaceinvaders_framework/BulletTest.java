package spaceinvaders.spaceinvaders_framework;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import bullet.Bullet;
import spaceinvaders.Game;
import state.Executor;

/**
 * BulletTest tests all the methods in the bullet class.
 * 
 * @author Group 23
 */
public class BulletTest {

	/**
	 * The game object that is used in all of the test cases.
	 */
	private Game game;
	private Executor exec;

	/**
	 * This method is executed before every test. It creates the game class and
	 * sets up the required spritesheet.
	 */
	@Before
	public void setUpGame() {
		exec = new Executor();
		exec.run();
		game = new Game(exec);
		game.init();
	}

	/**
	 * the JUnit test of the Constuctor method of Bullet.
	 */
	@Test
	public void testBullet() {
		final Bullet bullet = new Bullet(1, 1);
		assertEquals((double) 1, bullet.getX(), 0.00001);
		assertEquals((double) 1, bullet.getY(), 0.00001);
	}

	/**
	 * the JUnit test of the MoveUp method of Bullet.
	 */
	@Test
	public void testMoveUp() {
		final Bullet bullet = new Bullet(1, 20);
		game.setRunning(true);

		bullet.moveUp();
		assertEquals((double) 17.8, bullet.getY(), 0.00001);
		assertTrue(game.getRunning());
	}

	/**
	 * the JUnit test of the MoveDown method of Bullet.
	 */
	@Test
	public void testMoveDown() {
		final Bullet bullet = new Bullet(1, 20);
		game.setRunning(true);

		bullet.moveDown();
		assertEquals((double) 22.2, bullet.getY(), 0.00001);
		assertTrue(game.getRunning());
	}

	/**
	 * The JUnit test that test the equals method that compares two bullets, and
	 * checks if they are equal.
	 */
	@Test
	public void testEquals() {
		final Bullet bullet1 = new Bullet(1, 20);
		final Bullet bullet2 = new Bullet(1, 20);

		assertEquals(bullet1, bullet2);
		assertNotSame(bullet1, bullet2);
	}

}
