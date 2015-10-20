package archandperfs1.task2;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import archandperfs1.BytehitrateWarmingCache;
import archandperfs1.Request;

public class Task2 {
	public static void main(String[] args) throws IOException {
		int size = Integer.parseInt(args[1]), x = Integer.parseInt(args[0]);

		BytehitrateWarmingCache lru = new LRUCache(size, x);
		BytehitrateWarmingCache lfu = new LFUCache(size, x);
		BytehitrateWarmingCache rlf = new RLFCache(size, x);
		

		Scanner in = new Scanner(System.in);
		while(in.hasNext()) {
			Request res = new Request(in.next(), in.nextInt());
			lru.process(res);
			lfu.process(res);
			rlf.process(res);
		}
		in.close();

		System.out.println(String.format("LRU Hit rate: %.2f", lru.hitRate()*100));
		System.out.println(String.format("LRU Byte hit rate: %.2f", lru.byteHitRate()*100));
		System.out.println(String.format("LFU Hit rate: %.2f", lfu.hitRate()*100));
		System.out.println(String.format("LFU Byte hit rate: %.2f", lfu.byteHitRate()*100));
		System.out.println(String.format("RLF Hit rate: %.2f", rlf.hitRate()*100));
		System.out.println(String.format("RLF Byte hit rate: %.2f", rlf.byteHitRate()*100));
		
		PrintWriter pr = new PrintWriter("cache_lfu.txt");
		pr.write(lfu.dump());
		pr.close();
		pr = new PrintWriter("cache_lru.txt");
		pr.write(lru.dump());
		pr.close();
		pr = new PrintWriter("cache_size-based.txt");
		pr.write(rlf.dump());
		pr.close();
	}
}
