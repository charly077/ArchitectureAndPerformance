package archandperfs1.task1;

import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;

import archandperfs1.BytehitrateWarmingCache;
import archandperfs1.Request;
import archandperfs1.Resource;

public class LFUCache extends BytehitrateWarmingCache {
	private HashMap<String, ResNode> mapping = new HashMap<>();
	private final PriorityQueue<ResNode> pqueue = new PriorityQueue<>();
	private final int n;
	
	public LFUCache(int n, int x) {
		super(x);
		this.n = n;
	}

	@Override
	public Resource process(Request req) {
		ResNode rnode = mapping.get(req.url);
		if(rnode != null) {
//			Already in the HashMap
			Resource res = rnode.res;
			if(req.size == res.size) {
//				Same size => hit and return
				hit(req);
				pqueue.remove(rnode);
				rnode.addCount();
				pqueue.add(rnode);
				return res;
			}
			else {
//				Not the same size => miss, replace and return
				res = miss(req);
				pqueue.remove(rnode);
				rnode.resetResource(res);
				pqueue.add(rnode);
				return res;
			}
		}
		
		if(pqueue.size() == n) {
//			Full => find the less used and remove it
			ResNode removed = pqueue.poll();
			mapping.remove(removed.res.url);
		}
		
		Resource res = miss(req);
		rnode = new ResNode(res);
		pqueue.add(rnode);
		mapping.put(req.url, rnode);
		
		return res;
	}

	public static class ResNode implements Comparable<ResNode>{
		private int count = 0; // used by the priority queue!
		private long timestamp;
		private Resource res;
		
		public ResNode(Resource res){
			this.res = res;
			this.timestamp = System.nanoTime();
		}
		
		public void resetResource(Resource res) {
			this.res = res;
			this.count = 0;
			stamp();
		}
		
		public void addCount(){
			count++;
			stamp();
		}
		
		private void stamp() {
			this.timestamp = System.nanoTime();
		}
		
		@Override 
		public boolean equals(Object other) {
		    if (other instanceof ResNode) {
		        ResNode that = (ResNode) other;
		        return this.res.url.equals(that.res.url);
		    }
		    return false;
		}
		
//		poll -> remove the least frequency element
		@Override
		public int compareTo(ResNode other) {
		    final int BEFORE = -1;
		    final int AFTER = 1;
		    if (this.count == other.count)
//		    	The most recent goes further
		    	return (int) (this.timestamp - other.timestamp); 
		    else if (this.count < other.count)
		    	return BEFORE;
		    else
		    	return AFTER;
		}
	}

	@Override
	public String dump() {
		Iterator<ResNode> iter = pqueue.iterator();
		StringBuilder b = new StringBuilder();
		while(iter.hasNext()) {
			b.append(iter.next().res.toString()).append('\n');
		}
		return b.toString();
	}

}
