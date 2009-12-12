package test;
import processing.core.PApplet;

public class Enemy {

	float xpos;
	float ypos;
	float xspeed=0.1f; 
	float yspeed=0.1f;
	
	PApplet parent; // The parent PApplet that we will render ourselves onto

	Enemy(PApplet p) {
		parent = p;
	}

	void display() {	    
		parent.fill(255,0,0); 
		parent.ellipse(xpos,ypos,20,20);
	} 
	void move() {
		xpos =	xpos+xspeed;
		ypos = 	ypos+yspeed;
	}

}
