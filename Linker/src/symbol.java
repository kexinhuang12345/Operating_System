
public class symbol {
	String symbolString;
	int symbolValue=0;
	boolean used=false;
	int moduleLoc=0;
	boolean testIfMultipleDefined=false;
	int relativeAddress=0;
	int moduleLength=0;
	public symbol(String name,int x,int moduleloc,int rela){
		symbolString=name;
		symbolValue=x;
		moduleLoc=moduleloc;
		relativeAddress=rela;
	}
}
