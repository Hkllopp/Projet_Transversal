package SimulationCamions;

public class Coordonnee {
	
	private double longitude;
	private double latitude;
	
	public Coordonnee(double latitude, double longitude)
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
		return (obj instanceof Coordonnee) && this.longitude == ((Coordonnee)obj).getLongitude()
				&& this.latitude == ((Coordonnee) obj).getLatitude();
	}
	
	public void reductionDistance(Coordonnee coordonnee)
	{
		if(this.latitude < coordonnee.getLatitude() && Math.abs(this.latitude - coordonnee.getLatitude()) >= 0.5)
		{
			this.latitude += 0.5;
			//System.out.println("1" );//+this.latitude);
			
		}
		
		else if(this.latitude > coordonnee.getLatitude() && Math.abs(this.latitude - coordonnee.getLatitude()) >= 0.5)
		{
			this.latitude -= 0.5;
			//System.out.println("2");
		}
		
		else if(Math.abs(this.latitude - coordonnee.getLatitude()) < 0.5)
		{
			this.latitude = coordonnee.getLatitude();
			//System.out.println("3");
		}
		
		if(this.longitude < coordonnee.getLongitude() && Math.abs(this.longitude - coordonnee.getLongitude()) >= 0.5)
		{
			this.longitude += 0.5;
			//System.out.println("4");
		}
		
		else if(this.longitude > coordonnee.getLongitude() && Math.abs(this.longitude - coordonnee.getLongitude()) >= 0.5)
		{
			this.longitude -= 0.5;
			//System.out.println("5");
		}
		
		else if(Math.abs(this.longitude - coordonnee.getLongitude()) < 0.5)
		{
			this.longitude = coordonnee.getLongitude();
			//System.out.println("6");
		}
		
		
	}
}