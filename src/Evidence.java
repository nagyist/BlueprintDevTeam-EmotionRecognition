import java.util.Set;


public class Evidence {

	private Set<String> Emotionnames;
	private float Weight;
	
	public Evidence(Set<String> n, float w) {
	
		Emotionnames = n;
		Weight = w;
	}

	public Set<String> getEmotionnames() {
		return Emotionnames;
	}

	public void addEmotionname(String emotionname) {
		this.Emotionnames.add(emotionname);
	}

	public float getWeight() {
		return Weight;
	}

	public void setWeight(float weight) {
		this.Weight = weight;
	}
}
