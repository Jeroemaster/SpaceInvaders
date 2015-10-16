package spaceinvaders.spaceinvaders_framework;

/**
 * MegaBullet is a subclass of bullet.
 * this new Bullet is way larger than a normal bullet.
 * 
 * @author Group 23
 *
 */
public class MegaBullet extends Bullet{

    /**
     * the constructor method of Mega bullet.
     * 
     * @param x
     * @param y
     * @param ss
     */
    public MegaBullet(double x, double y, SpriteSheet ss) {
        super(x, y, ss);
        setSpritesheet(475,890,30,60);
    }

}
