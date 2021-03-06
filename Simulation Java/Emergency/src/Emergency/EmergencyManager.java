package Emergency;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class EmergencyManager {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		BankBDDSession bankRequeteLoginBDD= new BankBDDSession();
		ArrayList<Alerte> listAlertes = new ArrayList<Alerte>();
		ArrayList<Camion> listCamionsEmergency = new ArrayList<Camion>();
		ArrayList<Camion> listCamionsSimulation = new ArrayList<Camion>();
		
		ArrayList<Intervention> listInterventions = new ArrayList<Intervention>();
		
		SessionBDD BDDEmergency = new SessionBDD(bankRequeteLoginBDD.getURLSessionEmergency(),
				bankRequeteLoginBDD.getuserEmergency(),
				bankRequeteLoginBDD.getpasswdEmergency());//Ouverture sessions BDD Emergency
		
		SessionBDD BDDsimulation_Camion = new SessionBDD(bankRequeteLoginBDD.getURLSessionSimulationCamions(),
				bankRequeteLoginBDD.getuserEmergency(),
				bankRequeteLoginBDD.getpasswdEmergency());	//Ouverture sessions BDD Simu camion
									
		
		RequeteBDD SQLAlerte = new RequeteBDD(BDDEmergency.getConnect());
		SQLAlerte alertes = new SQLAlerte(SQLAlerte); //Generation outil de requete Alerte BDD Emergency
		
		RequeteBDD SQLCamionEmergency = new RequeteBDD(BDDEmergency.getConnect());
		SQLCamion camionsEmergency = new SQLCamion(SQLCamionEmergency);//Generation outil de requete Camion BDD Emergency
		
		RequeteBDD SQLCamionSimuCamion = new RequeteBDD(BDDsimulation_Camion.getConnect());
		SQLCamion camionsSimuCamion = new SQLCamion(SQLCamionSimuCamion);//Generation outil de requete Camion BDD Simu camion
		
		RequeteBDD SQLIntervention = new RequeteBDD(BDDEmergency.getConnect());
		SQLIntervention interventions = new SQLIntervention(SQLIntervention);//Generation outil de requete Intervention BDD Emergency
		
		
		int nbInterParAlerte = 0;
		Intervention newIntervention;
		
		while(true)
		{
			alertes.recuperationAlerte(bankRequeteLoginBDD.getRequeteRecuperationAllAlerte());
			listAlertes = alertes.generationAlerte();//Genration liste Alerte
			
			camionsEmergency.recuperationCamion(bankRequeteLoginBDD.getRequeteRecuperationAllCamion());
			listCamionsEmergency = camionsEmergency.generationCamion();//Generation Liste camion
			
			camionsSimuCamion.recuperationCamion(bankRequeteLoginBDD.getRequeteRecuperationAllCamion());
			listCamionsSimulation = camionsSimuCamion.generationCamion(); //Generation liste camion BDD Simulation
			
			Caserne caserneLyon = new Caserne(listCamionsEmergency, "LYO");//Generation liste Caserne
			
			interventions.recuperationIntervention(bankRequeteLoginBDD.getRequeteRecuperationAllIntervention());
			listInterventions = interventions.generationIntervention(listCamionsEmergency, listAlertes);//Generation liste intervention
			
			
			for(int i = 0;i < listAlertes.size();i++)//On parcours toutes les alertes
			{
				//TimeUnit.SECONDS.sleep(2);
				for(int j = 0; j < listInterventions.size(); j++)//Pour chaque alerte on parcours toutes les interventions
				{
					if(listAlertes.get(i).getIdAlerte() == listInterventions.get(j).getAlerte().getIdAlerte())//Si ya un match entre l'idAlerte de l'intervention et l'iD alerte de l'alerte
					{
						nbInterParAlerte++;//On augmente le nombre Intervention par alerte de 1
					}
				}
				if(nbInterParAlerte < 1)//Si il n'y a pas d'intervention sur une alerte on l'a cr�e
				{
					if(!(caserneLyon.getCamionDispo().isEmpty()))//On v�rifie que des camions sont dispo
					{
						camionsEmergency.updateCamion(bankRequeteLoginBDD.getRequeteUpdateCamionAller(), caserneLyon.getCamionDispo().get(0));
						//On update le statut du camion � aller
						newIntervention = new Intervention(caserneLyon.getCamionDispo().get(0), listAlertes.get(i));
						//On cr�e l'interventions
						caserneLyon.faireSortirCamion(caserneLyon.getCamionDispo().get(0));
						//On fait sortir de le camion de la caserne
						interventions.insertIterventionPrecise(bankRequeteLoginBDD.getRequeteInsertIntervention(), newIntervention);
						//On insert l'intervention en BDD
					}
						
				}
				newIntervention = null;
				nbInterParAlerte = 0;
			}
			
			
			
			for(int j = 0; j < listInterventions.size(); j++)//On parcours la liste des interventions
			{
				
				camionsSimuCamion.setRequeteCamionIntervention(listInterventions.get(j));//On parcours la liste des camions associ� � cette intervention sur la BDD simu camion mais qui sont pret � rentrer en caserne
				
				if(!(camionsSimuCamion.generationCamion().isEmpty()))//Si jamais ya des camions pret � rentrer
				{
					interventions.suppressionIntervention(bankRequeteLoginBDD.getRequeteDeleteInterventionUnique(), listInterventions.get(j));//On supprime leur intervention en BDD
					
					camionsEmergency.updateCamion(bankRequeteLoginBDD.getRequeteUpdateCamionDispo(), camionsSimuCamion.generationCamion().get(0));//On met � jour le statut du camion
					caserneLyon.faireRentrerCamion(camionsSimuCamion.generationCamion().get(0));//On le fait rentrer en caserne
				}
				
				
			}
			SQLAlerte.fermetureResult();
			SQLCamionEmergency.fermetureResult();
			SQLCamionSimuCamion.fermetureResult();
			SQLIntervention.fermetureResult();
			
			TimeUnit.SECONDS.sleep(2);
			
		}
	}
}
