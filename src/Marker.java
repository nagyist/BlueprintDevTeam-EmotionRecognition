
public abstract class Marker {

	protected float Speed;
	protected int Pitch;
	protected int Intensity;
	
	public String[] getMarkers() {
		
		String[] r = {"Speed", "Pitch", "Intensity"};
		
		return r;
	}

	public float getSpeed() {
		return Speed;
	}

	public void setSpeed(float speed) {
		Speed = speed;
	}

	public int getPitch() {
		return Pitch;
	}

	public void setPitch(int pitch) {
		Pitch = pitch;
	}
	
	public int getIntensity() {
		return Intensity;
	}

	public void setIntensity(int intensity) {
		Intensity = intensity;
	}
}
