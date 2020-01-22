
package lexer;

//Type of SymbolTableEntry
public class IODeviceEntry extends SymbolTableEntry{
    
    public IODeviceEntry(String name)
    {
        super(name);
    }
    
    @Override
    public boolean IsReserved()
    {
        return true;
    }
 
}
