
package lexer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

//The Semantic Actions produce some output based on the input
public class SemanticAction {
    
    private final Stack<Object> SemStack = new Stack<>();
    private boolean Insert = true;
    private boolean Global = true;
    private boolean Array = false;
    int GlobalMemory = 0;
    int LocalMemory = 0;
    
    private SymbolTable GlobalTable;
    private SymbolTable LocalTable = new SymbolTable(50);
    private SymbolTable ConstantTable = new SymbolTable(50);
    
    private int GlobalStore = 0; //keeps track of an alloc statement which can be filled in
                                 //after its value is known
    
    private int LocalStore = 0;//similar to the global store except it is used for alloc and
                               //free statements of functions and procedures
    
    public Quadruples Q = new Quadruples();
    
    Stack<Integer> ParamCount = new Stack<>(); //a parameter stack where each element is the number of 
                               //parameters of a function or procedures
    
    Stack<List<SymbolTableEntry>> ParamStack = new Stack<>(); //a parameter info stack, where each element is
                                              //is a list of symbol table entries representing 
                                              //the parameter information for a function of procedure
    
    //keeps track of the current index into the list on the top of paramStack
    private int NextParam;
    
    //keeps track of what kind of expression the semantic actions are dealing with
    private enum EType{ARITHMETIC, RELATIONAL};
    
    private int TempCount = 0;
    
    private SymbolTableEntry CurrentFunction;
    
    //***************************************
    //input your filename in this constructor
    //***************************************
    public SemanticAction()
    {
        GlobalTable = new SymbolTable(50);
        GlobalTable.Insert("READ", new ProcedureEntry("READ",0, null));
        GlobalTable.Insert("WRITE", new ProcedureEntry("WRITE",0, null));
        GlobalTable.Insert("INPUT", new VariableEntry("INPUT",0, "IDENTIFIER"));
        GlobalTable.Insert("OUTPUT", new VariableEntry("OUTPUT", 0, "IDENTIFIER"));
        GlobalTable.Insert("MAIN", new IODeviceEntry("MAIN"));
        
        GlobalTable.Insert("MAIN", new ProcedureEntry("MAIN",0, null));
        GlobalTable.Insert("READ", new ProcedureEntry("READ",0, null));
        GlobalTable.Insert("WRITE", new ProcedureEntry("WRITE",0, null));
        
        //*****************************************************
        //input your filename here -----------------------------------------------------------------------------------
        //                                              |                                                            |
        //                                              |                                                            |
        //                                              V                                                            V
        GlobalTable.Insert("/home/jahmed/Desktop/cs331/03_Parser/phase2-1.txt", new IODeviceEntry("/home/jahmed/Desktop/cs331/03_Parser/phase2-1.txt"));
        
    }
    
    //Token t is previous token before semantic action number
    //based on the different semantic action number, call the 
    //desired semantic action
    public void Execute(int semActionNum, Token t) throws SymbolTableError, SemanticActionError
    {
        switch (semActionNum)
        {
            case 1: Case1Mode();
                    break;
           
            case 2: Case2Mode();
                    break;
                    
            case 3: Case3Mode();
                    break;
                    
            case 4: Case4Mode(t);
                    break;
                    
            case 5: Case5Mode();
                    break;
                   
            case 6: Case6Mode();
                    break;
                    
            case 7: Case7Mode(t);
                    break;
                    
            case 9: Case9Mode();
                    break;
                    
            case 11: Case11Mode();
                    break;
                    
            case 13: Case13Mode(t);
                    break;
                    
            case 15: Case15Mode(t);
                    break;
                    
            case 16: Case16Mode();
                    break;
                    
            case 17: Case17Mode(t);
                    break;
                    
            case 19: Case19Mode();
                    break;
                    
            case 20: Case20Mode();
                    break;
                    
            case 21: Case21Mode();
                    break;
                    
            case 22: Case22Mode();
                    break;
                  
            case 24: Case24Mode();
                    break;
                    
            case 25: Case25Mode();
                    break;
                    
            case 26: Case26Mode();
                    break;
                    
            case 27: Case27Mode();
                    break;
                    
            case 28: Case28Mode();
                    break;
                    
            case 29: Case29Mode();
                    break;
                    
            case 30: Case30Mode(t);
                    break;
                    
            case 31: Case31Mode();
                    break;
                    
            case 32: Case32Mode();
                    break;
                    
            case 33: Case33Mode();
                    break;
                    
            case 34: Case34Mode(t);
                    break;
                    
            case 35: Case35Mode();
                    break;
            
            case 36: Case36Mode();
                    break;
                    
            case 37: Case37Mode();
                    break;

            case 38: Case38Mode(t);
                    break;
                    
            case 39: Case39Mode();
                    break;
                    
            case 40: Case40Mode(t);
                    break;
                    
            case 41: Case41Mode();
                    break;
                    
            case 42: Case42Mode(t);
                    break;
                    
            case 43: Case43Mode();
                    break;
                    
            case 44: Case44Mode(t);
                    break;
                    
            case 45: Case45Mode();
                    break;
                    
            case 46: Case46Mode(t);
                    break;
                    
            case 47: Case47Mode();
                    break;
                    
            case 48: Case48Mode(t);
                    break;
                    
            case 49: Case49Mode();
                    break;
                    
            case 50: Case50Mode();
                    break;
                    
            case 51: Case51Mode(t);
                    break;
                    
            case 52: Case52Mode();
                    break;
                    
            case 53: Case53Mode();
                    break;
                    
            case 54: Case54Mode();
                    break;
                    
            case 55: Case55Mode();
                    break;
                    
            case 56: Case56Mode();
                    break;
                    
            //51READ and 51WRITE
                    
            case 9998: Case51WRITE();
                    break;
                    
            case 9999: Case51READ();
                    break;
                  
        }
    }
    
    //Insert mode when accessing symbol table
    private void Case1Mode()
    {
        Insert = true;
    }
    
    //Search mode when accesssing symbol table
    private void Case2Mode()
    {
        Insert = false;
    }
    
