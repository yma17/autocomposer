package testing;

import autocomposer.Note;
import autocomposer.SoundPlayer;

public class SynthTester {

	public static void main(String[] args) {
		SoundPlayer player = new SoundPlayer();
		
		int[] diatonicPitches = {0, 2, 4, 5, 7, 9, 11};
		Note[] notes = new Note[diatonicPitches.length]; 
		for(int i = 0; i < diatonicPitches.length; ++i){
			//notes[i] = new Note(diatonicPitches[i]);
		}
		
		//player.play(notes);
	}

}
