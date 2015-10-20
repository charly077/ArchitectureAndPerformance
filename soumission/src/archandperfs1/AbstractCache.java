package archandperfs1;

/**
 * Simple cache class implementing basic hit rate calculation. In order to subclass it,
 * just implement the process(Request r) method and call hit and miss when they the
 * results have been found in the cache or not.
 */
public abstract class AbstractCache {
	public abstract Resource process(Request r);
	
	private int miss, hit;
	
	public int misses() {
		return miss;
	}
	
	public int hits() {
		return hit;
	}
	
	public double hitRate() {
		return (double) hit/(miss+hit);
	}
	
	public void hit(Request req) {
		hit++;
	}
	
	public Resource miss(Request req) {
		miss++;
		return new Resource(req.url, req.size);
	}
	
	public abstract String dump();
	
}