    //Deals with variables that are declared as arrays or simple
    private void Case3Mode() 
    {
        Token tok1 = (Token) SemStack.pop();
        String tokenType = tok1.GetTokenType();
        
        if (Array)
        {
            Token tok2 = (Token) SemStack.pop();
            String uBoundString = tok2.GetTokenValue();
            int upperBound = Integer.parseInt(uBoundString);
            
            Token tok3 = (Token) SemStack.pop();
            String lBoundString = tok3.GetTokenValue();
            int lowerBound = Integer.parseInt(lBoundString);
            
            int memorySize = (upperBound - lowerBound) + 1;
            
            while (! SemStack.empty())//while there are still elements in the stack
            {
                Token tok4 = (Token) SemStack.peek();
                if (tok4.GetTokenType().equalsIgnoreCase("IDENTIFIER"))
                {
                    Token tok5 = (Token)SemStack.pop();
                    ArrayEntry id = new ArrayEntry(tok5.GetTokenValue(),0, tokenType,upperBound, lowerBound);

                    if (Global)
                    {
                        id.SetAddress(GlobalMemory);
                        GlobalTable.Insert(id.GetName(), id);
                        GlobalMemory += memorySize;
                    }
                    else
                    {
                        id.SetAddress(LocalMemory);
                        LocalTable.Insert(id.GetName(), id);
                        LocalMemory += memorySize;
                    }
                }
            }
        }
        
        else //simple variable
        {
            
            while ((! SemStack.empty()) && SemStack.peek() instanceof Token)//while there are still elements in the stack
            {
                    Token tok6 = (Token) SemStack.peek();
                    if (tok6.GetTokenType().equalsIgnoreCase("IDENTIFIER"))
                    {
                        Token tok7 = (Token) SemStack.pop();

                        VariableEntry id = new VariableEntry(tok7.GetTokenValue(), 0, tokenType);

                        if (Global)
                        {
                            id.SetAddress(GlobalMemory);
                            GlobalTable.Insert(id.GetName(), id);
                            GlobalMemory++;
                        }
                        else
                        {
                            id.SetAddress(LocalMemory);
                            LocalTable.Insert(id.GetName(), id);
                            LocalMemory ++;
                        }
                    }
                
            }
            
        }    
        Array = false;
    }
    
    //Pushes a token, should be a type
    private void Case4Mode(Token t)
    {
        SemStack.push(t);
    }
    
    //Generate code for start of function
    private void Case5Mode()
    {
        Insert = false;
        SymbolTableEntry id = (SymbolTableEntry)SemStack.pop();
        Generate("PROCBEGIN", id.GetName());
        LocalStore = Q.GetNextQuad();
        Generate("alloc", "_");
    }
    
    //The next set of variables declared as arrays
    private void Case6Mode()
    {
        Array = true;
    }
    
    //Token should be an integer identifier
    //This action pushes on the upper and lower bounds of arrays
    //These are to be used during action 3
    private void Case7Mode(Token t) throws SymbolTableError 
    {
        SemStack.push(t);
    }
    
    //Pop and insert ids into the global table
    private void Case9Mode()
    {

        //pop the ids
        
        //output
        Token id1 = (Token)SemStack.pop();
        
        //input
        Token id2 = (Token)SemStack.pop();
        
        //program name
        Token id3 = (Token)SemStack.pop();
        
        //Insert the first two ids (id1 and id2) as reserved IODevice entries
        //in the global symbol table
        GlobalTable.Insert(id1.GetTokenValue(), new IODeviceEntry(id1.GetTokenValue()));
        GlobalTable.Insert(id2.GetTokenValue(), new IODeviceEntry(id2.GetTokenValue()));
        
        //Insert the last id (id3) as a reserved Procedure entry, with 0 parameters 
        //in the global symbol table
        GlobalTable.Insert(id3.GetTokenValue(), new ProcedureEntry(id3.GetTokenValue(), 0, null));
        
        //Change from global to local environment mode
        Insert = false;
        Generate("call", "main", "0");
        Generate("exit");
    }
    
    //Generate code for the end of the function
    private void Case11Mode()
    {
        Global = true;
        //delete the local symbol table
        LocalTable = new SymbolTable(50);
        CurrentFunction = null;
        Backpatch(LocalStore, LocalMemory);
        Generate("free", LocalMemory);
        Generate("PROCEND");
    }
    
    //Pushes IDENTIFIER token onto semStack
    private void Case13Mode(Token t) throws SymbolTableError 
    {
        //then checks if the value is an IDENTIFIER
        if (t.GetTokenType().equalsIgnoreCase("IDENTIFIER"))
            SemStack.push(t);
        
        else
            throw new SymbolTableError("Action 13 needs an IDENTIFIER");
        
    }
    
    //Store result of function
    private void Case15Mode(Token t)
    {
        //create a varaible to store the result of the function
        VariableEntry result = Create(t.GetTokenValue() + "_RESULT", "INTEGER");
        
        //set the result tag of the variable entry class
        result.SetResult();
        
        //create a new function entry with name from the token
        //from the parser and the result variable just created
        
        SymbolTableEntry id = new FunctionEntry(t.GetTokenValue(), 0, null, result);
        GlobalTable.Insert(id.GetName(), id);
        Global = false;
        LocalMemory = 0;
        CurrentFunction = id;
        SemStack.push(id);
    }
    
    //Set type of function and its result
    private void Case16Mode()
    {
        Token type = (Token)SemStack.pop();
        FunctionEntry id = (FunctionEntry)SemStack.peek();
        id.SetType(type.GetTokenType());
        //set the type of the result variable of id
        id.SetResultType(type.GetTokenType());
        CurrentFunction = id;
    }
    
    //Create procedure in symbol table
    private void Case17Mode(Token t)
    {
        //create a new procedure entry with the name of the token
        //from the parser
        SymbolTableEntry id = new ProcedureEntry(t.GetTokenValue(), 0, null);
        GlobalTable.Insert(id.GetName(),id);
        Global = false;
        LocalMemory = 0;
        CurrentFunction = id;
        SemStack.push(id);
    }
    
    //Initialize count of formal parameters
    private void Case19Mode()
    {
        ParamCount = new Stack<>();
        ParamCount.push(0);
    }
    
    //Get number of parameters
    private void Case20Mode()
    {
        SymbolTableEntry id = (SymbolTableEntry) SemStack.peek();
        int numParams = ParamCount.pop();
        //id is a function entry or a procedure entry
        id.SetNumberOfParameters(numParams);
    }
    
