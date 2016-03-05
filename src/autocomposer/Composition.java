package autocomposer;

import java.util.Arrays;

import autocomposer.Model;
import autocomposer.Note;
import java.util.ArrayList;

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
    public Note[] composeCantusFirmus() //composes the cantus firmus
    {
        Note[] cantusFirmus = new Note[model.getMeasures()];
        int[] relativePitches = new int[model.getMeasures()]; //rel. pitches in respect to tonic
        
        //first note & last note = tonic
        relativePitches[0] = 0;
        relativePitches[cantusFirmus.length-1] = 0;
        
		int focalPoint = this.determineFocalPointLocation();
		int preFPContourType = this.determinePreFPContour();
		
		int firstBeginPoint = 1; //note-by-note begins composing here- always begins at 2nd note
    	int firstEndPoint; //note-by-note stops composing here
    	
        if(preFPContourType== 1) { //tonic to low point, leap to FP
        	firstEndPoint = focalPoint - 2;
        	
        	int tonicToLowPointInterval = this.determineTonicToLPInterval();
        	int leapToFPInterval = this.determineLeapToFPInterval(model.getMode(), tonicToLowPointInterval);
        	
        	int tonicToFPInterval = leapToFPInterval + tonicToLowPointInterval + 1;
        	
        	relativePitches[focalPoint-1] = tonicToFPInterval - 1; //compose focal point
 
			int lowPoint = focalPoint - 1;
			double x = Math.random();
        	if(leapToFPInterval == 8 && focalPoint >= 6 && x < 0.2) { //special structures
        		if(x < 0.1) { //5th + 4th
        			boolean b = this.checkFifthPlusFourth(model.getMode(), tonicToLowPointInterval);
        			if(b) {
        				relativePitches[focalPoint - 3] = relativePitches[focalPoint - 1] - 7;
        				relativePitches[focalPoint - 2] = relativePitches[focalPoint - 1] - 3;
        				firstEndPoint = focalPoint - 3;
        			}
        		}
        		else if(this.checkAdditionalNote(relativePitches[focalPoint-1])){ //additional note before focal point
        			relativePitches[focalPoint - 2] = relativePitches[focalPoint - 1] - 1;
        			boolean b = this.checkSixth(NoteUtilities.convertRelativeToNote(relativePitches[focalPoint-2],model).getNoteName());
        			if(x < 0.15 && b)
        				relativePitches[focalPoint - 3] = relativePitches[focalPoint - 2] - 5; //asc m6
        			else
        				relativePitches[focalPoint - 3] = relativePitches[focalPoint - 2] - 4; //5th
        			firstEndPoint = focalPoint - 3;
        		}
        		else
        			relativePitches[lowPoint - 1] = tonicToLowPointInterval + 1; //compose low point
        	}
        	else {//single leap
        		relativePitches[lowPoint - 1] = tonicToLowPointInterval + 1; //compose low point
        	
        	}
        }
        else { //precontour = 2 or 3
        	boolean focalPointCanBeFour = true;
        	if(preFPContourType == 2) { //tonic back to tonic, leap to FP
            	relativePitches[focalPoint - 2] = 0;
            	firstEndPoint = focalPoint - 2;
            }
            else { //preFPContourType == 3, progressive motion to FP throughout
            	firstEndPoint = focalPoint - 1;
            	
            	double x = Math.random();
            	if(x < 0.25) { //special structure- 1,3,4
            		relativePitches[1] = 2;
            		relativePitches[2] = 3;
            		firstBeginPoint = 3;
            		focalPointCanBeFour = false;
            	}
            }
        	relativePitches[focalPoint - 1] = this.determineFocalPoint(preFPContourType,focalPointCanBeFour)-1;
        }
        //variables needed for note-to-note
        int lastPitch = relativePitches[0];
        int twoPitchesAgo = relativePitches[0]; //for now, will update as index increases
        int threePitchesAgo = relativePitches[0];
        int leapsSoFar = 0;
        int stepsSoFar = 0;
        int largeLeapsSoFar = 0;
        int smallerIntervalsSoFar = 0;
        int notesInSection = firstEndPoint - firstBeginPoint;
        
        /*
        //determine pre focal point
        int[] section = this.composeNoteByNote(notesInSection,lastPitch,twoPitchesAgo,threePitchesAgo,firstEndPoint,leapsSoFar,stepsSoFar,largeLeapsSoFar,smallerIntervalsSoFar);
        for(int x = firstBeginPoint; x < firstEndPoint; x++) {
        	relativePitches[x] = section[x - firstBeginPoint];
        }
        
        */
        //determine 2nd to last note
        double z = Math.random();
        if(z < 0.5)
        	relativePitches[cantusFirmus.length - 2] = 1; //step down
        else
        	relativePitches[cantusFirmus.length - 2] = -1; //step up
        /*
        for(int x = 0; x < relativePitches.length; x++) {
        	cantusFirmus[x] = NoteUtilities.convertRelativeToNote(relativePitches[x], model);
        }
        */
        
        //used for testing
        for(int x = 0; x < relativePitches.length; x++) {
        	System.out.println(relativePitches[x]);
        }
       
        
        return cantusFirmus;
    }
    private int determineFocalPoint(int i, boolean canBeFour) { //if case is not case 1
    	double x = Math.random();
    	if(x < 0.35 && canBeFour)
    		return 4;
    	else if(x < 0.65) {
    		if(i == 3)
    			return 6;
    	}
    	return 5;
    	//can't return 8 anymore- wanders too far away
    }
    private int determineFocalPointLocation() //helper method of composeCantusFirmus
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
    	double x = Math.random();
    	if(x < 0.3333)
    		return 1; //Type 1 = go down to low point and leap to focal point
    	else if(x < 0.6666)
    		return 2; //Type 2 = travel, return to tonic and leap to focal point
    	else
    		return 3; //Type 3 = gradual motion upwards towards focal point
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
    private boolean checkAdditionalNote(int interval) {
    	if(interval >= 4)
    		return false;
    	return true;
    }
    private boolean checkSixth(String topNote) {
    	if(topNote.equals("A") || 
    			topNote.equals("B") || 
    			topNote.equals("D") || 
    			topNote.equals("E")) //cases in which ascending m6 is not acceptable
    		return false;
    	return true;
    }
    public int[] composeNoteByNote(int notesInSection,int lastPitch,int twoPitchesAgo,int threePitchesAgo,int nextPitch,int leapsSoFar,int stepsSoFar,int largeLeapsSoFar,int smallerIntervalsSoFar) { //note-by-note algorithm
    	ArrayList<CounterpointError> errors = new ArrayList<CounterpointError>();
    	
    	int[] arr = new int[notesInSection];
    	int notesToBeComposed = notesInSection;
    	boolean skipRest = false;
    	boolean recomposing = false;
    	int recomposingIndex = 0;
    	int toEndInterval = nextPitch - lastPitch;
    	int twoAgo = twoPitchesAgo;
    	int threeAgo = threePitchesAgo;
    	for(int x = 0; x < notesInSection; x++) {
    		//special structures
    		boolean up;
			if(x == 0)
				up = (nextPitch > lastPitch);
			else
				up = (nextPitch > arr[x-1]);
			
    		double y = Math.random();
    		double z = Math.random();
    		int length; //between 4 and 6
    		if(y < 0.1) { //consecutive steps
    			if(notesToBeComposed == 4)
    				length = 4;
    			else if(notesToBeComposed == 5)
    				length = (int)(z*2)+4;
    			else //notesToBeComposed >= 6
    				length = (int)(z*3)+4;
    			
    			boolean valid;
    			if(up)
    				valid = this.checkNoteValidity(arr[x-1]+length,arr[x-1]+(length-1),arr[x-1]+(length-2),arr[x-1]+(length-3),nextPitch,-1,-1,-1,-1,notesToBeComposed-length); //-1 = do not consider these parameters
    			else
    				valid = this.checkNoteValidity(arr[x-1]-length,arr[x-1]+(-length+1),arr[x-1]+(-length+2),arr[x-1]+(length-3),nextPitch,-1,-1,-1,-1,notesToBeComposed-length); //-1 = do not consider these parameters
    			
    			if(valid) {
    				for(int a = 0; a < length; a++) {
    					if(up)
    						arr[x+a] = arr[x-1]+(x+1);
    					else
    						arr[x+a] = arr[x-1]-(x+1);
    				}
    			}
    		}
    		else if(y < 0.2) { //triad
    			int[] triadIntervals;
    			String position;
    			double b = Math.random();
    			if(b < 0.5) {
    				triadIntervals = ROOT_POSITION_TRIAD_INTERVALS;
    				position = "root position";
    			}
    			else if(b < 0.75) {
    				triadIntervals = FIRST_INVERSION_TRIAD_INTERVALS;
    				position = "first inversion";
    			}
    			else {
    				triadIntervals = SECOND_INVERSION_TRIAD_INTERVALS;
    				position = "second inversion";
    			}
    			
    			int next;
    			int after;
    			if(up) {
    			    next = arr[x-1] + triadIntervals[1]; 
    			    after = arr[x-1] + triadIntervals[2];
    			}
    			else {
    				if(position.equals("root position"))
    				    next = arr[x-1] - triadIntervals[1]; 
    				else
    					next = arr[x-1] - (5- triadIntervals[1]);
    				after = arr[x-1] - triadIntervals[2];
    			}
    			
    			
    		}
    		
    		int nextNote = this.decideNextNote(lastPitch,twoPitchesAgo,threePitchesAgo,nextPitch,leapsSoFar,stepsSoFar,largeLeapsSoFar,smallerIntervalsSoFar,notesToBeComposed);
    		if(nextNote == -1) { //no valid notes available
    			//store error
    			if(recomposing)
    			    errors.add(new CounterpointError(arr[x-1]));
    			errors.get(recomposingIndex).addToError(arr[x-1]);
    			
    			arr[x-1] = 0;
    			notesToBeComposed++;
    			toEndInterval = lastPitch - arr[x];
    			x--; //go back
    		}
    		else {
    			notesToBeComposed--;
    			toEndInterval = nextPitch - arr[x];
    		}
    	}
    	return arr;
    }
    private int decideNextNote(int lastPitch,int twoPitchesAgo,int threePitchesAgo,int nextPitch,int leapsSoFar,int stepsSoFar,int largeLeapsSoFar,int smallerIntervalsSoFar,int notesToBeComposed) {
    	ArrayList<Integer> range = this.listRange(lastPitch);
    	ArrayList<Integer> validRange = this.listValidRange(range, lastPitch, twoPitchesAgo, threePitchesAgo, nextPitch, leapsSoFar, stepsSoFar, largeLeapsSoFar, smallerIntervalsSoFar,notesToBeComposed);
    	
    	if(validRange.size() == 0) {
    		return -1;
    	}
    	int index = (int) Math.random() * validRange.size();
    	int proposedNote = validRange.get(index);
    	return proposedNote;
    	
    }
    private ArrayList<Integer> listRange(int lastPitch) {
    	ArrayList<Integer> range = new ArrayList<Integer>();
    	for(int x = 2; x <= 5; x++) {
    		range.add(lastPitch+x); //above
    		range.add(lastPitch-x); //below
    	}
    	return range;
    }
    private ArrayList<Integer> listValidRange(ArrayList<Integer> range, int lastPitch,int twoPitchesAgo,int threePitchesAgo,int nextPitch,int leapsSoFar,int stepsSoFar,int largeLeapsSoFar,int smallerIntervalsSoFar,int notesToBeComposed) {
    	ArrayList<Integer> validRange = new ArrayList<Integer>();
    	for(int x = 0; x < range.size(); x++) {
    		boolean valid = this.checkNoteValidity(range.get(x),lastPitch,twoPitchesAgo,threePitchesAgo,nextPitch,leapsSoFar,stepsSoFar,largeLeapsSoFar,smallerIntervalsSoFar,notesToBeComposed);
    		if(valid)
    			validRange.add(range.get(x));
    	}
    	return validRange;
    }
    private boolean checkNoteValidity(int pitch,int lastPitch,int twoPitchesAgo,int threePitchesAgo,int nextPitch,int leapsSoFar,int stepsSoFar,int largeLeapsSoFar,int smallerIntervalsSoFar,int notesToBeComposed) {
    	int thisInterval = pitch - lastPitch; //1= 2nd
    	int lastInterval = lastPitch - twoPitchesAgo;
    	int twoIntervalsAgo = twoPitchesAgo = threePitchesAgo;
    	int nextInterval = nextPitch - pitch;
    	
    	//no tri-tones
    	if(thisInterval == 4) {
    		if(model.getMode().equals("Ionian")) {
    			if((pitch == -1 && lastPitch == 3) || (pitch == 3 && lastPitch == -1))
    				return false;
    		}
    		else if(model.getMode().equals("Dorian")) {
    			if((pitch == -2 && lastPitch == 2) || (pitch == 2 && lastPitch == -2))
    				return false;
    		}
    		else if(model.getMode().equals("Phrygian")) {
    			if((pitch == -3 && lastPitch == 1) || (pitch == 1 && lastPitch == -3))
    				return false;
    		}
    		else if(model.getMode().equals("Lydian")) {
    			if((pitch == -4 && lastPitch == 0) || (pitch == 0 && lastPitch == -4)
    					|| (pitch == 3 && lastPitch == 7) || (pitch == 7 && lastPitch == 3))
    				return false;
    		}
    		else if(model.getMode().equals("Mixolydian")) {
    			if((pitch == -5 && lastPitch == -1) || (pitch == -1 && lastPitch == -5)
    					|| (pitch == 2 && lastPitch == -2) || (pitch == -2 && lastPitch == 2))
    				return false;
    		}
    		else if(model.getMode().equals("Aeolian")) {
    			if((pitch == -6 && lastPitch == -2) || (pitch == -2 && lastPitch == -6)
    					|| (pitch == 1 && lastPitch == -3) || (pitch == -3 && lastPitch == 1))
    				return false;
    		}
    	}
    	
    	//step-leap ratio
    	if(stepsSoFar != -1 && leapsSoFar != -1) {
    		double ratio1 = (double) stepsSoFar/leapsSoFar;
    		if(!(ratio1 <= 1.5) || !(ratio1 >= 0.8))
    			return false;
    	}
    	
    	//large leap (4+)- smaller interval ratio (3-)
    	if(largeLeapsSoFar != -1 && smallerIntervalsSoFar != -1) {
    		double ratio2 = (double) largeLeapsSoFar/smallerIntervalsSoFar;
    		if(!(ratio2 <= 0.25))
    			return false;
    	}
    	
    	//no leaps larger than 5th aside from leap to FP
    	if(thisInterval >= 5)
    		return false;
    	
    	//is a leap followed/preceded by motion in opposite direction? - any intervals above 5th must have both
    	if(Math.abs(thisInterval) > 1) { //leap
    		if(notesToBeComposed == 1) { //last note of section to be composed?
    			if(thisInterval > 0) { //up
    				if(!(lastInterval < 0 || nextInterval < 0)) //if either are opposite direction
    					return false;
    			}
    			else { //down
    				if(!(lastInterval > 0 || nextInterval > 0))
    					return false;
    			}
    		}
    		
    		boolean aboveFifth = (Math.abs(lastInterval) >= 5);
    		if(aboveFifth) {
    			if(lastInterval < 0 && (thisInterval < 0))
    				return false;
    			else if(lastInterval > 0 && (thisInterval > 0))
    				return false;
    		}
    		
    		if(thisInterval > 0) { //up
    			if(lastInterval > 1) { //if last interval is also up and is leap
    				if(twoIntervalsAgo > 0) //if interval before that is also up
    					return false;
    			}
    		}
    		else { //thisInterval < 0 = down
    			if(lastInterval < -1) { //if last interval is also down and is leap
    				if(twoIntervalsAgo < 0) //if interval before that is also down
    					return false;
    			}
    		}
    	}
    	
    	//interval-notes to definition ratio
    	double ratio3 = (double) nextInterval/notesToBeComposed;
    	if(ratio3 > 2.0)
    		return false; //can't wander too far away
    	
    	return true;
    	
    }
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
