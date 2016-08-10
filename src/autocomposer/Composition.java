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
    public CompositionInfo info;
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
        cantusFirmus[0] = new Note(model);
        cantusFirmus[cantusFirmus.length-1] = cantusFirmus[0];
        
		int focalPoint = this.determineFocalPointLocation();
		info.setFocalPoint(focalPoint);
		
		int preFPContourType = this.determinePreFPContour();
		info.setPreFPContourType(preFPContourType);
		
		//these variables will be used later in the note-to-note composing methods
		int firstBeginPoint = 1; //note-by-note begins composing here- always begins at 2nd note
    	int firstEndPoint; //note-by-note stops composing here
    	Note lastNote = cantusFirmus[0];
    	Note twoNotesAgo = null; //note doesn't exist
    	Note threeNotesAgo = null;
    	
    	if(preFPContourType == 1 || preFPContourType == 2) { //both types are similar as they both share a "pre focal point" note.
    		//In type 1, it is below the tonic in pitch
    		//In type 2, it is equal to the tonic in pitch
    		
    		int tonicToPreFPInterval = this.determineTonicToPreFPInterval();

    		Note preFPNote = new Note(cantusFirmus[0],tonicToPreFPInterval,model);
    		
    		//in preFPContourType 1, preFPNote is lowest note in the cantus firmus
    		if(preFPContourType == 1)
    			info.setMinPitch(preFPNote.getRelativePitch());
    		
    		//special structures - additional note, 5th + 4th, triad.
    		//requires for the focal point to be 6th note or more.
    		boolean specialStructureUsed = false;
    		
    		if(focalPoint >= 5) {
    			double d = Math.random();
    			if(d < 0.1) { //structure 1 - additional note
        			int leapToAdditionalInterval = this.determineLeapToAdditionalInterval(preFPNote);

    				if(leapToAdditionalInterval != 0) { //there exists a legal interval
    					this.composeAdditionalNoteStructure(preFPNote,leapToAdditionalInterval);
    					specialStructureUsed = true;
    				}
        		}
        		else if(d < 0.2) { //structure 2 - 5th + 4th
        	    	Note fifth = new Note(cantusFirmus[0],preFPNote.getRelativePitch()+4,model);
        	    	Note focal = new Note(cantusFirmus[0],preFPNote.getRelativePitch()+7,model);
        			
        			boolean valid = this.checkFifthPlusFourth(preFPNote,fifth,focal);
        			
        			if(valid) {
        				this.composeFifthFourthStructure(preFPNote, fifth, focal);
        				specialStructureUsed = true;
        			}
        		}
        		else if(d < 0.9) { //structure 3 - triad
        			//note: the probability is so high here, as there are several strict conditions that need to be met
        			//in order for a triad to be composed (e.g. leapTOFPInterval must be 4 or 5). It is to make up for the restraints.
        			int leapToFPInterval = this.determineLeapToFPInterval(preFPNote,true);

        			Note middle = null;
        			Note focal = null;
        			boolean triadPossible = (leapToFPInterval == 4) || (leapToFPInterval == 5);
        			String position = "";
        			
        			if(leapToFPInterval == 4) { //root position triad
        				middle = new Note(cantusFirmus[0],preFPNote.getRelativePitch()+2,model);
        				focal = new Note(cantusFirmus[0],preFPNote.getRelativePitch()+4,model);
            			position = "root position";
        			}
        			else if(leapToFPInterval == 5) {
            			double x = Math.random();
            			if(x < 0.5) { //first inversion triad
            				middle = new Note(cantusFirmus[0],preFPNote.getRelativePitch()+2,model);
            				focal = new Note(cantusFirmus[0],preFPNote.getRelativePitch()+5,model);
            				position = "first inversion";
            			}
            			else { //second inversion triad
            				middle = new Note(cantusFirmus[0],preFPNote.getRelativePitch()+3,model);
            				focal = new Note(cantusFirmus[0],preFPNote.getRelativePitch()+5,model);
            				position = "second inversion";
            			}
            		}
        			
        			if(triadPossible) {
            			boolean valid = this.checkTriadValidity(preFPNote, middle, focal, position);
            			if(valid) {
            				this.composeTriadStructure(preFPNote, middle, focal);
            				specialStructureUsed = true;
            			}
            		}
        		}
    		}

    		//no special structure used
    		if(!specialStructureUsed) {
    			int leapToFPInterval = this.determineLeapToFPInterval(preFPNote,false);

                Note focal = new Note(cantusFirmus[0],preFPNote.getRelativePitch()+leapToFPInterval,model);
                cantusFirmus[focalPoint-1] = preFPNote;
                cantusFirmus[focalPoint] = focal;
    		}

    	}
    	else { //precontour = type 3
        	int tonicToFPInterval = this.determineFocalPoint();

        	Note focal = new Note(cantusFirmus[0],tonicToFPInterval,model);
        	
        	cantusFirmus[focalPoint] = focal;
        }
        	/*
        	boolean focalPointCanBeFour = true;
        	if(preFPContourType == 2) { //tonic back to tonic, leap to FP
            	relativePitches[focalPoint - 2] = 0;
            	firstEndPoint = focalPoint - 2;
            }
            else { //preFPContourType == 3, progressive motion to FP throughout
            	firstEndPoint = focalPoint - 1;
            	
            	double x = Math.random();
            	if(x < 0.25) { //special structure- 1,3,4, first three notes
            		relativePitches[1] = 2;
            		relativePitches[2] = 3;
            		firstBeginPoint = 3;
            		focalPointCanBeFour = false;
            		lastPitch = relativePitches[2];
            		twoPitchesAgo = relativePitches[1];
            		threePitchesAgo = relativePitches[0];
            		info.incrementLeapsSoFar(1);
            		info.incrementStepsSoFar(1);
            		info.incrementSmallerIntervalsSoFar(2);
            	}
            }
        	relativePitches[focalPoint - 1] = this.determineFocalPoint(preFPContourType,focalPointCanBeFour)-1;
        	info.setMaxPitch(relativePitches[focalPoint - 1]);
        	*/
        
        
        /*
        int notesInSection = firstEndPoint - firstBeginPoint;
        
        
        //compose pre focal point
        int[] section = this.composeNoteByNote(notesInSection,lastPitch,twoPitchesAgo,threePitchesAgo,relativePitches[firstEndPoint],true);
        for(int x = firstBeginPoint; x < firstEndPoint; x++) {
        	relativePitches[x] = section[x - firstBeginPoint];
        }
        
        int notesInSection2 = (cantusFirmus.length - 1) - focalPoint;
        
        //compose post focal point
        int[] section2 = this.composeNoteByNote(notesInSection2,relativePitches[focalPoint - 1],relativePitches[focalPoint - 2],relativePitches[focalPoint - 3],relativePitches[cantusFirmus.length - 1],false);
        for(int x = focalPoint; x < cantusFirmus.length - 1; x++) {
        	relativePitches[x] = section2[x - focalPoint];
        }
        
        
        //determine 2nd to last note
        double z = Math.random();
        if(z < 0.5)
        	relativePitches[cantusFirmus.length - 2] = 1; //step down
        else
        	relativePitches[cantusFirmus.length - 2] = -1; //step up
        
        for(int x = 0; x < relativePitches.length; x++) {
        	cantusFirmus[x] = NoteUtilities.convertRelativeToNote(relativePitches[x], model);
        }
        
        
        //used for testing
        for(int x = 0; x < relativePitches.length; x++) {
        	System.out.println(relativePitches[x]);
        }
        */
    }
    
    
    
    //composeCantusFirmus() helper methods
    
    
    //special structure composing methods
    
    //additional note
    private void composeAdditionalNoteStructure(Note preFPNote,int leapToAdditionalInterval) {	
    	Note additional = new Note(cantusFirmus[0],preFPNote.getRelativePitch()+leapToAdditionalInterval,model);
		Note focal = new Note(cantusFirmus[0],preFPNote.getRelativePitch()+leapToAdditionalInterval+1,model); //exactly one note above
    	
		int focalPoint = info.getFocalPoint();
		
		cantusFirmus[focalPoint-2] = preFPNote;
		cantusFirmus[focalPoint-1] = additional;
		cantusFirmus[focalPoint] = focal;
		
		info.setMaxPitch(focal.getRelativePitch());
    }
    //5th + 4th
    private void composeFifthFourthStructure(Note preFPNote,Note fifth,Note focal) {
    	int focalPoint = info.getFocalPoint();
    	
    	cantusFirmus[focalPoint-2] = preFPNote;
		cantusFirmus[focalPoint-1] = fifth;
		cantusFirmus[focalPoint] = focal;
    }
    //triad
    private void composeTriadStructure(Note bottom,Note middle,Note top) {
        int focalPoint = info.getFocalPoint();
    	
    	cantusFirmus[focalPoint-2] = bottom;
		cantusFirmus[focalPoint-1] = middle;
		cantusFirmus[focalPoint] = top;
    }
    
    
    //other helper methods
    private int determineFocalPointLocation() //helper method of composeCantusFirmus. Determines the index of the focal point in the CF.
    {
    	//based entirely on randomization.
    	int n = model.getMeasures();
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
    private int determinePreFPContour() { //helper method of composeCantusFirmus. Determines the contour from the first note to the FP.
    	double x = Math.random();
    	
    	if(x < 0.3333)
    		return 1; //Type 1 = go down to low point and leap to focal point
    	else if(x < 0.6666)
    		return 2; //Type 2 = travel, return to tonic and leap to focal point
    	else
    		return 3; //Type 3 = gradual motion upwards towards focal point
    }
    private int determineTonicToPreFPInterval() { //Case I helper method. Determines the interval between the tonic and pre focal point, in relative pitches.
    	//precondition: preFPContourType is either 1 or 2.
    	if(info.getPreFPContourType() == 2)
    		return 0; //same note as tonic, no interval
    	else {
    		ArrayList<Integer> validIntervals = new ArrayList<Integer>();
        	
        	validIntervals.add(-1); //2nd
        	validIntervals.add(-2); //3rd
        	validIntervals.add(-3); //4th
        	validIntervals.add(-4); //5th
        	
        	int x = (int) (Math.random() * validIntervals.size());
        	
        	return validIntervals.get(x);
    	}
    }
    public int determineLeapToAdditionalInterval(Note preFPNote) { //composeCantusFirmus helper method. Determines the interval between the low point and additional note.
    	//precondition: preFPContourType 1 or 2
    	//checks fourth(type 2 only) fifth and sixth, the acceptable PF-AN intervals.
    	ArrayList<Integer> validIntervals = new ArrayList<Integer>();

    	for(int i = 3; i <= 5; i++) { //3-Perfect 4th, 4-Perfect 5th, 5-ascending m6
    		if(!(i == 3 && info.getPreFPContourType() != 2)) {
    			Note proposed = new Note(cantusFirmus[0],preFPNote.getRelativePitch()+i,model);

    			int difference = proposed.midiValue() - preFPNote.midiValue(); //difference in half steps between the two pitches
    		
    			//to ensure steepness from focal point to tonic is legal
    			int length = cantusFirmus.length - info.getFocalPoint();
    			boolean notTooSteep = this.checkSteepness(length,proposed.getRelativePitch()+1);
    			
    			if(proposed.getRelativePitch() >= 3 && notTooSteep) { //focal pt must be 4th or above tonic
    				if(difference == 5) { //perfect fourth
    					if(info.getPreFPContourType() == 2)
    						validIntervals.add(i);
    				}
    			    else if(difference == 7) //perfect fifth
    					validIntervals.add(i);
    				else if(difference == 8) //ascending m6
    					validIntervals.add(i);
    			}
    		}
    	}
    	
    	if(validIntervals.size() == 0)
    		return 0; //no valid intervals
    	    	
    	int x = (int) (Math.random()*validIntervals.size());
    	
    	return validIntervals.get(x);
    }
    public int determineLeapToFPInterval(Note preFPNote,boolean triad) { //Helper method for composeCantusFirmus.
    	//precondition: preFPContourType, 1 or 2
    	//lowest acceptable interval = 5th for type 1, 4th for type 2. Largest = 8th. Checks validity of all in between. (except 7th)
    	//conditions: interval between tonic and focal point must be a 4th or greater. No tri-tones. Not too steep.
    	//triad: major 6th acceptable for interval.
    	ArrayList<Integer> validIntervals = new ArrayList<Integer>();
    	for(int i = 3; i <= 7; i++) { //3 = 4th, 4= 5th, 5 = asc m6, 7 = octave
    		if(i != 6 && !(i == 3 && info.getPreFPContourType() != 2) ) {  
    			boolean valid = true;
    			
    			Note proposed = new Note(cantusFirmus[0],preFPNote.getRelativePitch()+i,model);
    			
    			int difference = proposed.midiValue() - preFPNote.midiValue();
    			
    			if(difference == 6) //tri-tone
    				valid = false;
    			
    			if(difference == 9 && !triad) //no major 6ths, except with triad
    				valid = false;
    			
    			if(proposed.getRelativePitch() < 3) //less than a 4th from tonic
    				valid = false;
    			
    			int length = cantusFirmus.length - info.getFocalPoint();
    			if(!this.checkSteepness(length, proposed.getRelativePitch())) //too steep
    				valid = false;
    			
    			if(valid) {
    				validIntervals.add(i);
    				
    			}
    		}
    	}
    	
    	int x = (int) (Math.random() * validIntervals.size());
    	
    	return validIntervals.get(x);
    }
    public boolean checkFifthPlusFourth(Note preFPNote,Note fifth,Note focal) { //composeCantusFirmus helper method. Checks validity of special structure 5th + 4th
    	//precondition: preFPContourType is either 1 or 2.
    	//also, interval between firstNote and fifth is a 5th of some kind. 
    	
    	//check steepness between focal and tonic
		int length = cantusFirmus.length - info.getFocalPoint();
        boolean notTooSteep = this.checkSteepness(length, focal.getRelativePitch());
        
        if(notTooSteep) {
        	int difference = fifth.midiValue() - preFPNote.midiValue(); 
        	
        	if(difference == 7)
        		return true;
        	else
        		return false;
        }
        else
        	return false;
    }
    public boolean checkTriadValidity(Note bottom,Note middle,Note top,String position) { //helper method for composeCantusFirmus. Checks for tri tones.
    	//precondition: position consistent with note names of bottom, middle, and top.
    	boolean valid = true;
    	
    	int bottomMIDIValue = bottom.midiValue();
    	int middleMIDIValue = middle.midiValue();
    	int topMIDIValue = top.midiValue();
    	
    	if(position.equals("root position")) {
    		if(topMIDIValue - bottomMIDIValue == 6)
    			valid = false;
    	}
    	else if(position.equals("first inversion")) {
    		if(topMIDIValue - middleMIDIValue == 6)
    			valid = false;
    	}
    	else if(position.equals("second inversion")) {
    		if(middleMIDIValue - bottomMIDIValue == 6)
    			valid = false;
    	}
    		
        return valid;
    }
    private int determineFocalPoint() { //helper method for composeCantusFirmus, case 3.
    	//possibilities: perfect 4th, perfect 5th, sixth, minor 7th, or octave
    	//as usual, no tri-tones, Major 7ths
    	//as there is no leap up to FP, leap rules don't apply here
    	ArrayList<Integer> validIntervals = new ArrayList<Integer>();
    	
    	for(int i = 3; i <= 7; i++) { //3-perfect 4th, 4-perfect 5th, 5-sixth, 6-minor 7th, 7-octave
    		Note proposed = new Note(cantusFirmus[0],i,model);
    		
    		//to ensure steepness from focal point to tonic is legal- from both the beginning and end
    		int lengthFromBeginning = info.getFocalPoint() + 1;
			int lengthToEnd = cantusFirmus.length - info.getFocalPoint();
			boolean notTooSteepFromBeginning = this.checkSteepness(lengthFromBeginning,proposed.getRelativePitch());
			boolean notTooSteepToEnd = this.checkSteepness(lengthToEnd,proposed.getRelativePitch());
    		
			if(notTooSteepFromBeginning && notTooSteepToEnd) {
				int difference = proposed.midiValue() - cantusFirmus[0].midiValue();
    		
				if(difference == 5) //P4
					validIntervals.add(i);
				else if(difference == 7) //P5
					validIntervals.add(i);
				else if((difference == 8) || (difference == 9)) //sixth
					validIntervals.add(i);
				else if(difference == 10) //minor 7th
					validIntervals.add(i);
				else if(difference == 12) //octave
					validIntervals.add(i);
			}
    	}
    	
    	int x = (int) (Math.random() * validIntervals.size());
		
		return validIntervals.get(x);
    }
    private boolean checkSteepness(int length,int interval) { //check the steepness of the second half of the cantusFirmus.
    	//length: # of notes from focal point to end
    	//interval = from FP to tonic, in relative pitches
    	//precondition: length is between 5 and 8
    	if(interval <= length) //interval must never greater than length
    		return true; //valid
    	else
    		return false;
    }
    
    /*
    public int[] composeNoteByNote(int notesInSection,int lastPitch,int twoPitchesAgo,int threePitchesAgo,int nextPitch,boolean preFP) { //note-by-note algorithm
    	ArrayList<CounterpointError> errors = new ArrayList<CounterpointError>();
    	
    	//for testing
    	
    	System.out.println(info.getLargeLeapsSoFar());
    	System.out.println(info.getLeapsSoFar());
    	System.out.println(info.getMaxPitch());
    	System.out.println(info.getSmallerIntervalsSoFar());
    	System.out.println(info.getStepsSoFar());
    	
    	
    	int[] arr = new int[notesInSection];
    	int notesToBeComposed = notesInSection;
    	boolean recomposing = false;
    	int recomposingIndex = 0;
    	int toEndInterval = nextPitch - lastPitch;
    	int last = lastPitch;
    	int twoAgo = twoPitchesAgo;
    	int threeAgo = threePitchesAgo;
    	
    	//for testing
    	int iterations = 0;
    	
    	for(int x = 0; x < notesInSection; x++) {
    		//used for testing
    		if(iterations > 12)
    			break;
    		
    		boolean secondToLast = false;
    		if(!preFP && notesToBeComposed == 1)
    			secondToLast = true;
    		
    		boolean skipRest = false;
    		System.out.println(x);
    		System.out.println();
    		/*
    		//special structures
    		boolean up;
			if(x == 0)
				up = (nextPitch > lastPitch);
			else
				up = (nextPitch > arr[x-1]);
			
    		double y = Math.random();
    		System.out.println(y);
    		double z = Math.random();
    		int length; //between 4 and 6
    		if(y < 0.1 && notesToBeComposed >= 4) { //consecutive steps
    			if(notesToBeComposed == 4)
    				length = 4;
    			else if(notesToBeComposed == 5)
    				length = (int)(z*2)+4;
    			else //notesToBeComposed >= 6
    				length = (int)(z*3)+4;
    			
    			boolean valid;
    			if(x == 0) {
    				if(up)
        				valid = this.checkNoteValidity(arr[x-1]+length,arr[x-1]+(length-1),arr[x-1]+(length-2),arr[x-1]+(length-3),nextPitch,notesToBeComposed-length,false); //-1 = do not consider these parameters
        			else
        				valid = this.checkNoteValidity(arr[x-1]-length,arr[x-1]+(-length+1),arr[x-1]+(-length+2),arr[x-1]+(length-3),nextPitch,notesToBeComposed-length,false); //-1 = do not consider these parameters
    			}
    			else {
    				if(up)
    					valid = this.checkNoteValidity(arr[x-1]+length,arr[x-1]+(length-1),arr[x-1]+(length-2),arr[x-1]+(length-3),nextPitch,notesToBeComposed-length,false); //-1 = do not consider these parameters
        			else
        				valid = this.checkNoteValidity(arr[x-1]-length,arr[x-1]+(-length+1),arr[x-1]+(-length+2),arr[x-1]+(length-3),nextPitch,notesToBeComposed-length,false); //-1 = do not consider these parameters
    			}
    			
    			if(valid) {
    				for(int a = 0; (a < length) && ((x+a) < notesInSection);a++) {
    					if(up) {
    						arr[x+a] = arr[x-1]+(x+1);
    						notesToBeComposed--;
    					}
    					else {
    						arr[x+a] = arr[x-1]-(x+1);
    						notesToBeComposed--;
    					}
    				}
    				skipRest = true;
    				x += length;
    				info.incrementStepsSoFar(length);
    				info.incrementSmallerIntervalsSoFar(length);
    				last = arr[x-1];
    				twoAgo = arr[x-2];
    				threeAgo = arr[x-3];
    			}
    		}
    		else if(y < 0.2 && notesToBeComposed >= 2) { //triad
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
    			if(x == 0) { //triad must always go in direction of nextPitch
    				if(up) {
        			    next = lastPitch + triadIntervals[1]; 
        			    after = lastPitch + triadIntervals[2];
        			}
        			else {
        				if(position.equals("root position"))
        				    next = lastPitch - triadIntervals[1]; 
        				else
        					next = lastPitch - (5- triadIntervals[1]);
        				after = lastPitch - triadIntervals[2];
        			}
    			}
    			else {
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
    			
    			//temporary, in order for checkNoteValidity to work
    			notesToBeComposed -= 2;
				info.incrementLeapsSoFar(2);
				if(triadIntervals[2] == 5) {
					info.incrementLargeLeapsSoFar(1);
					info.incrementSmallerIntervalsSoFar(1);
				}
				else {
					info.incrementSmallerIntervalsSoFar(2);
				}
    			
    			boolean triadIsValid;
    			if(x == 0)
    				triadIsValid = (this.checkNoteValidity(after, next, lastPitch, twoPitchesAgo, nextPitch, notesToBeComposed,false) && this.checkTriadValidity(lastPitch,next,after,position));
    			else if(x == 1)
    				triadIsValid = (this.checkNoteValidity(after, next, arr[0], lastPitch, nextPitch, notesToBeComposed,false) && this.checkTriadValidity(arr[x-1],next,after,position));
    			else
    				triadIsValid = (this.checkNoteValidity(after, next, arr[x-1], arr[x-2], nextPitch, notesToBeComposed,false) && this.checkTriadValidity(arr[x-1],next,after,position));
    			
    			
    			if(triadIsValid) {
    				arr[x] = next;
    				arr[x+1] = after;
    				last = arr[x-1];
    				twoAgo = arr[x];
    				if(x == 0)
    					threeAgo = lastPitch;
    				else
    					threeAgo = arr[x-1];
    				skipRest = true;
    				x++;
    			}
    			else { //reverse
        			notesToBeComposed += 2;
    				info.incrementLeapsSoFar(-2);
    				if(triadIntervals[2] == 5) {
    					info.incrementLargeLeapsSoFar(-1);
    					info.incrementSmallerIntervalsSoFar(-1);
    				}
    				else {
    					info.incrementSmallerIntervalsSoFar(-2);
    				}
    			}
    		}
    		
    		if(!skipRest) {
    			int nextNote = this.decideNextNote(last,twoAgo,threeAgo,nextPitch,notesToBeComposed,secondToLast);
        		if(nextNote == 100 && x >= 1) { //no valid notes available
        			//store error
        			if(!recomposing) {
        			    errors.add(new CounterpointError(arr[x-1],x));
        			    recomposing = true;
        			}
        			else
        			    errors.get(recomposingIndex).addToError(arr[x-1]);
        			
        			arr[x-1] = 0; //uncompose
        			notesToBeComposed++;
        			if(x >= 2)
        				toEndInterval = nextPitch - arr[x-2];
        			else //x = 1
        				toEndInterval = nextPitch - lastPitch;
        			x -= 2; //go back
        		}
        		else {
        			if(recomposing) {
        				recomposing = false;
        				recomposingIndex++;
        			}
        			int newInt = nextNote - last;
        			if(Math.abs(newInt) >= 3)
        				info.incrementLargeLeapsSoFar(1);
        			else
        				info.incrementSmallerIntervalsSoFar(1);
        			if(Math.abs(newInt) >= 2)
        				info.incrementLeapsSoFar(1);
        			else
        				info.incrementStepsSoFar(1);
        			arr[x] = nextNote;
        			last = arr[x];
        			if(x >= 1)
        				twoAgo = arr[x-1];
        			if(x >= 2)
        				threeAgo = arr[x-2];
        			notesToBeComposed--;
        			toEndInterval = nextPitch - arr[x];
        		}
    		}
    		
    		//used for testing
    		iterations++;
    		
    		/*
    		if(notesToBeComposed == 0) { //check final interval
        		boolean finalCheck = this.checkNoteValidity(nextPitch,arr[arr.length-1],arr[arr.length-2],arr[arr.length-3],100,0,true);
        		if(!finalCheck) {
        			errors.add(new CounterpointError(arr[x-1],x+1));
    			    recomposing = true;
    			    arr[x] = 0;
    			    notesToBeComposed++;
    			    toEndInterval = nextPitch - arr[x-1];
        			x--;
        		}
    		}
    		
    		
    	}
    	
    	//for testing
    	System.out.println();
    	
    	for(int x = 0; x < errors.size(); x++) {
    		System.out.println(errors.get(x).endIndex);
    		System.out.println();
    		for(int y = 0; y < errors.get(x).error.size(); y++) {
    			System.out.println(errors.get(x).error.get(y));
    		}
    	}
    	
    	System.out.println();
    	
    	return arr;
    }
    
    private boolean checkTriadValidity(int first,int second,int third,String position) { //check for tri tones
    	boolean valid;
    	if(position.equals("root position")) {
    		valid = this.checkForTriTones(first,third,4);
    	}
    	else if(position.equals("first inversion")) {
    		valid = this.checkForTriTones(second,third,3);
    	}
    	else //position = "second inversion"
    		valid = this.checkForTriTones(first, second, 3);
    	return valid;
    }
    
    
    private int decideNextNote(int lastPitch,int twoPitchesAgo,int threePitchesAgo,int nextPitch,int notesToBeComposed,boolean secondToLast) {
    	ArrayList<Integer> range = this.listRange(lastPitch);
    	ArrayList<Integer> validRange = this.listValidRange(range, lastPitch, twoPitchesAgo, threePitchesAgo, nextPitch, notesToBeComposed,secondToLast);
    	
    	if(validRange.size() == 0) {
    		return 100;
    	}
    	int index = (int) Math.random() * validRange.size();
    	int proposedNote = validRange.get(index);
    	return proposedNote;
    	
    }
    private ArrayList<Integer> listRange(int lastPitch) {
    	ArrayList<Integer> range = new ArrayList<Integer>();
    	for(int x = 1; x <= 4; x++) { //1=2nd
    		range.add(lastPitch+x); //above
    		range.add(lastPitch-x); //below
    	}
    	return range;
    }
    private ArrayList<Integer> listValidRange(ArrayList<Integer> range, int lastPitch,int twoPitchesAgo,int threePitchesAgo,int nextPitch,int notesToBeComposed,boolean secondToLast) {
    	ArrayList<Integer> validRange = new ArrayList<Integer>();
    	for(int x = 0; x < range.size(); x++) {
    		boolean valid = this.checkNoteValidity(range.get(x),lastPitch,twoPitchesAgo,threePitchesAgo,nextPitch,notesToBeComposed,true,secondToLast);
    		if(valid)
    			validRange.add(range.get(x));
    	}
    	return validRange;
    }
    private boolean checkNoteValidity(int pitch,int lastPitch,int twoPitchesAgo,int threePitchesAgo,int nextPitch,int notesToBeComposed,boolean considerIntervals,boolean secondToLast) {
    	int thisInterval = pitch - lastPitch; //1= 2nd
    	int lastInterval;
    	if(twoPitchesAgo != 100)
    	    lastInterval = lastPitch - twoPitchesAgo;
    	else
    		lastInterval = 100;
    	int twoIntervalsAgo;
    	if(threePitchesAgo != 100)
    	    twoIntervalsAgo = twoPitchesAgo = threePitchesAgo;
    	else
    		twoIntervalsAgo = 100;
    	int nextInterval = nextPitch - pitch;
    	
    	//if second to last note- must be stepwise from tonic
    	if(secondToLast) {
    		if(pitch != 1 && pitch != -1)
    			return false;
    	}
    	
    	//no tri-tones
    	boolean triTones = this.checkForTriTones(lastPitch,pitch,thisInterval);
    	if(triTones)
    		return false;
    	
    	//step-leap ratio
    	if(considerIntervals && info.getLeapsSoFar() != 0) {
    		double ratio1 = (double) info.getStepsSoFar()/info.getLeapsSoFar();
    		if(!(ratio1 <= 1.5) || !(ratio1 >= 0.8))
    			return false;
    	}
    	
    	//large leap (4+)- smaller interval ratio (3-)
    	if(considerIntervals && info.getSmallerIntervalsSoFar() != 0) {
    		double ratio2 = (double) info.getLargeLeapsSoFar()/info.getSmallerIntervalsSoFar();
    		if(!(ratio2 <= 0.25))
    			return false;
    	}
    	
    	//is a leap followed/preceded by motion in opposite direction? - any intervals above 5th must have both
    	if(Math.abs(thisInterval) > 1 && lastInterval != 100) { //leap
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
    		
    		if(twoIntervalsAgo != 100) {
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
    	}
    	
    	//interval-notes to distance ratio
    	if(notesToBeComposed != 0) {
    		double ratio3 = (double) Math.abs(nextInterval/notesToBeComposed);
        	if(ratio3 > 2.0)
        		return false; //can't wander too far away
    	}
    	
    	//can't reach predetermined max and min pitches
    	if(pitch >= info.getMaxPitch())
    		return false;
    	if(pitch <= info.getMinPitch())
    		return false;
    	
    	System.out.print(true);
    	System.out.println(pitch);
    	System.out.println();
    	
    	return true;
    	
    }
    */
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
