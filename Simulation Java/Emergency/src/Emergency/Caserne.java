package Emergency;

import java.util.ArrayList;

public class Caserne {
	
	private String idCaserne;
	private ArrayList<Camion> camionDispo;
	private ArrayList<Camion> camionEnIntervention;
	
	public Caserne(ArrayList<Camion> camion, String idCaserne)
	{
		this.idCaserne = idCaserne;
		this.camionDispo = new ArrayList<Camion>();
		this.camionEnIntervention = new ArrayList<Camion>();
		
		for(int i = 0; i < camion.size(); i++)
		{
			if(camion.get(i).getStatut() == statutCamion.valueOf("disponible"))
			{
				this.camionDispo.add(camion.get(i));
			}
			else
			{
				this.camionEnIntervention.add(camion.get(i));
			}
			
		}
		
		
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
	public void faireRentrerCamion(Camion camion)
	{
		this.camionDispo.add(camion);
		this.camionEnIntervention.remove(camion);
	}

	public String getIdCaserne() {
		return this.idCaserne;
	}

	public void setIdCaserne(String idCaserne) {
		this.idCaserne = idCaserne;
	}

	public ArrayList<Camion> getCamionEnIntervention() {
		return this.camionEnIntervention;
	}
	
	
	
}
