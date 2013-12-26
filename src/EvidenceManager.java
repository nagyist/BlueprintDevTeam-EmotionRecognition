import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class EvidenceManager {

	EmotionManager EmotionManager;
	MeasurementManager MeasurementManager;
	Map<String, Evidence> BasicEvidences;
	Set<Evidence> EvidenceMatrix;
	int Accuracy;
	String[] Marker;
	
	public EvidenceManager(int a) {
	
		EmotionManager = new EmotionManager();
		MeasurementManager = new MeasurementManager();
		BasicEvidences = new HashMap<String, Evidence>();
		EvidenceMatrix = new HashSet<Evidence>();
		Accuracy = a;
		Marker = new Emotion("dummy", 0f, 0, 0).getMarkers();
	}
	
	//TODO: Hinweis einfuegen "Achtung, dabei werden alle bisherigen Evid. geloescht! Fortfahren?"
	public void generateEvidenceMatrix (Measurement m) 
			throws SecurityException, IllegalArgumentException, NoSuchMethodException, 
					IllegalAccessException, InvocationTargetException, NoSuchFieldException {
		
		setUpBasicEvidences();
		generateBasicEvidences(m);
		
		for (String ma : BasicEvidences.keySet()) {
			
			System.out.println(ma);
			
			System.out.println(BasicEvidences.get(ma).getEmotionnames());
			System.out.println(BasicEvidences.get(ma).getWeight());
			
		}
		
	}
	
	public void generateBasicEvidences (Measurement m) 
			throws SecurityException, NoSuchMethodException, IllegalArgumentException, 
					IllegalAccessException, InvocationTargetException, NoSuchFieldException {
		
		for (String marker : Marker) {
			
			for (Emotion e : EmotionManager.getEmotions()) {
			
				if( e.getAnyValue(marker) <= m.getAnyValue(marker) + Accuracy &&
						e.getAnyValue(marker) >= m.getAnyValue(marker) - Accuracy) {
					
					BasicEvidences.get(marker).addEmotionname(e.Name);
				}
				
				BasicEvidences.get(marker + "Omega").addEmotionname(e.Name);
			}
			
			BasicEvidences.get(marker).setWeight(m.getWeight(marker));
			BasicEvidences.get(marker + "Omega").setWeight(1 - m.getWeight(marker));
		}
	} 
	
	public void setUpBasicEvidences () {
	
		//TODO: Abbrechen, wenn emotionenListe leer
		BasicEvidences.clear();
		
		for (String m : Marker) {
			
			BasicEvidences.put(m, new Evidence(new HashSet<String>(), 0f));
			BasicEvidences.put(m + "Omega", new Evidence(new HashSet<String>(), 0f));
		}
	}
}
