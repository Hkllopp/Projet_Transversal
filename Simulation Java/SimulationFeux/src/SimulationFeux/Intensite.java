package SimulationFeux;

public class Intensite {
	private int hauteur;
	
	public Intensite(int hauteur)
	{
		this.hauteur = hauteur;
	}
	public int getHauteur()
	{
		return this.hauteur;
	}
	public void setHauteur(int hauteur)
	{
		this.hauteur = hauteur;
	}
	public void baisseHauteur(int diminution)
	{
		this.hauteur-= diminution;
	}
	public void augmenterHauteur(int augmentation)
	{
		this.hauteur+= augmentation;
	}
	

}
