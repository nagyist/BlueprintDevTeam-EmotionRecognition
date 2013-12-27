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
		
		float correction;
		
		resetEvidences();
		generateBasicEvidences(m);
		
		for (int i = 1; i < Marker.length; i++) {
			
			if (i == 1) {
				
				EvidenceMatrix.add(new Evidence(BasicEvidences.get(Marker[i-1]).getEmotionnames(), 
						BasicEvidences.get(Marker[i-1]).getWeight()));
				EvidenceMatrix.add(new Evidence(BasicEvidences.get(Marker[i-1] + "Omega").getEmotionnames(), 
						BasicEvidences.get(Marker[i-1] + "Omega").getWeight()));
			}
			
			String[] s = {Marker[i], Marker[i] + "Omega"};
			EvidenceMatrix = combineEvidences(s, EvidenceMatrix);
		}
		
//		for (Evidence evidence : EvidenceMatrix) {
//			 
//			System.out.println(evidence.getEmotionnames() + "\t" + evidence.getWeight());
//		}
		
		correction = calcCorrection();
		System.out.println("Correction: " + correction);
		for (Emotion e : EmotionManager.getEmotions()) {
		
			System.out.println(e.Name + ": " + calcPlausibility(e.Name, correction));
		}
	}
	
	public float calcPlausibility (String en, float c) {
		
		float p = 0f;
		
		for (Evidence e : EvidenceMatrix) {
			
			if(e.getEmotionnames().contains(en)) p += e.getWeight();
		}
		
		p = p * c;
		
		return p;
	}
	
	public float calcCorrection () {
		
		float c = 0f;
		
		for (Evidence e : EvidenceMatrix) {
			
			if (e.getEmotionnames().isEmpty()) {
				
				c += e.getWeight();
			}
		}
		
		c = 1/(1-c);
		
		return c;
	}
	
	public Set<Evidence> combineEvidences (String[] e1, Set<Evidence> row) {
		
		Set<Evidence> col = new HashSet<Evidence>(),
					  ec = new HashSet<Evidence>();
		
		Set<String> intersection = new HashSet<String>();

		for (String s : e1)	col.add(BasicEvidences.get(s));
		
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
