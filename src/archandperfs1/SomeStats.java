package archandperfs1;

import java.util.HashMap;
import java.util.Scanner;

public class SomeStats {
	public static void main(String[] args) {
		long sizes = 0;
		long n = 0;
		Scanner s = new Scanner(System.in);
		HashMap<String, Integer> m = new HashMap<>();
		int conflicts = 0, redundant = 0;
		while(s.hasNext()) {
			String url = s.next();
			int size = s.nextInt();
			Integer res = m.get(url);
			if(res != null)
				if(res.intValue() != size) {
					conflicts++;
					m.put(url, size);
				}
				else
					redundant++;
			else
				m.put(url, size);
			
			sizes += size;
			n += 1;
		}
		s.close();
		System.out.println(m.size()+" different files, "+conflicts+" conflicts, "+redundant+" redundant requests");
		System.out.println("average size: " + sizes/n);
	}
}
