package autocomposer;

import java.util.Arrays;
public class Model implements NotesAndKeys
{
    public String key; // key of the counterpoint
    public String mode; //mode of the counterpoint
    public int measures; //number of measures
    public int modeValue; //to make DIATONIC_INTERVALS compatible with any mode (Ionian-0, Dorian-1, Phrygian-2, Lydian-3, Mixolydian-4, Aeolian-5)
    public boolean topLineisCF; //whether top line is cantus firmus(CF)
    public String[] specificNotes; //specific notes of the key and mode
    public boolean sharp; //sharp key?
    public Model() //in absence in user control, generate random key, mode, length, CF
    {
    	mode = determineKeyAndMode();
        measures = determineMeasureNumber();
        topLineisCF = determineCF();
    }
    public Model(String key, String mode, int measures) //in presence of user control, set user-preferred key, mode, length. Randomly determines CF.
    {
    	this.key = key;
    	//keep keys in circle of 5ths
    	if(key.equals("C-sharp"))
    		this.key = "D-flat";
    	else if(key.equals("D-sharp"))
    		this.key = "E-flat";
    	else if(key.equals("G-sharp"))
    		this.key = "A-flat";
    	else if(key.equals("A-sharp"))
    		this.key = "B-flat";
        
        this.mode = mode;
        
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
        else
        	modeValue = 5; //Aeolian
        
        this.measures = measures;
        topLineisCF = determineCF();
        
        this.specificNotes = determineSpecificNotes();
        
        if(key.equals("F-sharp") || key.equals("B"))
        	sharp = true;
        else if(key.equals("E")) {
        	if(!mode.equals("Phrygian"))
        		sharp = true;
        }
        else if(key.equals("A")) {
        	if(!mode.equals("Phrygian") && !mode.equals("Aeolian"))
        		sharp = true;
        }
        else if(key.equals("D")) {
        	if(mode.equals("Ionian") || mode.equals("Lydian") || mode.equals("Mixolydian"))
        		sharp = true;
        }
        else if(key.equals("G")) {
        	if(mode.equals("Ionian") || mode.equals("Lydian"))
        		sharp = true;
        }
        else
        	sharp = false;
    }
    public String toString() //used for purpose of testing
    {
        return key + " " + mode + " " + measures + " " + topLineisCF;
    }
    public String getKey()
    {
        return key;
    }
    public String getMode()
    {
        return mode;
    }
    public int getModeValue()
    {
    	return modeValue;
    }
    public int getMeasures()
    {
        return measures;
    }
    public boolean getCF()
    {
        return topLineisCF;
    }
    private String determineKeyAndMode() //In absence of user control only. For sake of simplicity, each mode will be paired up with one specific key such that there are no accidentals
    {
        double x = Math.random();
        if(x < 0.2)
        {
        	key = "A";
            return MODES[0];
        }
        else if(x < 0.4)
        {
            key = "C";
        	return MODES[1];
        }
        else if(x < 0.55)
        {
            key = "D";
        	return MODES[2];
        }
        else if(x < 0.7)
        {
            key = "E";
            return MODES[3];
        }
        else if(x < 0.85)
        {
            key = "F";
        	return MODES[4];
        }
        else
        {
            key = "G";
        	return MODES[5];
        }
    }
    private int determineMeasureNumber()
    {
        double x = Math.random() * 5;
        return (int) x + 8;
    }
    private boolean determineCF()
    {
        double x = Math.random();
        if(x < 0.5)
            return true;
        return false;
    }
    public String[] determineSpecificNotes()
    {
    	String[] arr = new String[7];
    	
    	int keyValue = 0;
    	
    	if(key.equals("G")||key.equals("D")||key.equals("A")||key.equals("E")||key.equals("B")||key.equals("F-sharp")) {
            for(int x = 0; x < NOTES_SHARPS.length; x++) {
               if(key.equals(NOTES_SHARPS[x]))
                    keyValue = x;
            }
            if(mode.equals("Ionian")) {
                for(int x = 0; x < arr.length; x++)
                    arr[x] = NOTES_SHARPS[(keyValue + IONIAN_DIATONIC_INTERVALS[x])%NOTES.length];
            }
            else if(mode.equals("Dorian")) {
                for(int x = 0; x < arr.length; x++)
                    arr[x] = NOTES_SHARPS[(keyValue + DORIAN_DIATONIC_INTERVALS[x])%NOTES.length];
            }
            else if(mode.equals("Phrygian")) {
                for(int x = 0; x < arr.length; x++)
                    arr[x] = NOTES_SHARPS[(keyValue + PHRYGIAN_DIATONIC_INTERVALS[x])%NOTES.length];
            }
            else if(mode.equals("Lydian")) {
                for(int x = 0; x < arr.length; x++)
                    arr[x] = NOTES_SHARPS[(keyValue + LYDIAN_DIATONIC_INTERVALS[x])%NOTES.length];
            }
            else if(mode.equals("Mixolydian")) {
                for(int x = 0; x < arr.length; x++)
                    arr[x] = NOTES_SHARPS[(keyValue + MIXOLYDIAN_DIATONIC_INTERVALS[x])%NOTES.length];
            }
            else { //Aeolian 
                for(int x = 0; x < arr.length; x++)
                    arr[x] = NOTES_SHARPS[(keyValue + AEOLIAN_DIATONIC_INTERVALS[x])%NOTES.length];
            }
        }
        else {
            for(int x = 0; x < NOTES.length; x++) {
    		    if(key.equals(NOTES[x]))
    			    keyValue = x;
    			 }
            if(mode.equals("Ionian")) {
                for(int x = 0; x < arr.length; x++)
                    arr[x] = NOTES[(keyValue + IONIAN_DIATONIC_INTERVALS[x])%NOTES.length];
            }
            else if(mode.equals("Dorian")) {
                for(int x = 0; x < arr.length; x++)
                    arr[x] = NOTES[(keyValue + DORIAN_DIATONIC_INTERVALS[x])%NOTES.length];
            }
            else if(mode.equals("Phrygian")) {
                for(int x = 0; x < arr.length; x++)
                    arr[x] = NOTES[(keyValue + PHRYGIAN_DIATONIC_INTERVALS[x])%NOTES.length];
            }
            else if(mode.equals("Lydian")) {
                for(int x = 0; x < arr.length; x++)
                    arr[x] = NOTES[(keyValue + LYDIAN_DIATONIC_INTERVALS[x])%NOTES.length];
            }
            else if(mode.equals("Mixolydian")) {
                for(int x = 0; x < arr.length; x++)
                    arr[x] = NOTES[(keyValue + MIXOLYDIAN_DIATONIC_INTERVALS[x])%NOTES.length];
            }
            else { //Aeolian 
                for(int x = 0; x < arr.length; x++)
                    arr[x] = NOTES[(keyValue + AEOLIAN_DIATONIC_INTERVALS[x])%NOTES.length];
            }
        }
    	
    	//check enharmonics, double accidentals
    	String ch = key.substring(0,1);
    	int a = Arrays.binarySearch(BASIC_NOTES,ch);
    	for(int x = 1; x < arr.length; x++) {
    		if(arr[x].indexOf(BASIC_NOTES[(a+x)%7]) < 0) { //listed note in arr not expected note
    			//enharmonics
    			if(arr[x].equals("C"))
    				arr[x] = "B-sharp";
    			else if(arr[x].equals("B"))
    				arr[x] = "C-flat";
    			else if(arr[x].equals("E"))
    				arr[x] = "F-flat";
    			else if(arr[x].equals("F"))
    				arr[x] = "E-sharp";
    			else if(arr[x].equals("A-sharp"))
    				arr[x] = "B-flat";
    			else if(arr[x].equals("B-flat"))
    				arr[x] = "A-sharp";
    			else if(arr[x].equals("G-sharp"))
    				arr[x] = "A-flat";
    			else if(arr[x].equals("A-flat"))
    				arr[x] = "G-sharp";
    			else if(arr[x].equals("F-sharp"))
    				arr[x] = "G-flat";
    			else if(arr[x].equals("G-flat"))
    				arr[x] = "F-sharp";
    			else if(arr[x].equals("D-sharp"))
    				arr[x] = "E-flat";
    			else if(arr[x].equals("E-flat"))
    				arr[x] = "D-sharp";
    			else if(arr[x].equals("C-sharp"))
    				arr[x] = "D-flat";
    			else if(arr[x].equals("D-flat"))
    				arr[x] = "C-sharp";
    			//double sharps, double flats
    			if(arr[x].indexOf(BASIC_NOTES[(a+x-1)%7]) >= 0 && arr[x].indexOf("-sharp") < 0) { //double flats
    				if(key.equals("D-flat")&&mode.equals("Phrygian")&&arr[x].equals("D")
    						|| key.equals("A-flat")&&mode.equals("Phrygian")&&arr[x].equals("A"))
    					arr[x] = BASIC_NOTES[(a+x)&7] + "-double-flat";
    				else
    					arr[x] = BASIC_NOTES[(a+x+1)&7] + "-double-flat";
    			}
    			else if(arr[x].indexOf(BASIC_NOTES[(a+x+1)%7]) >= 0)
    				arr[x] = BASIC_NOTES[(a+x-1)%7] + "-double-sharp";
    		}
    	}
    
    	return arr;
    }
    public String[] getSpecificArray()
    {
    	return specificNotes;
    }
    public boolean getSharp()
    {
    	return sharp;
    }
}