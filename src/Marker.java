
public abstract class Marker {

	protected float speed;
	protected int pitch;
	protected int intensity;
	
	public String[] getMarkers() {
		
		String[] r = {"speed", "speedOmega", "pitch", "pitchOmega", "intensity", "intensityOmega"};
		
		return r;
	}
	
	public float getSpeed() {
		return speed;
	}
	
	public int getPitch() {
		return pitch;
	}
	
	public int getIntensity() {
		return intensity;
	}
	
}
