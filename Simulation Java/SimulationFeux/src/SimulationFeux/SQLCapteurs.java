package SimulationFeux;

import java.util.ArrayList;

public class SQLCapteurs {
	
	private String RequeteCapteur;
	private RequeteBDD outil;
	
	public SQLCapteurs(RequeteBDD outil)
	{
		this.RequeteCapteur = "SELECT * FROM capteurs";
		this.outil = outil;
	}
	public void recuperationDonnee()
	{
		this.outil.setRequeteSQL(RequeteCapteur);
		this.outil.selectBDD();
	}
	public ArrayList<Capteur> generationCapteur()
	{
		ArrayList<Capteur> listCapteurs = new ArrayList<Capteur>();	
		try
		{
			while(this.outil.getResultatSelect().next())
		      {         
		        listCapteurs.add(new Capteur(this.outil.getResultatSelect().getInt("idCapteur"),
		        		new Coordonnee(this.outil.getResultatSelect().getInt("coordXCapteur"),
		        						this.outil.getResultatSelect().getInt("coordYCapteur")),
		        						this.outil.getResultatSelect().getBoolean("activation")));
		      }
		} catch (Exception e) 
		{
			e.printStackTrace();
		}					
		return listCapteurs;
	}
}
