
package lexer;

import java.util.ArrayList;
import java.util.List;

//Type of SymbolTableEntry
public class FunctionEntry extends SymbolTableEntry{
    
    private int NumberOfParameters = 0;
    private List ParameterInfo;
    private VariableEntry Result = null;
    private String ResultType = null;
    private String Type = null;
    
    public FunctionEntry(String name, int numberOfParameters, List parameterInfo, VariableEntry result)
    {
        super(name);
        this.NumberOfParameters = numberOfParameters;
        this.ParameterInfo = new ArrayList();
        this.Result = result;
    }
    
    @Override
    public boolean IsFunction()
    {
        return true;
    }
    
    public void SetType(String type)
    {
        this.Type = type;
    }
    
    public void SetResultType(String resultType)
    {
        this.Result.SetTokenType(resultType);
    }
    
    @Override
    public void SetNumberOfParameters(int numberOfParameters)
    {
        this.NumberOfParameters = numberOfParameters;
    }
  
    @Override
    public int GetNumberOfParameters()
    {
        return NumberOfParameters;
    }
    
    @Override
    public VariableEntry GetResult()
    {
        return Result;        
    }
    
    public void SetResult(VariableEntry result)
    {
        this.Result = result;
    }
    
    @Override
    public void AddParameter(SymbolTableEntry adder)
    {
        ParameterInfo.add(adder);
    }
    
    @Override
    public List GetParameterInfo()
    {
        return ParameterInfo;
    }
 
}
