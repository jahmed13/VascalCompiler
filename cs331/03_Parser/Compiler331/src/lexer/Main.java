//Jonayed Ahmed
package lexer;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

//Creates and initializes a Compiler object
//Passes control to the Compiler object
//Contains the main() function

public class Main 
{
    
    public static void main(String[] args) throws IOException, SymbolTableError, SemanticActionError
    {
        
        //hardcoded filename for purposes of testing
        Scanner sc = null;
        
        //*****************************************************
        //input your filename here ---------------------
        //                                              |
        //                                              |
        //                                              V
        sc = new Scanner(new File("/home/jahmed/Desktop/cs331/03_Parser/phase2-1.txt"));
        
        //***********************************************************
        //don't forget to also input filename in SemanticAction class 
        //***********************************************************
        
        //put all file contents into a String
        String fileText = " " + sc.useDelimiter("\\A").next().toLowerCase();
       
        sc.close();
        
        //Create a new Compiler object and start the compiler
        Compiler c = new Compiler(fileText);
        c.runCompiler(); 
    }
}
