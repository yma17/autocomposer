public class Composition
{
    private Model model;
    public static final int[] COUNTERPOINT_INTERVALS = {0,3,4,7,8,9,12};//intervals expressed in differences in MIDI numbers
    //0=unison, 3=minor 3rd, 4=Major 3rd, 7=Perfect 5th, 8=minor 6th, 9=Major 6th; 12=octave
    public Composition(Model m)
    {
        this.model = m;
    }
    public int[][] compose() //composes the 2-voice counterpoint
    {
        int[][] composition = new int[2][model.numMeasures()];
        int[] cantusFirmus = composeCantusFirmus(model);
        int[] secondVoice;
        for(int x = 0; x < model.numMeasures(); x++)
        {
            if(model.getCF())
            {
                secondVoice = composeBottomVoice(model);
                composition[0][x] = cantusFirmus[x];
                composition[1][x] = secondVoice[x];
            }
            else
            {
                secondVoice = composeTopVoice(model);
                composition[1][x] = cantusFirmus[x];
                composition[0][x] = secondVoice[x];
            }
        }
        return composition;
    }
    private int[] composeCantusFirmus() //composes the cantus firmus
    {
        int[] cantusFirmus = new int[model.numMeasures()];
        //insert code here
        return cantusFirmus;
    }
    private int[] composeBottomVoice() //composes the bottom voice, in case the CF is the top voice
    {
        int[] bottomVoice = new int[model.numMeasures()];
        //insert code here
        return bottomVoice;
    }
    private int[] composeTopVoice() //composes the top voice, in case the CF is the bottom voice;
    {
        int[] topVoice = new int[model.numMeasures()];
        //insert code here
        return topVoice;
    }
    private void checkOneVoice(int[] voice) //check if one voice obeys the guidelines of counterpoint
    {
        //insert code here
    }
    private void checkBothVoices(int[][] voices) //check if both voices obey the guidelines of counterpoint
    {
        //insert code here
    }
}
