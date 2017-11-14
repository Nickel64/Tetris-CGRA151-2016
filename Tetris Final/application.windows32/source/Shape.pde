class Shape {  //used for the currently selected shape in the game
  String shapeName;
  HashSet<PVector> shapeVectors;    //has a set of pvectors based on the current control point
  color c;                          //has a set color and score value, and a varying orientation
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

  void moveShape(String direction) {    
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

  boolean checkCollide() {    //this only checks downward collisions. Returns true if there is a collision, returns false otherwise
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
  
  boolean checkSides(String direction){  //checks side collisions, same logic as previous checks
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
  
  void rotate(){
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

  void drawShape() {      //draws each part of the shape, based on its position. This is how it can be used to draw the next shape, which is outside the grid
    //drawGhost();
    for (PVector p : shapeVectors) {
      fill(c);
      rect(p.x*blockSize + blockSize/2 + leftEdge, p.y*blockSize + blockSize/2, blockSize, blockSize);
    }
  }

  void setBlocks() {
    for (PVector p : shapeVectors) {
      grid[(int)p.x][(int)p.y] = new Block(c, scoreVal);    //sets the blocks in the shape to their corresponding position on the grid
    }
  }
}