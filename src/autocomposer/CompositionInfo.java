package autocomposer;

/* This class contains information of the structure of the composed work.
 * It is used extensively during the composition process to decide what to be composed next.
 * 
 * Variable of the Composition class.
 */
public class CompositionInfo {
	private int leapsSoFar; //3rd or higher
	private int stepsSoFar; //2nd
	private int largeLeapsSoFar; //4th or higher
	private int smallerIntervalsSoFar; //3rd or smaller
	private int maxPitch; //highest relative pitch
	private int minPitch; //lowest relative pitch
	private int focalPoint; //index of the FP in CF (nth note minus 1)
	private int preFPContourType; //of the cantus firmus
	private int preFPBeginPoint; //note-by-note begins composing here (before FP)
	private int preFPEndPoint; //note-by-note stops composing here (before FP)
	private int postFPBeginPoint; //note-by-note begins composing here (after FP)
	private int postFPEndPoint; //note-by-note stops composing here (after FP)
	private int notesComposed; //of the current line (once CF is done composing, will be set to 0)
	
	public CompositionInfo() {
		//default values
		leapsSoFar = 0;
		stepsSoFar = 0;
		largeLeapsSoFar = 0;
		smallerIntervalsSoFar = 0;
		maxPitch = Integer.MAX_VALUE;
		minPitch = Integer.MIN_VALUE;
	}
	public int getLeapsSoFar() {
		return leapsSoFar;
	}
	public void incrementLeapsSoFar(int number) {
		leapsSoFar += number;
	}
	public int getStepsSoFar() {
		return stepsSoFar;
	}
	public void incrementStepsSoFar(int number) {
		stepsSoFar += number;
	}
	public int getLargeLeapsSoFar() {
		return largeLeapsSoFar;
	}
	public void incrementLargeLeapsSoFar(int number) {
		largeLeapsSoFar += number;
	}
	public int getSmallerIntervalsSoFar() {
		return smallerIntervalsSoFar;
	}
	public void incrementSmallerIntervalsSoFar(int number) {
		smallerIntervalsSoFar += number;
	}
	public int getMaxPitch() {
		return maxPitch;
	}
	public void setMaxPitch(int pitch) {
		maxPitch = pitch;
	}
	public int getMinPitch() {
		return minPitch;
	}
	public void setMinPitch(int pitch) {
		minPitch = pitch;
	}
	public int getFocalPoint() {
		return focalPoint;
	}
	public void setFocalPoint(int focalPoint) { //determined in composeCantusFirmus
		this.focalPoint = focalPoint;
	}
	public int getPreFPContourType() {
		return preFPContourType;
	}
	public void setPreFPContourType(int type) { //determined in composeCantusFirmus
		preFPContourType = type;
	}
	public int getPreFPBeginPoint() {
		return preFPBeginPoint;
	}
	public void setPreFPBeginPoint(int location) { //determined in composeCantusFirmus
		preFPBeginPoint = location;
	}
	public int getPreFPEndPoint() {
		return preFPEndPoint;
	}
	public void setPreFPEndPoint(int location) { //determined in composeCantusFirmus
		preFPEndPoint = location;
	}
	public int getPostFPBeginPoint() {
		return postFPBeginPoint;
	}
	public void setPostFPBeginPoint(int location) { //determined in composeCantusFirmus
		postFPBeginPoint = location;
	}
	public int getPostFPEndPoint() {
		return postFPEndPoint;
	}
	public void setPostFPEndPoint(int location) { //determined in composeCantusFirmus
		postFPEndPoint = location;
	}
	public int getNotesComposed() {
		return notesComposed;
	}
	public void incrementNotesComposed() {
		notesComposed++;
	}
	public void reduceNotesComposed() {
		notesComposed--;
	}
	public String toString() {
		//precondition: all instance variables initialized
		return "leapsSoFar: " + leapsSoFar + "\n"
				+ "stepsSoFar: " + stepsSoFar + "\n"
				+ "largeLeapsSoFar: " + largeLeapsSoFar + "\n"
				+ "smallerIntervalsSoFar: " + smallerIntervalsSoFar + "\n"
				+ "maxPitch: " + maxPitch + "\n"
				+ "minPitch: " + minPitch + "\n"
				+ "focalPoint: " + focalPoint + "\n"
				+ "preFPContourType: " + preFPContourType + "\n"
				+ "preFPBeginPoint: " + preFPBeginPoint + "\n"
				+ "preFPEndPoint: " + preFPEndPoint + "\n"
				+ "postFPBeginPoint: " + postFPBeginPoint + "\n"
				+ "postFPEndPoint: " + postFPEndPoint + "\n"
				+ "notesComposed: " + notesComposed;
	}
}
