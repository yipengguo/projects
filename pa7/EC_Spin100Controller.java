/*
 * Name: Yipeng Guo
 * Login: cs11fadl 
 * Date: November 9, 2016
 * File: EC_Spin100Controller.java
 * Sources of Help: Object draw website with all the methods
 *
 * This program makes a wheel of fortune game.
 */

import objectdraw.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

/**
   * Spin Controller class determines how my program will work
   */
public class EC_Spin100Controller extends WindowController {
  int score = 0; //starting score of each player
  int totalOneScore = 0; //total player 1 wins
  int totalTwoScore = 0; //total player 2 wins
  boolean switched = false; //switch state
  private static final int[] SCORES = { 50, 85, 30, 65, 10, 45, 70, 25, 90, 5,
  100, 15, 80, 35, 60, 20, 40, 75, 55, 95 }; //score values of each image
  private static final int IMAGE_WIDTH = 185; //image size
  private static final int IMAGE_HEIGHT = 99;;
  private static final double LEFT_ARROW_X = 258; //position of arrows
  private static final double LEFT_ARROW_Y = 186;
  private static final double RIGHT_ARROW_X = 758;
  private static final double RIGHT_ARROW_Y = 186;
  private static final double START_ANGLE = -15; //angles of arrows
  private static final double ARC_ANGLE = 30;
  private static final int CANVAS_WIDTH = 1000; //canvas width
  private static final int CANVAS_HEIGHT = 660; //canvas height
  private static final double ARC_SIZE = 150; //arc size of arrows
  private Location leftWheelLoc = new Location(158, 10); 
  private Location rightWheelLoc = new Location(658, 10);
  private Location tieLocation = new Location(410, 5);
  private Location p1Winner = new Location(20, 5); //p1 wins text location
  private Location p2Winner = new Location(740, 5); //"p2"
  private Location scoreLoc = new Location(435, 130); //total score location
  //initiate a lot of objects that i can edit later
  private Text tie, p1Wins, p2Wins, totalScore; 
  private JButton finishOne, restart, finishTwo, Switch;
  private JButton spinOne = new JButton("Click to Spin P1");
  private JButton spinTwo = new JButton("Click to Spin P2");
  private EC_Spin100Wheel leftWheel, rightWheel;
  private JLabel firstScore, secondScore;
  private FilledArc leftArrow, rightArrow;
  int fontSize = 16; //victory font size
  int arraySize = 20; //image array size
  int playerTwo = 2;
  int maxScore = 100; //turn ends when this is the score
  Image[] images = new Image[20]; //image array that i later populate
  EC_Spin100Controller controller = this; //create controller instance
  
