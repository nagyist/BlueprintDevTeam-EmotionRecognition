import java.util.LinkedList;
import java.util.List;


public class EmotionManager {

	private List<Emotion> Emotions;

	public EmotionManager() {
	
		Emotions = new LinkedList<Emotion>();
	}
	
	public void loadDefault() {
		
		Emotions.add(new Emotion("fear",	5.5f, 3, 3));
		Emotions.add(new Emotion("suprise", 5.5f, 3, 5));
		Emotions.add(new Emotion("anger",	5.5f, 3, 5));
		Emotions.add(new Emotion("joy",		5.5f, 5, 5));
		Emotions.add(new Emotion("disgust",	2.9f, 3, 1));
		Emotions.add(new Emotion("sadness",	4.1f, 1, 1));
	}
	
	public boolean containsEmotion (String name) {
		
		for (Emotion e : Emotions) {
			
			if (e.getName().equals(name)) return true;
		}
		
		return false;
	}
	
	public void addEmotion (Emotion e) {
		
		Emotions.add(e);
	}
	
	public void removeEmotion (String n) {
		
		for (Emotion e : Emotions) {
			
			if(e.getName() == n) {
				
				Emotions.remove(e);
			}
		}
	}
	
	public List<Emotion> getEmotions() {
		return Emotions;
	}
}
