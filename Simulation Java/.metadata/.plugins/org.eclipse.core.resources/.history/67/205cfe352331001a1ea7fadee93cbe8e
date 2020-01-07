package SimulationFeux;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

public class Simulation {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Coordonnee test1 = new Coordonnee(1,3);
		Intensite test2 = new Intensite(19);
		Feu feu1 = new Feu(test1, test2);
		
		System.out.println("  " + test1.getLatitude());
		System.out.println("  " + test1.getLongitude());
		System.out.println("  " + test2.getHauteur());
		
		System.out.println("  " + feu1.getPosition().getLatitude());
		
		SessionBDD BDD = new SessionBDD();
		RequeteBDD SQL = new RequeteBDD(BDD.getConnect());
		SQLCapteurs capt = new SQLCapteurs(SQL);
		capt.recuperationDonnee();
		ArrayList<Capteur> listCapteurs = capt.generationCapteur();
		
		
		
			for(int i = 0; i < listCapteurs.size(); i++)
			{
				 System.out.println("   " + listCapteurs.get(i).getActivation()+"  ");
			}		
		
	}

}