    //Create temporary variables to store parameter info
    private void Case21Mode()
    {
        SemStackDump();
        Token type = (Token)SemStack.pop();
        
        //if array, then pop the upper and lower bounds
        int upperBound = -1;
        int lowerBound = -1;

        if (Array)
        {
              upperBound = Integer.parseInt(((Token)SemStack.pop()).GetTokenValue());
              lowerBound = Integer.parseInt(((Token)SemStack.pop()).GetTokenValue());
        }
       
        //the tokens on the stack, which represent paramaters,
        //must be added from the bottom-most id to the top-most
        Stack<Token> parameters = new Stack<>();
       
        //as the ids are popped off the stack, push them onto 
        //the new stack to reverse the order
        
        while (SemStack.peek() instanceof Token)
        {
            if (((Token)SemStack.peek()).GetTokenType().equals("IDENTIFIER"))
                parameters.push((Token)SemStack.pop());
            
        }
        
        while (! parameters.empty())
        {
            Token param = parameters.pop();
            if (Array)
            {
                ArrayEntry var = new ArrayEntry(param.GetTokenValue(), LocalMemory, 
                                                type.GetTokenType(), upperBound, lowerBound);
                var.SetParameter();
                LocalTable.Insert(var.GetName(), var);
                //current function is either a procedure of function entry
                CurrentFunction.AddParameter(var);
            }
            else
            {
                VariableEntry var = new VariableEntry(param.GetTokenValue(), LocalMemory, 
                                                      type.GetTokenType());
                var.SetParameter();
                LocalTable.Insert(var.GetName(), var);
                //current function is either a procedure or function entry
                CurrentFunction.AddParameter(var);
            }
            
            LocalMemory++;
            //increment the top of paramCount
            ParamCount.push(ParamCount.pop() + 1);
        }
        Array = false;
    }
    
    //Update branch destination for IF -> #t to next quad
    private void Case22Mode() throws SemanticActionError
    {
       EType etype = (EType)SemStack.pop();
       if (etype != EType.RELATIONAL) 
           throw new SemanticActionError("EType mismatch error");
       
       else
       {
           List<Integer> EFalse = (List)SemStack.pop();
           List<Integer> ETrue = (List)SemStack.pop();
           Backpatch(ETrue, Q.GetNextQuad());
           SemStack.push(ETrue);
           SemStack.push(EFalse);
       }
    }
    
    //Store line number of beginning of loop
    private void Case24Mode()
    {
        int beginLoop = Q.GetNextQuad();
        SemStack.push(beginLoop);
    }
    
    //Initialization for a WHILE loop
    private void Case25Mode() throws SemanticActionError
    {
        EType etype = (EType) SemStack.pop();
        if (etype != EType.RELATIONAL)
            throw new SemanticActionError("EType mismatch error");
        
        List<Integer> EFalse = (List)SemStack.pop();
        List<Integer> ETrue = (List)SemStack.pop();
        Backpatch(ETrue, Q.GetNextQuad());
        SemStack.push(ETrue);
        SemStack.push(EFalse);
    }
    
    //Write code at end of WHILE loop
    private void Case26Mode()
    {
        List<Integer> EFalse = (List)SemStack.pop();
        List<Integer> ETrue = (List)SemStack.pop();
        int beginLoop = (int)SemStack.pop();
        Generate("goto", beginLoop);
        Backpatch(EFalse, Q.GetNextQuad()); 
    }
    
    //Sets up ELSE case
    private void Case27Mode()
    {
        List<Integer> skipElse = MakeList(Q.GetNextQuad());
        Generate("goto", "_");
        List<Integer> EFalse = (List)SemStack.pop();
        List<Integer> ETrue = (List)SemStack.pop();
        Backpatch(EFalse, Q.GetNextQuad());
        SemStack.push(skipElse);
        SemStack.push(ETrue);
        SemStack.push(EFalse);
    }
    
    //End of ELSE statement
    private void Case28Mode()
    {
        List<Integer> EFalse = (List)SemStack.pop();
        List<Integer> ETrue = (List)SemStack.pop();
        List<Integer> skipElse = (List)SemStack.pop();
        Backpatch(skipElse, Q.GetNextQuad());
    }
    
    //End of IF without ELSE
    private void Case29Mode()
    {
        List<Integer> EFalse = (List)SemStack.pop();
        List<Integer> ETrue = (List)SemStack.pop();
        Backpatch(EFalse, Q.GetNextQuad());
    }
    
    //Check to see if a variable has been declared
    private void Case30Mode(Token t) throws SymbolTableError 
    {
        SymbolTableEntry id = LookupID(t);
        if (id == null)
            throw new SymbolTableError("UNDECLARED VARIABLE ERROR");
        else{
            SemStack.push(id);
            SemStack.push(EType.ARITHMETIC);
        }
    }
    
    //Put the value of variable ID2 in ID1 (ie Variable assignment)
    private void Case31Mode() throws SemanticActionError
    {
        EType etype = (EType)SemStack.pop();
        
        if(etype != EType.ARITHMETIC)
            throw new SemanticActionError("EType mismatch error");
        
        
        SymbolTableEntry id2 = (SymbolTableEntry)SemStack.pop();
        
        SymbolTableEntry offset = (SymbolTableEntry)SemStack.pop();
        
        SymbolTableEntry id1 = (SymbolTableEntry)SemStack.pop();
        
        if (Typecheck(id1, id2) == 3)
            throw new SemanticActionError("Type mismatch error: Can't assign real to an integer");
        
        if(Typecheck(id1, id2) == 2)
        {
            VariableEntry temp = Create("temp", "REAL");
            Generate("ltof", id2, temp);
            if (offset == null)
                Generate("move", temp, id1);
            
            else
                Generate("stor", temp, offset, id1);  
        }
        else 
        {
            if(offset == null)
                Generate("move", id2, id1);
            
            else
                Generate("stor", id2, offset, id1);
        }
    }
    
    //Ensure TOS is an array & typecheck
    private void Case32Mode() throws SemanticActionError
    {
        EType etype = (EType) SemStack.pop();
        SymbolTableEntry id = (SymbolTableEntry) SemStack.peek();
        
        if (! etype.equals(EType.ARITHMETIC))
            throw new SemanticActionError("EType mismatch error");
        
        else if (! id.IsArray())
            throw new SemanticActionError("Error: ID is not an array");
    }
    
    //Calculate memory offset for array element
    private void Case33Mode() throws SemanticActionError
    {
       EType etype = (EType)SemStack.pop();
       if (! etype.equals(EType.ARITHMETIC)) 
           throw new SemanticActionError("EType mismatch error");
       
       else
       {
            SymbolTableEntry id = (SymbolTableEntry) SemStack.pop();
            if (! id.GetTokenType().equalsIgnoreCase("INTEGER")) 
                throw new SemanticActionError("type mismatch error");
            
            ArrayEntry arr = (ArrayEntry)SemStack.peek();
            VariableEntry temp1 = Create("temp", "INTEGER");
            VariableEntry temp2 = Create("temp", "INTEGER");
            Generate("move", arr.GetLowerBound(), temp1);
            Generate("sub", id, temp1, temp2);
            SemStack.push(temp2);
       }
    }
    
