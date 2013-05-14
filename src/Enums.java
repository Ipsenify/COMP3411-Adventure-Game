/*********************************************
/*  Enums.java
/*  
/*  COMP3411 Artificial Intelligence
/*  UNSW Session 1, 2013
*/

public class Enums {
    
    public enum Direction {
        NORTH, SOUTH, EAST, WEST
    }

    public enum Symbol {
    	AGENT {
    		public String toString () {
    			return "@";
    		}
    	},
        AXE {
            public String toString () {
                return "a";
            }
        },
        DYNAMITE {
            public String toString () {
                return "d";
            }
        },
        DOOR {
            public String toString () {
                return "-";
            }
        },
        EMPTY {
            public String toString () {
                return " ";
            }
        },
        KEY {
            public String toString () {
                return "k";
            }
        },
        TREE {
            public String toString () {
                return "T";
            }
        },
        UNKNOWN {
            public String toString () {
                return "X";
            }
        },
        WALL {
            public String toString () {
                return "*";
            }
        },
        WATER {
            public String toString () {
                return "~";
            }
        }
    }
    
    public static Symbol charToEnum (char c) {
    
    	switch (c) {	
    		case '@':
    			return Symbol.AGENT;
    		
    		case 'a':
    			return Symbol.AXE;
   		
    		case 'd':
    			return Symbol.DYNAMITE;
    		
    		case '-':
    			return Symbol.DOOR;
    		
    		case ' ':
    			return Symbol.EMPTY;
    		
    		case 'k':
    			return Symbol.KEY;
    		
    		case 'T':
    			return Symbol.TREE;
    		
    		case 'X':
    			return Symbol.UNKNOWN;
    		
    		case '*':
    			return Symbol.WALL;
    		
    		case '~':
    			return Symbol.WATER;
    	}
    	
    	// TODO Make a separate Symbol enum for 'error'
    	return Symbol.UNKNOWN;
    }
    
}