  /**
   * Code runs as soon as canvas is opened.
   */
  public void begin() { 
    //I make a lot of panels so the text at the top will be formatted correctly
    //all of this code is just panel layout
    JPanel mainPanel = new JPanel(); 
    JPanel northPanel = new JPanel();
    JPanel southMainPanel = new JPanel(); 
    JPanel southTopPanel = new JPanel(); 
    JPanel southBottomPanel = new JPanel();
    JLabel spinLabel = new JLabel("Spin100"); 
    firstScore = new JLabel("Player 1's score: " + score);
    secondScore = new JLabel("Player 2's score: " + score);
    northPanel.add(spinLabel); 
    southTopPanel.add(firstScore);
    southBottomPanel.add(secondScore);
    southMainPanel.setLayout(new BorderLayout());
    southMainPanel.add(southTopPanel, BorderLayout.NORTH);
    southMainPanel.add(southBottomPanel, BorderLayout.SOUTH);
    mainPanel.setLayout(new BorderLayout()); 
    mainPanel.add(northPanel, BorderLayout.NORTH); 
    mainPanel.add(southMainPanel, BorderLayout.SOUTH); 
    Container contentPane = getContentPane(); 
    contentPane.add(mainPanel, BorderLayout.NORTH); 
    contentPane.validate(); 
    
    //initiate the buttons
    finishOne = new JButton("Finish Player 1");
    restart = new JButton("Restart");
    finishTwo = new JButton("Finish Player 2");
    Switch = new JButton("Switch");
    //lets buttons respond to click
    spinOne.addActionListener(new SpinOneButtonEventHandler());
    finishOne.addActionListener(new FinishOneButtonEventHandler());
    restart.addActionListener(new RestartButtonEventHandler());
    spinTwo.addActionListener(new SpinTwoButtonEventHandler());
    finishTwo.addActionListener(new FinishTwoButtonEventHandler());
    Switch.addActionListener(new SwitchButtonEventHandler());
    JPanel southPanel = new JPanel(); //button panel
    southPanel.add(spinOne);
    southPanel.add(finishOne);
    southPanel.add(restart);
    southPanel.add(Switch);
    southPanel.add(spinTwo);
    southPanel.add(finishTwo);
    //the game starts with player one going first
    finishTwo.setEnabled(false); 
    finishOne.setEnabled(false);
    spinTwo.setEnabled(false);
    contentPane.add(southPanel, BorderLayout.SOUTH);
    
    //tie and victory text
    tie = new Text("Tie", tieLocation, canvas);
    totalScore = new Text("Overall Wins P1: " + totalOneScore + ", P2: " +
		 totalTwoScore, scoreLoc, canvas); 
    p1Wins = new Text("P1 Winner", p1Winner, canvas);
    p2Wins = new Text("P2 Winner", p2Winner, canvas);
    //text formatting
    tie.setBold(true);
    p1Wins.setBold(true);
    p2Wins.setBold(true);
    tie.setFontSize(fontSize);
    p1Wins.setFontSize(fontSize);
    p2Wins.setFontSize(fontSize);
    tie.setColor(Color.BLUE);
    p1Wins.setColor(Color.GREEN);
    p2Wins.setColor(Color.GREEN);
    totalScore.setColor(Color.RED);
    //the messages don't show when the game just started
    tie.hide();
    p1Wins.hide();
    p2Wins.hide();
    totalScore.hide();

    for(int x = 0; x < arraySize; x++) { //populate image array
      images[x] = getImage("Big_Wheel-" + SCORES[x] + ".png"); //gets images
    }
    //create wheels and arrows
    leftWheel = new EC_Spin100Wheel(images, leftWheelLoc, spinOne, finishOne, 
		    restart, firstScore, 1, canvas, this);
    rightWheel = new EC_Spin100Wheel(images, rightWheelLoc, spinTwo, finishTwo
		     , restart, secondScore, playerTwo, canvas, this);
    leftArrow = new FilledArc(LEFT_ARROW_X, LEFT_ARROW_Y, ARC_SIZE, 
		          ARC_SIZE, START_ANGLE, ARC_ANGLE, canvas);
    rightArrow = new FilledArc(RIGHT_ARROW_X, RIGHT_ARROW_Y, ARC_SIZE, 
		          ARC_SIZE, START_ANGLE, ARC_ANGLE, canvas);
    leftArrow.setColor(Color.RED);
    rightArrow.setColor(Color.RED);
  }
  
  /**
   * This method runs every time a player reaches the max score or ends the game
   * @param player is the player whose turn it is
   */
  public void scoreCap(int player) {
    if(player == 1) {
      if(switched == false) {
        spinTwo.setEnabled(true); //goes to second player
      } else {
        spinTwo.setEnabled(false); //ends game
        finishTwo.setEnabled(false);
        //one of three messages displays
        if(leftWheel.score > maxScore && rightWheel.score > maxScore) {
   	  tie.show();
 	  totalScore.show();
	  totalScore.setText("Overall Wins P1: " + totalOneScore + ", P2: " +
		 totalTwoScore);
        } else if (leftWheel.score == rightWheel.score) {
	  tie.show();
	  totalScore.show();
	  totalScore.setText("Overall Wins P1: " + totalOneScore + ", P2: " +
		 totalTwoScore);
        } else if (leftWheel.score-maxScore < rightWheel.score-maxScore) {
	  p2Wins.show();
  	  totalScore.show();
	  totalScore.setText("Overall Wins P1: " + totalOneScore + ", P2: " +
		 totalTwoScore);
        } else {
	  p1Wins.show();
	  totalScore.show();
	  totalScore.setText("Overall Wins P1: " + totalOneScore + ", P2: " +
		 totalTwoScore);
        }
      }
    }
    if(player == playerTwo) {
      if(switched == true) {
        spinOne.setEnabled(true); //goes to second player
      } else {
        spinTwo.setEnabled(false); //ends game
        finishTwo.setEnabled(false);
        //one of three messages displays
        if(leftWheel.score > maxScore && rightWheel.score > maxScore) {
   	  tie.show();
 	  totalScore.show();
	  totalScore.setText("Overall Wins P1: " + totalOneScore + ", P2: " +
		 totalTwoScore);
        } else if (leftWheel.score == rightWheel.score) {
	  tie.show();
	  totalScore.show();
	  totalScore.setText("Overall Wins P1: " + totalOneScore + ", P2: " +
		 totalTwoScore);
        } else if (leftWheel.score-maxScore < rightWheel.score-maxScore) {
	  p2Wins.show();
  	  totalScore.show();
	  totalScore.setText("Overall Wins P1: " + totalOneScore + ", P2: " +
		 totalTwoScore);
        } else {
	  p1Wins.show();
	  totalScore.show();
	  totalScore.setText("Overall Wins P1: " + totalOneScore + ", P2: " +
		 totalTwoScore);
        }
      }
    }
  }

