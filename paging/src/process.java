
public class process {
	double A;
	double B;
	double C;
	double D;
	int time=1;
	int curWord=-1;
	boolean terminated=false;
	int faultCount=0;
	int curPageBegin=0;
	double sumResidency=0;
	double evictNum=0;
	public process(double a, double b, double c,double d){
		A=a;
		B=b;
		C=c;	
		D=d;
	}
	
}
