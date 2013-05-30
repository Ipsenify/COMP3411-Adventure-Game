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
	public static final int STAGE_3_PATH 	= 1;
	public static final int STAGE_4_RETURN 	= 3;

	// Class instance variables
	State globalState;
	Explore explorer;
	GoalGenerator goalGenerator;
	
	int stage;

	public Agent() {
	
		// Initialize class instances of Map and State variables
		globalState = new State();
		explorer = new Explore(globalState);
		
		// First stage of AI will be exploring the surrounding environment 
		stage = STAGE_1_EXPLORE;
	}


	public char get_action(char view[][]) {
		
		// Wait for testing purposes
		try {
			Thread.sleep(100);
		} catch (Exception e) {
			
		}
		
		System.out.println("\n==> Here is the already explored map:");
        this.globalState.map.updateMap(view, globalState);
        this.globalState.map.printMap();
        globalState.printState();
        
        
		
		// 1. Explorer everywhere we can FIRST!
		if (stage == STAGE_1_EXPLORE) {
			
			char moveToMake = explorer.run();
			
			// If we have the gold, go straight to STAGE_4_RETURN
	        if (globalState.gold == true) {
	        	stage = STAGE_4_RETURN;
	        } else if (explorer.stillExploring() == false) {
	        	stage++;
	        }
			
			return moveToMake;
		} 
		
		// 2. Set the intermediary goals we need to obtain the gold
		else if (stage == STAGE_2_SETGOAL) {
			
			State pathState = new State(globalState);
			pathState.giveItemsOnMap();
			pathState.printState();
			
			goalGenerator = new GoalGenerator(pathState);
			goalGenerator.run();
		}
		
		else if (stage == STAGE_3_PATH) {
			
			
		}
		
		// 4. Return the gold to [0,0]
		else if (stage == STAGE_4_RETURN) {
			
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
