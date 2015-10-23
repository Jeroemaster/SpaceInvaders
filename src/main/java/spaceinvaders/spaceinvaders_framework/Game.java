package spaceinvaders.spaceinvaders_framework;

import iterator.ConcreteAggregate;
import iterator.Iterator;

import java.awt.Canvas;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.util.Vector;
import java.util.Date;

import alien.Alien;
import alien.AlienFactory;
import bullet.Bullet;
import bullet.Bullet;
import alien.Alien;
import level.Level;
import level.LevelFactory;

/**
 * The game class is the main class.
 * 
 * @author Group 23
 *
 */
public class Game extends Canvas {

    /**
     * running == true when the game is running.
     */
    private boolean running = false;

    private ScoreMenu s_menu;

    /**
     * booleans related to the spaceships action
     */
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean spacePressed = false;

    private boolean bossLevel;

    private ConcreteAggregate ConcreteAggregate = new ConcreteAggregate();
    /**
     * long that is used to set a limit between the spaceship
     *
     * being able to fire.
     */
    private long lastFire = 0;

    /**
     * Vector to store all alien, bullet and barrier objects
     */
    private Vector<Alien> aliens;
    private Vector<Bullet> alienBullets;
    private Vector<Bullet> shipBullets;
    private Vector<Barrier> barriers;

    private int counter;
    private HighscoreManager highscoremanager;
    private int score = 0;

    private Spaceship spaceship;
    public static LogFile logfile;
    private Screen screen;

    private Level level;
    private int levelNumber = 1;

    public Game() {
        counter = 0;
        screen = new Screen();
    }

    /**
     * The start method will be called once at the start of the game. it is
     * mainly used to start up the main thread of our game.
     */
    public synchronized void start() {
        // an if statement that is to prevent that the start method creates two
        // threads if it accidently called twice.
        if (running) {
            return;
        }
        running = true;
        // initialize all the entities
        init();
    }

    /**
     * the stop method of the game. it is called only if we accidently leave the
     * game loop. if the game has no errors this method will not be called.
     */
    private synchronized void stop() {
        // returns if for some accident the stop method is called before the
        // game has started.
        if (!running) {
            return;
        }
        running = false;

        /*
         * // tries to join all the threads together. try { thread.join(); }
         * catch (InterruptedException e) { e.printStackTrace(); }
         */

        logfile.writeString("Game ended because of an error at " + new Date());
        logfile.close();

        // exits the application.
        System.exit(1);
    }

    /**
     * the init method initializes all the entities that will appear in the
     * game.
     */
    public void init() {
        aliens = new Vector<Alien>(0);
        alienBullets = new Vector<Bullet>(0);
        shipBullets = new Vector<Bullet>(0);
        barriers = new Vector<Barrier>(0);

        logfile = LogFile.getInstance();
        logfile.open();
        logfile.writeString("Game started at " + new Date());

        spaceship = new Spaceship();

        highscoremanager = new HighscoreManager();

        level = LevelFactory.createLevel(levelNumber);
        aliens = level.createAliens();
        barriers = level.createBarriers();
    }

    /**
     * the run method is the method that has the main game loop that will be
     * called repeatedly when the game is ongoing.
     */
    public void runGame() {
        screen.render(this);
        counter++;
        if (counter >= 50) {
            alienShoot();
            counter = 0;
        }
        if (levelNumber > 15) {
            end();
        } else if (aliens.size() == 0) {
            clearVectors();
            level = LevelFactory.createLevel(++levelNumber);
            spaceship.resetPosition();
            aliens = level.createAliens();
            barriers = level.createBarriers();
            if (levelNumber % 5 == 0) {
                bossLevel = true;
            } else {
                bossLevel = false;
            }
        } else if (aliens.get(aliens.size() - 1).reachedY(400)) {
            end();
        } else if (aliens.get(aliens.size() - 1).reachedY(360)) {
            barriers.clear();
        }
        removeOffScreenBullets();
        listenForKeys();
        checkIfHit();
        moveAliens();
    }

