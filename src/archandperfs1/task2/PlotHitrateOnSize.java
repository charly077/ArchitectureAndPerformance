package archandperfs1.task2;

import java.awt.Dimension;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartUtilities;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import archandperfs1.BytehitrateWarmingCache;
import archandperfs1.Request;

public class PlotHitrateOnSize {
	public static void main(String[] args) throws IOException {
		
		try (FileReader fr = new FileReader ("trace.txt")){
		ArrayList<Request> req = new ArrayList<>();
		Scanner in = new Scanner(fr);
		while(in.hasNext()) {
			req.add(new Request(in.next(), in.nextInt()));
		}
		in.close();
		
		XYSeries lrudata = new XYSeries("LRU");
		XYSeries lfudata = new XYSeries("LFU");
		XYSeries rlfdata = new XYSeries("RLF");
		
		XYSeries lruBdata = new XYSeries("LRU (byte hit rate)");
		XYSeries lfuBdata = new XYSeries("LFU (byte hit rate)");
		XYSeries rlfBdata = new XYSeries("RLF (byte hit rate)");
		
		BytehitrateWarmingCache lfu, lru, rlf;
		int step = 100000;
		for(int i = 100000; i <= 4000000; i += step) {
			lfu = new LFUCache(i, 4500);
			lru = new LRUCache(i, 4500);
			rlf = new RLFCache(i, 4500);
			
			for(int j = 0; j < req.size(); j++) {
				lfu.process(req.get(j));
				lru.process(req.get(j));
				rlf.process(req.get(j));
			}

			lfudata.add(i, lfu.hitRate());
			lrudata.add(i, lru.hitRate());
			rlfdata.add(i, rlf.hitRate());
			
			lfuBdata.add(i, lfu.byteHitRate());
			lruBdata.add(i, lru.byteHitRate());
			rlfBdata.add(i, rlf.byteHitRate());
			
			System.out.println(i);
		}
		
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(lfudata);
		dataset.addSeries(lrudata);
		dataset.addSeries(rlfdata);
		
		dataset.addSeries(lfuBdata);
		dataset.addSeries(lruBdata);
		dataset.addSeries(rlfBdata);
		
		ChartUtilities.saveChartAsPNG(new File("hitratio2.png"), ChartFactory.createXYLineChart("", "Number of requests", "Hit ratio", dataset), 1280, 720);
		
		JFrame f = new ChartFrame("", ChartFactory.createScatterPlot("", "Size of cache", "Hit ratio", dataset));
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setPreferredSize(new Dimension(640, 480));
		f.pack();
		f.setVisible(true);
		}
	}

}
