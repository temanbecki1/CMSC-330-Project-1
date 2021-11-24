// import javax.swing.*;
import java.io.IOException;
import java.io.*;

/*
*   File: LexicalAnalysis.java
*   Author: Teman Beck
*   Date:   November 21st, 2021
*   Purpose: This classes tokenizes our file input and assigns the type of the token as needed.
*
*/


//Define class LexicalAnalysis
class LexicalAnalysis {
    private static final int keyWords = 11;
    private StreamTokenizer tokenizer;
    private String lpunctuation = ",:;.()";

    
    private Token[] punctuationTokens = {Token.COMMA, Token.COLON, Token.SEMICOLON, Token.PERIOD, Token.LEFT_PAREN, Token.RIGHT_PAREN };    //Define punctuation Tokens

    //Define constructor
    public LexicalAnalysis(String fileName) throws FileNotFoundException {                          //constructor
        tokenizer = new StreamTokenizer(new FileReader(fileName));                                  //assigns input file to tokenizer
        tokenizer.ordinaryChar('.');                                                                
        tokenizer.quoteChar('"');                                                                   //tells tokenizer matching quotes "" define a quoted string
        
    }

    public Token getNextToken() throws SyntaxError, IOException {
        int ltoken = tokenizer.nextToken();                                                         //checks value of next token
    
        switch (ltoken) {                                                                           //switch statement to branch correctly based on nexttoken data
            case StreamTokenizer.TT_NUMBER:
                return Token.NUMBER;                                                                //returns Number type for token
            case StreamTokenizer.TT_WORD:
                for (Token laToken : Token.values()) {
                    if (laToken.ordinal() == keyWords)
                        break;

                    if (laToken.name().equals(tokenizer.sval.toUpperCase()))
                        return laToken;
                    }
                    
                    throw new SyntaxError(lineNo(), "Invalid ltoken " + getLexeme());
            case StreamTokenizer.TT_EOF:
                return Token.EOF;
            case '"':
                return Token.STRING;

                default:

                for (int li = 0; li < lpunctuation.length(); li++)
                    if (ltoken == lpunctuation.charAt(li))
                        return punctuationTokens[li];
        }
        
        return Token.EOF;
    }

    public String getLexeme() {
        return tokenizer.sval;
    }

    public double getValue() {
        return tokenizer.nval;
    }

    public int lineNo() {
        return tokenizer.lineno();
    }
}
