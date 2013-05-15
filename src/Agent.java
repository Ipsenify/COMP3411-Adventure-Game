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

	// Class instance variables
	Map map;
	State state;

	public Agent() {
	
		// Initialize class instances of Map and State variables
		map = new Map();
		state = new State();
	}


	public char get_action(char view[][]) {

        // REPLACE THIS CODE WITH AI TO CHOOSE ACTION

        int ch=0;

        System.out.print("Enter Action(s): ");
        
        System.out.println("\n==> Here is the already explored map:");
        map.updateMap(view, state);

        try {
            while ( ch != -1 ) {
                // read character from keyboard
                ch  = System.in.read();

                switch( ch ) { 
                	
                	// FORWARD
                	case 'F': 
                	case 'f':
                			
                		state.moveForward();
                		
                		System.out.println("--> Here is the UPDATED map:");
                		map.updateMap(view, state);
                		return ((char) ch);
                
                	// LEFT
                	case 'L':
                	case 'l':
                		
                		state.turnLeft();
                		
                		System.out.println("--> Here is the UPDATED map:");
                		map.updateMap(view, state);
                		return ((char) ch);
                
                	// RIGHT
                	case 'R':
                	case 'r':
                		
                		state.turnRight();
                		
                		System.out.println("--> Here is the UPDATED map:");
                		map.updateMap(view, state);
                		return ((char) ch);
                		
                	// CHOP
                	case 'C':
                	case 'c':
                		
                		if (state.axe) {
                		
                		} else {
                			System.out.println("Sorry, you don't have an AXE");
                		}
                		return ((char) ch);
                
                	// OPEN
                	case 'O':
                	case 'o':
                		
                		if (state.key) {
                		
                		} else {
                			System.out.println("Sorry, you don't have a KEY");
                		}
                		return ((char) ch);
                
                	// BOMB
                    case 'B':
                	case 'b':
                		
                		if (state.bombs > 0) {
                		
                		} else {
                			System.out.println("Sorry, you don't have any BOMBS");
                		}
                		return ((char) ch);
                      
                    // PRINT 
                    case 'P':
                    	map.printMap();	
                }
            }
        }
        catch (IOException e) {
            System.out.println ("IO error:" + e );
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
      
	  try { // scan 5-by-5 wintow around current location
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
            agent.print_view(view); 						// COMMENT THIS OUT BEFORE SUBMISSION
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