    //Function or procedure
    private void Case34Mode(Token t) throws SymbolTableError, SemanticActionError
    {
       EType etype = (EType)SemStack.pop();
       SymbolTableEntry id = (SymbolTableEntry)SemStack.peek();
       if (id.IsFunction()) {
           SemStack.push(etype);
           Execute(52, t);
       }
       else 
           SemStack.push(null);
    }
    
    //Set up to call a procedure
    private void Case35Mode() throws SemanticActionError
    {
        EType etype = (EType)SemStack.pop();
        //id is a procedure entry
        ProcedureEntry id = (ProcedureEntry) SemStack.peek();
        SemStack.push(etype);
        ParamCount.push(0);
        ParamStack.push(id.GetParameterInfo());
    }
    
    //Generate code to call a procedure
    private void Case36Mode() throws SemanticActionError
    {
        EType etype = (EType)SemStack.pop();
        //id is a procedure entry
        ProcedureEntry id = (ProcedureEntry) SemStack.pop();
        if (id.GetNumberOfParameters() != 0)
            throw new SemanticActionError("Wrong number of parameters error");
        
        Generate("call", id.GetName(), 0);
    }
    
    //Consume actual parameters in a list of parameters
    private void Case37Mode() throws SemanticActionError
    {
        EType etype = (EType)SemStack.pop();
        if (etype != EType.ARITHMETIC)
            throw new SemanticActionError("EType mismatch error");
        
        
        SymbolTableEntry id = (SymbolTableEntry)SemStack.peek();
        if (!(id.IsVariable() || id.IsConstant() || id.IsFunctionResult() || id.IsArray()))
            throw new SemanticActionError("Bad param type error");
        
        
        //increment the top of paramtCount
        ParamCount.push(ParamCount.pop() + 1);
        
        //find the name of the procedure/function on the bottom of the stack
        Stack parameters = new Stack();
        while (!(SemStack.peek() instanceof ProcedureEntry ||
                SemStack.peek() instanceof FunctionEntry))
            parameters.push(SemStack.pop());
        
        
        //funcId is a procedure or function entry
        SymbolTableEntry funcId = (SymbolTableEntry)SemStack.peek();
        while (!parameters.empty())
            SemStack.push(parameters.pop());
        
        if (!(funcId.GetName().equalsIgnoreCase("READ") || funcId.GetName().equals("WRITE")))
        {
            System.out.println("param: " + ParamCount.peek() + "func: " + funcId.GetNumberOfParameters());
            
            if (ParamCount.peek() > funcId.GetNumberOfParameters())
                throw new SemanticActionError("Wrong number of params error");
            
            SymbolTableEntry param = ParamStack.peek().get(NextParam);
            
            if (!id.GetTokenType().equalsIgnoreCase(param.GetTokenType()))
                throw new SemanticActionError("Bad param type error");
            
            if (param.IsArray())
            {
                if ((id.GetLowerBound() != param.GetLowerBound()) ||
                   (id.GetUpperBound() != param.GetUpperBound())) 
                    throw new SemanticActionError("Bad param type error");        
            }
            NextParam++;
        }
    }
    
    //Ensure arithmetic operation & push
    private void Case38Mode(Token t) throws SemanticActionError
    {
       EType etype = (EType)SemStack.pop();
       if (etype != EType.ARITHMETIC) 
           throw new SemanticActionError("EType mismatch error");
       
       else
        SemStack.push(t);
    }
    
    //Change to relational & add ET/F as required
    private void Case39Mode() throws SemanticActionError
    {
       EType etype = (EType)SemStack.pop();
       if (etype != EType.ARITHMETIC) 
           throw new SemanticActionError("EType mismatch error");
       
       else
       {
            SymbolTableEntry id2 = (SymbolTableEntry)SemStack.pop();
            Token operator = (Token)SemStack.pop();
            // the operator must be replaced with the proper TVI code which
            // jump if the condition is me
            // ex. the token representing "<" should be replaced with "blt"
            String opcode = operator.GetTokenValue();
            SymbolTableEntry id1 = (SymbolTableEntry)SemStack.pop();
            if (Typecheck(id1, id2) == 2)
            {
                VariableEntry temp = Create("temp", "REAL");
                Generate("ltof", id2, temp);
                Generate(Case39RelopHelper(opcode), id1, temp, "_");
            }
            else if (Typecheck(id1, id2) == 3) 
            {
                VariableEntry temp = Create("temp", "REAL");
                Generate("ltof", id1, temp);
                Generate(Case39RelopHelper(opcode), temp, id2, "_");
            }
            else 
                Generate(Case39RelopHelper(opcode), id1, id2, "_");
            
            Generate("goto", "_");
            List<Integer> ETrue = MakeList(Q.GetNextQuad() - 2);
            List<Integer> EFalse = MakeList(Q.GetNextQuad() - 1);
            SemStack.push(ETrue);
            SemStack.push(EFalse);
            SemStack.push(EType.RELATIONAL);
         }
    }
    
    //takes the opcode and translates to addop
    private String Case39RelopHelper(String s) 
    {
        
        int i = Integer.parseInt(s);
        
        switch (i)
        {
            case 1: return "beq";
            
            case 2: return "bne";
            
            case 3: return "blt";
            
            case 4: return "bgt";
            
            case 5: return "ble";
            
            case 6: return "bge";
        }
        return null; //should not reach this
        
    }
    
    //Push unary plus/minus to stack
    private void Case40Mode(Token t)
    {
        SemStack.push(t);
    }
    
    //Apply unary plus/minus
    private void Case41Mode() throws SemanticActionError
    {
        EType etype = (EType) SemStack.pop();
        if (etype != EType.ARITHMETIC)
            throw new SemanticActionError("EType mismatch error");
        
        else
        {
            SymbolTableEntry id = (SymbolTableEntry)SemStack.pop();
            Token sign = (Token)SemStack.pop();
            if (sign.GetTokenType().equalsIgnoreCase("UNARYMINUS"))
            {
                VariableEntry temp = Create("temp", id.GetTokenType());
                if (id.GetTokenType().equalsIgnoreCase("INTEGER"))
                    Generate("uminus", id, temp);
            
                else
                    Generate("fuminus", id, temp);
            
            SemStack.push(temp);
            }
            
            else
                SemStack.push(id);
        
        SemStack.push(EType.ARITHMETIC);
        }
    }
    
