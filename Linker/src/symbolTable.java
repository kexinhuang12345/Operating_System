import java.util.LinkedList;

public class symbolTable {
	LinkedList<symbol> list=new LinkedList<symbol>();
	
	public void addEntry(String i,int rela,int base,int loc,int relative){
		symbol entry=new symbol(i,rela+base,loc,relative);
		list.add(entry);
	}
	public void printList(){
		for(int index=0;index<list.size();index++){
			
			if(list.get(index).relativeAddress>=list.get(index).moduleLength){
				list.get(index).symbolValue-=list.get(index).relativeAddress;	
				list.get(index).relativeAddress=0;
				System.out.print(list.get(index).symbolString+" : "+list.get(index).symbolValue);
				System.out.println(" Error: Definition exceeds module size; first word in module used.");

			}else{
				System.out.print(list.get(index).symbolString+" : "+list.get(index).symbolValue);
			}
			
			if(list.get(index).testIfMultipleDefined){
				System.out.print(" Error: This variable is multiply defined; first value used.");
			}
			System.out.println(" ");
		}
	}
}
