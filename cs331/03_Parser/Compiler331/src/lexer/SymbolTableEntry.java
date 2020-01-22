
package lexer;

import java.util.List;

//Parent class of suite of child classes
//Child classes override methods in this class

public class SymbolTableEntry {
    
    private String name = "";
    
    public SymbolTableEntry(String name)
    {
        this.name = name;   
    }
    
    //Returns the address
    public int GetAddress()
    {
        return 0;
    }
    
    
    //Returns true if the STE is a variable
    public boolean IsVariable()
    {
        return false;
    }
    
    //Returns true if the STE is a procedure
    public boolean IsProcedure()
    {
        return false;
    }
    
    //Returns true if the STE is a function
    public boolean IsFunction()
    {
        return false;
    }
    
    //Returns true if the STE is a functionResult
    public boolean IsFunctionResult()
    {
        return false;
    }
    
    //Returns true if the STE is a parameter
    public boolean IsParameter()
    {
        return false;
    }
    
    //Returns true if the STE is an array
    public boolean IsArray()
    {
        return false;
    }
    
    //Returns true if the STE is reserved
    public boolean IsReserved()
    {
        return false;
    }
    
    //Returns true if the STE is a constant
    public boolean IsConstant()
    {
        return false;
    }
    
    //Returns the name of the STE
    public String GetName()
    {
        return name;
    }
     
    //Returns the token type of the STE
    public String GetTokenType()
    {
        return "";
    } 
    
    //Sets the number of parameters of the STE to int i
    public void SetNumberOfParameters(int i)
    {
        
    }
    
    //Returns the number of parameters of the STE
    public int GetNumberOfParameters()
    {
        return 0;
    }
    
    //Returns the lower bound of the STE
    public int GetLowerBound()
    {
        return 0;
    }
    
    //Returns the upper bound of the STE
    public int GetUpperBound()
    {
        return 0;
    }
    
    //Adds a parameter to the STE
    public void AddParameter(SymbolTableEntry ste)
    {
        
    }
    
    //Returns the VariableEntry result of the STE
    public VariableEntry GetResult()
    {
        return null;
    }
    
    //Returns the List of parameter info of the STE
    public List GetParameterInfo()
    {
        return null;
    }
}
