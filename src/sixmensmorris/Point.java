package sixmensmorris;
/**
 * Defines a mathematical representation of a circle using its x-coordinate and its y-coordinate.
 * @author Kelvin Lin, Jeremy Klotz
 * @version 1
 */
public class Point {

	private double x, y; // X and Y coordinates of the point object
	
	/**
	 * Point constructor using two parameters
	 * @param x Is the X coordinate.
	 * @param y Is the Y coordinate.
	 */
	public Point(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Returns the x-coordinate.
	 * @return X coordinate.
	 */
	public double getX(){
		return x;
	}
	
	/**
	 * Returns the y-coordinate.
	 * @return Y coordinate.
	 */
	public double getY(){
		return y;
	}
	
	/**
	 * Returns the x-coordinate as an integer.
	 * @return Integer approximation of the X coordinate.
	 */
	public int getIntX(){
		return (int)x;
	}
	
	/**
	 * Returns the y-coordinate as an integer.
	 * @return Integer approximation of the Y coordinate.
	 */
	public int getIntY(){
		return (int)y;
	}
	
	/**
	 * Return the distance between two point objects
	 * @param that Is a second point.
	 * @return The distance between two point objects.
	 */
	public double getDistance(Point that){
		return Math.sqrt(Math.pow((that.getX() - this.getX()), 2) + Math.pow((that.getY() - this.getY()), 2));
	}
	
}
