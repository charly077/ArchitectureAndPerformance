package archandperfs1.task2;

import java.util.HashMap;

import archandperfs1.BytehitrateWarmingCache;
import archandperfs1.Request;
import archandperfs1.Resource;

/**
 * A LRU cache implemented with an array of resources, and a hashmap in order to retrieve
 * the position in the array.
 */
public class LRUCache extends BytehitrateWarmingCache {
	private final HashMap<String, Node> mapping = new HashMap<>();
	private Node head, tail;
	private double sizeMax;
	
	private double size;
	
	
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
			res = miss(req);
			node.res = res;
			removeNode(node);
			toHead(node);
			return res;
		}
//		Enough space for stocking the ressource
		while((size+req.size) >= sizeMax){
			// remove the last one
			mapping.remove(tail.res.url);
			removeNode(tail);
		}
		
		// Create an element 
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
			head.pre = null;
		}	
		else
			previous.next = after;
		
// 		next modification
		if(after == null){
			tail = previous;
			previous.next = null;
		}
		else
			after.pre = previous;
		
		size -= n.res.size; // In order to keep the size correct
	}
	

	public void toHead(Node n){
		n.pre = null;
		n.next = head;
		
// 		Empty List
		if (head == null)
			tail = head;
		else
			head.pre = n;
		
		head = n;
		size += n.res.size; // In order to keep the size correct
	}
	
	
	public static class Node {
		private Resource res;
		private Node pre, next;
		public Node(Resource res){
			this.res = res;
		}
	}

}
