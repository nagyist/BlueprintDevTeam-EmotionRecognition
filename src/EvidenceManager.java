import java.lang.reflect.InvocationTargetException;
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
	private float Accuracy;
	private String[] Marker;
	private float Correction;
	
	public EvidenceManager(int a) {
	
		EmotionManager = new EmotionManager();
		MeasurementManager = new MeasurementManager();
		BasicEvidences = new HashMap<String, Evidence>();
		EvidenceMatrix = new HashSet<Evidence>();
		Accuracy = a;
		Marker = new Emotion("dummy", 0f, 0, 0).getMarkers();
		Correction = 1.0f;
	}
	
	public void dempsterShaferDetailed (Measurement m) throws 	SecurityException, 
																IllegalArgumentException, 
																NoSuchMethodException, 
																IllegalAccessException, 
																InvocationTargetException, 
																NoSuchFieldException {
		
		dempsterShaferSetUp(m);
		
		System.out.println("correction: " + Correction);
		for (Emotion e : getEmotionManager().getEmotions()) {
		
			System.out.println(e.getName() + ": " + calcPlausibility(e.getName(), Correction));
		}
	}
	
	public void dempsterShaferForAll (List<Measurement> measurements) throws SecurityException, 
																			IllegalArgumentException, 
																			NoSuchMethodException, 
																			IllegalAccessException, 
																			InvocationTargetException, 
																			NoSuchFieldException {
		
		float p;
		String emotionsForP, print;
		Map<String, Integer> occurences = new HashMap<String, Integer>();
		
		for (Emotion emotion : getEmotionManager().getEmotions()) occurences.put(emotion.getName(), 0);
		
		System.out.println("Frame-ID - Emotions - Plausibility");
		
		for (Measurement m : measurements) {
			
			dempsterShaferSetUp(m);
			p = 0f;
			emotionsForP = null;
			print = null;
			
			for (Emotion e : getEmotionManager().getEmotions()) {
				
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
		
		System.out.println("\nStatistics:");
		
		for (Emotion emotion : getEmotionManager().getEmotions()) {
			
			System.out.println(String.format("%-7s %2d (%3.0f%%)", emotion.getName(), 
					occurences.get(emotion.getName()), 
					((float) occurences.get(emotion.getName())/ (float) measurements.size())*100));
		}
	}
	
	public void dempsterShaferProveEmotion (List<Measurement> measurements, String name) 
																	throws	SecurityException, 
																			IllegalArgumentException, 
																			NoSuchMethodException, 
																			IllegalAccessException, 
																			InvocationTargetException, 
																			NoSuchFieldException {
		
		if (!EmotionManager.containsEmotion(name)) throw new IllegalArgumentException();
		
		float p;
		
		System.out.println("Plausibility for " + name + " per frame:");
		
		for (Measurement m : measurements) {
			
			dempsterShaferSetUp(m);
			p = calcPlausibility(name, Correction);
			System.out.println(String.format("%2d...%1.4f", m.ID, p));
		}
	}
	
	public void dempsterShaferSetUp (Measurement m) throws	SecurityException, 
															IllegalArgumentException, 
															NoSuchMethodException, 
															IllegalAccessException, 
															InvocationTargetException, 
															NoSuchFieldException {
		
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
		
		Correction = calcCorrection();
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
	
	public void generateBasicEvidences (Measurement m) throws	SecurityException, 
																NoSuchMethodException, 
																IllegalArgumentException, 
																IllegalAccessException, 
																InvocationTargetException, 
																NoSuchFieldException {
		
		for (String marker : Marker) {
			
			for (Emotion e : getEmotionManager().getEmotions()) {
			
				if( e.getAttributeValue(marker) <= m.getAttributeValue(marker) + Accuracy &&
						e.getAttributeValue(marker) >= m.getAttributeValue(marker) - Accuracy) {
					
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
	
	public float getAccuracy () {
		
		return Accuracy;
	}
	
	public void setAccuracy (float acc) {
		
		if (acc > 3 || acc < 0)	throw new IllegalArgumentException();
		else					Accuracy = acc;
	}

	public MeasurementManager getMeasurementManager() {
		return MeasurementManager;
	}

	public EmotionManager getEmotionManager() {
		return EmotionManager;
	}
}
