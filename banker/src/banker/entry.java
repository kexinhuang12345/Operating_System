package banker;
//from file entry, will store in to correspondent task
public class entry {

	String activity;
	int taskNum;
	int delay;
	int third;
	int fourth;
	int delayed;
	public entry(String activity, int taskNum, int delay, int resourceType, int resourceNum) {
		super();
		this.activity = activity;
		this.taskNum = taskNum;
		this.delay = delay;
		this.third = resourceType;
		this.fourth = resourceNum;
		delayed=delay;
	}
	
	public void printEntry(){
		System.out.println(activity+" "+taskNum+" "+delay+" "+third+" "+fourth);
		
	}
}
