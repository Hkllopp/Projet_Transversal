package SimulationFeux;

import java.util.ArrayList;

public class Capteur {
	private int idCapteur;
	private Coordonnee position;
	private boolean activation;
	
	
	
	
	public Capteur(int id, Coordonnee position, boolean activation)
	{
		this.idCapteur = id;
		this.position = position;
		this.activation = activation;
	}
	
	public int getIdCapteur()
	{
		return this.idCapteur;
	}
	public Coordonnee getPosition()
	{
		return this.position;
	}
	public void setPosition(Coordonnee position)
	{
		this.position = position;
	}
	public boolean getActivation()
	{
		return this.activation;
	}
	public void setActivation(boolean activation)
	{
		this.activation = activation;
	}
	
	public void detectionFeu(ArrayList<Feu> feu)
	{
		if(this.activation == false)
		{
			for(int i = 0; i < feu.size(); i++)
			{
				if(feu.get(i).getPosition() == this.position)
				{
					this.activation = true;
					//this.intensite = feu.get(i).getTaille().getHauteur();
				}
			}
		}
	}
	
}