    public void moveAliens() {
        Iterator iteralien = ConcreteAggregate.createIterator(aliens);

        while (iteralien.hasNext()) {
            Alien alien = (Alien) iteralien.next();
            alien.hmovement();
        }

        // this if statement will only be used if all the aliens need to be
        // updated simultaneously.
        if (Alien.getupdateLogic()) {

            Iterator iteralien2 = ConcreteAggregate.createIterator(aliens);

            while (iteralien2.hasNext()) {
                Alien alien = (Alien) iteralien2.next();
                alien.vmovement();
            }

            // logicRequiredThisLoop = false;

            Game.logfile.writeString("Aliens reached a border and moved down");
        }
    }

    public void listenForKeys() {
        // resolve the movement of the ship.
        if ((screen.getLeftPressed()) && (!screen.getRightPressed())) {
            spaceship.moveLeft();
            if (screen.getSpacePressed()) {
                spaceship.shoot();
            }
        } else if ((screen.getRightPressed()) && (!screen.getLeftPressed())) {
            spaceship.moveRight();
            if (screen.getSpacePressed()) {
                spaceship.shoot();
            }
        }
        // if we're pressing fire, attempt to fire
        if (screen.getSpacePressed()) {
            shipBullets.addElement(spaceship.shoot());
            screen.blockSpace();
        }
    }

    public void checkIfHit() {
        if (!spaceship.defeated()) {
            int hit = spaceship.ifHit(alienBullets);
            if (hit != -1) {
                alienBullets.removeElementAt(hit);
            }
        } else {
            end();
        }
        Iterator iteralien = ConcreteAggregate.createIterator(aliens);

        while (iteralien.hasNext()) {
            Alien alien = (Alien) iteralien.next();
            int hit = alien.ifHit(shipBullets);
            if (hit != -1) {
                if (alien.defeated()) {
                    score = alien.addScore(score);
                    aliens.removeElementAt(iteralien.position());
                }
                shipBullets.removeElementAt(hit);
            }
        }

        Iterator iterbarrier = ConcreteAggregate.createIterator(barriers);

        while (iterbarrier.hasNext()) {
            Barrier barrier = (Barrier) iterbarrier.next();
            int hit = barrier.ifHit(alienBullets);
            if (hit != -1) {
                alienBullets.removeElementAt(hit);
                if (barrier.destroyed()) {
                    barriers.removeElementAt(iterbarrier.position());
                }
            }
        }

    }

    /**
     * this method clears the vectors of the aliens, alienbullets,
     * spaceshipbullets and barriers for the next level.
     */
    private void clearVectors() {
        aliens.clear();
        barriers.clear();
        alienBullets.clear();
        shipBullets.clear();
    }

    /**
     * The method that that randomly selects an alien. and adds its bullet to
     * the vector.
     */
    public void alienShoot() {
        if (bossLevel != true) {
            Random rand = new Random();
            int randNr = rand.nextInt(aliens.size());
            alienBullets.addElement(aliens.get(randNr).shoot());
        } else {
            alienBullets.addAll(aliens.get(0).BossShoot());
        }
    }

    /**
     * The method that removes all the bullets that are offscreen.
     */
    public void removeOffScreenBullets() {
        Iterator iteralien = ConcreteAggregate.createIterator(alienBullets);

        while (iteralien.hasNext()) {

            Bullet bullet = (Bullet) iteralien.next();
            if (bullet.reachedY(450)) {
                Game.logfile.writeOffscreen("Alien", bullet.getX());
                alienBullets.removeElementAt(iteralien.position());
            }

        }

        Iterator itership = ConcreteAggregate.createIterator(shipBullets);

        while (itership.hasNext()) {

            Bullet bullet = (Bullet) itership.next();
            if (bullet.reachedY(450)) {
                Game.logfile.writeOffscreen("Spaceship", bullet.getX());
                alienBullets.removeElementAt(itership.position());
            }

        }

    }

