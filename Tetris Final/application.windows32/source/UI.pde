/** 
 CONTAINS METHODS AND VARIABLES FOR THE UI OF THE GAME
 */

int blockSize = 27;
int leftEdge = 100;
int leftLine = 70;
int rightLine = 400;

void drawUI() {
  background(#020E12);      //background
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

void drawGrid() {      //draws the game grid 
  for (int n = 0; n < grid.length; n++) {  //width
    for (int i = 0; i < grid[0].length; i++) {  //height
      noFill();
      stroke(255);
      strokeWeight(1);
      rect(n*blockSize + blockSize/2 + leftEdge, i*blockSize + blockSize/2, blockSize, blockSize);
    }
  }
}

void fillGrid() {    //fills the game grid with it's objects
  for (int n = 0; n < grid.length; n++) {
    for (int i = 0; i < grid[0].length; i++) {
      if (grid[n][i] != null) {
        fill(grid[n][i].blockColour);
        rect(n*blockSize + blockSize/2 + leftEdge, i*blockSize + blockSize/2, blockSize, blockSize);
      }
    }
  }
}

void drawNext() {
  Shape next = new Shape(sequence.peek());    //returns but not removes the next shape
  next.controlPoint = new PVector(14, 9);    //approximate position so that the shape fits in the next block of the UI
  next.shapeVectors = updatePoints(next.shapeName, 0, next.controlPoint);    //generates points based on this position
  stroke(255);
  strokeWeight(1);
  next.drawShape();    //draws the next shape
}

void drawOver() {      //if the game over is reached, a popup will be drawn in the middle of the screen
  fill(255);
  rect(235, 200, 250, 50, 10);
  fill(0);
  text("Game Over", 170, 210);
}

void drawPause() {      //draws the pause button based on the state of the pause condition and the position of the mouse
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

void drawRestart() {
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