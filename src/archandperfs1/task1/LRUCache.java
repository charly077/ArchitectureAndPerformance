package archandperfs1.task1;

import java.util.HashMap;

import archandperfs1.Request;
import archandperfs1.Resource;
import archandperfs1.ByterateWarmingCache;

/**
 * A LRU cache implemented with an array of resources, and a hashmap in order to retrieve
 * the position in the array.
 */
public class LRUCache extends ByterateWarmingCache {
	private final HashMap<String, Integer> mapping = new HashMap<>(); 
	private final Resource[] content;
	private final boolean[] recentlyUsed;
	private final int n;
	
	private int cursor = 0, elems = 0;
	
	public LRUCache(int n, int x) {
		super(x);
		this.n = n;
		this.content = new Resource[n];
		this.recentlyUsed = new boolean[n];
	}
	
	@Override
	public Resource process(Request req) {
		Integer i = mapping.get(req.url);
//		Already in the HashMap
		if(i != null) {
			recentlyUsed[i] = true;
			hit(req);
			return content[i];
		}
		
//		Not in the HashMap or not the same size. Full or not?
		if(elems == n) {
			while(recentlyUsed[cursor] == true) {
				recentlyUsed[cursor] = false;
				cursor = (cursor+1)%n;
			}
			
			Resource toRemove = content[cursor];
			mapping.remove(toRemove.url);
			elems--;
		}
		
		Resource res = miss(req);
		content[cursor] = res;
		mapping.put(res.url, cursor);
		recentlyUsed[cursor] = false;
		
		elems++;
		cursor = (cursor+1)%n;
		
		return res;
	}

}
