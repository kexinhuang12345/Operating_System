package banker;
import banker.task;
import banker.entry;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Scanner;

public class bankerSimul {
	public static void main(String[] args)throws IOException{
		//get filename from commandline
		String filename=args[0];
		String inputString=readFile(filename,Charset.defaultCharset());		
		Scanner input1=new Scanner(inputString);
		int taskN=input1.nextInt();
		task[] taskTable=new task[taskN];
		task[] taskTableBanker=new task[taskN];
		int resourceNumber=input1.nextInt();
		//initiate task table for fifo and banker
		for(int i=0;i<taskTable.length;i++){
			taskTable[i]=new task();
			taskTable[i].curResourceStatus=new int[resourceNumber];
			taskTable[i].entryIndex=resourceNumber;
			taskTable[i].claim=new int[resourceNumber];
			taskTable[i].taskNum=i;
			
			taskTableBanker[i]=new task();
			taskTableBanker[i].curResourceStatus=new int[resourceNumber];
			taskTableBanker[i].entryIndex=resourceNumber;
			taskTableBanker[i].claim=new int[resourceNumber];
			taskTableBanker[i].taskNum=i;
		}
		int[] resourceStatus=new int[resourceNumber];// resource manager initiation
		int[] resourceStatusBanker=new int[resourceNumber];// resource manager initiation
		
		//put data in resource manager 
		for(int i=0;i<resourceStatus.length;i++){
			int resourceContent=input1.nextInt();
			resourceStatus[i]=resourceContent;
			resourceStatusBanker[i]=resourceContent;
		}
		
		// read file into entry table for each task
		while(input1.hasNext()){
			//if it is text 
			String temp=input1.next();
			if(temp.equals("initiate")){
				int taskNum=input1.nextInt();
				int delay=input1.nextInt();
				int resourceType=input1.nextInt();
				int resourceNum=input1.nextInt();
				entry e=new entry(temp, taskNum,delay,resourceType,resourceNum);
				entry e1=new entry(temp, taskNum,delay,resourceType,resourceNum);
				taskTable[taskNum-1].entryTable.add(e);
				taskTable[taskNum-1].claim[resourceType-1]=resourceNum;
				taskTableBanker[taskNum-1].entryTable.add(e1);
				taskTableBanker[taskNum-1].claim[resourceType-1]=resourceNum;
			}else if(temp.equals("request")){
				int taskNum=input1.nextInt();
				int delay=input1.nextInt();
				int resourceType=input1.nextInt();
				int resourceNum=input1.nextInt();
				entry e=new entry(temp, taskNum,delay,resourceType,resourceNum);
				taskTable[taskNum-1].entryTable.add(e);
				entry e1=new entry(temp, taskNum,delay,resourceType,resourceNum);
				taskTableBanker[taskNum-1].entryTable.add(e1);
				
			}else if(temp.equals("release")){
				int taskNum=input1.nextInt();
				int delay=input1.nextInt();
				int resourceType=input1.nextInt();
				int resourceNum=input1.nextInt();
				entry e=new entry(temp, taskNum,delay,resourceType,resourceNum);
				taskTable[taskNum-1].entryTable.add(e);
				entry e1=new entry(temp, taskNum,delay,resourceType,resourceNum);
				taskTableBanker[taskNum-1].entryTable.add(e1);
				
			}else if(temp.equals("terminate")){
				int taskNum=input1.nextInt();
				int delay=input1.nextInt();
				int resourceType=input1.nextInt();
				int resourceNum=input1.nextInt();
				entry e=new entry(temp, taskNum,delay,resourceType,resourceNum);
				taskTable[taskNum-1].entryTable.add(e);
				entry e1=new entry(temp, taskNum,delay,resourceType,resourceNum);
				taskTableBanker[taskNum-1].entryTable.add(e1);
			}
			
		}
		
		//*******************************FIFO*****************************//

		int FIFOcycleCount=resourceNumber;
		boolean FIFOcycle=true;
		int terminateCount=0;
		int totalWaitingTime=0;
		int totalTimeFIFO=0;
		int taskNumTrack=taskTable.length;
		LinkedList<task> blockedList=new LinkedList<task>();
		String[] output1=new String[taskTable.length];
		//String[] outputFIFO=new String[taskTable.length+1];
		//System.out.println("In the cycle 0 task is initiated");

		while(FIFOcycle){
			int deadlockCount=0;

			//go to each task entry and iterate the entries		
			int[] tempResourceStatus=new int[resourceNumber];

			for(int i=0;i<resourceStatus.length;i++){
				tempResourceStatus[i]=resourceStatus[i];
				
			}
			
			//first check blocked list
			int blockedTrack=0;
			while(blockedTrack<blockedList.size()){
				if(resourceStatus[blockedList.get(blockedTrack).entryTable.get(blockedList.get(blockedTrack).entryIndex).third-1]>=blockedList.get(blockedTrack).entryTable.get(blockedList.get(blockedTrack).entryIndex).fourth){
					blockedList.get(blockedTrack).curResourceStatus[blockedList.get(blockedTrack).entryTable.get(blockedList.get(blockedTrack).entryIndex).third-1]+=blockedList.get(blockedTrack).entryTable.get(blockedList.get(blockedTrack).entryIndex).fourth;
					//request granted and update the task resource status
					tempResourceStatus[blockedList.get(blockedTrack).entryTable.get(blockedList.get(blockedTrack).entryIndex).third-1]-=blockedList.get(blockedTrack).entryTable.get(blockedList.get(blockedTrack).entryIndex).fourth;
					resourceStatus[blockedList.get(blockedTrack).entryTable.get(blockedList.get(blockedTrack).entryIndex).third-1]-=blockedList.get(blockedTrack).entryTable.get(blockedList.get(blockedTrack).entryIndex).fourth;
					blockedList.get(blockedTrack).entryIndex++;
					//go to next entry
					//System.out.println("In the cycle "+FIFOcycleCount+" "+(blockedList.get(blockedTrack).taskNum+1)+" task request granted in the blockedlist");	
					blockedList.get(blockedTrack).unblocked=true;
					blockedList.get(blockedTrack).blocked=false;
					blockedList.remove(blockedTrack);
					blockedTrack--;
					deadlockCount--;
					//get blocked status
					
				}else{
					blockedList.get(blockedTrack).blocked=true;
					blockedList.get(blockedTrack).waitingTime++;
					totalWaitingTime++;
					deadlockCount++;
					//System.out.println("In the cycle "+FIFOcycleCount+" "+(blockedList.get(blockedTrack).taskNum+1)+" task is still not available");
				}
				blockedTrack++;
				
			}
			for(int i=0;i<taskTable.length;i++){
				
				if(taskTable[i].unblocked||taskTable[i].terminated||taskTable[i].blocked){
					//System.out.println("In the cycle "+FIFOcycleCount+" "+(i+1)+" task is already terminated");
					if(taskTable[i].unblocked=true){
						taskTable[i].unblocked=false;
						taskTable[i].blocked=false;
					}
					
				}else{
					String activity=taskTable[i].entryTable.get(taskTable[i].entryIndex).activity;
					int delay=taskTable[i].entryTable.get(taskTable[i].entryIndex).delayed;
					int third=taskTable[i].entryTable.get(taskTable[i].entryIndex).third;
					int fourth=taskTable[i].entryTable.get(taskTable[i].entryIndex).fourth;
					
					if(delay!=0){
						taskTable[i].entryTable.get(taskTable[i].entryIndex).delayed--;
						//taskTable[i].waitingTime++;
						//totalWaitingTime++;
						//System.out.println("In the cycle "+FIFOcycleCount+" "+(taskTable[i].taskNum+1)+" task is waiting due to delay");
					}else{
						
						if(activity.equals("request")){
	
							if(resourceStatus[third-1]>=fourth){
								taskTable[i].curResourceStatus[third-1]+=fourth;
								//request granted and update the task resource status
								tempResourceStatus[third-1]-=fourth;
								resourceStatus[third-1]-=fourth;
								taskTable[i].entryIndex++;
								//go to next entry
								//System.out.println("In the cycle "+FIFOcycleCount+" "+(taskTable[i].taskNum+1)+" task request granted");
							}else{
								//wait 
								blockedList.add(taskTable[i]);
								deadlockCount++;
								//System.out.println(deadlockCount);
								taskTable[i].waitingTime++;
								totalWaitingTime++;
								//if deadlock count equals to the task numbers  deadlocked
								//System.out.println("In the cycle "+FIFOcycleCount+" "+(taskTable[i].taskNum+1)+" task is waiting due to not enough resource");
							}
							
						}else if(activity.equals("release")){
							taskTable[i].curResourceStatus[third-1]-=fourth;
							tempResourceStatus[third-1]+=fourth;
							taskTable[i].entryIndex++;
							//System.out.println("In the cycle "+FIFOcycleCount+" "+(taskTable[i].taskNum+1)+" task is released");

						}else if(activity.equals("terminate")){
							terminateCount++;
							taskTable[i].terminated=true;
							//deadlockCount++;
							taskNumTrack--;
							//when the count reaches to the task number, it means all the tasks are terminated
							//System.out.println("In the cycle "+(FIFOcycleCount-1)+" "+(i+1)+" task is just terminated in last cycle");
							taskTable[i].output="Task "+(taskTable[i].taskNum+1)+"     "+FIFOcycleCount+"   "+taskTable[i].waitingTime+"   "+((double)taskTable[i].waitingTime/FIFOcycleCount)*100+"%";
							totalTimeFIFO+=FIFOcycleCount;
							output1[taskTable[i].taskNum]=taskTable[i].output;
						}
					}
					}
				}
			resourceStatus=tempResourceStatus;
			
			if(taskNumTrack!=0&&deadlockCount==taskNumTrack){
				//deadlocked 
				boolean stilldeadlocked=true;
				while(stilldeadlocked){
					deadlockCount=0;
					//abort(taskTable number -1) 
					boolean x=true;
					int y=0;
					while(x){
						if(!taskTable[y].entryTable.get(taskTable[y].entryIndex).activity.equals("terminate")){
							x=false;
						}else{
							y++;
						}
					}
					for(int i=0;i<taskTable[y].curResourceStatus.length;i++){
						resourceStatus[i]+=taskTable[y].curResourceStatus[i];
						//give back the resource when aborted
					}
					//taskTable[y].output="Task "+(taskTable[y].taskNum+1)+"    aborted";
					output1[taskTable[y].taskNum]=taskTable[y].output;
					totalWaitingTime-=taskTable[y].waitingTime;
					blockedList.remove(taskTable[y]);
					//System.out.println("Task "+(taskTable[y].taskNum+1)+"     aborted");
					task[] taskTableTemp=new task[taskTable.length-1];
					for(int i=0;i<taskTableTemp.length;i++){
						if(i<y){
							taskTableTemp[i]=taskTable[i];
						}else{
							taskTableTemp[i]=taskTable[i+1];
						}
					}
					taskTable=taskTableTemp;
					taskNumTrack--;
					//System.out.println("In the cycle "+FIFOcycleCount+" "+" deadlocked and aborted one task");
					int teststilldeadlocked=0;
					for(int j=0;j<taskTable.length;j++){
							//taskTable[j].printEntryTable();
							if(taskTable[j].entryTable.get(taskTable[j].entryIndex).third!=0&&resourceStatus[taskTable[j].entryTable.get(taskTable[j].entryIndex).third-1]<taskTable[j].entryTable.get(taskTable[j].entryIndex).fourth){
								teststilldeadlocked++;
							}
					}
					if(teststilldeadlocked==taskNumTrack){
						stilldeadlocked=true;
					}else{
						stilldeadlocked=false;
					}
				}
			}
			
			FIFOcycleCount++;
			if(terminateCount==taskTable.length){
				FIFOcycle=false;
				FIFOcycleCount--;// all terminated so the last cycle doesn't count
			}
		}
		
		System.out.println("FIFO:");
		for(int i=0;i<output1.length;i++){
			System.out.println(output1[i]);
		}
		System.out.println("Total      "+totalTimeFIFO+"   "+totalWaitingTime+"   "+((double)totalWaitingTime/FIFOcycleCount)*100+"%");
		
		
		//*******************************Banker*****************************//
		System.out.println("   ");
		boolean BankerCycle=true;
		int bankerCycleCount=resourceNumber;
		int terminateCountBanker=0;
		int totalWaitingTimeBanker=0;
		int totalBankerTime=0;
		LinkedList<task> blockedBanker=new LinkedList<task>();
		String[] outputBanker=new String[taskTableBanker.length];
		
		//error check
		for(int i=0;i<taskTableBanker.length;i++){
			boolean claimExceed=false;
			for(int j=0;j<resourceNumber;j++){
				if(taskTableBanker[i].claim[j]>resourceStatusBanker[j]){
					claimExceed=true;
				}
			}
			// check if claim over resource
			if(claimExceed){
				terminateCountBanker++;
				task[] tempTable=new task[taskTableBanker.length-1];
				outputBanker[i]="Task "+(taskTableBanker[i].taskNum+1)+"       aborted";
				System.out.println("Task "+(taskTableBanker[i].taskNum+1)+" aborted due to claim more than resource has");
				for(int k=0;k<tempTable.length;k++){
					if(k<i){
						tempTable[k]=taskTableBanker[k];
					}else{
						tempTable[k]=taskTableBanker[k+1];
					}
				}
				taskTableBanker=tempTable;
			}
			
		}
		
		
		while(BankerCycle){
			
			int[] tempResourceStatus=new int[resourceNumber];

			for(int i=0;i<resourceStatus.length;i++){
				tempResourceStatus[i]=resourceStatusBanker[i];
				
			}
			
			//check blocked list first
			//System.out.println(" ");
			//System.out.println("******blocked list********");
			
			// first check block list
			int blockedTrack=0;
			while(blockedTrack<blockedBanker.size()&&(!blockedBanker.isEmpty())){
				int fourth=blockedBanker.get(blockedTrack).entryTable.get(blockedBanker.get(blockedTrack).entryIndex).fourth;
				int third=blockedBanker.get(blockedTrack).entryTable.get(blockedBanker.get(blockedTrack).entryIndex).third;
				
				//System.out.println("task 1 units: "+taskTableBanker[0].curResourceStatus[third-1]);
				//System.out.println("current resource available:" +resourceStatusBanker[0]);
				if(safeCheck(taskTableBanker,resourceStatusBanker,blockedBanker.get(blockedTrack),fourth,third)==1){
					//safe
					blockedBanker.get(blockedTrack).curResourceStatus[third-1]+=fourth;
					resourceStatusBanker[third-1]-=fourth;
					tempResourceStatus[third-1]-=fourth;
					blockedBanker.get(blockedTrack).entryIndex++;
					//System.out.println("Cycle "+bankerCycleCount+" Task "+(blockedBanker.get(blockedTrack).taskNum+1)+"'s request fulfilled (becomes safe) ");
					blockedBanker.get(blockedTrack).blocked=true;
					blockedBanker.remove(blockedTrack);
					blockedTrack--;
				}else{
					//not safe
					blockedBanker.get(blockedTrack).waitingTime++;
					totalWaitingTimeBanker++;
					//System.out.println("Cycle "+bankerCycleCount+" Task "+(blockedBanker.get(blockedTrack).taskNum+1)+"'s request still not safe ");
					blockedBanker.get(blockedTrack).blocked=true;
					//System.out.println("temp "+tempResourceStatus[third-1]);
					//System.out.println("cur "+ resourceStatusBanker[third-1]);
				}
				
				blockedTrack++;
			}
			//System.out.println("****** end of blocked list********");
			//System.out.println(" ");

			
			// iterate over each task
			for(int i=0;i<taskTableBanker.length;i++){
				if(taskTableBanker[i].blocked==true){
					if(blockedBanker.contains(taskTableBanker[i])){
						//System.out.println(taskTableBanker[i].blocked);
						//System.out.println(resourceStatusBanker[0]);

					}else{
						taskTableBanker[i].blocked=false;
						//System.out.println(taskTableBanker[i].blocked);
						//System.out.println(resourceStatusBanker[0]);

					}
					
				}else{
					//taskTableBanker[i].printEntryTable();
					//System.out.println(taskTableBanker[i].entryIndex);
					String activity=taskTableBanker[i].entryTable.get(taskTableBanker[i].entryIndex).activity;
					int delay=taskTableBanker[i].entryTable.get(taskTableBanker[i].entryIndex).delayed;
					int third=taskTableBanker[i].entryTable.get(taskTableBanker[i].entryIndex).third;
					int fourth=taskTableBanker[i].entryTable.get(taskTableBanker[i].entryIndex).fourth;
					
					if(delay!=0){
						taskTableBanker[i].entryTable.get(taskTableBanker[i].entryIndex).delayed--;
						//taskTableBanker[i].waitingTime++;
						//totalWaitingTimeBanker++;
					}else{
					
						if(activity.equals("request")){
							//System.out.println("request "+fourth);
							
							//check if request over claim
							if((fourth+taskTableBanker[i].curResourceStatus[third-1])>taskTableBanker[i].claim[third-1]){
								//request exceeds its claims
								
								task[] tempTable=new task[taskTableBanker.length-1];
								outputBanker[taskTableBanker[i].taskNum]="Task "+(taskTableBanker[i].taskNum+1)+"     aborted";
								//System.out.println("Task "+(taskTableBanker[i].taskNum+1)+" aborted due to request more than claim");
								terminateCountBanker++;
								tempResourceStatus[third-1]+=fourth;
								for(int k=0;k<tempTable.length;k++){
									if(k<i){
										tempTable[k]=taskTableBanker[k];
									}else{
										tempTable[k]=taskTableBanker[k+1];
									}
								}
								taskTableBanker=tempTable;
								
								
							}else{
								
							
								if(safeCheck(taskTableBanker,resourceStatusBanker,taskTableBanker[i],fourth,third)==1){
									//safe, request granted
									taskTableBanker[i].curResourceStatus[third-1]+=fourth;
									resourceStatusBanker[third-1]-=fourth;
									tempResourceStatus[third-1]-=fourth;
									taskTableBanker[i].entryIndex++;
								
								}else{
									//not safe go to blocked list 
									taskTableBanker[i].waitingTime++;
									totalWaitingTimeBanker++;
									blockedBanker.add(taskTableBanker[i]);
									//System.out.println("Cycle "+bankerCycleCount+"  Task "+(taskTableBanker[i].taskNum+1)+"'s request cannot be granted (state would not be safe)");
								}
							}
						}else if(activity.equals("release")){
							taskTableBanker[i].curResourceStatus[third-1]-=fourth;
							tempResourceStatus[third-1]+=fourth;
							taskTableBanker[i].entryIndex++;
							//System.out.println("Cycle "+bankerCycleCount+"  Task "+(taskTableBanker[i].taskNum+1)+"'s release");						
							
						}else if(activity.equals("terminate")){
							terminateCountBanker++;
						//	System.out.println("Cycle "+(bankerCycleCount-1)+"  Task "+(taskTableBanker[i].taskNum+1)+"'s terminates");
							String tempOut="Task "+(taskTableBanker[i].taskNum+1)+"     "+bankerCycleCount+"   "+taskTableBanker[i].waitingTime+"   "+((double)taskTableBanker[i].waitingTime/bankerCycleCount)*100+"%";
							outputBanker[taskTableBanker[i].taskNum]=tempOut;
							totalBankerTime+=bankerCycleCount;
							//remove it from the task table
							task[] tempTaskTable=new task[taskTableBanker.length-1];
							for(int g=0;g<tempTaskTable.length;g++){
								if(g<i){
									tempTaskTable[g]=taskTableBanker[g];
								}else{
									tempTaskTable[g]=taskTableBanker[g+1];
								}
								
							}
							taskTableBanker=tempTaskTable;
							i--;
						}
					}
				}
			}
			
			if(terminateCountBanker==taskN){
				BankerCycle=false;
			}
			//System.out.println(resourceStatusBanker[0]);
			resourceStatusBanker=tempResourceStatus;
			//System.out.println(resourceStatusBanker[0]);

			bankerCycleCount++;
		}
		
	
		System.out.println("Banker:");
		for(int i=0;i<outputBanker.length;i++){
			System.out.println(outputBanker[i]);
		}
		System.out.println("Total      "+totalBankerTime+"   "+totalWaitingTimeBanker+"   "+((double)totalWaitingTimeBanker/totalBankerTime)*100+"%");
		
		
		
		
		
		
		
		//test print entry
		 
		/* for(int j=0;j<taskTableBanker.length;j++){
			taskTableBanker[j].printEntryTable();
			
		}*/
		
		
		
		
		input1.close();
		
	}
	