    //Push ADDOP operator (+, -, etc.) on to stack
    private void Case42Mode(Token t) throws SemanticActionError
    {
        EType etype = (EType)SemStack.pop();
        if (t.GetTokenValue().equals("3"))
        {
            if(etype != EType.RELATIONAL)
                throw new SemanticActionError("EType mismatch error");
            
            //the top of the stack should be a list of integers
            List<Integer> EFalse = (List)SemStack.peek();
            Backpatch(EFalse, Q.GetNextQuad());
        }
        
        else if (etype != EType.ARITHMETIC)
            throw new SemanticActionError("EType mismatch error");
        
        SemStack.push(t);
    }
    
    //Perform ADDOP based on OP popped from stack
    private void Case43Mode()
    {
        
       EType etype = (EType)SemStack.pop();
       if (etype.equals(EType.RELATIONAL)) 
       {
           List<Integer> E2False = (List)SemStack.pop();
           List<Integer> E2True = (List)SemStack.pop();
           Token operator = (Token)SemStack.pop();
           List<Integer> E1False = (List)SemStack.pop();
           List<Integer> E1True = (List)SemStack.pop();
           List<Integer> ETrue = Merge(E1True, E2True);
           
           SemStack.push(ETrue);
           SemStack.push(E2False);
           SemStack.push(EType.RELATIONAL);
       }
       else // if etype == EType.ARITHMETIC
       { 
            SymbolTableEntry id2 = (SymbolTableEntry)SemStack.pop();
            Token operator = (Token)SemStack.pop();
            // get the TVI opcode associated with the operator token
            // ex. for a token representing addition, opcode would be "add"
            String opcode = operator.GetTokenValue();
            SymbolTableEntry id1 = (SymbolTableEntry)SemStack.pop();
            System.out.println("id1 name/val: " + id1.GetName() + " id1 token type: " + id1.GetTokenType());
            System.out.println("id2 name/val: " + id2.GetName() + " id2 token type: " + id2.GetTokenType());

           switch (Typecheck(id1, id2)) {
               case 0:
               {
                   VariableEntry temp = Create("temp", "INTEGER");
                   Generate(Case43AddopHelper(opcode), id1, id2, temp);
                   SemStack.push(temp);
                   break;
               }
               case 1:
               {
                   VariableEntry temp = Create("temp", "REAL");
                   Generate("f" + Case43AddopHelper(opcode), id1, id2, temp);
                   SemStack.push(temp);
                   break;
               }
               case 2:
               {
                   VariableEntry temp1 = Create("temp", "REAL");
                   VariableEntry temp2 = Create("temp", "REAL");
                   Generate("ltof", id2, temp1);
                   Generate("f" + Case43AddopHelper(opcode), id1, temp1, temp2);
                   SemStack.push(temp2);                 
                   break;
               }
               case 3:
               {
                   VariableEntry temp1 = Create("temp", "REAL");
                   VariableEntry temp2 = Create("temp", "REAL");
                   Generate("ltof", id1, (SymbolTableEntry)temp1);
                   Generate("f" + Case43AddopHelper(opcode), temp1, id2, temp2);
                   SemStack.push(temp2);
                   break;
                }
                default:
                   break;
           }
            
            SemStack.push(EType.ARITHMETIC);
       }
       
    }
    
    //takes the opcode and translates to addop
    private String Case43AddopHelper(String s) 
    {
        
        int i = Integer.parseInt(s);
        
        switch (i)
        {
            case 1: return "add";
            
            case 2: return "sub";
            
            case 3: return "or";
        }
        return null; //should not reach this
        
    }
    
    //Push MULOP operator (*, /, etc.) on to stack
    private void Case44Mode(Token t) throws SemanticActionError
    {
        EType etype = (EType)SemStack.pop();
        if (etype == EType.RELATIONAL)
        {
            List<Integer> EFalse = (List)SemStack.pop();
            List<Integer> ETrue = (List)SemStack.pop();
            if(t.GetTokenValue().equals("AND"))
                Backpatch(ETrue, Q.GetNextQuad());
            
            SemStack.push(ETrue);
            SemStack.push(EFalse);
        }
        
        SemStack.push(t);
    }
    
    //Perform MULOP based on OP popped from stack
    private void Case45Mode() throws SemanticActionError
    {

       EType etype = (EType)SemStack.pop();
       if (etype == EType.RELATIONAL) 
       {
           List<Integer> E2False = (List)SemStack.pop();
           List<Integer> E2True = (List)SemStack.pop();
           Token operator = (Token)SemStack.pop();
           System.out.println(operator.GetTokenType() + operator.GetTokenValue() + "TOKEN ... VALUE");
           
           if (operator.GetTokenValue().equals("5"))
           {
               List<Integer> E1False = (List)SemStack.pop();
               List<Integer> E1True = (List)SemStack.pop();

               List<Integer> EFalse = Merge(E1False, E2False);
               SemStack.push(E2True);
               SemStack.push(EFalse);
               SemStack.push(EType.RELATIONAL);
           } 
       }
       else //if etype == EType.ARITHMETIC
       { 

            SymbolTableEntry id2 = (SymbolTableEntry)SemStack.pop();
            Token operator = (Token)SemStack.pop();
            String opcode = operator.GetTokenValue();
            SymbolTableEntry id1 = (SymbolTableEntry)SemStack.pop();

            if (Typecheck(id1, id2) != 0 && (opcode.equals("4") || opcode.equals("3")))
               throw new SemanticActionError("THROW BAD PARAMETER ERROR");
            
            if (Typecheck(id1, id2) == 0) 
            {
                if (opcode.equals("4")) 
                {
                    VariableEntry temp1 = Create("temp", "INTEGER");
                    VariableEntry temp2 = Create("temp", "INTEGER");
                    VariableEntry temp3 = Create("temp", "INTEGER");
                    Generate("div", id1, id2, temp1);
                    Generate("mul", id2, temp1, temp2);
                    Generate("sub", id1, temp2, temp3);
                    SemStack.push(temp3);
                } 
                
                else if (opcode.equals("2")) 
                {
                    VariableEntry temp1 = Create("temp", "REAL");
                    VariableEntry temp2 = Create("temp", "REAL");
                    VariableEntry temp3 = Create("temp", "REAL");
                    Generate("ltof", id1, temp1);
                    Generate("ltof", id2, temp2);
                    Generate("fdiv", temp1, temp2, temp3);
                    SemStack.push(temp3);
                } 
                
                else 
                {
                    VariableEntry temp = Create("temp", "INTEGER");
                    Generate(Case45MulopHelper(opcode), id1, id2, temp);
                    SemStack.push(temp);
                }
            } 
            
            else if (Typecheck(id1, id2) == 1) 
            {
                VariableEntry temp = Create("temp", "REAL");
                Generate("f" + Case45MulopHelper(opcode), id1, id2, temp);
                SemStack.push(temp);
            } 
            
            else if (Typecheck(id1, id2) == 2) 
            {
                VariableEntry temp1 = Create("temp", "REAL");
                VariableEntry temp2 = Create("temp", "REAL");
                Generate("ltof", id2, temp1);
                Generate("f" + Case45MulopHelper(opcode), id1, temp1, temp2);
                SemStack.push(temp2);
                
            } 
            
            else if (Typecheck(id1, id2) == 3) 
            {
                VariableEntry temp1 = Create("temp", "REAL");
                VariableEntry temp2 = Create("temp", "REAL");
                Generate("ltof", id1, temp1);
                Generate("f" + Case45MulopHelper(opcode), temp1, id2, temp2);
                SemStack.push(temp2);
            }
            
            SemStack.push(EType.ARITHMETIC);
       }
       
    }
    
