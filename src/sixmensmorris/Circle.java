package sixmensmorris;
/**
 * Defines a mathematical representation of a circle using its center point and radius. 
 * Contains access programs to field variables, and to detect user input.
 * @author Kelvin Lin, Jeremy Klotz
 * @version 1
 */

public class Circle {

	private Point center; // declaring private variables for abstract data type - Circle
	private double radius;// they allow us to calculate the following properties
	
	/**
	 * Constructor method required to create object of type Circle with a radius and center point.
	 * @param center	The center of the circle
	 * @param radius	The radius of the circle
	 */
	public Circle(Point center, double radius){  
		this.center = center;
		this.radius = radius;
	}
	
	/**
	 * Knowing the radius, we can find the diameter ( d = 2 * r )
	 * @return The diameter of the circle.
	 */
	public int getIntDiameter(){ 	
		return (int) (this.radius*2);
	}
	
	/** 
	 * Returns the x-coordinate of the center point as an integer.
	 * @return The X - coordinate of the center point, useful for properly constructing the board, and for the debugging section.
	 */
	public int getIntPointX(){		
		return this.center.getIntX();
	}
	
	/**
	 * Returns the y-coordinate of the center point as an integer.
	 * @return The Y - coordinate of the center point, useful for properly constructing the board, and for the debugging section.
	 */
	public int getIntPointY(){		
		return this.center.getIntY();
	}
	
	/**
	 * Returns the radius of the circle as an integer.
	 * @return The radius of the circle.
	 */
	public int getIntRadius(){		// return radius as an integer value
		return (int)this.radius;
	}
	
	/**
	 * 	Returns TRUE if mouse is pointing over the circle, otherwise returns FALSE
	 * @param mouse is the X, and Y coordinates of where the mouse is pointing
	 * @return TRUE if mouse is pointing over the circle, otherwise return FALSE
	 */
	public boolean isMouseOver(Point mouse){ 
		int x = mouse.getIntX();
		int y = mouse.getIntY();
		return x > center.getIntX() - radius && x < center.getIntX() + radius && y > center.getIntY() - radius && y < center.getIntY() + radius;
	}
	
}
