package game;
/**
 * 
 */

/**
 * @author patrick
 *
 */
public class Player {

	private int viewDirection;
	
	private Sonar sonar;
	
	
	public Player(Sonar p) {
		
		this.sonar = p;
		viewDirection = Sonar.NORTH;
	}
	
	/**
	 * make the user look north
	 * */
	public void viewNorth() {
		System.out.println("NORTH");
		viewDirection = Sonar.NORTH;
	}

	/**
	 * make the user look south
	 * */
	public void viewSouth() {
		System.out.println("SOUTH");
		viewDirection = Sonar.SOUTH;
	}

	/**
	 * make the user look west
	 * */
	public void viewWest() {
		System.out.println("WEST");
		viewDirection = Sonar.WEST;
	}

	/**
	 * make the user look east
	 * */
	public void viewEast() {
		System.out.println("EAST");
		viewDirection = Sonar.EAST;
	}

	/**
	 * @return the viewDirection
	 */
	public int getViewDirection() {
		return viewDirection;
	}

	/**
	 * @param viewDirection the viewDirection to set
	 */
	public void setViewDirection(int viewDirection) {
		this.viewDirection = viewDirection;
		printViewDirection(viewDirection);
	}
	
	public void printViewDirection(int viewDirection) {
		if(viewDirection == 0)
			System.out.println("NORTH");
		else if(viewDirection == 1)
			System.out.println("EAST");
		else if(viewDirection == 2)
			System.out.println("SOUTH");
		else if(viewDirection == 3)
			System.out.println("WEST");
	}
	
	
}
