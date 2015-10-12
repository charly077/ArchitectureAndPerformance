import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * 
 * @author Istasse Maxime & Jacquet Charles
 * 
 * LINGI 2241 : Architecture and Performance of Computer Systems
 * Assignment 1, Task 1,1 
 *
 */


public class CacheManagement { 
	public static int  X=0,N=0;
	public static String CacheLRU[];
	
	/**
	 * 
	 * @param args : the first param must be X, the number of the request where we begin to compute de hit rate
	 * 				 the second param must be N, the size of cache
	 */
	public static void main(String[] args) {
		// take care of arguments
		try {
			X=Integer.parseInt(args[0]);
			N=Integer.parseInt(args[1]);
		} catch(NumberFormatException e) {
			System.out.println("Arguments are needed (X and N) and must be integers");
			System.exit(0);
		}
		
		//We have to open the file
		//Send Line by Line the file to the cache simulation
		try( BufferedReader br = new BufferedReader(new FileReader("trace.txt"))) {
			LRUCache lru = new LRUCache(N); //create the LRU cache
			String line="";
			while((line = br.readLine()) != null){
				String tab[] = line.split(" ");
				lru.get(Elem, size); // ici, on ne renvoie rien parce qu'en réalité on ne fait rien avec --'
			}
		} catch (IOException e) {
			System.out.println("File not found!");
		}
		
		
	}
	

}
