package autocomposer;

import autocomposer.Model;
import autocomposer.Note;
import java.util.ArrayList;

/* Contains the composition, the music, as well as the variables and properties of it.
 * 
 */
public class Composition implements NotesAndKeys
{
	public Model model; //contains all intrinsic information (e.g. key, mode, etc.) of the music to be composed. See Model class for specifics.
	
    //counterpoint composed- visually laid out
	private Note[] cantusFirmus; //the main line; the line that the counterpoint will be written off of.
    private Note[] counterpoint; //the second line; may be written on top or below CF
    
    //contains variables to be updated during the composition process
    private CompositionInfo info; //of the current line being composed. Will reset when CF is finished
    
    //variables of the cantusFirmus - independent of time points in the algorithm's execution - related to "musical decisions" - "inside" the counterpoint
	private int maxPitch; //highest relative pitch [pre-comp]
	private int minPitch; //lowest relative pitch [pre-comp]
	private int focalPoint; //index of the FP in CF (nth note minus 1) [pre-comp]
	private int preFPContourType; //of the cantus firmus [pre-comp]
	private int preFPBeginPoint; //note-by-note begins composing here (before FP) [pre-comp]
	private int preFPEndPoint; //note-by-note stops composing here (before FP) [pre-comp]
	private int postFPBeginPoint; //note-by-note begins composing here (after FP) [pre-comp]
	private int postFPEndPoint; //note-by-note stops composing here (after FP) [pre-comp]
	
	private boolean CF; //is CF the current line being composed?
    
    public Composition(Model m)
    {
    	model = m;
    	
    	cantusFirmus = new Note[m.getMeasures()];
    	counterpoint = new Note[m.getMeasures()];
        
    	//default values
    	maxPitch = Integer.MAX_VALUE;
    	minPitch = Integer.MIN_VALUE;
    	
    	info = new CompositionInfo();
    	
    	CF = true; //cantusFirmus is the first line to be composed
    }
    public Note[] getCantusFirmus() {
    	return cantusFirmus;
    }
    public Note[] getCounterpoint() {
    	return counterpoint;
    }
    public Note[] getCurrentLine() { //return the current line being composed.
    	if(CF)
    		return cantusFirmus;
    	else
    		return counterpoint;
    }
    public void composeNote(Note toBeComposed,int index) {
    	if(CF) {
    		cantusFirmus[index] = toBeComposed;
			info.updateInfo(toBeComposed, cantusFirmus[index-1], cantusFirmus[index+1], true);
    	}
    	else { //second line - counterpoint
    		counterpoint[index] = toBeComposed;
			info.updateInfo(toBeComposed, counterpoint[index-1], counterpoint[index+1], true);
    	}
    }
    public void uncomposeNote(Note uncomposed,int index) { //helper method of composeNoteByNote().
    	if(CF) {
    		cantusFirmus[index] = null;
	    	info.updateInfo(uncomposed, cantusFirmus[index-1],cantusFirmus[index+1],false);
    	}
    	else { //second line - counterpoint
    		counterpoint[index] = null;
	    	info.updateInfo(uncomposed, counterpoint[index-1],counterpoint[index+1],false);
    	}
    }
    public Note getNote(String line, int index) {
    	if(line.equals("CF"))
    		return cantusFirmus[index];
    	else //line = "CP"
    		return counterpoint[index];
    }
    public Note getNote(int index) {
    	String line;
    	if(CF)
    		line = "CF";
    	else
    		line = "CP";
    	
    	return this.getNote(line,index);
    }
    public int getLength() {
    	return cantusFirmus.length;
    }
    public Model getModel() {
    	return model;
    }
    public CompositionInfo getInfo() {
    	return info;
    }
    public void resetInfo() {
    	info = new CompositionInfo();
    }
	public int getMaxPitch() {
		return maxPitch;
	}
	public void setMaxPitch(int pitch) {
		maxPitch = pitch;
	}
	public int getMinPitch() {
		return minPitch;
	}
	public void setMinPitch(int pitch) {
		minPitch = pitch;
	}
	public int getFocalPoint() {
		return focalPoint;
	}
	public void setFocalPoint(int focalPoint) { //determined in composeCantusFirmus
		this.focalPoint = focalPoint;
	}
	public int getPreFPContourType() {
		return preFPContourType;
	}
	public void setPreFPContourType(int type) { //determined in composeCantusFirmus
		preFPContourType = type;
	}
	public int getPreFPBeginPoint() {
		return preFPBeginPoint;
	}
	public void setPreFPBeginPoint(int location) { //determined in composeCantusFirmus
		preFPBeginPoint = location;
	}
	public int getPreFPEndPoint() {
		return preFPEndPoint;
	}
	public void setPreFPEndPoint(int location) { //determined in composeCantusFirmus
		preFPEndPoint = location;
	}
	public int getPostFPBeginPoint() {
		return postFPBeginPoint;
	}
	public void setPostFPBeginPoint(int location) { //determined in composeCantusFirmus
		postFPBeginPoint = location;
	}
	public int getPostFPEndPoint() {
		return postFPEndPoint;
	}
	public void setPostFPEndPoint(int location) { //determined in composeCantusFirmus
		postFPEndPoint = location;
	}
	public void switchLine() {
		CF = !CF;
	}
	public void updateInfo(Note note,Note noteBefore,Note noteAfter,boolean composing) {
		info.updateInfo(note, noteBefore, noteAfter, composing);
	}
	public String toString() {
		//precondition: all instance variables initialized
		return	"maxPitch: " + maxPitch + "\n"
				+ "minPitch: " + minPitch + "\n"
				+ "focalPoint: " + focalPoint + "\n"
				+ "preFPContourType: " + preFPContourType + "\n"
				+ "preFPBeginPoint: " + preFPBeginPoint + "\n"
				+ "preFPEndPoint: " + preFPEndPoint + "\n"
				+ "postFPBeginPoint: " + postFPBeginPoint + "\n"
				+ "postFPEndPoint: " + postFPEndPoint + "\n"
				+ "\n" + "info variables: " + "\n"
				+ info.toString();
	}
}
