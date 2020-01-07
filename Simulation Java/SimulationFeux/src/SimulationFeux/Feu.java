package SimulationFeux;

public class Feu {
	private Coordonnee position;
	private Intensite taille;
	
	public Feu(Coordonnee position, Intensite taille)
	{
		this.position = position;
		this.taille = taille;
	}
	public Coordonnee getPosition()
	{
		return this.position;
	}
	public void setPosition(Coordonnee position)
	{
		this.position = position;
	}
	public Intensite getTaille()
	{
		return this.taille;
	}
	public void setTaille(Intensite taille)
	{
		this.taille = taille;
	}

}
