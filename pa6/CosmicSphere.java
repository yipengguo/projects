/*
 * Name: Yipeng Guo
 * Login: cs11fadl 
 * Date: November 2, 2016
 * File: CosmicSphere.java
 * Sources of Help: Object draw website with all the methods
 *
 * This program defines the behavior of the cosmic spheres.
 */

import objectdraw.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * This class defines what a CosmicSphere is and how it should act.
 */
public class CosmicSphere extends ActiveObject
implements MouseListener, MouseMotionListener, ActionListener, ChangeListener {
  //To respond to mouse and action events
  private FilledOval cosmicSphere; //cosmic sphere initiator
  int translate = 25; //Since the coordinates correspond to top left of sphere
  int maxSize = 100; //Max size of sphere
  int minSize = 25; //Min size of sphere
  int state = 0; //To alternate between grow and shrink
  int delay = 50; //50 millisecond delay
  int startingSize = 50; //starting size of sphere
  int maxSpeed = 101; //used to toggle speed toolbar
  int limit = 4; //this is the boundary next to edge that sphere cannot move to
  Location pressPoint; //keep track of point pressed before dragging
  double xLocation;//this is to calculate the x relative to current canvas size
  double yLocation;//this is to calculate the y relative to current canvas size
  int half = 2; //to divide stuff by two and find centers etc.
  boolean growth = true; //sphere only grows or shrinks if true
  double shrinkMove = 1; //Move the sphere so it stays centered
  double growMove = -1; //Move the sphere so it stays centered
  double xLoc; //xLoc to create the sphere
  double yLoc; //yLoc to create the sphere
  Line hLine; //Detects when line is moved to change color of sphere
  Line vLine; //Detects when line is moved to change color of sphere
  JButton start, stop, clear; //jbutton initiators
  JSlider slider; //jslider initiator
  DrawingCanvas canvas; //canvas initiator
  boolean checkPress; //check if sphere is pressed
  boolean spherePresent = true; //so sphere only removed if true
  //I need these abstract methods becase i use their interfaces
  public void mouseClicked(MouseEvent e){}
  public void mouseEntered(MouseEvent e){}
  public void mouseExited(MouseEvent e){}
  public void mouseMoved(MouseEvent e){}

  /**
   * CosmicSphere constructor
   * @param some parameter functions are explained above
   * @param growthState determines if the sphere will grow or not when its 
   * created depending on setting in CosmicSphereController
   * @param speed is how fast it will grow or shrink
   */
  public CosmicSphere(double xLoc, double yLoc, double size, 
		      DrawingCanvas canvas, Line hLine, Line vLine,
                      JButton start, JButton stop, JButton clear,
                      boolean growthState, int speed, JSlider slider) {
    cosmicSphere = new FilledOval(xLoc-translate, yLoc-translate, size, size,
                                  canvas);
    start();
    this.xLoc = xLoc; //So I can access xLoc in other methods
    this.yLoc = yLoc; //Same reason for these
    this.hLine = hLine; 
    this.vLine = vLine;
    this.start = start;
    this.stop = stop;
    this.slider = slider;
    this.canvas = canvas;
    this.clear = clear;
    xLocation = center().getX()/canvas.getWidth();
    //x location relative to current canvas size
    yLocation = center().getY()/canvas.getHeight();
    //y location relative to current canvas size
    growth = growthState; //growth is in run method
    setSpeed(speed); //set growth speed
    //so the buttons and stuff respond to events
    clear.addActionListener(this); 
    start.addActionListener(this);
    stop.addActionListener(this);
    slider.addChangeListener(this);
    canvas.addMouseListener(this);
    canvas.addMouseMotionListener(this);
  }

  /**
   * MousePress event
   * @param e is where the mouse was pressed
   */
  public void mousePressed(MouseEvent e) {
    Location point = new Location(e.getX(), e.getY()); //make e a location
    if(cosmicSphere.contains(point)) { //checks if sphere pressed
      checkPress = true;
      pressPoint = point; //this is used for mouse drag
    }
  }

  /**
   * MouseDrag event
   * @param e is where the mouse was draggged
   */
  public void mouseDragged(MouseEvent e) {
    xLocation = center().getX()/canvas.getWidth();
    //x location relative to current canvas size
    yLocation = center().getY()/canvas.getHeight();
    //y location relative to current canvas size
    Location point = new Location(e.getX(), e.getY()); //make e a location
    if(checkPress) {  //only moves if sphere pressed
      if(center().getY() < canvas.getHeight()-limit && center().getY() > limit
      && center().getX() < canvas.getWidth()-limit && center().getX() > limit){
    //Mouse drag only moves sphere centers up to 5 pixels from the boundary
        cosmicSphere.move(point.getX()-pressPoint.getX(), 
                         point.getY()-pressPoint.getY()); //move sphere
	pressPoint = point; //update presspoint 
      }
    }
    if(checkPress) { //this is if the sphere makes it to a boundary
      //When center has passed left side
      if(point.getX()-pressPoint.getX()>0 && center().getX()<=limit) {
        cosmicSphere.move(point.getX()-pressPoint.getX(), 0);
        pressPoint = point; //since point is always changing
      }
      //When center has passed top side
      if(point.getY()-pressPoint.getY()>0 && center().getY()<=limit) {
        cosmicSphere.move(0,point.getY()-pressPoint.getY());
        pressPoint = point; //since point is always changing
      }
      //When center has passed right side
      if(point.getX()-pressPoint.getX()<0 && center().getX()>=
         canvas.getWidth()-limit) {
        cosmicSphere.move(point.getX()-pressPoint.getX(), 0);
        pressPoint = point; //since point is always changing
      }
      //When center has passed bottom side
      if(center().getY()>=canvas.getHeight()-limit && 
	 point.getY()-pressPoint.getY()<0) { 
      //Only moves if dy negative
        cosmicSphere.move(0,point.getY()-pressPoint.getY());
        pressPoint = point; //since point is always changing
      }
    }
  }

  /**
   * Finds center of sphere
   * @return returns center of sphere as a location
   */
  public Location center() {
    Location center = new Location(cosmicSphere.getX()+cosmicSphere.getWidth()/
		                   half, cosmicSphere.getY()+
				   cosmicSphere.getHeight()/half);
    return center;
  }
  
  /**
   * MouseRelease event
   * @param e is where the mouse was released
   */
  public void mouseReleased(MouseEvent e) {
    checkPress = false; //sphere is no longer pressed
  }

  /**
   * Action Performed event
   * @param e is which button was pressed
   */
  public void actionPerformed(ActionEvent e) {
    if(e.getSource() == start) {
      growth = true; //so all spheres will grow and shrink
    } else if(e.getSource() == stop) { 
      growth = false; //so all spheres stop growing and shrinking
    } else if(e.getSource() == clear && spherePresent) {
      spherePresent = false; //only works if sphere is present
      cosmicSphere.removeFromCanvas(); //if sphere present, then remove it
    }
  }

  /**
   * State changed event
   * @param e is how the slider was changed
   */
  public void stateChanged(ChangeEvent e) {
    delay = maxSpeed-slider.getValue(); //so higher speed causes less delay
  }

  /**
   * Changes speed setting
   * @param setting is the speed setting
   */
  public void setSpeed(int setting) {
    delay = maxSpeed-setting; //changes delay to new setting
  }

  /**
   * How the sphere will act after start() method called in constructor
   */
  public void run() {
    int currentSize = startingSize; //Local variable that is different for each                                     //individual sphere
    while(true) { //runs indefinitely
      if(checkPress == false) { //only runs while sphere not selected
        cosmicSphere.moveTo(xLocation*canvas.getWidth()-
  		            cosmicSphere.getWidth()/half,
		            yLocation*canvas.getHeight()-
			    cosmicSphere.getHeight()/half);
        //relocates sphere when canvas size changed
      }
      if(center().getX() < (vLine.getEnd()).getX() && center().getY() < 
	(hLine.getEnd().getY())) {
        cosmicSphere.setColor(Color.cyan); //Quadrant 2 is cyan
      }
      if(center().getX() < (vLine.getEnd()).getX() && center().getY() >= 
	(hLine.getEnd().getY())) {
        cosmicSphere.setColor(Color.yellow); //Quadrant 3 is yellow
      }
      if(center().getX() >= (vLine.getEnd()).getX() && center().getY() < 
	(hLine.getEnd().getY())) {
        cosmicSphere.setColor(Color.magenta); //Quadrant 1 is magenta
      }
      if(center().getX() >= (vLine.getEnd()).getX() && center().getY() >= 
	(hLine.getEnd().getY())) {
        cosmicSphere.setColor(Color.black); //Quadrant 4 is black
      }
      if(state == 0 && cosmicSphere.getWidth() < maxSize && growth) { 
        //Growing state, runs if growth true
        currentSize++; //2 increments for growth of 2
	currentSize++;
        cosmicSphere.setSize(currentSize, currentSize); //Causes growth
	cosmicSphere.move(growMove, growMove); //Translate so centered
        pause(delay); //Delay makes it more viewable
        if(cosmicSphere.getWidth() >= maxSize) { 
          state = 1; //At max size switches to shrinking state
        }
      }
      if(state == 1 && cosmicSphere.getWidth() > minSize && growth) { 
        //Shrinking state, runs if growth true
        currentSize--; //2 increments for shrink of 2
	currentSize--;
        cosmicSphere.setSize(currentSize, currentSize); //Causes shrinkage
        cosmicSphere.move(shrinkMove, shrinkMove); //Translate so centered
        pause(delay); 
        if(cosmicSphere.getWidth() <= minSize) {
          state = 0; //At min size switches to shrinking state
        }
      }
    }
  }
}
