package game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

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
	private int enemyTimer;
	private int midX;
	private int midY;
	/**
	 * {@value Key: Direction} {@value List of Enemies in this direction}
	 * */
	private HashMap<Integer, ArrayList<Enemy>> enemies;

	/**
	 * init method: sets up everything
	 * */
	public void setup() {
		size(400, 400);
		enemies = new HashMap<Integer, ArrayList<Enemy>>();
		enemyTimer = 0;

		player = new Player(this);
		// createEnemies();
		midX = width/2;
		midY = height/2;
	}

	public void draw() {
		background(255);
		stroke(0);
		if(player.getViewDirection() == NORTH)
			line(midX, midY, midX, midY-10);
		else if(player.getViewDirection() == SOUTH)
			line(midX, midY, midX, midY+10);
		else if(player.getViewDirection() == WEST)
			line(midX, midY, midX-10, midY);
		else if(player.getViewDirection() == EAST)
			line(midX, midY, midX+10, midY);
		
		//fill(255);
		// ellipse(width / 2, height / 2, width, height);

		if (!enemies.isEmpty()) {
			Collection<ArrayList<Enemy>> temp = enemies.values();

			for (int i = 0; i < temp.size(); i++) {
				for (ArrayList<Enemy> listToDraw : temp) {
					for (int j = 0; j < listToDraw.size(); j++) {
						listToDraw.get(j).move();
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
			if (keyCode == LEFT)
				player.setViewDirection(prevDirection(currentDirection));
			if (keyCode == RIGHT)
				player.setViewDirection(nextDirection(currentDirection));
			if (keyCode == CONTROL){
				targetLocked();
			}
		}
	}
	
	public int nextDirection(int currentDirection) {
		if(currentDirection != numberDirections-1)
			return ++currentDirection;
		return 0;
	}
	
	public int prevDirection(int currentDirection) {
		if(currentDirection != 0)
			return --currentDirection;
		return numberDirections-1;
	}
	
	
	/**
	 * @return true if enemy is in field of view (removes the enemy if so)
	 * */
	public boolean targetLocked(){
		boolean targetvisible = false;
		
		if (!enemies.isEmpty()) {
			Collection<ArrayList<Enemy>> temp = enemies.values();

			for (int i = 0; i < temp.size(); i++) {
				for (ArrayList<Enemy> seekList : temp) {
					for (int j = 0; j < seekList.size(); j++) {
						if(seekList.get(j).getDirection() == player.getViewDirection())
							seekList.remove(seekList.get(j));
					}
				}
			}
		}
		
		return targetvisible;
	}

}