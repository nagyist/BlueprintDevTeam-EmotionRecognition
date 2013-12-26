import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class EmotionRecognizer {

	/* TODO Finale Checks:
	 * Kommentare entfernen
	 * Einheitliche Bezeichner (z.B. e ist immer Emotionion und nicht mal Emotion und mal Emotionen-Liste)
	 * Was public, was private, was ... ???
	 * Konventionen (Gro§-/Kleinschreibung)
	 */
	public static void main(String[] args) throws IOException, NoSuchFieldException {
		
		EvidenceManager em = new EvidenceManager(1);

		em.EmotionManager.loadDefault();
		em.MeasurementManager.measurementReader("/Users/sophie/Projekte/eese/dempster/EmotionRecognition/E_020/E_020.csv");

		Measurement m = em.MeasurementManager.getMeasurement(1);
		
		try {
			em.dempsterShafer(m);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Float dempsterShafer(List<Emotion> e, Measurement m) {
		
		//Evidence
		
		return 1f;
	}
}