 /**
  * inner class for one of the buttons
  */
  private class SwitchButtonEventHandler implements ActionListener {
    public void actionPerformed(ActionEvent evt) {
      if(switched == false) {
        switched = true;
        spinOne.setEnabled(false);
	finishOne.setEnabled(false);
	spinTwo.setEnabled(true);
      } else {
	switched = false;
	spinOne.setEnabled(true);
	spinTwo.setEnabled(false);
      }
    }
  }

  /**
   * inner class for one of the buttons
   */
  private class SpinOneButtonEventHandler implements ActionListener {
    public void actionPerformed(ActionEvent evt) {
      finishOne.setEnabled(true); //can finish turn after one spin
      leftWheel.setTicks(); //spins player one wheel
      if(leftWheel.spinning == true) { //while spinning buttons disabled
      	spinOne.setEnabled(false);
        finishOne.setEnabled(false);
	restart.setEnabled(false);
      } 
    }
  }

  /**
   * inner class for one of the buttons
   */
  private class FinishOneButtonEventHandler implements ActionListener {
    public void actionPerformed(ActionEvent evt) {
      spinOne.setEnabled(false); //player one cant go again
      finishOne.setEnabled(false); //player ones turn already finished
      spinTwo.setEnabled(true); 
    }
  }

  /**
   * inner class for one of the buttons
   */
  private class RestartButtonEventHandler implements ActionListener {
    public void actionPerformed(ActionEvent evt) { 
      p1Wins.hide(); //hide victory messages because new game
      p2Wins.hide();
      tie.hide();
      //reset all the text and other objects
      firstScore.setText("Player 1's score: " + score); 
      secondScore.setText("Player 2's score: " + score);	    
      leftWheel = null;
      rightWheel = null;
      leftWheel = new EC_Spin100Wheel(images, leftWheelLoc, spinOne, finishOne,
		      restart, firstScore, 1, canvas, controller);
      rightWheel = new EC_Spin100Wheel(images, rightWheelLoc, spinTwo, finishTwo,
		       restart, secondScore, playerTwo, canvas, controller);
      leftArrow = null;
      rightArrow = null;
      leftArrow = new FilledArc(LEFT_ARROW_X, LEFT_ARROW_Y, ARC_SIZE, 
		          ARC_SIZE, START_ANGLE, ARC_ANGLE, canvas);
      rightArrow = new FilledArc(RIGHT_ARROW_X, RIGHT_ARROW_Y, ARC_SIZE, 
		          ARC_SIZE, START_ANGLE, ARC_ANGLE, canvas);
      leftArrow.setColor(Color.RED);
      rightArrow.setColor(Color.RED);
      if(switched == false) {
        spinOne.setEnabled(true);
      } else {
	spinTwo.setEnabled(true);
      }
    }
  }

  /**
   * inner class for one of the buttons
   */
  private class SpinTwoButtonEventHandler implements ActionListener {
    public void actionPerformed(ActionEvent evt) {
      finishTwo.setEnabled(true); //player two can finish after one spin
      rightWheel.setTicks(); //right wheel spins
      if(rightWheel.spinning == true) { //buttons disabled while spinning
      	spinTwo.setEnabled(false);
        finishTwo.setEnabled(false);
	restart.setEnabled(false);
      } 
    }
  }

  /**
   * inner class for one of the buttons
   */
  private class FinishTwoButtonEventHandler implements ActionListener {
    public void actionPerformed(ActionEvent evt) {
      finishTwo.setEnabled(false);
      spinTwo.setEnabled(false);
      scoreCap(playerTwo); //the game ends
    }
  }

  /**
   * main method
   * @param args is command line text
   */
  public static void main(String[] args) {
    new Acme.MainFrame(new EC_Spin100Controller(), args, CANVAS_WIDTH,
    CANVAS_HEIGHT);
  }
}
