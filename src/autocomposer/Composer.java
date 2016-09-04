package autocomposer;

import java.util.ArrayList;

/* This class contains all methods used to compose the counterpoint.
 * 
 * Contains methods that use randomization and musical guidelines to decide
 * specific variables and information about the music (e.g. contour, locations
 * of certain musical points).
 */

public class Composer {
	public Composition cpt; //the counterpoint, the music.
	public Model model; //contains all intrinsic information (e.g. key, mode, etc.) of the music to be composed. See Model class for specifics.
    public ErrorManager manager;
    public Composer(Model m) {
    	//counterpoint = new Composition();
    	this.model = m;
    	manager = new ErrorManager();
    }
    public void compose() //composes the 2-voice counterpoint
    {
    	this.composeCantusFirmus();
    	//this.composeCounterpoint();
    }
    public void composeCantusFirmus() //composes the cantus firmus
    {        
        //compose first note & last note = tonic
    	cpt.composeNote(new Note(model),0,true);
    	cpt.composeNote(cpt.getNote("CF",0),cpt.getLength()-1,true);
        
        //focal point = a "peak"-like point that the notes gradually build up towards and down from
		int focalPoint = this.determineFocalPointLocation();
		cpt.setFocalPoint(focalPoint); //info updated whenever any necessary information is determined
		
		//preFPContourType refers to contours that lead up to the focal point - 3 types
		//1 - notes go down to a low point, then leap to the focal point
		//2 - notes return to tonic, then leap to the focal point
		//3 - notes gradually build upward towards focal point
		int preFPContourType =  this.determinePreFPContour();
		cpt.setPreFPContourType(preFPContourType);
		
		//update info- note-by-note composition beginning and end points to be used later
		cpt.setPreFPBeginPoint(1); //2nd note
		cpt.setPostFPBeginPoint(focalPoint+1); //note after focal point
		cpt.setPostFPEndPoint(cpt.getLength()-2); //2nd to last note
		//preFPEndPoint to be set later in pre-determined note composition.
		//when preFPContourType is 1 or 2, it is the note before the preFPNote.
		//when preFPContourType is 3, it is the note before the focal point.
		
    	//pre-determined composition begins here. Notes such as the focal point and others are composed ahead of the main note-by-note composition.
    	
    	if(preFPContourType == 1 || preFPContourType == 2) { //both types are similar as they both share a "pre focal point" note.
    		//In type 1, it is below the tonic in pitch
    		//In type 2, it is equal to the tonic in pitch
    		
    		int tonicToPreFPInterval = this.determineTonicToPreFPInterval();

    		//basic method - create a Note object, and if determined to be valid along with the rest of the structure, compose it to the cantusFirmus array
    		
    		Note preFPNote = new Note(cpt.getNote("CF",0),tonicToPreFPInterval,model); //a tenative note to go immediately before the focal point
    		
    		//in preFPContourType 1, preFPNote is lowest note in the cantus firmus
    		if(preFPContourType == 1)
    			cpt.setMinPitch(preFPNote.getRelativePitch());
    		
    		
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
    					cpt.setPreFPEndPoint(focalPoint-3); //3 notes before focal pt
    				}
    				//if no legal intervals, don't employ any special structures
        		}
        		else if(d < 0.2) { //structure 2 - 5th + 4th - additional note between preFP and FP a perfect fifth above preFP. (e.g. D-A-D)
        	    	Note fifth = new Note(cpt.getNote("CF",0),preFPNote.getRelativePitch()+4,model);
        	    	Note focal = new Note(cpt.getNote("CF",0),preFPNote.getRelativePitch()+7,model);
        			
        			boolean valid = this.checkFifthPlusFourth(preFPNote,fifth,focal);
        			
        			if(valid) {
        				this.composeFifthFourthStructure(preFPNote, fifth, focal);
        				specialStructureUsed = true;
        				
    					//update info
        				cpt.setPreFPEndPoint(focalPoint-3);
        			}
        		}
        		else if(d < 0.9) { //structure 3 - triad with preFP as bottom note and FP as top note (e.g. C-E-G)
        			//note: the probability is so high here because there are several strict conditions that need to be met.
        			//e.g. in order for a triad to be composed (e.g. leapTOFPInterval must be 4 or 5) (fifth or sixrh).
        			int leapToFPInterval = this.determineLeapToFPInterval(preFPNote,true);

        			Note focal = new Note(cpt.getNote("CF",0),preFPNote.getRelativePitch()+leapToFPInterval,model);
        			//the middle note of the triad, position depends on leapToFPInterval
        			Note middle = null;
          			String position = "";
        			
        			if(leapToFPInterval == 4) { //root position triad
        				middle = new Note(cpt.getNote("CF",0),preFPNote.getRelativePitch()+2,model);
        				focal = new Note(cpt.getNote("CF",0),preFPNote.getRelativePitch()+4,model);
            			position = "root position";
        			}
        			else if(leapToFPInterval == 5) {
            			double x = Math.random();
            			if(x < 0.5) { //first inversion triad
            				middle = new Note(cpt.getNote("CF",0),preFPNote.getRelativePitch()+2,model);
            				position = "first inversion";
            			}
            			else { //second inversion triad
            				middle = new Note(cpt.getNote("CF",0),preFPNote.getRelativePitch()+3,model);
            				focal = new Note(cpt.getNote("CF",0),preFPNote.getRelativePitch()+5,model);
            				position = "second inversion";
            			}
            		}
        			
        			if(!position.equals("")) { //if initial conditions are met
            			boolean valid = this.checkTriadValidity(preFPNote, middle, focal, position);
            			if(valid) {
            				this.composeTriadStructure(preFPNote, middle, focal);
            				specialStructureUsed = true;
            				
            				//update info
            				cpt.setPreFPEndPoint(focalPoint-3);
            			}
            		}
        		}
    		}

    		//no special structure used
    		if(!specialStructureUsed) {
    			int leapToFPInterval = this.determineLeapToFPInterval(preFPNote,false);

                Note focal = new Note(cpt.getNote("CF",0),preFPNote.getRelativePitch()+leapToFPInterval,model);
                
                cpt.composeNote(preFPNote, focalPoint-1, true);
                cpt.composeNote(focal, focalPoint, true);
               
                //update info
                cpt.setMaxPitch(focal.getRelativePitch());
                cpt.setPreFPEndPoint(focalPoint-2);
    		}

    	}
    	else { //precontour = type 3
        	int tonicToFPInterval = this.determineFocalPoint();

        	Note focal = new Note(cpt.getNote("CF",0),tonicToFPInterval,model);
        	
        	cpt.composeNote(focal, focalPoint, true);

        	//update info (no consecutive notes = no step/leap info to update)
        	cpt.setMaxPitch(focal.getRelativePitch());
        	cpt.setPreFPEndPoint(focalPoint-1);
        }
    	
    	//now that the pre-determined focal point composition is complete,
    	//the note-to-note and the rest of the composition of the CF begins.
    
    	int begin = cpt.getPreFPBeginPoint();
        int end = cpt.getPreFPEndPoint();
        this.composeNoteByNote(begin, end);
    	
    	begin = cpt.getPostFPBeginPoint();
    	end = cpt.getPostFPEndPoint();
    	this.composeNoteByNote(begin, end);        
    }
    
   
    
    //composeCantusFirmus() helper methods
    
    
    //special structure composing methods
    
    //additional note
    private void composeAdditionalNoteStructure(Note preFPNote,int leapToAdditionalInterval) {	
    	Note additional = new Note(cpt.getNote("CF",0),preFPNote.getRelativePitch()+leapToAdditionalInterval,model);
		Note focal = new Note(cpt.getNote("CF",0),preFPNote.getRelativePitch()+leapToAdditionalInterval+1,model); //one note above
    	
		int focalPoint = cpt.getFocalPoint();
		
		cpt.composeNote(preFPNote,focalPoint-2,true);
		cpt.composeNote(additional,focalPoint-1,true);
		cpt.composeNote(focal,focalPoint,true);

		//update info
		cpt.setMaxPitch(focal.getRelativePitch());
    }
    //5th + 4th
    private void composeFifthFourthStructure(Note preFPNote,Note fifth,Note focal) {
    	int focalPoint = cpt.getFocalPoint();
    	
    	cpt.composeNote(preFPNote,focalPoint-2,true);
    	cpt.composeNote(fifth,focalPoint-1,true);
    	cpt.composeNote(focal,focalPoint,true);
		
		//update info
		cpt.setMaxPitch(focal.getRelativePitch());
    }
    //triad
    private void composeTriadStructure(Note bottom,Note middle,Note top) {
        int focalPoint = cpt.getFocalPoint();
    	
        cpt.composeNote(bottom,focalPoint-2,true);
        cpt.composeNote(middle,focalPoint-1,true);
        cpt.composeNote(top,focalPoint,true);
		
		//update info
		cpt.setMaxPitch(top.getRelativePitch());
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
    	if(cpt.getPreFPContourType() == 2)
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
    		if(!(i == 3 && cpt.getPreFPContourType() != 2)) {
    			Note proposed = new Note(cpt.getNote("CF",0),preFPNote.getRelativePitch()+i,model);

    			int difference = proposed.midiValue() - preFPNote.midiValue(); //difference in half steps between the two pitches
    		
    			//to ensure steepness from focal point to tonic is legal
    			int length = cpt.getLength() - cpt.getFocalPoint();
    			boolean notTooSteep = this.checkSteepness(length,proposed.getRelativePitch()+1);
    			
    			if(proposed.getRelativePitch() >= 3 && notTooSteep) { //focal pt must be 4th or above tonic
    				if(difference == 5) { //perfect fourth
    					if(cpt.getPreFPContourType() == 2)
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
    		if(i != 6 && !(i == 3 && cpt.getPreFPContourType() != 2) ) {  
    			boolean valid = true;
    			
    			Note proposed = new Note(cpt.getNote("CF",0),preFPNote.getRelativePitch()+i,model);
    			
    			int difference = proposed.midiValue() - preFPNote.midiValue();
    			
    			if(difference == 6) //no tri-tones allowed
    				valid = false;
    			
    			if(difference == 9 && !triad) //major 6th acceptable for interval for triad only.
    				valid = false;
    			
    			if(proposed.getRelativePitch() < 3) //less than a 4th from tonic
    				valid = false;
    			
    			int length = cpt.getLength() - cpt.getFocalPoint();
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
		int length = cpt.getLength()- cpt.getFocalPoint();
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
    		Note proposed = new Note(cpt.getNote("CF",0),i,model);
    		
    		//to ensure steepness from focal point to tonic is legal- from both the beginning and end
    		int lengthFromBeginning = cpt.getFocalPoint() + 1;
			int lengthToEnd = cpt.getLength() - cpt.getFocalPoint();
			boolean notTooSteepFromBeginning = this.checkSteepness(lengthFromBeginning,proposed.getRelativePitch());
			boolean notTooSteepToEnd = this.checkSteepness(lengthToEnd,proposed.getRelativePitch());
    		
			if(notTooSteepFromBeginning && notTooSteepToEnd) {
				int difference = proposed.midiValue() - cpt.getNote("CF",0).midiValue();
    		
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
	 * 4. melodic motion mostly by steps. (Decision by the programmer: usually no more leaps than steps, but some flexibility is fine)
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
    public void composeNoteByNote(int begin, int end) { //note-by-note composition method
    	
    	//composition loop - notes composed linearly from begin to end
    	for(int i = begin; i <= end; i++) {
    		System.out.println("i: " + i); //for testing
    		
    		ArrayList<Note> rangeForNextNote = this.listRange(i, cpt.getCantusFirmus(), null);
    		    		
    		if(rangeForNextNote.size() == 0 && i > begin) {
    			Note lastNote = cpt.getNote("CF",i-1);
    			cpt.uncomposeNote(lastNote,i-1, true);
    			this.goBackward(i-1, cpt.getCantusFirmus(), lastNote);
    		}
    		else if(rangeForNextNote.size() == 0 && i == begin) { //for testing
    			System.out.println("break");
    			break;
    		}
    		else {
    			//select a random note from the arraylist and compose it
    			double d = Math.random();
    			int n = (int)(d*rangeForNextNote.size());
    			Note nextNote = rangeForNextNote.get(n);
    			cpt.composeNote(nextNote,i,true);
    		}
    	}
    }
	//create helper methods as needed
    public ArrayList<Note> listRange(int index,Note[] array,Note problematic) {
    	//problematic - note that must be omitted from the range. Only valid during error-managing process, null otherwise.
    	
    	//initialize previousNotes and futureNotes
		//previousNotes count notes from current index, backwards (e.g. 6th note,5th note,4th note...)
		ArrayList<Note> previousNotes = new ArrayList<Note>();
		for(int x = index-1; x >= index-4; x--) { //<=4 notes
			if(x >= 0) //to avoid ArrayOutOfBoundsException, to manage first 3 notes of the CF
				previousNotes.add(array[x]);
		}
		//futureNotes count notes from current index, forwards (e.g. 8th note,9th note,10th note...)
		ArrayList<Note> futureNotes = new ArrayList<Note>();
		for(int x = index+1; x <= index+3; x++) { //<=3 notes
			if(x <= cpt.getLength()-1)
				futureNotes.add(array[x]);
		}
		
		return this.listValidRange(index, previousNotes, futureNotes, problematic);
    }
    public boolean goForward(int index,Note[] array) { //forwards.
    	//precondition: index is between either pre or post begin and end points.
        ArrayList<Note> range = this.listRange(index,array,null);
        if(range.size() == 0) {
        	return false;
        }
        else {
        	Note[] thisArray = array;
    		Note thisNote = range.get(0);
        	ArrayList<Note> validRange = new ArrayList<Note>();
        	for(int i = 0; i < range.size(); i++) {
        		thisArray[index] = thisNote;
        		boolean b = this.goForward(index+1, array);
        		
        		if(b)
        			validRange.add(thisNote);
            }
        	
        	if(validRange.size() == 0) {
        		return false;
        	}
        	else if(index == manager.getMaxIndex()) {
        		for(int i = 0; i < validRange.size(); i++) {
        			
        		}
        		thisArray[index] = thisNote;
        		
        		//translate all notes in the re-composed section to a seperate array to add to list of valid sections in manager
        		Note[] revisedSection = new Note[manager.getMaxIndex()-manager.getMinIndex()+1];
        		for(int j = 0; j < revisedSection.length; j++)
        			revisedSection[j] = thisArray[manager.getMinIndex() + j];
        		manager.addToValidSections(revisedSection);
        		
        		return true; //one or more valid sections found in this iteration of manageError.
        	}
        	else
        		return true; //one or more valid sections found in a previous iteration of manageError.
        }
    }
    public void goBackward(int index,Note[] array,Note problematic) { //backwards.
    	ArrayList<Note> range = this.listRange(index, array, problematic);
    	if(range.size() == 0) { //when problematic is the only "valid" option leading out of the previous note at index-1
    		Note[] thisArray = array;
    		Note lastNote = thisArray[index-1];
    		thisArray[index-1] = null; //uncompose previousNote
    		
    		//go back to the previous index and continue to try to find a solution.
    		this.goBackward(index-1, thisArray, lastNote);
    	}
    	else {
    		boolean solutionFound = this.goForward(index, array);
    		
    		if(solutionFound) {
    			//do nothing. //error checking algorithm is complete.
    		}
    		else {
    			Note[] thisArray = array;
        		Note lastNote = thisArray[index-1];
        		thisArray[index-1] = null; //uncompose previousNote
        		
        		//go back to the previous index and continue to try to find a solution.
        		this.goBackward(index-1, thisArray, lastNote);
    		}
    	}
    }
    public ArrayList<Note> listValidRange(int index,ArrayList<Note> previousNotes,ArrayList<Note> futureNotes, Note problematic) {
    	ArrayList<Note> validRange = new ArrayList<Note>();
    	
    	for(int i = -4; i <= 4; i++) {
    		if(i != 0) {
    			Note proposed = new Note(cpt.getNote("CF",0),previousNotes.get(0).getRelativePitch()+i,model);
    			
    			boolean valid = true; //default value
    			//to ensure that proposed does not match problematic
    			if(problematic != null) {
    				if(proposed.midiValue() == problematic.midiValue())
    					valid = false;
    			}
    			
    			if(valid) //saves code in case proposed = problematic
    				valid = this.checkGuidelines(proposed, previousNotes, futureNotes, index);
    			
                if(valid)
                	validRange.add(proposed);
    		}
    	}

    	return validRange;
    }
    public boolean checkGuidelines(Note proposed,ArrayList<Note> previousNotes,ArrayList<Note> futureNotes,int index) {
    	//method will call helper methods for each specific guideline
		if(!checkWithinMinMaxPitches(proposed))
			return false;
        if(!checkForTriTones(proposed,previousNotes.get(0)))
        	return false;
        if(previousNotes.size() >= 3 && previousNotes.get(2) != null) { //if the entire previousNotes arraylist is valid (get(0) is always valid(first note tonic))
        	if(!checkOppositeMotion(previousNotes.get(2),previousNotes.get(1),previousNotes.get(0),proposed))
            	return false;
        	
            if(previousNotes.size() == 4) {
            	//if(!checkTriToneStress(previousNotes.get(3),previousNotes.get(2),previousNotes.get(1),previousNotes.get(0)));
            		//return false;
            }
            else { //size = 3
            	//if(!checkTriToneStress(null,previousNotes.get(2),previousNotes.get(1),previousNotes.get(0)))
            		//return false;
            }
            
        }
        if(futureNotes.get(0) != null) { //note exists in index directly ahead
        	if(!this.checkIdenticalNotes(proposed,futureNotes.get(0)))
        		return false;
        	
        	//if(!checkTriToneStress(previousNotes.get(1),previousNotes.get(0),proposed,futureNotes.get(0)))
        		//return false;
        	
        	if(index == cpt.getLength()-2) { //2nd to last note
        		if(!checkStepFromTonic(proposed))
        			return false;
        	}
        	else { //just before notes from pre-determined composition (index = preFPEndPoint)
        		if(!checkForTriTones(proposed,futureNotes.get(0)))
        			return false;
        		if(!checkOppositeMotion(previousNotes.get(1),previousNotes.get(0),proposed,futureNotes.get(0)))
        			return false;
        		if(futureNotes.get(1) != null && futureNotes.get(2) != null && cpt.getPreFPContourType() <= 2) { //if the entire futureNotes array is valid
        			if(!checkOppositeMotionFromBehind(futureNotes,proposed))
        				return false;
        		}
        	}
        }   	
        
        //check step/leap ratios
        if(!checkStepLeapRatios(proposed,previousNotes.get(0),index))
      		return false;
        if(index == cpt.getPreFPEndPoint()) {
        	if(!checkStepLeapRatios(previousNotes.get(0),proposed,futureNotes.get(0)))
    			return false;
        }
        
        return true;
    	
    }
	public void raisePitches() { //to meet guidelines 1 and 2
    	//implement after everything else - not a guideline to be checked during the note-by-note phase. Will be implemented at the end.
    }
    public boolean checkStepLeapRatios(Note proposed,Note otherNote,int index) { //guideline 4
    	//one new interval created here - with otherNote
       	int steps = cpt.getStepsSoFar();
    	int leaps = cpt.getLeapsSoFar();
    	int smallerIntervals = cpt.getSmallerIntervalsSoFar();
    	int largeLeaps = cpt.getLargeLeapsSoFar();
    	
    	int interval = Math.abs(otherNote.getRelativePitch()-proposed.getRelativePitch()); //interval between the 2 notes
    	
    	if(index < cpt.getFocalPoint()) { //before the focal point - looser rules
    		if(interval >= 2) { //leap (3rd or larger)
    		    if(leaps >= ((cpt.getFocalPoint()+1)/2)+1)
        		    return false;
    		}
	    	if(interval >= 3) { //large leap (4th or larger)
	    		if(largeLeaps >= ((cpt.getFocalPoint()+1)/3))
	    			return false;
	    	}
    	}
    	else if(index == cpt.getFocalPoint()+1) { //note directly after focal point
    		//step/leap rules don't apply to this particular note- to ensure that notes can lead to end smoothly.
    	}
    	else if(index >= cpt.getFocalPoint()+2) { //after that - stricter rules
			if(interval >= 2) { //leap (3rd or larger)
	    		if((leaps+1) >= steps)
	    			return false;
	    	}
	    	if(interval >= 3) { //large leap (4th or larger)
	        	if((2*largeLeaps) >= smallerIntervals)
	        		return false;
	    	}
		}

		return true;
    }
    public boolean checkStepLeapRatios(Note noteBefore,Note proposed,Note noteAfter) {
    	//precondition: just before notes from pre-determined composition (preFP,FP,etc.)
    	//2 new intervals are created here - with noteBefore and noteAfter
    	int leaps = cpt.getLeapsSoFar();
    	int largeLeaps = cpt.getLargeLeapsSoFar();
    	
    	int interval = Math.abs(noteBefore.getRelativePitch()-proposed.getRelativePitch());
    	
  		if(interval >= 2)
  			leaps++;
    	if(interval >= 3)
    		largeLeaps++;
	
    	interval = Math.abs(noteAfter.getRelativePitch()-proposed.getRelativePitch());

    	if(interval >= 2) { //leap (3rd or larger)
    	    if(leaps >= ((cpt.getFocalPoint()+1)/2)+1)
       		    return false;
   		}
	   	if(interval >= 3) { //large leap (4th or larger)
	   		if(largeLeaps >= ((cpt.getFocalPoint()+1)/3))
	   			return false;
    	}
    	
		return true;
    }
    public boolean checkForTriTones(Note proposed,Note other) { //guideline 5
    	if(Math.abs(other.midiValue()-proposed.midiValue()) == 6)
    		return false;
    	return true;
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
    public boolean checkOppositeMotionFromBehind(ArrayList<Note> nextNotes,Note proposed) { //guideline 8/9
    	//preconditions: preFPContourType is 1 or 2.
    				//   nextNotes.get(1) is valid
    				//   just before notes from pre-determined composition (preFP,FP,etc.)
    	
		//interval between preFP and note after is greater than P5 
    	
    	if(nextNotes.get(1).getRelativePitch() > nextNotes.get(0).getRelativePitch()) {
    		if(!(proposed.getRelativePitch() > nextNotes.get(0).getRelativePitch()))
    			return false;
    	}
    	
    	return true;
    }
    public boolean checkTriToneStress(Note first,Note second,Note third,Note fourth) { //guideline 10
    	//precondition: first may or may not exist, all others are valid
    	//precondition: index that is being composed >= 2
    	
    	//step 1 - check for tri-tone && augmented 4th (not diminished 5th)
    	//scenario 1 - step+3rd over three notes (e.g. B-D-E)
    	//scenario 2 - completely stepwise over four notes (e.g. B-C-D-E)
    	int scenario = 0;
    	if(Math.abs(fourth.getRelativePitch()-second.getRelativePitch()) == 3
    			&& Math.abs(fourth.midiValue()-second.midiValue()) == 6)
    		scenario = 1;
    	if(first != null) {
    		if(Math.abs(fourth.getRelativePitch()-first.getRelativePitch()) == 3
        			&& Math.abs(fourth.midiValue()-first.midiValue()) == 6)
        		scenario = 2;
    	}
    	
    	//step 2 - check for stepwise/linear
    	if(scenario != 0) { //scenario calls for scrutiny of tri-tone stress
    		if(scenario == 1) {
    			if(third.getRelativePitch() > fourth.getRelativePitch()
    				&& third.getRelativePitch() < second.getRelativePitch()) //descending
					return false;
    			else if(third.getRelativePitch() < fourth.getRelativePitch()
        				&& third.getRelativePitch() > second.getRelativePitch()) //ascending
    					return false;
    		}
    		else { //scenario = 2
    			/*
    			int step = fourth.getRelativePitch()-third.getRelativePitch(); //1 - up, -1 - down
    			if(Math.abs(step) == 1) {
    				for(int i = 1; i < 3; i++) {
    					if(lastNotes.get(i).getRelativePitch()-lastNotes.get(i+1).getRelativePitch() != step)
    						return false;
    				}
    			}
    			*/
    			boolean allSteps = (Math.abs(fourth.getRelativePitch()-third.getRelativePitch()) == 1)
    								&& (Math.abs(third.getRelativePitch()-second.getRelativePitch()) == 1)
    								&& (Math.abs(second.getRelativePitch()-first.getRelativePitch()) == 1);
    			if(allSteps)
    				return false;
    		}
    	}
    	
    	return true;
    }
    private boolean checkStepFromTonic(Note proposed) { //guideline 11
		if(Math.abs(proposed.getRelativePitch()-cpt.getNote("CF",0).getRelativePitch()) == 1)
			return true;
		return false;
	}
    private boolean checkWithinMinMaxPitches(Note proposed) {
    	//to ensure that proposed.getRelativePitch() is between info.getMinPitch() and info.getMaxPitch()
    	//precondition: info.getMaxPitch() is set already (during the pre-det. comp.)
    	int relativePitch = proposed.getRelativePitch();
    	if(cpt.getMinPitch() != Integer.MIN_VALUE) { //minPitch has been set
    		if(relativePitch <= cpt.getMinPitch())
    			return false;
    	}
    	if(relativePitch >= cpt.getMaxPitch())
    		return false;
    	
    	return true;
    }
    private boolean checkIdenticalNotes(Note proposed,Note after) {
    	if(proposed.midiValue() == after.midiValue())
    		return false;
    	return true;
    }
    private void composeCounterpoint(boolean istopLineCF) {
    	//insert code here
    }
}
