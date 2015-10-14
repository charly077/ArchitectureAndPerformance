package archandperfs1;

/**
 * Another Cache base class, relying on AbstractCache, but this time, taking a warmup parameter,
 * and computing the byte hit rate as well.
 */
public abstract class BytehitrateWarmingCache extends AbstractCache {
	private final int x;
	private int requests = 0;
	private long bytehit = 0;
	private long bytemiss = 0;
	
	public BytehitrateWarmingCache(int x) {
		this.x = x;
	}
	
	@Override
	public Resource miss(Request req) {
		requests++;
		if(requests > x) {
			bytemiss += req.size;
			return super.miss(req);
		}
		return new Resource(req.url, req.size);
	}
	
	@Override
	public void hit(Request req) {
		requests++;
		if(requests > x) {
			bytehit += req.size;
			super.hit(req);
		}
	}
	
	public long bytehits() {
		return bytehit;
	}
	
	public long bytemisses() {
		return bytemiss;
	}
	
	public double byteHitRate() {
		return (double) bytehit/(bytehit+bytemiss);
	}
	
}
