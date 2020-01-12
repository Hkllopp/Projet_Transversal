package SimulationCamions;

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
	
	public void updateInsertDeleteCamion(String requeteSQL, Camion camion)
	{
		this.outil.setRequeteSQL(requeteSQL +"'" +camion.getIdCamion()+"'");
		this.outil.insertionUpdateDeleteBDD();
	}
	
	public void updateCamion(ArrayList<Camion> listCamion)
	{
		
		for(int i = 0;i<listCamion.size();i++)
		{
			this.outil.setRequeteSQL("UPDATE camions set coordonneeactuelxcamion = "+listCamion.get(i).getPositionActuel().getLatitude()+
					", coordonneeactuelycamion = "+listCamion.get(i).getPositionActuel().getLongitude()+
					", coordonneedestinationxcamion = "+listCamion.get(i).getCoordonneeDestination().getLatitude()+
					", coordonneedestinationycamion = "+listCamion.get(i).getCoordonneeDestination().getLongitude()+
					", statut = '"+listCamion.get(i).getStatut()+
					"' WHERE idCamion = '"+listCamion.get(i).getIdCamion()+"'");
			this.outil.insertionUpdateDeleteBDD();
		}
		
	}
	
	public void updateInsertDeleteCamion(String requeteSQL)
	{
		this.outil.setRequeteSQL(requeteSQL);
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
		        		new Coordonnee(this.outil.getResultatSelect().getDouble("coordonneeactuelxcamion"),
						this.outil.getResultatSelect().getDouble("coordonneeactuelycamion")),
		        		new Coordonnee(this.outil.getResultatSelect().getDouble("coordonneeDepartxcamion"),
		        						this.outil.getResultatSelect().getDouble("coordonneeDepartycamion")),
		        		new Coordonnee(this.outil.getResultatSelect().getDouble("coordonneeDestinationxcamion"),
		        						this.outil.getResultatSelect().getDouble("coordonneeDestinationycamion")),
		        		statutCamion.valueOf(this.outil.getResultatSelect().getString("statut"))));
		        						
		      }
		} catch (Exception e) 
		{
			e.printStackTrace();
		}					
		return listCamions;
	}
	
	public ArrayList<Camion> generationNouveauCamion(ArrayList<Camion> listCamions)
	{	
		int nbOccurenceCamion;
		Camion camionTmp;
		ArrayList<Camion> listCamionsNouveau = new ArrayList<Camion>();
		try
		{
			while(this.outil.getResultatSelect().next())
		      {
				nbOccurenceCamion = 0;
				
				for(int i = 0; i < listCamions.size(); i++)
				{
					if(listCamions.get(i).getIdCamion().equals(this.outil.getResultatSelect().getString("idCamion")))
					{
						nbOccurenceCamion++;
					}
				}
				//System.out.println(nbOccurenceCamion);
				if(nbOccurenceCamion == 0)
				{
					
					camionTmp = new Camion(this.outil.getResultatSelect().getString("idCamion"),
			        		new Coordonnee(this.outil.getResultatSelect().getDouble("coordcasernex"),
	        						this.outil.getResultatSelect().getDouble("coordcaserney")),
	        		new Coordonnee(this.outil.getResultatSelect().getDouble("coordxfeu"),
	        						this.outil.getResultatSelect().getDouble("coordyfeu")),
	        		statutCamion.valueOf("aller"));
					
					listCamionsNouveau.add(camionTmp);
					//listCamions.add(camionTmp);
				
					
				}    						
		      }
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return listCamionsNouveau;
	}
	
	public void inserstionlistCamion(ArrayList<Camion> listCamions)
	{
		for(int i = 0; i < listCamions.size(); i++)
		{
			updateInsertDeleteCamion("INSERT INTO camions VALUES('"+listCamions.get(i).getIdCamion()+"', "
					+listCamions.get(i).getPositionActuel().getLatitude()+", "+listCamions.get(i).getPositionActuel().getLongitude()+", "
					+listCamions.get(i).getCoordonneeDepart().getLatitude()+", "+listCamions.get(i).getCoordonneeDepart().getLongitude()+", "
					+listCamions.get(i).getCoordonneeDestination().getLatitude()+", "+listCamions.get(i).getCoordonneeDestination().getLongitude()+", '"
					+listCamions.get(i).getStatut()+"')");
		}
	}
	
	public int recuperationIntensiteFeuCamion(Camion camion)
	{
		
		this.recuperationCamion("SELECT intensiteFeu FROM Alertes WHERE coordxfeu = "
								+camion.getPositionActuel().getLatitude()+" AND coordyfeu = "
								+camion.getPositionActuel().getLongitude());
		int intensiteFeu = 1;
		try
		{
			if(this.outil.getResultatSelect().next())
		    {
				if(this.outil.getResultatSelect().getInt("IntensiteFeu") == 0)
				{
					intensiteFeu = 0;
				}
			}
			else
			{
				intensiteFeu = 0;
			}
		      
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return intensiteFeu;
	}
	
	
}

/*this.updateInsertDeleteCamion("INSERT INTO camions VALUES('"+camionTmp.getIdCamion()+"', "
		+camionTmp.getCoordonneeDepart().getLatitude()+", "+camionTmp.getCoordonneeDepart().getLongitude()+", "
		+camionTmp.getCoordonneeDepart().getLatitude()+", "+camionTmp.getCoordonneeDepart().getLongitude()+", "
		+camionTmp.getCoordonneeDestination().getLatitude()+", "+camionTmp.getCoordonneeDestination().getLongitude()+", '"
		+camionTmp.getStatut()+"')");*/