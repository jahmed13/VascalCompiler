//Jonayed Ahmed
package lexer;

//Tokens are created by the Lexer
//Each token contains a tokenType, a value, and a token size
public class Token {
    
    private String TokenType = "";
    private String Value = "";
    private int TokenSize = 0;
    
    public Token(String tokenType, String value, int tokenSize)
    {
        this.TokenType = tokenType;
        this.Value = value;
        this.TokenSize = tokenSize;
    }
    
    //print the Token information using this format
    public void PrintToken()
    {
        System.out.println("['" + TokenType + "', '" + Value + "']");
    }
    
    //return the size of the token
    public int GetTokenSize()
    {
        return TokenSize;
    }
    
    //return the type of the token
    public String GetTokenType()
    {
        return TokenType;
    }
    
    //return the value of the token
    public String GetTokenValue()
    {
        return Value;
    }
}
