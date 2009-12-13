package game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import com.sun.org.apache.xpath.internal.axes.SelfIteratorNoPredicate;

import processing.core.*;

/**
 * @author patrick
 * */
public class Sonar extends PApplet {

	public static final int NORTH = 0;
	public static final int EAST = 1;
	public static final int SOUTH = 2;
	public static final int WEST = 3;
	public static int numberDirections = 4;
	
	private Player player;
	private static Boolean play;
	private int enemyTimer;
	private int midX;
	private int midY;
	/**
	 * {@value Key: Direction} {@value List of Enemies in this direction}
	 * */
	private HashMap<Integer, ArrayList<Enemy>> enemies;
	private PFont myFont;

	/**
	 * init method: sets up everything
	 * */
	public void setup() {
		size(400, 400);
		play = true;
		myFont = createFont("verdana", 12);
		textFont(myFont);

		enemies = new HashMap<Integer, ArrayList<Enemy>>();
		enemyTimer = 0;

		player = new Player(this);
		// createEnemies();
		midX = width/2;
		midY = height/2;
	}

	public void draw() {
		if (play) {
			background(255);
			//visual pointer in the middle
			fill(0);
			ellipse(midX, midY, 4, 4);
			if(player.getViewDirection() == NORTH)
				line(midX, midY, midX, midY-10);
			else if(player.getViewDirection() == SOUTH)
				line(midX, midY, midX, midY+10);
			else if(player.getViewDirection() == WEST)
				line(midX, midY, midX-10, midY);
			else if(player.getViewDirection() == EAST)
				line(midX, midY, midX+10, midY);
	
			if (!enemies.isEmpty()) {
				Collection<ArrayList<Enemy>> temp = enemies.values();
	
				for (int i = 0; i < temp.size(); i++) {
					for (ArrayList<Enemy> listToDraw : temp) {
						for (int j = 0; j < listToDraw.size(); j++) {
							listToDraw.get(j).move();
							if(listToDraw.get(j).gameover) {
								gameOver();
								return;
							}
							listToDraw.get(j).display();
						}
					}
				}
			}
	
			enemyTimer++;
			// determine a random direction
			// Random random = new Random();
			// int direction = random.nextInt(50000);
	
			if (enemyTimer == 200) {
				createEnemies();
				enemyTimer = 0;
			}
		}
	}

	/**
	 * fille the enemies list
	 * */
	public void createEnemies() {

		// determine a random direction
		Random random = new Random();
		Integer direction = random.nextInt(4);

		System.out.println("ENEMY DIRECTION: " + direction);

		ArrayList<Enemy> directionList;

		// case: no enemies in this direction
		if (enemies.get(direction) == null) {
			directionList = new ArrayList<Enemy>();
			directionList.add(new Enemy(this, direction));

			enemies.put(direction, directionList);
		} else {
			enemies.get(direction).add(new Enemy(this, direction));
		}

	}

	public void keyPressed() {
		if (key == CODED) {
			int currentDirection = player.getViewDirection();
			if (keyCode == LEFT){
				adjustSound(currentDirection, "left");
				player.setViewDirection(getLeftDirection(currentDirection));
			}
			if (keyCode == RIGHT){
				adjustSound(currentDirection, "right");
				player.setViewDirection(getRightDirection(currentDirection));
			}
			if (keyCode == CONTROL){
				targetLocked();
			}
		}
	}
	
	/**
	 * @param direction
	 * @return Returns the directions right to the input-direction.
	 */
	public int getRightDirection(int direction) {
		if(direction != numberDirections-1)
			return ++direction;
		return 0;
	}
	
	/**
	 * @param direction
	 * @return Returns the direction left to the input-direction.
	 */
	public int getLeftDirection(int direction) {
		if(direction != 0)
			return --direction;
		return numberDirections-1;
	}
	
