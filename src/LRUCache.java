import java.util.Dictionary;


public class LRUCache {
	
	private int maxNbData=0;
	private int nbData = 0;
	private Node head = null;
	private Node tail = null;
	
	
	private class Node{
		private String lien = "";
		private int size = 0; 
		private Node next = null;
		private Node previous = null;
		public Node(String elem, int size, Node next, Node previous){
			this.lien = elem;
			this.size = size;
			this.next = next;
			this.previous = previous;
		}
		public String getLien() {
			return lien;
		}
		public int getSize() {
			return size;
		}
		public Node getNext() {
			return next;
		}
		public Node getPrevious() {
			return previous;
		}
	}
	
	public LRUCache(int maxNbData) {
		this.maxNbData = maxNbData;
	}
	
	
	public boolean isEmpty(){
		return nbData==0;
	}
	
	public boolean isFull(){
		return nbData == maxNbData;
	}
	
	/*
	 * This method is use to get an url (so we have to get it and put it in the cache)
	 * return true for a hit, false in other cases
	 */
	public Boolean get(String Elem, int size){
		if(isEmpty()){
			this.add(Elem,size);
			return false;
		}
		else if (this.putFirstIfContained(Elem,size)){
			return true;
		}
		else{
			this.add(Elem,size);
			return false;
		}	
	}
	
	public void add(String Elem, int size){
		//TODO ajouter l'élément au début et peut être supprimer le dernier et si c'était pas complet faire taille+1
	}

	//ne peut pas être appelé si la chaine est vide !!!
	public Boolean putFirstIfContained(String Elem,int size){
		Node node = head;
		do{
			if (node.getLien()==Elem){
				//si l'élément Elem est contenu et que size = size ==> on le déplace en premier
				if (node.size == size){
					//TODO
				}
				//si l'élément Elem est contenu et que size != size ==> on le place en dernier
				else{
					//TODO supprimer node
					add(Elem,size);
					return false;
				}
			}
		//si l'élement n'est pas contenu ==> false
		}while ((node=node.next)!=null);
		return false;
	}
}
