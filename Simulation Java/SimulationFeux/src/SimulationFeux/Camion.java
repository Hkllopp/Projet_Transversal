package SimulationFeux;

public class Camion {
	private Coordonnee positionActuel;
	
	public Camion()
	{
		this.positionActuel = new Coordonnee(0, 0);
	}
	public Camion(Coordonnee positionActuel)
	{
		this.positionActuel = positionActuel;
	}
	public Coordonnee getPositionActuel()
	{
		return this.positionActuel;
	}
	public void setHauteur(Coordonnee positionActuel)
	{
		this.positionActuel = positionActuel;
	}

}
