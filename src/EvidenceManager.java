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
	public void dempsterShafer (Measurement m) 
			throws SecurityException, IllegalArgumentException, NoSuchMethodException, 
					IllegalAccessException, InvocationTargetException, NoSuchFieldException {
		
		Set<Evidence> matrix = new HashSet<Evidence>();
		
		resetEvidences();
		generateBasicEvidences(m);
	
//		for (String ma : BasicEvidences.keySet()) {
//		
//			System.out.println(ma);
//			
//			System.out.println(BasicEvidences.get(ma).getEmotionnames());
//			System.out.println(BasicEvidences.get(ma).getWeight());	
//		}
		
		String[] s1 = {"Speed", "SpeedOmega"}, s2 = {"Pitch", "PitchOmega"};
		matrix = combineEvidences(s1, s2);
		
		for (Evidence evidence : matrix) {
			
			System.out.println(evidence.getEmotionnames() + "\t" + evidence.getWeight());
		}
		
		for (String marker : Marker) {
			
			
		}
	}
	
	public Set<Evidence> combineEvidences (String[] e1, String[] e2) {
		
		Set<Evidence> col = new HashSet<Evidence>(), 
					  row = new HashSet<Evidence>(),
					  ec = new HashSet<Evidence>();
		
		Set<String> intersection = new HashSet<String>();

		for (String s : e1)	col.add(BasicEvidences.get(s));
		for (String s : e2)	row.add(BasicEvidences.get(s));
		
		col.add(BasicEvidences.get(Marker[0]));
		col.add(BasicEvidences.get(Marker[0] + "Omega"));
		                                  
		row.add(BasicEvidences.get(Marker[1]));
		row.add(BasicEvidences.get(Marker[1] + "Omega"));
		
		for (Evidence c : col) {
			
			for (Evidence r : row) {
				
				intersection = new HashSet<String>(c.getEmotionnames());
				intersection.retainAll(r.getEmotionnames());
				ec.add(new Evidence(intersection, c.getWeight() * r.getWeight()));
			}
		}
		
		return ec;
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
	
	public void resetEvidences () {
	
		//TODO: Abbrechen, wenn emotionenListe leer
		BasicEvidences.clear();
		
		for (String m : Marker) {
			
			BasicEvidences.put(m, new Evidence(new HashSet<String>(), 0f));
			BasicEvidences.put(m + "Omega", new Evidence(new HashSet<String>(), 0f));
		}
	}
}
