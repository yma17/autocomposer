package autocomposer;

import autocomposer.NoteUtilities;

/* A Note object simply serves to represent a note in the music. It contains
 * intrinsic info about the note (pitch, octave, and note name)
 */
public class Note implements NotesAndKeys
{
    public int midiValue;
	public int pitch; // 0-11, 0 = C
	public int octave;
	public String noteName;
	public int relativePitch; //all notes a part of a piece of counterpoint; relativePitch = pitch in relation to that of tonic of CF
	public Note(Model m) { //compose first note of cantus firmus
		noteName = m.getKey(); //first note = tonic
		
		pitch = 0; //default value
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
		midiValue = (12 * this.octave) + this.pitch + 12;	
		relativePitch = 0;
	}
	public Note(int relativePitch,Model m) {
		//precondition: first note of CF (relativeNote) created already
		//creates note by comparing to first note of the line
		
		int arrayValue = (relativePitch+56)%7; //value of note in specific array
		//modulus - to avoid ArrayOutOfBoundsException
		//56 - to cover all octaves used in music
		noteName = m.getSpecificArray()[arrayValue];
		pitch = NoteUtilities.findPitch(m.getSpecificArray()[arrayValue],m.getKeyIsSharp());
		
		//adjust octave as necessary
		octave = m.getOctaveOfCF(); //octave of the tonic
		int octaveRelPitchDistance = 0; //distance of octaves in relative pitches. Will increase/decrease by 7 at a time with each octave.
	    if(relativePitch <= m.getOctaveDownValue()) { //if relativePitch is lower than the value needed to lower the octave, the octave must be lowered
	    	while(m.getOctaveDownValue() - relativePitch + octaveRelPitchDistance >= 7) {
	    		//to lower additional octaves if relativePitch is low enough
	    		octaveRelPitchDistance -= 7;
	    		octave--;
	    	}
	    	//lower one octave
	    	octave--;
		    octaveRelPitchDistance = 0;
	    }
	    else if(relativePitch >= m.getOctaveUpValue()) {
	    	while(relativePitch - m.getOctaveUpValue() - octaveRelPitchDistance >= 7) {
	    		octaveRelPitchDistance += 7;
	    		octave++;
	    	}
	    	octave++;
	    }
	    
		midiValue = (12 * this.octave) + this.pitch + 12;;		
		this.relativePitch = relativePitch;
	}
	public int midiValue() {
		return midiValue;
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
	public int getRelPitch() {
		return relativePitch;
	}
	public String toString() { //for testing
		return "MidiValue = " + midiValue + " Pitch = " + pitch + " Note name = " + noteName + " Octave = " + octave;
	}
}
