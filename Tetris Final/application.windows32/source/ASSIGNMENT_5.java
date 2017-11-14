import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class ASSIGNMENT_5 extends PApplet {

  //allows the use of maps, sets etc

/**
TETRIS
------
CONTROLS:
W: ROTATE
A: LEFT
D: RIGHT
S: MOVE DOWN

THIS TAB CONTAINS THE MAIN IMPLEMENTATION OF THE GAME. 
*/

int levelCount = 1;  //initial level count
int score = 0;       //initial score
int gridWidth = 10;  //width of the grid
int gridHeight = 22;    //height of the grid
Shape currentShape;    //the currently controlled shape
int timer;            //controls automatic movement of current shape

boolean isOver = false;    
boolean isPaused = false;    //toggles for stopping/pausing the game

HashMap <String, ShapeInfo> shapeInfoMap;    //maps strings (name of shape) to information of type ShapeInfo about the shape
ArrayList<String> typeList;        //Indexed list of the different types of shape
ArrayDeque<String> sequence;      //set sequence of blocks to be used, used in the randomiser implementation

Block[][] grid = new Block[gridWidth][gridHeight];    //the game board itself

public void settings() {
  size(600, 600);    //set here so that processing doesn't crash, for some reason
}

public void setup() {
  background(0xff020E12);
  rectMode(CENTER);
  frameRate(10);

  shapeInfoMap = new HashMap<String, ShapeInfo>();
  typeList = new ArrayList<String>();    //initialising fields of the game
  setupInfoForShape();                  
  sequence = generateSequence();        //these methods are in utility methods, fill out the sequence and the map
  timer = 0;
  currentShape = new Shape(sequence.pop());  //pops the first shape from the sequence to be used
  
  //initial drawing methods
  drawUI();
  drawGrid();
  fillGrid();
  currentShape.drawShape();      //Shape does not exist in the grid until it collides with something, so a separate draw method is used
}

public void draw() {
  timer++;    //increases timer, this will cause the automatic movement
 // background(#020E12);
  
  if(!isPaused && !isOver){    //if the game is either paused, or game over, nothing will happen.
  if (timer%(10-getDec()) == 0) {  //changing the mod amount changes the speed of the drop
    currentShape.moveShape("down");  //current shape moves down every 10-getDec() seconds
  }

  if(currentShape.checkCollide()){  //if the current shape collides with something
    currentShape.setBlocks();      //set the grid positions of the current shape to be blocks of the corresponding type
    if(checkGameOver()){        
       isOver = true;
    }
    currentShape = new Shape(sequence.pop());    //get new shape
    if(sequence.isEmpty()){    //if the sequence does not have anything left in it, a new sequence will be generated
      sequence = generateSequence();
    }
    for(int n = 0; n < 4; n++){  //arbitrary, will check for this amount in sequence
    checkRowClear();
    }
  }
  increaseLevel();
  drawUI();
  drawGrid(); //testing, may or may not keep
  fillGrid();
  currentShape.drawShape();
  }
  else {     //this will be used only if the game is paused or over. Does not have any of the gameplay methods, so will do nothing 
  drawUI();
  drawGrid();
  fillGrid();
  currentShape.drawShape();
  if(isOver){
    drawOver();    //draws game over popup on screen
  }
  drawPause();    //draws pause button based on status
  }
}

public void keyPressed() {
  if(!isPaused && !isOver){    //key presses will only do something if the game is not paused and not over
  if (key == 'a') {
    currentShape.moveShape("left");    //move shape left
  }
  if (key == 'd') {
    currentShape.moveShape("right");   //move shape right
  }
  if (key == 's') {
    currentShape.moveShape("down");    //move shape up
    timer = 0;
  }
  if (key == 'w') {
    currentShape.rotate();            //rotate the shape clockwise
  }
  drawUI();
  drawGrid();
  fillGrid();
  currentShape.drawShape();      //without these draw methods here, the game gets a bit jumpy
  drawNext();
  }
}

public void mousePressed(){
  if(mouseX > 425 && mouseX < 725 && mouseY > 355 && mouseY < 455){  //if mouse is pressed within confines of pause button
    isPaused = !isPaused;
  }
  else if(mouseX > 425 && mouseX < 725 && mouseY > 435 && mouseY < 485){    //if mouse is pressed within confines of restart button
  //SETS ALL THE IMPORTANT FIELDS AND INFORMATION BACK TO HOW IT WAS AT THE START, THEN REDRAWS
  grid = new Block[gridWidth][gridHeight];
  isOver = false;
  timer = 0;
  score = 0;
  levelCount = 1;
  sequence = generateSequence();
  currentShape = new Shape(sequence.pop());
  drawUI();
  drawGrid();
  fillGrid();
  currentShape.drawShape();
  drawNext();
  
  }
}
class Block {    //simple class to store the color and score of any position in the grid
  int blockColour;
  int scoreValue;

  public Block(int c, int score) {
    blockColour = c;
    scoreValue = score;
  }
}
class Shape {  //used for the currently selected shape in the game
  String shapeName;
  HashSet<PVector> shapeVectors;    //has a set of pvectors based on the current control point
  int c;                          //has a set color and score value, and a varying orientation
  PVector controlPoint;
  int scoreVal;
  int orientation;

  public Shape(String name) {
    shapeName = name;
    ShapeInfo info = shapeInfoMap.get(shapeName);
    c = info.c;
    scoreVal = info.scoreVal;
    controlPoint = new PVector(4, 1);    //places the shape at the top of the grid, roughly.
    shapeVectors = updatePoints(shapeName,orientation, controlPoint);  //update points is a complicated beast, it's in utility methods
    orientation = 0;
  }

  public void moveShape(String direction) {    
    if (direction.equals("left")) {
      if(checkSides("left")){    //if the shape will collide with anything on its left
        return;
      }
      for (PVector p : shapeVectors) {
        if (p.x < 1) {      //if any points of the shape will be out of bounds of the array
          return;
        }
      }
      controlPoint.x--;     //move the control point left and update the set of points
      shapeVectors = updatePoints(shapeName,orientation, controlPoint);    //the beast returns...
    } 
    
    else if (direction.equals("right")) {    //if the shape will collide with anything on its right
      if(checkSides("right")){
        return;
      }
      for (PVector p : shapeVectors) {
        if (p.x > grid.length-1) {      //if any of the points of the shape will be out of bounds of the array on the right
          return;
        }
      }
      controlPoint.x++;      //moves the control point right and updates the set of points
      shapeVectors = updatePoints(shapeName,orientation, controlPoint);
    }
    
    else if (direction.equals("down")) {
      if(!checkCollide()){
      for (PVector p : shapeVectors) {      //checks
        if (p.y >=  grid[0].length-1) {
          return;
        }
      }
      controlPoint.y++;
      shapeVectors = updatePoints(shapeName,orientation, controlPoint);
    }
    }
  }

  public boolean checkCollide() {    //this only checks downward collisions. Returns true if there is a collision, returns false otherwise
    for (PVector p : shapeVectors) {
      if (p.y == grid[0].length-1) {
        return true;
      }
      if (grid[(int)p.x][(int)p.y+1] != null) {
        return true;
      }
    }
    return false;
  }
  
  public boolean checkSides(String direction){  //checks side collisions, same logic as previous checks
    if(direction.equals("left")){
    for(PVector p: shapeVectors){
      if(p.x-1 < 0 || grid[(int)p.x-1][(int)p.y] != null){  //have to check if the point is within the array first, otherwise it crashes
        return true;
      }
    }
    }
    else if(direction.equals("right")){
    for(PVector p: shapeVectors){
      if(p.x+1 > grid.length-1 || grid[(int)p.x+1][(int)p.y] != null){
        return true;
      }
    }
    }
    return false;
  }
  
  public void rotate(){
    HashSet<PVector> checkPoints = updatePoints(shapeName, (orientation+1)%4, controlPoint);    //generates a set of points for the next rotation
    for(PVector p : checkPoints){      //if all of the rotated points are within the array and are not already taken
      if(p.x < 0 || p.x > grid.length-1 || grid[(int)p.x][(int)p.y] != null){
        return;
      }
      if(p.y < 0 || p.y > grid[0].length-1){
        return;
      }
    }      //if these conditions are not met, the current shape will be updated to the new orientation
      shapeVectors = checkPoints;
      orientation++;
      drawShape();
  }

  public void drawShape() {      //draws each part of the shape, based on its position. This is how it can be used to draw the next shape, which is outside the grid
    //drawGhost();
    for (PVector p : shapeVectors) {
      fill(c);
      rect(p.x*blockSize + blockSize/2 + leftEdge, p.y*blockSize + blockSize/2, blockSize, blockSize);
    }
  }

  public void setBlocks() {
    for (PVector p : shapeVectors) {
      grid[(int)p.x][(int)p.y] = new Block(c, scoreVal);    //sets the blocks in the shape to their corresponding position on the grid
    }
  }
}
class ShapeInfo {    //simple wrapper class for storing color value and score value of each type of shape
  int c;
  int scoreVal;

  public ShapeInfo(int col, int score) {
    c = col;
    scoreVal = score;
  }
}
/** 
 CONTAINS METHODS AND VARIABLES FOR THE UI OF THE GAME
 */

int blockSize = 27;
int leftEdge = 100;
int leftLine = 70;
int rightLine = 400;

public void drawUI() {
  background(0xff020E12);      //background
  stroke(200);
  strokeWeight(1);
  fill(255);
  line(rightLine, 0, rightLine, height);  //divider line for UI
  line(leftLine, 0, leftLine, height);  //divider line for UI

  //  LEVEL COUNTER
  strokeWeight(2);
  stroke(0);
  rect(500, 50, 150, 40, 10);
  fill(0);
  textSize(26);
  text("Level " + levelCount, 440, 60);


  //SCORE AREA
  fill(255);
  strokeWeight(2);
  stroke(0);
  rect(500, 120, 150, 50, 10);
  fill(0);
  textSize(20);
  text("Score: " + score, 430, 130);


  //NEXT BLOCK DISPLAY
  fill(255);
  strokeWeight(2);
  stroke(0);
  rect(500, 250, 150, 150, 10);
  fill(0);
  textSize(26);
  text("Next block", 430, 205);
  drawNext();

  drawPause();  //separate methods, because they vary based on mouse position
  drawRestart();
}

public void drawGrid() {      //draws the game grid 
  for (int n = 0; n < grid.length; n++) {  //width
    for (int i = 0; i < grid[0].length; i++) {  //height
      noFill();
      stroke(255);
      strokeWeight(1);
      rect(n*blockSize + blockSize/2 + leftEdge, i*blockSize + blockSize/2, blockSize, blockSize);
    }
  }
}

public void fillGrid() {    //fills the game grid with it's objects
  for (int n = 0; n < grid.length; n++) {
    for (int i = 0; i < grid[0].length; i++) {
      if (grid[n][i] != null) {
        fill(grid[n][i].blockColour);
        rect(n*blockSize + blockSize/2 + leftEdge, i*blockSize + blockSize/2, blockSize, blockSize);
      }
    }
  }
}

public void drawNext() {
  Shape next = new Shape(sequence.peek());    //returns but not removes the next shape
  next.controlPoint = new PVector(14, 9);    //approximate position so that the shape fits in the next block of the UI
  next.shapeVectors = updatePoints(next.shapeName, 0, next.controlPoint);    //generates points based on this position
  stroke(255);
  strokeWeight(1);
  next.drawShape();    //draws the next shape
}

public void drawOver() {      //if the game over is reached, a popup will be drawn in the middle of the screen
  fill(255);
  rect(235, 200, 250, 50, 10);
  fill(0);
  text("Game Over", 170, 210);
}

public void drawPause() {      //draws the pause button based on the state of the pause condition and the position of the mouse
  if (mouseX > 425 && mouseX < 725 && mouseY > 355 && mouseY < 455) {    //if the mouse is within the button, fill the button with grey instead of white
    fill(200);
  } else {
    fill(255);
  }
  strokeWeight(2);
  stroke(255, 0, 0);
  rect(500, 380, 150, 50, 10);
  if (isPaused) {
    fill(200, 0, 0);    //if the game is paused, make the pause text red
  } else {
    fill(0);
  }
  text("Pause ", 465, 390);
}

public void drawRestart() {
  if (mouseX > 425 && mouseX < 725 && mouseY > 435 && mouseY < 485) {   //if the mouse is within the button, fill the button with grey instead of white
    fill(200);
  } else {
    fill(255);
  }
  strokeWeight(2);
  stroke(255, 0, 0);
  rect(500, 460, 150, 50, 10);
  fill(0);
  text("Restart", 455, 470);
}

/**
void drawGhost() {
  Shape ghost = new Shape(currentShape.shapeName);
  ghost.orientation = currentShape.orientation;
  ghost.controlPoint = new PVector(currentShape.controlPoint.x, grid[0].length-1);
  ghost.shapeVectors = updatePoints(ghost.shapeName, ghost.orientation, ghost.controlPoint);
  while (!ghost.checkCollide()) {
    ghost.controlPoint = new PVector(ghost.controlPoint.x, ghost.controlPoint.y-1);
    ghost.shapeVectors = updatePoints(ghost.shapeName, ghost.orientation, ghost.controlPoint);
    }
  for (PVector p : ghost.shapeVectors) {
    fill(200);
    rect(p.x*blockSize + blockSize/2 + leftEdge, p.y*blockSize + blockSize/2, blockSize, blockSize);
  }
}
*/
/**
  UTILITY METHODS
  THE BACKBONE OF THE PROGRAM
*/

public HashSet<PVector> updatePoints(String type, int orientation, PVector control) {    //the beast itself. takes the type, orientation and a control point, and generates a set of points based on the combination of these.
  HashSet<PVector> newPoints = new HashSet<PVector>();
  
  /**
  With the exception of the o-block, each type has 4 orientations. Each of these will generate a separate set of points, using the control point as a reference. if I was
  to comment all of these, I'd still be doing it in the marking session (Exaggeration, I know, but they're basically all the same idea)
  */

  if (type.equals("o-block")) {
    newPoints.add(new PVector(control.x, control.y));
    newPoints.add(new PVector(control.x, control.y+1));
    newPoints.add(new PVector(control.x+1, control.y));
    newPoints.add(new PVector(control.x+1, control.y+1));
  }

  if (type.equals("i-block")) {
    if (orientation%4 == 0) {
      newPoints.add(new PVector(control.x, control.y));
      newPoints.add(new PVector(control.x-1, control.y));
      newPoints.add(new PVector(control.x+1, control.y));
      newPoints.add(new PVector(control.x+2, control.y));
    }
    if (orientation%4 == 1) {
      newPoints.add(new PVector(control.x, control.y));
      newPoints.add(new PVector(control.x, control.y-1));
      newPoints.add(new PVector(control.x, control.y+1));
      newPoints.add(new PVector(control.x, control.y+2));
    }
    if (orientation%4 == 2) {
      newPoints.add(new PVector(control.x, control.y));
      newPoints.add(new PVector(control.x+1, control.y));
      newPoints.add(new PVector(control.x-1, control.y));
      newPoints.add(new PVector(control.x-2, control.y));
    }
    if (orientation%4 == 3) {
      newPoints.add(new PVector(control.x, control.y));
      newPoints.add(new PVector(control.x, control.y+1));
      newPoints.add(new PVector(control.x, control.y-1));
      newPoints.add(new PVector(control.x, control.y-2));
    }
  }

  if (type.equals("j-block")) {
    if (orientation%4 == 0) {
      newPoints.add(new PVector(control.x, control.y));
      newPoints.add(new PVector(control.x, control.y+1));
      newPoints.add(new PVector(control.x, control.y-1));
      newPoints.add(new PVector(control.x+1, control.y-1));
    }
    if (orientation%4 == 1) {
      newPoints.add(new PVector(control.x, control.y));
      newPoints.add(new PVector(control.x-1, control.y));
      newPoints.add(new PVector(control.x+1, control.y));
      newPoints.add(new PVector(control.x+1, control.y+1));
    }
    if (orientation%4 == 2) {
      newPoints.add(new PVector(control.x, control.y));
      newPoints.add(new PVector(control.x, control.y-1));
      newPoints.add(new PVector(control.x, control.y+1));
      newPoints.add(new PVector(control.x-1, control.y+1));
    }
    if (orientation%4 == 3) {
      newPoints.add(new PVector(control.x, control.y));
      newPoints.add(new PVector(control.x-1, control.y));
      newPoints.add(new PVector(control.x+1, control.y));
      newPoints.add(new PVector(control.x-1, control.y-1));
    }
  }

  if (type.equals("l-block")) {
    if (orientation%4 == 0) {
      newPoints.add(new PVector(control.x, control.y));
      newPoints.add(new PVector(control.x, control.y+1));
      newPoints.add(new PVector(control.x, control.y-1));
      newPoints.add(new PVector(control.x-1, control.y-1));
    }
    if (orientation%4 == 1) {
      newPoints.add(new PVector(control.x, control.y));
      newPoints.add(new PVector(control.x-1, control.y));
      newPoints.add(new PVector(control.x+1, control.y));
      newPoints.add(new PVector(control.x+1, control.y-1));
    }
    if (orientation%4 == 2) {
      newPoints.add(new PVector(control.x, control.y));
      newPoints.add(new PVector(control.x, control.y-1));
      newPoints.add(new PVector(control.x, control.y+1));
      newPoints.add(new PVector(control.x+1, control.y+1));
    }
    if (orientation%4 == 3) {
      newPoints.add(new PVector(control.x, control.y));
      newPoints.add(new PVector(control.x+1, control.y));
      newPoints.add(new PVector(control.x-1, control.y));
      newPoints.add(new PVector(control.x-1, control.y+1));
    }
  }

  if (type.equals("t-block")) {
    if (orientation%4 == 0) {
      newPoints.add(new PVector(control.x, control.y));
      newPoints.add(new PVector(control.x, control.y-1));
      newPoints.add(new PVector(control.x, control.y+1));
      newPoints.add(new PVector(control.x+1, control.y));
    }
    if (orientation%4 == 1) {
      newPoints.add(new PVector(control.x, control.y));
      newPoints.add(new PVector(control.x+1, control.y));
      newPoints.add(new PVector(control.x-1, control.y));
      newPoints.add(new PVector(control.x, control.y+1));
    }
    if (orientation%4 == 2) {
      newPoints.add(new PVector(control.x, control.y));
      newPoints.add(new PVector(control.x-1, control.y));
      newPoints.add(new PVector(control.x, control.y+1));
      newPoints.add(new PVector(control.x, control.y-1));
    }
    if (orientation%4 == 3) {
      newPoints.add(new PVector(control.x, control.y));
      newPoints.add(new PVector(control.x, control.y-1));
      newPoints.add(new PVector(control.x+1, control.y));
      newPoints.add(new PVector(control.x-1, control.y));
    }
  }

  if (type.equals("s-block")) {
    if (orientation%4 == 0) {
      newPoints.add(new PVector(control.x, control.y));
      newPoints.add(new PVector(control.x, control.y-1));
      newPoints.add(new PVector(control.x+1, control.y));
      newPoints.add(new PVector(control.x+1, control.y+1));
    }
    if (orientation%4 == 1) {
      newPoints.add(new PVector(control.x, control.y));
      newPoints.add(new PVector(control.x+1, control.y));
      newPoints.add(new PVector(control.x, control.y+1));
      newPoints.add(new PVector(control.x-1, control.y+1));
    }
    if (orientation%4 == 2) {
      newPoints.add(new PVector(control.x, control.y));
      newPoints.add(new PVector(control.x, control.y+1));
      newPoints.add(new PVector(control.x-1, control.y));
      newPoints.add(new PVector(control.x-1, control.y-1));
    }
    if (orientation%4 == 3) {
      newPoints.add(new PVector(control.x, control.y));
      newPoints.add(new PVector(control.x-1, control.y));
      newPoints.add(new PVector(control.x+1, control.y-1));
      newPoints.add(new PVector(control.x, control.y-1));
    }
  }

  if (type.equals("z-block")) {
    if (orientation%4 == 0) {
      newPoints.add(new PVector(control.x, control.y));
      newPoints.add(new PVector(control.x, control.y-1));
      newPoints.add(new PVector(control.x-1, control.y));
      newPoints.add(new PVector(control.x-1, control.y+1));
    }
    if (orientation%4 == 1) {
      newPoints.add(new PVector(control.x, control.y));
      newPoints.add(new PVector(control.x, control.y-1));
      newPoints.add(new PVector(control.x-1, control.y-1));
      newPoints.add(new PVector(control.x+1, control.y));
    }
    if (orientation%4 == 2) {
      newPoints.add(new PVector(control.x, control.y));
      newPoints.add(new PVector(control.x+1, control.y));
      newPoints.add(new PVector(control.x+1, control.y-1));
      newPoints.add(new PVector(control.x, control.y+1));
    }
    if (orientation%4 == 3) {
      newPoints.add(new PVector(control.x, control.y));
      newPoints.add(new PVector(control.x-1, control.y));
      newPoints.add(new PVector(control.x, control.y+1));
      newPoints.add(new PVector(control.x+1, control.y+1));
    }
  }
  return newPoints;
}

public void setupInfoForShape() {      //fills out the information about each type of block, and adds the types to the type list that is used to generate a sequence
//scores were chosen randomly, it doesn't really matter
  shapeInfoMap.put("o-block", new ShapeInfo(color(0xff990099), 5));
  shapeInfoMap.put("i-block", new ShapeInfo(color(0xff0000CC), 10));
  shapeInfoMap.put("j-block", new ShapeInfo(color(0xff00C8FF), 6));
  shapeInfoMap.put("l-block", new ShapeInfo(color(0xffFF6600), 8));
  shapeInfoMap.put("z-block", new ShapeInfo(color(0xffFF0000), 9));
  shapeInfoMap.put("s-block", new ShapeInfo(color(0xff00CC00), 7));
  shapeInfoMap.put("t-block", new ShapeInfo(color(0xffFFCC00), 11));

  typeList.add("o-block");
  typeList.add("i-block");
  typeList.add("j-block");
  typeList.add("l-block");
  typeList.add("z-block");
  typeList.add("s-block");
  typeList.add("t-block");
}

public ArrayDeque<String> generateSequence() {
  ArrayList<String> temp = new ArrayList<String>();
  for (String s : typeList) {  //make a separate copy of the typelist to be acted on
    temp.add(s);
  }

  ArrayDeque <String> newSequence = new ArrayDeque<String>(); 

  while (!temp.isEmpty()) {    //while the copy is not empty, pick a random index within the arraylist and add that String to the queue. This is what gives the 'drawn from a bag' effect
    int index = (int) random(0, temp.size()-1);
    String s = temp.remove(index);
    newSequence.offer(s);
  }
  return newSequence;
}

public boolean checkGameOver() {  //checks the top two rows of the grid to see if they have any blocks (not current shapes) in them. If they do, the game is over.
  for (int col = 0; col < grid.length; col++) {
    if (grid[col][1] != null || grid[col][0] != null) {
      return true;
    }
  }
  return false;
}


public void checkRowClear() {
  int row = grid[0].length-1;
  while (row > 0) {    //iterates through each row, starting from the bottom and going up
    int nullInRow = 0;
    for (int n = 0; n < grid.length; n++) {
      if (grid[n][row] == null) {
        nullInRow++;    //if there is a null in the row, nullInRow is increased
        row--;
        break;
      }
    }
    if (nullInRow == 0) {    //if there are no nulls in that row, the scores of each block will be added to the score, and the row will be cleared. The row will then be moved down
      for (int n = 0; n < grid.length; n++) {
        score += grid[n][row].scoreValue;
        grid[n][row] = null;
      }
      moveAllRowsDown(row-1);
      row--;
    }
  }
}

public void moveAllRowsDown(int rowUp) {
  //start at bottom, move each item to position below it. repeat for each row. Is called whenever checkRowClear is successful, so no gaps
  for (int row = rowUp; row > 0; row--) {
    for (int col = 0; col < grid.length; col++) {
      grid[col][row+1] = grid[col][row];
      grid[col][row] = null;
    }
  }
}

public void increaseLevel() {
  if (100*levelCount <= score) {    //simple function relating the levelCount to the score
    levelCount++;
  }
}

public int getDec(){      //Gets the decrement of the timer (I.e. how fast you want it to go up to/after certain levels
  if(levelCount < 7){    //if the level is less than seven, the level will be used as a modifier. Otherwise, 7 will be used as a modifier
    return levelCount;
  }
  return 7;
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "ASSIGNMENT_5" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
