package alien;

import bullet.Bullet;
import spaceinvaders.LogFile;

/**
 * the first alien type is considered the easiest. it has the least health and
 * the bullets fly the slowest.
 * 
 * @author Group 23
 *
 */
public class AlienType1 extends Alien {

  /**
   * the constructor of alien type 1.
   * 
   * @param x
   * @param y
   * @param g
   */
  public AlienType1(final double x, final double y) {
    super(x, y);
    LogFile.getInstance().writeCreate("AlienType1", x, y);
    setSpritesheet(7, 225, 16, 16);
    setScore(10);
    setHealth(1);
  }

  /**
   * the method creates a new bullet on the position of the alien and returns
   * it.
   *
   * @return Bullet newBullet
   */
  @Override
  public final Bullet shoot() {
    final Bullet newBullet = new Bullet(getX() + 5, getY() + 2);
    LogFile.getInstance().writeShoot("AlienType1", getX(), getY());
    return newBullet;
  }
}
