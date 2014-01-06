
public class Emotion extends Marker{

	private String Name;

	public Emotion(String n, float s, int p, int i) {

		Name = n;
		Speed = s;
		Pitch = p;
		Intensity = i;
	}

	public String getName() {
		return Name;
	}
}
