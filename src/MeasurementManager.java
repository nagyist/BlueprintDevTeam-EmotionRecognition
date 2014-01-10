import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


public class MeasurementManager {

	private List<Measurement> Measurements;

	public MeasurementManager () {
		
		Measurements = new LinkedList<Measurement>();
	}
	
	public void measurementReader(String filename) throws IOException {
		
		BufferedReader br = null;
		
		String line = "";
		String[] value = null;
		
		Measurements.clear();
		
		br = new BufferedReader(new FileReader(filename));
		br.readLine();
		
		while ((line = br.readLine()) != null) {
 
			value = line.split(";");
				
			Measurements.add(new Measurement(
		
				Integer.parseInt(value[0]), 
				Float.parseFloat(value[1].replaceAll(",", ".")),
				wordToScale(value[2]), 
				wordToScale(value[3])
			));
			
		}
		
		br.close();
	}
	
	public int wordToScale (String w) {
		
		if (w.equalsIgnoreCase("sehr niedrig")	|| w.equalsIgnoreCase("very low"))	return 1;
		if (w.equalsIgnoreCase("niedrig") 		|| w.equalsIgnoreCase("low"))		return 2;
		if (w.equalsIgnoreCase("normal"))											return 3;
		if (w.equalsIgnoreCase("hoch") 			|| w.equalsIgnoreCase("high"))		return 4;
		if (w.equalsIgnoreCase("sehr hoch") 	|| w.equalsIgnoreCase("very high"))	return 5;
		else throw new IllegalArgumentException();
	}
	
	public void addMeasurement (Measurement m) {
		
		Measurements.add(m);
	}
	
	public void removeMeasurement (int id) {
		
		for (Measurement m : Measurements) {
			
			if (m.ID == id) {
				
				Measurements.remove(m);
			}
		}
	}
	
	public Measurement getMeasurement(int index) {
		
		return Measurements.get(index);
	}
	
	public List<Measurement> getMeasurements () {
		
		return Measurements;
	}
}
