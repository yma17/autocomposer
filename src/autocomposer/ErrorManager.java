package autocomposer;

import java.util.ArrayList;

/* Class containing variables and methods designed to act as
 * distantly stored information during the error management process.
 */
public class ErrorManager {
    public boolean active; //signifies if the ErrorManager is currently in use
	public int minIndex; //the lowest index so far that must be re-composed
	public int maxIndex; //the highest index in the process (the index of the dead end that prompts activation of ErrorManager.
	public ArrayList<Note[]> validSections; //a list of revised, valid sections of the problematic section (from minIndex to maxIndex)
	public ErrorManager() {
		active = true;
		minIndex = -1; //default value - signifies "not in use"
		maxIndex = -1; //default value - signifies "not in use"
		validSections = new ArrayList<Note[]>();
	}
	public void activate(int index) {
		active = true;
		maxIndex = index;
		minIndex = maxIndex - 1; //default value, can be lowered further if needed
	}
	public void deactivate() {
		active = false;
		//reset values back to default
		minIndex = -1;
		maxIndex = -1;
	}
	public void lowerMinIndex() { //by one.
		minIndex--;
	}
	public void addToValidSections(Note[] validRoute) { //adds an ArrayList of notes (a valid route of notes from minIndex to maxIndex) to validRoutes.
		validSections.add(validRoute);
	}
	public boolean getActive() { return active; }
	public int getMinIndex() { return minIndex; }
	public int getMaxIndex() { return maxIndex; }
	public ArrayList<Note[]> getValidSections() { return validSections; }
}
