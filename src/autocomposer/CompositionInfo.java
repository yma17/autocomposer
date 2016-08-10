package autocomposer;

/* This class contains information of the structure of the composed work.
 * It is used extensively during the composition process to decide what to be composed next.
 * 
 * Variable of the Composition class.
 */
public class CompositionInfo {
	private int leapsSoFar;
	private int stepsSoFar;
	private int largeLeapsSoFar; //4th or higher
	private int smallerIntervalsSoFar;
	private int maxPitch;
	private int minPitch;
	private int focalPoint; //index of the FP in CF (nth note minus 1)
	private int preFPContourType; //of the cantus firmus
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
}
