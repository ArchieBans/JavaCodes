/* Aggregate.java
Archit Kumar - 06/10/2017
Read from a CSV file.
*/


import java.io.*;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.Set;
import java.util.Iterator;
import java.util.Map;
import java.lang.*;
import java.lang.Object;
import java.util.PriorityQueue;

public class Aggregate{

	
	public static void showUsage(){
		System.err.printf("Usage: java Aggregate <function> <aggregation column> <csv file> <group column 1> <group column 2> ...\n");
		System.err.printf("Where <function> is one of \"count\", \"sum\", \"avg\"\n");	
	}
	
	PriorityQueue<Integer> type = new PriorityQueue<Integer>();
	public static void main(String[] args){
		
		//At least four arguments are needed
		
		if (args.length < 4){
			showUsage();
			return;
		}
		String agg_function = args[0];
		String agg_column = args[1];
		String csv_filename = args[2];
		String[] group_columns = new String[args.length - 3];
		TreeMap<String,String> input1 = new  TreeMap<String,String>();
		
		
		for(int i = 3; i < args.length; i++)
			group_columns[i-3] = args[i];
		
		if (!agg_function.equals("count") && !agg_function.equals("sum") && !agg_function.equals("avg")){
			showUsage();
			return;
		}
		
		BufferedReader br = null;
		
		try{
			br = new BufferedReader(new FileReader(csv_filename));
		}catch( IOException e ){
			System.err.printf("Error: Unable to open file %s\n",csv_filename);
			return;
		}
		
		String header_line;
		try{
			header_line = br.readLine(); //The readLine method returns either the next line of the file or null (if the end of the file has been reached)
		} catch (IOException e){
			System.err.printf("Error reading file\n", csv_filename);
			return;
		}
		if (header_line == null){
			System.err.printf("Error: CSV file %s has no header row\n", csv_filename);
			return;
		}
		
		//Split the header_line string into an array of string values using a comma
		//as the separator.
		String[] column_names = header_line.split(",");
		int[] coun = new int[args.length-3];
		int numcols  = column_names.length; 
		int groupcolen = group_columns.length;
		int track=0;
		int track2=0;
		int aggcolpos=0;
		for(int i = 0; i < group_columns.length; i++)
		{
			
			for(int j=0;j<column_names.length;j++)
			{
				
				if(group_columns[i].equals(column_names[j]))
				{	
					
					coun[track] = j;
					track++;
				}
				if(column_names[j].equals(agg_column))
				{
					 aggcolpos = j;
					
				}
			}
		}
		for(int i=0;i<groupcolen;i++)
			{
				System.out.print(group_columns[i]+",");
			}
			
	
		if(agg_function.equals("count"))
		{
			
			System.out.println("count"+"("+args[1]+")");
			sum(csv_filename,agg_column,coun,numcols,groupcolen,aggcolpos,args[0]);
			
		}
		else if(agg_function.equals("sum"))
		{
			System.out.println("sum"+"("+args[1]+")");
			sum(csv_filename,agg_column,coun,numcols,groupcolen,aggcolpos,args[0]);
			
		}
		else if(agg_function.equals("avg"))
		{
			System.out.println("avg"+"("+args[1]+")");
			
			sum(csv_filename,agg_column,coun,numcols,groupcolen,aggcolpos,args[0]);
			
		}
		
	}

