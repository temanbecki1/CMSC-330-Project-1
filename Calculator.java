import java.io.IOException;

import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;


/*
*   File: Calculator.java
*   Author: Teman Beck
*   Date:   November 21st, 2021
*   Purpose: This class builds our Calculator gui if the input syntax is correct. Checks input based off the BNF format required 
*
*/


public class Calculator {

    Token token;                                                            //declares a token object
    Token lexTokenLevel;                                                    //declares variable to track token level for GUI construction
    LexicalAnalysis lexicalAnalysis;                                        //Lexical analyzer for input file

    String lexGUIString;

    JFrame lexWindowFrame;
    JPanel lpanel;
    ButtonGroup lradioGroup;
    JFileChooser fileChooser;
    JRadioButton lradioButton;

    public Calculator() {

        try {

            fileChooser = new JFileChooser(".");                                           //creates new Filechooser for selection of parsable input

            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                lexicalAnalysis = new LexicalAnalysis(fileChooser.getSelectedFile().toString());
            }

            token = lexicalAnalysis.getNextToken();

            this.parseGUI();

        
        }   catch (SyntaxError | IOException e) {

                e.printStackTrace();

        } 
    }

    public static void main(String[] args) {                                                //main method to create new Calculator
        new Calculator();
    }

    public boolean parseGUI() throws SyntaxError, IOException {                             //this boolean checks for correct GUI syntax per BNF gui design
        int width;
        int height;     

        if(token == Token.WINDOW) {                                                         //checks for token type
            lexTokenLevel = Token.WINDOW;                                                   //assigns lexTokenLevel to Token.Window for next check
            lexWindowFrame = new JFrame();                                                  //creates new JFrame object
            lexWindowFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                  //closes the JFrame properly upon exit

            token = lexicalAnalysis.getNextToken();                                         //points token to next Token node

            if(token == Token.STRING) {                                                     //checks for token type of String
                lexWindowFrame.setTitle(lexicalAnalysis.getLexeme());                       //assigns window frame title 
                token = lexicalAnalysis.getNextToken();                                     //points token to next Token node
                
                if(token == Token.LEFT_PAREN) {                                             //checks for token type Left parenthesis
                    token = lexicalAnalysis.getNextToken();                                 //points to next Token node
                    
                    if(token == Token.NUMBER) {                                             //checks for numbers
                        width = (int) lexicalAnalysis.getValue();                           //assigns int value to width
                        token = lexicalAnalysis.getNextToken();                             //points to next Token node

                        if(token == Token.COMMA) {                                          //checks for token type of comma
                            token = lexicalAnalysis.getNextToken();                         //points to next token node
                            
                            if(token == Token.NUMBER) {                                     //checks for token type number
                                height = (int) lexicalAnalysis.getValue();                  //assigns int height from current value
                                token = lexicalAnalysis.getNextToken();                     //points to next Token node
                                
                                if(token == Token.RIGHT_PAREN) {                            //checks for right parenthesis
                                    lexWindowFrame.setSize(width, height);                  //sets windowFrame from width and height token values
                                    token = lexicalAnalysis.getNextToken();                 //points to next Token node

                                    if(this.getLayout()) {                                  //checks for layout 
                                        
                                        if(this.getWidgets()) {                             //checks for widget
                                        
                                            if(token == Token.END) {                        //checks for end of file
                                                token = lexicalAnalysis.getNextToken();     //points to next token node
                                                        
                                                if(token == Token.PERIOD) {                 //checks for ending period
                                                    lexWindowFrame.setVisible(true);        //if everything is correct, sets the frame visibility to true
                                                    
                                                    return true;                            //returns true
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        return false;
    }

    private boolean getLayout() throws SyntaxError, IOException {
        if(token == Token.LAYOUT) {
            token = lexicalAnalysis.getNextToken();
            
            if(this.getLayoutType()) {
                
                if(token == Token.COLON) {
                    token = lexicalAnalysis.getNextToken();
                    
                    return true;
                }
            }
        }
    
        return false;
    }

    private boolean getLayoutType() throws SyntaxError, IOException {
        int rows;
        int columns;
        int columnGap;
        int rowGap;
        
        if(token == Token.FLOW) {
            
            if(lexTokenLevel == Token.WINDOW) {
                lexWindowFrame.setLayout(new FlowLayout());
            } else {
                lpanel.setLayout(new FlowLayout());
            }

            token = lexicalAnalysis.getNextToken();
            
            return true;

        } else if(token == Token.GRID) {
            token = lexicalAnalysis.getNextToken();
            
            if(token == Token.LEFT_PAREN) {
                token = lexicalAnalysis.getNextToken();
                
                if(token == Token.NUMBER) {
                    rows = (int) lexicalAnalysis.getValue();
                    token = lexicalAnalysis.getNextToken();
                    
                    if(token == Token.COMMA) {
                        token = lexicalAnalysis.getNextToken();

                        if(token == Token.NUMBER) {
                            columns = (int) lexicalAnalysis.getValue();;
                            token = lexicalAnalysis.getNextToken();
                            
                            if(token == Token.RIGHT_PAREN) {
                                
                                if(lexTokenLevel == Token.WINDOW) {
                                    lexWindowFrame.setLayout(new GridLayout(rows, columns));
                                } else {
                                    lpanel.setLayout(new GridLayout(rows, columns));
                                }
                                
                                token = lexicalAnalysis.getNextToken();
                                
                                return true;
                            } else if(token == Token.COMMA) {
                                token = lexicalAnalysis.getNextToken();
                                
                                if(token == Token.NUMBER) {
                                    columnGap = (int) lexicalAnalysis.getValue();
                                    token = lexicalAnalysis.getNextToken();
                                    
                                    if(token == Token.COMMA) {
                                        token = lexicalAnalysis.getNextToken();
                                        
                                        if(token == Token.NUMBER) {
                                            rowGap = (int) lexicalAnalysis.getValue();
                                            token = lexicalAnalysis.getNextToken();
                                            
                                            if(token == Token.RIGHT_PAREN) {
                                                
                                                if(lexTokenLevel == Token.WINDOW) {
                                                    lexWindowFrame.setLayout(new GridLayout(rows, columns, columnGap, rowGap));
                                                } else {
                                                    lpanel.setLayout(new GridLayout(rows, columns, columnGap, rowGap));
                                                }
                                                token = lexicalAnalysis.getNextToken();
                                                
                                                return true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
        return false;
    }

    private boolean getWidgets() throws SyntaxError, IOException {
        if(this.getWidget()) {
            
            if(this.getWidgets()) {
                
                return true;
            }
        
            return true;
        }
        
        return false;
    }
    
    private boolean getWidget() throws SyntaxError, IOException {
        int length;
        
        if(token == Token.BUTTON) {
            token = lexicalAnalysis.getNextToken();
            
            if(token == Token.STRING) {
                lexGUIString = lexicalAnalysis.getLexeme();
                token = lexicalAnalysis.getNextToken();
                
                if(token == Token.SEMICOLON) {
                    
                    if(lexTokenLevel == Token.WINDOW) {
                        lexWindowFrame.add(new JButton(lexGUIString));
                    } else {
                        lpanel.add(new JButton(lexGUIString));
                    }
                    
                    token = lexicalAnalysis.getNextToken();
                    
                    return true;
                }
            }
        } else if(token == Token.GROUP) {
            lradioGroup = new ButtonGroup();
            token = lexicalAnalysis.getNextToken();
            
            if(getRadioButtons()) {

                if(token == Token.END) {
                    token = lexicalAnalysis.getNextToken();

                    if(token == Token.SEMICOLON) {
                        token = lexicalAnalysis.getNextToken();

                        return true;
                    }
                }
            }
        } else if(token == Token.LABEL) {
            token = lexicalAnalysis.getNextToken();

            if(token == Token.STRING) {
                lexGUIString = lexicalAnalysis.getLexeme();
                token = lexicalAnalysis.getNextToken();

                if(token == Token.SEMICOLON) {

                    if(lexTokenLevel == Token.WINDOW) {
                        lexWindowFrame.add(new JLabel(lexGUIString));
                    } else {
                        lpanel.add(new JLabel(lexGUIString));
                    }

                    token = lexicalAnalysis.getNextToken();
                    
                    return true;
                }
            }
        } else if(token == Token.PANEL) {
            
            if(lexTokenLevel == Token.WINDOW) {
                lexWindowFrame.add(lpanel = new JPanel());
            } else {
                lpanel.add(lpanel = new JPanel());
            }
            
            lexTokenLevel = Token.PANEL;
            token = lexicalAnalysis.getNextToken();

            if(getLayout()) {

                if(getWidgets()) {

                    if(token == Token.END) {
                        token = lexicalAnalysis.getNextToken();

                        if(token == Token.SEMICOLON) {
                            token = lexicalAnalysis.getNextToken();
                            
                            return true;
                        }
                    }
                }
            }
        } else if(token == Token.TEXTFIELD) {
            token = lexicalAnalysis.getNextToken();

            if(token == Token.NUMBER) {
                length = (int) lexicalAnalysis.getValue();
                token = lexicalAnalysis.getNextToken();

                if(token == Token.SEMICOLON) {
    
                    if(lexTokenLevel == Token.WINDOW) {
                        lexWindowFrame.add(new JTextField(length));
                    } else {
                        lpanel.add(new JTextField(length));
                    }

                    token = lexicalAnalysis.getNextToken();

                    return true;
                }
            }
        }

        return false;
    }

    private boolean getRadioButtons() throws SyntaxError, IOException {
        if(getRadioButton()) {
            
            if(getRadioButtons()) {
    
                return true;
            }

            return true;
        }
        
        return false;
    }

    private boolean getRadioButton() throws SyntaxError, IOException {
        if(token == Token.RADIO) {
            token = lexicalAnalysis.getNextToken();

            if(token == Token.STRING) {
                lexGUIString = lexicalAnalysis.getLexeme();
                token = lexicalAnalysis.getNextToken();

                if(token == Token.SEMICOLON) {
                    lradioButton = new JRadioButton(lexGUIString);
                    lradioGroup.add(lradioButton);

                    if(lexTokenLevel == Token.WINDOW) {
                        lexWindowFrame.add(lradioButton);
                    } else {
                        lpanel.add(lradioButton);
                    }

                    token = lexicalAnalysis.getNextToken();

                    return true;
                }
            }
        }
        
        return false;
    }
}