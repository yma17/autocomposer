package testing;

import java.util.Arrays;

import autocomposer.Composition;
import autocomposer.Model;
import autocomposer.Note;
import autocomposer.NotesAndKeys;

public class AutocomposerTester implements NotesAndKeys
{

	public static void main(String[] args)
	{
		Model m = new Model("C","Ionian",9);
		Composition composer = new Composition(m);
		
		/*
		int x = composer.findPitch(m.getKey()) + DIATONIC_INTERVALS[4];
		
		Note n = new Note(x, 4);
		System.out.println(n.midiValue());
		*/
		
		Note[] array = composer.composeCantusFirmus();
		
		for(int x = 0; x < array.length; x++) {
			if(array[x] != null) {
				System.out.println(x + " " + array[x].midiValue());
			}

		}
	}
}
