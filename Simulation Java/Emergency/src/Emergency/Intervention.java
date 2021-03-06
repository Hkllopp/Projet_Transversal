package Emergency;

public class Intervention {
	private Camion camion;
	private Alerte alerte;
	private static int nbIntervention = 0;
	private int idIntervention;
	
	public Intervention(Camion camion, Alerte alerte)
	{
		nbIntervention++;
		this.camion = camion;
		this.alerte = alerte;
		this.idIntervention = nbIntervention;
	}
	
	public Alerte getAlerte()
	{
		return this.alerte;
	}
	
	public Camion getCamion()
	{
		return this.camion;
	}
	public int getIdIntervention()
	{
		return this.idIntervention;
	}

}