	public static void sum(String csvname,String column,int[] whichcol,int numcols,int groupcolen,int aggcolpos,String function)
	{
		TreeMap<String,Integer> data = new TreeMap<String,Integer>();
		TreeMap<String,Integer> dataavg = new TreeMap<String,Integer>();
		int linetrack=0;
		int foo = 0;
		int count=0;
		try{
		Scanner file  = new Scanner(csvname);
		File given = new File(file.nextLine());
		file = new Scanner(given);
		
		while(file.hasNextLine())
		{
			
			if(linetrack==0)
			{
				String firstskip = file.nextLine();
				linetrack++;
			}
			String[] sorted = new String[numcols];
			String key = null;
			String line = file.nextLine();
			sorted = line.split(",");
			int j=0;
			
				if(groupcolen>1) //Concatatnate the key.
				{
					j=0;
					key = keyGenerator(sorted,whichcol);
					if(data.containsKey(key))//Key check in Red Black Tree.
					{
						int value = Integer.parseInt(sorted[aggcolpos]);
						int one = data.get(key);
						int two = dataavg.get(key);
						dataavg.put(key,two+1);
						data.put(key,one+value);
						count++;
					}
					else
					{
						int value = Integer.parseInt(sorted[aggcolpos]);
						dataavg.put(key,1);
						data.put(key,value);
						j++;
					}
							
							
				}	
				else
				{
					
				for(int i =0;i<sorted.length;i++)
				{
					if(i==whichcol[0])
					{
						if(data.containsKey(sorted[i]))//Key check in Red Black Tree.
						{
							int value = Integer.parseInt(sorted[aggcolpos]);
							int one = data.get(sorted[i]);
							int two = dataavg.get(sorted[i]);
							dataavg.put(sorted[i],two+1);
							data.put(sorted[i],one+value);
							count++;
						
						}
						else
						{
							int value = Integer.parseInt(sorted[aggcolpos]);
							dataavg.put(sorted[i],1);
							data.put(sorted[i],value);
							j++;
						}
					}
				}
			
				}			
		}
		}
		catch(FileNotFoundException ex)
		{
			ex.printStackTrace();
		}
		
		if(function.equals("sum"))
		{
			Set set = data.entrySet();
			Iterator iterator = set.iterator();
			while(iterator.hasNext())
			{
				Map.Entry mentry = (Map.Entry)iterator.next();
				System.out.print(mentry.getKey() + ",");
				System.out.println(mentry.getValue());
			}
		}
		
		if(function.equals("count"))
		{
			Set set1 = dataavg.entrySet();
			Iterator iterator1 = set1.iterator();
			int[] countv = new int[10000];
			int temp = 0;
			while(iterator1.hasNext()) 
			{
				Map.Entry mentry1 = (Map.Entry)iterator1.next();
				countv[temp] =dataavg.get(mentry1.getKey());
				System.out.print(mentry1.getKey() + ",");
				System.out.println(mentry1.getValue());
				temp++;
			}
		}
		if(function.equals("avg"))
		{
			float avgvalue = 0;
			int temp = 0;
			Set set1 = dataavg.entrySet();
			Iterator iterator1 = set1.iterator();
			int[] countv = new int[10000];
			while(iterator1.hasNext()) 
			{
				Map.Entry mentry1 = (Map.Entry)iterator1.next();
				countv[temp] =dataavg.get(mentry1.getKey());
				temp++;
			}
			temp = 0;
			Set set = data.entrySet();
			Iterator iterator = set.iterator();
			while(iterator.hasNext()) 
			{
				Map.Entry mentry = (Map.Entry)iterator.next();
				avgvalue=data.get(mentry.getKey());
				avgvalue = avgvalue/countv[temp];
				System.out.print(mentry.getKey() + ",");
				System.out.println(avgvalue);
				temp++;
			}
		}		
	
	}
public static String keyGenerator(String[] sorted,int[] whichcol)
	{
			String key = null;
			int[] temp = new int[whichcol.length+1];
			int j=0;
			for(int i =0;i<whichcol.length;i++)
			{
				temp[i] = whichcol[i];
			}
			for(int k=0;k<sorted.length;k++)
			{
							
				if(k==temp[j])
					{
							
					if(key!=null)//Checking if not initialized null
					{
						key = key+","+sorted[k];
						
					}
					else
					{	
					key = sorted[k];
					
					}
					j++;
					}					
			}
			return key;
	}	
	
		
	
	

	
}  