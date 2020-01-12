package SimulationCamions;

import java.util.ArrayList;


public class SQLIntervention {
	
	private RequeteBDD outil;
	
	public SQLIntervention(RequeteBDD outil)
	{
		
		this.outil = outil;
	}
	
	public void recuperationIntervention(String requeteSQL)
	{
		this.outil.setRequeteSQL(requeteSQL);
		this.outil.selectBDD();
	}
	
	
	public ArrayList<Intervention> generationIntervention(ArrayList<Camion> listCamions)
	{
		ArrayList<Intervention> listInterventions = new ArrayList<Intervention>();
		Camion camionTmp = null;
		try
		{
			while(this.outil.getResultatSelect().next())
		      {
				for(int i = 0;i<listCamions.size();i++)
				{
					if(listCamions.get(i).getIdCamion().equals(this.outil.getResultatSelect().getString("idCamion")))
					{
						camionTmp = listCamions.get(i);	
					}
				}
				if(camionTmp != null)
				{
					listInterventions.add(new Intervention(camionTmp));
				}
				
		      }
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		
		
		return listInterventions;
	}

}
