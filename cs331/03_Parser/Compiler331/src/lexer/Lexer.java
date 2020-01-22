//Jonayed Ahmed
package lexer;

import java.util.HashMap;

//Isolate tokens from the file text
//Designed to recognize tokens in 'Vascal', a subset of Pascal
//Accessed by the Parser

public class Lexer {
    
    private String FileText = "";
    private int FileSize = 0;
    
    private final String NullValue = "None";
    
    //global variable representing current char position
    //used to iterate through the file
    private int Ite = 0;
    
    private static final String VALID_CHARS =
    "ABCDEFHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890" +
    ".,;:<>/*[]+-=()}{";
    
    //all the key words in Vascal
    private static final String[] KEY_WORDS = 
    
    {"program", "begin", "end", "var", "function", "procedure", "result",
      "integer", "real", "array", "of", "not", "if", "then", "else",
      "while", "do", "div", "mod", "and", "or"};
    
    //initialize the HashMap key to 1
    private int TokenMapKey = 1;
    
    //hash map of all Tokens
    //used for UNARYPLUS/MINUS and Parser 
    private final HashMap<Integer, Token> TokenMap = new HashMap<>();
    
    
    public Lexer(String fileText)
    {
        this.FileText = fileText;
        this.FileSize = fileText.length();  
    }
    
    //returns hashmap containing all tokens in input file 
    public HashMap<Integer, Token> getTokenMap()
    {
        return TokenMap;
    }
    
    //Go through the file text and add the Tokens to the HashMap TokenMap
    public void goThroughFile(int startPos, int tokenSize)
    {
        //go through each character in fileText
        for (Ite = startPos; Ite < FileSize - 1; Ite += tokenSize)
        {
            char currChar = FileText.charAt(Ite); 
            
            if ((VALID_CHARS.indexOf(currChar) < 0) && (currChar != ' ')
               && (currChar != '\n') && (currChar != '\t')) //invalid char
            {
                System.out.println();
                System.out.println(currChar + " is not a valid character!");
                System.exit(0);
            }  
            //currChar is { then there is a comment we ignore
            else if (currChar == '{')
                tokenSize = findCommentSize();
            
            //' ', '\n', '\t' then there is whitespace we ignore
            else if ((currChar == ' ') ||
                    (currChar == '\n') ||
                    (currChar == '\t'))
                tokenSize = findWhiteSpaceSize();
                 
            else //currChar is valid and is Token
            {
               //get the token 
               Token currToken = GetNextToken();
               //print the token
               currToken.PrintToken();
               //update hashmap with new token and increment key number
               TokenMap.put(TokenMapKey, currToken);
               TokenMapKey++;
               
               //offset to next character position(ite) with tokenSize
               tokenSize = currToken.GetTokenSize();
            }
        }
        //Reached end of file
        //Create, add to tokenMap, and print ENDOFFILE Token
            
        Token EOFToken = new Token("ENDOFFILE", NullValue, 0);
        TokenMap.put(TokenMapKey++, EOFToken);
        EOFToken.PrintToken();
    }
    
    //Using the current character, get the respective Token 
    private Token GetNextToken()
    {
       char currChar = FileText.charAt(Ite);
       
       switch (currChar)
       {
           //can't start off with right curly brace
           case '}': 
                commentErrorMessage();
                break;
  
           //----------STRAIGHTFORWARD TOKENS------------
       
           //comma
           case ',':
                return new Token("COMMA", NullValue, 1);
                   
           //semi colon
           case ';':
                return new Token("SEMICOLON", NullValue, 1);
       
           //left bracket
           case '[':
                return new Token("LBRACKET", NullValue, 1);
       
           //right bracket
           case ']':
                return new Token("RBRACKET", NullValue, 1);
           
           //left paren
           case '(':
                return new Token("LPAREN", NullValue, 1);
       
           //right paren
           case ')':
                return new Token("RPAREN", NullValue, 1);
       
           //equal sign
           case '=':
                return new Token("RELOP", "1", 1);
                
           //multiply
           case '*':
                return new Token("MULOP", "1", 1);
                
           //divide
           case '/':
                return new Token("MULOP", "2", 1);
                
           //-----------ENDMARKER or DOUBLEDOT------------------
           
           case '.':
                return getDotTokenizer();
                
           //---------<, <=, >, >=, <> TOKENS -------------------
                
           //determine if <, <=, or <> and return Token
           case '<': 
                return getLessTokenizer();
                
           //determine if > or >= and return Token
           case '>':
                return getGreaterTokenizer();
                
           //---------:=, : TOKENS ------------------------------
                
           //determine if := or : and return Token
           case ':':
                return getColonTokenizer();  
                
           //----------UNARY OR ADDOP TOKENS----------------------
           
           //determine if UNARYPLUS or + and return Token
           
           case '+':
                return getPlusTokenizer();
                
           //determine if UNARYMINUS or - and return Token
                
           case '-':
                return getMinusTokenizer();
              
       }    
            
       //----------REALCONSTANT or INTCONSTANT------------------
                
       if (isNumber(currChar))
          return getConstantTokenizer();
                
           //------------IDENTIFIER OR KEYWORDS----------------------
       else if (isLetter(currChar))     
           return getIdentifierTokenizer();
       
       //wont reach this case
       return null;
    }
    
