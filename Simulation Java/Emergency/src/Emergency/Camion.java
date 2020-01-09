package Emergency;

public class Camion {
	private String idCamion;
	//private String idCaserne;
	private Coordonnee positionActuel;
	private statutCamion statut;
	
	public Camion()
	{
		this.positionActuel = new Coordonnee(0, 0);
		this.statut = statutCamion.disponible;
	}
	public Camion(String idCamion, Coordonnee positionActuel, statutCamion statut)
	{
		this.idCamion = idCamion;
		this.positionActuel = positionActuel;
		this.statut = statut;
	}
	public Coordonnee getPositionActuel()
	{
		return this.positionActuel;
	}
	public void setHauteur(Coordonnee positionActuel)
	{
		this.positionActuel = positionActuel;
	}
	public String getIdCamion()
	{
		return this.idCamion;
	}
	public statutCamion getStatut() {
		return this.statut;
	}
	public void setStatut(statutCamion statut) {
		this.statut = statut;
	}
	
}