    //takes the opcode and translates it to a mulop
    private String Case45MulopHelper(String s)
    {
        int i = Integer.parseInt(s);
        
        switch (i)
        {
            case 1: return "mul";
            
            case 2: return "frac";
            
            case 3: return "div";
            
            case 4: return "mod";
            
            case 5: return "and";
        }
        
        return null; //should not reach this
    }
       
    //Look up value of variable of constant from SymbolTable
    private void Case46Mode(Token t) throws SymbolTableError
    {
        if (t.GetTokenType().equalsIgnoreCase("IDENTIFIER")) 
        {
           // look for the token in the global or local symbol
           // table, as appropriate
           SymbolTableEntry id = LookupID(t);
           // if token is not found
           if (id == null) 
               throw new SymbolTableError("TYPE UNDECLARED VARIABLE ERROR");
           
           SemStack.push(id);
        } 
        else if (t.GetTokenType().equalsIgnoreCase("INTCONSTANT") ||
                 t.GetTokenType().equalsIgnoreCase("REALCONSTANT")) 
        {
           // look for the token in the constant symbol table
           SymbolTableEntry id = ConstantTable.Lookup(t.GetTokenValue());

           // if token is not found
           if (id == null) 
           {
               if (t.GetTokenType().equalsIgnoreCase("INTCONSTANT")) 
                   id = new ConstantEntry(t.GetTokenValue(), "INTEGER");
              
               else if (t.GetTokenType().equalsIgnoreCase("REALCONSTANT")) 
                   id = new ConstantEntry(t.GetTokenValue(), "REAL");
                   
               ConstantTable.Insert(id.GetName(), id);
           }
           SemStack.push(id);
       }
       SemStack.push(EType.ARITHMETIC);
    }
    
    //Reserved word NOT
    private void Case47Mode() throws SemanticActionError
    {
       EType etype = (EType)SemStack.pop();
       if (etype != EType.RELATIONAL) 
           throw new SemanticActionError("EType mismatch error");
       
       else
       {
            // swap ETrue and EFalse on the stack
            List<Integer> EFalse = (List)SemStack.pop();
            List<Integer> ETrue = (List)SemStack.pop();
            SemStack.push(ETrue);
            SemStack.push(EFalse);
            SemStack.push(EType.RELATIONAL);
       }
    }
    
    //Array lookup
    private void Case48Mode(Token t) throws SymbolTableError, SemanticActionError
    {
       SymbolTableEntry offset = (SymbolTableEntry)SemStack.pop();
       if (offset != null) 
       {
           
           if (offset.IsFunction()) 
               // call action 52 with the token from the parser
               Execute(52, t);
           
           
           else
           {
                SymbolTableEntry id = (SymbolTableEntry)SemStack.pop();
                VariableEntry temp = Create("temp", id.GetTokenType());
                Generate("load", id, offset, temp);
                SemStack.push(temp);
           }
       }
       
       SemStack.push(EType.ARITHMETIC);
    }
    
    //Ensure this is a function & get parameter data
    private void Case49Mode() throws SemanticActionError
    {
        //get etype and id but do not change the stack
        EType etype = (EType) SemStack.pop();
        //id should be a functIon
        SymbolTableEntry id = (SymbolTableEntry) SemStack.peek();
        SemStack.push(etype);
        
        if (etype != EType.ARITHMETIC)
            throw new SemanticActionError("EType mismatch error");
        
        if (!id.IsFunction())
            throw new SemanticActionError("ID not a function error");
        
        ParamCount.push(0);
        ParamStack.push(id.GetParameterInfo());
    }
    
    //Generate code to assign memory for function parameters & call function
    private void Case50Mode() throws SemanticActionError
    {
        //the parameters must be generated from the bottom-most 
        //to the top most
        Stack<SymbolTableEntry> parameters = new Stack<>();
        //for each parameter on the stack
        while (SemStack.peek() instanceof ArrayEntry ||
              SemStack.peek() instanceof ConstantEntry ||
              SemStack.peek() instanceof VariableEntry)
            parameters.push((SymbolTableEntry)SemStack.pop());
        
        
        //generate code for each of the parameters
        while (! parameters.empty())
        {
            //this is the one place where you will use getParamPrefix()
            GenerateParam("param", parameters.pop());
            LocalMemory++;
        }
        
        EType etype = (EType)SemStack.pop();
        FunctionEntry id = (FunctionEntry)SemStack.pop();
        int numParams = ParamCount.pop();
        if (numParams > id.GetNumberOfParameters())
            throw new SemanticActionError("Wrong number of parameters error");
        
        Generate("call", id.GetName(), numParams);
        ParamStack.pop();
        NextParam = 0;
        
        VariableEntry temp = Create("temp", id.GetResult().GetTokenType());
        Generate("move", id.GetResult(), temp);
        SemStack.push(temp);
        SemStack.push(EType.ARITHMETIC);
    }
    
    //Generate code to assign memory for procedure parameters & call function
    private void Case51Mode(Token t) throws SymbolTableError, SemanticActionError
    {
        Stack<SymbolTableEntry> parameters = new Stack<>();
        
        while (SemStack.peek() instanceof ArrayEntry ||
              SemStack.peek() instanceof ConstantEntry ||
              SemStack.peek() instanceof VariableEntry)
            parameters.push((SymbolTableEntry)SemStack.pop());
        
        
        EType etype = (EType)SemStack.pop();
        ProcedureEntry id = (ProcedureEntry)SemStack.pop();
        
        if ((id.GetName().equalsIgnoreCase("READ")) ||
            id.GetName().equalsIgnoreCase("WRITE"))
        {
            //replace everything on the stack and call 51WRITE
            SemStack.push(id);
            SemStack.push(etype);
            
            while (!parameters.empty())
                SemStack.push(parameters.pop());
            
            if (id.GetName().equalsIgnoreCase("READ"))
                Execute(9999, t);
            
            else // id is WRITE
                Execute(9998, t);
            
        }
        else
        {
            int numParams = ParamCount.pop();
            
            if (numParams != id.GetNumberOfParameters())
                throw new SemanticActionError("Wrong number of parameters error");
            
            
            while (! parameters.empty())
            {
                //this is one place where you will use getParamPrefix()
                GenerateParam("param", parameters.pop());
                LocalMemory++;         
            }
            Generate("call", id.GetName(), numParams);
            ParamStack.pop();
            NextParam = 0;
        }
    }
    
    
// The following two actions do not appear in the grammar, but are
// called in the special case of input and output statements
// \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/ \/
    
