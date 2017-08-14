import java.util.ArrayList;

public class symbolUsedTrack {
	String useListString;
	ArrayList<Integer> arrlist = new ArrayList<Integer>();
	boolean multipleVar=false;
	boolean exceedModuleSize=false;
	public symbolUsedTrack(String name,ArrayList<Integer> x){
		useListString=name;
		arrlist=x;
	}

	public String getI() {
		return useListString;
	}

	public void setI(String i) {
		this.useListString = i;
	}

	public ArrayList<Integer> getArrlist() {
		return arrlist;
	}

	public void setArrlist(ArrayList<Integer> arrlist) {
		this.arrlist = arrlist;
	}
}
