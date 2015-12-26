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
        Note[][] composition = new Note[2][model.numMeasures()];
        Note[] cantusFirmus = composeCantusFirmus();
        Note[] secondVoice;
        for(int x = 0; x < model.numMeasures(); x++)
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
    private Note[] composeCantusFirmus() //composes the cantus firmus
    {
        Note[] cantusFirmus = new Note[model.numMeasures()];
        
        cantusFirmus[0] = new Note(model.getKey(), 4); //first note of the CF
        
        Note[] array = composeRestofCF();
        for(int i = 1; i < cantusFirmus.length; i++)
        	cantusFirmus[i] = array[i - 1];
        
        return cantusFirmus;
    }
    private Note[] composeRestofCF()
    {
    	
    }
    private Note[] composeBottomVoice() //composes the bottom voice, in case the CF is the top voice
    {
        Note[] bottomVoice = new Note[model.numMeasures()];
        //insert code here
        return bottomVoice;
    }
    private Note[] composeTopVoice() //composes the top voice, in case the CF is the bottom voice;
    {
        Note[] topVoice = new Note[model.numMeasures()];
        //insert code here
        return topVoice;
    }
    private void checkOneVoice(Note[] voice) //check if one voice obeys the guidelines of counterpoint
    {
        //insert code here
    }
    private void checkBothVoices(Note[][] voices) //check if both voices obey the guidelines of counterpoint
    {
        //insert code here
    }
}
