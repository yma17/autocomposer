package autocomposer;

import autocomposer.Model;
import autocomposer.Note;
import java.util.ArrayList;

/* This class contains all methods used to compose the counterpoint.
 * 
 * Contains methods that use randomization and musical guidelines to decide
 * specific variables and information about the music (e.g. contour, locations
 * of certain musical points).
 * 
 * Methods in this class relay this information to the info variable, a storage
 * space for necessary information to refer to throughout this entire process.
 */
public class Composition implements NotesAndKeys
{
    //counterpoint composed- visually laid out
	private Note[] cantusFirmus; //the main line; the line that the counterpoint will be written off of.
    private Note[] counterpoint; //the second line; may be written on top or below CF
    
    public Model model; //contains all intrinsic information (e.g. key, mode, etc.) of the music to be composed. See Model class for specifics.
    public CompositionInfo info; //contains information to refer to throughout the composition process. See CompositionInfo class for specifics.
    public Composition(Model m)
    {
    	cantusFirmus = new Note[m.getMeasures()];
    	counterpoint = new Note[m.getMeasures()];
        this.model = m;
        info = new CompositionInfo();
    }
    public void compose() //composes the 2-voice counterpoint
    {
    	this.composeCantusFirmus();
    	//this.composeCounterpoint();
    }
    public void composeCantusFirmus() //composes the cantus firmus
    {        
    	//first note & last note = tonic
    	//only 2 precomposed notes
    	cantusFirmus[0] = new Note(model);
    	cantusFirmus[cantusFirmus.length-1] = cantusFirmus[0];
    	
    	//focal point = a "peak"-like point that the notes gradually build up towards and down from
    	int focalPoint = this.determineFocalPointLocation(); //index
   		info.setFocalPoint(focalPoint); //info updated whenever any necessary information is determined
    	
       	//preFPContourType refers to contours that lead up to the focal point - 2 types
   		//1 - there exists a pre-focal point before the focal point that leaps up to the focal point by either interval or special structure.
   		     //It can either be equal to below the tonic note in midiValue
   		//2 - no pre-focal point- notes gradually ascend to focal point
   		int preFPContourType = this.determinePreFPContour();
   		info.setPreFPContourType(preFPContourType);
   		
   		this.composeNoteByNote();
    }
    private int determineFocalPointLocation() //Determines the index of the focal point in the CF.
    {
    	//based entirely on randomization.
    	int n = model.getMeasures(); //depends on length of CF.
    	double x = Math.random();
    	if(n == 9)
    		return 4; //focal point is 5th note of cantus firmus in this case (what is returned minus 1)
    	else if(n == 10)
    		return 5; //6th note of CF
    	else if(n == 11) {
    		if(x < 0.5)
    			return 5;
    		else
    			return 6; //7th note of CF
    	}
    	else if(n == 12) {
    	    if(x < 0.333)
    	    	return 5;
    		else if(x < 0.666)
    			return 6;
    		else
    			return 7; //8th note of CF
    	}
    	else { //13 measures
    		if(x < 0.25)
    			return 5;
    		else if(x < 0.5)
    			return 6;
    		else if(x < 0.75)
    			return 7;
    		else
    			return 8; //9th note of CF
    	}
    }
    private int determinePreFPContour() { //Determines the contour from the first note to the FP.
    	double x = Math.random(); //randomly determined
    	
    	if(x < 0.5)
    		return 1; //Type 1 = pre focal point
    	else
    		return 2; //Type 2 = no pre focal point
    }
    public void composeNoteByNote() {
        for(int i = 1; i <= cantusFirmus.length-2; i++) { //i= always index of cantusFirmus
        	ArrayList<Note> rangeForNextNote;
        	boolean cont = true; //continue with regular note-to-note algorithm
        	if(i == info.getFocalPoint() - 2) { //implement special structures?
        		double d = Math.random();
        		if(d < 0.25 && info.getPreFPContourType() == 1) {
        			ArrayList<Note[]> possibleStructures = this.determinePossibleStructures();
        			int e = (int)(Math.random()*possibleStructures.size());
        			Note[] toBeUsed = possibleStructures.get(e); //array of next 3 notes
        			cantusFirmus[i] = toBeUsed[0];
        			i++; //to the next note
        			cantusFirmus[i] = toBeUsed[1];
        			i++;
        			cantusFirmus[i] = toBeUsed[2];
        			cont = false;
        		}
        	}
        	else if(i == info.getFocalPoint() - 1) { //preFP,FP composed in one unit
        		Note preFP = this.determinePreFP();
        		Note focal = this.determineFocal(preFP);
        		cantusFirmus[i] = preFP;
        		i++;
        		cantusFirmus[i] = focal;
        		cont = false;
        	}
            
        	if(cont) { //standard, default, note-to-note algorithm
        		ArrayList<Note> rangeForNextNote;
            	
            	= this.listRange(cantusFirmus[i-1]); //raw range
            	
            	//initialize previousNotes and futureNotes
        		//note: both arraylists count notes forwards (e.g. 4th note,5th note,6th note...)
        		ArrayList<Note> previousNotes = new ArrayList<Note>();
        		for(int x = i-3; x <= i-1; x++) { //<=3 notes
        			if(x >= 0) //to avoid ArrayOutOfBoundsException, to manage first 3 notes of the CF
        				previousNotes.add(cantusFirmus[x]);
        		}
        		ArrayList<Note> futureNotes = new ArrayList<Note>();
        		for(int x = i+1; x <= i+3; x++) { //<=3 notes
        			if(x <= cantusFirmus.length-1)
        				futureNotes.add(cantusFirmus[x]);
        		}
        		
        		rangeForNextNote = this.listValidRange(rangeForNextNote, i, previousNotes, futureNotes);
        		
        		if(rangeForNextNote.size() == 0) {
        			//code to manage error
        		}
        		else {
        			//select a random note from the arraylist and compose it
        			int n = ((int)Math.random()*rangeForNextNote.size());
        			Note nextNote = rangeForNextNote.get(n);
        		    cantusFirmus[i] = nextNote;
        		}
        	}
        }
    }
    public ArrayList<Note> listRange(Note previous) { //lists raw range from fifth below to fifth above
    	ArrayList<Note> range = new ArrayList<Note>();
    	for(int i = -4; i <= 4; i++) {
    		if(i != 0) //no repeated notes - guideline 3
    			range.add(new Note(cantusFirmus[0],previous.getRelativePitch()+i,model));
    	}
    	return range;
    }
    public ArrayList<Note> listValidRange(ArrayList<Note> originalRange,int index,ArrayList<Note> previousNotes,ArrayList<Note> futureNotes) {
    	ArrayList<Note> validRange = new ArrayList<Note>();
    	for(int i = 0; i < originalRange.size(); i++) {
    		Note proposed = originalRange.get(i);
    		
    		boolean valid = true;
    		
        	//method will call helper methods for each specific guideline
            if(!checkStepToLeapRatio())
            	valid = false;
            if(!checkForTriTones(proposed,previousNotes.get(2)))
            	valid = false;
            if(!checkOppositeMotion(previousNotes.get(0),previousNotes.get(1),previousNotes.get(2),proposed))
            	valid = false;
            if(previousNotes.get(0) != null && previousNotes.get(1) != null) { //if the entire previousNotes array is valid (get(2) is always valid(first note tonic))
            	if(!checkOppositeMotionFromAhead(previousNotes))
            		valid = false;
            	if(!checkTriToneStress(previousNotes))
            		valid = false;
            }
            if(futureNotes.get(0) != null) {
            	if(!checkForTriTones(proposed,futureNotes.get(0)))
            		valid = false;
            	if(!checkOppositeMotion(previousNotes.get(1),previousNotes.get(2),proposed,futureNotes.get(1)))
            		valid = false;
            	if(futureNotes.get(1) != null && futureNotes.get(2) != null) { //if the entire futureNotes array is valid
            		if(!checkOppositeMotionFromBehind(futureNotes))
            			valid = false;
            	}
            }   	
    	}
    	return validRange;
    }
    private void composeCounterpoint(boolean istopLineCF) {
    	if(istopLineCF)
    		this.composeBottomVoice();
    	else
    	    this.composeTopVoice();
    }
    private void composeBottomVoice() //composes the bottom voice, in case the CF is the top voice
    {
        //insert code here
    }
    private void composeTopVoice() //composes the top voice, in case the CF is the bottom voice;
    {
    	//insert code here
    }
    public Note[] getCantusFirmus() {
    	return cantusFirmus;
    }
    public Note[] getCounterpoint() {
    	return counterpoint;
    }
}
