
package lexer;

//Error class for errors that occur with the SymbolTable
public class SemanticActionError extends Exception{
    
    public SemanticActionError(String errorMessage)
    {
        super(errorMessage);
    }
}
