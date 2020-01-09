package Emergency;

public class Alerte {
	
	private Coordonnee position;
	private Intensite taille;
	private int idAlerte;
	
	public Alerte(int idAlerte, Coordonnee position, Intensite taille)
	{
		this.idAlerte =idAlerte;
		this.position = position;
		this.taille = taille;	
	}
	public int getIdAlerte()
	{
		return this.idAlerte;
	}
	public Coordonnee getPosition()
	{
		return this.position;
	}

}
