package archandperfs1.task2;

import archandperfs1.BytehitrateWarmingCache;
import archandperfs1.Request;
import archandperfs1.Resource;

/**
 * A LRU cache implemented with an array of resources, and a hashmap in order to retrieve
 * the position in the array.
 */
public class LRUCache extends BytehitrateWarmingCache {
	
	public LRUCache(int n, int x) {
		super(x);
	}
	
	@Override
	public Resource process(Request req) {
		return null;
	}

}
