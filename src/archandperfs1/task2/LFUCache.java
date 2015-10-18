package archandperfs1.task2;

import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;

import archandperfs1.BytehitrateWarmingCache;
import archandperfs1.Request;
import archandperfs1.Resource;

//TODO Test if when we modify a node, it modify the priorityQueue

public class LFUCache extends BytehitrateWarmingCache{
	private HashMap<String, ResNode> mapping = new HashMap<>(); // to be able of modifying a node
	private  PriorityQueue<ResNode> pqueue = new PriorityQueue<ResNode>();
	
	private int sizeMax, size = 0;
	
	public LFUCache(int sizeMax, int x) {
		super(x);
		this.sizeMax = sizeMax;
	}

	@Override
	public Resource process(Request req) {
//		System.out.println("taille de la req = "+ req.size + "\ncache: "+size+"/"+sizeMax);
		ResNode resNode = mapping.get(req.url);
		if(resNode != null){
//			Already in the hash
			Resource res = resNode.res;
			if(res.size == req.size){
				hit(req);
				pqueue.remove(resNode);
				resNode.addCount(); // add 1 to the frequency count
				pqueue.add(resNode);
				return res;
			}
			else{
//				size is different
				size -= res.size;
				res = miss(req);
				size += res.size;
				pqueue.remove(resNode);
				resNode.resetResource(res);
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
