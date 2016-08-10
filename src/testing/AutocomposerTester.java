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
		//testing all cases in composeCantusFirmus - works?
		
		Model m = new Model("C","Ionian",9);
		Composition c = new Composition(m);
		c.composeCantusFirmus();
		Note[] cantusFirmus = c.getCantusFirmus();
		for(int i = 0; i < cantusFirmus.length; i++) {
        	if(cantusFirmus[i] != null) {
        		System.out.println(i + "  " + cantusFirmus[i].toString());
        	}
        }
        
		
		//testing Case 1 in composeCantusFirmus - works
		/*
		Model m = new Model("D","Dorian",10);
		Composition c = new Composition(m);
		Note[] cantusFirmus = c.composeCantusFirmus();
		for(int i = 0; i < cantusFirmus.length; i++) {
        	if(cantusFirmus[i] != null) {
        		System.out.println(i + "  " + cantusFirmus[i].toString());
        	}
        }
        */
		
		//testing checkTriadValidity - works?
		/*
		Model m = new Model("A-flat", "Aeolian",10);
		Composition c = new Composition(m);
		Note tonic = new Note(m);
		Note bottom = new Note(tonic,-1,m);
		System.out.println(bottom.getNoteName());
		Note middle = new Note(tonic,1,m);
		System.out.println(middle.getNoteName());
		Note top = new Note(tonic,4,m);
		System.out.println(top.getNoteName());
		System.out.println(c.checkTriadValidity(bottom,middle,top,"first inversion"));
		*/
		
		//testing determineLeapToAdditionalInterval - works?
		/*
		Model m = new Model("B", "Dorian",10);
		Composition c = new Composition(m);
		Note tonic = new Note(m);
		Note lowPoint = new Note(tonic,-4,m);
		System.out.println(lowPoint.getNoteName());
		System.out.println(c.determineLeapToAdditionalInterval(tonic,lowPoint));
		*/
		
		//testing determineLeapToFPInterval - works?
		/*
		Model m = new Model("D", "Dorian",10);
		Composition c = new Composition(m);
		Note tonic = new Note(m);
		Note lowPoint = new Note(tonic,-1,m);
		System.out.println(lowPoint.getNoteName());
		System.out.println(c.determineLeapToFPInterval(tonic,lowPoint));
		*/
		
		//testing Note class constructors - works
		/*
		Model m = new Model("G", "Mixolydian",10);
		int[] relPitchArray = {0,2,3,4,2,0,1,0,-1,0};
		Note[] noteArray = new Note[10];
		noteArray[0] = new Note(m);
		for(int i = 1; i < relPitchArray.length; i++) {
			noteArray[i] = new Note(noteArray[0],relPitchArray[i],m);
		}
		for(int i = 0; i < relPitchArray.length; i++) {
			System.out.println(noteArray[i].toString());
		}
		*/
			
		
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
