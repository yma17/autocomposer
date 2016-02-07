package autocomposer;

public interface NotesAndKeys
{
    String[] NOTES = {"C","D-flat","D","E-flat","E","F","G-flat","G","A-flat","A","B-flat","B"};
    String[] KEYS = {"C","G","D","A","E","B","G-flat","D-flat","A-flat","E-flat","B-flat","F"};
    String[] MODES = {"Aeolian","Ionian","Dorian","Phrygian","Lydian","Mixolydian","Locrian"};
    int[] COUNTERPOINT_INTERVALS = {0,3,4,7,8,9,12};//intervals expressed in differences in MIDI numbers
    //0=unison, 3=minor 3rd, 4=Major 3rd, 7=Perfect 5th, 8=minor 6th, 9=Major 6th; 12=octave
    int[] DIATONIC_INTERVALS = {0,2,4,5,7,9,11}; 
}
