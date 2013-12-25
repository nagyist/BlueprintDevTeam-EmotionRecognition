import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class EvidenceManager {

	EmotionManager emotionManager;
	MeasurementManager measurementManager;
	Map<String, Set<String>> evidences;
	Set<Evidence> evidenceMatrix;
	int accuracy;
	String[] marker;
	
	public EvidenceManager(int a) {
	
		emotionManager = new EmotionManager();
		measurementManager = new MeasurementManager();
		evidences = new HashMap<String, Set<String>>();
		evidenceMatrix = new HashSet<Evidence>();
		accuracy = a;
		marker = new Emotion("dummy", 0f, 0, 0).getMarkers();
	}
	
	//TODO: Hinweis einfuegen "Achtung, dabei werden alle bisherigen Evid. geloescht! Fortfahren?"
	public void generateEvidenceMatrix (Measurement m) {
		
		generateEvidences(m);
		
		
	}
	
	public void generateEvidences (Measurement m)  {
		
		setUpEvidences();
		
		for (Emotion e : emotionManager.getEmotions()) {
			
			if (e.speed < m.speed + accuracy || e.speed > m.speed -1) {
				
				evidences.get("speed").add(e.name);
			}
			
			if (e.pitch < m.pitch + accuracy || e.pitch > m.pitch -1) {
				
				emotionSets.get(Marker.PITCH).add(e.name);
			
			}
			
			if (e.intensity < m.intensity + accuracy || e.intensity > m.intensity -1) {
				
				emotionSets.get(Marker.INTENSITY).add(e.name);
			}
			
			emotionSets.get().add(e.name);

		}
	} 
	
	public void setUpEvidences () {
	
		//TODO: Abbrechen, wenn emotionenListe leer
		evidences.clear();
		
		for (String m : marker) {
			
			evidences.put(m, null);
		}
	}
}
