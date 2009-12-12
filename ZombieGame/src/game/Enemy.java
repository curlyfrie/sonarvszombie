package game;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import processing.core.PApplet;

public class Enemy {

	float xpos;
	float ypos;
	float xspeed=0.1f; 
	float yspeed=0.1f;
	
	//"noise" control
	float gain;
	static final float gainN = 0f;
	static final float gainE = 0.25f;
	static final float gainS = 0.5f;
	static final float gainW = 0.75f;
	
	// The parent PApplet that we will render ourselves onto
	PApplet parent; 
	
	
	int direction;
	
	Minim minim;
	AudioPlayer sound;

	/**
	 * public Enemy(!) default constructor
	 * @param p calling applet
	 * @param pDirection defines the enemy's direction
	 * */
	public Enemy(PApplet p, int pDirection) {
		parent = p;
		
		minim = new Minim(p);
		sound = minim.loadFile("/game/sting.mp3");
		
		//setting start values
		if(pDirection == Sonar.NORTH) {
			xpos = p.getWidth()/2;
			ypos = 0;
			direction = Sonar.NORTH;
			gain = gainN;
		}
		if(pDirection == Sonar.EAST) {
			xpos = p.getWidth();
			ypos = p.getHeight()/2;
			direction = Sonar.EAST;
			gain = gainE;
		}
		if(pDirection == Sonar.SOUTH) {
			xpos = p.getWidth()/2;
			ypos = p.getHeight();
			direction = Sonar.SOUTH;
			gain = gainS;
		}
		if(pDirection == Sonar.WEST) {
			xpos = 0;
			ypos = p.getHeight()/2;
			direction = Sonar.WEST;
			gain = gainW;
		}
		
		//setting up playback of sound (depending on direction)
//		sound.setVolume(volume);
//		System.out.println("sound volume = " + sound.getVolume());
//		System.out.println("sound volume = " + sound.volume());
//		System.out.println("sound gain = " + sound.gain());
//		System.out.println("sound gain = " + sound.getGain());
		
		
		sound.setGain(-1*gain);
		sound.loop();
		
	}

	void display() {	    
		parent.fill(255,0,0); 
		parent.ellipse(xpos,ypos,20,20);
	} 
	void move() {
//		xpos =	xpos+xspeed;
//		ypos = 	ypos+yspeed;
		
		if(direction == Sonar.NORTH) {
			ypos += yspeed;
		}
		if(direction == Sonar.EAST) {
			xpos -= xspeed;
		}
		if(direction == Sonar.SOUTH) {
			ypos -= yspeed;
		}
		if(direction == Sonar.WEST) {
			xpos += xspeed;
		}
		
		//afte movement, we transform the sound
		modifySound();
	}
	
	void modifySound(){
		
	}

	/**
	 * @return the direction
	 */
	public int getDirection() {
		return direction;
	}
	
}