    //----------FUNCTIONS FOR COMMENTS------------------------------
    
    //Function finds size of the comments 
    //Start lexer again at the end of the comment
    private int findCommentSize()
    {
        int tempPos = Ite;
        for (int i = Ite; i < FileSize - 1; i++)
        {
            //if the next character is a }
            if (FileText.charAt(i+1) == '}')
                //start lexer again at character after }
                return (i+2) - tempPos; 
        }
        //did not close comment with }
        commentErrorMessage();  
        return 0;
    }
    
    //Print error message and exit program when comment is incorrect
    private void commentErrorMessage()
    {
        System.out.println("Comment syntax is incorrect.");
        System.out.println("Comments are surrounded by { and }");
        System.out.println("Body of comment can not contain end of comment character");
        System.out.println("For example, { this } } is not allowed");
        System.exit(0);
    }
    
    //Function to find how much whiteSpace there is
    //Start lexer again at the end of whitespace
    private int findWhiteSpaceSize()
    {
        int tempPos = Ite;
        for (int i = Ite; i < FileSize - 1; i++)
        {    
            //if the next character is not \n, \t or whitespace
            if ((FileText.charAt(i+1) != ' ') ||
                    (FileText.charAt(i+1) != '\n') ||
                    (FileText.charAt(i+1) != '\t'))
                //start lexer again at character that is not whitespace
                return (i+1) - tempPos;
            
        }
        //reach end of file
        return FileSize - 1;
    }
    
    //------------------FUNCTIONS FOR . TOKENS----------------------------
    
    //determines if a Token is either an INTCONSTANT or an ENDMARKER
    private Token getDotTokenizer()
    {
        //get the last token from tokenMap
        int lastTokenKey = TokenMap.keySet().size();
        Token lastToken = TokenMap.get(lastTokenKey);
        String lastTokenType = lastToken.GetTokenType();
        
        //if the last token was an int constant, has to be DOUBLEDOT
        if (lastTokenType.equals("INTCONSTANT"))
            return new Token("DOUBLEDOT", NullValue, 2);
        
        else //ENDMARKER
            return new Token("ENDMARKER", NullValue, 1);
    }
    
    //-----------FUNCTIONS FOR <, <=, >, >=, <> TOKENS -------------------
    
    //determines if a Token is either a <=, <>, or <
    private Token getLessTokenizer()
    {
       char peekChar = FileText.charAt(Ite + 1);
       
        //<= Token
        switch (peekChar) 
        {
            case '=':
                return new Token("RELOP", "5", 2);
            //<> Token
            case '>':
                return new Token("RELOP", "2", 2);
            //< Token
            default:
                return new Token("RELOP", "3", 1);
        }
    }
    
    //determines if a Token is either a >= or >
    private Token getGreaterTokenizer()
    {
        char peekChar = FileText.charAt(Ite + 1);
        
        //>= Token
        if (peekChar == '=')
            return new Token("RELOP", "6", 2);
        //> Token
        else
            return new Token("RELOP", "4", 1);
    }
    
    //determines if a Token is either a := or :
    private Token getColonTokenizer()
    {
        char peekChar = FileText.charAt(Ite + 1);
        
        //:= Token
        if (peekChar == '=')
            return new Token("ASSIGNOP", NullValue, 2);
        //: Token
        else
            return new Token("COLON", NullValue, 1);
    }
            
    //------------FUNCTIONS FOR UNARY AND ADDOP TOKENS-------------------
    
