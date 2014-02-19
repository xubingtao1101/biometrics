package settings.coordinatorsettings;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedHashMap;

import settings.Settings;

public class DefaultTestingPrequantizedSettings extends CoordinatorSettings{

	@Override
	public String getCoordinator() {
		return "DEFAULTTESTINGPREQUANTIZED";
	}

	@Override
	protected void addSettings() {
	}

	
	//Singleton
	private static DefaultTestingPrequantizedSettings instance;
	private DefaultTestingPrequantizedSettings(){
	}
	public static DefaultTestingPrequantizedSettings getInstance(){
		if(instance == null){
			instance = new DefaultTestingPrequantizedSettings();
		}
		return instance;
	}
	private void writeObject(ObjectOutputStream out) throws IOException{
		out.writeObject(instance.settingsVariables);
	}
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
		instance.settingsVariables = (LinkedHashMap<String, Settings>) in.readObject();
	}
	
	
	@Override
	public String getLabel(){
		return "Default Testing Prequantized";
	}

}
