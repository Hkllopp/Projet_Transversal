package SimulationFeux;

import java.sql.SQLException;

public class SQLAlerte {
	private String RequeteDeleteAlerte;
	private String RequeteRecuperationAlerte;
	private RequeteBDD outil;
	
	public SQLAlerte(RequeteBDD outil)
	{
		this.RequeteDeleteAlerte = "DELETE FROM Alertes";
		this.RequeteRecuperationAlerte="SELECT count(idAlerte) FROM Alertes WHERE idAlerte =";
		this.outil = outil;
	}

	public void suppressionDonnee()
	{
		this.outil.setRequeteSQL(RequeteDeleteAlerte);
		this.outil.insertionUpdateBDD();
	}
	
	public int getNbAlerteSurFeu(int idFeu) throws SQLException
	{
		this.RequeteRecuperationAlerte = this.RequeteRecuperationAlerte + idFeu;
		this.outil.setRequeteSQL(RequeteRecuperationAlerte);
		this.outil.selectBDD();
		this.outil.getResultatSelect().next();
		return this.outil.getResultatSelect().getInt("count");
	}
	public modifi
}
