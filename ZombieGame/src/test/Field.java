package test;
import processing.core.*;
public class Field extends PApplet{

	Enemy e;
	int sonarx;
	int sonary;
	
	int fieldSize = 400;
	
	int frequency=2;
	private float angle;

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
		
		drawcenter();
		drawSonarline();
	}
	
	public void drawSonarline() {
		  float px = width/2 + cos(radians(angle))*(width/2);
		  float py = height/2 + sin(radians(angle))*(height/2);
		  angle-=frequency;

		line(width/2,height/2,px,py);
	}
	
	public void drawcenter() {
		fill(0);
		ellipse(width/2,height/2,10,10);
	}

}