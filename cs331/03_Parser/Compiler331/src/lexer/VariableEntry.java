
package lexer;

//Type of SymbolTableEntry
public class VariableEntry extends SymbolTableEntry{
    
    private int address = 0;
    private String tokenType = "";
    private boolean resultTag = false;
    private boolean paramTag = false;
    
    public VariableEntry(String name, int address, String tokenType)
    {
        super(name);
        this.address = address;
        this.tokenType = tokenType;
    }
    
    @Override
    public boolean IsVariable()
    {
        return true;
    }
    
    @Override
    public boolean IsFunctionResult()
    {
        return resultTag;
    }
    
    public void SetFunctionResult()
    {
        resultTag = true;
    }
    
    public void SetAddress(int address)
    {
        this.address = address;
    }
    
    @Override
    public String GetTokenType()
    {
        return tokenType;
    }
    
    public void SetTokenType(String type)
    {
        tokenType = type;
    }
    
    @Override
    public int GetAddress()
    {
        return address;
    }
    
    public void SetResult()
    {
        resultTag = true;
    }
    
    @Override
    public boolean IsParameter()
    {
        return paramTag;
    }
    
    public void SetParameter()
    {
        paramTag = true;
    }
    
}
