//Jonayed Ahmed
package lexer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Stack;

/*The function of the parser is to ensure that the stream of tokens conforms to
rules of the language- ensure that the input is syntactically correct.
Consists of the Parser which calls the Lexer until ENDOFFILE is reached
RHS Table which contains list of productions for all non-terminals
Parse Table which is a matrix indexed by stack symbol (non-terminals and 
terminals) and the current symbol from the input stream (terminals only)*/
public class Parser {
    
    //HashMap that contains all tokens in order of input file
    private final HashMap<Integer, Token> TokenMap;
    
    //Hashmap that contains the production values of the parse table
    private final HashMap<TermAndNonTermPair, Integer> ParseTable = new HashMap<>(); 
    
    //The parse stack that contains the grammar
    private final Stack<String> ParseStack = new Stack();
    
    //boolean value that determines whether or not the current contents of the parse stack
    //are printed using DumpStack()
    //Stop printing of parse stack by setting value equal to false
    private final boolean PRINT_STACK = true;
    
    //counter for the phase of parsing, initialize to 0
    private int CurrentPhase = 0;
 
    private final String Eps = "EPSILON";
    
    //all the possible nonterminals
    //used with loadParseTable()
    private final String[] NonTerminals = {"<program>","<identifier-list>","<declarations>","<sub-declarations>","<compound-statement>","<identifier-list-tail>","<declaration-list>", "<type>",
                                           "<declaration-list-tail>","<standard-type>","<array-type>","<subprogram-declaration>","<subprogram-head>","<arguments>","<parameter-list>","<parameter-list-tail>",
                                           "<statement-list>","<statement>","<statement-list-tail>","<elementary-statement>","<expression>","<else-clause>","<es-tail>","<subscript>","<parameters>",
                                           "<expression-list>","<expression-list-tail>","<simple-expression>","<expression-tail>","<term>","<simple-expression-tail>","<sign>","<factor>","<term-tail>",
                                           "<factor-tail>","<actual-parameters>","<Goal>","<constant>"};
    
    //all the possible terminals
    //used with loadParseTable()
    private final String[] Terminals = {"program","begin","end","var","function","procedure","result","integer","real","array","of","if","then","else","while","do","not","identifier","intconstant",
                                        "realconstant","relop","mulop","addop","assignop","COMMA","SEMICOLON","COLON","LPAREN","RPAREN","LBRACKET","RBRACKET","UNARYMINUS","UNARYPLUS","DOUBLEDOT","ENDMARKER"};
    
    //all the possible productions
    //used with loadParseTable()
    private final int[] Productions = {1,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,65,999,
                                       999,999,-6,-16,25,999,999,999,-9,999,999,999,999,999,999,999,26,29,999,35,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,
                                       999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,-28,999,999,33,37,39,41,999,999,999,-47,999,-51,999,999,-54,60,62,999,999,
                                       999,999,5,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,
                                       999,999,-6,15,999,999,999,999,-9,999,999,17,18,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,
                                       999,999,-6,15,999,999,999,999,-9,999,999,17,19,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,
                                       999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,
                                       999,999,999,999,999,999,999,10,999,12,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,
                                       999,999,999,999,999,999,999,10,999,13,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,
                                       999,999,999,999,999,999,999,11,999,999,14,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,
                                       999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,
                                       999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,26,30,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,
                                       999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,39,999,999,999,999,-47,999,-51,999,999,-54,60,62,999,999,
                                       999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,32,37,39,41,999,999,999,-47,999,-51,999,999,-54,60,62,999,999,
                                       999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,26,31,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,
                                       999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,39,999,999,999,999,-47,999,-51,999,999,-54,60,62,999,999,
                                       999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,45,999,999,999,999,42,999,48,999,52,999,999,58,999,999,999,999,999,
                                       999,2,999,999,999,999,7,999,8,999,999,999,999,999,22,999,26,29,999,34,45,999,999,999,999,42,999,48,999,52,999,999,55,999,999,999,999,999,
                                       999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,45,999,999,999,999,42,999,48,999,52,999,999,56,999,999,999,999,66,
                                       999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,45,999,999,999,999,42,999,48,999,52,999,999,56,999,999,999,999,67,
                                       999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,39,999,999,999,999,46,999,-51,999,999,-54,60,62,999,999,
                                       999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,39,999,999,999,999,999,999,999,999,999,53,60,62,999,999,
                                       999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,39,999,999,999,999,999,999,50,999,999,-54,60,62,999,999,
                                       999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,36,39,999,999,999,999,999,999,999,999,999,999,999,999,999,999,
                                       999,999,999,999,999,3,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,39,999,999,43,999,-47,999,-51,999,999,-54,60,62,999,999,
                                       999,999,999,999,999,999,999,999,999,999,999,999,999,-21,999,23,999,999,27,999,999,33,37,39,41,999,999,999,-47,999,-51,999,999,-54,60,62,999,999,
                                       999,999,999,999,999,-4,999,999,999,999,999,999,999,-21,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,
                                       999,999,999,999,999,999,999,999,999,999,999,999,999,20,999,999,999,999,999,999,45,999,37,999,40,42,999,48,999,52,999,999,57,999,59,61,999,999,
                                       999,999,999,999,999,-4,999,999,999,999,999,999,999,999,999,-24,999,999,999,999,999,999,999,39,999,999,-44,999,-47,999,-51,999,999,-54,60,62,999,999,
                                       999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,36,38,999,999,999,999,999,999,999,999,999,999,60,999,999,999,
                                       999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,39,999,999,999,999,-47,999,-51,999,999,-54,60,62,999,999,
                                       999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,45,999,999,999,999,42,999,49,999,999,999,64,999,999,999,999,999,999,
                                       999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,45,999,999,999,999,42,999,49,999,999,999,63,999,999,999,999,999,999,
                                       999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,
                                       999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999,999};
    
