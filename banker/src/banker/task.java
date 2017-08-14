package banker;

import java.util.ArrayList;
// task as process which stores info 
public class task {
	ArrayList<entry> entryTable=new ArrayList<entry>();
	int[] curResourceStatus;
	int[] claim;
	int entryIndex=0;
	int delayed=0;
	boolean terminated =false;
	String output;
	int waitingTime=0;
	int taskNum;
	boolean blocked=false;
	boolean unblocked=false;
	public void printEntryTable(){
		for(int i=0; i<entryTable.size();i++){
			entryTable.get(i).printEntry();
		}
		
		System.out.print("claim is ");
		for(int i=0;i<claim.length;i++){
			System.out.print(claim[i]+" ");
		}
	}
}
