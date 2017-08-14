import java.util.LinkedList;

public class programText {
	LinkedList<text> list=new LinkedList<text>();
	
	public void addEntry(String i,int op){
		text entry=new text(i,op);
		list.add(entry);
		
	}
	
	public void printList(){
		for(int index=0;index<list.size();index++)
			System.out.println(list.get(index).textSt+" "+list.get(index).textInt);
	}

}
