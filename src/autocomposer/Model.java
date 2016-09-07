package autocomposer;

import java.util.Arrays;

/* The Model serves as a template for the main composition algorithm to follow,
 * containing necessary intrinsic information of the counterpoint such as key,
 * mode, length, cantus firmus orientation, etc.
 * 
 * All variables independent of musical decisions (e.g. the notes, how they are oriented) to be made during the composition process
 * 
 * "outside of the counterpoint"
 * 
 * Variable of the Composition class.
 */
public class Model implements NotesAndKeys
{
    public String key; // key of the counterpoint
    public String mode; //mode of the counterpoint
    public int measures; //number of measures in the counterpoint
    public boolean topLineIsCF; //whether top line of the counterpoint is cantus firmus(CF)
    public String[] specificNotes; //7 note names specific to the key and mode
    public boolean keyIsSharp; //whether the key contains sharps of flats (true = sharp)
    public int octaveOfCF; //octave that the first note of the cantus firmus is in
    public int octaveUpValue; //how many notes up (relative pitch distance) from the tonic is required to raise the octave (always > 0)
    public int octaveDownValue; //how many notes down (relative pitch distance) from the tonic is required to lower the octave (always < 0)
    public Model() //in absence in user control, generates random key, mode, length, CF
    {
    	this.determineKeyAndMode();
        measures = determineMeasures();
        
        topLineIsCF = determineTopLineIsCF();
        keyIsSharp = determineSharp(key,mode);
        octaveOfCF = determineOctaveofCF(key,topLineIsCF,keyIsSharp);
        this.specificNotes = determineSpecificNotes(key,mode,keyIsSharp);
        this.determineOctaveValues();
        
    }
    public Model(String key, String mode, int measures) //in presence of user control, set user-preferred key, mode, length. Randomly determines CF.
    {
    	this.key = key;
        this.mode = mode;
        this.measures = measures;
        
        topLineIsCF = determineTopLineIsCF();
        keyIsSharp = determineSharp(key,mode);
        octaveOfCF = determineOctaveofCF(key,topLineIsCF,keyIsSharp);
        this.specificNotes = determineSpecificNotes(key,mode,keyIsSharp);
        this.determineOctaveValues();
    }
    
