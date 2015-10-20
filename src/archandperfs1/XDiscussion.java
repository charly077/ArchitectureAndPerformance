package archandperfs1;

import java.awt.Dimension;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import archandperfs1.task1.LFUCache;
import archandperfs1.task1.LRUCache;

public class XDiscussion {
	public static void main(String[] args) throws IOException {
		int n = Integer.parseInt(args[1]), x = Integer.parseInt(args[0]);

		BytehitrateWarmingCache lfu = new LFUCache(n, x);
		BytehitrateWarmingCache lru = new LRUCache(n, x);
		
		Scanner in = new Scanner(System.in);
		
		double reqs = 0;
		XYSeries lfudata = new XYSeries("LFU");
		XYSeries lrudata = new XYSeries("LRU");
		
		while(in.hasNext()) {
			Request req = new Request(in.next(), in.nextInt());
			lfu.process(req);
			lfudata.add(reqs, lfu.hitRate());
			
			lru.process(req);
			lrudata.add(reqs, lru.hitRate());
			reqs++;
		}
		in.close();
		
		System.out.println(String.format("LRU hitrate: %.2f", lru.hitRate()*100));
		System.out.println(String.format("LFU hitrate: %.2f", lfu.hitRate()*100));
		
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(lfudata);
		dataset.addSeries(lrudata);
		
//		ChartUtilities.saveChartAsPNG(new File("hitratio.png"), ChartFactory.createXYLineChart("", "Number of requests", "Hit ratio", dataset), 1280, 720);
		
		JFrame f = new ChartFrame("", ChartFactory.createXYLineChart("", "Number of requests", "Hit ratio", dataset));
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

}
