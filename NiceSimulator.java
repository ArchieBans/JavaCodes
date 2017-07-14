/* NiceSimulator.java
   CSC 225 - Summer 2017
	Archit Kumar - 06/20/2017
	V00875990.
*/


import java.io.*;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.Set;
import java.util.Iterator;
import java.util.Map;
import java.lang.*;

public class NiceSimulator{

	public static final int SIMULATE_IDLE = -2;
	public static final int SIMULATE_NONE_FINISHED = -1;
	public static int maxtask = 0 ;
	TreeMap<Integer,Integer> data = new TreeMap<Integer,Integer>();
	TreeMap<Integer,Integer> data2 = new TreeMap<Integer,Integer>();
	int[] array;
	int[][] array2;
	
	
	/* Constructor(maxTasks)
	   Instantiate the data structure with the provided maximum 
	   number of tasks. No more than maxTasks different tasks will
	   be simultaneously added to the simulator, and additionally
	   you may assume that all task IDs will be in the range
	     0, 1, ..., maxTasks - 1
	*/
	public NiceSimulator(int maxTasks){
	this.maxtask = maxTasks;
	array = new int[maxTasks];
	array2 = new int[maxTasks][2];
	}
	
	/* taskValid(taskID)
	   Given a task ID, return true if the ID is currently
	   in use by a valid task (i.e. a task with at least 1
	   unit of time remaining) and false otherwise.
	   
	   Note that you should include logic to check whether 
	   the ID is outside the valid range 0, 1, ..., maxTasks - 1
	   of task indices.
	
	*/
	public boolean taskValid(int taskID){		//this O(1) because array indexing is constant time.
		
		if(taskID>maxtask)
			return false;	
		if(data.isEmpty())
			return false;
		if(array[taskID]>=1)
		{
		return true;
		}
		else 
			return false;
	
	}
	
	/* getPriority(taskID)
	   Return the current priority value for the provided
	   task ID. You may assume that the task ID provided
	   is valid.
	
	*/
	public int getPriority(int taskID){		//this O(1) because array indexing is constant time.
		return array2[taskID][1];
	}
	
	/* getRemaining(taskID)
	   Given a task ID, return the number of timesteps
	   remaining before the task completes. You may assume
	   that the task ID provided is valid.
	
	*/
	public int getRemaining(int taskID){		//this O(1) because array indexing is constant time.
		return array[taskID];
	}
	
	
	/* add(taskID, time_required)
	   Add a task with the provided task ID and time requirement
	   to the system. You may assume that the provided task ID is in
	   the correct range and is not a currently-active task.
	   The new task will be assigned nice level 0.
	*/
	public void add(int taskID, int time_required){		//Java Treemap add operation is worst case O(logn)
		int defaultnice = 0;
		data.put(taskID,time_required);
		data2.put(taskID,defaultnice);
		array[taskID] = time_required;
	

	}
	
	
	/* kill(taskID)
	   Delete the task with the provided task ID from the system.
	   You may assume that the provided task ID is in the correct
	   range and is a currently-active task.
	*/
	public void kill(int taskID){	//Java Treemap remove operation is worst case O(logn)
		data.remove(taskID);
		data2.remove(taskID);
		array[taskID] = 0;

	}
	
	
	/* renice(taskID, new_priority)
	   Change the priority of the the provided task ID to the new priority
       value provided. The change must take effect at the next simulate() step.
	   You may assume that the provided task ID is in the correct
	   range and is a currently-active task.
	
	*/
	public void renice(int taskID, int new_priority){		//Java Treemap find operation is worst case O(logn)
		data2.put(taskID,new_priority);
		array2[taskID][1] = new_priority;
		array2[taskID][0] = taskID;

	}

	
	/* simulate()
	   Run one step of the simulation:
		 - If no tasks are left in the system, the CPU is idle, so return
		   the value SIMULATE_IDLE.
		 - Identify the next task to run based on the criteria given in the
		   specification (tasks with the lowest priority value are ranked first,
		   and if multiple tasks have the lowest priority value, choose the 
		   task with the lowest task ID).
		 - Subtract one from the chosen task's time requirement (since it is
		   being run for one step). If the task now requires 0 units of time,
		   it has finished, so remove it from the system and return its task ID.
		 - If the task did not finish, return SIMULATE_NONE_FINISHED.
	*/
	public int simulate(){
		if(data.isEmpty())
			return SIMULATE_IDLE;
		
		int smallestv = 0;
		int smallestk = 0;
		smallestk = data2.firstKey();
		smallestv = data2.get(smallestk);
		for(Map.Entry<Integer,Integer> entry : data2.entrySet()) 
		{
			Integer key = entry.getKey();
			Integer value = entry.getValue();
			if(value<smallestv)
			{	
				smallestk = key;
				smallestv = value;
			}
			if(value==smallestv)
			{
				if(key<smallestk)
				{
					smallestk = key;
				}
			}
		}
		
	int timeremain = data.get(smallestk);
	if(timeremain>0)
	{	
		timeremain--;
		if(timeremain==0)
	{
		data.remove(smallestk);
		data2.remove(smallestk);
		array[smallestk] = 0;
			return smallestk;
	}
		data.put(smallestk,timeremain);
		array[smallestk] = timeremain;
	}
	
		return -1;
	}


}