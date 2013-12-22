import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class EmotionRecognizer {

	public static void main(String[] args) throws IOException {
		
		List<Emotion> emotions = new LinkedList<Emotion>();
		List<Measurement> measurements = new LinkedList<Measurement>();
		
		/* TODO: Bisherige Skala anpassen?
		 * 1 ... sehr niedrig
		 * 2 ... niedrig
		 * 3 ... normal
		 * 4 ... hoch
		 * 5 ... sehr hoch
		 */
		
		/* TODO: "†bersetzung" von Silben/sec in unsere 5er Skala ???
		 * (5.5 sind sehr hoch, 2.9 sind sehr niedrig)
		 */
		// Emotion(String n, float s, int p, int i)
		emotions.add(new Emotion("fear",	5.5f, 3, 3));
		emotions.add(new Emotion("suprise", 5.5f, 3, 5));
		emotions.add(new Emotion("anger",	5.5f, 3, 5));
		emotions.add(new Emotion("joy",		5.5f, 5, 5));
		emotions.add(new Emotion("disgust",	2.9f, 3, 1));
		emotions.add(new Emotion("sadness",	4.1f, 1, 1));

		
		measurements.add(new Measurement(5.5f, 3, 3));
	}
	
	public Float dempsterShafer(Emotion e, Measurement m) {
		
		return 1f;
	}
}
