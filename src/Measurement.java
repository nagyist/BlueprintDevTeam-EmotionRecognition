import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;


public class Measurement {

	public int beat;
	public float speed;
	public int pitch;
	public int intensity; 

	public Measurement (int b, float s, int p, int i) {

		this.beat = b;
		this.speed = s;
		this.pitch = p;
		this.intensity = i;
	}
	
	@SuppressWarnings("null")
	public List<Measurement> measurementReader(String filename) {
		
		BufferedReader br = null;
		List<Measurement> m = null;
		String line = "";
		String[] value = null;
		
		try {
			 
			br = new BufferedReader(new FileReader(filename));
			
			while ((line = br.readLine()) != null) {
	 
				value = line.split(";");
				m.add(new Measurement(Integer.parseInt(value[0]), Float.parseFloat(value[1]), 
						Integer.parseInt(value[2]), Integer.parseInt(value[3])));
			    //System.out.println(line);
	 
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
	 
		return m;
	}
}
