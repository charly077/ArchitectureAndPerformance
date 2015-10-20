package archandperfs1.task1;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import archandperfs1.BytehitrateWarmingCache;
import archandperfs1.Request;

public class PlotHitrateOnSize {
	public static void main(String[] args) throws IOException {
		ArrayList<Request> req = new ArrayList<>();
		Scanner in = new Scanner(System.in);
		while(in.hasNext()) {
			req.add(new Request(in.next(), in.nextInt()));
		}
		in.close();
		
		XYSeries lfudata = new XYSeries("LFU");
		XYSeries lrudata = new XYSeries("LRU");
		
		BytehitrateWarmingCache lfu, lru;
		int step = 10;
		for(int i = 1; i <= 3001; i += step) {
			lfu = new LFUCache(i, 20000);
			lru = new LRUCache(i, 20000);
			
			for(int j = 0; j < req.size(); j++) {
				lfu.process(req.get(j));
				lru.process(req.get(j));
			}

			lfudata.add(i, lfu.hitRate());
			lrudata.add(i, lru.hitRate());
			
		}
		
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(lfudata);
		dataset.addSeries(lrudata);
		
		ChartUtilities.saveChartAsPNG(new File("hitratio.png"), ChartFactory.createXYLineChart("", "Number of requests", "Hit ratio", dataset), 1280, 720);
		
//		JFrame f = new ChartFrame("", ChartFactory.createXYLineChart("", "Size of cache (slots)", "Hitrate", dataset));
//		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		f.setPreferredSize(new Dimension(640, 480));
//		f.pack();
//		f.setVisible(true);
	}

}
