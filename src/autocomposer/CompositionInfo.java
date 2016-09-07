package autocomposer;

/* Variable of the Composition class.
 * An object of CompositionInfo exists for each of the two lines (cantusFirmus, counterpoint)
 * Contains variables dependent on the number of notes composed so far.
 * Will be updated as notes are composed.
 * Instances of this class are also employed during the error-checking algorithm in the Composer class.
 */
public class CompositionInfo {
	private int leapsSoFar; //3rd or higher [info]
	private int stepsSoFar; //2nd [info]
	private int largerIntervalsSoFar; //4th or higher [info]
	private int smallerIntervalsSoFar; //3rd or smaller [info]
	private int notesComposedSoFar; //well, self explanatory.
	public CompositionInfo() {
		//all default values = 0.
	}
	public void updateInfo(Note note,Note noteBefore,Note noteAfter,boolean composing) { 
		//composing = true - note has just been composed
		//composing = false - note has just been removed
		//Updates all variables.
		
		int value; //amount to increase variables by
		if(composing)
			value = 1; //increment by 1
		else
			value = -1; //reduce by 1
		
		if(noteBefore != null) {
			int intervalWithNoteBefore = Math.abs(note.getRelPitch()-noteBefore.getRelPitch());
			if(intervalWithNoteBefore == 1) //step
				stepsSoFar += value;
			else //leap
				leapsSoFar += value;
			
			if(intervalWithNoteBefore <= 2) //smaller interval
				smallerIntervalsSoFar += value;
			else //large leaps
				largerIntervalsSoFar += value;
		}
		if(noteAfter != null) { //same code with noteAfter
			int intervalWithNoteAfter = Math.abs(note.getRelPitch()-noteAfter.getRelPitch());
			if(intervalWithNoteAfter == 1)
				stepsSoFar += value;
			else
				leapsSoFar += value;
			
			if(intervalWithNoteAfter <= 2)
				smallerIntervalsSoFar += value;
			else
				largerIntervalsSoFar += value;
		}
		
		notesComposedSoFar += value;
	}
	//accessor methods
	public int getStepsSoFar() { return stepsSoFar; }
	public int getLeapsSoFar() { return leapsSoFar; }
	public int getSmallerIntervalsSoFar() { return smallerIntervalsSoFar; }
	public int getLargerIntervalsSoFar() { return largerIntervalsSoFar; }
	public int getNotesComposedSoFar() { return notesComposedSoFar; }
	
	public String toString() {
		return "stepsSoFar: " + stepsSoFar + "\n"
				+ "leapsSoFar: " + leapsSoFar + "\n"
				+ "smallerIntervalsSoFar: " + smallerIntervalsSoFar + "\n"
				+ "largerIntervalsSoFar: " + largerIntervalsSoFar + "\n"
				+ "notesComposedSoFar: " + notesComposedSoFar;
	}
}
