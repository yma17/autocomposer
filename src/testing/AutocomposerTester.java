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
		Model a = new Model("C","Ionian",9);
		Model b = new Model("D","Dorian",9);
		Model c = new Model("E","Phrygian",9);
		Model d = new Model("F","Lydian",9);
		Model e = new Model("G","Mixolydian",9);
		Model f = new Model("A","Aeolian",9);
		
		Composition composer1 = new Composition(a);
		Composition composer2 = new Composition(b);
		Composition composer3 = new Composition(c);
		Composition composer4 = new Composition(d);
		Composition composer5 = new Composition(e);
		Composition composer6 = new Composition(f);
		
		/*
		int x = composer.findPitch(m.getKey()) + DIATONIC_INTERVALS[4];
		
		Note n = new Note(x, 4);
		System.out.println(n.midiValue());
		*/
		
		//System.out.println(composer.findPitch("G-flat"));
		Note[] array1 = composer1.composeCantusFirmus();

		for(int x = 0; x < array1.length; x++) {
			if(array1[x] != null) {
				System.out.println(x + " " + array1[x].midiValue());
			}
		}
		
		System.out.println();
		Note[] array2 = composer2.composeCantusFirmus();
		for(int x = 0; x < array2.length; x++) {
			if(array2[x] != null) {
				System.out.println(x + " " + array2[x].midiValue());
			}
		}
		System.out.println();
		Note[] array3 = composer3.composeCantusFirmus();
		for(int x = 0; x < array3.length; x++) {
			if(array3[x] != null) {
				System.out.println(x + " " + array3[x].midiValue());
			}
		}
		System.out.println();
		Note[] array4 = composer4.composeCantusFirmus();
		for(int x = 0; x < array4.length; x++) {
			if(array4[x] != null) {
				System.out.println(x + " " + array4[x].midiValue());
			}
		}
		System.out.println();
		Note[] array5 = composer5.composeCantusFirmus();
		for(int x = 0; x < array5.length; x++) {
			if(array5[x] != null) {
				System.out.println(x + " " + array5[x].midiValue());
			}
		}
		System.out.println();
		Note[] array6 = composer6.composeCantusFirmus();
		
		for(int x = 0; x < array6.length; x++) {
			if(array6[x] != null) {
				System.out.println(x + " " + array6[x].midiValue());
			}
		}
	}
}
