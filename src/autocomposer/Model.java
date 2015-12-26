package autocomposer;

public class Model implements NotesAndKeys
{
    public String key; // key of the counterpoint
    public String mode; //mode of the counterpoint
    public int measures; //number of measures
    public boolean topLineisCF; //whether top line is cantus firmus(CF)
    public Model() //in absence in user control, generate random key, mode, length, CF
    {
    	mode = determineKeyAndMode();
        measures = determineMeasureNumber();
        topLineisCF = determineCF();
    }
    public Model(String key, String mode, int measures) //in presence of user control, set user-preferred key, mode, length. Randomly determines CF.
    {
        this.key = key;
        this.mode = mode;
        this.measures = measures;
        topLineisCF = determineCF();
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
    public int numMeasures()
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
}
    
