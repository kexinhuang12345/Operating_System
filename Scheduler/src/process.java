
public class process {
	int arriveTime;
	int cpuBurst;
	int cpuTime;
	int ioBurst;
	
	int curCpuBurstRemain=0;
	int curIOBurstRemain=0;
	
	int state=1;
	// 1 means unstarted, 2 means ready, 3 means running, 4 means blocked, 5 terminated
	int IOtime=0;
	int waitingTime=0;
	int finishingTime=0;
	int cpuTimeRecord=0;
	
	int inputIndex=0;
	
	int timer=2;
	boolean preempted=false;
	boolean noSameBlocked=true;
	public process (int A,int B,int C,int D){
		arriveTime=A;
		cpuBurst=B;
		cpuTime=C;
		ioBurst=D;
	}
	
	
	public void printProcess(){
		
		System.out.print(arriveTime+" ");
		System.out.print(cpuBurst+" ");
		System.out.print(cpuTime+" ");
		System.out.print(ioBurst+" ");

		
	}
}