    //An array list consisting of array lists that represent each production
    private final ArrayList<ArrayList<String>> GProductions = new ArrayList<>(67);
    
    //Global Semantic action object to call execute
    //also initializes the globalTable
    private final SemanticAction S = new SemanticAction();

    public Parser(HashMap<Integer, Token> tokenMap)
    {
        this.TokenMap = tokenMap;
        
        //initialize the stack with ENDOFFILE token and
        //distinguished symbol of grammar <Goal>
       
        ParseStack.push("ENDOFFILE");
        ParseStack.push("<Goal>");
    }
    
    //go through tokenMap until last token (EOF) is reached
    //and print the production
    public void goThroughTokenMap() throws SymbolTableError, SemanticActionError
    {
        
        //tokenMap starts at position 1
        
        for (int i = 1; i <= TokenMap.keySet().size(); i++)
        {
            System.out.println();
            CurrentPhase++;
            printPhase(i);
            
        }
        //reach this then ACCEPT
        System.out.println();
        
        //print the semantic action quadruples
        S.Q.Print();
        System.out.println("\n!  ACCEPT  !");
    }
    
    //Go through all the tokens and determine if either 
    //non-terminal, terminal, semantic action
    private void printPhase(int i) throws SymbolTableError, SemanticActionError
    {
           //the phase of parsing we are at
           System.out.println(">>-  " + CurrentPhase + "  -<<");
           
           //print contents of stack, set PRINT_STACK to false to turn off
           if (PRINT_STACK)
               DumpStack();
           
           
           //get the current token and compare it to the symbol on top of stack
           Token currToken = TokenMap.get(i);
           String currStackSym = ParseStack.peek();
           String currTokenType = currToken.GetTokenType();
           
           //NONTERMINAL STACK SYM
           if (currStackSym.charAt(0)=='<')
               getProductionRule(i, currStackSym, currTokenType);
           
           //SEMANTIC ACTION NUM
           else if (currStackSym.charAt(0)=='#') 
           {
               //call getActionIndex with the currStackSym to get the number
               //without the #
               //Send the previous token as well with (i-1)
               S.Execute(getActionIndex(currStackSym), TokenMap.get(i-1));
               ParseStack.pop();
           
               System.out.println("Popped " + currStackSym + " with token "
                                    + currTokenType + " -> # SEMANTIC ACTION # " + "[ " + currStackSym.substring(1) + " ]" );
               
               System.out.println();
               CurrentPhase++;
               printPhase(i);
          
           }
           //TERMINAL STACK SYM
           else 
           {
               
               //terminal and matches the current input symbol the stack top is popped
               //and the parser requests another input token
               if (currStackSym.equalsIgnoreCase(currTokenType))
               {
                   ParseStack.pop();
                   System.out.println("Popped " + currStackSym + " with token " 
                                       + currTokenType + " -> * MATCH *"
                                       + " {consume tokens}");
               }
               else //ERROR MESSAGE stack symbol doesnt match with language
               {
                   System.out.println("ERROR stack symbol doesnt match w language\n" + 
                                      "CURR TOKEN: " + currTokenType + 
                                       "\nCURR STACK SYM:  " + currStackSym);
                   System.exit(0);
               }
               
           }   
           
    }
    
