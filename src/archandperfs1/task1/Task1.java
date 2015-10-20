package archandperfs1.task1;

import java.io.IOException;
import java.util.Scanner;

import archandperfs1.BytehitrateWarmingCache;
import archandperfs1.Request;

public class Task1 {
	public static void main(String[] args) throws IOException {
		int n = Integer.parseInt(args[1]), x = Integer.parseInt(args[0]);

		BytehitrateWarmingCache lfu = new LFUCache(n, x);
		BytehitrateWarmingCache lru = new LRUCache(n, x);
		
		Scanner in = new Scanner(System.in);
		
		while(in.hasNext()) {
			Request req = new Request(in.next(), in.nextInt());
			lfu.process(req);
			lru.process(req);
		}
		in.close();
		
		System.out.println(String.format("LRU hitrate: %.2f", lru.hitRate()*100));
		System.out.println(String.format("LFU hitrate: %.2f", lfu.hitRate()*100));
	}
}
