import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


public class MeasurementManager {

	private List<Measurement> measurements;
	private float weightSpeed, weightPitch, weightIntensity;

	public MeasurementManager () {
		
		measurements = new LinkedList<Measurement>();
		weightSpeed = 0.8f;
		weightPitch = 0.6f;
		weightIntensity = 0.7f; 
	}
	
	public MeasurementManager (float s, float p, float i) {
		
		measurements = new LinkedList<Measurement>();
		weightSpeed = s;
		weightPitch = p;
		weightIntensity = i;
	}
	
	public void measurementReader(String filename) {
		
		BufferedReader br = null;
		
		String line = "";
		String[] value = null;
		
		try {
			 
			br = new BufferedReader(new FileReader(filename));
			br.readLine();
			
			while ((line = br.readLine()) != null) {
	 
				value = line.split(";");
				if (line.length() > 4) {
					
					//TODO: Throw a "Wrong CSV Format" Exception
				}
				
				measurements.add(new Measurement(
						Integer.parseInt(value[0]), 
						Float.parseFloat(value[1].replaceAll(",", ".")),
						wordToScale(value[2]), 
						wordToScale(value[3])
				));
			}
	 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public int wordToScale (String w) {
		
		int r = 0;
		
		if (w.equals("sehr niedrig"))	r = 1;
		if (w.equals("niedrig"))		r = 2;
		if (w.equals("normal"))			r = 3;
		if (w.equals("hoch"))			r = 4;
		if (w.equals("sehr hoch"))		r = 5;

		return r;
			
	}
	
	public void addMeasurement (Measurement m) {
		
		measurements.add(m);
	}
	
	public void removeMeasurement (int id) {
		
		for (Measurement m : measurements) {
			
			if (m.id == id) {
				
				measurements.remove(m);
			}
		}
	}
	
	public Measurement getMeasurement(int index) {
		return measurements.get(index);
	}
	
	public float getWeightSpeed() {
		return weightSpeed;
	}

	public float getWeightPitch() {
		return weightPitch;
	}

	public float getWeightIntensity() {
		return weightIntensity;
	}
}
