package testing;


import autocomposer.Composition;
import autocomposer.Model;
import autocomposer.Note;
import autocomposer.NoteUtilities;
import autocomposer.NotesAndKeys;
import autocomposer.SoundPlayer;

public class AutocomposerTester implements NotesAndKeys
{

	public static void main(String[] args)
	{
		//testing Note class constructors - works
		
		Model m = new Model("G", "Phrygian",10);
		int[] relPitchArray = {0,2,4,8,10,-8,-6,-4,-2,0};
		Note[] noteArray = new Note[10];
		noteArray[0] = new Note(m);
		for(int i = 1; i < relPitchArray.length; i++) {
			noteArray[i] = new Note(noteArray[0],relPitchArray[i],m);
		}
		for(int i = 0; i < relPitchArray.length; i++) {
			System.out.println(noteArray[i].toString());
		}
		
			
		
		//testing findPitch - works
		/*
		int pitch = NoteUtilities.findPitch("B",true);
		System.out.println(pitch);
		*/
		
		//testing getSpecificArray - works
		/*
		Model m = new Model("A-sharp","Lydian",10); //F-flat should be E		

		for(int x = 0; x < 7; x++)
			System.out.println(m.getSpecificArray()[x]);
		
		*/
			
	}
}
