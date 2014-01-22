package system.base;

import java.util.ArrayList;
import java.util.Collections;

import system.allcommonclasses.commonstructures.Histogram;
import system.allcommonclasses.commonstructures.RawScores;
import system.allcommonclasses.commonstructures.Results;

public class EvaluatePerformance {


	protected static Results processResults(RawScores rawScores) {
		Results results = new Results();
		
		// TODO Jesse - fix this
		if(!rawScores.genuineScores.isEmpty() && !rawScores.imposterScores.isEmpty()){
			results.setRates(EvaluatePerformance.computeRates(rawScores));
			results.setEer(EvaluatePerformance.computeEER(results.getRates()));
		}
		
		results.setFieldHistogram(EvaluatePerformance.computeFieldHistogram(rawScores));
		results.setVariableHistograms(EvaluatePerformance.computeVariableHistograms(rawScores));
		results.setIndexingResults(EvaluatePerformance.computeIndexingResults(rawScores));
		
		
		return results;
	}
	

	private static ArrayList<Long> computeIndexingResults(RawScores rawScores) {
		// TODO Jesse - compute indexing results
		return null;
	}


	private static ArrayList<Histogram> computeVariableHistograms(RawScores rawScores) {
		// TODO Jim
		return null;
	}


	private static Histogram computeFieldHistogram(RawScores rawScores) {
		// TODO Jim
		return null;
	}


	protected static ArrayList<RatesPoint> computeRates(RawScores rawScores) {
		
		Collections.sort(rawScores.genuineScores);
		Collections.sort(rawScores.imposterScores);
		
		// This needs to be looked over
		Results results = new Results();

		ArrayList<Double> gens = rawScores.genuineScores;
		ArrayList<Double> imps = rawScores.imposterScores;
		
		Collections.sort(gens);
		Collections.sort(imps);

		Double min = Math.min(gens.get(0), imps.get(0));
		Double max = Math.min(gens.get(gens.size()-1), imps.get(imps.size()-1));
		Double stepSize = 0.9;
		
		ArrayList<RatesPoint> rates = new ArrayList<RatesPoint>();
		
		for(Double i = min-stepSize; i<=max+stepSize; i+=stepSize){
			RatesPoint point = new RatesPoint();
			point.threshold=i;
			
			Double falseAccepts = Double.valueOf(imps.size());
			Double falseRejects = 0.0;
			for(Double d : gens){
				if(d<point.threshold){
					falseRejects += 1.0;
				}else{
					break;
				}
			}
			
			for(Double d : imps){
				if(d<point.threshold){
					falseAccepts -= 1.0;
				}else{
					break;
				}
			}
			point.far = falseAccepts/Double.valueOf(imps.size());
			point.frr = falseRejects/Double.valueOf(gens.size());
			
			rates.add(point);
		}
		
		return rates;
	}

	
	protected static Double computeEER(ArrayList<RatesPoint> rates){
		
		Double eer = Double.POSITIVE_INFINITY;
		
		for(RatesPoint point : rates){
			if(point.frr >= point.far){
				eer = (point.frr + point.far)/2.0;
				break;
			}
		}
		
		return eer;
	}
	
	
}
