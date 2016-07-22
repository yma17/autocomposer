package autocomposer;

import java.util.Arrays;

import autocomposer.NoteUtilities;

/* A Note object simply serves to represent a note in the music. It contains
 * intrinsic info about the note (pitch, octave, and note name)
 */
public class Note implements NotesAndKeys
{
    public int midiValue;
	public int pitch; // 0-11, 0 = C
	public int octave; // 0-N
	public String noteName;
	public Note(Model m) { //compose first note of cantus firmus
		noteName = m.getKey();
		
		pitch = 0;
		if(m.getKeyIsSharp()) {
			for(int i = 0; i < NOTES_SHARPS.length; i++) {
				if(NOTES_SHARPS[i].equals(noteName))
					pitch = i;
			}
		}
		else {
			for(int i = 0; i < NOTES.length; i++) {
				if(NOTES[i].equals(noteName))
					pitch = i;
			}
		}

		octave = m.getOctaveOfCF();
		midiValue = midiValue();	
	}
	public Note(Note firstNote,int relativePitch,Model m) {
		//precondition: first note of CF (relativeNote) created already
		//creates note by comparing to first note of the line
		
		int arrayValue = (relativePitch+28)%7; //value of note in specific array
		//21 = note won't wander more than four octaves of relativeNote
		noteName = m.getSpecificArray()[arrayValue];
		pitch = NoteUtilities.findPitch(m.getSpecificArray()[arrayValue],m.getKeyIsSharp());
		
		//adjust octave as necessary
		octave = firstNote.getOctave();
		//System.out.println(octave); //for testing
		int octaveDownPitch = 0;
	    boolean stop = false;
	    int i = 6;
    	//System.out.println("reached"); //for testing
	    while(!stop) {
	    	//System.out.println("executing"); //for testing
	    	octaveDownPitch--;
	    	int thisPitch = NoteUtilities.findPitch(m.getSpecificArray()[i],m.getKeyIsSharp());
	    	if(thisPitch >= 10) {
	    		stop = true;
	    	}
	    	i--;
	    }
	    
    	//two exceptions: B key, Ionian and Lydian mode. The 2nd note of the scale
	    if(m.getKey().equals("B")) {
	    	if(m.getMode().equals("Ionian") || m.getMode().equals("Lydian")) {
	   			octaveDownPitch = -6;
	   			if(arrayValue == 1)
	   				octave++;
	    	}
	    }
	    
	    int octaveUpPitch = octaveDownPitch + 7;

	    if(relativePitch <= octaveDownPitch)
	    	octave--;
	    else if(relativePitch > octaveUpPitch)
	    	octave++;
	    
	    
		midiValue = midiValue();		
	}
	public int midiValue() {
		return (12 * this.octave) + this.pitch + 12;
	}
	public int getPitch() {
		return pitch;
	}
	public String getNoteName() {
		return noteName;
	}
	public int getOctave() {
		return octave;
	}
	public String toString() { //for testing
		return "MidiValue = " + midiValue + " Pitch = " + pitch + " Note name = " + noteName + " Octave = " + octave;
	}
}
