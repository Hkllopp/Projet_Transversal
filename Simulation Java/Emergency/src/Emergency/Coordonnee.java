package Emergency;

public class Coordonnee {
	
	private double longitude;
	private double latitude;
	
	public Coordonnee(double longitude, double latitude)
	{
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
	public double getLongitude()
	{
		return this.longitude;
	}
	public void setLongitude(int longitude)
	{
		this.longitude = longitude;
	}
	public double getLatitude()
	{
		return this.latitude;
	}
	public void setLatitude(int latitude)
	{
		this.latitude = latitude;
	}
	public boolean equals(Object obj)
	{
		return (obj instanceof Coordonnee) && Math.round(this.longitude) == Math.round(((Coordonnee)obj).getLongitude())
				&& Math.round(this.latitude) == Math.round(((Coordonnee)obj).getLatitude());	
	}

}
