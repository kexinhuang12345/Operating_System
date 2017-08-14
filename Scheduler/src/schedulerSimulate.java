import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

public class schedulerSimulate {
	public static void main(String[] args) throws IOException{
		String filename=null;
		if(args.length==2){
			filename=args[1];
		}if(args.length==1){
			filename=args[0];
		}
		String inputString=readFile(filename,Charset.defaultCharset());		
		Scanner input2=new Scanner(inputString);
		int processNum=input2.nextInt();
		process[] processes=new process[processNum];
		process[] processes1=new process[processNum];
		process[] processes2=new process[processNum];
		process[] processes3=new process[processNum];

		for(int i=0;i<processNum;i++){
			int A=input2.nextInt();
			int B=input2.nextInt();
			int C=input2.nextInt();
			int D=input2.nextInt();
			processes[i]=new process(A,B,C,D);
			processes[i].cpuTimeRecord=processes[i].cpuTime;
			processes[i].inputIndex=i;
			processes1[i]=new process(A,B,C,D);
			processes1[i].cpuTimeRecord=processes[i].cpuTime;
			processes1[i].inputIndex=i;
			processes2[i]=new process(A,B,C,D);
			processes2[i].cpuTimeRecord=processes[i].cpuTime;
			processes2[i].inputIndex=i;
			processes3[i]=new process(A,B,C,D);
			processes3[i].cpuTimeRecord=processes[i].cpuTime;
			processes3[i].inputIndex=i;
		}
		input2.close();
		

		String workingDir=System.getProperty("user.dir");
		workingDir=workingDir.concat("/random-numbers.txt");
		String inputString2=readFile(workingDir,Charset.defaultCharset());
		Scanner input3=new Scanner(inputString2);
		Scanner input4=new Scanner(inputString2);
		Scanner input5=new Scanner(inputString2);
		Scanner input6=new Scanner(inputString2);

		System.out.println("The original input was: "+processNum+" ");
		
		for(int i=0;i<processNum;i++){
			processes[i].printProcess();
			System.out.print("   ");
		}
		System.out.println("  ");
		System.out.println("The (sorted) input is: "+processNum+" ");
		for(int i=0;i<processNum;i++){
			process x=processes[i];
			int j=i-1;
			while(j>=0&&(processes[j].arriveTime>x.arriveTime)){
				processes[j+1]=processes[j];
				j=j-1;
			}
			processes[j+1]=x;
		}
		for(int i=0;i<processNum;i++){
			process x=processes1[i];
			int j=i-1;
			while(j>=0&&(processes1[j].arriveTime>x.arriveTime)){
				processes1[j+1]=processes1[j];
				j=j-1;
			}
			processes1[j+1]=x;
		}
		for(int i=0;i<processNum;i++){
			process x=processes2[i];
			int j=i-1;
			while(j>=0&&(processes2[j].arriveTime>x.arriveTime)){
				processes2[j+1]=processes2[j];
				j=j-1;
			}
			processes2[j+1]=x;
		}
		for(int i=0;i<processNum;i++){
			process x=processes3[i];
			int j=i-1;
			while(j>=0&&(processes3[j].arriveTime>x.arriveTime)){
				processes3[j+1]=processes3[j];
				j=j-1;
			}
			processes3[j+1]=x;
		}
		for(int i=0;i<processNum;i++){
			processes[i].printProcess();
			System.out.print("   ");
		}
		System.out.println(" ");
	
		
			//FCFS
			//FCFS Queue
			System.out.println("---------------------------------------------------------------");
			System.out.println("The scheduling algorithm used was First Come First Served");
			Queue<process> FCFSqueue=new LinkedList<process>();
			boolean FCFScycleEnd=true;
			int FCFScycleCount=0;
			process FCFSrunning=null;
			double FCFStotalCPU=0;
			double FCFStotalIO=0;
			
			while(FCFScycleEnd){
				//verbose 
				if(args.length==2)	{
					
					System.out.print("Before cycle "+FCFScycleCount+"     ");
				for(int k=0;k<processNum;k++){
					if(processes[k].state==1){
						System.out.print("unstarted: 0"+" ");
					}
					if(processes[k].state==2){
						System.out.print("ready: 0"+" ");
					}
					if(processes[k].state==3){
						System.out.print("running: "+processes[k].curCpuBurstRemain+" ");
					}
					if(processes[k].state==4){
						System.out.print("blocked: "+processes[k].curIOBurstRemain+" ");
					}
					if(processes[k].state==5){
						System.out.print("terminated: 0"+" ");
					}
				}
					System.out.println(" ");
				}
				//starting process gets in the queue
				boolean IOused=false;
				for(int i=0;i<processNum;i++){
					if(processes[i].curIOBurstRemain>0){
						IOused=true;
					}
				}
				if(IOused){
					FCFStotalIO++;
				}
				for(int i=0;i<processNum;i++){
					if(processes[i].state==2){
						processes[i].waitingTime++;
					}
					if(processes[i].arriveTime==FCFScycleCount){
						FCFSqueue.add(processes[i]);
						processes[i].state=2;
					}
					
					// terminate process
					if(processes[i].state==5){
						
						
					}
					
					//do blocked process
					if(processes[i].state==4){
						processes[i].curIOBurstRemain--;
						processes[i].IOtime++;
						if(processes[i].curIOBurstRemain==0){
							processes[i].state=2;
							FCFSqueue.add(processes[i]);
						}
						
					}
					
					//do running process
					if(processes[i].state==3){
						processes[i].curCpuBurstRemain--;
						processes[i].cpuTime--;
						FCFStotalCPU++;
						if(processes[i].cpuTime==0){
							processes[i].state=5;
							processes[i].finishingTime=FCFScycleCount;
							FCFSrunning=null;
						}else{
							if(processes[i].curCpuBurstRemain==0){
								processes[i].state=4;
								int curIoBurst=randomOS(processes[i].ioBurst,input3);
								processes[i].curIOBurstRemain=curIoBurst;
								FCFSrunning=null;
							}	
						}
					}
					
					
				}
				
				//the running process in last cycle goes to blocked 
				if(FCFSrunning==null){
					if(FCFSqueue.peek()!=null)	{
						FCFSrunning=FCFSqueue.remove();
						int curCpuBurst=randomOS(FCFSrunning.cpuBurst,input3);
						if(curCpuBurst>FCFSrunning.cpuTime){
							curCpuBurst=FCFSrunning.cpuTime;
						}
						FCFSrunning.curCpuBurstRemain=curCpuBurst;
						FCFSrunning.state=3;
					}else{
						int countTerminate=0;
						for(int j=0;j<processNum;j++){
							if(processes[j].state==5){
								countTerminate++;
							}
						}
						if(countTerminate==processNum){
							//all processes terminated
							FCFScycleEnd=false;
							break;
						}
					}
				

				}
				FCFScycleCount++;
			}
			
			double FCFSavgTurn=0;
			double FCFSavgWait=0;
			for(int l=0;l<processNum;l++){
				FCFSavgTurn+=(processes[l].finishingTime-processes[l].arriveTime);
				FCFSavgWait+=processes[l].waitingTime;
				System.out.println(" ");
				System.out.println("Process "+l+" :");
				System.out.println("(A,B,C,IO)=("+processes[l].arriveTime+","+processes[l].cpuBurst+","+processes[l].cpuTimeRecord+","+processes[l].ioBurst+")");
				System.out.println("Finishing Time: "+processes[l].finishingTime);
				System.out.println("Turnaround Time: "+(processes[l].finishingTime-processes[l].arriveTime));
				System.out.println("I/O Time: "+processes[l].IOtime);
				System.out.println("Waiting Time: "+processes[l].waitingTime);
				System.out.println(" ");
			}
			input3.close();
			FCFSavgTurn=FCFSavgTurn/processNum;
			FCFSavgWait=FCFSavgWait/processNum;
			System.out.println(" ");
			System.out.println("Summary Data : ");
			System.out.println("Finishing Time: "+FCFScycleCount);
			double cpuUti=FCFStotalCPU/FCFScycleCount;
			System.out.println("CPU utilization: "+cpuUti);
			double ioUti=FCFStotalIO/FCFScycleCount;
			System.out.println("I/O utilization : "+ioUti);
			double put=(double)processNum/FCFScycleCount*100;
			System.out.println("Throughput: "+put+" processes per hundred cycles");
			System.out.println("Average Turnaround Time: "+FCFSavgTurn);
			System.out.println("Average Waiting Time: "+FCFSavgWait);

			
		
			//RR q=2
			System.out.println("---------------------------------------------------------------");
			System.out.println("The scheduling algorithm used was Round Robbin");
			double RRtotalCPU=0;
			double RRtotalIO=0;
			Queue<process> RRqueue=new LinkedList<process>();
			boolean RRcycleEnd=true;
			int RRcycleCount=0;
			process RRrunning=null;

			
			while(RRcycleEnd){
				//verbose 
				if(args.length==2)	{

				System.out.print("Before cycle "+RRcycleCount+"     ");
				for(int k=0;k<processNum;k++){
					if(processes1[k].state==1){
						System.out.print("unstarted: 0"+" ");
					}
					if(processes1[k].state==2){
						System.out.print("ready: 0"+" ");
					}
					if(processes1[k].state==3){
						if(processes1[k].timer<processes1[k].curCpuBurstRemain){
							System.out.print("running: "+processes1[k].timer+" ");
						}else{
							System.out.print("running: "+processes1[k].curCpuBurstRemain+" ");
						}
					}
					if(processes1[k].state==4){
						System.out.print("blocked: "+processes1[k].curIOBurstRemain+" ");
					}
					if(processes1[k].state==5){
						System.out.print("terminated: 0"+" ");
					}
				}
				System.out.println(" ");
				}
				//starting process gets in the queue
				boolean IOused=false;
				for(int i=0;i<processNum;i++){
					
					if(processes1[i].curIOBurstRemain>0){
						IOused=true;
					}
				}
				
				if(IOused){
					RRtotalIO++;
				}
				
				for(int i=0;i<processNum;i++){
					if(processes1[i].state==2){
						processes1[i].waitingTime++;
					}
					if(processes1[i].arriveTime==RRcycleCount){
						RRqueue.add(processes1[i]);
						processes1[i].state=2;
					}
					
					// terminate process
					if(processes1[i].state==5){
						
						
					}
					
					//do blocked process
					if(processes1[i].state==4){
						processes1[i].curIOBurstRemain--;
						processes1[i].IOtime++;
						if(processes1[i].curIOBurstRemain==0){
							processes1[i].state=2;
							RRqueue.add(processes1[i]);
						}
						
					}
					
					//do running process
					if(processes1[i].state==3){
						processes1[i].curCpuBurstRemain--;
						processes1[i].cpuTime--;
						processes1[i].timer--;
						RRtotalCPU++;
						if(processes1[i].cpuTime==0){
							processes1[i].state=5;
							processes1[i].finishingTime=RRcycleCount;
							RRrunning=null;
						}else{
							if(processes1[i].curCpuBurstRemain==0){
								processes1[i].state=4;
								int curIoBurst=randomOS(processes1[i].ioBurst,input4);
								processes1[i].curIOBurstRemain=curIoBurst;
								RRrunning=null;
								processes1[i].timer=2;
								processes1[i].preempted=false;
							}
							if((processes1[i].curCpuBurstRemain>0)&&processes1[i].timer==0){
								processes1[i].state=2;
								RRqueue.add(processes1[i]);
								RRrunning=null;
								processes1[i].timer=2;
								processes1[i].preempted=true;
							}
						}
					}
					
					
				}
				
				//the running process in last cycle goes to blocked 
				if(RRrunning==null){
					if(RRqueue.peek()!=null)	{
						RRrunning=RRqueue.remove();
						if(RRrunning.preempted==false){
							int curCpuBurst=randomOS(RRrunning.cpuBurst,input4);
							if(curCpuBurst>RRrunning.cpuTime){
								curCpuBurst=RRrunning.cpuTime;
							}
							RRrunning.curCpuBurstRemain=curCpuBurst;
						}
						if(RRrunning.preempted==true){
							
							
							
						}
						RRrunning.state=3;
					}else{
						int countTerminate=0;
						for(int j=0;j<processNum;j++){
							if(processes1[j].state==5){
								countTerminate++;
							}
						}
						if(countTerminate==processNum){
							//all processes1 terminated
							RRcycleEnd=false;
							break;
						}
					}		
				
				}
				RRcycleCount++;
			}
			input4.close();
			double RRavgTurn=0;
			double RRavgWait=0;
			for(int l=0;l<processNum;l++){
				RRavgTurn+=(processes1[l].finishingTime-processes1[l].arriveTime);
				RRavgWait+=processes1[l].waitingTime;
				System.out.println(" ");
				System.out.println("Process "+l+" :");
				System.out.println("(A,B,C,IO)=("+processes1[l].arriveTime+","+processes1[l].cpuBurst+","+processes1[l].cpuTimeRecord+","+processes1[l].ioBurst+")");
				System.out.println("Finishing Time: "+processes1[l].finishingTime);
				System.out.println("Turnaround Time: "+(processes1[l].finishingTime-processes1[l].arriveTime));
				System.out.println("I/O Time: "+processes1[l].IOtime);
				System.out.println("Waiting Time: "+processes1[l].waitingTime);
				System.out.println(" ");
			}
			
			RRavgTurn=RRavgTurn/processNum;
			RRavgWait=RRavgWait/processNum;
			System.out.println(" ");
			System.out.println("Summary Data : ");
			System.out.println("Finishing Time: "+RRcycleCount);
			double RRcpuUti=RRtotalCPU/RRcycleCount;
			System.out.println("CPU utilization: "+RRcpuUti);
			double RRioUti=RRtotalIO/RRcycleCount;
			System.out.println("I/O utilization : "+RRioUti);
			double RRput=(double)processNum/RRcycleCount*100;
			System.out.println("Throughput: "+RRput+" processes per hundred cycles");
			System.out.println("Average Turnaround Time: "+RRavgTurn);
			System.out.println("Average Waiting Time: "+RRavgWait);

		
			//LCFS
			System.out.println("---------------------------------------------------------------");
			System.out.println("The scheduling algorithm used was LastCome First Served");
			Stack<process> LCFSstack=new Stack<process>();
			boolean LCFScycleEnd=true;
			int LCFScycleCount=0;
			process LCFSrunning=null;
			double LCFStotalCPU=0;
			double LCFStotalIO=0;
			
			while(LCFScycleEnd){
				//verbose 
				if(args.length==2)	{

				System.out.print("Before cycle "+LCFScycleCount+"     ");
				for(int k=0;k<processNum;k++){
					if(processes2[k].state==1){
						System.out.print("unstarted: 0"+" ");
					}
					if(processes2[k].state==2){
						System.out.print("ready: 0"+" ");
					}
					if(processes2[k].state==3){
						System.out.print("running: "+processes2[k].curCpuBurstRemain+" ");
					}
					if(processes2[k].state==4){
						System.out.print("blocked: "+processes2[k].curIOBurstRemain+" ");
					}
					if(processes2[k].state==5){
						System.out.print("terminated: 0"+" ");
					}
				}
				System.out.println(" ");
				}
				
				
				ArrayList<process> sameBlockedCheck=new ArrayList<process>();
				int blockedCount=0;
				for(int v=0;v<processNum;v++){
						if(processes2[v].curIOBurstRemain==1){
							blockedCount++;
							sameBlockedCheck.add(processes2[v]);
						}
					}
				
				if(blockedCount>1){
					for(int v=0;v<sameBlockedCheck.size();v++){
						sameBlockedCheck.get(v).noSameBlocked=false;
						process x=sameBlockedCheck.get(v);
						int u=v-1;
						while(u>=0&&x.arriveTime<sameBlockedCheck.get(u).arriveTime){
							process temp=sameBlockedCheck.get(u);
							sameBlockedCheck.set(u+1, temp);
							u=u-1;
						}
						sameBlockedCheck.set(u+1,x);
					}
					for(int v=0;v<sameBlockedCheck.size()-1;v++){
						if(sameBlockedCheck.get(v).arriveTime==sameBlockedCheck.get(v+1).arriveTime){
							if(sameBlockedCheck.get(v).inputIndex>sameBlockedCheck.get(v+1).inputIndex){
								process temp=sameBlockedCheck.get(v+1);
								process temp1=sameBlockedCheck.get(v);
								sameBlockedCheck.set(v,temp);
								sameBlockedCheck.set(v+1, temp1);
							}
						}
					}
					
					for(int v=sameBlockedCheck.size()-1;v>=0;v--){
						
						LCFSstack.push(sameBlockedCheck.get(v));
					}
					
				}
				
				boolean IOused=false;
				for(int j=processNum-1;j>=0;j--){
					if(processes2[j].arriveTime==LCFScycleCount){
							LCFSstack.push(processes2[j]);
							processes2[j].state=2;
						}	
					if(processes2[j].curIOBurstRemain>0){
							IOused=true;
						}
				}
				
				if(IOused){
					LCFStotalIO++;
				}
			
				//starting process gets in the queue
				for(int i=0;i<processNum;i++){

					if(processes2[i].state==2){
						processes2[i].waitingTime++;
					}
					
					// terminate process
					if(processes2[i].state==5){
						
						
					}
					
					//do blocked process
					if(processes2[i].state==4){
						processes2[i].curIOBurstRemain--;
						processes2[i].IOtime++;
						if(processes2[i].curIOBurstRemain==0){
							if(processes2[i].noSameBlocked==true){
								processes2[i].state=2;
								LCFSstack.push(processes2[i]);
							}else{
								processes2[i].noSameBlocked=true;	
								processes2[i].state=2;
							}
								
						
						}
						
					}
					
					//do running process
					if(processes2[i].state==3){
						processes2[i].curCpuBurstRemain--;
						processes2[i].cpuTime--;
						LCFStotalCPU++;
						if(processes2[i].cpuTime==0){
							processes2[i].state=5;
							processes2[i].finishingTime=LCFScycleCount;
							LCFSrunning=null;
						}else{
							if(processes2[i].curCpuBurstRemain==0){
								processes2[i].state=4;
								int curIoBurst=randomOS(processes2[i].ioBurst,input5);
								processes2[i].curIOBurstRemain=curIoBurst;
								LCFSrunning=null;
							}	
						}
					}
					
					
				}
				
				//the running process in last cycle goes to blocked 
				if(LCFSrunning==null){
					if(!LCFSstack.empty())	{
						LCFSrunning=LCFSstack.pop();
						int curCpuBurst=randomOS(LCFSrunning.cpuBurst,input5);
						if(curCpuBurst>LCFSrunning.cpuTime){
							curCpuBurst=LCFSrunning.cpuTime;
						}
						LCFSrunning.curCpuBurstRemain=curCpuBurst;
						LCFSrunning.state=3;
					}else{
						int countTerminate=0;
						for(int j=0;j<processNum;j++){
							if(processes2[j].state==5){
								countTerminate++;
							}
						}
						if(countTerminate==processNum){
							//all processes2 terminated
							LCFScycleEnd=false;
							break;
						}
					}
				}
				
				LCFScycleCount++;
			}
			input5.close();
			double LCFSavgTurn=0;
			double LCFSavgWait=0;
			for(int l=0;l<processNum;l++){
				LCFSavgTurn+=(processes2[l].finishingTime-processes2[l].arriveTime);
				processes2[l].waitingTime--;
				LCFSavgWait+=processes2[l].waitingTime;
				System.out.println(" ");
				System.out.println("Process "+l+" :");
				System.out.println("(A,B,C,IO)=("+processes2[l].arriveTime+","+processes2[l].cpuBurst+","+processes2[l].cpuTimeRecord+","+processes2[l].ioBurst+")");
				System.out.println("Finishing Time: "+processes2[l].finishingTime);
				System.out.println("Turnaround Time: "+(processes2[l].finishingTime-processes2[l].arriveTime));
				System.out.println("I/O Time: "+processes2[l].IOtime);
				System.out.println("Waiting Time: "+processes2[l].waitingTime);
				System.out.println(" ");
			}
			
			LCFSavgTurn=LCFSavgTurn/processNum;
			LCFSavgWait=LCFSavgWait/processNum;
			System.out.println(" ");
			System.out.println("Summary Data : ");
			System.out.println("Finishing Time: "+LCFScycleCount);
			double LCFScpuUti=LCFStotalCPU/LCFScycleCount;
			System.out.println("CPU utilization: "+LCFScpuUti);
			double LCFSioUti=LCFStotalIO/LCFScycleCount;
			System.out.println("I/O utilization : "+LCFSioUti);
			double LCFSput=(double)processNum/LCFScycleCount*100;
			System.out.println("Throughput: "+LCFSput+" processes per hundred cycles");
			System.out.println("Average Turnaround Time: "+LCFSavgTurn);
			System.out.println("Average Waiting Time: "+LCFSavgWait);
			
			
			//PSJF
			System.out.println("---------------------------------------------------------------");
			System.out.println("The scheduling algorithm used was Preemptive Shortest Job First");
			ArrayList<process> PSJFlist=new ArrayList<process>();
			boolean PSJFcycleEnd=true;
			int PSJFcycleCount=0;
			process PSJFrunning=null;
			double PSJFtotalCPU=0;
			double PSJFtotalIO=0;
			while(PSJFcycleEnd){
				//verbose 
				if(args.length==2)	{

				System.out.print("Before cycle "+PSJFcycleCount+"     ");
				for(int k=0;k<processNum;k++){
					if(processes3[k].state==1){
						System.out.print("unstarted: 0"+" ");
					}
					if(processes3[k].state==2){
						System.out.print("ready: 0"+" ");
					}
					if(processes3[k].state==3){
						System.out.print("running: "+processes3[k].curCpuBurstRemain+" ");
					}
					if(processes3[k].state==4){
						System.out.print("blocked: "+processes3[k].curIOBurstRemain+" ");
					}
					if(processes3[k].state==5){
						System.out.print("terminated: 0"+" ");
					}
				}
				System.out.println(" ");
				}
				boolean IOused=false;
				for(int j=processNum-1;j>=0;j--){
					if(processes3[j].arriveTime==PSJFcycleCount){
						processes3[j].state=2;
						if(PSJFlist.isEmpty()){
							PSJFlist.add(processes3[j]);
						}else{			
								PSJFlist.add(processes3[j]);
								sortList(PSJFlist,processes3[j]);
							}
						}
					if(processes3[j].curIOBurstRemain>0){
						IOused=true;
					}
						
				}
				if(IOused){
					PSJFtotalIO++;

				}
				//starting process gets in the queue
				for(int i=0;i<processNum;i++){
					
					if(processes3[i].state==2){
						processes3[i].waitingTime++;
					}
					
					// terminate process
					if(processes3[i].state==5){
						
						
					}
					
					//do blocked process
					if(processes3[i].state==4){
						
						processes3[i].curIOBurstRemain--;
						processes3[i].IOtime++;
						if(processes3[i].curIOBurstRemain==0){
							processes3[i].state=2;
							if(PSJFlist.isEmpty()){
								PSJFlist.add(processes3[i]);
							}else{
								PSJFlist.add(processes3[i]);
								sortList(PSJFlist,processes3[i]);
							}
						}
					}
					//do running process
						if(processes3[i].state==3){
							processes3[i].curCpuBurstRemain--;
							processes3[i].cpuTime--;
							PSJFtotalCPU++;
							if(processes3[i].cpuTime==0){
								processes3[i].state=5;
								processes3[i].finishingTime=PSJFcycleCount;
								PSJFrunning=null;
							}else{
								if(processes3[i].curCpuBurstRemain==0){
									processes3[i].preempted=false;
									processes3[i].state=4;
									int curIoBurst=randomOS(processes3[i].ioBurst,input6);
									processes3[i].curIOBurstRemain=curIoBurst;
									//System.out.println(processes3[i].curIOBurstRemain+" "+curIoBurst);
									PSJFrunning=null;
								}	
							}
							
							
						}
				
					}
				
				if(PSJFrunning!=null&&!PSJFlist.isEmpty()){
					ArrayList<process> checkPreempted=new ArrayList<process>();
					for(int i=0;i<PSJFlist.size();i++){
						checkPreempted.add(PSJFlist.get(i));
					}
					checkPreempted.add(PSJFrunning);
					sortList(checkPreempted,PSJFrunning);
					if(checkPreempted.get(checkPreempted.size()-1)!=PSJFrunning){
						process temp=PSJFrunning;
						PSJFrunning=checkPreempted.get(checkPreempted.size()-1);
						if(PSJFrunning.preempted==false){	
							int curCpuBurst=randomOS(PSJFrunning.cpuBurst,input6);
							if(curCpuBurst>PSJFrunning.cpuTime){
								curCpuBurst=PSJFrunning.cpuTime;
							}
							PSJFrunning.curCpuBurstRemain=curCpuBurst;
						}
						PSJFrunning.state=3;					
						temp.state=2;
						temp.preempted=true;
						PSJFlist.remove(PSJFrunning);
						if(PSJFlist.isEmpty()){
							PSJFlist.add(temp);
						}else{
							PSJFlist.add(temp);
							sortList(PSJFlist,temp);
						}
					}
				}
				
				//the running process in last cycle goes to blocked 
				if(PSJFrunning==null){
					if(!PSJFlist.isEmpty())	{
						PSJFrunning=PSJFlist.remove(PSJFlist.size()-1);		
						if(PSJFrunning.preempted==false){

							int curCpuBurst=randomOS(PSJFrunning.cpuBurst,input6);
							if(curCpuBurst>PSJFrunning.cpuTime){
								curCpuBurst=PSJFrunning.cpuTime;
							}
							PSJFrunning.curCpuBurstRemain=curCpuBurst;
						}
						PSJFrunning.state=3;
					}else{
						int countTerminate=0;
						for(int j=0;j<processNum;j++){
							if(processes3[j].state==5){
								countTerminate++;
							}
						}
						if(countTerminate==processNum){
							//all processes3 terminated
							PSJFcycleEnd=false;
							break;
						}
					}
				}
				
				
				PSJFcycleCount++;
			}
			input6.close();
			double PSJFavgTurn=0;
			double PSJFavgWait=0;
			for(int l=0;l<processNum;l++){
				PSJFavgTurn+=(processes3[l].finishingTime-processes3[l].arriveTime);
				processes3[l].waitingTime--;
				PSJFavgWait+=processes3[l].waitingTime;
				System.out.println(" ");
				System.out.println("Process "+l+" :");
				System.out.println("(A,B,C,IO)=("+processes3[l].arriveTime+","+processes3[l].cpuBurst+","+processes3[l].cpuTimeRecord+","+processes3[l].ioBurst+")");
				System.out.println("Finishing Time: "+processes3[l].finishingTime);
				System.out.println("Turnaround Time: "+(processes3[l].finishingTime-processes3[l].arriveTime));
				System.out.println("I/O Time: "+processes3[l].IOtime);
				System.out.println("Waiting Time: "+processes3[l].waitingTime);
				System.out.println(" ");
			}
			
			PSJFavgTurn=PSJFavgTurn/processNum;
			PSJFavgWait=PSJFavgWait/processNum;
			System.out.println(" ");
			System.out.println("Summary Data : ");
			System.out.println("Finishing Time: "+PSJFcycleCount);
			double PSJFcpuUti=PSJFtotalCPU/PSJFcycleCount;
			System.out.println("CPU utilization: "+PSJFcpuUti);
			double PSJFioUti=PSJFtotalIO/PSJFcycleCount;
			System.out.println("I/O utilization : "+PSJFioUti);
			double PSJFput=(double)processNum/PSJFcycleCount*100;
			System.out.println("Throughput: "+PSJFput+" processes per hundred cycles");
			System.out.println("Average Turnaround Time: "+PSJFavgTurn);
			System.out.println("Average Waiting Time: "+PSJFavgWait);
		
	}
	
