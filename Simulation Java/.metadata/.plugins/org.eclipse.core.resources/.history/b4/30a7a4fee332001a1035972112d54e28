package Emergency;

import java.util.ArrayList;

public class Caserne {
	private String idCaserne;
	private ArrayList<Camion> camionDispo;
	private ArrayList<Camion> camionEnIntervention;
	
	public Caserne(ArrayList<Camion> camionDispo, String idCaserne)
	{
		this.idCaserne = idCaserne;
		this.camionDispo = camionDispo;
		this.camionEnIntervention = new ArrayList<Camion>();
	}
	
	public Caserne(String idCaserne)
	{
		this.idCaserne = idCaserne;
		this.camionDispo = null;
		this.camionEnIntervention = null;
	}
	
	public ArrayList<Camion> getCamionDispo()
	{
		return this.camionDispo;
	}
	
	public void setCamionDispo(ArrayList<Camion> camionDispo)
	{
		this.camionDispo = camionDispo;
	}
	
	public void setCamionEnIntervention(ArrayList<Camion> camionEnIntervention)
	{
		this.camionEnIntervention = camionEnIntervention;
	}
	
	public void faireSortirCamion(Camion camion) 
	{
		this.camionDispo.remove(camion);
		this.camionEnIntervention.add(camion);
	}
	
}
