package archandperfs1.task1;

import java.util.HashMap;

import archandperfs1.Request;
import archandperfs1.Resource;
import archandperfs1.BytehitrateWarmingCache;

/**
 * A LRU cache implemented with an array of resources, and a hashmap in order to retrieve
 * the position in the array.
 */
public class LRUCache extends BytehitrateWarmingCache {
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
		if(i != null) {
//			Already in the HashMap
			Resource res = content[i];
			if(req.size == res.size) {
//				Same size => hit and return
				recentlyUsed[i] = true;
				hit(req);
				return res;
			}
			else {
//				Not the same size => miss, replace and return
				res = miss(req);
				recentlyUsed[i] = false;
				content[i] = res;
				return res;
			}
		}
		
//		Not in the HashMap. Full or not?
		if(elems == n) {
//			Full => remove the next unused element
			while(recentlyUsed[cursor] == true) {
				recentlyUsed[cursor] = false;
				cursor = (cursor+1)%n;
			}
			Resource toRemove = content[cursor];
			mapping.remove(toRemove.url);
			elems--;
		}
		
//		Let's put the resource at position cursor
		Resource res = miss(req);
		content[cursor] = res;
		mapping.put(res.url, cursor);
		recentlyUsed[cursor] = false;
		
		elems++;
		cursor = (cursor+1)%n;
		
		return res;
	}

}
