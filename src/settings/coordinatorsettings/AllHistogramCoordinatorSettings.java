package settings.coordinatorsettings;

import javax.swing.JComboBox;
import javax.swing.JPanel;

import settings.ComboBoxSettings;
import settings.Settings;
import settings.SettingsRenderer;
import settings.hashersettings.HasherSettings;
import settings.hashersettings.FuzzyVaultSettings;
import settings.hashersettings.AllHasherSettings;
import settings.hashersettings.StraightHasherSettings;
import settings.settingsvariables.SettingsString;

public class AllHistogramCoordinatorSettings extends ComboBoxSettings{

	private static final long serialVersionUID = 1L;
	

	
	
	public static String getHistogramCoordinator(){
		CoordinatorSettings coordinatorSettings = (CoordinatorSettings) instance.settingsVariables.get(instance.variableString);
		return coordinatorSettings.getCoordinator();
	}
	
	
	//Singleton
	private static AllHistogramCoordinatorSettings instance;
	private AllHistogramCoordinatorSettings() {}
	public static AllHistogramCoordinatorSettings getInstance(){
		if(instance == null){
			instance = new AllHistogramCoordinatorSettings();
		}
		return instance;
	}


	
	@Override
	protected void init() {
		this.variableString = "histogram";
		this.settingsVariables.put("histogram", NoCoordinator.getInstance());
	}

	@Override
	protected void addALLOptions() {
		this.addToOptions(NoCoordinator.getInstance());
		this.addToOptions(HistogramSettings.getInstance());
	}


}