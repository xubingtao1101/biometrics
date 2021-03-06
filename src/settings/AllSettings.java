package settings;

import interfaces.gui.ResultsGUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedHashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import settings.coordinatorsettings.histogramcoordinatorsettings.AllHistogramCoordinatorSettings;
import settings.coordinatorsettings.indexingcoordinatorsettings.AllIndexingCoordinatorSettings;
import settings.coordinatorsettings.matchingcoordinatorsettings.AllMatchingCoordinatorSettings;
import settings.coordinatorsettings.multiservercoordinatorsettings.AllMultiserverCoordinatorSettings;
import settings.hashersettings.AllHasherSettings;
import settings.modalitysettings.AllModalitySettings;
import settings.quantizersettings.AllQuantizerSettings;
import system.allcommonclasses.commonstructures.Results;
import system.biometricsystem.BiometricSystem;

public class AllSettings extends Settings {

	public static final long serialVersionUID = 1L;

	private transient JPanel panel;

	// This block of code must be in all settings files (except settings
	// variables) to enable serialization.
	private static AllSettings instance;

	private AllSettings() {
//		 addSettings();
	}

	public static AllSettings getInstance() {
		if (instance == null) {
			instance = new AllSettings();
		}
		return instance;
	}

	// private void writeObject(ObjectOutputStream out) throws IOException{
	// out.writeObject(instance.settingsVariables);
	// }
	// private void readObject(ObjectInputStream in) throws IOException,
	// ClassNotFoundException{
	// instance.settingsVariables = (LinkedHashMap<String, Settings>)
	// in.readObject();
	// }

	// override just to make this public
	@Override
	public JPanel makeJPanel() {
		return super.makeJPanel();
	}

	@Override
	public JPanel getJPanel() {
		this.updateGUI();
		return this.panel;
	}

	@Override
	protected void addSettings() {
		this.settingsVariables.put("Multiserver",AllMultiserverCoordinatorSettings.getInstance()); // default to all prequantized
		this.settingsVariables.put("Matching",AllMatchingCoordinatorSettings.getInstance()); // default to all prequantized
		this.settingsVariables.put("Indexing",AllIndexingCoordinatorSettings.getInstance()); // default to none
		this.settingsVariables.put("Histogram",AllHistogramCoordinatorSettings.getInstance()); //
		this.settingsVariables.put("Hasher", AllHasherSettings.getInstance());
		this.settingsVariables.put("Quantizer",AllQuantizerSettings.getInstance());
		this.settingsVariables.put("Modality",AllModalitySettings.getInstance());
	}

	public void updateGUI() {
		if (AllSettings.getInstance().panel == null) {
			AllSettings.getInstance().panel = new JPanel();
		}
		// AllSettings.getInstance().panel.setBackground(BACKGROUNDCOLOR);
		AllSettings.getInstance().panel.removeAll();
		AllSettings.getInstance().panel.add(AllSettings.getInstance().makeJPanel());
		AllSettings.getInstance().panel.validate();
		AllSettings.getInstance().panel.repaint();
		AllSettings.getInstance().panel.updateUI();
	}

	public BiometricSystem buildSystem() {
		AllSettings.getInstance();
		BiometricSystem system = new BiometricSystem();
		return system;
	}

	public void runSystemAndMakeGraphs() {

		BiometricSystem system = this.buildSystem();

		Results results = system.go();

		if(results != null){
			System.out.print(results.getRawScores());
			System.out.println(results.toString());

			@SuppressWarnings("unused")
			ResultsGUI resultsGUI = new ResultsGUI(results);
		}

	}

	public Results runSystemAndGetResults() {
		BiometricSystem system = this.buildSystem();
		return system.go();
	}

	protected JPanel thisJPanel() {
		JPanel panel = new JPanel();
		JButton goButton = new JButton("GO!") {
			@Override
			protected void paintComponent(Graphics g) {
				if (getModel().isPressed()) {
					g.setColor(Color.RED);
				} else {
					g.setColor(Color.GREEN);
				}
				g.fillRect(0, 0, getWidth(), getHeight());
				super.paintComponent(g);
			}
		};

		goButton.setFont(goButton.getFont().deriveFont(Font.BOLD));
		goButton.setFont(goButton.getFont().deriveFont(20.0f));
		goButton.setBorder(BorderFactory.createRaisedBevelBorder());
		goButton.setPreferredSize(new Dimension(150, 100));

		goButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				instance.runSystemAndMakeGraphs();
			}
		});

		panel.add(goButton);
		return panel;
	}

	//
	// public AllModalitySettings modality(){
	// return (AllModalitySettings) this.settingsVariables.get("Modality");
	// }
	// public AllHasherSettings hasher(){
	// return (AllHasherSettings) this.settingsVariables.get("Hasher");
	// }
	// public AllQuantizerSettings quantizer(){
	// return (AllQuantizerSettings) this.settingsVariables.get("Quantizer");
	// }
	// public AllMatchingCoordinatorSettings matchingCoordinator(){
	// return (AllMatchingCoordinatorSettings)
	// this.settingsVariables.get("Matching");
	// }
	// public AllIndexingCoordinatorSettings indexingCoordinator(){
	// return (AllIndexingCoordinatorSettings)
	// this.settingsVariables.get("Indexing");
	// }
	// public AllHistogramCoordinatorSettings histogramCoordinator(){
	// return (AllHistogramCoordinatorSettings)
	// this.settingsVariables.get("Histogram");
	// }

}
