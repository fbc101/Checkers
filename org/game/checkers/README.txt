===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. 2D array - Since the checkers board consists of rows and columns, it 
  was easier to represent the cells of the board and the pieces that this
  contained. At first, I was going to implement a String 2D array, but then 
  I changed it to a 2D int array since I think ints are easier to compare. 
  The 2D array represents the checkers board, if a cell is zero, it is empty,
  if it is 1 or 2, then it is a soldier, and 7 or 8 a king. Using a 2D array 
  made it easier to visualize where the pieces are, and how they can
  interact around the rest of the board. 

  2. JUnit testing - checkers has many rules and scenarios. In order to make 
  sure that my game would run correctly and not stop during the middle of 
  the game, I had to make sure that the pieces were interacting correctly 
  with one another. JUnit testing allowed me to make sure that I could 
  change methods, while still functioning as they should. One of the features 
  this helped me most was on the consecutive attack part of the game because it
  allowed me to see that a player's turn did not change even after attacking 
  once, etc. There were many other scenarios that JUnit testing allowed me 
  to explore and make me feel confident that my game would not crash or 
  stop in such cases. I tested when soldiers should be able 
  to move, attack, and when the game should end, etc.
  
  3. File I/O - this implements the save / load aspect of the game. The File I/O
  allowed me to store the game state in a file. This means that even when the 
  game is not played, the game state is saved in a file for future access. 
  For the load part, I would need to read from a file with some saved 
  game state, which is why File I/O makes sense for this feature.

  4. Collections / Maps - At first I was going to use a Set<Soldier> to 
  represent soldiers and to remove them "easily" but I was told that 
  this implemented what 2D arrays was doing. So instead I decided 
  to use Maps as a history of moves. This made sense because there cannot 
  be repeated keys in a Map, and also keys can be removed/replaced. 
  This means there cannot be two of the same game state stored, 
  since something has to change such as a move or attack. If I 
  wanted to undo a movement, I could just simply remove it
  from the Map and use the previous key holding the previous game state.
  
=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.
  
  Checkers - holds the logic/rules of the game. It also contains the history of the moves
  made in the game. It has the methods necessary for saving, loading and restarting a game.
  This is because Checkers holds all the essential fields. At first, it reads off the 
  game state file to hold the values in the fields, and then these fields are used to save
  them into the file later on.
  
  GameBoard - this class draws and represents how the checkers class should look to an 
  user. It implements the interactive part of the game. This class allows players to
  click on a soldier, and perform an action which is translated to the checkers class. This is
  basically the controller-visual component of the game.
  
  Cell (Inner Class within GameBoard) - It draws how a cell should look like, and stores a cell's relevant
  information such as row, column and what soldier it holds. The class also contains a static currSoldier
  int array, which basically tells all the other cells what cell is currently selected and how a cell 
  should respond if it is clicked based on that information. This is the class that actually takes in
  the user's input (click) and translates it to the model within Checkers object. It uses the methods
  provided by the Checkers class to perform specific actions.
  
  RunCheckers - it is responsible for how the buttons, the board and the status look on the screen.
  It allows the game to run.
  
  FileEditorCheckers - it is in charge of extracting information from the game state file
  as well as writing new information to it when required.
  
  GameTest - it contains several test cases that ensure that the Checker's rules/logic are followed
  correctly.

- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?
  
  Yes, my biggest problem was how to connect the game model and the user-interactive part. At first, 
  I thought about using buttons to represent soldiers since you could click on buttons and these
  have a response, but then I thought about that a cell in the board was more like a canvas
  because there is something drawn on it and you can also click on it. This led to me creating
  the inner class Cell in GameBoard. GameBoard had access to the cells in the Checkers 2D array, so
  I used that to feed it to each Cell instance.
  
  
- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?
  
  Most of my methods are public because I would have no other way to test them if they were private.
  But there is a good separation of functionality. Checkers class is in charge of the actual game logic,
  so it could be played even without the controller-visual part. The Checkers class depends on 
  FileEditorCheckers to load and save game states. GameBoard depends on what the checkers class provides
  in order to draw what the Checkers class represents. RunCheckers depends on what the GameBoard 
  provides to make a window on the computer to display everything. The 2D array representing the board
  in the Checkers class is well encapsulated because it cannot be accessed from outside nor edited. 
  There is a method in Checkers called setCell(int row, int col, int val) which allows to assign a value
  to a cell in the 2D array. In theory, this should be private, but it is not because I needed it to be
  public in order to test certain things. But if it was not private, then the only ways to change things
  inside the board would be through the action methods move() and attack(). 
	
========================
=: External Resources :=
========================

- Cite any external resources (images, tutorials, etc.) that you may have used 
  while implementing your game.
  
  "https://docs.oracle.com/javase/7/docs/api/overview-summary.html"
  "https://www.javatpoint.com/java-joptionpane"
