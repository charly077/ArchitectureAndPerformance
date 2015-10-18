package archandperfs1.task2;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import archandperfs1.BytehitrateWarmingCache;
import archandperfs1.Request;

public class Task2Curves {



	public static void main(String[] args) throws FileNotFoundException {
		PrintWriter prlfu = new PrintWriter("result_lfu_task2.txt");
		PrintWriter prlru = new PrintWriter("result_lru_task2.txt");
		PrintWriter prrlf = new PrintWriter("result_rlf_task2.txt");
		
		for (int size = 0; size <= 500000; size += 1000){
			System.out.println(size +" "+ (size < Integer.MAX_VALUE));
			int x = size;
			BytehitrateWarmingCache lru = new LRUCache(size, x);
			BytehitrateWarmingCache lfu = new LFUCache(size, x);
			BytehitrateWarmingCache rlf = new RLFCache(size, x);
			
			FileReader fr;
			
			try{
			fr = new FileReader ("trace.txt"); 
			Scanner in = new Scanner(fr);
			while(in.hasNext()) {

				Request res = new Request(in.next(), in.nextInt());
				lru.process(res);
				lfu.process(res);
				rlf.process(res);
			}
			in.close();
			fr.close();
			
			prlru.write(String.format("%d %.1f %.1f\n", size, lru.hitRate()*100, lru.byteHitRate()*100));
			prlru.flush();
			prlfu.write(String.format("%d %.1f %.1f\n", size, lfu.hitRate()*100, lfu.byteHitRate()*100));
			prlfu.flush();
			prrlf.write(String.format("%d %.1f %.1f\n", size, rlf.hitRate()*100, rlf.byteHitRate()*100));
			prrlf.flush();
			
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		prlfu.close();
		prlru.close();
		prrlf.close();
		System.out.println("End");
	}


}