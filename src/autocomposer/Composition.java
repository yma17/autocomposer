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
        this.model = m;
        info = new CompositionInfo();
    }
    public Note[][] compose() //composes the 2-voice counterpoint
    {
        Note[][] composition = new Note[2][model.getMeasures()];
    	cantusFirmus = composeCantusFirmus();
        counterpoint = composeCounterpoint(model.getTopLineIsCF());
        for(int x = 0; x < model.getMeasures(); x++)
        {
            if(model.getTopLineIsCF())
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
        
        //first note & last note = tonic
        cantusFirmus[0] = new Note(model);
        cantusFirmus[cantusFirmus.length-1] = cantusFirmus[0];
        
		int focalPoint = this.determineFocalPointLocation();
		int preFPContourType = this.determinePreFPContour();
		
		//for testing
		System.out.println("preFPContourType: " + preFPContourType);
		
		int firstBeginPoint = 1; //note-by-note begins composing here- always begins at 2nd note
    	int firstEndPoint; //note-by-note stops composing here
    	
    	Note lastNote = cantusFirmus[0];
    	Note twoNotesAgo = null; //note doesn't exist
    	Note threeNotesAgo = null;
    	
        if(preFPContourType== 1) { //defined by tonic to low point, leap to FP
        	
        	firstEndPoint = focalPoint - 2;
        	
        	int tonicToLowPointInterval = this.determineTonicToLPInterval(cantusFirmus[0]);
        	Note lowNote = new Note(cantusFirmus[0],tonicToLowPointInterval + 1,model);
			info.setMinPitch(lowNote.getRelativePitch());
			
			//two routes from here- additional note one note under, immediately before focal point (special structure) or not.
			
			
			//route 1 - additional note
			double c = Math.random();
			boolean toRouteTwo = true;
			
			if(focalPoint >= 6 && c < 0.15) {
				int leapToAdditionalInterval = this.determineLeapToAdditionalInterval(cantusFirmus[0],lowNote);
				
				//if no valid intervals, abandon route 1 and go to route 2
				boolean continueRouteOne = true;
				if(leapToAdditionalInterval == 0) //no legal intervals
					continueRouteOne = false;
				
				if(continueRouteOne) {
					int tonicToAdditionalInterval = tonicToLowPointInterval + leapToAdditionalInterval + 1;
					
					Note additional = new Note(cantusFirmus[0],tonicToAdditionalInterval-1,model);
					Note focal = new Note(cantusFirmus[0],tonicToAdditionalInterval,model); //exactly one note above
					
					cantusFirmus[focalPoint-3] = lowNote;
					cantusFirmus[focalPoint-2] = additional;
					cantusFirmus[focalPoint-1] = focal;
					
					info.setMaxPitch(focal.getRelativePitch());
					
					toRouteTwo = false; //completion of route 1 precludes route 2
				}
			}
        	
			//route 2 - no additional note
			if(toRouteTwo) {
				int leapToFPInterval = this.determineLeapToFPInterval(cantusFirmus[0], lowNote);
				
	        	int tonicToFPInterval = leapToFPInterval + tonicToLowPointInterval + 1;
	        	
	        	Note focal = new Note(cantusFirmus[0],tonicToFPInterval-1,model);
	        	info.setMaxPitch(focal.getRelativePitch());
	        	
	        	//special structures
	        	boolean specialStructureUsed = false;
	        	
	        	if(focalPoint >= 6) {
	        		double d = Math.random();
	        		    		
	        		if(d < 0.1 && leapToFPInterval == 8) { //5th+4th. Only works if leapToFPInterval = 8 (octave).
	        			Note fifth = new Note(cantusFirmus[0],lowNote.getRelativePitch()+4,model);
	        			
	        			boolean valid = this.checkFifthPlusFourth(lowNote, fifth);
	        			
	        			if(valid) {
	        				cantusFirmus[focalPoint-3] = lowNote;
	        				cantusFirmus[focalPoint-2] = fifth;
	        				cantusFirmus[focalPoint-1] = focal;
	        				specialStructureUsed = true;
	        			}
	        		}
	        		else { //triad. Only works if leapToFPInterval = 5 or 6.
	        			//note: opportunities for a triad are often minimized by several constraints within the code.
	        			//value of d irrelevant to maximize chances of getting a triad.
	        			Note middle = null;
	        			boolean middleExists = false;
	            		String position = "";
	            		            		
	            		if(leapToFPInterval == 5) { //root position triad
	            			middle = new Note(cantusFirmus[0],lowNote.getRelativePitch()+2,model);
	            			middleExists = true;
	            			position = "root position";
	            		}
	            		else if(leapToFPInterval == 6) {
	            			double x = Math.random();
	            			if(x < 0.5) { //first inversion triad
	            				middle = new Note(cantusFirmus[0],lowNote.getRelativePitch()+2,model);
	                			middleExists = true;
	            				position = "first inversion";
	            			}
	            			else { //second inversion triad
	            				middle = new Note(cantusFirmus[0],lowNote.getRelativePitch()+3,model);
	            				middleExists = true;
	            				position = "second inversion";
	            			}
	            		}

	            		if(middleExists) {
	            			boolean valid = this.checkTriadValidity(lowNote, middle, focal, position) && !this.checkForTriTones(cantusFirmus[0], focal);
	            			if(valid) {
	            				cantusFirmus[focalPoint-3] = lowNote;
	            				cantusFirmus[focalPoint-2] = middle;
	            				cantusFirmus[focalPoint-1] = focal;
	            				specialStructureUsed = true;
	            			}
	            		}
	        		}
	        	}
	        	
	        	if(!specialStructureUsed) {
	        		cantusFirmus[focalPoint-2] = lowNote;
	        		cantusFirmus[focalPoint-1] = focal;
	        	}
			}
        }
        else if(preFPContourType == 2) { //defined by tonic back to tonic, leap to FP
        	Note secondTonic = cantusFirmus[0];
        	
            //two routes from here- additional note one note under, immediately before focal point (special structure) or not.
			
			//route 1 - additional note
			double c = Math.random();
			
			boolean toRouteTwo = true;
			
			if(focalPoint >= 6 && c < 0.15) {
				int leapToAdditionalInterval = this.determineLeapToFPInterval(cantusFirmus[0],1);
				
				//if no valid intervals, abandon route 1 and go to route 2
				boolean continueRouteOne = true;
				if(leapToAdditionalInterval == 0) //no legal intervals
					continueRouteOne = false;
				
				if(continueRouteOne) {
					Note additional = new Note(cantusFirmus[0],leapToAdditionalInterval-1,model);
					Note focal = new Note(cantusFirmus[0],leapToAdditionalInterval,model); //exactly one note above
					
					//to check possibility of tri tone between tonic and focal point
					boolean triTone = this.checkForTriTones(secondTonic, focal);
					
					if(!triTone) {
						cantusFirmus[focalPoint-3] = secondTonic;
						cantusFirmus[focalPoint-2] = additional;
						cantusFirmus[focalPoint-1] = focal;
					
						info.setMaxPitch(focal.getRelativePitch());
					
						toRouteTwo = false; //completion of route 1 precludes route 2
					}
				}
			}
        	
			//route 2 - no additional note
			if(toRouteTwo) {
				int leapToFPInterval = this.determineLeapToFPInterval(cantusFirmus[0],2);
				
	        	Note focal = new Note(cantusFirmus[0],leapToFPInterval-1,model);
	        	info.setMaxPitch(focal.getRelativePitch());
	        	
	        	//special structures
	        	boolean specialStructureUsed = false;
	        	
	        	double d = Math.random();
	        	
	        	if(focalPoint >= 6 && d < 0.3) {
	        		//triad. Only works if leapToFPInterval = 5 or 6.
        			//note: unlike in case 1, there is little constraint, so variable d is used to control freq. of triads
        			Note middle = null;
        			boolean middleExists = false;
            		String position = "";
            		            		
            		if(leapToFPInterval == 5) { //root position triad
            			middle = new Note(cantusFirmus[0],cantusFirmus[0].getRelativePitch()+2,model);
            			middleExists = true;
            			position = "root position";
            		}
            		else if(leapToFPInterval == 6) {
            			double x = Math.random();
            			if(x < 0.5) { //first inversion triad
            				middle = new Note(cantusFirmus[0],cantusFirmus[0].getRelativePitch()+2,model);
                			middleExists = true;
            				position = "first inversion";
            			}
            			else { //second inversion triad
            				middle = new Note(cantusFirmus[0],cantusFirmus[0].getRelativePitch()+3,model);
            				middleExists = true;
            				position = "second inversion";
            			}
            		}

            		if(middleExists) {
            			boolean valid = this.checkTriadValidity(secondTonic, middle, focal, position);
            			if(valid) {
            				cantusFirmus[focalPoint-3] = secondTonic;
            				cantusFirmus[focalPoint-2] = middle;
            				cantusFirmus[focalPoint-1] = focal;
            				specialStructureUsed = true;
            			}
            		}
	        	}
	        	
	        	if(!specialStructureUsed) {
	        		cantusFirmus[focalPoint-2] = secondTonic;
	        		cantusFirmus[focalPoint-1] = focal;
	        	}
			}
        }
        else { //precontour = type 3
        	int tonicToFPInterval = this.determineFocalPoint(cantusFirmus[0]);
        	
        	Note focal = new Note(cantusFirmus[0],tonicToFPInterval-1,model);
        	
        	cantusFirmus[focalPoint-1] = focal;
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
        
       
        
        return cantusFirmus;
    }
    
    
    
    //composeCantusFirmus() helper methods, in order of appearance
    private int determineFocalPointLocation() //helper method of composeCantusFirmus. Determines the index of the focal point in the CF.
    {
    	//based entirely on randomization.
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
    	
    	//for testing
    	System.out.println(x);
    	
    	if(x < 0.3333)
    		return 1; //Type 1 = go down to low point and leap to focal point
    	else if(x < 0.6666)
    		return 2; //Type 2 = travel, return to tonic and leap to focal point
    	else
    		return 3; //Type 3 = gradual motion upwards towards focal point
    }
    private int determineTonicToLPInterval(Note tonic) { //Case I helper method. Determines the interval between the tonic and low point.
    	ArrayList<Integer> validIntervals = new ArrayList<Integer>();
    	
    	//these intervals- valid in all cases
    	validIntervals.add(-2);
    	validIntervals.add(-3);
    	validIntervals.add(-4);
    	
    	//check for tri tone in 5th interval
    	Note fifthBelow = new Note(tonic,-4,model);
    	boolean triTone = this.checkForTriTones(fifthBelow,tonic);
    	if(!triTone)
    		validIntervals.add(-5);
    	
    	int x = (int) (Math.random() * validIntervals.size());
    	
    	return validIntervals.get(x);
    }
    public int determineLeapToAdditionalInterval(Note tonic,Note lowPoint) { //Case I helper method, route 1. Determines the interval between the low point and additional note.
    	//checks fifth and sixth, the acceptable LP-AN intervals.
    	ArrayList<Integer> validIntervals = new ArrayList<Integer>();
    	int lowPointPitch = lowPoint.getPitch();
    	
    	for(int i = 5; i <= 6; i++) {
    		Note proposed = new Note(tonic,lowPoint.getRelativePitch()+i-1,model);
    		int proposedPitch = proposed.getPitch();
    		
    		//to avoid tri-tones between tonic and focal point
    		boolean triTone = this.checkForTriTones(tonic,new Note(tonic,proposed.getRelativePitch()+1,model));
    		
    		if(proposed.getRelativePitch() >= 3 && !triTone) { //focal pt must be 4th or above tonic
    		
    			if(proposedPitch < lowPointPitch)
    				proposedPitch += 12;
    		
    			if(proposedPitch - lowPointPitch == 7) //perfect fifth
    				validIntervals.add(i);
    			else if(proposedPitch - lowPointPitch == 8) //ascending m6
    				validIntervals.add(i);
    		}
    	}
    	
    	if(validIntervals.size() == 0)
    		return 0; //no valid intervals
    	    	
    	int x = (int) (Math.random()*validIntervals.size());
    	
    	return validIntervals.get(x);
    }
    public int determineLeapToFPInterval(Note tonic,Note lowPoint) { //Case I helper method, route 2. Determines the interval between the low point and the focal point.
    	//precondition: apart from "additional note" method.
    	//lowest acceptable interval = 5th. Largest = 8th. Checks validity of all in between. (except 7th)
    	//conditions: interval between tonic and focal point must be a 4th or greater. No tri-tones.
    	int lowPointPitch = lowPoint.getPitch();
    	ArrayList<Integer> validIntervals = new ArrayList<Integer>();
    	for(int i = 5; i <= 8; i++) {
    		if(i != 7) {  
    			boolean valid = true;
    			
    			Note proposed = new Note(tonic,lowPoint.getRelativePitch()+i-1,model);
    			int proposedPitch = proposed.getPitch();
    			
    			if(proposedPitch < lowPointPitch)
    				proposedPitch += 12;
    			
    			if(proposedPitch - lowPointPitch == 6) //tri-tone
    				valid = false;
    			
    			if(proposedPitch - lowPointPitch == 9) //no major 6ths, only ascending m6s allowed
    				valid = false;
    			
    			if(proposed.getRelativePitch() < 3) //less than a 4th
    				valid = false;
    			
    			if(valid) {
    				validIntervals.add(i);
    				
    			}
    		}
    	}
    	
    	int x = (int) (Math.random() * validIntervals.size());
    	
    	return validIntervals.get(x);
    }
    public boolean checkFifthPlusFourth(Note firstNote,Note fifth) { //Case I helper method. Checks validity of special structure 5th + 4th
    	//precondition: interval between FP and LP is an octave, FP already composed, LP determined and to be composed.
    	//also, interval between firstNOte and fifth is a 5th of some kind.
    	int firstNotePitch = firstNote.getPitch();
    	int fifthPitch = fifth.getPitch();
    	
    	if(fifthPitch < firstNotePitch) {
    		fifthPitch += 12;
    	}
    	
    	//to avoid tri-tones between tonic and focal point
		boolean triTone = this.checkForTriTones(cantusFirmus[0],new Note(cantusFirmus[0],firstNote.getRelativePitch()+7,model));
		
		if(triTone)
			return false;
		
    	if(fifthPitch - firstNotePitch == 7)
    		return true;
    	else
    		return false;
    }
    public boolean checkTriadValidity(Note bottom,Note middle,Note top,String position) { //helper method for composeCantusFirmus. Checks for tri tones.
    	//precondition: position consistent with note names of bottom, middle, and top.
    	boolean valid = true;
    	
    	int bottomPitch = bottom.getPitch();
    	int middlePitch = middle.getPitch();
    	int topPitch = top.getPitch();
    	
    	if(middlePitch < bottomPitch)
    		middlePitch += 12;
    	
    	if(topPitch < middlePitch)
    		topPitch += 12;
    	
    	if(position.equals("root position")) {
    		if(topPitch - bottomPitch == 6)
    			valid = false;
    	}
    	else if(position.equals("first inversion")) {
    		if(topPitch - middlePitch == 6)
    			valid = false;
    	}
    	else if(position.equals("second inversion")) {
    		if(middlePitch - bottomPitch == 6)
    			valid = false;
    	}
    		
        return valid;
    }
    public int determineLeapToFPInterval(Note tonic,int route) { //helper method for composeCantusFirmus, case 2.
    	//works with both route 1(additional route), and route 2(other), unlike case 1.
    	//acceptable intervals: third (additional note only), P4, P5, ascending m6.
    	//route 1 = additional note, route 2 = not additional note
    	ArrayList<Integer> validIntervals = new ArrayList<Integer>();
    	for(int i = 3; i <= 6; i++) {
    		Note proposed = new Note(tonic,i-1,model);
    		
    		int tonicPitch = tonic.getPitch();
    		int proposedPitch = proposed.getPitch();
    		
    		if(proposedPitch < tonicPitch)
    			proposedPitch += 12;
    		
    		if(route == 1) {
    			if((proposedPitch - tonicPitch == 3) || (proposedPitch - tonicPitch == 4)) //third
        			validIntervals.add(i);
    		}
    			
    		if(proposedPitch - tonicPitch == 5) //perfect 4th
    			validIntervals.add(i);
    		
    		if(proposedPitch - tonicPitch == 7) //perfect 5th
    			validIntervals.add(i);
    			
    	    if(proposedPitch - tonicPitch == 8) //ascending m6
    	    	validIntervals.add(i);
    	}
    	
    	if(validIntervals.size() == 0)
    		return 0; //no valid intervals
    	
    	int x = (int) (Math.random() * validIntervals.size());
    	
    	return validIntervals.get(x);
    }
    private boolean checkForTriTones(Note bottom,Note top) { //helper method of composeCantusFirmus.
    	//used to check for tritone between 2 notes.
    	//precondition: top is above bottom
    	int bottomPitch = bottom.getPitch();
        int topPitch = top.getPitch();

    	if(topPitch < bottomPitch)
    		topPitch += 12;
    	
    	if(topPitch - bottomPitch == 6)
    		return true;
    	else
    		return false;
    }
    
    private int determineFocalPoint(Note tonic) { //helper method for composeCantusFirmus, case 3.
    	//possibilities: perfect 4th, perfect 5th, sixth, minor 7th (if CF is 11 measures or longer), or octave (if CF is 12 measures or longer)
    	//as usual, no tri-tones, Major 7ths
    	//as there is no leap up to FP, leap rules don't apply here
    	ArrayList<Integer> validIntervals = new ArrayList<Integer>();
    	
    	for(int i = 4; i <= 8; i++) {
    		Note proposed = new Note(tonic,i-1,model);
    		
    		int tonicPitch = tonic.getPitch();
    		int proposedPitch = proposed.getPitch();
    		
    		if(proposedPitch < tonicPitch)
    			proposedPitch += 12;
    		
    		if(proposedPitch - tonicPitch == 5) //P4
    			validIntervals.add(i);
    		else if(proposedPitch - tonicPitch == 7) //P5
    			validIntervals.add(i);
    		else if((proposedPitch - tonicPitch == 8) || (proposedPitch - tonicPitch == 9)) //sixth
    			validIntervals.add(i);
    		else if(proposedPitch - tonicPitch == 10 && model.getMeasures() >= 11) //minor 7th
    			validIntervals.add(i);
    		else if(proposedPitch == tonicPitch && model.getMeasures() >= 12) //octave
    			validIntervals.add(i);
    	}
    	
    	int x = (int) (Math.random() * validIntervals.size());
		
		return validIntervals.get(x);
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
    private boolean checkForTriTones(int firstPitch, int secondPitch, int interval) {
    	if(interval == 4) {
    		if(model.getMode().equals("Ionian")) {
    			if((secondPitch == -1 && firstPitch == 3) || (secondPitch == 3 && firstPitch == -1))
    				return true;
    		}
    		else if(model.getMode().equals("Dorian")) {
    			if((secondPitch == -2 && firstPitch == 2) || (secondPitch == 2 && firstPitch == -2))
    				return true;
    		}
    		else if(model.getMode().equals("Phrygian")) {
    			if((secondPitch == -3 && firstPitch == 1) || (secondPitch == 1 && firstPitch == -3))
    				return true;
    		}
    		else if(model.getMode().equals("Lydian")) {
    			if((secondPitch == -4 && firstPitch == 0) || (secondPitch == 0 && firstPitch == -4)
    					|| (secondPitch == 3 && firstPitch == 7) || (secondPitch == 7 && firstPitch == 3))
    				return true;
    		}
    		else if(model.getMode().equals("Mixolydian")) {
    			if((secondPitch == -5 && firstPitch == -1) || (secondPitch == -1 && firstPitch == -5)
    					|| (secondPitch == 2 && firstPitch == -2) || (secondPitch == -2 && firstPitch == 2))
    				return true;
    		}
    		else if(model.getMode().equals("Aeolian")) {
    			if((secondPitch == -6 && firstPitch == -2) || (secondPitch == -2 && firstPitch == -6)
    					|| (secondPitch == 1 && firstPitch == -3) || (secondPitch == -3 && firstPitch == 1))
    				return true;
    		}
    	}
    	else if(interval == 3) {
    		if(model.getMode().equals("Ionian")) {
    			if((secondPitch == 3 && firstPitch == 6) || (secondPitch == 6 && firstPitch == 6)
    					|| (secondPitch == -4 && firstPitch == -1) || (secondPitch == -1 && firstPitch == -4))
    				return true;
    		}
    		else if(model.getMode().equals("Dorian")) {
    			if((secondPitch == 2 && firstPitch == 5) || (secondPitch == 5 && firstPitch == 2)
    					|| (secondPitch == -5 && firstPitch == -2) || (secondPitch == -2 && firstPitch == -5))
    				return true;
    		}
    		else if(model.getMode().equals("Phrygian")) {
    			if((secondPitch == 1 && firstPitch == 4) || (secondPitch == 4 && firstPitch == 1)
    					|| (secondPitch == -6 && firstPitch == -3) || (secondPitch == -3 && firstPitch == -6))
    				return true;
    		}
    		else if(model.getMode().equals("Lydian")) {
    			if((secondPitch == 0 && firstPitch == 3) || (secondPitch == 3 && firstPitch == 0))
    				return true;
    		}
    		else if(model.getMode().equals("Mixolydian")) {
    			if((secondPitch == -1 && firstPitch == 2) || (secondPitch == 2 && firstPitch == -1))
    				return true;
    		}
    		else if(model.getMode().equals("Aeolian")) {
    			if((secondPitch == -2 && firstPitch == 1) || (secondPitch == 1 && firstPitch == -2))
    				return true;
    		}
    	}
    	return false;
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
