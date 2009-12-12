import java.util.ArrayList;
import java.util.Hashtable;

import ddf.minim.AudioPlayer;
import ddf.minim.Minim;

import processing.core.*;

/**
 * @author patrick
 * */
public class MyProcessingSketch extends PApplet {

	private boolean north;
	private boolean south;
	private boolean west;
	private boolean east;

	// list of zombies for each direction
	private ArrayList<AudioPlayer> northernList;
	private ArrayList<AudioPlayer> southernList;
	private ArrayList<AudioPlayer> westernList;
	private ArrayList<AudioPlayer> easternList;

	private Minim minim;
	private AudioPlayer soundEffect;

	/**
	 * initialize method
	 * */
	public void setup() {
		size(200, 200);
		background(0);

		northernList = new ArrayList<AudioPlayer>();
		southernList = new ArrayList<AudioPlayer>();
		easternList = new ArrayList<AudioPlayer>();
		westernList = new ArrayList<AudioPlayer>();

		minim = new Minim(this);
		soundEffect = minim.loadFile("sting.mp3");
		// minim.getLineIn();

	}

	/**
	 * draw method is required. it should be called every x-ms: we use this to
	 * our advantage...
	 * */
	public void draw() {
		stroke(255);
		if (mousePressed) {
			line(mouseX, mouseY, pmouseX, pmouseY);
		}
	}

	/**
	 * determines what to do with key input
	 * */
	public void keyPressed() {
		if (key == CODED) {
			if (keyCode == UP)
				viewNorth();
			if (keyCode == DOWN)
				viewSouth();
			if (keyCode == LEFT)
				viewWest();
			if (keyCode == RIGHT)
				viewEast();
			if (keyCode == CONTROL)
				soundEffect.play();
			
		}
	}

	/**
	 * make the user look north
	 * */
	public void viewNorth() {
		System.out.println("NORTH");
		north = true;
		south = false;
		east = false;
		west = false;
	}

	/**
	 * make the user look south
	 * */
	public void viewSouth() {
		System.out.println("SOUTH");
		north = false;
		south = true;
		east = false;
		west = false;
	}

	/**
	 * make the user look west
	 * */
	public void viewWest() {
		System.out.println("WEST");
		north = false;
		south = false;
		east = false;
		west = true;
	}

	/**
	 * make the user look east
	 * */
	public void viewEast() {
		System.out.println("EAST");
		north = false;
		south = false;
		east = true;
		west = false;
	}

	public void stop() {
		// always close Minim audio classes when you are done with them
		soundEffect.close();
		// always stop Minim before exiting
		minim.stop();

		super.stop();
	}

}
