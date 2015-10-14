package archandperfs1.task1;

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
	private final int n;
	
	private Node head, last;
	
	public LRUCache(int n, int x) {
		super(x);
		this.n = n;
	}
	
	@Override
	public Resource process(Request req) {
		Node node = mapping.get(req.url);
		if(node != null) {
//			Already in the HashMap
			Resource res = node.res;
			if(req.size == res.size) {
//				Same size => hit, toHead and return
				hit(req);
				removeNode(node);
				toHead(node);
				return res;
			}
			else {
//				Not the same size => miss, toHead and return
				res = miss(req);
				node.res = res;
				removeNode(node);
				toHead(node);
				return res;
			}
		}
//		Not in the HashMap. Full or not?
		if(mapping.size() >= n) {
//			Full => remove the last element
			mapping.remove(last.res.url);
			removeNode(last);
		}
		
//		Let's create a Node and put it at the beginning
		Resource res = miss(req);
		node = new Node(res);
		toHead(node);
		mapping.put(req.url, node);
		return res;
	}
	
	public void removeNode(Node n) {
		Node before = n.prev, after = n.next;
		n.prev = n.next = null;
		
//		Has a prev. No? then was first
		if(before == null)
			head = after;
		else
			before.next = after;
		
//		Has a next. No? then was last
		if(after == null)
			last = before;
		else
			after.prev = before;
	}
	
	public void toHead(Node n) {
		n.prev = null;
		n.next = head;
//		One element only
		if(head == null)
			last = n;
		else
			head.prev = n;
		head = n;
	}
	
	public static class Node {
		private Resource res;
		private Node prev, next;
		public Node(Resource res) {
			this.res = res;
		}
	}

	@Override
	public String dump() {
		Node n = head;
		StringBuilder b = new StringBuilder();
		while(n != null) {
			b.append(n.res.url).append('\n');
			n = n.next;
		}
		return b.toString();
	}

}
