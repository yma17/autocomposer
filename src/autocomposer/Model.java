package autocomposer;

import java.util.Arrays;

/* The Model serves as a template for the main composition algorithm to follow,
 * containing necessary intrinsic information of the counterpoint such as key,
 * mode, length, cantus firmus orientation, etc.
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
    public int modeValue; //to make DIATONIC_INTERVALS compatible with any mode (Ionian-0, Dorian-1, Phrygian-2, Lydian-3, Mixolydian-4, Aeolian-5)
    public int octaveUpValue; //how many notes up (relative pitch distance) from the tonic is required to raise the octave
    public int octaveDownValue; //how many notes down (relative pitch distance) from the tonic is required to lower the octave
    public Model() //in absence in user control, generates random key, mode, length, CF
    {
    	key = determineKey();
    	mode = determineMode();
        measures = determineMeasures();
        topLineIsCF = determineTopLineIsCF();
    }
    public Model(String key, String mode, int measures) //in presence of user control, set user-preferred key, mode, length. Randomly determines CF.
    {
    	this.key = key;
        this.mode = mode;
        this.measures = measures;
        
        if(mode.equals("Ionian"))
        	modeValue = 0;
        else if(mode.equals("Dorian"))
        	modeValue = 1;
        else if(mode.equals("Phrygian"))
        	modeValue = 2;
        else if(mode.equals("Lydian"))
        	modeValue = 3;
        else if(mode.equals("Mixolydian"))
        	modeValue = 4;
        else if(mode.equals("Aeolian"))
        	modeValue = 5;
        
        topLineIsCF = determineTopLineIsCF();
        keyIsSharp = determineSharp(key,mode);
        octaveOfCF = determineOctaveofCF(key,topLineIsCF,keyIsSharp);
        this.specificNotes = determineSpecificNotes(key,mode,keyIsSharp);
        this.determineOctaveValues();
    }
    private String determineKey() //In absence of user control only. For sake of simplicity, each mode will be paired up with one specific key such that there are no accidentals
    {
        double x = Math.random();
        if(x < 0.143)
        	return "A";
        else if(x < 0.286)
        	return "B";
        else if(x < 0.429)
        	return "C";
        else if(x < 0.571)
        	return "D";
        else if(x < 0.714)
        	return "E";
        else if(x < 0.857)
        	return "F";
        else
        	return "G";
    }
    private String determineMode() {
    	double x = Math.random();
    	if(x < 0.167)
        	return "Ionian";
        else if(x < 0.333)
        	return "Dorian";
        else if(x < 0.500)
        	return "Phrygian";
        else if(x < 0.667)
        	return "Lydian";
        else if(x < 0.833)
        	return "Mixolydian";
        else
        	return "Aeolian";
    }
    private int determineMeasures() { //will be between 8 and 12 measures
        double x = Math.random() * 5;
        return (int) x + 8;
    }
    private boolean determineTopLineIsCF()
    {
        double x = Math.random();
        if(x < 0.5)
            return true;
        return false;
    }
    private boolean determineSharp(String key, String mode) {
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
    private int determineOctaveofCF(String key, boolean topLineIsCF, boolean keyIsSharp) {
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
    public String[] determineSpecificNotes(String key, String mode, boolean sharp)
    {
    	String[] specificNotes = new String[7];
    	specificNotes = createRawScale(key,mode,sharp);
    	
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
    private String[] createRawScale(String key, String mode, boolean sharp) {
    	String[] rawScale = new String[7];
    	int keyIndex = 0; //index of key in NoTES or NOTES_SHARPS
    	if(sharp) {
            for(int x = 0; x < NOTES_SHARPS.length; x++) {
               if(key.equals(NOTES_SHARPS[x]))
                    keyIndex = x;
            }
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
    public void determineOctaveValues() {
    	int octaveDownPitch = 0;
	    boolean stop = false;
	    int i = 6;
	    int lastPitch = NoteUtilities.findPitch(this.getKey(), this.getKeyIsSharp());
	    while(!stop) {
	    	octaveDownPitch--;
	    	int thisPitch = NoteUtilities.findPitch(this.getSpecificArray()[i],this.getKeyIsSharp());
	    	if(thisPitch > lastPitch) {
	    		stop = true;
	    	}
	    	lastPitch = thisPitch;
	    	i--;
	    }
	    int octaveUpPitch = 8 + octaveDownPitch;
	    this.octaveUpValue = octaveUpPitch;
	    this.octaveDownValue = octaveDownPitch;
    }
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
    public int getModeValue()
    {
    	return modeValue;
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