    //use substring to get rid of the # and then parse the String to get int
    private int getActionIndex(String currStackSym)
    {
        String semNum = currStackSym.substring(1);
        return Integer.parseInt(semNum);
    }
    
    //create copy of stack, reverse stack, and then print contents
    private void DumpStack()
    {
        Stack<String> reverseStack = (Stack<String>)ParseStack.clone();
        Collections.reverse(reverseStack); 
        System.out.println("Stack ::==> " + reverseStack.toString());
    }
    
    //using the TermAndNonTermPair, get the productionRule value
    private void getProductionRule(int i, String currStackSym, String currTokenType) throws SymbolTableError, SemanticActionError
    {
        int productionRule;
        
        for (TermAndNonTermPair key: ParseTable.keySet())
        {
            if (key.GetNonTerminal().equalsIgnoreCase(currStackSym) &&
               key.GetTerminal().equalsIgnoreCase(currTokenType))
            {
                productionRule = ParseTable.get(key);
                printNonTerminal(i, productionRule, currStackSym, currTokenType);
            }
        }  
        
    }
    
    //print nonterminal based on the value from parseTable
    private void printNonTerminal(int i, int productionRule, String currStackSym, String currTokenType) throws SymbolTableError, SemanticActionError
    {
        if (productionRule == 999) //error
        {
            System.out.println("999 ERROR");
            System.out.println("Shouldn't have this token: " + currTokenType +
                                " because it doesn't work with the current stack symbol: "
                                + currStackSym);
            System.exit(0);
        }
        else if (productionRule < 0) //epsilon
        {
            CurrentPhase++;
            
            ParseStack.pop();
            
            System.out.println("Popped " + currStackSym + " with token " 
                                       + currTokenType + " -> @ " + Eps + " @  "
                                       + "  [ " + Math.abs(productionRule) + " ]" 
                                       + currStackSym + " ::= @ " 
                                       + Eps + " @ ");
            
            System.out.println();

            
            printPhase(i);
            
        }
        else //production rule
        {
            CurrentPhase++;
            
            ParseStack.pop();
            
            ArrayList<String> ntProductions = GProductions.get(productionRule - 1);
            Collections.reverse(ntProductions);
            
            for (String s: ntProductions)
                ParseStack.push(s);
            
            
            Collections.reverse(ntProductions);
            
            System.out.print("Popped " + currStackSym + " with token " 
                                       + currTokenType + " -> $ PUSH $"
                                       + "  [ " + productionRule + " ] " 
                                       + currStackSym + " ::= " );
            
            System.out.println(ntProductions);
            
            System.out.println();

            printPhase(i);
        }   
    }
    
    //initializes ParseTable
    public void loadParseTable()
    {
        int productionsPos = 0;
        
        for (String t: Terminals)
        {
            for (String nt: NonTerminals)
            {
                ParseTable.put(new TermAndNonTermPair(nt,t), Productions[productionsPos]);
                productionsPos++;
            }
        } 
    }
    
