
package lexer;

import java.util.ArrayList;
import java.util.List;

//Type of SymbolTableEntry
public class ProcedureEntry extends SymbolTableEntry{
    
    private int NumberOfParameters = 0;
    private List ParameterInfo;
    
    public ProcedureEntry(String name, int numberOfParameters, List parameterInfo)
    {
        super(name);
        this.NumberOfParameters = numberOfParameters;
        this.ParameterInfo = new ArrayList();
    }
    
    @Override
    public boolean IsProcedure()
    {
        return true;
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
    public List GetParameterInfo()
    {
        return ParameterInfo;
    }
    
    @Override
    public void AddParameter(SymbolTableEntry ste)
    {
        ParameterInfo.add(ste);
    }
   
}
