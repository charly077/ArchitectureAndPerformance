package archandperfs1.task1;

import java.util.Arrays;
import java.util.HashMap;

import archandperfs1.BytehitrateWarmingCache;
import archandperfs1.Request;
import archandperfs1.Resource;

public class LFUCache extends BytehitrateWarmingCache {
	
	private HashMap<String, Integer> mapping = new HashMap<>();
	private final Resource[] content;
	private final int[] counts;
	private final int n;
	
	private int elems = 0;
	
	public LFUCache(int n, int x) {
		super(x);
		this.n = n;
		this.content = new Resource[n];
		this.counts = new int[n];
		Arrays.fill(counts,  0);
	}

	@Override
	public Resource process(Request req) {
		Integer i = mapping.get(req.url);
		if(i != null) {
//			Already in the HashMap
			Resource res = content[i];
			if(req.size == res.size) {
//				Same size => hit and return
				hit(req);
				counts[i]++;
				return res;
			}
			else {
//				Not the same size => miss, replace and return
				res = miss(req);
				content[i] = res;
				counts[i] = 0;
				return res;
			}
		}
		
//		Add an element
		int cursor = elems;
		if(elems == n) {
//			Full => find the less used and remove it
			int lessUsed = lessUsed();
			Resource toRemove = content[lessUsed];
			mapping.remove(toRemove.url);
			content[lessUsed] = null;
			counts[lessUsed] = 0;
			cursor = lessUsed;
			elems--;
		}
		
		Resource res = miss(req);
		mapping.put(res.url, cursor);
		content[cursor] = res;
		
		elems++;
		return res;
	}
	
	private int lessUsed() {
		int iless = 0, lesscount = Integer.MAX_VALUE;
		
		for(int i = 0; i < elems; i++) {
			if(counts[i] < lesscount) {
				iless = i;
				lesscount = counts[i];
			}
		}
		
		return iless;
	}

	@Override
	public String dump() {
		StringBuilder b = new StringBuilder();
		for(int i = 0; i < content.length; i++) {
			if(content[i] != null)
				b.append(content[i].url).append('\n');
		}
		return b.toString();
	}

}
