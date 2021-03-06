package unittests.testsystem.testmethod.testfingerprintmethods;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;

import org.junit.Test;

import settings.fingerprintmethodsettings.PRINTSettings;
import settings.settingsvariables.SettingsMethodVariable;
import system.allcommonclasses.modalities.Minutia;
import system.method.fingerprintmethods.PRINTS;
import system.method.fingerprintmethods.PRINTS.PRINT;

public class TestPRINTS {
	
	private PRINTSettings getSettings(){
		PRINTSettings settings = PRINTSettings.getInstance();
		
		settings.n().setValue(5);
		settings.rotationRegions().setValue(100);
		return settings;
	}
	
	@org.junit.Test
	public void testMakePRINT() {
		
//		private PRINT makePRINT(ArrayList<Minutia> minutiaList)
		
		this.getSettings();
		
		PRINTS printMethod = new PRINTS();
		PRINT computedPRINT = printMethod.new PRINT();
		
		
		try {
			Method makePRINT = printMethod.getClass().getDeclaredMethod("makePRINT", ArrayList.class);
			makePRINT.setAccessible(true);
			
			ArrayList<Minutia> points = new ArrayList<Minutia>();
			points.add(new Minutia(102,  -75, 360)); //p0, distance to center: 56.66462741428730156792553985722627737957387043380753560202088
			points.add(new Minutia(52, 106, 721));   //p2, distance to center: 131.1849076685271749785644788968742865175471452809913958598429
			points.add(new Minutia(60, -100, 2));    //p4, distance to center: 85.35033684760711007372125021555034249066208450905006224155474
			points.add(new Minutia(32, -86, 2));     //p3, distance to center: 87.48874213291673563172928803231040779315102519945831953905962
			points.add(new Minutia(200, 56, 12));    //p1, distance to center: 134.2470856294467198442216710975508940294260083359754912356535
													 //center = (89.2, -19.8)
			computedPRINT = (PRINT) makePRINT.invoke(printMethod, points);
			
		} 
		catch (Exception e) {e.printStackTrace();}
		
		PRINT expectedPRINT = printMethod.new PRINT();

		expectedPRINT.variables.get("distance0").setPrequantizedValue(56.6646274142);
		expectedPRINT.variables.get("sigma0").setPrequantizedValue(12.0);
		expectedPRINT.variables.get("phi0").setPrequantizedValue(111.32138975531047043739);
		
		expectedPRINT.variables.get("distance1").setPrequantizedValue(134.2470856294);
		expectedPRINT.variables.get("sigma1").setPrequantizedValue(11.0);
		expectedPRINT.variables.get("phi1").setPrequantizedValue(72.096705089175344356292);
		
		expectedPRINT.variables.get("distance2").setPrequantizedValue(131.1849076685271749);
		expectedPRINT.variables.get("sigma2").setPrequantizedValue(1.0);
		expectedPRINT.variables.get("phi2").setPrequantizedValue(122.69805665078455280515222617);
		
		expectedPRINT.variables.get("distance3").setPrequantizedValue(87.488742132916735);
		expectedPRINT.variables.get("sigma3").setPrequantizedValue(0.0);
		expectedPRINT.variables.get("phi3").setPrequantizedValue(20.82255350720074415846140);
		
		expectedPRINT.variables.get("distance4").setPrequantizedValue(85.350336847607110);
		expectedPRINT.variables.get("sigma4").setPrequantizedValue(2.0);
		expectedPRINT.variables.get("phi4").setPrequantizedValue(33.06129499752888831306168);

		
		assertTrue(
				"\nExpected: " + expectedPRINT.prequantizedToString() + " " + 
				"\nComputed: " + computedPRINT.prequantizedToString() + " ",
				expectedPRINT.equalsPrequantized(computedPRINT)
				);
	}

	@Test
	public void testGetRegionNumber(){
		this.getSettings();
		PRINTSettings.getInstance().rotationRegions().setValue(5);
		PRINTS printsMethod = new PRINTS();
		PRINT print = printsMethod.new PRINT();
		print.setAngle(126.6);
		Long computed = print.getRegionNumber();
		Long expected = 1L;
		assertTrue(
				"\nExpected: "+expected+" "+
				"\nComputed: "+computed,
				expected.equals(computed));
	}
	@Test
	public void testGetRegionNumber1(){
		this.getSettings();
		PRINTSettings.getInstance().rotationRegions().setValue(3);
		PRINTS printsMethod = new PRINTS();
		PRINT print = printsMethod.new PRINT();
		print.setAngle(270.0);
		Long computed = print.getRegionNumber();
		Long expected = 2L;
		assertTrue(
				"\nExpected: "+expected+" "+
				"\nComputed: "+computed,
				expected.equals(computed));
	}
	@Test
	public void testGetRegionNumber2(){
		this.getSettings();
		PRINTSettings.getInstance().rotationRegions().setValue(100);
		PRINTS printsMethod = new PRINTS();
		PRINT print = printsMethod.new PRINT();
		print.setAngle(126.6);
		Long computed = print.getRegionNumber();
		Long expected = 35L;
		assertTrue(
				"\nExpected: "+expected+" "+
				"\nComputed: "+computed,
				expected.equals(computed));
	}
	

