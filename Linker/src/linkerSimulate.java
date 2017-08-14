import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class linkerSimulate {
	//file use split method to format
	//iterate over each value   for( String x: Str.split(" "))
	//first pass
	//the first value a, create an array of length of a of modules
	//create tracking by the first number of each list, def list length x, then for loop x until all the value are stored in symbol table
	//create tracking by use list length x, track by ending with -1, store in uselist 
	//create tracking by program text length x, track by the capital letter, store in program text
	//two-pass
	//iterate over the array of module
	
	public static void main(String[] args)throws IOException{
		
		Scanner input1=new Scanner(System.in);
		System.out.println("TYPE THE FILE NAME OF THE INPUT");
		String fileName=input1.nextLine();
		String inputString=readFile(fileName,Charset.defaultCharset());
	//  System.out.println(inputString);
		input1.close();
		Scanner input2=new Scanner(inputString);
		int moduleNumber=input2.nextInt();
	//	System.out.println(moduleNumber);

		module[] moduleList=new module[moduleNumber];
		symbolTable table=new symbolTable();
		int baseAddress=0;
		int trackSymbolModuleNum=0;
		//pass one
		for(int i=0;i<moduleNumber;i++){
			int defListLength=input2.nextInt();
			for(int j=0;j<defListLength;j++){
				String variableName=input2.next();
				int relaad=input2.nextInt();
				boolean testMultipleTemp=false;
				for(int x=0;x<table.list.size();x++){
					if(variableName.equals(table.list.get(x).symbolString)){
						table.list.get(x).testIfMultipleDefined=true;
						testMultipleTemp=true;
					}
				}
				    if(!testMultipleTemp){
				    	//if this is a multiple definition, don't add it, but print error
				    	table.addEntry(variableName, relaad, baseAddress,i+1,relaad);
				    }
			}

			int useListLength=input2.nextInt();
			useList use=new useList();
			for(int k=0;k<useListLength;k++){
				String var=input2.next();
				ArrayList<Integer> arrlist = new ArrayList<Integer>();
				arrlist=met(arrlist,input2);
				//System.out.println(sumUseList);
				use.addEntry(var, arrlist);
				//use.printList();
			}
			int textLength=input2.nextInt();
			//System.out.println(textLength);
			int tempBaseAd=baseAddress;
			baseAddress+=textLength;
			programText program=new programText();
			for(int d=trackSymbolModuleNum;d<table.list.size();d++){
				trackSymbolModuleNum=table.list.size();
				table.list.get(d).moduleLength=textLength;
			}
			for(int l=0;l<textLength;l++){
				String var=input2.next();
				int x=input2.nextInt();
				program.addEntry(var, x);
			}
			//program.printList();
			moduleList[i]=new module(use,program,tempBaseAd,textLength);
			
			//moduleList[i].printModule();
			
		}
	
		System.out.println("Symbol Table:");
		table.printList();
		System.out.println(" ");
		System.out.println("Memory Map:");

		//pass two 
		for(int i=0;i<moduleNumber;i++){
			//moduleList[i].useModule.multipleVariableUsed();
			for(int j=0;j<moduleList[i].textModule.list.size();j++){
				boolean testAbExceeds=false;
				//iterate over each program text
				String op=moduleList[i].textModule.list.get(j).getTextSt();
				int val=moduleList[i].textModule.list.get(j).getTextInt();
				int outputInt=val;
				boolean testNotExist=false;
				boolean testReExceeds=false;
				boolean multipleVar=false;
				String tempTestNotExist=" ";
				if(op.equals("I")){
					outputInt=val;
				}else if(op.equals("A")){
					outputInt=val;
					if(outputInt%1000>=200){
						outputInt=(outputInt/1000)*1000;
						testAbExceeds=true;
					}
				}else if(op.equals("R")){
					outputInt=val+moduleList[i].moduleAd;
					if((val%1000)>moduleList[i].moduleLength){
						outputInt=(val/1000)*1000;
						testReExceeds=true;
					}
					//System.out.println(outputInt);
				}else if(op.equals("E")){	
					boolean test=false;
					for(int k=0;k<moduleList[i].useModule.list.size();k++){
						//iterate the length of use list
						for(int l=0;l<moduleList[i].useModule.list.get(k).arrlist.size();l++){
							//iterate over the use list of a specific element get(k)
							int index=moduleList[i].useModule.list.get(k).arrlist.get(l);
							if(j==index){
								test=true;
								//if found it equals to the value, then we found the external reference
								String temp=moduleList[i].useModule.list.get(k).useListString;
								if(moduleList[i].useModule.list.get(k).multipleVar){
									multipleVar=true;
								}
								int tempInt=0;
								while(!temp.equals(table.list.get(tempInt).symbolString)){
									//found the symbol string this value corresponds to 
									tempInt++;
									if(tempInt>=table.list.size()){
										testNotExist=true;
										outputInt=val/1000*1000;
										tempTestNotExist=moduleList[i].useModule.list.get(k).useListString;
										break;
									}
								}
								
								for(int m=k+1;m<moduleList[i].useModule.list.size();m++){
									for(int n=0;n<moduleList[i].useModule.list.get(m).arrlist.size();n++){
										if(moduleList[i].useModule.list.get(m).arrlist.get(n)==index){
											moduleList[i].useModule.list.get(m).multipleVar=true;
											multipleVar=true;
										}
									}
								}
								
								if(!testNotExist){
									outputInt=val/1000*1000+table.list.get(tempInt).symbolValue;
									table.list.get(tempInt).used=true;
								}
								//if value equals to the value we are going to search, then find the string and the corresponding symbol table and resolve it
								break;
							}
						}
						if(test){
							break;
						}
					}
				}
				
				int wholeAd=moduleList[i].moduleAd+j;
				System.out.print(wholeAd+":      "+outputInt);
				if(testAbExceeds){
					
					System.out.print(" Error: Absolute address exceeds machine size; zero used.");
				}
				if(testNotExist){
					System.out.print(" Error: "+ tempTestNotExist+"is not defined; zero used.");
					
				}
				if(testReExceeds){
					
					System.out.print(" Error: Relative address exceeds module size; zero used.");
				}
				if(multipleVar){
					System.out.print(" Error: Multiple variables used in instruction; all but first ignored.");
				}
				System.out.println(" ");
				
			}		
			moduleList[i].useModule.exceedModuleSize(moduleList[i].moduleLength, i+1);

		}
		
		for(int b=0;b<moduleNumber;b++){
			for(int c=0;c<moduleList[b].useModule.list.size();c++)
			if(moduleList[b].useModule.list.get(c).exceedModuleSize){
				System.out.println("Error: Use of " +moduleList[b].useModule.list.get(c).useListString+" in module " + b+" exceeds module size; use ignored.");
			}
		}
		
		//defined but not used
		for(int a=0;a<table.list.size();a++){
			if(!table.list.get(a).used){
				System.out.println("Warning: "+table.list.get(a).symbolString+" was defined in module " + table.list.get(a).moduleLoc+" but never used.");
			}
		}
		input2.close();
	}
	
	
	public static String readFile(String path, Charset encoding) 
			  throws IOException 
			{
			  byte[] encoded = Files.readAllBytes(Paths.get(path));
			  return new String(encoded, encoding);
			}
	
	public static ArrayList<Integer> met(ArrayList<Integer> arrlist, Scanner x){
		int newValue=x.nextInt();
		if(newValue!=(-1)){
			arrlist.add(newValue);
			arrlist=met(arrlist,x);
		}
		return arrlist;
	}

}


