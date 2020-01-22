
package lexer;

//Stores lines of intermediate code as it is generated during parsing
public class Quadruple {
    
    //can have up to 4 values
    private String S0 = "";
    private String S1 = "";
    private String S2 = "";
    private String S3 = "";
    
    public Quadruple(String s0, String s1, String s2, String s3)
    {
        this.S0 = s0;
        this.S1 = s1;
        this.S2 = s2;
        this.S3 = s3;
    }
 
}