    /**
     * getter method for the LogicrequiredThisLoop boolean
     * 
     * @return boolean
     */
    public boolean getRunning() {
        return running;
    }

    /**
     * setter method for the running boolean.
     * 
     * @param boolean b
     */
    public void setRunning(boolean b) {
        running = b;
    }

    /**
     * getter method for the counter integer
     */
    public int getcounter() {
        return counter;
    }

    /**
     * getter method for the spaceship object
     */
    public Spaceship getSpaceship() {
        return spaceship;
    }

    /**
     * equals method that compares if two game objects are equal.
     */
    public boolean equals(Object other) {
        boolean result = false;
        if (other instanceof Game) {
            Game that = (Game) other;
            if (this.getcounter() == that.getcounter()) {
                result = true;
            }
        }
        return result;
    }

    /**
     * Compares two images pixel by pixel.
     *
     * @param imgA
     *            - the first image.
     * @param imgB
     *            - the second image.
     * @return whether the images are both the same.
     */
    public boolean compareImages(Object a, Object b) {

        if (a instanceof BufferedImage && b instanceof BufferedImage) {
            BufferedImage imgA = (BufferedImage) a;
            BufferedImage imgB = (BufferedImage) b;

            // The images mush be the same size.
            if (imgA.getWidth() == imgB.getWidth()
                    && imgA.getHeight() == imgB.getHeight()) {
                int width = imgA.getWidth();
                int height = imgA.getHeight();

                // Loop over every pixel.
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        // Compare the pixels for equality.
                        if (imgA.getRGB(x, y) != imgB.getRGB(x, y)) {
                            return false;
                        }
                    }
                }
            } else {
                return false;
            }

            return true;
        }
        return false;

    }

    /**
     * the getter metod that returns the alienvector.
     * 
     * @return
     */
    public Vector<Alien> getAlienVector() {
        return aliens;
    }

    /**
     * the method that adds a bullet to the shipbullets vector.
     * 
     * @param bill
     */
    public void addShipBullets(Bullet bill) {
        shipBullets.add(bill);
    }

    /**
     * the getter methdo that returns the shipbullets vector.
     * 
     * @return
     */
    public Vector<Bullet> getShipBullets() {
        return shipBullets;
    }

    /**
     * the method that adds a bullet to the alienbullets vector.
     * 
     * @param bill
     */
    public void addAlienBullets(Bullet bill) {
        alienBullets.add(bill);
    }

    /**
     * the getter method that returns the alienbullets vector.
     * 
     * @return
     */
    public Vector<Bullet> getAlienBullets() {
        return alienBullets;
    }

    /**
     * the method that adds an alien to the alien vector.
     * 
     * @param a
     */
    public void addAlien(Alien a) {
        aliens.add(a);
    }

    public Vector<Barrier> getBarriers() {
        return barriers;
    }

    /**
     * the method we use to exit the application.
     */
    public void end() {
        running = false;
        highscoremanager.addScore(score);
        logfile.writeString("Game ended at " + new Date());
        logfile.close();
        screen.close();
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public int getScore() {
        return score;
    }

    public HighscoreManager getHSManager() {
        return highscoremanager;
    }

    /**
     * the set highscoremanager sets the new Highscoremanager. This method is
     * used only for testing purposes.
     */
    public void setHighscoremanager(HighscoreManager manager) {
        highscoremanager = manager;
    }

    /**
     * the set score method is able to manually set the score of the player.
     * this method is only used for testing purposes.
     * 
     * @param score1
     */
    public void setscore(int score1) {
        score = score1;
    }

    /**
     * the getter method for the scoremenu of the game. mainly used for testing.
     * 
     * @return s_menu
     */
    public ScoreMenu getScoreMenu() {
        return s_menu;
    }
}
