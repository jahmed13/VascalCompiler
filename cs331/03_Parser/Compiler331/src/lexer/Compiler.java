//Jonayed Ahmed
package lexer;

import java.util.HashMap;

//Wraps up the rest of the classes in the lexer package
//Responsible for creating a Lexer object to tokenize file text
//Responsible for creating a Parser object and sending tokens to parse file

public class Compiler 
{
    
    private final String FileText;
    
    public Compiler(String fileText)
    {
       this.FileText = fileText; 
    }
    
    //starts the compiler by creating a Lexer and Parser
    public void runCompiler() throws SymbolTableError, SemanticActionError
    {
        //create new Lexer object with the file contents
        Lexer l = new Lexer(FileText);   
        
        //start at char index 0, go through file 
        //iterate by 1 for each character
        //and check for tokens
        l.goThroughFile(0, 1); 
                               
                  
        //HashMap containing all of the tokens 
        HashMap<Integer, Token> tokenMap = l.getTokenMap();
        
        //create a new Parser object with all of the tokens
        Parser p = new Parser(tokenMap);

        //load the parse table and grammar table
        p.loadParseTable();
        p.loadGrammar();
        
        //start the parsing of the file by going through the tokens
        p.goThroughTokenMap();
    }
    
}
