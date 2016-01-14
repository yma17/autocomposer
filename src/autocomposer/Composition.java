package autocomposer;

public class Composition implements NotesAndKeys
{
    private Model model;
    public static final int[] COUNTERPOINT_INTERVALS = {0,3,4,7,8,9,12};//intervals expressed in differences in MIDI numbers
    //0=unison, 3=minor 3rd, 4=Major 3rd, 7=Perfect 5th, 8=minor 6th, 9=Major 6th; 12=octave
    public Composition(Model m)
    {
        this.model = m;
    }
    public Note[][] compose() //composes the 2-voice counterpoint
    {
        Note[][] composition = new Note[2][model.getMeasures()];
        Note[] cantusFirmus = composeCantusFirmus();
        Note[] secondVoice;
        for(int x = 0; x < model.getMeasures(); x++)
        {
            if(model.getCF())
            {
                secondVoice = composeBottomVoice();
                composition[0][x] = cantusFirmus[x];
                composition[1][x] = secondVoice[x];
            }
            else
            {
                secondVoice = composeTopVoice();
                composition[1][x] = cantusFirmus[x];
                composition[0][x] = secondVoice[x];
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
    private Note[] composeCantusFirmus() //composes the cantus firmus
    {
        Note[] cantusFirmus = new Note[model.getMeasures()];
        
        //TODO
        cantusFirmus[0] = new Note(this.findPitch(model.getKey()), 4); //compose first note of the CF (tonic)
        cantusFirmus[cantusFirmus.length - 1] = cantusFirmus[0]; //compose last note of the CF (tonic)
        
        @SuppressWarnings("unused")
		int focalPoint = this.determineFocalPoint();
        @SuppressWarnings("unused")
		int preFPContourType = this.determinePreFPContour();
        @SuppressWarnings("unused")
		int postFPContourType = this.determinePostFPContour();
        
        //TODO
        @SuppressWarnings("unused")
		Note[] preFP = this.composePreFP(focalPoint, preFPContourType);
        @SuppressWarnings("unused")
		Note[] postFP = this.composePostFP(focalPoint, postFPContourType);
        
        return cantusFirmus;
    }
    private int determineFocalPoint() //helper method of composeCantusFirmus
    {
    	int n = model.getMeasures();
    	if(n == 9)
    		return 4; //focal point is 5th note of cantus firmus in this case (what is returned minus 1)
    	else if(n == 10)
    		return 5;
    	else if(n == 11)
    		return 6;
    	else if(n == 12) {
    		double x = Math.random();
    		if(x < 0.5)
    			return 6;
    		else
    			return 7;
    	}
    	else if(n == 13) {
    		double x = Math.random();
    		if(x < 0.5)
    			return 7;
    	}
    	return 8;
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
    private int determinePostFPContour() { //helper method of composeCantusFirmus. Determines the contour from the FP to the last note.
    	double x = Math.random();
    	if(x < 0.5)
    		return 1; //Type 1 = gradual motion downwards towards last note
    	else
    		return 2; //Type 2 = downward motion to below last note, then stepwise motion to last note
    }
    private Note[] composePreFP(int focalPoint, int contourType) {
    	//TODO
    	return new Note[0]; //so that it compiles
    }
    private Note[] composePostFP(int focalPoint, int contourType) {
    	//TODO
    	return new Note[0]; //so that it compiles
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
    //private void checkOneVoice(Note[] voice) //check if one voice obeys the guidelines of counterpoint
    //{
        //insert code here
    //}
    //private void checkBothVoices(Note[][] voices) //check if both voices obey the guidelines of counterpoint
    //{
        //insert code here
    //}
}
