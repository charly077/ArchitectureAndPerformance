package archandperfs1;

/**
 * The class the caches be able to process and return a Resource from it.
 */
public class Request {
	public String url;
	public int size;
	
	public Request(String url, int size) {
		this.url = url;
		this.size = size;
	}
	
	@Override
	public String toString() {
		return url+" "+size;
	}
}
