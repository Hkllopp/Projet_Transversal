package Emergency;

import java.util.ArrayList;


public class SQLCamion 
{
	
	private RequeteBDD outil;
	private String requeteCamionIntervention;
	
	public SQLCamion(RequeteBDD outil)
	{
		this.outil = outil;
	}
	
	public String getRequeteCamionIntervention() 
	{
		return this.requeteCamionIntervention;
	}
	public void setRequeteCamionIntervention(Intervention intervention) 
	{
		this.requeteCamionIntervention = "SELECT * FROM camions WHERE idCamion = "+intervention.getCamion().getIdCamion()+
																				" AND statut = "+statutCamion.valueOf("enAttenteCaserne");
	}
	
	public void recuperationCamion(String requeteSQL)
	{
		this.outil.setRequeteSQL(requeteSQL);
		this.outil.selectBDD();
	}
	
	public void updateCamion(String requeteUpdate, Camion camion)
	{
		this.outil.setRequeteSQL(requeteUpdate +"'" +camion.getIdCamion()+"'");
		this.outil.insertionUpdateDeleteBDD();
	}
	
	public ArrayList<Camion> generationCamion()
	{
		ArrayList<Camion> listCamions = new ArrayList<Camion>();	
		try
		{
			while(this.outil.getResultatSelect().next())
		      {         
		        listCamions.add(new Camion(this.outil.getResultatSelect().getString("idCamion"),
		        		new Coordonnee(this.outil.getResultatSelect().getInt("coordonneeactuelxcamion"),
		        						this.outil.getResultatSelect().getInt("coordonneeactuelycamion")),
		        		statutCamion.valueOf(this.outil.getResultatSelect().getString("statut"))));
		        						
		      }
		} catch (Exception e) 
		{
			e.printStackTrace();
		}					
		return listCamions;
	}
	
	
}
