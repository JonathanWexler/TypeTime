/*
 Jonathan Wexler
 3/22/13
 wexman@brandeis.edu
 */

package dictionary;

// imports for array lists
import java.io.*;
import java.util.*;

//class called Words to compare distance between words
public class Words {
	
	// main client to run all methods
	public static void main (String [] args)throws FileNotFoundException {
		
		LinkedList<String> diction = dictionary();
		
		Map<String, LinkedList<String>> map = mapping(diction);	
		
		
		while (true) {
			System.out.println("What words would you like to trace (separate by space)?");
			Scanner words = new Scanner(System.in);
			String w1 = words.next();
			String w2 = words.next();
			if (!diction.contains(w1) || !diction.contains(w2)) {

				System.out
						.println("Sorry, one or both of your words do not exist in the dictionary.");
				break;
			} else if (w1.length() != w2.length()) {

				System.out.println("Sorry, No Solution.");

			}
			
			if(w1.charAt(0)<w2.charAt(0)){
				System.out.println("w1 is before w2");
			}
			path(w1, w2, map);
		}

	}
	
	// makes the dictionary into a list
	public static LinkedList<String> dictionary() throws FileNotFoundException{

		Scanner ask = new Scanner(System.in);
		
		System.out.println("What text file would you like to use (dict.txt or test.txt)?");
				
		Scanner read = new Scanner (new File (ask.next()));
		
		LinkedList<String> diction = new LinkedList<String>();
		
			while(read.hasNext()){
				
				diction.add(read.next());
				
			}
			//System.out.println("DONE  " + diction.get(1)); ///////	///////	///////	///////	///////	///////	///////	///////	///////	///////	///////	///////	
			return diction;
	}
	
	//maps words in dictionary
	public static Map <String, LinkedList<String>> mapping(LinkedList<String> diction){
		
		Map<String,LinkedList<String>> temp = new HashMap<String,LinkedList<String>>();
		
		LinkedList <String> mapList;
		
		Iterator<String> itr = diction.iterator();
		Iterator<String> itr2;
		
		while (itr.hasNext()){
			mapList = new LinkedList <String>();
			itr2 = diction.iterator();
			String outer = itr.next();
			
			while(itr2.hasNext()){
				
				String inner = itr2.next();
				
				if (compareWord (outer, inner)){
					
					mapList.add(inner);
							
				}
				
			}
			temp.put(outer, mapList);		
		}

			
		return temp;
		
	}
	
	
	
	// boolean to compare words
	public static boolean compareWord(String word, String mapped){
		
		int count = 0;
		
		if (word.length() != mapped.length()){
			
			return false;
		
		}else{
			for (int i = 0; i< word.length(); i++){
					
				if (word.charAt(i) != mapped.charAt(i) ){
						
						count ++;	
				}else if(count > 1 ){
					
					return false;
				}
				
			}
			
			if(count > 1){
				return false;
			}
			
			return true;
		}	
		
	}
	
	//creates final path
	public static void pathFinder (String w1, String w2, Map<String,String> path){
		
		LinkedList<String> printList = new LinkedList<String>();
		
		boolean complete =false;
		
		String temp = w2;
	
		while(!complete){
			
			
			if (temp.equals(w1)){
				
				complete = true;
			}
			

			printList.addFirst(temp + ", ");
			temp = path.get(temp);
				
		}
		
		for(String s: printList ){
			
			System.out.print(s);
			
		}
		
		System.out.println ("");
		System.out.println ("Edit Distance = " + (printList.size() - 1));
		
	}
	
	// final method
	public static void path(String w1, String w2, Map<String, LinkedList<String>>  neighbors){
		

		Map<String, String>  path = new HashMap<String, String>();
		
		LinkedList<String> lastList = new LinkedList<String>();
		LinkedList<String> checkedList = new LinkedList<String>();
		
		
		lastList.add(w1);		
		
	       while (!lastList.isEmpty()) {
	    	   
	            String next = lastList.removeFirst();
	            
	            if (next.equals(w2)) {
	                
	            	pathFinder(w1, w2, path);
	            
	                
	            } else {
	            	
	            	checkedList.add(next);
	            	
	                for (String checker : neighbors.get(next)) {
	                	
	                    if (!checkedList.contains(checker)) {
	                    	
	                    	checkedList.add(checker);
	                    	
	                        lastList.add(checker);
	                        
	                        path.put(checker, next);

	                    
	                   }
	                }
	              
	            }
	        }
		
	}

}
