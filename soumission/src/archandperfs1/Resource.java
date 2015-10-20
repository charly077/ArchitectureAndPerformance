package archandperfs1;

/**
 * The class the cache must return in response to a request.
 */
public class Resource {
	public String url;
	public int size;
	
	public Resource(String url, int size) {
		this.url = url;
		this.size = size;
	}
	
	@Override
	public String toString() {
		return url+" "+size;
	}
}
