import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EvidenceManager {

	private EmotionManager EmotionManager;
	private MeasurementManager MeasurementManager;
	private Map<String, Evidence> BasicEvidences;
	private Set<Evidence> EvidenceMatrix;
	private int Tolerance;
	private String[] Marker;
	private float Correction;
	
	public EvidenceManager(int a) {
	
		EmotionManager = new EmotionManager();
		MeasurementManager = new MeasurementManager();
		
		BasicEvidences = new HashMap<String, Evidence>();
		EvidenceMatrix = new HashSet<Evidence>();
		Marker = new Emotion("dummy", 0f, 0, 0).getMarkers();
		
		Tolerance = a;
		Correction = 1.0f;
	}
	
	public void dempsterShaferDetailed (Measurement m) {
		
		dempsterShaferSetUp(m);
		
		System.out.println(String.format("Frame %d: Speed = %.1f; Pitch = %d; Intensity = %d\n", 
				m.ID, m.Speed, m.Pitch, m.Intensity));
		
		System.out.println("correction: " + Correction);
		for (Emotion e : EmotionManager.getEmotions()) {
		
			System.out.println(String.format("%s: %1.4f", e.getName(), calcPlausibility(e.getName(), Correction)));
		}
	}
	
	public void dempsterShaferForAll (List<Measurement> measurements) {
		
		float p;
		String emotionsForP, print;
		Map<String, Integer> occurences = new HashMap<String, Integer>();
		
		for (Emotion emotion : EmotionManager.getEmotions()) {
			
			occurences.put(emotion.getName(), 0);
		}
		
		System.out.println("Frame-ID - Emotions - Plausibility");
		
		for (Measurement m : measurements) {
			
			dempsterShaferSetUp(m);
			p = 0f;
			emotionsForP = null;
			print = null;
			
			for (Emotion e : EmotionManager.getEmotions()) {
				
				if (calcPlausibility(e.getName(), Correction) > p) {
					
					p = calcPlausibility(e.getName(), Correction);
					emotionsForP = e.getName();
				} else if (calcPlausibility(e.getName(), Correction) == p) {
				
					emotionsForP += "," + e.getName();
				}
			}
			
			print = String.format(
				"%2d %s %f", m.ID, String.format("%-38s", emotionsForP).replace(" ", "."), p
			);
			
			for (String emotionname : emotionsForP.split(",")) {
				
				occurences.put(emotionname, occurences.get(emotionname) + 1);
			}
			
			System.out.println(print);
		}
		
		System.out.println("\nStatistics (abolute & relative):");
		
		for (Emotion emotion : EmotionManager.getEmotions()) {
			
			System.out.println(String.format("%-7s %2d (%3.0f%%)", emotion.getName(), 
					occurences.get(emotion.getName()), 
					((float) occurences.get(emotion.getName())/ (float) measurements.size())*100));
		}
	}
	
	public void dempsterShaferProveEmotion (List<Measurement> measurements, String name) {
		
		if (!EmotionManager.containsEmotion(name)) throw new IllegalArgumentException();
		
		float p, average = 0;
		
		System.out.println("Plausibility for '" + name + "' per frame:");
		
		for (Measurement m : measurements) {
			
			dempsterShaferSetUp(m);
			p = calcPlausibility(name, Correction);
			System.out.println(String.format("%2d. %1.4f", m.ID, p));
			average += p;
		}
		
		average = average / measurements.size();
		System.out.println(String.format("\nAverage Plausibility: %1.4f%%", average));
	}
	
	public void dempsterShaferSetUp (Measurement m) {
		
		resetEvidences();
		generateBasicEvidences(m);
		
		EvidenceMatrix.add(new Evidence(BasicEvidences.get(Marker[0]).getEmotionnames(), 
				BasicEvidences.get(Marker[0]).getWeight()));
		EvidenceMatrix.add(new Evidence(BasicEvidences.get(Marker[0] + "Omega").getEmotionnames(), 
				BasicEvidences.get(Marker[0] + "Omega").getWeight()));
		
		for (int i = 1; i < Marker.length; i++) {
						
			EvidenceMatrix = combineEvidences(Marker[i], EvidenceMatrix);
		}
		
		calcCorrection();
	}
	
	public float calcPlausibility (String emotionname, float c) {
		
		float p = 0f;
		
		for (Evidence e : EvidenceMatrix) {
			
			if(e.getEmotionnames().contains(emotionname)) p += e.getWeight();
		}
		
		p = p * c;
		
		return p;
	}
	
	public void calcCorrection () {
		
		float c = 0f;
		
		for (Evidence e : EvidenceMatrix) {
			
			if (e.getEmotionnames().isEmpty()) {
				
				c += e.getWeight();
			}
		}
		
		Correction = 1/(1-c);
	}
	
	public Set<Evidence> combineEvidences (String markername, Set<Evidence> row) {
		
		Set<Evidence> column = new HashSet<Evidence>(),
					  combined_evidences = new HashSet<Evidence>();
		Set<String> intersection_emotionnames;

		column.add(BasicEvidences.get(markername));
		column.add(BasicEvidences.get(markername + "Omega"));
		
		for (Evidence e1 : column) {
			
			for (Evidence e2 : row) {
				
				intersection_emotionnames = new HashSet<String>(e1.getEmotionnames());
				intersection_emotionnames.retainAll(e2.getEmotionnames());
				combined_evidences.add(new Evidence(intersection_emotionnames, 
													e1.getWeight() * e2.getWeight()));
			}
		}
		
		return combined_evidences;
	}
	
	public void generateBasicEvidences (Measurement m) {
		
		for (String marker : Marker) {
			
			for (Emotion e : EmotionManager.getEmotions()) {
			
				if( e.getAttributeValue(marker) <= m.getAttributeValue(marker) + Tolerance &&
						e.getAttributeValue(marker) >= m.getAttributeValue(marker) - Tolerance) {
					
					BasicEvidences.get(marker).addEmotionname(e.getName());
				}
				
				BasicEvidences.get(marker + "Omega").addEmotionname(e.getName());
			}
			
			BasicEvidences.get(marker).setWeight(m.getWeight(marker));
			BasicEvidences.get(marker + "Omega").setWeight(1 - m.getWeight(marker));
		}
	} 
	
	public void resetEvidences () {
	
		BasicEvidences.clear();
		EvidenceMatrix.clear();
		Correction = 1.0f;
		
		for (String m : Marker) {
			
			BasicEvidences.put(m, new Evidence(new HashSet<String>(), 0f));
			BasicEvidences.put(m + "Omega", new Evidence(new HashSet<String>(), 0f));
		}
	}
	
	public float getTolerance () {
		
		return Tolerance;
	}
	
	public void setTolerance (int t) {
		
		if (t > 3 || t < 0)	throw new IllegalArgumentException();
		else					Tolerance = t;
	}

	public MeasurementManager getMeasurementManager() {
		return MeasurementManager;
	}

	public EmotionManager getEmotionManager() {
		return EmotionManager;
	}
}
