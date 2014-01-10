
public abstract class Marker {

	protected float Speed;
	protected int Pitch;
	protected int Intensity;
	
	public String[] getMarkers() {
		
		String[] r = {"Speed", "Pitch", "Intensity"};
		
		return r;
	}

	public Float getWeight (String marker) {
		
		float w = 0f;
		
		if (marker.equals("Speed"))	w = 0.8f;
		if (marker.equals("Pitch"))	w = 0.6f;
		if (marker.equals("Intensity"))	w = 0.7f;
		
		return w;
	}
	
	public float getAttributeValue(String marker) {
		
		float f = 0f;
		
		if(marker.equals("Speed"))	f = getSpeed();
		if(marker.equals("Pitch"))	f = getPitch();
		if(marker.equals("Intensity")) f = getIntensity();
		
		return f;
	}
	
	public float getSpeed() {
		return Speed;
	}

	public int getPitch() {
		return Pitch;
	}
	
	public int getIntensity() {
		return Intensity;
	}
}
