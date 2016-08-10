package autocomposer;

import java.util.Arrays;

/* This class contains methods meant to serve as means of conversion, simplification, and search
 * for notes and note names in this entire project.
 */
public class NoteUtilities implements NotesAndKeys {
	public NoteUtilities() {
	}
	public static String convertToDouble(String currentNote, boolean convertUp) {
		//precondition: if convertUp = true; currentNote is not E or B
		//precondition: if convertUp = false; currentNote is not F or C
		String basicNote = currentNote.substring(0,1);
		int basicNoteIndex = Arrays.binarySearch(BASIC_NOTES, basicNote);
		if(convertUp) //convert basic note up
			return BASIC_NOTES[(basicNoteIndex + 1)%7] + "-double-flat";
		else
			return BASIC_NOTES[(basicNoteIndex - 1 + 7)%7] + "-double-sharp";
	}
	public static String convertOutOfDouble(String currentNote) { //converts a note name out of double accidental form. (e.g. B-double-flat to A.)
		//precondition: note contains "double-sharp" or "double-flat"
		String basicNote = currentNote.substring(0,1);
		int basicNoteIndex = Arrays.binarySearch(BASIC_NOTES, basicNote);
		if(currentNote.indexOf("sharp") >= 0) //convert basic note up
			return BASIC_NOTES[(basicNoteIndex + 1)%7];
		else
			return BASIC_NOTES[(basicNoteIndex - 1 + 7)%7];
	}
	public static String convertToComplexEnharmonic(String note) {
		//precondition: note is C,B,E,F
		//method will have no effect on any other notes
		String returnString = note;
		if(note.equals("C"))
			returnString = "B-sharp";
		else if(note.equals("B"))
			returnString = "C-flat";
		else if(note.equals("E"))
			returnString = "F-flat";
		else if(note.equals("F"))
			returnString = "E-sharp";
		
		return returnString;
	} 
	public static String convertToSimpleEnharmonic(String note) {
		//precondition: note is B-sharp, C-flat,F-flat,E-sharp
		//method will have no effect on any other notes
		String returnString = note;
		if(note.equals("B-sharp"))
			returnString = "C";
		else if(note.equals("C-flat"))
			returnString = "B";
		else if(note.equals("F-flat"))
			returnString = "E";
		else if(note.equals("E-sharp"))
			returnString = "F";
		
		return returnString;
	}
	public static int findPitch(String note, boolean keyIsSharp){ //finds pitch(0-11) of a given note
		//precondition: note is still unsimplified
		//precondition: keyIsSharp is consistent with note
		int pitch = 0;
		
		//simplify note name
		String thisNote = note;
        thisNote = NoteUtilities.convertToSimpleEnharmonic(note);

        if(thisNote.indexOf("double") >= 0)
        	thisNote = NoteUtilities.convertOutOfDouble(thisNote);

		if(keyIsSharp) {
			for(int i = 0; i < NOTES_SHARPS.length; i++) {
				if(NOTES_SHARPS[i].equals(thisNote))
					pitch = i;
			}
		}
		else {
			for(int i = 0; i < NOTES.length; i++) {
				if(NOTES[i].equals(thisNote))
					pitch = i;
			}
		}
	    return pitch;
	}
}
