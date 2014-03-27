package system.hasher.fuzzyvault;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

import system.hasher.fuzzyvault.bwdecoding.BWDecoder;
import system.hasher.fuzzyvault.bwdecoding.BigPoly;

public class BerlekampWelchWrapper implements RSDecoder {

	public BerlekampWelchWrapper() {
	}

	@Override
	public SecretPolynomial decode(ArrayList<FuzzyVaultPoint> fuzzyVaultPoints, Integer k, BigInteger mod) {
//		ArrayList<BigInteger> zValues = new ArrayList<BigInteger>();
//		ArrayList<BigInteger> gammaValues = new ArrayList<BigInteger>();
//		for (FuzzyVaultPoint vp : fuzzyVaultPoints) {
//			zValues.add(vp.getZ());
//			gammaValues.add(vp.getGamma());
//		}
//		BigInteger[] zValueArray = (BigInteger[]) zValues.toArray();
//		BigInteger[] gammaValueArray = (BigInteger[]) gammaValues.toArray();

		int numberOfPoints = fuzzyVaultPoints.size();
		
		if(numberOfPoints < k+2){
			return null;
		}
		
		BigInteger[] zValueArray = new BigInteger[numberOfPoints];
		BigInteger[] gammaValueArray = new BigInteger[numberOfPoints];
		
		for(int i=0; i<numberOfPoints; i++){
			zValueArray[i] = fuzzyVaultPoints.get(i).getZ();
			gammaValueArray[i] = fuzzyVaultPoints.get(i).getGamma();
		}
		
		BWDecoder decoder = new BWDecoder(zValueArray, gammaValueArray, k, mod);
		decoder.CalcPoly();
		BigPoly polynomial = decoder.getSecretPolynomial();
		return new SecretPolynomial(k.longValue(), mod.longValue(), new ArrayList<BigInteger>(Arrays.asList(polynomial.getCoefficients())));
	}

	
}