    //Read input from the user
    private void Case51READ()
    {
        //for every parameter on the stack in reverse order
        Stack<SymbolTableEntry> parameters = new Stack<>();
        while (SemStack.peek() instanceof VariableEntry)
            parameters.push((SymbolTableEntry)SemStack.pop());
        
        
        while (!parameters.empty())
        {
            SymbolTableEntry id = parameters.pop();
            if (id.GetTokenType().equalsIgnoreCase("REAL"))
                Generate("finp", id);
            
            else
                Generate("inp", id);
            
        }
        EType etype = (EType)SemStack.pop();
        SymbolTableEntry id = (SymbolTableEntry)SemStack.pop();
        ParamCount.pop();
    }
    
    //Display variable name and contents
    private void Case51WRITE()
    {
        //for each paramter on the stack in reverse order
        Stack<SymbolTableEntry> parameters = new Stack<>();
        while (SemStack.peek() instanceof ConstantEntry || 
              SemStack.peek() instanceof VariableEntry)
            parameters.push((SymbolTableEntry) SemStack.pop());
        
        while (!parameters.empty())
        {
            SymbolTableEntry id = (SymbolTableEntry) parameters.pop();
            if (id.IsConstant())
            {
                if (id.GetTokenType().equalsIgnoreCase("REAL"))
                    Generate("foutp", id.GetName());
                
                else //id.getToken type is an integer
                    Generate("outp", id.GetName());
                
            }
            else //id is a variable entry
            {
                Generate("print", "\"" + id.GetName() + " = \"");
                if(id.GetTokenType().equalsIgnoreCase("REAL"))
                    Generate("foutp", id);
                
                else //idtype is an integer
                    Generate("outp", id);
            }
            Generate("newl");
        }
        EType etype = (EType)SemStack.pop();
        SymbolTableEntry id = (SymbolTableEntry)SemStack.pop();
        ParamCount.pop();
    }
    
    //Case for function with no parameters
    private void Case52Mode() throws SemanticActionError
    {
        EType etype = (EType) SemStack.pop();
        SymbolTableEntry id = (SymbolTableEntry) SemStack.pop();
        if (! id.IsFunction())
            throw new SemanticActionError("ID is not a function error");
        
        if ( id.GetNumberOfParameters() > 0)
            throw new SemanticActionError("Wrong number of parameters error");
        
        Generate("call", ((FunctionEntry)id).GetName(), 0);
        VariableEntry temp = Create("temp", id.GetTokenType());
        Generate("move", ((FunctionEntry)id).GetResult(), temp);
        SemStack.push(temp);
        SemStack.push(null);
    }
    
    //Look up variable or function result
    private void Case53Mode() throws SemanticActionError
    {
       EType etype = (EType)SemStack.pop();
       SymbolTableEntry id = (SymbolTableEntry)SemStack.pop();
       if (id.IsFunction()) 
       {
           if (id != CurrentFunction)
               throw new SemanticActionError("Throw illegal procedure error");
           
           SemStack.push(id.GetResult());
           SemStack.push(EType.ARITHMETIC);
       }
       else
       {
           SemStack.push(id);
           SemStack.push(etype);
       }
    }
    
    //Confirm statement is a procedure call
    private void Case54Mode() throws SemanticActionError
    {
       EType etype = (EType)SemStack.pop();
       SymbolTableEntry id = (SymbolTableEntry)SemStack.peek();
       SemStack.push(etype);
       if (!id.IsProcedure()) 
           throw new SemanticActionError("ID is not a procedure error");
    }
    
    //Generate end-of-MAIN:: wrapper code
    private void Case55Mode()
    {
       Backpatch(GlobalStore, GlobalMemory);
       Generate("free", GlobalMemory);
       Generate("procend");
    }
    
    //Generate start-of-MAIN:: wrapper code
    private void Case56Mode()
    {
       Generate("procbegin", "main");
       GlobalStore = Q.GetNextQuad();
       // the underscore as the second argument in generate
       // is a placeholder that will be filled in later by backpatch
       Generate("alloc", "_");
    }
    
    //***Generate functions***
    //------------------------
    /*to be called in the semantic actions,
    which is responsible for generating the appropriate TVI
    code and storing it in the quadruples class
    overloaded function because of different parameters that are sent*/
    
    public void Generate(String s1)
    {
        String[] str_arr = {s1, null, null, null};
        Q.AddQuad(str_arr);
    }
    
    private void Generate(String s1, String s2)
    {
        String[] str_arr = {s1,s2, null, null};
        Q.AddQuad(str_arr);
    }
    
    private void Generate(String s1, String s2, VariableEntry temp)
    {
        
        String[] str_arr = {s1,s2, GetSTEPrefix(temp) + GetSTEAddress(temp), null};
        Q.AddQuad(str_arr);
    }
    
    private void Generate(String s1, int j)
    {
        String[] str_arr = {s1, Integer.toString(j), null, null};
        Q.AddQuad(str_arr);
    }
    
    private void Generate(String s1, String s2, String s3)
    {
        String[] str_arr = {s1,s2,s3, null};
        Q.AddQuad(str_arr);
    }
    
    private void Generate(String s1, String s2, int j)
    {
        String[] str_arr = {s1, s2, Integer.toString(j), null};
        Q.AddQuad(str_arr);
    }
    
    private void Generate(String s1, SymbolTableEntry id1)
    {
        String[] str_arr = {s1, GetSTEPrefix(id1) + GetSTEAddress(id1), null, null};
        Q.AddQuad(str_arr);
    }
    
    private void GenerateParam(String s1, SymbolTableEntry id1)
    {
        String[] str_arr = {s1, GetParamPrefix(id1) + GetSTEAddress(id1), null, null};
        Q.AddQuad(str_arr);
    }
    
    private void Generate(String s1, SymbolTableEntry temp1, SymbolTableEntry temp2, String s2)
    {
        String[] str_arr = {s1, GetSTEPrefix(temp1) + GetSTEAddress(temp1), GetSTEPrefix(temp2) + GetSTEAddress(temp2), s2};
        Q.AddQuad(str_arr);
    }
    
