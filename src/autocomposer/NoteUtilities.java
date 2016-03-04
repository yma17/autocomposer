package autocomposer;

import java.util.Arrays;

public class NoteUtilities implements NotesAndKeys {
	public NoteUtilities() {
	}
	public static String convertToDouble(String note, Model m, boolean sharp) { //precondition- requires to be doubled
		String output = note;
		if(sharp)
			for(int x = 0; x < m.getSpecificArray().length; x++) {
				String ch = m.getSpecificArray()[x].substring(0,1);
				if(sharp)
					output = ch + "-double-sharp";
				else
					output = ch + "-double-flat";
			}
		return output;
	}
	public static String convertOutOfDouble(String note) { //converts a note name out of double accidental form. (e.g. B-double-flat to A.)
		String str = note.substring(0,1);
    	String returnString;
    	if(note.indexOf("sharp")>=0)
    		returnString = BASIC_NOTES[(Arrays.binarySearch(BASIC_NOTES, str) + 1)%7];
    	else {
    		int i = Arrays.binarySearch(BASIC_NOTES, str) - 1;
    		if(i < 0)
    			i += 7;
    		returnString = BASIC_NOTES[i];
    	}
    	return returnString;
	}
	public static String convertToEnharmonic(String note) {
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
	public static String simplifyEnharmonic(String note) {
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
	public static int findPitch(String note){ //finds pitch(0-11) of a given note
    	int pitch = 0;
    	for(int x = 0; x < NOTES.length; x++) {
    		if(note.equals(NOTES[x]))
    			pitch = x;
    	}
    	return pitch;
	}
	public static int findMIDIInterval(String[] specificArray, Note previous, int interval) {
    	int x = 0;
    	String[] arr = specificArray;
    	String lastNoteName = previous.getNoteName();
    	
    	System.out.println(lastNoteName);
    	
    	for(int a = 0; a < arr.length; a++) {
    		if(arr[a].equals(lastNoteName))
    			x = a;
    	}
    	
    	//determine note name of desired note
    	int z;
    	if(interval < 0)
    		z = x + (interval + 1);
    	else
    		z = x + (interval - 1);
    	boolean cycle = false; //around the ends of the array
    	boolean bottomToTop = false; //nature of the cycle, if any
    	if(z < 0 || z >= 7) {
    		if(z < 0) {
    			z += 7;
    			bottomToTop = true;
    		}
    		cycle = true;
    	}
    	String thisNoteName = arr[z%7];
    	
    	System.out.println(thisNoteName);
    	
    	if(lastNoteName.indexOf("double") >= 0)
    		lastNoteName = NoteUtilities.convertOutOfDouble(lastNoteName);
    	else if(thisNoteName.indexOf("double") >= 0)
    		thisNoteName = NoteUtilities.convertOutOfDouble(thisNoteName);
    	
    	lastNoteName = NoteUtilities.simplifyEnharmonic(lastNoteName);
    	thisNoteName = NoteUtilities.simplifyEnharmonic(thisNoteName);

    	System.out.println(x);
    	System.out.println(z);
    	System.out.println(cycle);
    	System.out.println(bottomToTop);
    	System.out.println(lastNoteName);
    	System.out.println(thisNoteName);
    	
    	//check octaves
    	if(cycle) {
    		if(bottomToTop)
    			return ((NoteUtilities.findNoteIndex(thisNoteName)) - (NoteUtilities.findNoteIndex(lastNoteName))) - 12;
    		else
    			return 12-((NoteUtilities.findNoteIndex(lastNoteName)) - (NoteUtilities.findNoteIndex(thisNoteName)));
    		/*
    		else {
    			if(b2 > 0)
    				return this.findNoteIndex(ch1)+ b2 - (this.findNoteIndex(ch2)+b1); //a
    			else
    				return (this.findNoteIndex(ch2)+ b2) - (this.findNoteIndex(ch1)+b1);
    				
    		}
    		*/
    	}
    	else {
    		if(interval < 0) {
    			if((NoteUtilities.findNoteIndex(thisNoteName)) > (NoteUtilities.findNoteIndex(lastNoteName)))
    				return (NoteUtilities.findNoteIndex(thisNoteName)) - (NoteUtilities.findNoteIndex(lastNoteName)+12); //b
    		}
    		if(interval > 0) {
    			if((NoteUtilities.findNoteIndex(thisNoteName)) < (NoteUtilities.findNoteIndex(lastNoteName)))
    				return (NoteUtilities.findNoteIndex(thisNoteName)+12) - (NoteUtilities.findNoteIndex(lastNoteName));
    		}
    		return (NoteUtilities.findNoteIndex(thisNoteName)) - (NoteUtilities.findNoteIndex(lastNoteName));
    	}
	}
	public static int findNoteIndex(String note) { //finds index of a note on the NOTES array
    	int a = 0;
    	if(note.indexOf("sharp") >= 0) {
    		for(int x = 0; x < NOTES_SHARPS.length; x++) {
    			if(NOTES_SHARPS[x].equals(note))
    				a = x;
    		}
    	}
    	else {
    		for(int x = 0; x < NOTES.length; x++) {
    			if(NOTES[x].equals(note))
    				a = x;
    		}
    	}
    	return a;
    }
	
	public static int findPitchIndex(int lastIndex, int interval) {
		int relativeIndex = interval + 1;
		return lastIndex + relativeIndex;
	}
	
	public static Note convertRelativeToNote(int relativePitch,Model mod) {
		String mode = mod.getMode();
		String key = mod.getKey();
		
		Note returnNote;
		
		int[] m;
		if(mode.equals("Ionian"))
			m = IONIAN_DIATONIC_INTERVALS;
		else if(mode.equals("Dorian"))
			m = DORIAN_DIATONIC_INTERVALS;
		else if(mode.equals("Phrygian"))
			m = PHRYGIAN_DIATONIC_INTERVALS;
		else if(mode.equals("Lydian"))
			m = LYDIAN_DIATONIC_INTERVALS;
		else if(mode.equals("Mixolydian"))
			m = MIXOLYDIAN_DIATONIC_INTERVALS;
		else //Aeolian
			m = AEOLIAN_DIATONIC_INTERVALS;
		
		int octave = 4;
		if(relativePitch < 0) {
			relativePitch += 7;
			octave--;
		}
		else if(relativePitch >= 7) {
			relativePitch -= 7;
			octave++;
		}
		
		returnNote = new Note(m[relativePitch]+findNoteIndex(key),mod,octave);
		
		return returnNote;
	}
	public static String simplifyNoteName(String s) {
		String returnString = s;
		
		//simplify double
		if(s.indexOf("double") >= 0) {
			String basicNote = s.substring(0,1);
			int index = Arrays.binarySearch(BASIC_NOTES,basicNote);
			if(s.indexOf("-double-sharp") >= 0)
				returnString = BASIC_NOTES[(index+1)%7];
			else if(s.indexOf("-double-flat") >= 0) {
				if(index == 0)
					index += 7;
				returnString = BASIC_NOTES[index - 1];
			}
		}
		
		//simplify enharmonic
		if(s.equals("B-sharp"))
			returnString = "C";
		else if(s.equals("C-flat"))
			returnString = "B";
		else if(s.equals("F-flat"))
			returnString = "E";
		else if(s.equals("E-sharp"))
			returnString = "F";
		
		return returnString;
	}
}
