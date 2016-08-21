package autocomposer;

/* Counterpoint Error consists of a Note object and an array index.
 * Used during note-by-note composition to resolve "dead-end situations".
 * While resolving a dead end, the algorithm cannot proposed this Note at this index.
 */
public class CounterpointError {
	public Note errorNote;
	public int index;
	public CounterpointError(Note note,int index)
	{
		errorNote = note;
		this.index = index;
	}
	public Note getErrorNote() {
		return errorNote;
	}
	public int getIndex() {
		return index;
	}
}
