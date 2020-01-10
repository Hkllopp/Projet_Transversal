package SimulationFeux;

import java.sql.SQLException;
import java.util.ArrayList;

public class SQLAlerte {
	private String RequeteDeleteAlerte;
	private String RequeteRecuperationAlerte;
	private RequeteBDD outil;
	
	public SQLAlerte(RequeteBDD outil)
	{
		this.RequeteDeleteAlerte = "DELETE FROM Alertes";
		this.RequeteRecuperationAlerte="";
		this.outil = outil;
	}

	public void suppressionTouteLesAlerteDonnee()
	{
		this.outil.setRequeteSQL(RequeteDeleteAlerte);
		this.outil.insertionUpdateBDD();
	}
	
	public int getNbAlerteSurFeu(Feu feu) throws SQLException
	{
		this.RequeteRecuperationAlerte = "SELECT count(idAlerte) FROM Alertes WHERE idAlerte = " + feu.getIdFeu();
		this.outil.setRequeteSQL(RequeteRecuperationAlerte);
		this.outil.selectBDD();
		this.outil.getResultatSelect().next();
		return this.outil.getResultatSelect().getInt("count");
	}
	public void suppressionAlertePrecice(Feu feu)
	{
		this.outil.setRequeteSQL(RequeteDeleteAlerte + " WHERE idalerte = "+feu.getIdFeu());
		this.outil.insertionUpdateBDD();
	}
	public void updateAlertePrecice(Feu feu)
	{
		this.outil.setRequeteSQL("UPDATE alertes set intensitefeu = "+feu.getTaille().getHauteur()+" WHERE idalerte = "+feu.getIdFeu());
		this.outil.insertionUpdateBDD();
	}
	public void insertAlertePrecise(Feu feu, Capteur capteur)
	{
		this.outil.setRequeteSQL("INSERT INTO alertes VALUES("+feu.getIdFeu()+" , "
															  +capteur.getIdCapteur()+", "
															  +feu.getPosition().getLatitude()+", "
															  +feu.getPosition().getLongitude()+", "
															  +feu.getTaille().getHauteur()+")");
		this.outil.insertionUpdateBDD();
	}
	public void recuperationAlerte(String requeteSQL)
	{
		this.outil.setRequeteSQL(requeteSQL);
		this.outil.selectBDD();
	}
	public ArrayList<Feu> generationFeu()
	{
		ArrayList<Feu> listFeu = new ArrayList<Feu>();	
		try
		{
			while(this.outil.getResultatSelect().next())
		      {         
		        listFeu.add(new Feu(this.outil.getResultatSelect().getInt("idalerte"),
		        			new Coordonnee(this.outil.getResultatSelect().getInt("coordXfeu"),
		        						this.outil.getResultatSelect().getInt("coordYfeu")),
		        						new Intensite(this.outil.getResultatSelect().getInt("intensitefeu"))));
		      }
		} catch (Exception e) 
		{
			e.printStackTrace();
		}					
		return listFeu;
	}
}
