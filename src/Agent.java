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

	// Class instance variables
	State globalState;
	Explore explorer;
	GoalGenerator goalGenerator;
	
	ArrayList<Character> pathToExecute;
	
	int stage;
	
	Point currentGoal;

	public Agent() {
	
		// Initialize class instances of Map and State variables
		globalState = new State();
		explorer = new Explore(globalState);
		
		pathToExecute = new ArrayList<Character>();
		
		// First stage of AI will be exploring the surrounding environment 
		stage = STAGE_1_EXPLORE;
	}


	public char get_action(char view[][]) {
		
		// Wait for testing purposes
		try {
			Thread.sleep(50);
		} catch (Exception e) {
			
		}
		
		System.out.println("\n==> Here is the already explored map:");
        this.globalState.map.updateMap(view, globalState);
        this.globalState.map.printMap(this.globalState.c, this.globalState.direction);
        this.globalState.printState();
        
        
		
		// 1. Explorer everywhere we can FIRST!
		if (stage == STAGE_1_EXPLORE) {
			
			char moveToMake = explorer.run();
			
			// If we have the gold, go straight to STAGE_4_RETURN
	        if (globalState.gold == true) {
	        	stage = STAGE_4_RETURN;
	        } else if (explorer.stillExploring() == false) {
	        	stage += 1;	//TODO
	        }
			
			return moveToMake;
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
				
				if (pathToExecute.size() == 0) {
					stage--;
				}
				
				return moveToMake;
				
			} else {
				//Path pathFinder = new Path(new State(globalState), globalState.map.findItem(Enums.Symbol.GOLD).get(0));
//				Path pathFinder = new Path(new State(globalState), new Point(80, 80, Enums.Symbol.EMPTY));
				
				Path pathFinder = new Path(new State(globalState), currentGoal);
				
				System.out.println("\n\n\n ABOUT TO PATH TO SOMEWHERE: \n");
				try {
					Thread.sleep(1000);
				} catch (Exception e) {
					
				}
				ArrayList<Character> moves = pathFinder.movesToPoint();
				
				pathToExecute.addAll(moves);
				
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
