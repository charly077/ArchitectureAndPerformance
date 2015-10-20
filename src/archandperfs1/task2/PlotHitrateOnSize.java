package archandperfs1.task2;

import java.io.File;
import java.io.FileReader;
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

			XYSeries lruBdata = new XYSeries("LRU (byte hitrate)");
			XYSeries lfuBdata = new XYSeries("LFU (byte hitrate)");
			XYSeries rlfBdata = new XYSeries("RLF (byte hitrate)");

			BytehitrateWarmingCache lfu, lru, rlf;
			int step = 10*21152;
			for(int i = 1; i <= 64*1024*1024; i += step) {
				lfu = new LFUCache(i, 20000);
				lru = new LRUCache(i, 20000);
				rlf = new RLFCache(i, 20000);

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

//				System.out.println(i);
			}

			XYSeriesCollection dataset = new XYSeriesCollection();
			dataset.addSeries(lfudata);
			dataset.addSeries(lrudata);
			dataset.addSeries(rlfdata);

			dataset.addSeries(lfuBdata);
			dataset.addSeries(lruBdata);
			dataset.addSeries(rlfBdata);

			ChartUtilities.saveChartAsPNG(new File("hitratio2.png"), ChartFactory.createXYLineChart("", "Cache size (bytes)", "Hitrate", dataset), 1280, 720);

//			JFrame f = new ChartFrame("", ChartFactory.createScatterPlot("", "Size of cache", "Hit ratio", dataset));
//			f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//			f.setPreferredSize(new Dimension(640, 480));
//			f.pack();
//			f.setVisible(true);
		}
	}

}
