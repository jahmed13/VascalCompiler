
package lexer;

//Error class for errors that occur with the SymbolTable
public class SymbolTableError extends Exception{

    public SymbolTableError(String errorMessage)
    {
        super(errorMessage);
    }
}