    //determines if a Token is either ADDOP or UNARYPLUS
    private Token getPlusTokenizer()
    {
        //getting the size of the tokenMap will give the key
        //to the most recent token
        //use the key to get the token then check if the tokenType 
        //is a ) } IDENTIFIER or CONSTANT
        //if the tokenType is one of those then it is ADDOP token
        
        int lastTokenKey = TokenMap.keySet().size();
        Token lastToken = TokenMap.get(lastTokenKey);
        String lastTokenType = lastToken.GetTokenType();
        
        if (lastTokenType.equals("RPAREN") ||
           lastTokenType.equals("RBRACKET") ||
           lastTokenType.equals("IDENTIFIER") ||
           lastTokenType.equals("CONSTANT"))
            return new Token("ADDOP", "1", 1);
        
        //UNARYPLUS Token
        else
            return new Token("UNARYPLUS", NullValue, 1);
    }
    
    //determines if a Token is either ADDOP or UNARYMINUS
    private Token getMinusTokenizer()
    {
        //getting the size of the tokenMap will give the key
        //to the most recent token
        //use the key to get the token then check if the tokenType 
        //is a ) } IDENTIFIER or CONSTANT
        //if the tokenType is one of those then it is ADDOP token
        
        int lastTokenKey = TokenMap.keySet().size();
        Token lastToken = TokenMap.get(lastTokenKey);
        String lastTokenType = lastToken.GetTokenType();
        
        if (lastTokenType.equals("RPAREN") ||
           lastTokenType.equals("RBRACKET") ||
           lastTokenType.equals("IDENTIFIER") ||
           lastTokenType.equals("CONSTANT"))
            return new Token("ADDOP", "2", 1);
        
        //UNARYMINUS Token
        else
            return new Token("UNARYMINUS", NullValue, 1);
    }
    
    //----------FUNCTIONS FOR REAL CONSTANT OR INT CONSTANT---------------
    
    //determines if a Token is INTCONSTANT
    private Token getConstantTokenizer()
    {
        String constant = FileText.charAt(Ite) + "";
        
        for (int i = Ite; i < FileSize - 1; i++)
        {
            char peekChar = FileText.charAt(i+1);
            //next char is e, scientific notation
            if (peekChar == 'e')
            {
                constant += 'E';
                return realConstantExpTokenizer(constant, i+1);
            }
            //next char is . could be DOUBLEDOT or REALCONSTANT
            else if (peekChar == '.')
            {
                
                //DOUBLEDOT so pushback and return INTCONSTANT
                if (FileText.charAt(i+2) == '.')
                    return new Token("INTCONSTANT", constant, constant.length());
                //If another, digit have realConstant
                else if (isNumber(FileText.charAt(i+2)))
                {
                    constant = constant.concat("." + FileText.charAt(i+2));
                    return realConstantTokenizer(constant, i+2);
                }
                else //incorrect character after digit . 
                {
                   System.out.println("LEXER ERROR. Incorrect character following .");
                   System.exit(0); 
                }
                
            }
            //next char is whitespace or punctuation, INTCONSTANT
            else if (isWhiteSpaceOrPunc(peekChar))
                return new Token("INTCONSTANT", constant, constant.length()); 
            
            //next char is another digit
            else if (isNumber(peekChar))
                //add the digit to the constant string
                constant += peekChar;
            
            //incorrect character following first digit
            else
            {
                System.out.println("LEXER ERROR. Incorrect character following digit.");
                System.exit(0);
            }
        }
        //wont reach this
        return null;
    }
    
    //determines if a Token is a REALCONSTANT
    private Token realConstantTokenizer(String constant, int currCharPos)
    {
        for (int i = currCharPos; i < FileSize - 1; i++)
        {
            char peekChar = FileText.charAt(i+1);
            //next char is e, scientific notation
            if (peekChar == 'e')
            {
                constant += 'E';
                return realConstantExpTokenizer(constant, i+1);
            }
            //next char is another digit
            else if (isNumber(peekChar))
                constant += peekChar;
            
            //next char is whitespace or punctuation, REALCONSTANT
            else if (isWhiteSpaceOrPunc(peekChar))
                return new Token("REALCONSTANT", constant, constant.length());
            
            //incorrect character following digit
            else
            {
                System.out.println("LEXER ERROR. Incorrect character following digit.");
                System.exit(0);
            }
        }
        //wont reach this
        return null;
    }
    
