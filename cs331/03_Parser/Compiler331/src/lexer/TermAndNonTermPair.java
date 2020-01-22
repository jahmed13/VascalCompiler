
package lexer;

//Term and NonTermPair is used to traverse the Parse table
public class TermAndNonTermPair {
    
    private final String nonTerminal;
    private final String terminal;
    
    public TermAndNonTermPair(String nonTerminal, String terminal)
    {
        this.nonTerminal = nonTerminal;
        this.terminal = terminal;
    }
   
    //returns the non-terminal
    public String GetNonTerminal()
    {
        return nonTerminal;
    }
    
    //returns the terminal
    public String GetTerminal()
    {
        return terminal;
    }
}
