package autocomposer;

import java.util.ArrayList;
public final class ErrorNoteList {
	public ArrayList<Note> noteList;
	public int index;
	public int maxSize;
	public ErrorNoteList() {
		noteList = new ArrayList<Note>();
		index = -1; //default value - means list is not currently in use
		maxSize = -1; //deafult value
	}
	public ArrayList<Note> getList() {
		return noteList;
	}
	public void addNote(Note toBeAdded) {
		System.out.println("added"); //for testing
		noteList.add(toBeAdded);
	}
	public void clearNoteList() {
		System.out.println("cleared"); //for testing
		noteList.clear();
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		System.out.println("index: " + index);
		this.index = index;
	}
	public int getMaxSize() {
		return maxSize;
	}
	public void setMaxSize(int i) {
		System.out.println("maxSize: " + i);
		maxSize = i;
	}
}
