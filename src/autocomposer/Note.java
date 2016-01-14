package autocomposer;


public class Note
{
	
//	public int midiValue;
	public int pitch; // 0-11, 0 = C
	public int octave; // 0-N
	
	public Note(int pitch, int octave)
	{
		this.pitch = pitch;
		this.octave = octave;
		
//		int i = 0;
//		for(int x = 0; x < NOTES.length; x++)
//		{
//			if(NOTES[x].equals(note))
//				i = x;
//		}
	}
	
	public Note(int pitch) {
		this(pitch, 4);
	}
	
	public int midiValue() {
		return (12 * this.octave) + this.pitch + 12;
	}
}
