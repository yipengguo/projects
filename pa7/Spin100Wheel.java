/*
 * Name: Yipeng Guo
 * Login: cs11fadl 
 * Date: November 9, 2016
 * File: Spin100Wheel.java
 * Sources of Help: Object draw website with all the methods
 *
 * This program defines the behavior of the wheel.
 */

import objectdraw.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.*;
import java.lang.*;

/*
 * This class defines how a spinWheel object should act
 */
public class Spin100Wheel extends ActiveObject {
  private Location loc = null; //declare location object
  private VisibleImage image; //Initiate visible image object
  private JButton spin, finish, restart; //pass these as parameters
  public int ticks = 0; //this is how many ticks are left on the wheel
  int top = 2; //index of image array that corresponds to top of wheel
  int delay = 1; //delay constantly changes
  boolean spinning = false; //state of wheel
  Image[] images = new Image[20]; //image array
  private static final int[] SCORES = { 50, 85, 30, 65, 10, 45, 70, 25, 90, 5,
  100, 15, 80, 35, 60, 20, 40, 75, 55, 95 }; //scores array
  VisibleImage firstNumber, secondNumber, thirdNumber, fourthNumber,fifthNumber;
  //declare visible images
  JLabel scoreLabel; //parameter in constructor
  int score = 0; //score is stored in the wheel
  int player = 0; //player number
  int fourthIndex = 19; //fourth image of wheel index in the image array
  int fifthIndex = 18; //fifth image of wheel index in the image array
  //I will add these to the y-value of the top image location
  double secondPos = 99;
  double thirdPos = 198;
  double fourthPos = 297;
  double fifthPos = 396;
  int middle = 2; //subtract from top to get index of middle image
  int fourth = 17; //subtract from top to get index of fourth image
  int fifth = 18; //subtract from top to get index of fifth image
  int mod = 20; // so I can modulus the image index values for the wheel
  int middleDelay = 20; //middle delay value
  int highDelay = 200; //higher delay value
  int delayAdd = 5; //amount to add to delay value
  int maxScore = 100; //exceeding this ends turn
  double convert = 0.05; //convert random value to a tick value
  double tickThreshhold = 0.65; //this is where tick starts to decrement
  int baseTickValue = 50; //base tick value to add to
  int maxTickValue = 63; //start decrementing here
  Spin100Controller controller; //controller object so I can access its buttons

  /**
   * Spin100Wheel constructor
   * @param images so wheel pulls images from this array
   * @param loc location of the top number in the wheel
   * @param spin spin button that corresponds to the wheel
   * @param finish finish button that corresponds to the wheel 
   * @param restart so both wheels can control restart
   * @param scoreLabel so wheel can edit corresponding score label
   * @param player is the player for the corresponding wheel
   * @param canvas is the canvas the wheel is made on
   */
  public Spin100Wheel(Image[] images, Location loc, JButton spin, JButton
  finish, JButton restart, JLabel scoreLabel, int player, DrawingCanvas canvas, 
  Spin100Controller controller) {
    this.spin = spin; //spin button
    this.finish = finish; //finish button
    this.restart = restart; //restart button
    this.images = images; //match up image arrays
    this.scoreLabel = scoreLabel; //score label
    this.player = player; //players match up
    this.controller = controller; //reference to controller
    firstNumber = new VisibleImage(images[top], loc, canvas); //top of wheel
    secondNumber = new VisibleImage(images[1], loc.getX(), loc.getY()+secondPos,
		   canvas); //second image of wheel
    thirdNumber = new VisibleImage(images[0], loc.getX(), loc.getY()+thirdPos, 
		  canvas); //third image of wheel
    fourthNumber = new VisibleImage(images[fourthIndex], loc.getX(), 
		   loc.getY()+fourthPos, canvas); //fourth image of wheel
    fifthNumber = new VisibleImage(images[fifthIndex], loc.getX(), 
		  loc.getY()+fifthPos, canvas); //fifth image of wheel
    start(); 
  }

  /**
   * run method defines what the wheel does after running
   */
  public void run() {
    while(true) { //runs indefinitely
      pause(0); //Idk why but the wheel only spins with some code here
      if(ticks > 0) { //Wheel spins while there are ticks left
        ticks--; //each spin is one less tick
        top++; //index of image of top of wheel changes each tick
	//mod 20 for the indexes so that the images cycle in a wheel
        firstNumber.setImage(images[top%mod]); 
        secondNumber.setImage(images[(top-1)%mod]);
        thirdNumber.setImage(images[(top-middle)%mod]);
        fourthNumber.setImage(images[(top+fourth)%mod]);
        fifthNumber.setImage(images[(top+fifth)%mod]);
        if(delay < middleDelay) {
          pause(delay); //wheel moves fast at start
 	  delay++;
        } else if(delay >= middleDelay) {
   	  pause(delay); //wheel slows down
	  //delay += delayAdd;
        } else if(delay >= highDelay) {
	  pause(delay); //wheel slows down more
	  delay += middleDelay;
        }
        if(ticks == 0) { //when the wheel is done spinning
  	  delay = 1; //reset wheel
 	  spinning = false; //wheel stopped
	  //buttons enabled again since wheel isnt moving
	  spin.setEnabled(true); 
	  finish.setEnabled(true);
	  restart.setEnabled(true);
	  //add the corresponding score array value depending on index of image
	  //in the middle of the wheel
	  score += SCORES[(top-middle)%mod];
	  //update the players score 
	  scoreLabel.setText("Player " + player + "'s score: " + score);
        }
	if(score>=maxScore) {
          //the turn will end
	  spin.setEnabled(false); 
	  finish.setEnabled(false);
	  controller.scoreCap(player); //either ends game or switches players
	}
      }
    }
  }

  /**
   * Random number generator
   * @return returns a random number of ticks
   */
  public int getRandomTicks() {
    Random tick = new Random(); //random object
    double number = tick.nextDouble(); //random double from 0-1
    if(number < tickThreshhold) { //if under threshhold
      return baseTickValue + (int)Math.ceil(number/convert); //increase ticks   
    } else { //after threshhold
      return maxTickValue - (int)Math.ceil(1-number); //decrease ticks
    }
  }

  /**
   * Set tick method sets value of ticks variable when called
   */
  public void setTicks() {
    if(spinning == false) { //won't affect tick value while wheel spinning
      ticks = getRandomTicks();
      spinning = true; //after a value has been set, the wheel starts to spin
    }
  }
}
