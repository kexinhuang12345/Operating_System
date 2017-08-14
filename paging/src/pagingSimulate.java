import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Scanner;

public class pagingSimulate {

	public static void main(String[] args)throws IOException{
		
		int machineSize=Integer.parseInt(args[0]);
		int pageSize=Integer.parseInt(args[1]);
		int processSize=Integer.parseInt(args[2]);
		int jobMix=Integer.parseInt(args[3]);
		int numRefprocess=Integer.parseInt(args[4]);
		String replace=args[5];
		int quantum=3;
		int processNum = 0;
		process[] processTable=null;
		
		System.out.println("The machine size is "+ machineSize);
		System.out.println("The page size is "+pageSize);
		System.out.println("The process size is "+processSize);
		System.out.println("The job mix number is "+jobMix);
		System.out.println("The number of references per process is "+numRefprocess);
		System.out.println("The replacement algorithm is "+replace);
		
		if(jobMix==1){
			processTable=new process[1];
			processTable[0]=new process(1,0,0,0);
			processNum=1;
			
		}else if(jobMix==2){
			processTable=new process[4];
			for(int i=0;i<4;i++){
				processTable[i]=new process(1,0,0,0);
			}
			processNum=4;

			
		}else if(jobMix==3){
			processTable=new process[4];
			for(int i=0;i<4;i++){
				processTable[i]=new process(0,0,0,1);
				
			}
			processNum=4;

			
		}else if(jobMix==4){
			processTable=new process[4];
			processTable[0]=new process(0.75,0.25,0,0);
			processTable[1]=new process(0.75,0,0.25,0);
			processTable[2]=new process(0.75,0.125,0.125,0);
			processTable[3]=new process(0.5,0.125,0.125,0.25);
			processNum=4;

		}
		
		
		//random scanner
		String workingDir=System.getProperty("user.dir");
		workingDir=workingDir.concat("/random-numbers.txt");
		String inputString2=readFile(workingDir,Charset.defaultCharset());
		Scanner randomInput=new Scanner(inputString2);
				
		LinkedList<frame> lruList=new LinkedList<frame>();
		LinkedList<frame> fifoList=new LinkedList<frame>();

		//initiate frames
		int frames=machineSize/pageSize;
		frame[] frameTable=new frame[frames];
		for(int i=0;i<frames;i++){
			frameTable[i]=new frame();
			frameTable[i].index=i;
			lruList.add(frameTable[i]);
		}
		
		//lru linkedlist
		
		
		boolean loop=true;
		int processTrack=0;
		int timeCount=1;
		int terminateCount=0;
		//start
		while(loop){
			//quantum=3
			for(int ref=0;ref<quantum;ref++){
				if(processTable[processTrack].terminated!=true){
					if(processTable[processTrack].time==1){
						//the first reference of the process
						processTable[processTrack].curWord=(111*(processTrack+1)+processSize)%processSize;
					}
					//simulate the current reference
					int hitOrNot=containFrame(frameTable,processTrack,processTable[processTrack].curWord/pageSize);
					if(hitOrNot==-1){
						//not hit, fault
						//still has free frames left
						boolean nofree=true;
						for(int i=(frameTable.length-1);i>=0;i--){
							if(frameTable[i].curProcess!=null){

							}else{
								nofree=false;
								frameTable[i].pageNum=processTable[processTrack].curWord/pageSize;
								frameTable[i].processNum=processTrack;
								frameTable[i].curProcess=processTable[processTrack];
								if(replace.equals("lru")){
									lruList.remove(frameTable[i]);
									lruList.add(frameTable[i]);
								}
								if(replace.equals("fifo")){
									fifoList.add(frameTable[i]);
								}
								//System.out.println((processTrack+1)+" references word"+processTable[processTrack].curWord+" (page "+processTable[processTrack].curWord/pageSize+") at time"+timeCount+":"+"fault, using free frame"+i);
								processTable[processTrack].faultCount++;
								frameTable[i].curPageBegin=timeCount;
								i=-1;
								break;
							}
							
							
						}
						
						if(nofree){
							//no free frame left, evict
							processTable[processTrack].faultCount++;
							if(replace.equals("lru")){
								//System.out.println((processTrack+1)+" references word"+processTable[processTrack].curWord+" (page "+(processTable[processTrack].curWord/pageSize)+") at time"+timeCount+":"+"fault, evict page "+lruList.getFirst().pageNum+" of "+(lruList.getFirst().processNum+1)+" from frame"+lruList.getFirst().index);
								processTable[lruList.getFirst().processNum].sumResidency+=(timeCount-lruList.getFirst().curPageBegin);
								processTable[lruList.getFirst().processNum].evictNum++;

								lruList.getFirst().pageNum=processTable[processTrack].curWord/pageSize;
								lruList.getFirst().processNum=processTrack;
								lruList.getFirst().curProcess=processTable[processTrack];
								lruList.getFirst().curPageBegin=timeCount;
								lruList.add(lruList.getFirst());
								lruList.remove();
								
							}else if(replace.equals("fifo")){
								//System.out.println((processTrack+1)+" references word"+processTable[processTrack].curWord+" (page "+(processTable[processTrack].curWord/pageSize)+") at time"+timeCount+":"+"fault, evict page "+fifoList.getFirst().pageNum+" of "+(fifoList.getFirst().processNum+1)+" from frame"+fifoList.getFirst().index);
								processTable[fifoList.getFirst().processNum].sumResidency+=(timeCount-fifoList.getFirst().curPageBegin);
								processTable[fifoList.getFirst().processNum].evictNum++;

								fifoList.getFirst().pageNum=processTable[processTrack].curWord/pageSize;
								fifoList.getFirst().processNum=processTrack;
								fifoList.getFirst().curProcess=processTable[processTrack];
								fifoList.getFirst().curPageBegin=timeCount;
								fifoList.add(fifoList.getFirst());
								fifoList.remove();
								
							}else if(replace.equals("random")){
								double randomAlgo=randomOS(randomInput);
								int nextWord=(int) ((randomAlgo+frames)%frames);
								processTable[frameTable[nextWord].processNum].sumResidency+=(timeCount-frameTable[nextWord].curPageBegin);
								processTable[frameTable[nextWord].processNum].evictNum++;

								//System.out.println((processTrack+1)+"uses random number: "+randomAlgo);
								//System.out.println((processTrack+1)+" references word"+processTable[processTrack].curWord+" (page "+(processTable[processTrack].curWord/pageSize)+") at time"+timeCount+":"+"fault, evict page "+frameTable[nextWord].pageNum+" of "+(frameTable[nextWord].processNum+1)+" from frame"+frameTable[nextWord].index);
								int pageNumber=processTable[processTrack].curWord/pageSize;
								int processNumber=processTrack;
								process curProcess=processTable[processTrack];
								frameTable[nextWord].curProcess=curProcess;
								frameTable[nextWord].pageNum=pageNumber;
								frameTable[nextWord].processNum=processNumber;
								frameTable[nextWord].curPageBegin=timeCount;
							}
						}
					}else{
						//hit
						//for lru
						if(replace.equals("lru")){
							frameTable[hitOrNot].timeStamp=timeCount;
							lruList.remove(frameTable[hitOrNot]);
							lruList.add(frameTable[hitOrNot]);
							//put in the end of the list
						}
						//System.out.println((processTrack+1)+" references word"+processTable[processTrack].curWord+" (page "+processTable[processTrack].curWord/pageSize+") at time"+timeCount+":"+"hit in frame "+hitOrNot);
						
					}
					
					if(processTable[processTrack].time==numRefprocess){
						processTable[processTrack].terminated=true;
						terminateCount++;
						
					}
					
					//if(processTable[processTrack].terminated!=true){		
						processTable[processTrack].time++;
						timeCount++;
						//if the process is terminated, no need to calculate the next reference
						//calculate the next reference
						double random=randomOS(randomInput);
						double determinant=random/(Integer.MAX_VALUE+1d);
						//System.out.println("use random number "+random);
						int nextWord=-1;
						if(determinant<processTable[processTrack].A){
							nextWord=(processTable[processTrack].curWord+1+processSize)%processSize;
						}else if(determinant<(processTable[processTrack].A+processTable[processTrack].B)){
							nextWord=(processTable[processTrack].curWord-5+processSize)%processSize;
						}else if(determinant<(processTable[processTrack].A+processTable[processTrack].B+processTable[processTrack].C)){
							nextWord=(processTable[processTrack].curWord+4+processSize)%processSize;
						}else{
							double randomCase4=randomOS(randomInput);
							nextWord=(int) ((randomCase4+processSize)%processSize);
							//System.out.println("use random number "+randomCase4);
						}
						processTable[processTrack].curWord=nextWord;
					}
				//}
			}
			
			processTrack++;
			if(processTrack==processNum){
				//iterate over all the processes
				processTrack=0;
			}
			if(terminateCount==processNum){
				loop=false;
			}
			
		}
		
		int totalFaultCount=0;
		double totalResidencyCount=0;
		double totalEviction=0;
		System.out.println("  ");
		for(int i=0;i<processNum;i++){
			totalFaultCount+=processTable[i].faultCount;
			totalEviction+=processTable[i].evictNum;
			totalResidencyCount+=processTable[i].sumResidency;
			System.out.println("Process "+(i+1)+" had "+processTable[i].faultCount+" faults and "+processTable[i].sumResidency/processTable[i].evictNum+" average residency.");
		}
		
		System.out.println(" ");
		System.out.println("The total number of faults is "+totalFaultCount+" and the overall average residency is "+totalResidencyCount/totalEviction);
		
		randomInput.close();
	}
	
	
	
	public static int randomOS(Scanner input3) throws IOException{
		int x=0;
		if(input3.hasNext()){
			x=input3.nextInt();
		}
		return x;
		
	}
	
	public static int containFrame(frame[] frameTable,int process,int pageNum){
		//if not contain, return -1
		//if contain, return the index of the frame 
		for(int i=0;i<frameTable.length;i++){
			
			if((frameTable[i].pageNum==pageNum)&&(frameTable[i].processNum==process)){
				return i;
			}
			
		}
		
		return -1;
	}
	public static String readFile(String path, Charset encoding) 
			  throws IOException 
			{
			  byte[] encoded = Files.readAllBytes(Paths.get(path));
			  return new String(encoded, encoding);
			}
	
}



