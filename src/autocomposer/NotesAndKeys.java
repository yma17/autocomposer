package autocomposer;

/* Contains objective musical information, guidelines, and rules.
 * 
 */
public interface NotesAndKeys
{
	String[] BASIC_NOTES = {"A","B","C","D","E","F","G"};
    String[] NOTES = {"C","D-flat","D","E-flat","E","F","G-flat","G","A-flat","A","B-flat","B"};
    String[] NOTES_SHARPS = {"C","C-sharp","D","D-sharp","E","F","F-sharp","G","G-sharp","A","A-sharp","B"};
    String[] KEYS = {"C","G","D","A","E","B","G-flat","D-flat","A-flat","E-flat","B-flat","F"};
    String[] MODES = {"Aeolian","Ionian","Dorian","Phrygian","Lydian","Mixolydian","Locrian"};
    int[] PITCH_INDICES = {0,1,2,3,4,5,6};
    int[] COUNTERPOINT_INTERVALS = {0,3,4,7,8,9,12};//intervals expressed in differences in MIDI numbers
    //0=unison, 3=minor 3rd, 4=Major 3rd, 7=Perfect 5th, 8=minor 6th, 9=Major 6th; 12=octave
    int[] IONIAN_DIATONIC_INTERVALS = {0,2,4,5,7,9,11};
    int[] DORIAN_DIATONIC_INTERVALS = {0,2,3,5,7,9,10};
    int[] PHRYGIAN_DIATONIC_INTERVALS = {0,1,3,5,7,8,10};
    int[] LYDIAN_DIATONIC_INTERVALS = {0,2,4,6,7,9,11};
    int[] MIXOLYDIAN_DIATONIC_INTERVALS = {0,2,4,5,7,9,10};
    int[] AEOLIAN_DIATONIC_INTERVALS = {0,2,3,5,7,8,10};
    int[] ROOT_POSITION_TRIAD_INTERVALS = {0,2,4};
    int[] FIRST_INVERSION_TRIAD_INTERVALS = {0,2,5};
    int[] SECOND_INVERSION_TRIAD_INTERVALS = {0,3,5};
}