    //initializes gProductions
    public void loadGrammar()
    {
        //the 67 possible productions
        
        //production 1
        ArrayList<String> p1 = new ArrayList<>();
        p1.add("program");
        p1.add("identifier");
        p1.add("#13");
        p1.add("LPAREN");
        p1.add("<identifier-list>");
        p1.add("RPAREN");
        p1.add("#9");
        p1.add("SEMICOLON");
        p1.add("<declarations>");
        p1.add("<sub-declarations>");
        p1.add("#56");
        p1.add("<compound-statement>");
        p1.add("#55");
        GProductions.add(p1);
        
        //production 2
        ArrayList<String> p2 = new ArrayList<>();
        p2.add("identifier");
        p2.add("#13");
        p2.add("<identifier-list-tail>");
        GProductions.add(p2);
        
        //production 3
        ArrayList<String> p3 = new ArrayList<>();
        p3.add("COMMA");
        p3.add("identifier");
        p3.add("#13");
        p3.add("<identifier-list-tail>");
        GProductions.add(p3);
        
        //production 4 
        ArrayList<String> p4 = new ArrayList<>();
        p4.add(Eps);
        GProductions.add(p4);
        
        //production 5
        ArrayList<String> p5 = new ArrayList<>();
        p5.add("var");
        p5.add("#1");
        p5.add("<declaration-list>");
        p5.add("#2");
        GProductions.add(p5);
        
        //production 6
        ArrayList<String> p6 = new ArrayList<>();
        p6.add(Eps);
        GProductions.add(p6);
        
        //production 7
        ArrayList<String> p7 = new ArrayList<>();
        p7.add("<identifier-list>");
        p7.add("COLON");
        p7.add("<type>");
        p7.add("#3");
        p7.add("SEMICOLON");
        p7.add("<declaration-list-tail>");
        GProductions.add(p7);
        
        //production 8
        ArrayList<String> p8 = new ArrayList<>();
        p8.add("<identifier-list>");
        p8.add("COLON");
        p8.add("<type>");
        p8.add("#3");
        p8.add("SEMICOLON");
        p8.add("<declaration-list-tail>");
        GProductions.add(p8);
        
        //production 9
        ArrayList<String> p9 = new ArrayList<>();
        p9.add(Eps);
        GProductions.add(p9);
        
        //production 10
        ArrayList<String> p10 = new ArrayList<>();
        p10.add("<standard-type>");
        GProductions.add(p10);
        
        //production 11
        ArrayList<String> p11 = new ArrayList<>();
        p11.add("<array-type>");
        GProductions.add(p11);
    
        //production 12
        ArrayList<String> p12 = new ArrayList<>();
        p12.add("integer");
        p12.add("#4");
        GProductions.add(p12);
        
        //production 13
        ArrayList<String> p13 = new ArrayList<>();
        p13.add("real");
        p13.add("#4");
        GProductions.add(p13);
        
        //production 14
        ArrayList<String> p14 = new ArrayList<>();
        p14.add("#6");
        p14.add("array");
        p14.add("LBRACKET");
        p14.add("intconstant");
        p14.add("#7");
        p14.add("DOUBLEDOT");
        p14.add("intconstant");
        p14.add("#7");
        p14.add("RBRACKET");
        p14.add("of");
        p14.add("<standard-type>");
        GProductions.add(p14);
        
        //production 15
        ArrayList<String> p15 = new ArrayList<>();
        p15.add("<subprogram-declaration>");
        p15.add("<sub-declarations>");
        GProductions.add(p15);
        
        //production 16
        ArrayList<String> p16 = new ArrayList<>();
        p16.add(Eps);
        GProductions.add(p16);
        
        //production 17
        ArrayList<String> p17 = new ArrayList<>();
        p17.add("#1");
        p17.add("<subprogram-head>");
        p17.add("<declarations>");
        p17.add("#5");
        p17.add("<compound-statement>");
        p17.add("#11");
        GProductions.add(p17);
        
        //production 18
        ArrayList<String> p18 = new ArrayList<>();
        p18.add("function");
        p18.add("identifier");
        p18.add("#15");
        p18.add("<arguments>");
        p18.add("COLON");
        p18.add("result");
        p18.add("<standard-type>");
        p18.add("SEMICOLON");
        p18.add("#16");
        GProductions.add(p18);
        
        //production 19
        ArrayList<String> p19 = new ArrayList<>();
        p19.add("procedure");
        p19.add("identifier");
        p19.add("#17");
        p19.add("<arguments>");
        p19.add("SEMICOLON");
        GProductions.add(p19);
        
        //production 20
        ArrayList<String> p20 = new ArrayList<>();
        p20.add("LPAREN");
        p20.add("#19");
        p20.add("<parameter-list>");
        p20.add("RPAREN");
        p20.add("#20");
        GProductions.add(p20);
        
        //production 21
        ArrayList<String> p21 = new ArrayList<>();
        p21.add(Eps);
        GProductions.add(p21);
        
        //production 22
        ArrayList<String> p22 = new ArrayList<>();
        p22.add("<identifier-list>");
        p22.add("COLON");
        p22.add("<type>");
        p22.add("#21");
        p22.add("<parameter-list-tail>");
        GProductions.add(p22);
        
        //production 23
        ArrayList<String> p23 = new ArrayList<>();
        p23.add("SEMICOLON");
        p23.add("<identifier-list>");
        p23.add("COLON");
        p23.add("<type>");
        p23.add("#21");
        p23.add("<parameter-list-tail>");
        GProductions.add(p23);
        
        //production 24
        ArrayList<String> p24 = new ArrayList<>();
        p24.add(Eps);
        GProductions.add(p24);
        
        //production 25
        ArrayList<String> p25 = new ArrayList<>();
        p25.add("begin");
        p25.add("<statement-list>");
        p25.add("end");
        GProductions.add(p25);
        
        //production 26
        ArrayList<String> p26 = new ArrayList<>();
        p26.add("<statement>");
        p26.add("<statement-list-tail>");
        GProductions.add(p26);
        
        //production 27
        ArrayList<String> p27 = new ArrayList<>();
        p27.add("SEMICOLON");
        p27.add("<statement>");
        p27.add("<statement-list-tail>");
        GProductions.add(p27);
        
        //production 28
        ArrayList<String> p28 = new ArrayList<>();
        p28.add(Eps);
        GProductions.add(p28);
        
        //production 29
        ArrayList<String> p29 = new ArrayList<>();
        p29.add("<elementary-statement>");
        GProductions.add(p29);
        
        //production 30
        ArrayList<String> p30 = new ArrayList<>();
        p30.add("if");
        p30.add("<expression>");
        p30.add("#22");
        p30.add("then");
        p30.add("<statement>");
        p30.add("<else-clause>");
        GProductions.add(p30);
        
        //production 31
        ArrayList<String> p31 = new ArrayList<>();
        p31.add("while");
        p31.add("#24");
        p31.add("<expression>");
        p31.add("#25");
        p31.add("do");
        p31.add("<statement>");
        p31.add("#26");
        GProductions.add(p31);
        
        //production 32
        ArrayList<String> p32 = new ArrayList<>();
        p32.add("else");
        p32.add("#27");
        p32.add("<statement>");
        p32.add("#28");
        GProductions.add(p32);
        
        //production 33
        ArrayList<String> p33 = new ArrayList<>();
        p33.add("#29");
        GProductions.add(p33);
        
        //production 34
        ArrayList<String> p34 = new ArrayList<>();
        p34.add("identifier");
        p34.add("#30");
        p34.add("<es-tail>");
        GProductions.add(p34);
        
        //production 35
        ArrayList<String> p35 = new ArrayList<>();
        p35.add("<compound-statement>");
        GProductions.add(p35);
        
        //production 36
        ArrayList<String> p36 = new ArrayList<>();
        p36.add("#53");
        p36.add("<subscript>");
        p36.add("assignop");
        p36.add("<expression>");
        p36.add("#31");
        GProductions.add(p36);
        
        //production 37
        ArrayList<String> p37 = new ArrayList<>();
        p37.add("#54");
        p37.add("<parameters>");
        GProductions.add(p37);
        
        //production 38
        ArrayList<String> p38 = new ArrayList<>();
        p38.add("#32");
        p38.add("LBRACKET");
        p38.add("<expression>");
        p38.add("RBRACKET");
        p38.add("#33");
        GProductions.add(p38);
        
        //production 39
        ArrayList<String> p39 = new ArrayList<>();
        p39.add("#34");
        GProductions.add(p39);
        
        //production 40
        ArrayList<String> p40 = new ArrayList<>();
        p40.add("#35");
        p40.add("LPAREN");
        p40.add("<expression-list>");
        p40.add("RPAREN");
        p40.add("#51");
        GProductions.add(p40);
        
        //production 41
        ArrayList<String> p41 = new ArrayList<>();
        p41.add("#36");
        GProductions.add(p41);
        
        //production 42
        ArrayList<String> p42 = new ArrayList<>();
        p42.add("<expression>");
        p42.add("#37");
        p42.add("<expression-list-tail>");
        GProductions.add(p42);
        
        //production 43
        ArrayList<String> p43 = new ArrayList<>();
        p43.add("COMMA");
        p43.add("<expression>");
        p43.add("#37");
        p43.add("<expression-list-tail>");
        GProductions.add(p43);
        
        //production 44
        ArrayList<String> p44 = new ArrayList<>();
        p44.add(Eps);
        GProductions.add(p44);
        
        //production 45
        ArrayList<String> p45 = new ArrayList<>();
        p45.add("<simple-expression>");
        p45.add("<expression-tail>");
        GProductions.add(p45);
        
        //production 46
        ArrayList<String> p46 = new ArrayList<>();
        p46.add("relop");
        p46.add("#38");
        p46.add("<simple-expression>");
        p46.add("#39");
        GProductions.add(p46);
        
        //production 47
        ArrayList<String> p47 = new ArrayList<>();
        p47.add(Eps);
        GProductions.add(p47);
        
        //production 48
        ArrayList<String> p48 = new ArrayList<>();
        p48.add("<term>");
        p48.add("<simple-expression-tail>");
        GProductions.add(p48);
        
        //production 49
        ArrayList<String> p49 = new ArrayList<>();
        p49.add("<sign>");
        p49.add("#40");
        p49.add("<term>");
        p49.add("#41");
        p49.add("<simple-expression-tail>");
        GProductions.add(p49);
        
        //production 50
        ArrayList<String> p50 = new ArrayList<>();
        p50.add("addop");
        p50.add("#42");
        p50.add("<term>");
        p50.add("#43");
        p50.add("<simple-expression-tail>");
        GProductions.add(p50);
        
        //production 51
        ArrayList<String> p51 = new ArrayList<>();
        p51.add(Eps);
        GProductions.add(p51);
        
        //production 52
        ArrayList<String> p52 = new ArrayList<>();
        p52.add("<factor>");
        p52.add("<term-tail>");
        GProductions.add(p52);
        
        //production 53
        ArrayList<String> p53 = new ArrayList<>();
        p53.add("mulop");
        p53.add("#44");
        p53.add("<factor>");
        p53.add("#45");
        p53.add("<term-tail>");
        GProductions.add(p53);
        
        //production 54
        ArrayList<String> p54 = new ArrayList<>();
        p54.add(Eps);
        GProductions.add(p54);
        
        //production 55
        ArrayList<String> p55 = new ArrayList<>();
        p55.add("identifier");
        p55.add("#46");
        p55.add("<factor-tail>");
        GProductions.add(p55);
        
        //production 56
        ArrayList<String> p56 = new ArrayList<>();
        p56.add("<constant>");
        p56.add("#46");
        GProductions.add(p56);
        
        //production 57
        ArrayList<String> p57 = new ArrayList<>();
        p57.add("LPAREN");
        p57.add("<expression>");
        p57.add("RPAREN");
        GProductions.add(p57);
        
        //production 58
        ArrayList<String> p58 = new ArrayList<>();
        p58.add("not");
        p58.add("<factor>");
        p58.add("#47");
        GProductions.add(p58);
        
        //production 59
        ArrayList<String> p59 = new ArrayList<>();
        p59.add("<actual-parameters>");
        GProductions.add(p59);
        
        //production 60
        ArrayList<String> p60 = new ArrayList<>();
        p60.add("<subscript>");
        p60.add("#48");
        GProductions.add(p60);
        
        //production 61
        ArrayList<String> p61 = new ArrayList<>();
        p61.add("#49");
        p61.add("LPAREN");
        p61.add("<expression-list>");
        p61.add("RPAREN");
        p61.add("#50");
        GProductions.add(p61);
        
        //production 62
        ArrayList<String> p62 = new ArrayList<>();
        p62.add("#52");
        GProductions.add(p62);
        
        //production 63
        ArrayList<String> p63 = new ArrayList<>();
        p63.add("unaryplus");
        GProductions.add(p63);
        
        //production 64
        ArrayList<String> p64 = new ArrayList<>();
        p64.add("unaryminus");
        GProductions.add(p64);
        
        //production 65
        ArrayList<String> p65 = new ArrayList<>();
        p65.add("<program>");
        p65.add("endmarker");
        GProductions.add(p65);
        
        //production 66
        ArrayList<String> p66 = new ArrayList<>();
        p66.add("intconstant");
        GProductions.add(p66);
        
        //production 67
        ArrayList<String> p67 = new ArrayList<>();
        p67.add("realconstant");
        GProductions.add(p67);
    }
}
