package SimulationCamions;

public class BankBDDSession {
	
	private String URLSessionSimulationFeux;
	private String URLSessionSimulationCamions;
	private String URLSessionEmergency;
	
	private String userSimuFeu;
	private String passwdSimuFeu;
	
	private String userSimuCamion;
	private String passwdSimuCamion;
	
	private String userEmergency;
	private String passwdEmergency;
	
	private String recupIntervention;
	private String recupCamion;
	private String requeteInterventionCamionEmergency;
	private String requeteSupressionCamion;
	
	
	public BankBDDSession()
	{
		this.URLSessionSimulationFeux = "jdbc:postgresql://localhost:5432/Simulation_feux";
		this.URLSessionSimulationCamions = "jdbc:postgresql://localhost:5432/Simulation_camions";
		this.URLSessionEmergency = "jdbc:postgresql://localhost:5432/Emergency";
		
		this.userSimuFeu = "simu_feux";
		this.passwdSimuFeu = "simu_feux";
		
		this.userSimuCamion = "simu_camions";
		this.passwdSimuCamion = "simu_camions";
		
		this.userEmergency = "emergency";
		this.passwdEmergency = "emergency";
		
		this.recupIntervention = "SELECT * FROM Interventions";
		this.recupCamion = "SELECT * FROM camions";
		this.requeteInterventionCamionEmergency ="SELECT idAlertes, I.idcamion, Ca.coordcasernex, Ca.coordcaserney, Ca.idCaserne,"
		+" A.coordxfeu, A.coordyfeu FROM interventions I"+ 
		" INNER JOIN camions C ON I.idCamion = C.idCamion"+
		" INNER JOIN Alertes A ON I.idAlertes = A.idAlerte"+
		" INNER JOIN Casernes Ca ON C.idCaserne = Ca.idCaserne";
		
		this.requeteSupressionCamion = "DELETE FROM Camions";
		
		
	}
	
	public String getURLSessionSimulationFeux() 
	{
		return this.URLSessionSimulationFeux;
	}
	public String getURLSessionSimulationCamions() 
	{
		return this.URLSessionSimulationCamions;
	}
	public String getURLSessionEmergency()
	{
		return this.URLSessionEmergency;
	}
	public String getuserSimuFeu()
	{
		return this.userSimuFeu;
	} 
	public String getpasswdSimuFeu()
	{
		return this.passwdSimuFeu;
	}
	public String getuserSimuCamion()
	{
		return this.userSimuCamion;
	}
	public String getpasswdSimuCamion()
	{
		return this.passwdSimuCamion;
	}
	public String getuserEmergency()
	{
		return this.userEmergency;
	}
	public String getpasswdEmergency()
	{
		return this.passwdEmergency;
	}

	public String getRecupIntervention() {
		return this.recupIntervention;
	}

	public String getRecupCamion() {
		return this.recupCamion;
	}

	public String getRequeteInterventionCamionEmergency() {
		return this.requeteInterventionCamionEmergency;
	}

	public String getRequeteSupressionCamion() {
		return this.requeteSupressionCamion;
	}
	 
	

}
