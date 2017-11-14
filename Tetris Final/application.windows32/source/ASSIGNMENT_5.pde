import java.util.*;  //allows the use of maps, sets etc

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

void settings() {
  size(600, 600);    //set here so that processing doesn't crash, for some reason
}

void setup() {
  background(#020E12);
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

void draw() {
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

void keyPressed() {
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

void mousePressed(){
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