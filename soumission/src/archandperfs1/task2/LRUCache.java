package archandperfs1.task2;

import java.util.HashMap;

import archandperfs1.BytehitrateWarmingCache;
import archandperfs1.Request;
import archandperfs1.Resource;

/**
 * A LRU cache implemented with a linked list of Resources, using a HashMap for lookup.
 */
public class LRUCache extends BytehitrateWarmingCache {
	private final HashMap<String, Node> mapping = new HashMap<>();
	private Node head, tail;
	private long sizeMax;
	
	private long size;
	
	
	public LRUCache(int size, int x) {
		super(x);
		this.sizeMax = size;
	}
	
	@Override
	public Resource process(Request req) {
		Node node = mapping.get(req.url);
		
		if(node != null){
//			Already in the HashMap
			Resource res = node.res;
			if(res.size == req.size){
//				Same size => hit, toHead and return
				hit(req);
				removeNode(node);
				toHead(node);
				return res;
			}
//			Not the same size => miss, toHead and return
			removeNode(node);
			res = miss(req);
			node.res = res;
			toHead(node);
			return res;
		}
//		Not in the hashMap and too big for the cache
		if (req.size >= sizeMax){
			Resource res = miss(req);
			return res;
		}
//		Not in the hashMap but space available
		
		while((size+req.size) >= sizeMax){
			// remove the last one
			mapping.remove(tail.res.url);
			removeNode(tail);
		}
		
//		Create an element 
		Resource res = miss(req);
		node = new Node(res);
		toHead(node);
		mapping.put(req.url, node);
		return res;
	}
	
	
	public void removeNode(Node n){
		Node previous=n.pre, after=n.next;
		
// 		previous modification
		if(previous == null){
			head = after;
		}	
		else
			previous.next = after;
		
// 		next modification
		if(after == null){
			tail = previous;
		}
		else
			after.pre = previous;
		
//		add the size to the one of what is already full 
		size -= n.res.size; 
	}
	
//	Put a node at the state of the FIFO
	public void toHead(Node n){
		n.pre = null;
		n.next = head;
		
// 		Empty List
		if (head == null)
			tail = head = n;
		else
			head.pre = n;
		
		head = n;
		
//		In order to keep the size correct
		size += n.res.size;
	}
	
//	Keep the resource
	public static class Node {
		private Resource res;
		private Node pre, next;
		public Node(Resource res){
			this.res = res;
		}
	}

	@Override
	public String dump() {
		Node n = head;
		
		StringBuilder b = new StringBuilder();
		while (n!=null){
			b.append(n.res.url).append('\n');
			n=n.next;

		}
		return b.toString();
	}

}
