package SimulationCamions;

public class Camion {
	private String idCamion;
	//private String idCaserne;
	private Coordonnee positionActuel;
	private Coordonnee coordonneeDepart;
	private Coordonnee coordonneeDestination;
	private statutCamion statut;
	
	public Camion()
	{
		this.positionActuel = new Coordonnee(0, 0);
		this.statut = statutCamion.disponible;
	}
	public Camion(String idCamion, Coordonnee coordonneeDepart, Coordonnee coordonneeDestination, statutCamion statut)
	{
		this.idCamion = idCamion;
		this.positionActuel = coordonneeDepart;
		this.coordonneeDepart = coordonneeDepart;
		this.coordonneeDestination = coordonneeDestination;
		this.statut = statut;
	}
	
	public Camion(String idCamion, Coordonnee positionActuel, Coordonnee coordonneeDepart, Coordonnee coordonneeDestination, statutCamion statut)
	{
		this.idCamion = idCamion;
		this.positionActuel = positionActuel;
		this.coordonneeDepart = coordonneeDepart;
		this.coordonneeDestination = coordonneeDestination;
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
	public Coordonnee getCoordonneeDepart() {
		return this.coordonneeDepart;
	}
	public void setCoordonneeDepart(Coordonnee coordonneeDepart) {
		this.coordonneeDepart = coordonneeDepart;
	}
	public Coordonnee getCoordonneeDestination() {
		return this.coordonneeDestination;
	}
	public void setCoordonneeDestination(Coordonnee coordonneeDestination) {
		this.coordonneeDestination = coordonneeDestination;
	}
	public void setIdCamion(String idCamion) {
		this.idCamion = idCamion;
	}
	public void setPositionActuel(Coordonnee positionActuel) {
		this.positionActuel = positionActuel;
	}
	
	public void checkArriver()
	{
		if(this.positionActuel.equals(this.coordonneeDestination) && this.statut == statutCamion.valueOf("aller"))
		{
			this.statut = statutCamion.valueOf("surZone");
		}
		
	}
	public void seDeplacer()
	{
		if(this.statut == statutCamion.valueOf("aller") || this.statut == statutCamion.valueOf("retour"))
		{
			this.positionActuel.reductionDistance(this.coordonneeDestination);
		}
	}
}
