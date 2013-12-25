import java.util.Set;


public class Evidence {

	public Set<String> emotionnames;
	public float weight;
	
	public Evidence(Set<String> n, float w) {
	
		emotionnames = n;
		weight = w;
	}
}
