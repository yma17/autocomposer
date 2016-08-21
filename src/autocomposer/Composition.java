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
        cantusFirmus[0] = new Note(model);
        cantusFirmus[cantusFirmus.length-1] = cantusFirmus[0];
        
        //focal point = a "peak"-like point that the notes gradually build up towards and down from
		int focalPoint = this.determineFocalPointLocation();
		info.setFocalPoint(focalPoint); //info updated whenever any necessary information is determined
		
		//preFPContourType refers to contours that lead up to the focal point - 3 types
		//1 - notes go down to a low point, then leap to the focal point
		//2 - notes return to tonic, then leap to the focal point
		//3 - notes gradually build upward towards focal point
		int preFPContourType = this.determinePreFPContour();
		info.setPreFPContourType(preFPContourType);
		
		//update info- note-by-note composition beginning and end points to be used later
		info.setPreFPBeginPoint(1); //2nd note
		info.setPostFPBeginPoint(focalPoint+1); //note after focal point
		info.setPostFPEndPoint(cantusFirmus.length-2); //2nd to last note
		//preFPEndPoint to be set later in pre-determined note composition.
		//when preFPContourType is 1 or 2, it is the note before the preFPNote.
		//when preFPContourType is 3, it is the note before the focal point.
		
    	//pre-determined composition begins here. Notes such as the focal point and others are composed ahead of the main note-by-note composition.
    	
    	if(preFPContourType == 1 || preFPContourType == 2) { //both types are similar as they both share a "pre focal point" note.
    		//In type 1, it is below the tonic in pitch
    		//In type 2, it is equal to the tonic in pitch
    		
    		int tonicToPreFPInterval = this.determineTonicToPreFPInterval();

    		//basic method - create a Note object, and if determined to be valid along with the rest of the structure, compose it to the cantusFirmus array
    		
    		Note preFPNote = new Note(cantusFirmus[0],tonicToPreFPInterval,model); //a tenative note to go immediately before the focal point
    		
    		//in preFPContourType 1, preFPNote is lowest note in the cantus firmus
    		if(preFPContourType == 1)
    			info.setMinPitch(preFPNote.getRelativePitch());
    		
    		
    		//special structures are structures that are variations of the intended structure for each preFPContourType.
    		//these structures require additional methods for deciding, checking, and verifying.
    		//special structures possible here - additional note, 5th + 4th, triad.
    		boolean specialStructureUsed = false;
    		
    		if(focalPoint >= 5) { //requires for the focal point to be 6th note or more so that they don't feel "compressed" within the line.
    			double d = Math.random();
    			if(d < 0.1) { //structure 1 - additional note one step down from focal point, between preFP and focal point. (e.g. C-G-A)
        			int leapToAdditionalInterval = this.determineLeapToAdditionalInterval(preFPNote);

        			//leapToAdditionalInterval = 0 means that there are no legal intervals
    				if(leapToAdditionalInterval != 0) {
    					this.composeAdditionalNoteStructure(preFPNote,leapToAdditionalInterval); //compose the planned structure
    					specialStructureUsed = true; //to end composition of pre-determined notes
    					
    					//update info
    					info.incrementLeapsSoFar(1);
    					info.incrementLargeLeapsSoFar(1); 
    					info.incrementStepsSoFar(1);
    					info.incrementSmallerIntervalsSoFar(1);
    					info.setPreFPEndPoint(focalPoint-3); //3 notes before focal pt
    				}
    				//if no legal intervals, don't employ any special structures
        		}
        		else if(d < 0.2) { //structure 2 - 5th + 4th - additional note between preFP and FP a perfect fifth above preFP. (e.g. D-A-D)
        	    	Note fifth = new Note(cantusFirmus[0],preFPNote.getRelativePitch()+4,model);
        	    	Note focal = new Note(cantusFirmus[0],preFPNote.getRelativePitch()+7,model);
        			
        			boolean valid = this.checkFifthPlusFourth(preFPNote,fifth,focal);
        			
        			if(valid) {
        				this.composeFifthFourthStructure(preFPNote, fifth, focal);
        				specialStructureUsed = true;
        				
    					//update info
        				info.incrementLargeLeapsSoFar(2);
        				info.incrementLeapsSoFar(2);
        				info.setPreFPEndPoint(focalPoint-3);
        			}
        		}
        		else if(d < 0.9) { //structure 3 - triad with preFP as bottom note and FP as top note (e.g. C-E-G)
        			//note: the probability is so high here because there are several strict conditions that need to be met.
        			//e.g. in order for a triad to be composed (e.g. leapTOFPInterval must be 4 or 5) (fifth or sixrh).
        			int leapToFPInterval = this.determineLeapToFPInterval(preFPNote,true);

        			Note focal = new Note(cantusFirmus[0],preFPNote.getRelativePitch()+leapToFPInterval,model);
        			//the middle note of the triad, position depends on leapToFPInterval
        			Note middle = null;
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
            				position = "first inversion";
            			}
            			else { //second inversion triad
            				middle = new Note(cantusFirmus[0],preFPNote.getRelativePitch()+3,model);
            				focal = new Note(cantusFirmus[0],preFPNote.getRelativePitch()+5,model);
            				position = "second inversion";
            			}
            		}
        			
        			if(!position.equals("")) { //if initial conditions are met
            			boolean valid = this.checkTriadValidity(preFPNote, middle, focal, position);
            			if(valid) {
            				this.composeTriadStructure(preFPNote, middle, focal);
            				specialStructureUsed = true;
            				
            				//update info
            				info.incrementLeapsSoFar(2);
            				info.setPreFPEndPoint(focalPoint-3);
            				if(position.indexOf("inversion") >= 0) { //is an inversion triad
            					info.incrementLargeLeapsSoFar(1);
            					info.incrementSmallerIntervalsSoFar(1);
            				}
            				else //root position triad
            					info.incrementSmallerIntervalsSoFar(2);
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
                
                //update info
                info.incrementLargeLeapsSoFar(1);
                info.incrementLeapsSoFar(1);
                info.setMaxPitch(focal.getRelativePitch());
				info.setPreFPEndPoint(focalPoint-2);
    		}

    	}
    	else { //precontour = type 3
        	int tonicToFPInterval = this.determineFocalPoint();

        	Note focal = new Note(cantusFirmus[0],tonicToFPInterval,model);
        	
        	cantusFirmus[focalPoint] = focal;

        	//update info (no consecutive notes = no step/leap info to update)
        	info.setMaxPitch(focal.getRelativePitch());
        	info.setPreFPEndPoint(focalPoint-1);
        }
    	
    	//now that the pre-determined focal point composition is complete,
    	//the note-to-note and the rest of the composition of the CF begins.
    
    	
    	//compose empty section before focal point
    	//call note-by-note composition method
    	//(insert code here)
    	
    	//compose empty sectionafter focal point
    	//call note-by-note composition method
    	//(insert code here)
    }
    
    
    
    //composeCantusFirmus() helper methods
    
    
    //special structure composing methods
    
    //additional note
    private void composeAdditionalNoteStructure(Note preFPNote,int leapToAdditionalInterval) {	
    	Note additional = new Note(cantusFirmus[0],preFPNote.getRelativePitch()+leapToAdditionalInterval,model);
		Note focal = new Note(cantusFirmus[0],preFPNote.getRelativePitch()+leapToAdditionalInterval+1,model); //one note above
    	
		int focalPoint = info.getFocalPoint();
		
		cantusFirmus[focalPoint-2] = preFPNote;
		cantusFirmus[focalPoint-1] = additional;
		cantusFirmus[focalPoint] = focal;
		
		//update info
		info.setMaxPitch(focal.getRelativePitch());
    }
    //5th + 4th
    private void composeFifthFourthStructure(Note preFPNote,Note fifth,Note focal) {
    	int focalPoint = info.getFocalPoint();
    	
    	cantusFirmus[focalPoint-2] = preFPNote;
		cantusFirmus[focalPoint-1] = fifth;
		cantusFirmus[focalPoint] = focal;
		
		//update info
		info.setMaxPitch(focal.getRelativePitch());
    }
    //triad
    private void composeTriadStructure(Note bottom,Note middle,Note top) {
        int focalPoint = info.getFocalPoint();
    	
    	cantusFirmus[focalPoint-2] = bottom;
		cantusFirmus[focalPoint-1] = middle;
		cantusFirmus[focalPoint] = top;
		
		//update info
		info.setMaxPitch(top.getRelativePitch());
    }
    
    
    //other helper methods
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
    	
    	if(x < 0.3333)
    		return 1; //Type 1 = go down to low point and leap to focal point
    	else if(x < 0.6666)
    		return 2; //Type 2 = travel, return to tonic and leap to focal point
    	else
    		return 3; //Type 3 = gradual motion upwards towards focal point
    }
    private int determineTonicToPreFPInterval() { //Determines the interval between the tonic and pre focal point, in relative pitches.
    	//precondition: preFPContourType is either 1 or 2.
    	if(info.getPreFPContourType() == 2)
    		return 0; //same note as tonic, no interval
    	else {
    		ArrayList<Integer> validIntervals = new ArrayList<Integer>();
        	
    		//four intervals possible
        	validIntervals.add(-1); //2nd
        	validIntervals.add(-2); //3rd
        	validIntervals.add(-3); //4th
        	validIntervals.add(-4); //5th
        	
        	int x = (int) (Math.random() * validIntervals.size());
        	
        	return validIntervals.get(x);
    	}
    }
    public int determineLeapToAdditionalInterval(Note preFPNote) { //Determines the interval between the low point and additional note, in relative pitches.
    	//precondition: preFPContourType is either 1 or 2.
    	//precondition: additional note special structure is being implemented.
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
    public int determineLeapToFPInterval(Note preFPNote,boolean triad) { //Determines the interval between the low point and focal point, in relative pitches.
    	//precondition: preFPContourType is either 1 or 2.
    	//precondition: either triad or no special structure is being implemented.
    	//lowest acceptable interval = 5th for type 1, 4th for type 2. Largest = 8th. Checks validity of all in between. (except 7th)
    	//conditions: interval between tonic and focal point must be a 4th or greater. No tri-tones. Not too steep.
    	ArrayList<Integer> validIntervals = new ArrayList<Integer>();
    	for(int i = 3; i <= 7; i++) { //3 = 4th, 4= 5th, 5 = asc m6, 7 = octave
    		if(i != 6 && !(i == 3 && info.getPreFPContourType() != 2) ) {  
    			boolean valid = true;
    			
    			Note proposed = new Note(cantusFirmus[0],preFPNote.getRelativePitch()+i,model);
    			
    			int difference = proposed.midiValue() - preFPNote.midiValue();
    			
    			if(difference == 6) //no tri-tones allowed
    				valid = false;
    			
    			if(difference == 9 && !triad) //major 6th acceptable for interval for triad only.
    				valid = false;
    			
    			if(proposed.getRelativePitch() < 3) //less than a 4th from tonic
    				valid = false;
    			
    			int length = cantusFirmus.length - info.getFocalPoint();
    			if(!this.checkSteepness(length, proposed.getRelativePitch())) //too steep
    				valid = false;
    			
    			if(valid)
    				validIntervals.add(i);
    		}
    	}
    	
    	int x = (int) (Math.random() * validIntervals.size());
    	
    	return validIntervals.get(x);
    }
    public boolean checkFifthPlusFourth(Note preFPNote,Note fifth,Note focal) { //Checks validity of special structure 5th + 4th.
    	//precondition: preFPContourType is either 1 or 2.
    	//precondition: 5th+4th special structure is being implemented.
    	//precondition: interval between firstNote and fifth is a 5th of some kind. 
    	
    	//check steepness between focal and tonic
		int length = cantusFirmus.length - info.getFocalPoint();
        boolean notTooSteep = this.checkSteepness(length, focal.getRelativePitch());
        
        if(notTooSteep) {
        	int difference = fifth.midiValue() - preFPNote.midiValue(); 
        	
        	if(difference == 7) //Perfect 5th
        		return true;
        	else
        		return false;
        }
        else
        	return false;
    }
    public boolean checkTriadValidity(Note bottom,Note middle,Note top,String position) { //Checks for tri tones within proposed triads.
    	//precondition: triad special structure is being implemented.
    	//precondition: position consistent with note names of bottom, middle, and top.
    	boolean valid = true;
    	
    	int bottomMIDIValue = bottom.midiValue();
    	int middleMIDIValue = middle.midiValue();
    	int topMIDIValue = top.midiValue();
    	
    	//6 half steps = tri-tone
    	
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
    private int determineFocalPoint() { //the name of the method, basically.
    	//precondition: preFPContourType is 3.
    	//possibilities: perfect 4th, perfect 5th, sixth, minor 7th, or octave
    	//as usual, no tri-tones or Major 7ths
    	//as there is no single leap up to FP, leap rules don't apply here
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
    private boolean checkSteepness(int length,int interval) { //check the validity of the steepness of a length of notes given the interval.
    	//length: # of notes from focal point to end
    	//interval = from FP to tonic, in relative pitches
    	//why this is necessary: so a passage of notes going up or down does not feel "forced" due to the interval being too large.
    	if(interval <= length) //interval must never greater than length
    		return true; //valid
    	else
    		return false;
    }
    
    
    
    
    //note-by-note composition methods
    
  //basic method, steps to follow:
	
	/* Create a method called listRange that lists all possibilities of
	 * the next note to be composed as all notes from a 5th above the last note to the fifth below.
	 * 
	 * Then, for each proposed note, utilize the counterpoint guidelines to check the validity of each one 
	 * and create a valid list of possibilities.
	 * 
	 * Guidelines [and necessary parameters/information for method to verify/check these guidelines]:
	 * 1. raise leading tone at cadence in minor, G modes
	 * 		[mode, previous one/two notes and the note after (to decide if note is part of a cadence]
	 * 2. raise 6th and 7th scale degrees in ascending fragment 5-#6-#7-1 in minor modes
	 * 		[mode, previous one/two notes and the note after (to decide if note is part of that ascending fragment]
	 * 3. no repeated notes.
	 * 		[the previous note and/or the note after]
	 * 4. melodic motion mostly by steps. (Decision by the programmer: never more leaps than steps)
	 * 		[number of steps so far, number of leaps so far]
	 * 5. no augmented, diminished intervals (e.g. B-F)
	 * 		[the previous note and/or the note after]
	 * 6. leaps not larger than perfect 5th
	 * 		[no info necessary: listRange will ensure that all proposed notes are never over a 5th away]
	 * 7. all leaps either preceded or followed by motion in the opposite direction
	 * 		[the previous two notes and/or the note after]
	 * 8. ascending m6, octaves (only found in leap rom preFP to focal point, never elsewhere)
	 *    both preceded and followed by motion in the opposite direction
	 *		[TBD - not sure how this would work at this point]
	 * 9. Two successive leaps in same direction must be preceded and followed by motion in the opposite direction
	 *    and may outline only a major or minor triad (not a diminished triad)
	 *		[TBD - not sure how this would work at this point]
	 * 10. Last 3 or 4 pitches in a single direction should not stress a tri-tone (e.g. Bb-C-D-E) (dim. 5th is fine)
	 * 		[the previous three notes (composing from the last pitch in that passage)]
	 * 11. Last note approached by step from above or below.
	 * 		[the index of the note to be composed - is it cantusFirmus.length - 2?]
	 * 
	 * After list of valid intervals is created, a random one is selected as the next note.
	 * 
	 * The next note is composed next.
	 * 
	 * The entire process is contained within a for loop, with the index representing the indices of cantusFirmus
	 * that need to be composed. (e.g. for(int i = 1; i <= 3; i++) to compose the 2nd to 4th notes in cantusFirmus)
	 * 
	 * If there are no possible valid intervals left for the next note (dead end), something wrong was done in the past
	 * that led to this; it needs to be fixed. If so, un-compose the last note composed, and store it in a CounterpointError
	 * object. Another note is then proposed (while not allowing it to match the CounterpointError so the same mistake is
	 * not made again) and composed in that note's place.
	 */
    public void composeNoteByNote(boolean beforeFP) { //note-by-note composition method
    	
    	//determine begin and end indices based on parameter beforeFP
    	int begin;
    	int end;
    	if(beforeFP) {
    		begin = info.getPreFPBeginPoint();
    		end = info.getPreFPEndPoint();
    	}
    	else {
    		begin = info.getPostFPBeginPoint();
    		end = info.getPostFPEndPoint();
    	}
    	
    	//composition loop - notes composed linearly from begin to end
    	for(int i = begin; i <= end; i++) {
    		ArrayList<Note> rangeForNextNote = this.listRange(cantusFirmus[i-1]); //raw range
    		
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
    //create helper methods as needed
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
            if(!checkStepLeapRatios())
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
    public void raisePitches() { //to meet guidelines 1 and 2
    	//implement after everything else - not a guideline to be checked during the note-by-note phase. Will be implemented at the end.
    }
    public boolean checkStepLeapRatios() { //guideline 4
    	//number of composed notes so far must be greater or equal to n for each condition to work
    	//(n depends on number of measures)
    	//9 measures - minimum acceptable n = 6.  10, 7.  11, 7.  12, 8.  13, 8. 
    	int n = info.getNotesComposed();
    	int steps = info.getStepsSoFar();
    	int leaps = info.getLeapsSoFar();
    	int smallerIntervals = info.getSmallerIntervalsSoFar();
    	int largeLeaps = info.getLargeLeapsSoFar();
    	
    	if(n >= 2 + (model.getMeasures()/2)) {
    		if(leaps > steps)
    			return false;
    		if((2*largeLeaps) > smallerIntervals)
    			return false;
    		
    		return true; //if both conditions are met
    	}
    	else
    		return true; //condition not counted
    }
    public boolean checkForTriTones(Note proposed,Note other) { //guideline 5
    	
    }    
    public boolean checkOppositeMotion(Note first,Note second,Note third,Note fourth) { //guideline 7
    	//precondition: all parameters are valid Note objects
    	//precondition: one of two conditions
    	//one - proposed note is fourth (always checked with each note composed)
    	//two - proposed note is third (only checked when there exists a Note directly ahead of the to-be-composed note
    	boolean motionUp = third.midiValue() > second.midiValue();
    	if(motionUp) {
    		boolean eitherSideMotionDown = (first.midiValue() > second.midiValue()) || (third.midiValue() > fourth.midiValue());
    		if(eitherSideMotionDown)
    			return true; //valid
    		else
    			return false; //not valid
    	}
    	else { //motionDown
    		boolean eitherSideMotionUp = (first.midiValue() < second.midiValue()) || (third.midiValue() < fourth.midiValue());
    		if(eitherSideMotionUp)
    			return true;
    		else
    			return false;
    	}
    }
    public boolean checkOppositeMotionFromBehind(ArrayList<Note> nextNotes) { //guideline 8/9
    	
    }
    public boolean checkOppositeMotionFromAhead(ArrayList<Note> lastNotes) { //guideline 8/9
    	
    }
    public boolean checkTriToneStress(ArrayList<Note> lastNotes) { //guideline 10
    	
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
