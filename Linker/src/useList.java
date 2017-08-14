import java.util.ArrayList;
import java.util.LinkedList;

public class useList {
	LinkedList<symbolUsedTrack> list=new LinkedList<symbolUsedTrack>();
	
	public void addEntry(String i,ArrayList<Integer> ad){
		symbolUsedTrack entry=new symbolUsedTrack(i,ad);
		list.add(entry);
		
	}

	public void printList(){
		for(int index=0;index<list.size();index++){
			System.out.println(list.get(index).useListString);
			for(int i=0;i<list.get(index).arrlist.size();i++){
				System.out.println(list.get(index).arrlist.get(i));
			}
		}
			
	}
	
	public void exceedModuleSize(int moduleSize, int moduleNumber){
		for(int i=0;i<list.size();i++){
			for(int j=0;j<list.get(i).arrlist.size();j++){
				if(list.get(i).arrlist.get(j)>=moduleSize){
					list.get(i).exceedModuleSize=true;
					break;
				}
			}
		}
		
		
	}
/*	public void multipleVariableUsed(){
		for(int i=0;i<list.size();i++){
			for(int j=i;j<list.size();j++){
				for(int k=0;k<list.get(i).arrlist.size();k++){
					for(int l=0;l<list.get(j).arrlist.size();l++){
						if(list.get(i).arrlist.get(k)==list.get(j).arrlist.get(l)){
							list.get(l).multipleVar=true;
						}
					}
				}
			}
		}	
	}*/
}
