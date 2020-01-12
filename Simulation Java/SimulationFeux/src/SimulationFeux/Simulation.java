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
				//System.out.println(random);
				
				Alerte.recuperationAlerte(infoBDD.getRequetRecupAllAlertes());
				listFeu = Alerte.generationFeu();
				
				
				if(random < 250 && listFeu.size() < 10)
				{
				
					Feu nouveauFeu = new Feu(new Coordonnee(Math.random() * 10, Math.random() * 10),
											 new Intensite(10));
					listFeu.add(nouveauFeu);
					
				}
							
				camion.recuperationDonnee();
				listCamion = camion.generationCamionSurZone();
				
				for(int i = 0;i < listFeu.size();i++)
				{
					for(int j = 0;j <listCamion.size(); j++)
					{
						if(listFeu.get(i).getPosition().equals(listCamion.get(j).getPositionActuel()))
						{
							listFeu.get(i).diminutionIntensiteFeu(3);
						}
					}
				}
				for(int i = 0;i < listFeu.size();i++)
				{
					listFeu.get(i).augmentationIntensiteFeu(1);
				}
				
				for(int i = 0;i < listCapteurs.size();i++)
				{
					for(int j = 0;j < listFeu.size();j++)
					{
						if(listCapteurs.get(i).getPosition().equals(listFeu.get(j).getPosition()))
						{
							if(Alerte.getNbAlerteSurFeu(listFeu.get(j)) > 0)
							{
								if(listFeu.get(j).getTaille().getHauteur() <= 0)
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
				
				
				TimeUnit.SECONDS.sleep(2);
				Feu.setNbFeu(0);
				SQLCapteur.fermetureResult();
				SQLAlerte.fermetureResult();
				//SQLCamion.fermetureResult();
			}
			//SQLCapteur.closeStateResult();
			//SQLCamion.closeStateResult();			
		
	}

}
