
package lexer;

//Type of SymbolTableEntry
public class ConstantEntry extends SymbolTableEntry{
    
    private String TokenType = "";
    private boolean ParamTag = false;
    private boolean FuncTag = false;
    
    public ConstantEntry(String name, String tokenType)
    {
        super(name);
        this.TokenType = tokenType;
    }
    
    public void SetParameter()
    {
        ParamTag = true;
    }
    
    @Override
    public boolean IsParameter()
    {
        return ParamTag;
    }
    
    public void SetFunctionResult()
    {
        FuncTag = true;
    }
    
    @Override
    public boolean IsFunctionResult()
    {
        return FuncTag;
    }
    
    @Override
    public boolean IsConstant()
    {
        return true;
    }
      
    @Override
    public String GetTokenType()
    {
        return TokenType;
    }
}
