package game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

//import com.sun.org.apache.xpath.internal.axes.SelfIteratorNoPredicate;

import ddf.minim.AudioPlayer;
import ddf.minim.Minim;

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
	
	public static final String INTRO = "intro";
	public static final String OUTRO = "outro";
	public static final String START = "start";
	public static final String LEVEL1 = "level1";
	public static final String LEVEL1_INTRO = "level1intro";
	public static final String LEVEL2 = "level2";
	public static final String LEVEL2_INTRO = "level2intro";
	public static final String LEVEL3 = "level3";
	public static final String LEVEL3_INTRO = "level3intro";
	
	private int maxEnemyCount = 10;
	
	private String level;
	private Player player;
	private static Boolean play;
	private int enemyTimer;
	private int randomTime;
	private int midX;
	private int midY;
	private float max_speed = 0.4f;
	/**
	 * {@value Key: Direction} {@value List of Enemies in this direction}
	 * */
	private HashMap<Integer, ArrayList<Enemy>> enemies;
	private PFont myFont;
	
	private Minim minim;
	private AudioPlayer welcome;
	private AudioPlayer introplayer;
	private AudioPlayer outroplayer;
	private AudioPlayer kapitel1player;
	private AudioPlayer kapitel2player;
	private AudioPlayer kapitel3player;
	private AudioPlayer gameover;
	private AudioPlayer miss;
	private AudioPlayer shoot;
	private AudioPlayer rocket;
	private AudioPlayer no_ammo;
	private AudioPlayer reload;
	private int welcomeTimer;
	private int missCounter;
	private int enemyCount;
	private int max_ammo = 3;
	private int ammo = max_ammo;
	private boolean lastEnemy;
	private String enemyType;


	/**
	 * init method: sets up everything
	 * */
	public void setup() {
		size(400, 400);
		play = false;
		myFont = createFont("verdana", 12);
		textFont(myFont);
		
		enemyCount=0;
		
		enemies = new HashMap<Integer, ArrayList<Enemy>>();
		enemyTimer = 0;
		missCounter = 0;
		Random random = new Random();
		randomTime = random.nextInt(50) + 150;

		player = new Player(this);
		// createEnemies();
		midX = width/2;
		midY = height/2;
		minim = new Minim(this);
		welcome = minim.loadFile("/sounds/welcome.mp3");
		gameover = minim.loadFile("/sounds/gameover.mp3");
		miss = minim.loadFile("/sounds/miss.mp3");
		shoot = minim.loadFile("/sounds/gunshot1.mp3");
		rocket = minim.loadFile("/sounds/rocket.mp3");
		no_ammo = minim.loadFile("/sounds/no_ammo.mp3");
		reload = minim.loadFile("/sounds/shotgun_pump.mp3");
		introplayer = minim.loadFile("/sounds/intro.mp3");
		outroplayer = minim.loadFile("/sounds/outro.mp3");
		kapitel1player = minim.loadFile("/sounds/intro_long.mp3");
		kapitel2player = minim.loadFile("/sounds/kapitel2.mp3");
		kapitel3player = minim.loadFile("/sounds/kapitel3.mp3");
		
		level = START;
		
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
	
			enemyTimer++;
			// determine a random direction
			// Random random = new Random();
			// int direction = random.nextInt(50000);
	
			if (enemyTimer == randomTime) {
				System.out.println("level: "+level);
				if(level.equals("level1") && !moreThanXEnemies(0))
					createEnemies();
				else if(level.equals("level2") && !moreThanXEnemies(1))
					createEnemies();
				else if(level.equals("level3") && !moreThanXEnemies(1))
					createEnemies();
				enemyTimer = 0;
				Random random = new Random();
				randomTime = random.nextInt(300) + 150;
			}
			} else if (level==INTRO) {
				background(255);
				fill(255,0,0);
				text("†berspringen mit SPACE",midX,midY);
				textAlign(CENTER);
				introplayer.play();
			} else if (level==LEVEL1_INTRO) {
				max_speed = 0.4f;
				background(255);
				fill(255,0,0);
				text("†berspringen mit SPACE",midX,midY);
				textAlign(CENTER);
				kapitel1player.play();
			} else if (level==LEVEL2_INTRO) {
				max_speed = 0.5f;
				background(255);
				fill(255,0,0);
				text("†berspringen mit SPACE",midX,midY);
				textAlign(CENTER);
				kapitel2player.play();
			} else if (level==LEVEL3_INTRO) {
				max_speed = 0.6f;
				background(255);
				fill(255,0,0);
				text("†berspringen mit SPACE",midX,midY);
				textAlign(CENTER);
				kapitel3player.play();
			} else if (level==OUTRO) {
				background(255);
				fill(255,0,0);
				text("Danke fŸrs Spielen. Lg Gott",midX,midY);
				textAlign(CENTER);
				outroplayer.play();
				
			} else if (level==START){
				background(255);
				fill(255,0,0);
				text("Welcome to Zombie Game! Press SPACE to begin",midX,midY);
				textAlign(CENTER);
	//			welcome.setGain(0);
	//			welcome.setPan(0);
				welcome.play();
			}
	}

	/**
	 * fille the enemies list
	 * */
	public void createEnemies() {
		
		enemyCount ++;
		
		// determine a random direction
		Random random = new Random();
		Integer direction = random.nextInt(4);

		System.out.println("ENEMY DIRECTION: " + direction);

		ArrayList<Enemy> directionList;

		// case: no enemies in this direction
		if (enemies.get(direction) == null) {
			directionList = new ArrayList<Enemy>();
			
			directionList.add(new Enemy(this, direction, enemyType, max_speed));

			enemies.put(direction, directionList);
			System.out.println("speed:"+enemies.get(direction).get(0).xspeed);
		} else {
			enemies.get(direction).add(new Enemy(this, direction, enemyType, max_speed));
		}
		
		if(enemyCount==maxEnemyCount) {
			lastEnemy = true;
			System.out.println("lastenemy");
		}

	}

	public void keyPressed() {
		if(key == ' '){
			if (level==INTRO) {
				if (introplayer.isPlaying()) introplayer.close();
				level=START;
				return;
			}
			if (level==START) {
				if (welcome.isPlaying()) welcome.close();
				if (outroplayer.isPlaying()) welcome.close();
				level=LEVEL1_INTRO;
				return;
			}
			else if (level==LEVEL1_INTRO) {
				if (kapitel1player.isPlaying()) kapitel1player.close();
				enemyType = Enemy.ZOMBIE;
				level = LEVEL1;
			}
			else if (level==LEVEL2_INTRO) {
				if (kapitel2player.isPlaying()) kapitel2player.close();
				enemyType = Enemy.SUBMARINE;
				level = LEVEL2;
			}
			else if (level==LEVEL3_INTRO) {
				if (kapitel3player.isPlaying()) kapitel3player.close();
				enemyType = Enemy.DEVIL;
				level=LEVEL3;
			}
			else if (level==OUTRO) {
				if (kapitel3player.isPlaying()) kapitel3player.close();
				level=START;
			}
			if (!play) {
				//welcome.close();
//				minim.stop();
				play=true;
			}
		}
		if (key == CODED) {
			int currentDirection = player.getViewDirection();
			if (keyCode == LEFT){
				adjustSound(currentDirection, "left");
				player.setViewDirection(getLeftDirection(currentDirection));
			}
			else if (keyCode == RIGHT){
				adjustSound(currentDirection, "right");
				player.setViewDirection(getRightDirection(currentDirection));
			}
			else if (keyCode == CONTROL){
				if((!shoot.isPlaying() || !rocket.isPlaying()) && !reload.isPlaying()){
					targetLocked();
				}
			}
			else if (keyCode == SHIFT){
				reload();
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
		if(ammo != 0){
			ammo--;
			shoot.rewind();
			if (level.equals("level2")) {
				rocket.play();
			}
			else
				shoot.play();
		
		
		
			if (!enemies.isEmpty() && enemies.get(player.getViewDirection()) != null && enemies.get(player.getViewDirection()).size()>0) {
				System.out.println(enemies.get(player.getViewDirection()).size());
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
						
				/*
				 * Shoot just one enemy -> enemy closest to the center.
				 * diffX = abs( centerX - enemyX )
				 * diffY = abs( centerY - enemyY )
				 * sumXY = diffX + diffY
				 * 
				 * The enemy with the smallest sumXY is the nearest to the center and should get shot.
				 * 
				 */
				ArrayList<Enemy> currentDirectionEnemies = enemies.get(player.getViewDirection());
				float diffX = 0;
				float diffY = 0;
				float sumXY = 0;
				int closestEnemyPos = 0;
				diffX = abs(midX - currentDirectionEnemies.get(closestEnemyPos).xpos);
				diffY = abs(midY - currentDirectionEnemies.get(closestEnemyPos).ypos);
				sumXY = diffX + diffY;
				
				for(int i=0; i < currentDirectionEnemies.size(); i++){
					diffX = abs(midX - currentDirectionEnemies.get(i).xpos);
					diffY = abs(midY - currentDirectionEnemies.get(i).ypos);
					if(diffX+diffY <= sumXY){
						sumXY = diffX + diffY;
						closestEnemyPos = i;
					}
				}
				currentDirectionEnemies.get(closestEnemyPos).stopSound();
				currentDirectionEnemies.remove(closestEnemyPos);
				
				// check if player has done level
				if (lastEnemy) {
					play=false;
					lastEnemy = false;
					enemyCount = 0;
					removeAllEnemies();
					if (level == LEVEL1)
						level = LEVEL2_INTRO;
					if (level == LEVEL2)
						level = LEVEL3_INTRO;
					if (level == LEVEL3)
						level = OUTRO;
				}
				
			} else {
				System.out.println("miss");
				miss.rewind();
				miss.play();
				/*missCounter++;
				if (missCounter==3) {
					delay(1000);
					gameOver();
					return false;
				}*/
			}
		}
		else{
			no_ammo.rewind();
			no_ammo.play();
		}
		
		return targetvisible;
	}
	
	public void reload() {
		ammo = max_ammo;
		reload.rewind();
		reload.play();
	}
	
	public boolean enemiesEmpty() {
		for(int i=0; i<numberDirections-1; i++){
			if (enemies.get(i) != null && !enemies.get(i).isEmpty()){
				//System.out.println("enemies auf "+i);
				return false;
			}
		}
		return true;
	}
	
	public boolean moreThanXEnemies(int number) {
		int countZombies = 0;
		for(int i=0; i<numberDirections-1; i++){
			if (enemies.get(i) != null && !enemies.get(i).isEmpty()){
				countZombies += enemies.get(i).size();
			}
		}
		if(countZombies > number){
			System.out.println("countZombies > "+number+". "+countZombies);
			return true;
		}
		return false;
	}
	
	public void gameOver() {
		play = false;
		removeAllEnemies();
		text("oh no! it's over! :((((((((((((",100,100);
		gameover.play();
		
		
	}
	
	/*public void stopSound(){
		// always close Minim audio classes when you are done with them
//		sound.close();
		// always stop Minim before exiting
		minim.stop();
		 
		super.stop();
	}*/

}