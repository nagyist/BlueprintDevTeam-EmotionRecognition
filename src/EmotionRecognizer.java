import java.io.IOException;
import java.util.List;

public class EmotionRecognizer {

	/* TODO Finale Checks:
	 * Kommentare entfernen
	 * Einheitliche Bezeichner (z.B. e ist immer Emotionion und nicht mal Emotion und mal Emotionen-Liste)
	 * Was public, was private, was ... ???
	 */
	public static void main(String[] args) throws IOException {
		
		EmotionManager emotions = new EmotionManager();
		MeasurementManager measurements = new MeasurementManager();

		emotions.loadDefault();
		measurements.measurementReader("/Users/sophie/Projekte/eese/dempster/EmotionRecognition/E_020/E_020.csv");

		
	}
	
	public Float dempsterShafer(List<Emotion> e, Measurement m) {
		
		//Evidence
		
		return 1f;
	}
}
