package archandperfs1.task2;

import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;

import archandperfs1.BytehitrateWarmingCache;
import archandperfs1.Request;
import archandperfs1.Resource;

/**
 * Remove large first cache strategy
 *
 */
public class RLFCache extends BytehitrateWarmingCache{
	private HashMap<String, ResNode> mapping = new HashMap<>(); // to be able of modifying a node
	private  PriorityQueue<ResNode> pqueue = new PriorityQueue<ResNode>();
	
	int x,sizeMax,size = 0;
	
	
	public RLFCache(int sizeMax, int x) {
		super(x);
		this.sizeMax = sizeMax;
	}

	@Override
	public Resource process(Request req) {
		ResNode resNode = mapping.get(req.url);
		if(resNode != null){
//			Already in the hash
			Resource res = resNode.res;
			if(res.size == req.size){
//				Nothing to do
				hit(req);
				return res;
			}
			else{
//				size is different
				size -= res.size;
				res = miss(req);
				size += res.size;
				pqueue.remove(resNode);
				resNode.res = res; // modify the content
				pqueue.add(resNode);
				return res;
			}
		}
//		Not in the hashMap and too big for the cache
		if (req.size >= sizeMax){
			Resource res = miss(req);
			return res;
		}
//		Not in the hashMap but space available
		while( (size+req.size >= sizeMax)){
			resNode = pqueue.poll();
			mapping.remove(resNode.res.url);
			size -= resNode.res.size;
		}
		Resource res = miss(req);
		resNode = new ResNode(res);
		mapping.put(resNode.res.url, resNode);
		pqueue.add(resNode);
		size += res.size;
		return res;
	}

	
//	PriorityQueue : Resource with comparator
	public static class ResNode implements Comparable<ResNode>{
		private Resource res;
		
		public ResNode(Resource res){
			this.res = res;
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
		    final int EQUAL = 0;
		    final int AFTER = 1;
		    if (this.res.size == other.res.size) 
		    	return EQUAL;
		    else if (this.res.size < other.res.size)
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