	//to check if the state is safe
	public static int safeCheck(task[] process1,int[] resource1,task task,int request,int requestType){
		
		// copy each item to pretend make sure the original value unchanged 
		
		task[] process=new task[process1.length];
		process=process1;
		int[] resource=new int[resource1.length];
		for(int i=0;i<resource1.length;i++){
			resource[i]=resource1[i];
		}
		
		if(resource[requestType-1]==0){
			return 2;	
		}
		task.curResourceStatus[requestType-1]+=request;
		
		resource[requestType-1]-=request;
		//initiaition pretend request granted 
		
		boolean cycle=true;
		while(cycle&&process.length!=0){
			int proNum=0;
			boolean found=true;
			while(proNum<process.length&&found==true){
				int taskTerminated=0;
				for(int j=0;j<resource.length;j++){
					//System.out.println("task "+(process[proNum].taskNum+1)+" "+process[proNum].curResourceStatus[j]);
						if(resource[j]+process[proNum].curResourceStatus[j]>=process[proNum].claim[j]){
							taskTerminated++;
						}
				}
				if(taskTerminated==resource.length){
					//process can be terminated
					found=false;
					for(int j=0;j<resource.length;j++){
						resource[j]+=process[proNum].curResourceStatus[j];
				}
					proNum--;
				}
				proNum++;
			}
			
			if(found==false){
				
				if(process.length==1){
					//this is the last element that can be granted fully by banker
					task.curResourceStatus[requestType-1]-=request;
					return 1;
					//safe state
					
				}else{
				//pretend it terminates
					task[] tempProcess=new task[process.length-1];
					for(int k=0;k<tempProcess.length;k++){
						if(k<proNum){
							tempProcess[k]=process[k];
							
						}else{
							tempProcess[k]=process[k+1];
						}
						
					}
					process=tempProcess;
				
				}
			}else{
				task.curResourceStatus[requestType-1]-=request;
				//keep same 
				return 2;
				//un safe
			}
			
		}	
		return 3;
	}
	
	//to read over the file without format constraint
	public static String readFile(String path, Charset encoding) 
			  throws IOException 
			{
			  byte[] encoded = Files.readAllBytes(Paths.get(path));
			  return new String(encoded, encoding);
			}
	
}
