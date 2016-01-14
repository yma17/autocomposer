package testing;

import autocomposer.Composition;
import autocomposer.Model;
import autocomposer.Note;

public class AutocomposerTester
{

	public static void main(String[] args)
	{
		Model m = new Model("D","Dorian",9);
		Composition composer = new Composition(m);
		
		int x = composer.findPitch(m.getKey());
		
		Note n = new Note(x, 4);
		System.out.println(n.midiValue());
	}

}