	/**
	 * removes all enemys from list (and stops them beeing so noisy)
	 */
	public void removeAllEnemies() {
		if (!enemies.isEmpty()) {
			Collection<ArrayList<Enemy>> temp = enemies.values();

			for (ArrayList<Enemy> listToDraw : temp) {
				for (int j = 0; j < listToDraw.size(); j++) {
					listToDraw.get(j).stopSound();
				}
			}
		}
		enemies = new HashMap<Integer, ArrayList<Enemy>>();
	}
	
	
	/**
	 * @param direction
	 * @param moveTo Direction to which the pointer will move.
	 * 
	 * Adjusts gain and pan according to current-direction and moving-direction.
	 * The gain of the new direction will be set to 1, all other directions (last-direction) should have a gain of -10.
	 * The pan of new direction will be set to 0, the direction left to it to -1, right to it to 1 and the direction behind it to 0.
	 */
	public void adjustSound(int direction, String moveTo){
		if(moveTo.equals("left")){
			setGain(direction, -10);
			setGain(getLeftDirection(direction), 1);
			
			setPan(direction, 1);
			setPan(getLeftDirection(direction), 0);
			setPan(getLeftDirection(getLeftDirection(direction)), -1);
			setPan(getRightDirection(direction), 0);
		}
		else if(moveTo.equals("right")){
			setGain(direction, -10);
			setGain(getRightDirection(direction), 1);
			
			setPan(direction, -1);
			setPan(getRightDirection(direction), 0);
			setPan(getRightDirection(getRightDirection(direction)), 1);
			setPan(getLeftDirection(direction), 0);
		}
	}
	
	/**
	 * @param direction
	 * @param gain
	 * 
	 * Sets the gain of all enemies existing on the direction and the startingGain of the direction itself.
	 */
	public void setGain(int direction, float gain){
		if(enemies.get(direction) != null){
			ArrayList<Enemy> directionEnemies = enemies.get(direction);
			for(int i=0; i < directionEnemies.size(); i++){
				directionEnemies.get(i).setGain(gain);
			}
		}
		//set starting gain-value of direction (for future enemies)
		setStartingGain(direction, gain);
	}
	
	/**
	 * @param direction
	 * @param gain
	 * 
	 * Sets the startingGain of the input direction.
	 */
	public void setStartingGain(int direction, float gain){
		if(direction == 0)
			Enemy.gainN = gain;
		else if(direction == 1)
			Enemy.gainE = gain;
		else if(direction == 2)
			Enemy.gainS = gain;
		else if(direction == 3)
			Enemy.gainW = gain;
	}
	
	
	/**
	 * @param direction
	 * @param pan
	 * 
	 * Analogous to the setGain-method: sets the pan of all enemies existing on the direction and the startingPan of the direction itself.
	 */
	public void setPan(int direction, float pan){
		if(enemies.get(direction) != null){
			ArrayList<Enemy> directionEnemies = enemies.get(direction);
			for(int i=0; i < directionEnemies.size(); i++){
				directionEnemies.get(i).setPan(pan);
			}
		}
		//set starting pan-value of direction (for future enemies)
		setStartingPan(direction, pan);
	}
	
	/**
	 * @param direction
	 * @param pan
	 * 
	 * Sets the startingPan of the input direction.
	 */
	public void setStartingPan(int direction, float pan){
		if(direction == 0)
			Enemy.panN = pan;
		else if(direction == 1)
			Enemy.panE = pan;
		else if(direction == 2)
			Enemy.panS = pan;
		else if(direction == 3)
			Enemy.panW = pan;
	}
	
	/**
	 * @return true if enemy is in field of view (removes the enemy if so)
	 * */
	public boolean targetLocked(){
		boolean targetvisible = false;
		
		if (!enemies.isEmpty() && enemies.get(player.getViewDirection()) != null) {
			/*Collection<ArrayList<Enemy>> temp = enemies.values();
			
			for (int i = 2; i < temp.size(); i++) {
				for (ArrayList<Enemy> seekList : temp) {
					for (int j = 0; j < seekList.size(); j++) {
						if(seekList.get(j).getDirection() == player.getViewDirection()){
							seekList.remove(seekList.get(j));
						}
					}
				}
			}*/
			
			//Shoot all enemies of direction.
			ArrayList<Enemy> currentDirectionEnemies = enemies.get(player.getViewDirection());
			for(int i=currentDirectionEnemies.size()-1; i >= 0; i--){
				currentDirectionEnemies.get(i).stopSound();
				currentDirectionEnemies.remove(i);
			}
			
			//Shoot just one enemy.
			/*ArrayList<Enemy> currentDirectionEnemies = enemies.get(player.getViewDirection());
			currentDirectionEnemies.get(0).stopSound();
			currentDirectionEnemies.remove(0);*/
		}
		
		return targetvisible;
	}
	
	public void gameOver() {
		play = false;
		removeAllEnemies();
		text("oh no! it's over! :((((((((((((",100,100);
	}

}