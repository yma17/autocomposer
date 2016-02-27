package autocomposer;

import autocomposer.Model;
import autocomposer.Note;
import java.util.ArrayList;

//import com.sun.javafx.sg.prism.NGShape.Mode;

public class Composition implements NotesAndKeys
{
    //counterpoint composed- visually laid out
	private Note[] cantusFirmus;
    private Note[] counterpoint; //may be written on top or below CF
    public Model model;
    public Composition(Model m)
    {
        this.model = m;
    }
    public Note[][] compose() //composes the 2-voice counterpoint
    {
        Note[][] composition = new Note[2][model.getMeasures()];
    	cantusFirmus = composeCantusFirmus();
        counterpoint = composeCounterpoint(model.getCF());
        for(int x = 0; x < model.getMeasures(); x++)
        {
            if(model.getCF())
            {
                composition[0][x] = cantusFirmus[x];
                composition[1][x] = counterpoint[x];
            }
            else
            {
                composition[1][x] = cantusFirmus[x];
                composition[0][x] = counterpoint[x];
            }
        }
        return composition;
    }
    public int findPitch(String note){ //finds pitch(0-11) of a given note
    	int pitch = 0;
    	for(int x = 0; x < NOTES.length; x++) {
    		if(note.equals(NOTES[x]))
    			pitch = x;
    	}
    	return pitch;
    }
    public int findMIDIInterval(int interval) {
    	/*
    	int x = 0;
    	boolean b = true;
    	int a = model.getModeValue()+(interval-1); //to facilitate method- avoid outofBoundsException
    	*/
    	/*
    	if(a <= 0) {
    		x = -1;
    		a += 9; 
    	}
    	if(a > 7) {
    		if(a == 8 || a == 9)
    			x = 0;
    		else
    			x = 1;
    		a -= 7;
    	}
    	*/
    	/*
    	if(a < -1) {
    		x = -1;
    	    a += 9;
    	}
    	if(a == -1)
    	
		return (DIATONIC_INTERVALS[a]+12*x) - DIATONIC_INTERVALS[model.getModeValue()];
		*/
        /*
    	int a = (interval-1) + model.getModeValue();
        int x = 0;
        if(a < 0) {
        	while(a < 0) {
        		x--;
        		a += 7;
        	}
        }
        else if(a >= 7) {
        	while(a >= 7) {
        		x++;
        		a -= 7;
        	}
        }
        return (DIATONIC_INTERVALS[a]+12*x) - DIATONIC_INTERVALS[model.getModeValue()];
        */
    	/*
    	String s = model.getMode();
    	if(s.equals("Ionian")) {
    		if(interval < 0)
    			return IONIAN_DIATONIC_INTERVALS[7+interval+1] - 12;
    		else
    			return IONIAN_DIATONIC_INTERVALS[interval-1];
        */
        
    }
    public Note[] composeCantusFirmus() //composes the cantus firmus
    {
        Note[] cantusFirmus = new Note[model.getMeasures()];
        
        //TODO
        cantusFirmus[0] = new Note(this.findPitch(model.getKey())); //compose first note of the CF (tonic)
        cantusFirmus[cantusFirmus.length - 1] = cantusFirmus[0]; //compose last note of the CF (tonic)
        
		int focalPoint = this.determineFocalPoint();
		int preFPContourType = this.determinePreFPContour();
		//int postFPContourType = this.determinePostFPContour();
        
        if(preFPContourType== 1) {
        	int tonicToLowPointInterval = this.determineTonicToLPInterval();
        	int leapToFPInterval = this.determineLeapToFPInterval(model.getMode(), tonicToLowPointInterval);
        	
        	int tonicToFPInterval = leapToFPInterval + tonicToLowPointInterval + 1;
        	cantusFirmus[focalPoint - 1] = new Note(findPitch(model.getKey()) + this.findMIDIInterval(tonicToFPInterval)); //compose focal point

			int lowPoint = focalPoint - 1;
        	if(leapToFPInterval == 8 && focalPoint >= 6) { //2-interval leap (1-interval if mode and tonic to low pt interval do not permit)
        		double x = Math.random();
        		if(x < 0.2) { //5th + 4th
        			boolean b = this.checkFifthPlusFourth(model.getMode(), tonicToLowPointInterval);
        			if(b) {
        		        cantusFirmus[lowPoint - 2] = new Note(this.findPitch(model.getKey()) + this.findMIDIInterval(tonicToLowPointInterval)); //compose low point
        	            cantusFirmus[lowPoint - 1] = new Note(cantusFirmus[focalPoint - 1].getPitch() + 7);
        			}
        		}
        		//if(x < 0.4) { //additional single note before focal point (include???)
        			//cantusFirmus[focalPoint - 2] = new Note(this.findPitch(model.getKey()) - DIATONIC_INTERVALS[tonicToLowPointInterval - 1]); //compose low point
        			//TODO
        		//}
        		cantusFirmus[lowPoint - 1] = new Note(this.findPitch(model.getKey()) + this.findMIDIInterval(tonicToLowPointInterval)); //compose low point
        		lowPoint--;
        	}
        	else //single leap
        		cantusFirmus[lowPoint - 1] = new Note(this.findPitch(model.getKey()) + this.findMIDIInterval(tonicToLowPointInterval)); //compose low point 	
        }
        /*
        else if(preFPContourType == 2) {
        	//TODO
        }
        else if(preFPContourType == 3) {
        	//TODO
        }
        */

        //@SuppressWarnings("unused")
		//Note[] preFP = this.composePreFP(focalPoint, preFPContourType);
        
        //determine 2nd note
        /*
        double w = Math.random();
        if(w < 0.5)
        	cantusFirmus[1] = new Note(this.findPitch(model.getKey()) + DIATONIC_INTERVALS[model.getModeValue() + determineSecondNote(w) - 1] - DIATONIC_INTERVALS[model.getModeValue()]);
        else
        	cantusFirmus[1] = new Note(this.findPitch(model.getKey()) - DIATONIC_INTERVALS[model.getModeValue() + determineSecondNote(w) - 1] - DIATONIC_INTERVALS[model.getModeValue()]);
        */
        
        //determine pre focal point
        for(int x = 2; x < focalPoint - 3; x++) {
        	//cantusFirmus[x] = composeNextNote(x, lastInterval, twoIntervalsBefore, lastDirection, twoDirectionsBefore); //TODO
        }
        
        
        //determine 2nd to last note
        double z = Math.random();
        if(z < 0.5)
        	cantusFirmus[cantusFirmus.length - 2] = new Note(this.findPitch(model.getKey()) - DIATONIC_INTERVALS[model.getModeValue() + 1] - DIATONIC_INTERVALS[model.getModeValue()]); //step up
        else
        	cantusFirmus[cantusFirmus.length - 2] = new Note(this.findPitch(model.getKey()) + DIATONIC_INTERVALS[model.getModeValue() + 1] - DIATONIC_INTERVALS[model.getModeValue()]); //step down
        
        //@SuppressWarnings("unused")
		//Note[] postFP = this.composePostFP(model.getMeasures(), focalPoint, postFPContourType);
        
        /*
        for(int x = 0; x < preFP.length; x++)
        	cantusFirmus[x + 1] = preFP[x];
        for(int x = postFP.length; x > 0; x--)
        	cantusFirmus[x - 2] = postFP[x];
        */
        
        return cantusFirmus;
    }
    private int determineFocalPoint() //helper method of composeCantusFirmus
    {
    	int n = model.getMeasures();
    	double x = Math.random();
    	if(n == 9)
    		return 5; //focal point is 5th note of cantus firmus in this case (what is returned minus 1)
    	else if(n == 10)
    		return 6;
    	else if(n == 11) {
    		if(x < 0.5)
    			return 6;
    		else
    			return 7;
    	}
    	else if(n == 12) {
    	    if(x < 0.333)
    	    	return 6;
    		else if(x < 0.666)
    			return 7;
    		else
    			return 8;
    	}
    	else { //13 measures
    		if(x < 0.25)
    			return 6;
    		else if(x < 0.5)
    			return 7;
    		else if(x < 0.75)
    			return 8;
    		else
    			return 9;
    	}
    }
    private int determinePreFPContour() { //helper method of composeCantusFirmus. Determines the contour from the first note to the FP.
    	/*
    	double x = Math.random();
    	if(x < 0.3333)
    		return 1; //Type 1 = go down to low point and leap to focal point
    	else if(x < 0.6666)
    		return 2; //Type 2 = travel, return to tonic and leap to focal point
    	else
    		return 3; //Type 3 = gradual motion upwards towards focal point
    	*/
    	return 1;
    }
    private int determinePostFPContour() { //helper method of composeCantusFirmus. Determines the contour from the FP to the last note.
    	double x = Math.random();
    	if(x < 0.5)
    		return 1; //Type 1 = gradual motion downwards towards last note
    	else
    		return 2; //Type 2 = downward motion to below last note, then stepwise motion to last note
    }
    private int determineTonicToLPInterval() { //Case I helper method
    	double x = Math.random();
    	return (int)(x * 4.0) - 5; //-2 = 2nd, -3 = 3rd, etc. (will return -2-(-5))
    }
    private int determineLeapToFPInterval(String mode, int tonicToLP) { //Case I helper method
    	double x = Math.random();
    	if(tonicToLP == 2) {
    		if(mode.equals("Lydian")) {
    			if(x < 0.333)
    				return 6; //ascending 6th
    			x = Math.random();
    		}
    		if(mode.equals("Ionian")) {
    			if(x < 0.5)
    				return 6;
    			x = Math.random();
    		}
    		if(x < 0.5 && !mode.equals("Ionian"))
    			return 5; //perfect 5th
    		else
    			return 8; //octave (8ve)
    	}
        else if(tonicToLP == 3) {
        	if(mode.equals("Dorian") || mode.equals("Mixolydian") || mode.equals("Ionian")) {
        		if(x < 0.5)
        			return 6;
        	}
        	return 8;
    	}
        else
        	return 8;
    }
    private boolean checkFifthPlusFourth(String mode, int tonicToLPInterval) { //Case 1 helper method
    	if((mode.equals("Dorian") && tonicToLPInterval == 3) ||
    			(mode.equals("Phrygian") && tonicToLPInterval == 4) ||
    			(mode.equals("Lydian") && tonicToLPInterval == 5) ||
    			(mode.equals("Ionian") && tonicToLPInterval == 2)) //cases in which 5th+4th is not acceptable
    		return false;
    	return true;
    }
    private int determineSecondNote(double w) { //returns 1-2 interval
    	return (int)w*4 + 2; //returns number between 2 and 5
    }
    /*
    public Note[] composeNoteByNote(int notesToBeComposed, Note[] previousNotes, ArrayList<Note> futureNotes) { //composes all notes in an empty section
    	Note[] arr = new Note[notesToBeComposed];
    	
    	for(int x = 0; x < arr.length; x++) {
    		//special structures here
    		double y = Math.random();
    		if(y < 0.1) {
    			//composeConsecutiveSteps;
    		}
    		else if(y < 0.2) {
    			//composeTriad;
            }
    		
    		//main note-to-note algorithm
    		Note nextNote = decideNextNote(previousNotes, futureNotes);
            arr[x] = nextNote;
    	}
    	return arr;
    }
    
    private Note decideNextNote(Note[] previousNotes, ArrayList<Note> futureNotes) {
    	return new Note[0];
    }
    */
    //TODO
    /*
    private Note[] composePreFP(int focalPoint, int contourType) {
    	Note[] preFP = new Note[focalPoint - 3];
    	
    	
    	//TODO
    	return new Note[0]; //so that it compiles
    }
    private Note[] composePostFP(int measures, int focalPoint, int contourType) {
    	Note postFP = new Note[measures - focalPoint - 2];
    	
    	//TODO
    	return new Note[0]; //so that it compiles
    }
    */
    
    private Note[] composeCounterpoint(boolean istopLineCF) {
    	if(istopLineCF)
    		return composeBottomVoice();
    	else
    		return composeTopVoice();
    }
    private Note[] composeBottomVoice() //composes the bottom voice, in case the CF is the top voice
    {
        Note[] bottomVoice = new Note[model.getMeasures()];
        //insert code here
        return bottomVoice;
    }
    private Note[] composeTopVoice() //composes the top voice, in case the CF is the bottom voice;
    {
        Note[] topVoice = new Note[model.getMeasures()];
        //insert code here
        return topVoice;
    }
}
