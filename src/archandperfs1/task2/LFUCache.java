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
	
	int x,sizeMax,size = 0;
	
	/**
	 * to delete it was a test to be sure that the PriorityQueue was working correctly!!!
	 */
//	public static void main(String[] args) {
//		Resource res3 = new Resource("Bonjour,je suis la resource 3 ",5);
//		Resource res1 = new Resource("Bonjour, je suis la resource 1 ",1000);
//		Resource res2 = new Resource("Bonjour, je suis la resource 2 ", 10);
//		LFUCache lfu = new LFUCache(1000,0);
//		
//		ResNode resNode1 = new ResNode(res1);
//		ResNode resNode2 = new ResNode(res2);
//		ResNode resNode3 = new ResNode(res3);
//
//		resNode1.addCount();
//		resNode1.addCount();
//		
//		lfu.pqueue.add(resNode1);
//		lfu.pqueue.add(resNode2);
//		lfu.pqueue.add(resNode3);
//		
//		lfu.pqueue.remove(resNode2);
//		resNode2.addCount();
//		lfu.pqueue.add(resNode2);
//		
//		System.out.println(lfu.pqueue.poll().res);
//		System.out.println(lfu.pqueue.poll().res);
//		System.out.println(lfu.pqueue.poll().res);
//	}
	
	
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
				resNode.count = 0; // reset the count
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
		private int count = 0; // used by the priority queue!
		private Resource res;
		
		public ResNode(Resource res){
			this.res = res;
		}
		
		public void addCount(){
			count ++;
		}
		
		@Override 
		public boolean equals(Object other) {
		    boolean result = false;
		    if (other instanceof ResNode) {
		        ResNode that = (ResNode) other;
		        result = that.res.url == this.res.url;
		    }
		    return result;
		}
		
//		poll -> remove the least frequency element
		@Override
		public int compareTo(ResNode other) {
		    final int BEFORE = -1;
		    final int EQUAL = 0;
		    final int AFTER = 1;
		    if (this.count == other.count) 
		    	return EQUAL;
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
