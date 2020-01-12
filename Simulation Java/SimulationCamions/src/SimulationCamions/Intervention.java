package SimulationCamions;

public class Intervention {
	private Camion camion;
	private static int nbIntervention = 0;
	private int idIntervention;
	
	public Intervention(Camion camion)
	{
		nbIntervention++;
		this.camion = camion;
		this.idIntervention = nbIntervention;
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
