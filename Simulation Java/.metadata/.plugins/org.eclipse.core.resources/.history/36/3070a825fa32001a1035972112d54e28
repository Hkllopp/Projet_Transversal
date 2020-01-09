package SimulationFeux;

import java.util.ArrayList;

public class SQLCamionsSurZone {
	
	private String RequeteCamionsSurZone;
	private RequeteBDD outil;
	
	public SQLCamionsSurZone(RequeteBDD outil)
	{
		this.RequeteCamionsSurZone = "SELECT * FROM camions WHERE statut='sur zone'";
		this.outil = outil;
	}
	public void recuperationDonnee()
	{
		this.outil.setRequeteSQL(RequeteCamionsSurZone);
		this.outil.selectBDD();
	}
	public ArrayList<Camion> generationCamionSurZone()
	{
		ArrayList<Camion> listCamionsSurZone = new ArrayList<Camion>();	
		try
		{
			while(this.outil.getResultatSelect().next())
		    {         
				listCamionsSurZone.add(new Camion(new Coordonnee(this.outil.getResultatSelect().getDouble("coordonneeActuelXCamion"),
		        						this.outil.getResultatSelect().getDouble("coordonneeActuelYCamion"))));
		    }
		} catch (Exception e) 
		{
			e.printStackTrace();
		}					
		return listCamionsSurZone;
	}

}
