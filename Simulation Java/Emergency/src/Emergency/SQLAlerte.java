package Emergency;

import java.sql.SQLException;
import java.util.ArrayList;

public class SQLAlerte {
	
	private RequeteBDD outil;
	
	public SQLAlerte(RequeteBDD outil)
	{
		this.outil = outil;
	}
	
	public void recuperationAlerte(String requeteSQL)
	{
		this.outil.setRequeteSQL(requeteSQL);
		this.outil.selectBDD();
	}
	
	public void suppressionAlerteDonnee(String requeteSQL)
	{
		this.outil.setRequeteSQL(requeteSQL);
		this.outil.insertionUpdateDeleteBDD();
	}
	
	public ArrayList<Alerte> generationAlerte()
	{
		ArrayList<Alerte> listAlertes = new ArrayList<Alerte>();	
		try
		{
			while(this.outil.getResultatSelect().next())
		      {         
				listAlertes.add(new Alerte(this.outil.getResultatSelect().getInt("idalerte"),
		        		new Coordonnee(this.outil.getResultatSelect().getInt("coordXfeu"),
		        						this.outil.getResultatSelect().getInt("coordYfeu")), 
		        						new Intensite(this.outil.getResultatSelect().getInt("intensitefeu"))));
		      }
		} catch (Exception e) 
		{
			e.printStackTrace();
		}					
		return listAlertes;
	}
	
}