	public static int randomOS(int U,Scanner input3) throws IOException{
		int x=0;
		if(input3.hasNext()){
			x=input3.nextInt();
		}
		x=1+x%U;
		return x;
		
	}
	
	public static String readFile(String path, Charset encoding) 
			  throws IOException 
			{
			  byte[] encoded = Files.readAllBytes(Paths.get(path));
			  return new String(encoded, encoding);
			}
	
	public static void swap(int A, int B, process[] C){
		process temp=C[A];
		C[A]=C[B];
		C[B]=temp;
		
	}
	
	public static void sortList(ArrayList<process> list, process A){
			if(A.cpuTime==list.get(list.size()-2).cpuTime){
				if(A.arriveTime>list.get(list.size()-2).arriveTime){
					process temp=list.get(list.size()-2);
					list.set(list.size()-2, A);
					list.set(list.size()-1, temp);
				}else if(A.arriveTime==list.get(list.size()-2).arriveTime){
					if(A.inputIndex>list.get(list.size()-2).inputIndex){
						process temp=list.get(list.size()-2);
						list.set(list.size()-2, A);
						list.set(list.size()-1, temp);
					}
				}
			}else if(A.cpuTime<list.get(list.size()-2).cpuTime){
				
			}else{
				process x=list.get(list.size()-1);
				int j=list.size()-2;
				while(j>=0&&list.get(j).cpuTime<x.cpuTime){
					process temp=list.get(j);
					list.set(j+1, temp);	
					j=j-1;
				}
				list.set(j+1, x);
			}
	}
	
}
