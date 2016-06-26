package autocomposer;

import java.util.Arrays;

import autocomposer.NoteUtilities;

public class Note implements NotesAndKeys
{
	
//	public int midiValue;
	public int pitch; // 0-11, 0 = C
	public int octave; // 0-N
	public String noteName;
	public Note(int pitch,Model m,int octave) //notes composed in the context of a specific key and mode
	{
		this.pitch = pitch;
		this.octave = octave;
		while(pitch < 0) {
			pitch += 12;
			octave--;
		}
		while(pitch >= 12) {
			pitch -= 12;
			octave++;
		}
		
		noteName = this.determineNoteName(m,pitch);
	}
	
	public Note(int pitch, Model m) {
		this(pitch,m, 4); //4 - default octave
	}
	
	public int midiValue() {
		return (12 * this.octave) + this.pitch + 12;
	}
	public int getPitch() {
		return pitch;
	}
	public String determineNoteName(Model m, int pitch) {
		String name;
		
		//System.out.println(pitch); //for testing purposes
		
		//initial note name
		if(m.getKeyIsSharp())
			name = NOTES_SHARPS[pitch];
		else
			name = NOTES[pitch];
		
		boolean found = false;
		for(int x = 0; x < m.getSpecificArray().length; x++) { //search for note name in specific array
			if(m.getSpecificArray()[x].equals(name)){
				found = true;
				break;
			}
		}
		if(!found) {
			//simplify specific array
			String[] simplified = new String[m.getSpecificArray().length];
			
			for(int x = 0; x < m.getSpecificArray().length; x++) {
				simplified[x] = NoteUtilities.simplifyNoteName(m.getSpecificArray()[x]);
				if(simplified[x].equals(name)) {
					name = m.getSpecificArray()[x];
					break;
				}
			}
		}
		
        /*
		if(name.indexOf("flat") < 0 && name.indexOf("sharp") < 0) {
			for(int x = 0; x < m.getSpecificArray().length; x++) {
				int i = x;
				int a = i - 1;
				if(a < 0)
					a += 7;
				int b = i - 2;
				if(b < 0)
					b += 7;
				//double accidentals
				if((name.substring(0,1).equals(m.getSpecificArray()[a%7].substring(0,1)) && (m.getSpecificArray()[x].indexOf("flat") >= 0) && (m.getSpecificArray()[b%7].indexOf("flat") >= 0))) {
					name = NoteUtilities.convertToDouble(name,m,false); //double flat)
					break;
				}
				else if(name.substring(0,1).equals(m.getSpecificArray()[(x+1)%7].substring(0,1)) && m.getSpecificArray()[x].indexOf("sharp") >=0 && m.getSpecificArray()[(x+2)%7].indexOf("sharp") >=0) {
					name = NoteUtilities.convertToDouble(name,m,true); //double sharp
				    break;
				}
				//enharmonic notes
				else if(name.substring(0,1).equals(m.getSpecificArray()[a%7].substring(0,1)))
					name = NoteUtilities.convertToEnharmonic(name);
				else if(name.substring(0,1).equals(m.getSpecificArray()[(x+1)%7].substring(0,1)))
					name = NoteUtilities.convertToEnharmonic(name);
			}
			
			
		}
		*/
			
		
		return name;
	}
	public String getNoteName() {
		return noteName;
	}
	public int getOctave() {
		return octave;
	}
}
