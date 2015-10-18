package archandperfs1.task1;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import archandperfs1.BytehitrateWarmingCache;
import archandperfs1.Request;

public class Task1 {
	public static void main(String[] args) throws IOException {
		int n = Integer.parseInt(args[1]), x = Integer.parseInt(args[0]);

		BytehitrateWarmingCache lfu = new LFUCache(n, x);
		BytehitrateWarmingCache lru = new LRUCache(n, x);
		
		StringBuilder b = new StringBuilder("");
		Scanner in = new Scanner(System.in);
		
		double reqs = 0;
		XYSeries lfudata = new XYSeries("LFU");
		XYSeries lrudata = new XYSeries("LRU");
		
		while(in.hasNext()) {
			Request req = new Request(in.next(), in.nextInt());
			lfu.process(req);
			lfudata.add(reqs, lfu.hitRate());
			b.append(String.format("%.2f", lfu.hitRate())).append(' ');
			
			lru.process(req);
			lrudata.add(reqs, lru.hitRate());
			b.append(String.format("%.2f", lru.hitRate())).append(';');
			reqs++;
		}
		in.close();
		
		System.out.println(String.format("LRU hitrate: %.2f", lru.hitRate()*100));
		System.out.println(String.format("LFU hitrate: %.2f", lfu.hitRate()*100));
		
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(lfudata);
		dataset.addSeries(lrudata);
		
		JFrame f = new ChartFrame("", ChartFactory.createXYLineChart("", "", "", dataset));
		f.setPreferredSize(new Dimension(640, 480));
		f.pack();
		f.setVisible(true);
//		PrintWriter pr = new PrintWriter("cache_lfu.txt");
//		pr.write(lfu.dump());
//		pr.close();
//		pr = new PrintWriter("cache_lru.txt");
//		pr.write(lru.dump());
//		pr.close();
	}
	
	public static void toClipboard(String s) {
		StringSelection sel = new StringSelection(s);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sel, sel);
	}
}
