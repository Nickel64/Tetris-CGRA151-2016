/**
  UTILITY METHODS
  THE BACKBONE OF THE PROGRAM
*/

HashSet<PVector> updatePoints(String type, int orientation, PVector control) {    //the beast itself. takes the type, orientation and a control point, and generates a set of points based on the combination of these.
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

void setupInfoForShape() {      //fills out the information about each type of block, and adds the types to the type list that is used to generate a sequence
//scores were chosen randomly, it doesn't really matter
  shapeInfoMap.put("o-block", new ShapeInfo(color(#990099), 5));
  shapeInfoMap.put("i-block", new ShapeInfo(color(#0000CC), 10));
  shapeInfoMap.put("j-block", new ShapeInfo(color(#00C8FF), 6));
  shapeInfoMap.put("l-block", new ShapeInfo(color(#FF6600), 8));
  shapeInfoMap.put("z-block", new ShapeInfo(color(#FF0000), 9));
  shapeInfoMap.put("s-block", new ShapeInfo(color(#00CC00), 7));
  shapeInfoMap.put("t-block", new ShapeInfo(color(#FFCC00), 11));

  typeList.add("o-block");
  typeList.add("i-block");
  typeList.add("j-block");
  typeList.add("l-block");
  typeList.add("z-block");
  typeList.add("s-block");
  typeList.add("t-block");
}

ArrayDeque<String> generateSequence() {
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

boolean checkGameOver() {  //checks the top two rows of the grid to see if they have any blocks (not current shapes) in them. If they do, the game is over.
  for (int col = 0; col < grid.length; col++) {
    if (grid[col][1] != null || grid[col][0] != null) {
      return true;
    }
  }
  return false;
}


void checkRowClear() {
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

void moveAllRowsDown(int rowUp) {
  //start at bottom, move each item to position below it. repeat for each row. Is called whenever checkRowClear is successful, so no gaps
  for (int row = rowUp; row > 0; row--) {
    for (int col = 0; col < grid.length; col++) {
      grid[col][row+1] = grid[col][row];
      grid[col][row] = null;
    }
  }
}

void increaseLevel() {
  if (100*levelCount <= score) {    //simple function relating the levelCount to the score
    levelCount++;
  }
}

int getDec(){      //Gets the decrement of the timer (I.e. how fast you want it to go up to/after certain levels
  if(levelCount < 7){    //if the level is less than seven, the level will be used as a modifier. Otherwise, 7 will be used as a modifier
    return levelCount;
  }
  return 7;
}