    private void Generate(String s1, SymbolTableEntry id, VariableEntry temp)
    {
        String[] str_arr = {s1, GetSTEPrefix(id) + GetSTEAddress(id), GetSTEPrefix(temp) + GetSTEAddress(temp), null};
        Q.AddQuad(str_arr);
    }
    
    private void Generate(String s1, SymbolTableEntry id1, SymbolTableEntry id2)
    {
        String[] str_arr = {s1, GetSTEPrefix(id1) + GetSTEAddress(id1), GetSTEPrefix(id2) + GetSTEAddress(id2), null};
        Q.AddQuad(str_arr);
    }

    private void Generate(String s1, int id1, VariableEntry id2)
    {
        String[] str_arr = {s1, Integer.toString(id1), GetSTEPrefix(id2) + GetSTEAddress(id2), null};
        Q.AddQuad(str_arr);
    }
    
    private void Generate(String s1, SymbolTableEntry temp, SymbolTableEntry offset, SymbolTableEntry id)
    {
        String[] str_arr = {s1, GetSTEPrefix(temp) + GetSTEAddress(temp), GetSTEPrefix(offset) + GetSTEAddress(offset), 
                                GetSTEPrefix(id) + GetSTEAddress(id)};
        Q.AddQuad(str_arr);
    }
    
    private void Generate(String s1, SymbolTableEntry id, SymbolTableEntry offset, VariableEntry temp)
    {
        String[] str_arr = {s1, GetSTEPrefix(id) + GetSTEAddress(id), GetSTEPrefix(offset) + GetSTEAddress(offset),
                                GetSTEPrefix(temp) + GetSTEAddress(temp)};
        Q.AddQuad(str_arr);
    }
    
    //assigns a Symbol Table Entry an address
    private int GetSTEAddress(SymbolTableEntry ste)
    {
        if (ste instanceof ArrayEntry ||
           ste instanceof VariableEntry)
            return ste.GetAddress();
        
        if (ste instanceof ConstantEntry)
        {
            VariableEntry temp = Create("temp", ste.GetTokenType());
            Generate("move", ste.GetName(), temp);
            return temp.GetAddress();
        }
        
        return 0;
    }
    
    //determines the prefix of a Symbol Table Entry
    // _ , %, ^%, or %
    private String GetSTEPrefix(SymbolTableEntry ste)
    {
        if (Global)
            return "_";
        
        else //local
        {
            SymbolTableEntry entry = LocalTable.Lookup(ste.GetName());
            
            if (entry == null) //entry is a global variable
            {
                SymbolTableEntry entry2 = ConstantTable.Lookup(ste.GetName());
                
                if(entry2 == null)
                    return "_";
                
                else
                    return "%";
            }
            else //entry is a local variable
            {
                if(ste.IsParameter())
                    return "^%";
                
                else
                    return "%";
            } 
        }
    }
    
    // a function which returns the proper prefix for a parameter, to be used when 
    //generating code with the opcode "param".
    
    private String GetParamPrefix(SymbolTableEntry param)
    {
        if (Global)
            return "@_";
        
        else //local
        {
            if(param.IsParameter())
                return "%";
            
            else
                return "@%"; 
        }
    }
    
    //responsible for inserting a new variable entry
    //into the appropriate symbol table and associating
    //it with a valid memory address
    private VariableEntry Create(String name, String tokenType)
    {
        VariableEntry ve = new VariableEntry(name + TempCount, -1, tokenType);
        if (Global)
        {
            ve.SetAddress(GlobalMemory);
            GlobalMemory++;
            GlobalTable.Insert("temp" + TempCount, ve);
            TempCount++;
        }
        else //local
        {
            ve.SetAddress(LocalMemory);
            LocalMemory++;
            LocalTable.Insert("temp" + TempCount, ve);
            TempCount++;
        }  
        return ve;
    }
    
    //a typecheck function, which is responsible for checking the type
    //of two ids with the following specifications:
    //0    if id1 and id 2 are both integers
    //1    if id1 and id2 are both reals
    //2    if id1 is real and id2 is integer
    //3    if id1 is integer and id2 is real
    private int Typecheck(SymbolTableEntry id1, SymbolTableEntry id2)
    {
        
        String type1 = id1.GetTokenType();
        String type2 = id2.GetTokenType();
        
        if (type1.equalsIgnoreCase("INTEGER"))
        {
            if(type2.equalsIgnoreCase("INTEGER"))
                return 0;
            
            else
                return 3;
            
        }
        else if (type2.equalsIgnoreCase("INTEGER"))
            return 2;
        
        else
            return 1;
       
    }
    
    //inserts x in the second field of the quadruple at index i
    //in quadruples
    private void Backpatch(int i, int x)
    {
        Q.SetField(i,1,Integer.toString(x)); 
    }
    
    //a backpatch function which takes a list of integers as its first
    //argument (instead of just an integer as specified in phase 2)
    private void Backpatch(List<Integer> list, int x)
    {
        for (Integer i : list)
        {
            if (Q.GetField(i,0).equalsIgnoreCase("goto"))
                Q.SetField(i, 1, x+""); //set second field of the quad at i to x
            
            else //quadruples.getQuad(i) is a branch statement
                Q.SetField(i, 3, x+""); //set the fourth field fo the quad at i to x
            
        }         
    }
    
    //returns list1 and list2 concatenated together
    private List<Integer> Merge(List<Integer> list1, List<Integer> list2)
    {
        List<Integer> listFinal = new ArrayList<>();
        listFinal.addAll(list1);
        listFinal.addAll(list2);
        return listFinal;
    }
    
    //returns a list of one element
    private List<Integer> MakeList(int i)
    {
        List<Integer> oneElemList = new ArrayList<>();
        oneElemList.add(i);
        return oneElemList;
    }
    
    //Given a Token, find the Symbol Table Entry associated with it
    private SymbolTableEntry LookupID(Token id)
    {
        //first look in the local table
        SymbolTableEntry ste = LocalTable.Lookup(id.GetTokenValue());
        //if id is not in local table look in global table
        if (ste == null)
        {
           GlobalTable.DumpTable();
           ste = GlobalTable.Lookup(id.GetTokenValue());
        }
        return ste;
    }
    
    
    //Print the contents of the semStack
    private void SemStackDump()
    {
        Stack reverseStack = (Stack)SemStack.clone();
        Collections.reverse(reverseStack); 
        System.out.println("Stack ::==> " + reverseStack.toString());
    }
}
