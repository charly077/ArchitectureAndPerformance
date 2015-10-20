package archandperfs1;

import java.util.Scanner;

public class AverageSize {
	public static void main(String[] args) {
		long sizes = 0;
		long n = 0;
		Scanner s = new Scanner(System.in);
		while(s.hasNext()) {
			s.next();
			sizes += s.nextInt();
			n += 1;
		}
		s.close();
		
		System.out.println(sizes/n);
	}
}
