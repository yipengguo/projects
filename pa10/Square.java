/*
 * Name: Yipeng Guo
 * Login: cs11fadl 
 * Date: December 1, 2016
 * File: Rectangle.java
 * Sources of Help: Object draw website with all the methods
 *
 * This program defines the behavior of a square.
 */

import java.awt.*;
import objectdraw.*;

/**
 * Square is an instance of an ARectangle object.
 */
public class Square extends ARectangle {
  //side is the length of a side of a square
  private int side;

  /**
   * Square constructor
   * makes a square with meaningless values
   */
  public Square() {
    //to set upperleft corner	  
    super();
    setSide(0);
  }

  /**
   * Square constructor
   * @param x is the x coordinate of the upper left corner
   * @param y is the y coordinate of the upper left corner
   * @param side is the length of a side
   */
  public Square( int x, int y, int side ) {
    //set upperleft corner and name
    super("Square", x, y);
    setSide(side);
  }

  /**
   * Square constructor
   * @param upperleft is the point to make a copy of
   * @param side is the side of the square
   */
  public Square( Point upperLeft, int side ) {
    //set name and make copy of upperleft
    super("Square", upperLeft);
    setSide(side);
  }

  /**
   * square constructor
   * @param r is the square to make a copy of
   */
  public Square( Square r ) { 
    //setname and make a copy of upperleft
    super("Square", r.getUpperLeft() );
    setSide( r.getSide() );
  }

  /**
   * to string method
   * writes out the shape name and its attributes
   * @return returns this as a string
   */
  @Override
  public String toString() { 
    return super.toString() + " Sides: " + side;
  } 

  /**
   * equals method
   * I just check if the toString matches up because that will achieve the 
   * check just fine.
   * @param o is the object to check equality to
   * @return returns true for equality, false for not
   */
  @Override
  public boolean equals( Object o ) {  
    if(o != null && this.toString().equals(o.toString())) {
      return true;
    } else {
      return false;
    }	  
  } 

  /**
   * draw method draws the square
   * @param canvas is what to draw on
   * @param c is the color of the square
   * @param fill is whether or not the square is filled
   */
  public void draw( DrawingCanvas canvas, Color c, boolean fill ) {
    //make upperleft into a location
    Location upperLeft = new Location(super.getUpperLeft().getX(),
    super.getUpperLeft().getY() );
    if(fill == true) {
      //filled square
      FilledRect square = new FilledRect(upperLeft, side, side, canvas);
      square.setColor(c);
    } else {
      //empty square
      FramedRect square = new FramedRect(upperLeft, side, side, canvas);
      square.setColor(c);
    }
  }

  /**
   * used to get the side of a square
   * @return returns this as an int
   */
  public int getSide() {
    return this.side;		  
  } 

  /**
   * used to set the side of a square
   * @param side is the value to set the side to
   */
  private void setSide( int side ) {
    this.side = side;
  }
}
