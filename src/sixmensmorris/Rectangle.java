package sixmensmorris;
/**
 * Defines a mathematical representation of a rectangle defined by the top left, top right, and bottom left corners.
 * @author Kelvin Lin, Jeremy Klotz
 * @version 1
 */
public class Rectangle {

	private Point topLeft, topRight, bottomRight; // three points will be used to construct each rectangle
	
	/**
	 * Initializes the field variables
	 * @param topLeft Is the top left corner point.
	 * @param topRight Is the top right corner point.
	 * @param bottomRight Is the bottom right corner point.
	 */
	public Rectangle(Point topLeft, Point topRight, Point bottomRight){
		this.topLeft = topLeft;
		this.topRight = topRight;
		this.bottomRight = bottomRight;
	}
	
	/**
	 * Returns the top left point of the rectangle.
	 * @return The top left point of the rectangle.
	 */
	public Point getTopLeft() {
		return topLeft;
	}
	
	/**
	 * Returns the top right point of the rectangle. 
	 * @return The top right point of the rectangle.
	 */
	public Point getTopRight() {
		return topRight;
	}
	
	/**
	 * Returns the bottom right point of the rectangle.
	 * @return The bottom right point of the rectangle.
	 */
	public Point getBottomRight() {
		return bottomRight;
	}
	
	/**
	 * Returns the width of the rectangle.
	 * @return The width of the rectangle. This will be used for assistance in properly scaling based on window size.
	 */
	public int getIntWidth(){
		return (int) Math.abs(this.topLeft.getX() - this.topRight.getX());
	}
	
	/**
	 * Return the height of the rectangle.
	 * @return The height of the rectangle. This will be used for assistance in properly scaling based on window size.
	 */
	public int getIntHeight(){
		return (int) Math.abs(this.topRight.getY() - this.bottomRight.getY());
	}
	
	/**
	 * Return the x-coordinate of the top left corner as an integer.
	 * @return The X coordinate of the top left corner. Parameter to draw the rectangle.
	 */
	public int getTopLeftIntX(){
		return topLeft.getIntX();
	}
	
	/**
	 * Return the y-coordinate of the top left corner as an integer.
	 * @return The Y coordinate of the top left corner. Parameter to draw the rectangle.
	 */
	public int getTopLeftIntY(){
		return topLeft.getIntY();
	}
	
	/**
	 * Return the bottom left point.
	 * @return The bottom left point since it aligns with the top left Y and bottom right X coordinates.
	 */
	public Point getBottomLeft(){
		return new Point(this.topLeft.getX(), this.bottomRight.getY());
	}
	
	
}