    //determines if a Token is a REALCONSTANT given scientific notation
    private Token realConstantExpTokenizer(String constant, int currCharPos)
    {
        for (int i = currCharPos; i < FileSize - 1; i++)
        {
            char peekChar = FileText.charAt(i+1);
            
            if (isNumber(peekChar))
                constant += peekChar;
            
            //unary + or -
            else if ((peekChar == '+') || (peekChar == '-'))
            {
                //check if digit after unary + or -
                if (isNumber(FileText.charAt(i+2)))
                    constant = constant.concat("" + peekChar + FileText.charAt(i+2));
                
                //incorrect character after unary + or -
                else
                {
                    System.out.println("LEXER ERROR. Need digit after unary + or -");
                    System.exit(0);
                }
            }
            else if (isWhiteSpaceOrPunc(peekChar))
                return new Token("REALCONSTANT", constant, constant.length());
            
            //incorrect character following e
            else
            {
                System.out.println("LEXER ERROR. Incorrect character after e");
                System.exit(0);
            }
        }
        //wont reach this
        return null;
    }
    
    //Returns true if character is white space of punctuation
    private boolean isWhiteSpaceOrPunc(char checkChar)
    {
        String wSpaceOrPunc = ".,;:<>/*[]+-=()}{ \t\n";
        
        return wSpaceOrPunc.indexOf(checkChar) >= 0;
    }
    
    //Returns true if the character is a number
    private boolean isNumber(char checkChar)
    {
        String digits = "0123456789";
        
        return digits.indexOf(checkChar) >= 0;
    }
    
    //-----------FUNCTIONS FOR IDENTIFIER OR KEYWORDS------------------
    
    //determines if a Token is an IDENTIFIER
    private Token getIdentifierTokenizer()
    {
        String identifier = FileText.charAt(Ite) + "";
        
        for (int i = Ite; i < FileSize - 1; i++)
        {
            char peekChar = FileText.charAt(i+1);
            
            if (isAlphaNumeric(peekChar))
                identifier += peekChar;
            
            //character is not alphanumeric so decide
            //if IDENTIFIER or KEYWORD
            else 
            {
                if (isKeyWord(identifier))
                    return getKeyWordTokenizer(identifier);
                else //is identifier
                {
                    //maximum length of identifiers is 25 characters
                    if (identifier.length() > 25) 
                    {
                        System.out.println("LEXER ERROR. Identifier can be max 25 characters");
                        System.exit(0);
                    }
                    else
                        return new Token("IDENTIFIER", identifier.toUpperCase(), identifier.length());
                } 
            }
        }
        //wont reach this
        return null;
    }
    
    //Returns true if a String is a Pascal key word
    private boolean isKeyWord(String identifier)
    {
        for (String keyWord: KEY_WORDS)
        {
            if (identifier.equals(keyWord)) 
                return true;  
        }
        return false;
    }
    
    //Returns the Token that matches with the key word
    private Token getKeyWordTokenizer(String keyWord)
    {
        switch (keyWord)
        {
            case "program":
                return new Token("PROGRAM", NullValue, 7);
            case "begin":
                return new Token("BEGIN", NullValue, 5);
            case "end":
                return new Token("END", NullValue, 3);
            case "var":
                return new Token("VAR", NullValue, 3);
            case "function":
                return new Token("FUNCTION", NullValue, 8);
            case "procedure":
                return new Token("PROCEDURE", NullValue, 9);
            case "result":
                return new Token("RESULT", NullValue, 6);
            case "integer":
                return new Token("INTEGER", NullValue, 7);
            case "real":
                return new Token("REAL", NullValue, 4);
            case "array":
                return new Token("ARRAY", NullValue, 5);
            case "of":
                return new Token("OF", NullValue, 2);
            case "not":
                return new Token("NOT", NullValue, 3);
            case "if":
                return new Token("IF", NullValue, 2);
            case "then":
                return new Token("THEN", NullValue, 4);
            case "else":
                return new Token("ELSE", NullValue, 4);
            case "while":
                return new Token("WHILE", NullValue, 5);
            case "do":
                return new Token("DO", NullValue, 2);
            case "div":
                return new Token("MULOP", "3", 3);
            case "mod":
                return new Token("MULOP", "4", 3);
            case "and":
                return new Token("MULOP", "5", 3);
            case "or":
                return new Token("ADDOP", "3", 2);
        }
        //wont reach this
        return null;
    }
    
    //Returns true if the character is a letter
    private boolean isLetter(char checkChar)
    {
        String letters = "abcdefghijklmnopqrstuvwxyz";
        
        return letters.indexOf(checkChar) >= 0;
    }
    
    //Returns true if the chacter is a letter or a number
    private boolean isAlphaNumeric(char checkChar)
    {
        String alpha = "abcdefghijklmnopqrstuvwxyz" +
                       "0123456789";
        
        return alpha.indexOf(checkChar) >= 0;
    }
}
