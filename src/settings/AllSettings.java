package settings;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedHashMap;

import javax.swing.JButton;
import javax.swing.JPanel;

import settings.coordinatorsettings.AllHistogramCoordinatorSettings;
import settings.coordinatorsettings.AllIndexingCoordinatorSettings;
import settings.coordinatorsettings.AllMatchingCoordinatorSettings;
import settings.hashersettings.AllHasherSettings;
import settings.modalitysettings.AllModalitySettings;
import system.allcommonclasses.commonstructures.Results;
import system.biometricsystem.BiometricSystem;

public class AllSettings extends Settings{

	public static final long serialVersionUID = 1L;
	
	private transient JPanel panel;

	
	//This block of code must be in all settings files (except settings variables) to enable serialization.
	private static AllSettings instance;
	private AllSettings(){}
	public static AllSettings getInstance(){
		if(instance == null){
			instance = new AllSettings();
		}
		return instance;
	}
//	private void writeObject(ObjectOutputStream out) throws IOException{
//		out.writeObject(instance.settingsVariables);
//	}
//	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
//		instance.settingsVariables = (LinkedHashMap<String, Settings>) in.readObject();
//	}
	
	


	
	// override just to make this public 
	@Override
	public JPanel makeJPanel() {	
		return super.makeJPanel();
	}
	
	@Override
	public JPanel getJPanel(){
		this.updateGUI();
		return this.panel;
	}
	

	
	@Override 
	protected void addSettings(){
		this.settingsVariables.put("Matching", AllMatchingCoordinatorSettings.getInstance());
		this.settingsVariables.put("Indexing", AllIndexingCoordinatorSettings.getInstance());
		this.settingsVariables.put("Histogram", AllHistogramCoordinatorSettings.getInstance());
		this.settingsVariables.put("Hasher", AllHasherSettings.getInstance());
		this.settingsVariables.put("Modality", AllModalitySettings.getInstance());
	}
	
	public void updateGUI(){
		if(AllSettings.getInstance().panel == null){
			AllSettings.getInstance().panel = new JPanel();
		}
		AllSettings.getInstance().panel.removeAll();
		AllSettings.getInstance().panel.add(AllSettings.getInstance().makeJPanel());
		AllSettings.getInstance().panel.validate();
		AllSettings.getInstance().panel.repaint();
		AllSettings.getInstance().panel.updateUI();
	}
	
	public BiometricSystem buildSystem(){

		AllSettings.getInstance();
		
		BiometricSystem system = new BiometricSystem();
		Results results = system.go();
		
		//this shouldn't go here
//		ResultsGUI resultsGUI = new ResultsGUI(results);

		System.out.print(results.rawScores);
		System.out.println(results);
		
		return system;	
	}
	
	protected JPanel thisJPanel(){
		JPanel panel = new JPanel();
		JButton goButton = new JButton("GO!");
		
		goButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e)
            {buildSystem();}
            }
		);   
		
		panel.add(goButton);
		return panel;
	}

	public AllModalitySettings modality(){
		return (AllModalitySettings) this.settingsVariables.get("Modality");
	}
	public AllHasherSettings hasher(){
		return (AllHasherSettings) this.settingsVariables.get("Hasher");
	}
	public AllMatchingCoordinatorSettings matchingCoordinator(){
		return (AllMatchingCoordinatorSettings) this.settingsVariables.get("Matching");
	}
	public AllIndexingCoordinatorSettings indexingCoordinator(){
		return (AllIndexingCoordinatorSettings) this.settingsVariables.get("Indexing");
	}
	public AllHistogramCoordinatorSettings histogramCoordinator(){
		return (AllHistogramCoordinatorSettings) this.settingsVariables.get("Histogram");
	}


}