    //helper methods called in constructors
    private void determineKeyAndMode() { //in absence of user control only.
    	//For sake of simplicity, key and mode combinations will allows for white keys only (no accidentals).
    	double x = Math.random();
    	
    	if(x < 0.167) {
    		key = "C";
    		mode = "Ionian";
    	}
    	else if(x < 0.333) {
    		key = "D";
    		mode = "Dorian";
    	}
    	else if(x < 0.500) {
    		key = "E";
    		mode = "Phrygian";
    	}
    	else if(x < 0.667) {
    		key = "F";
    		mode = "Lydian";
    	}
    	else if(x < 0.833) {
    		key = "G";
    		mode = "Mixolydian";
    	}
    	else {
    		key = "A";
    		mode = "Aeolian";
    	}
    }
    private int determineMeasures() { //In absence of user control only.
    	//will be between 9 and 13 measures.
        double x = Math.random() * 5;
        return (int) x + 9;
    }
    private boolean determineTopLineIsCF() {
    	//determined entirely on randomization.
        double x = Math.random();
        if(x < 0.5)
            return true;
        return false;
    }
    private boolean determineSharp(String key, String mode) { //determines if the key used in sharp, flat, or natural.
    	//note: false = flat or natural
    	if(key.indexOf("sharp") >= 0 || key.equals("B"))
        	return true;
        else if(key.equals("E") && !mode.equals("Phrygian"))
        	return true;
        else if(key.equals("A") && !mode.equals("Phrygian") && !mode.equals("Aeolian"))
        	return true;
        else if(key.equals("D") && (mode.equals("Ionian") || mode.equals("Lydian") || mode.equals("Mixolydian")))
        	return true;
        else if(key.equals("G") && (mode.equals("Ionian") || mode.equals("Lydian")))
        	return true;
        else if(key.equals("C") && mode.equals("Lydian"))
        	return true;
        else
        	return false;
    }
    private int determineOctaveofCF(String key, boolean topLineIsCF, boolean keyIsSharp) { //determines octave of the first note of the CF.
    	//for testing purposes, for now, return 4
    	return 4;
    	/*
    	double x = Math.random();
    	
    	int keyIndex;
    	if(keyIsSharp)
    		keyIndex = Arrays.binarySearch(NOTES_SHARPS, key);
    	else
    		keyIndex = Arrays.binarySearch(NOTES,key);
    	
    	if(topLineIsCF) { //range: F3-C5
    		if(keyIndex == 0) { //C
    			if(x < 0.5)
    				return 4;
    			else
    				return 5;
    		}
    		else if(keyIndex >= 1 && keyIndex <= 5) //C-sharp/D-flat to F
    			return 4;
    		else { //F-sharp/G-flat to B
    			if(x < 0.5)
    				return 3;
    			else
    				return 4;
    		}
    	}
    	else { //range: C3-G4
    		if(keyIndex >= 0 && keyIndex <= 7) { //C to G
    			if(x < 0.5)
    				return 3;
    			else
    				return 4;
    		}
    		else //G-sharp/A-flat to B
    			return 3;
    	}
    	*/
    }
    public String[] determineSpecificNotes(String key, String mode, boolean sharp) { //determine the 7-note scale of the given key and mode.
    	//e.g. C Ionian: C-D-E-F-G-A-B
    	String[] specificNotes = new String[7];
    	specificNotes = createRawScale(key,mode,sharp); //raw scale - without enharmonics,double accidentals
    	
    	//convert to enharmonics, add double accidentals as needed
    	String basicNoteOfKey = key.substring(0,1);
    	int basicNoteOfKeyIndex = Arrays.binarySearch(BASIC_NOTES, basicNoteOfKey);
    	for(int i = 1; i < specificNotes.length; i++) {
    		String expectedBasicNote = BASIC_NOTES[(basicNoteOfKeyIndex + i)%7];
    		String thisBasicNote = specificNotes[i].substring(0,1);
    		if(!(thisBasicNote.equals(expectedBasicNote))) {
    			String[] enharmonicsToBeConverted = {"B","C","E","F","B-sharp","C-flat","E-sharp","F-flat"};
    			boolean found = false;
    			for(int x = 0; x < enharmonicsToBeConverted.length; x++) {
    				if(specificNotes[i].equals(enharmonicsToBeConverted[x]))
    					found = true;
    			}
    			if(found) {
    				if(specificNotes[i].length() == 1) //simple note, no sharps of flats
    					specificNotes[i] = NoteUtilities.convertToComplexEnharmonic(specificNotes[i]);
    				else
    					specificNotes[i] = NoteUtilities.convertToSimpleEnharmonic(specificNotes[i]);
    			}
    			else {
    				if(sharp)
    					specificNotes[i] = NoteUtilities.convertToDouble(specificNotes[i], false);
    				else
    					specificNotes[i] = NoteUtilities.convertToDouble(specificNotes[i], true);

    			}
    		}
    	}
    	
    	//singular exception: A-sharp Lydian
    	if(key.equals("A-sharp") && mode.equals("Lydian")) {
    		specificNotes[3] = "D-double-sharp";
    	}
    	
    	return specificNotes;
    }
    private String[] createRawScale(String key, String mode, boolean sharp) { //helper method of determineSpecificNotes.
    	//raw scale only contains notes from NOTES or NOTES_SHARPS. Does not include necessary double accidentals or enharmonics.
    	String[] rawScale = new String[7];
    	int keyIndex = 0; //index of key in NoTES or NOTES_SHARPS
    	if(sharp) {
    		//search for the index of the key (tonic note) in array
            for(int x = 0; x < NOTES_SHARPS.length; x++) {
               if(key.equals(NOTES_SHARPS[x]))
                    keyIndex = x;
            }
            //given that index, search for the other 6 notes
            if(mode.equals("Ionian")) {
                for(int x = 0; x < rawScale.length; x++)
                    rawScale[x] = NOTES_SHARPS[(keyIndex + IONIAN_DIATONIC_INTERVALS[x])%NOTES.length];
            }
            else if(mode.equals("Dorian")) {
                for(int x = 0; x < rawScale.length; x++)
                	rawScale[x] = NOTES_SHARPS[(keyIndex + DORIAN_DIATONIC_INTERVALS[x])%NOTES.length];
            }
            else if(mode.equals("Phrygian")) {
                for(int x = 0; x < rawScale.length; x++)
                	rawScale[x] = NOTES_SHARPS[(keyIndex + PHRYGIAN_DIATONIC_INTERVALS[x])%NOTES.length];
            }
            else if(mode.equals("Lydian")) {
                for(int x = 0; x < rawScale.length; x++)
                	rawScale[x] = NOTES_SHARPS[(keyIndex + LYDIAN_DIATONIC_INTERVALS[x])%NOTES.length];
            }
            else if(mode.equals("Mixolydian")) {
                for(int x = 0; x < rawScale.length; x++)
                	rawScale[x] = NOTES_SHARPS[(keyIndex + MIXOLYDIAN_DIATONIC_INTERVALS[x])%NOTES.length];
            }
            else { //Aeolian 
                for(int x = 0; x < rawScale.length; x++)
                	rawScale[x] = NOTES_SHARPS[(keyIndex + AEOLIAN_DIATONIC_INTERVALS[x])%NOTES.length];
            }
        }
        else {
            for(int x = 0; x < NOTES.length; x++) {
    		    if(key.equals(NOTES[x]))
    			    keyIndex = x;
    			 }
            if(mode.equals("Ionian")) {
                for(int x = 0; x < rawScale.length; x++)
                	rawScale[x] = NOTES[(keyIndex + IONIAN_DIATONIC_INTERVALS[x])%NOTES.length];
            }
            else if(mode.equals("Dorian")) {
                for(int x = 0; x < rawScale.length; x++)
                	rawScale[x] = NOTES[(keyIndex + DORIAN_DIATONIC_INTERVALS[x])%NOTES.length];
            }
            else if(mode.equals("Phrygian")) {
                for(int x = 0; x < rawScale.length; x++)
                	rawScale[x] = NOTES[(keyIndex + PHRYGIAN_DIATONIC_INTERVALS[x])%NOTES.length];
            }
            else if(mode.equals("Lydian")) {
                for(int x = 0; x < rawScale.length; x++)
                	rawScale[x] = NOTES[(keyIndex + LYDIAN_DIATONIC_INTERVALS[x])%NOTES.length];
            }
            else if(mode.equals("Mixolydian")) {
                for(int x = 0; x < rawScale.length; x++)
                	rawScale[x] = NOTES[(keyIndex + MIXOLYDIAN_DIATONIC_INTERVALS[x])%NOTES.length];
            }
            else { //Aeolian 
                for(int x = 0; x < rawScale.length; x++)
                	rawScale[x] = NOTES[(keyIndex + AEOLIAN_DIATONIC_INTERVALS[x])%NOTES.length];
            }
        }
    	return rawScale;
    }
    public void determineOctaveValues() { //determines octaveUpValue and octaveDownValue
    	int octaveDownPitch = 0; //default value
    	
    	//as octaveUp/DownPitch refer to the # of rel.pitches from tonic needed to alter the octave,
    	//this method finds the first note beneath the tonic that the octave changes (the first note
    	//to pass C downwards). With each note searched, octaveDownPitch is lowered by 1 until the
    	//lower octave is found. That value is set as octaveDownValue and used to find octaveUpValue.
	    boolean stop = false;
	    int i = 6; //index of specific array, start at last value and work backwards
	    int lastPitch = NoteUtilities.findPitch(this.getKey(), this.getKeyIsSharp()); //pitch of tonic
	    while(!stop) {
	    	octaveDownPitch--; //1 rel. pitch further down
	    	int thisPitch = NoteUtilities.findPitch(this.getSpecificArray()[i],this.getKeyIsSharp());
	    	if(thisPitch > lastPitch) { //when the array of 12 notes comes back around, stop
	    		stop = true;
	    	}
	    	lastPitch = thisPitch;
	    	i--;
	    }
	    
	    int octaveUpPitch = 8 + octaveDownPitch;
	    this.octaveUpValue = octaveUpPitch;
	    this.octaveDownValue = octaveDownPitch;
    }

    
    //accessor methods
    public String getKey()
    {
        return key;
    }
    public String getMode()
    {
        return mode;
    }
    public int getMeasures()
    {
        return measures;
    }
    public boolean getTopLineIsCF()
    {
        return topLineIsCF;
    }
    public String[] getSpecificArray()
    {
    	return specificNotes;
    }
    public boolean getKeyIsSharp()
    {
    	return keyIsSharp;
    }
    public int getOctaveOfCF()
    {
    	return octaveOfCF;
    }
    public int getOctaveUpValue()
    {
    	return octaveUpValue;
    }
    public int getOctaveDownValue()
    {
    	return octaveDownValue;
    }
    public String toString() //used for purpose of testing
    {
        return key + " " + mode + " " + measures + " " + topLineIsCF;
    }
}