/*********************************************
/*  Agent.java 
/*  Sample Agent for Text-Based Adventure Game
/*  COMP3411 Artificial Intelligence
/*  UNSW Session 1, 2013
*/

import java.util.*;
import java.io.*;
import java.net.*;

public class Agent {
	
	// Class constants
	public static final int STAGE_1_EXPLORE = 0;
	public static final int STAGE_2_SETGOAL = 1;	
	public static final int STAGE_3_PATH 	= 2;
	public static final int STAGE_4_RETURN 	= 3;
	

	public static final int ITEM_MOVE_LIMIT = 10;

	// Class instance variables
	State globalState;
	Explore explorer;
	GoalGenerator goalGenerator;
	
	ArrayList<Character> pathToExecute;
	
	int stage;
	
	ArrayList<Point> itemsUnreachable;
	
	ArrayList<Point> nearbyItems;
	Point currentGoal;
	boolean gettingNeabyItem;
	boolean continueExploring;

	public Agent() {
		
		itemsUnreachable = new ArrayList<Point>();
		continueExploring = false;
	
		// Initialize class instances of Map and State variables
		globalState = new State();
		explorer = new Explore(globalState);
		
		pathToExecute = new ArrayList<Character>();
		gettingNeabyItem = false;
		// First stage of AI will be exploring the surrounding environment 
		stage = STAGE_1_EXPLORE;
	}


	public char get_action(char view[][]) {
		
		// Wait for testing purposes
		try {
			Thread.sleep(25);
		} catch (Exception e) {
			
		}
		
		System.out.println("\n==> Here is the already explored map:");
        this.globalState.map.updateMap(view, globalState);
        this.globalState.map.printMap(this.globalState.c, this.globalState.direction);
        this.globalState.printState();
        
        
		
		// 1. Explorer everywhere we can FIRST!
		if (stage == STAGE_1_EXPLORE) {
			
			// If there is a pickup nearby DO IT!
			nearbyItems = this.globalState.map.findNearItems(this.globalState.c);
			
			// TODO
			if (nearbyItems.size() > 0 && listContains(itemsUnreachable, nearbyItems.get(0)) == false) {
				Db.p("FOUND A NEARBY ITEM, GOING THERE NOW!!!!", 1000);
				
				currentGoal = nearbyItems.remove(0);
				// If the item is unreachable continue exploring
				if (listContains(itemsUnreachable, currentGoal) == false) {
						gettingNeabyItem = true;
						stage = STAGE_3_PATH;
						explorer.followingWall = false;
				} else {
					char moveToMake = explorer.run();
					return moveToMake;
				}
				
			} else {
				if (continueExploring == true) {
					continueExploring = false;
				}
				Db.p("EXPLORING", 0);
				char moveToMake = explorer.run();
				
				// If we have the gold, go straight to STAGE_4_RETURN
		        if (globalState.gold == true) {
		        	stage = STAGE_4_RETURN;
		        } else if (explorer.stillExploring() == false) {
		        	stage = STAGE_2_SETGOAL;
		        }
		        return moveToMake;
			}
			
		} 
		
		// 2. Set the intermediary goals we need to obtain the gold
		else if (stage == STAGE_2_SETGOAL) {
			
			if(globalState.gold == false) {
				currentGoal = this.globalState.map.findItem(Enums.Symbol.GOLD).get(0);
				
			} else {
				currentGoal = new Point(80, 80, Enums.Symbol.EMPTY);
			}
			
			stage++;
			
			//State pathState = new State(globalState);
			//pathState.giveItemsOnMap();
			//pathState.printState();
			
			//goalGenerator = new GoalGenerator(pathState);
			
			/*goalGenerator = new GoalGenerator(new State(globalState));
			
			// Generate an ArrayList of Points that are required to be reach
			// in order to get the gold
			goalGenerator.run();
			
			// TESTING A* WORKS
			ArrayList<Point> goalList = goalGenerator.getGoalList();
			System.out.println("\n\n\nGENERATED GOAL LIST (size = "+goalList.size()+": \n");
			int count = 1;
			for (Point p : goalList) {
				System.out.println("goal "+count+" = " + p.symbol);
				count++;
			}*/
		}
		
		// 3. Take the first goal and path to it
		// 	  Repeat until all goals have been made
		else if (stage == STAGE_3_PATH) {
			
			System.out.println("PATH TO EXECUTE SIZE = "+pathToExecute.size());
			
			// TESTING
			if (pathToExecute.size() > 0) {
				
				char moveToMake = pathToExecute.remove(0);
				
				System.out.println("moveToMake: " + moveToMake);
				
				globalState.makeMove(moveToMake);
				
				System.out.print("pathToExecute: ");
				for (char c : pathToExecute) {
					System.out.print(c + ", ");
				}
				
				if (pathToExecute.size() == 0 && gettingNeabyItem == true) {
					stage = STAGE_1_EXPLORE;
					gettingNeabyItem = false;
					this.explorer.contactPoints.clear();
					this.explorer.addContactPoints = true;
				} else if (pathToExecute.size() == 0){
					stage = STAGE_2_SETGOAL;
				}
				
				return moveToMake;
				
			} else {
				//Path pathFinder = new Path(new State(globalState), globalState.map.findItem(Enums.Symbol.GOLD).get(0));
//				Path pathFinder = new Path(new State(globalState), new Point(80, 80, Enums.Symbol.EMPTY));
				
				// Reset the past cost before performing A*
				State copy = new State(globalState);
				copy.pastCost = 0;
				Path pathFinder = new Path(copy, currentGoal);
				
				if (gettingNeabyItem == true) {
					pathFinder.pastCostLimit = ITEM_MOVE_LIMIT;
				}
				
				System.out.println("\n\n\n ABOUT TO PATH TO SOMEWHERE: \n");
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					
				}
				ArrayList<Character> moves = pathFinder.movesToPoint();
				
				if (moves != null) {
					pathToExecute.addAll(moves);
				} else if (moves == null && gettingNeabyItem == true) {
					Db.p("Coudnt reach item", 1000);
					itemsUnreachable.add(currentGoal);
					stage = STAGE_1_EXPLORE;
					gettingNeabyItem = false;
					continueExploring = true;
					this.explorer.contactPoints.clear();
					this.explorer.addContactPoints = true;
				}
				
//				System.out.println("\n\n\n MOVES TO KEY: \n");
//				for (Character c : moves) {
//					System.out.println(c);
//				}
			}
		}
		
