import java.util.LinkedList;
import java.util.List;

public class EmotionRecognizer {

	public static void main(String[] args) {
		
		List<Emotion> emotions = new LinkedList<Emotion>();
		
		emotions.add(new Emotion("fear",	5.5f, Level.HIGH, Level.HIGH));
		emotions.add(new Emotion("suprise", 5.5f, Level.HIGH, Level.HIGH));
		emotions.add(new Emotion("anger",	5.5f, Level.HIGH, Level.HIGH));
		emotions.add(new Emotion("joy",		5.5f, Level.VERYHIGH,	Level.HIGH));
		emotions.add(new Emotion("disgust",	2.9f, Level.HIGH, Level.HIGH));
		emotions.add(new Emotion("sadness",	4.1f, Level.HIGH, Level.HIGH));
	}
}
