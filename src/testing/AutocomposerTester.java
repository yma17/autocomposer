package testing;


import autocomposer.Composition;
import autocomposer.Model;
import autocomposer.Note;
import autocomposer.NoteUtilities;
import autocomposer.NotesAndKeys;

public class AutocomposerTester implements NotesAndKeys
{

	public static void main(String[] args)
	{
		Model m = new Model("D","Mixolydian",9);
		int[] arr = {0,-2,-1,-2,-4,3,2,0,1,0};
		Note[] arr2 = new Note[arr.length];
		for(int x = 0; x < arr2.length; x++) {
			arr2[x] = NoteUtilities.convertRelativeToNote(arr[x],m);
		}
		for(int x = 0; x < arr2.length;x++) {
			System.out.println(x + " " + arr2[x].getNoteName() + arr2[x].midiValue());
		}
		
		//Note[] arr3 = NoteUtilities.convertRelativeToNote(int[] relativePitches,String mode,String key,Model mod)
		
		/*
		int[] arr = new int[5];
		arr[0] = 0;
		for(int x = 1; x < arr.length; x++) {
			arr[x] = NoteUtilities.findPitchIndex(arr[x-1], -2);
			System.out.println(arr[x]);
		}
		*/
		
		/*
		Model x = new Model("C","Ionian",9);
		Composition z = new Composition(x);
		Note n = new Note(11,x);
		String s = n.getNoteName();
		System.out.println(s);
		int i = NoteUtilities.findMIDIInterval(x.getSpecificArray(),n,-5);
		System.out.println(i);
		*/
		/*
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
		
		*/
		
		/*
		String[] arr = x.getSpecificArray();
		for(int q = 0; q < arr.length; q++)
			System.out.println(arr[q]);
		*/
		
        
	}
}
