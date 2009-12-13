package game;
import ddf.minim.AudioPlayer;
import ddf.minim.Minim;
import processing.core.PApplet;

public class Enemy {

	float xpos;
	float ypos;
	float xspeed=0.1f; 
	float yspeed=0.1f;
	
	float radius = 20f;
	
	//"noise" control
	float gain;
	float pan;
	
	static float gainN = 1;
	static float gainE = -10;
	static float gainS = -10;
	static float gainW = -10;
	
	static float panN = 0;
	static float panE = 1;
	static float panS = 0;
	static float panW = -1;
	
	// The parent PApplet that we will render ourselves onto
	PApplet parent; 
	
	Boolean gameover;
	
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
		gameover=false;
		minim = new Minim(p);
		sound = minim.loadFile("/game/sting.mp3");
		
		//setting start values
		if(pDirection == Sonar.NORTH) {
			xpos = p.getWidth()/2;
			ypos = 0;
			direction = Sonar.NORTH;
			gain = gainN;
			pan = panN;
		}
		if(pDirection == Sonar.EAST) {
			xpos = p.getWidth();
			ypos = p.getHeight()/2;
			direction = Sonar.EAST;
			gain = gainE;
			pan = panE;
		}
		if(pDirection == Sonar.SOUTH) {
			xpos = p.getWidth()/2;
			ypos = p.getHeight();
			direction = Sonar.SOUTH;
			gain = gainS;
			pan = panS;
		}
		if(pDirection == Sonar.WEST) {
			xpos = 0;
			ypos = p.getHeight()/2;
			direction = Sonar.WEST;
			gain = gainW;
			pan = panW;
		}
		
		//setting up playback of sound (depending on direction)
//		sound.setVolume(volume);
//		System.out.println("sound volume = " + sound.getVolume());
//		System.out.println("sound volume = " + sound.volume());
//		System.out.println("sound gain = " + sound.gain());
//		System.out.println("sound gain = " + sound.getGain());
		
		
		sound.setGain(gain);
		sound.setPan(pan);
		sound.loop();
		
	}

	void display() {	    
		parent.fill(255,0,0); 
		parent.ellipse(xpos,ypos,radius,radius);
	} 
	void move() {
//		xpos =	xpos+xspeed;
//		ypos = 	ypos+yspeed;
		
		if(direction == Sonar.NORTH) {
			if (ypos>=parent.height/2-radius/2) {
				gameover=true;
				return;
			}
			ypos += yspeed;
		}
		if(direction == Sonar.EAST) {
			if (xpos<=parent.width/2+radius/2) {
				gameover = true;
				return;
			}
			xpos -= xspeed;
		}
		if(direction == Sonar.SOUTH) {
			if (ypos<=parent.height/2+radius/2) {
				gameover=true;
				return;
			}
			ypos -= yspeed;
		}
		if(direction == Sonar.WEST) {
			if (xpos>=parent.width/2-radius/2) {
				gameover=true;
				return;
			}
			xpos += xspeed;
		}
		
		//afte movement, we transform the sound
		modifySound();
	}
	
	void modifySound(){
		
	}
	
	void setGain(float v){
		sound.setGain(v);
	}
	
	void setPan(float v){
		sound.setPan(v);
	}
	
	void stopSound(){
		// always close Minim audio classes when you are done with them
		sound.close();
		// always stop Minim before exiting
		minim.stop();
		 
//		super.stop();
	}
	
	/**
	 * @return the direction
	 */
	public int getDirection() {
		return direction;
	}
	
}
