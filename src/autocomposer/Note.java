package autocomposer;

public class Note implements NotesAndKeys
{
	public int midiValue;
	public String note;
	
	public Note(String note, int octave)
	{
		this.note = note;
		
		int i = 0;
		for(int x = 0; x < NOTES.length; x++)
		{
			if(NOTES[x].equals(note))
				i = x;
		}
		midiValue = (12 * octave) + i + 12;
	}
}
