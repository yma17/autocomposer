package testing;

import autocomposer.Model;
import autocomposer.Composer;
import autocomposer.Note;
import autocomposer.Composition;
import autocomposer.NotesAndKeys;

import java.util.ArrayList;

/* Tester class for this project.
 * 
 */
public class AutocomposerTester implements NotesAndKeys
{

	public static void main(String[] args)
	{	
		Model m = new Model("C","Ionian",10);
		Note a = new Note(m);
		Note b = new Note(1,m);
		Note c = new Note(2,m);
		Note d = new Note(3,m);
		Note e = new Note(4,m);
		ArrayList<Note> list = new ArrayList<Note>();
		list.add(a);
		list.add(b);
		list.add(c);
		list.add(d);
		list.add(e);
		Note f = c;
		list.remove(f);
		for(Note note : list) {
			System.out.println(note.toString());
		}
		System.out.println(list.size());
		/*
		Model m = new Model("C","Ionian",10);
		Composition cpt = new Composition(m);
		Composer c = new Composer(cpt);
		c.compose();
		Note[] cantusFirmus = cpt.getCantusFirmus();
		for(int i = 0; i < cpt.getLength(); i++) {
			Note thisNote = cantusFirmus[i];
			if(thisNote != null)
				System.out.println(thisNote.toString());
		}
		*/
	}
}