	@Test
	public void testAppendRegionNumber(){
		this.getSettings();
		PRINTSettings.getInstance().rotationRegions().setValue(33);
		BigInteger computed = PRINTS.appendRegionNumber(BigInteger.valueOf(6L), 13L);
		BigInteger expected = BigInteger.valueOf(397L);
		assertTrue(
				"\nExpected: "+expected+" "+
				"\nComputed: "+computed,
				expected.equals(computed));
	}

	@Test
	public void testAppendRegionNumber1(){
		this.getSettings();
		PRINTSettings.getInstance().rotationRegions().setValue(5);
		BigInteger computed = PRINTS.appendRegionNumber(BigInteger.valueOf(21348973L), 0L);
		BigInteger expected = BigInteger.valueOf(170791784L);
		assertTrue(
				"\nExpected: "+expected+" "+
				"\nComputed: "+computed,
				expected.equals(computed));
	}
	@Test
	public void testAppendRegionNumber2(){
		this.getSettings();
		PRINTSettings.getInstance().rotationRegions().setValue(100);
		BigInteger computed = PRINTS.appendRegionNumber(BigInteger.valueOf(432158976234905L), 98L);
		BigInteger expected = BigInteger.valueOf(55316348958067938L);
		assertTrue(
				"\nExpected: "+expected+" "+
				"\nComputed: "+computed,
				expected.equals(computed));
	}
	


	@Test
	public void testGetAppendedRegion(){
		this.getSettings();
		PRINTSettings.getInstance().rotationRegions().setValue(33);
		Long computed = PRINTS.getAppendedRegion(BigInteger.valueOf(397L));
		Long expected = 13L;
		assertTrue(
				"\nExpected: "+expected+" "+
				"\nComputed: "+computed,
				expected.equals(computed));
	}

	@Test
	public void testGetAppendedRegion1(){
		this.getSettings();
		PRINTSettings.getInstance().rotationRegions().setValue(5);
		Long computed = PRINTS.getAppendedRegion(BigInteger.valueOf(170791784L));
		Long expected = 0L;
		assertTrue(
				"\nExpected: "+expected+" "+
				"\nComputed: "+computed,
				expected.equals(computed));
	}

	@Test
	public void testGetAppendedRegion2(){
		this.getSettings();
		PRINTSettings.getInstance().rotationRegions().setValue(100);
		Long computed = PRINTS.getAppendedRegion(BigInteger.valueOf(55316348958067938L));
		Long expected = 98L;
		assertTrue(
				"\nExpected: "+expected+" "+
				"\nComputed: "+computed,
				expected.equals(computed));
	}
	
	

	@Test
	public void testGetPrintWithoutRegionNumber(){
		this.getSettings();
		PRINTSettings.getInstance().rotationRegions().setValue(33);
		BigInteger computed = PRINTS.getPrintWithoutRegionNumber(BigInteger.valueOf(397L));
		BigInteger expected = BigInteger.valueOf(6L);
		assertTrue(
				"\nExpected: "+expected+" "+
				"\nComputed: "+computed,
				expected.equals(computed));
	}
	@Test
	public void testGetPrintWithoutRegionNumber1(){
		this.getSettings();
		PRINTSettings.getInstance().rotationRegions().setValue(5);
		BigInteger computed = PRINTS.getPrintWithoutRegionNumber(BigInteger.valueOf(170791784L));
		BigInteger expected = BigInteger.valueOf(21348973L);
		assertTrue(
				"\nExpected: "+expected+" "+
				"\nComputed: "+computed,
				expected.equals(computed));
	}
	@Test
	public void testGetPrintWithoutRegionNumber2(){
		this.getSettings();
		PRINTSettings.getInstance().rotationRegions().setValue(100);
		BigInteger computed = PRINTS.getPrintWithoutRegionNumber(BigInteger.valueOf(55316348958067938L));
		BigInteger expected = BigInteger.valueOf(432158976234905L);
		assertTrue(
				"\nExpected: "+expected+" "+
				"\nComputed: "+computed,
				expected.equals(computed));
	}

//	@Test
//	public void testGetRegionNumber(){
//		this.getSettings();
//		PRINTS p = new PRINTS();
//		BigInteger testRegion = p.getRegionBigInteger(BigInteger.valueOf(900685799));
//		assertTrue(
//				"\nExpected: "+BigInteger.valueOf(99)+" "+
//				"\nComputed: "+testRegion,
//				testRegion.equals(BigInteger.valueOf(99)));
//		
//	}

}
