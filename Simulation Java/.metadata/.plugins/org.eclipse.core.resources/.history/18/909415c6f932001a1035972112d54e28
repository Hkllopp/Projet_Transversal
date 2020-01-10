package SimulationFeux;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Simulation {

	public static void main(String[] args) throws InterruptedException, SQLException {
		// TODO Auto-generated method stub
		BankBDDSession infoBDD = new BankBDDSession();
		double random	= 0;
		int MAX = 1000;
		int MIN = 0;
		ArrayList<Camion> listCamion;
		ArrayList<Capteur> listCapteurs;
		ArrayList<Feu> listFeu = new ArrayList<Feu>();
				
		//Ouverture session BDD simu feux
		SessionBDD BDDsimuFeu = new SessionBDD(infoBDD.getURLSessionSimulationFeux(),
												infoBDD.getuserSimuFeu(),
												infoBDD.getpasswdSimuFeu());
		SessionBDD BDDCamion = new SessionBDD(infoBDD.getURLSessionSimulationCamions(),
				infoBDD.getuserSimuFeu(),
				infoBDD.getpasswdSimuFeu());
		
		//Init capteurs
		RequeteBDD SQLCapteur = new RequeteBDD(BDDsimuFeu.getConnect());
		SQLCapteurs capteur = new SQLCapteurs(SQLCapteur);
		capteur.recuperationDonnee();
		listCapteurs = capteur.generationCapteur();
		
		//Purge Alerte
		RequeteBDD SQLAlerte = new RequeteBDD(BDDsimuFeu.getConnect());
		SQLAlerte Alerte = new SQLAlerte(SQLAlerte);
		Alerte.suppressionTouteLesAlerteDonnee();
		
		//Recuperation Camion
		RequeteBDD SQLCamion = new RequeteBDD(BDDCamion.getConnect());
		SQLCamionsSurZone camion = new SQLCamionsSurZone(SQLCamion);	
			
			
			
			
			while(true)
			{
				random = Math.random() * (MAX - MIN);
				System.out.println(random);
				if(random < 250)
				{
				
					Feu nouveauFeu = new Feu(new Coordonnee(Math.random() * 10, Math.random() * 10),
											 new Intensite(10));
					//System.out.println("position feu" +nouveauFeu.getPosition().getLatitude()+"  "+nouveauFeu.getPosition().getLongitude());
					listFeu.add(nouveauFeu);
					
				}
				
				//Init camions
				
				camion.recuperationDonnee();
				listCamion = camion.generationCamionSurZone();
				
				for(int i = 0;i < listFeu.size();i++)
				{
					for(int j = 0;j <listCamion.size(); j++)
					{
						System.out.println("pos camion"+listCamion.get(j).getPositionActuel().getLatitude());
						System.out.println("pos camion"+listCamion.get(j).getPositionActuel().getLongitude());
						if(listFeu.get(i).getPosition().equals(listCamion.get(j).getPositionActuel()))
						{
							listFeu.get(i).diminutionIntensiteFeu(3);
						}
					}
				}
				for(int i = 0;i < listFeu.size();i++)
				{
					listFeu.get(i).augmentationIntensiteFeu(1);
					//System.out.println(listFeu.get(i).getTaille().getHauteur());
				}
				
				for(int i = 0;i < listCapteurs.size();i++)
				{
					//System.out.println("position capteur :" +listCapteurs.get(i).getPosition().getLatitude()+"  "+listCapteurs.get(i).getPosition().getLongitude());
					for(int j = 0;j < listFeu.size();j++)
					{
						//System.out.println(Alerte.getNbAlerteSurFeu(listFeu.get(j)));
						if(listCapteurs.get(i).getPosition().equals(listFeu.get(j).getPosition()))
						{
							if(Alerte.getNbAlerteSurFeu(listFeu.get(j)) > 0)
							{
								if(listFeu.get(j).getTaille().getHauteur() == 0)
								{
									Alerte.suppressionAlertePrecice(listFeu.get(j));
								}
								if(listFeu.get(j).getTaille().getHauteur() > 0)
								{
									Alerte.updateAlertePrecice(listFeu.get(j));
								}
							}
							else
							{
								Alerte.insertAlertePrecise(listFeu.get(j), listCapteurs.get(i));
							}
						}
					}
				}
				
				
				System.out.println(listFeu.size());
				System.out.println(listCapteurs.size());
				
				TimeUnit.SECONDS.sleep(2);	
			}
			//SQLCapteur.closeStateResult();
			//SQLCamion.closeStateResult();			
		
	}

}
