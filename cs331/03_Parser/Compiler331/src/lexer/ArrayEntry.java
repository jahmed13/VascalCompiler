
package lexer;

//Type of SymbolTableEntry
public class ArrayEntry extends SymbolTableEntry{
    
    private int Address = 0;
    private String TokenType = "";
    private int UpperBound = 0;
    private int LowerBound = 0;
    private boolean ParamTag = false;
    private boolean FuncTag = false;
    
    public ArrayEntry(String name, int address, String tokenType, int upperBound, int lowerBound)
    {
        super(name);
        this.Address = address;
        this.TokenType = tokenType;
        this.UpperBound = upperBound;
        this.LowerBound = lowerBound;
    }
    
    @Override
    public boolean IsArray()
    {
        return true;
    }
    
    public void SetAddress(int address)
    {
        this.Address = address;
    }
    
    @Override
    public int GetAddress()
    {
        return Address;
    }
    
    @Override
    public String GetTokenType()
    {
        return TokenType;
    }
    
    @Override
    public int GetLowerBound()
    {
        return LowerBound;
    }
    
    @Override
    public int GetUpperBound()
    {
        return UpperBound;
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
}
