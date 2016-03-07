package autocomposer;

import java.util.ArrayList;

public class CounterpointError {
	public ArrayList<Integer> error;
	public int endIndex;
	public CounterpointError(int note,int index)
	{
		error = new ArrayList<Integer>();
		error.add(note);
		endIndex = index;
	}
	public void addToError(int note)
	{
		error.add(0,note);
	}
}
