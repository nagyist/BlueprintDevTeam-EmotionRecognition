import java.util.LinkedList;
import java.util.List;


public class EmotionManager {

	private List<Emotion> emotions;

	public EmotionManager() {
	
		emotions = new LinkedList<Emotion>();
	}
	
	public void loadDefault() {
		
		emotions.add(new Emotion("fear",	5.5f, 3, 3));
		emotions.add(new Emotion("suprise", 5.5f, 3, 5));
		emotions.add(new Emotion("anger",	5.5f, 3, 5));
		emotions.add(new Emotion("joy",		5.5f, 5, 5));
		emotions.add(new Emotion("disgust",	2.9f, 3, 1));
		emotions.add(new Emotion("sadness",	4.1f, 1, 1));
	}
	
	public void addEmotion (Emotion e) {
		
		emotions.add(e);
	}
	
	public void removeEmotion (String n) {
		
		for (Emotion e : emotions) {
			
			if(e.getName() == n) {
				
				emotions.remove(e);
			}
		}
	}
	
	public List<Emotion> getEmotions() {
		return emotions;
	}
}
