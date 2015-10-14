package archandperfs1.task1;

import java.io.IOException;
import java.util.Scanner;

import archandperfs1.Request;

public class Task1 {
	public static void main(String[] args) throws IOException {
		LRUCache c = new LRUCache(1000, 0);
		
		Scanner in = new Scanner(System.in);
		while(in.hasNext()) {
			c.process(new Request(in.next(), in.nextInt()));
		}
		in.close();
		
		System.out.println(c.hitRate());
		System.out.println(c.byteHitRate());
	}
}
