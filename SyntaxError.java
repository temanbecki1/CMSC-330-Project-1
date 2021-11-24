/*
*   File: SyntaxError.java
*   Author: Teman Beck
*   Date:   November 21st, 2021
*   Purpose: This class creates a template for a generic handling of Syntax erors, displaying the line in which the error occured 
*
*/

class SyntaxError extends Exception {
    private static final long serialVersionUID = 1L;
        
    public SyntaxError(int line, String description) {
            super("Line: " + line + " " + description);
    }
}