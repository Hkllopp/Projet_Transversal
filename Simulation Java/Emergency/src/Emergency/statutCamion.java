package Emergency;

public enum statutCamion {
	disponible("Disponible"),
	aller("En route vers une intervention"),
	retour("De retour d'une intervention"),
	surZone("Sur zone"),
	enAttenteCaserne("En attente de rentrer dans la caserne");
	
	private String statut = "";
	
	statutCamion(String statut)
	{
		this.statut = statut;
	}
	
	public String ToString()
	{
		return statut;
	}

}
