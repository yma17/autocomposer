package autocomposer;


import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.MidiChannel;



public class SoundPlayer {
	public static int MidiChannelNumber = 0; // Piano
	public static int DefaultMidiVelocity = 80;
	
	public static int Duration = 1000;
	
	
	private static Synthesizer synth = null;

	private MidiChannel channel;

	public static void initSynth(){
		if( synth == null ){
			try {
				synth = MidiSystem.getSynthesizer();
				synth.open();
			} catch (MidiUnavailableException e) {
				e.printStackTrace();
			}
		}
	}
	
	public SoundPlayer() {
		initSynth();
		this.channel = synth.getChannels()[MidiChannelNumber];
	}
	
	public void play(Note note) {
		int n = note.midiValue();
		this.channel.noteOn(n, DefaultMidiVelocity);
		try {
			Thread.sleep(Duration);
		} catch (InterruptedException e) {
			System.err.println("[note playing interrupted]");
		}
		this.channel.noteOff(n);
	}
	
	public void play(Note[] notes) {
		for( Note note : notes ){
			this.play(note);
		}
	}
}
