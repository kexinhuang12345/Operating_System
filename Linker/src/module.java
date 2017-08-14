
public class module {
	useList useModule;
	programText textModule;
	int moduleAd=0;
	int moduleLength=0;
	public module(useList a,programText b,int ad,int len){
		useModule=a;
		textModule=b;
		moduleLength=len;
		moduleAd=ad;
		}
	
	public void printModule(){
		useModule.printList();
		textModule.printList();
		System.out.println(moduleAd);
		System.out.println(moduleLength);
		
	}
}