		// 4. Return the gold to [0,0]
		else if (stage == STAGE_4_RETURN) {
			currentGoal = new Point(80, 80, Enums.Symbol.EMPTY);
			stage--;
		}

        return 0;
   }

   void print_view( char view[][] )
   {
    int i,j;

    System.out.println("\n+ - - - - - +");
        for( i=0; i < 5; i++ ) {
            System.out.print("| ");
            for( j=0; j < 5; j++ ) {
                if(( i == 2 )&&( j == 2 )) {
                    System.out.print("^ ");
                }
                else {
                    System.out.print( view[i][j] + " ");
                }
            }
            System.out.println("|");
        }
        System.out.println("+ - - - - - +");
   }
   
   private boolean listContains(ArrayList<Point> list, Point p) {
	   for (Point z : list) {
		   if (p.x == z.x && p.y == z.y) {
			   return true;
		   }
	   }
	   return false;
   }

   public static void main( String[] args )
   {
      InputStream in  = null;
      OutputStream out= null;
      Socket socket   = null;
      Agent  agent    = new Agent();
      char   view[][] = new char[5][5];
      char   action   = 'F';
      int port;
      int ch;
      int i,j;

      if( args.length < 2 ) {
         System.out.println("Usage: java Agent -p <port>\n");
         System.exit(-1);
      }

      port = Integer.parseInt( args[1] );

      try { // open socket to Game Engine
         socket = new Socket( "localhost", port );
         in  = socket.getInputStream();
         out = socket.getOutputStream();
      }
      catch( IOException e ) {
         System.out.println("Could not bind to port: "+port);
         System.exit(-1);
      }
      
	  try { // scan 5-by-5 window around current location
         while( true ) {
            for( i=0; i < 5; i++ ) {
               for( j=0; j < 5; j++ ) {
                  if( !(( i == 2 )&&( j == 2 ))) {
                     ch = in.read();
                     if( ch == -1 ) {
                        System.exit(-1);
                     }
                     view[i][j] = (char) ch;
                  }
               }
            }
            //agent.print_view(view); 						// COMMENT THIS OUT BEFORE SUBMISSION
            action = agent.get_action(view);
            out.write( action );
         }
      }
      catch( IOException e ) {
         System.out.println("Lost connection to port: "+ port );
         System.exit(-1);
      }
      finally {
         try {
            socket.close();
         }
         catch( IOException e ) {}
      }
   }
   
   
}
