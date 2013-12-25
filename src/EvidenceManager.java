import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;



public class EvidenceManager {

	EmotionManager EmotionManager;
	MeasurementManager MeasurementManager;
	Map<String, Set<String>> Evidences;
	Set<Evidence> EvidenceMatrix;
	int Accuracy;
	String[] Marker;
	
	public EvidenceManager(int a) {
	
		EmotionManager = new EmotionManager();
		MeasurementManager = new MeasurementManager();
		Evidences = new HashMap<String, Set<String>>();
		EvidenceMatrix = new HashSet<Evidence>();
		Accuracy = a;
		Marker = new Emotion("dummy", 0f, 0, 0).getMarkers();
	}
	
	//TODO: Hinweis einfuegen "Achtung, dabei werden alle bisherigen Evid. geloescht! Fortfahren?"
	public void generateEvidenceMatrix (Measurement m) 
			throws SecurityException, IllegalArgumentException, NoSuchMethodException, 
					IllegalAccessException, InvocationTargetException {
		
		generateEvidences(m);
		
	}
	
	public void generateEvidences (Measurement m) 
			throws SecurityException, NoSuchMethodException, IllegalArgumentException, 
					IllegalAccessException, InvocationTargetException {
		
		setUpEvidences();
		
		for (Emotion e : EmotionManager.getEmotions()) {
			
			for (String marker : Marker) {
				
				Method methodGet = Emotion.class.getMethod("get" + marker);
				
			
				if((Float) methodGet.invoke(e) < (Float) methodGet.invoke(m) + Accuracy ||
						(Float) methodGet.invoke(e) > (Float) methodGet.invoke(m) - Accuracy) {
					
					Evidences.get(marker).add(e.Name);
				}
			}
		}
	} 
	
	public void setUpEvidences () {
	
		//TODO: Abbrechen, wenn emotionenListe leer
		Evidences.clear();
		
		for (String m : Marker) {
			
			Evidences.put(m, null);
			Evidences.put(m + "Omega", null);
		}
	}
}
