package test;
import processing.core.*;
public class Field extends PApplet{

	Enemy e;

	public void setup() {
		size(400,400);
		e = new Enemy(this);
	}

	public void draw() {
		background(255);
		fill(255);
		ellipse(width/2,height/2,width,height);
		e.move();
		e.display();
	}

}