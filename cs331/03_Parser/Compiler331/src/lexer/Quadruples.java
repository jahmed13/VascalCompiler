package lexer;

import java.util.Enumeration;
import java.util.Vector;

//Stores and manages the collection of Quadruple
//Quadruple Store
public class Quadruples
{
	private Vector<String[]> Quadruple;
	private int NextQuad;
	
	public Quadruples()
	{		
            Quadruple = new Vector<String[]>();
            NextQuad = 0;
            String[] dummy_quadruple = new String[4];
            dummy_quadruple[0] = dummy_quadruple[1] = dummy_quadruple[2] = dummy_quadruple[3] = null;
            Quadruple.add(NextQuad,dummy_quadruple);
            NextQuad++;
	}

        //return the element at the specified index and field
	public String GetField(int quadIndex, int field)
	{
            return Quadruple.elementAt(quadIndex)[field];
	}

        //set the element at the specified index to the String field
	public void SetField(int quadIndex, int index, String field)
	{
            Quadruple.elementAt(quadIndex)[index] = field;
	}

        //return the next quad value
	public int GetNextQuad()
	{
            return NextQuad;
	}
	
        //increment the next quad value
	public void IncrementNextQuad()
	{
            NextQuad++;
	}

        //return the quad at the specified index
	public String[] GetQuad(int index)
	{
            return (String []) Quadruple.elementAt(index);
	}

        //add a new quad to the a Quadruple collection
	public void AddQuad(String[] quad)
	{
            Quadruple.add(NextQuad, quad);
            NextQuad++;
	}

        //print the contents of a Quadruples
	public void Print()
	{
            int quadLabel = 1;
		//String separator;
		
            System.out.println("CODE");

            Enumeration<String[]> e = this.Quadruple.elements();
            e.nextElement();
		
            while (e.hasMoreElements())
            {
                String[] quad = e.nextElement();
		System.out.print(quadLabel + ":  " + quad[0]);

		if (quad[1] != null)
                    System.out.print(" " + quad[1]);

		if (quad[2] != null)
                    System.out.print(", " + quad[2]);

		if (quad[3] != null)
                    System.out.print(", " + quad[3]); 

		System.out.println();
		quadLabel++;
            }
	}
}
