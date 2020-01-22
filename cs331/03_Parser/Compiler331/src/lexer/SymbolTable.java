package lexer;

import java.util.Hashtable;

//SymbolTable used by semantic actions which are called at appropriate points
//in the parse to insert names and information and look up names used in the
//body of the program

public class SymbolTable {
    
    private Hashtable<String, SymbolTableEntry> SymTable = null;
    
    public SymbolTable(int capacity)
    {
        SymTable = new Hashtable<>(capacity);
    }
    
    //find a value in SymbolTable using name
    public SymbolTableEntry Lookup(String name) 
    {
        return SymTable.get(name);
    }
    
    //put an entry into the SymbolTable
    public void Insert(String keyName, SymbolTableEntry value)
    {
        SymTable.put(keyName, value);
    }
    
    //return the size of the SymbolTable
    public int Size()
    {
       return SymTable.size(); 
    }
    
    //Print contents of the SymbolTable
    public void DumpTable()
    {
       System.out.println(SymTable); 
    }
}
