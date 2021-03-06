package optimize;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import settings.AllSettings;
import settings.coordinatorsettings.testgeneratorsettings.AllTestGeneratorSettings;
import settings.coordinatorsettings.testgeneratorsettings.TestGeneratorFVCTestsSettings;
import settings.fingerprintmethodsettings.AllFingerprintMethodSettings;
import settings.fingerprintmethodsettings.NgonSettings;
import settings.fingerprintmethodsettings.PRINTSettings;
import settings.modalitysettings.AllModalitySettings;
import settings.modalitysettings.FingerprintSettings;
import settings.settingsvariables.SettingsDropDownItem;
import system.allcommonclasses.commonstructures.Results;

public class GeneticAlgorithm {
	// FIXME Matt
	private FitnessFunction f;

	public GeneticAlgorithm(FitnessFunction fitness) {
		// AllFingerprintMethodSettings.getInstance().manuallySetComboBox(NgonSettings.getInstance());
		AllFingerprintMethodSettings.getInstance().manuallySetComboBox(
				PRINTSettings.getInstance());
		AllModalitySettings.getInstance().manuallySetComboBox(
				FingerprintSettings.getInstance());
		AllTestGeneratorSettings.getInstance().manuallySetComboBox(
				new TestGeneratorFVCTestsSettings());
		FingerprintSettings
				.getInstance()
				.testingDataset()
				.manuallySetComboBox(
						new SettingsDropDownItem("FVC20022Small.ser"));
		FingerprintSettings
				.getInstance()
				.trainingDataset()
				.manuallySetComboBox(
						new SettingsDropDownItem("FVC20022Small.ser"));
		this.f = fitness;
	}

	public static void main(String[] args) {
		GeneticAlgorithm ga = new GeneticAlgorithm(new FitnessFunction() {
			@Override
			public Results evaluateFitness(ArrayList<Chromosome> chromosomes) {
				AllSettings settings = AllSettings.getInstance();
				for (Chromosome c : chromosomes) {
					c.execute();
				}
				return settings.runSystemAndGetResults();
				// Results results = settings.runSystemAndGetResults();
				// System.out.println(results.getEer());
				//
				// return (1-results.getEer())*100.0; //minimizing EER
			}
		});

		ArrayList<Candidate> candidates = ga.intializePopulation(10);
		ArrayList<Candidate> init = new ArrayList<Candidate>(candidates);
		System.out.println("Initial Population:");
		for (Candidate c : init) {
			System.out.println("Candidate: " + c);
		}
		for (int i = 0; i < 4; i++) {
			candidates = ga.evolve(candidates);
			for (Candidate c : candidates) {
				System.out.println(i + "th Gen Candidate: " + c);
			}
		}
		System.out.println("\n\n\n\n\n\n\n\n\n");
		System.out.println("Initial Population:");
		for (Candidate c : init) {
			System.out.println("Candidate: " + c);
		}

		System.out.println("Current Generation:");

		for (Candidate c : candidates) {
			System.out.println("Candidate: " + c);
		}
		System.exit(0);
	}

	// TODO Matt: the type of Candidate.fitness was change to Results from
	// Double to interface
	// with OptimizationResults. Alter this code to work with Results instead of
	// Double.
	private ArrayList<Candidate> evolve(ArrayList<Candidate> candidates) {
		// Evaluation -- expensive fitness function evaluations
		for (Candidate candidate : candidates) {
			candidate.evaluate(f);
			System.out.println(candidate);
		}

		// Selection
		// normalizing

		// Double norm = 0.0;
		// for(Candidate candidate : candidates){
		// norm += candidate.getFitness();
		// }
		// for(int i = 0; i < candidates.size(); i++){
		// candidates.get(i).setNormalizedFitness(candidates.get(i).getFitness()/norm);
		// }

		Collections.sort(candidates, Collections.reverseOrder());

		for (int i = 0; i < candidates.size(); i++) {
			Double anf = 0.0;
			for (int j = 0; j <= i; j++) {
				anf += candidates.get(j).getNormalizedFitness();
			}
			candidates.get(i).setAccumlatedNormalizedFitness(anf);
		}

		// constructing breeding population based on accumulated frequency
		int offspringProportion = 2; // make this more clear
		// int numberToSelect = candidates.size()/offspringProportion;
		int numberToSelect = candidates.size() / 3;
		for (Candidate c : candidates)
			System.out
					.println(c + "anf: " + c.getAccumlatedNormalizedFitness());
		ArrayList<Candidate> breedingPopulation = new ArrayList<Candidate>();
		Random r = new Random();
		while (breedingPopulation.size() < numberToSelect) {
			Double R = r.nextDouble();
			for (Candidate c : candidates) {
				if (c.getAccumlatedNormalizedFitness() > R) {
					breedingPopulation.add(c);
					break;
				}
			}
		}
		System.out.println("\n\n\n");

		// Crossover/Mutation
		ArrayList<Candidate> nextGenPopulation = new ArrayList<Candidate>();
		nextGenPopulation.addAll(breedingPopulation);
		nextGenPopulation.addAll(intializePopulation(1));
		Random R = new Random();
		while (nextGenPopulation.size() != candidates.size()) {
			Candidate parentA = breedingPopulation.get(R
					.nextInt(breedingPopulation.size()));
			Candidate parentB = breedingPopulation.get(R
					.nextInt(breedingPopulation.size()));
			Candidate child = makeOffspring(parentA, parentB);
			nextGenPopulation.add(child);
		}

		return nextGenPopulation;
	}

	private Candidate makeOffspring(Candidate A, Candidate B) {
		Random R = new Random();
		Double crossoverConstant = .7, mutationConstant = .6; // adjust these
																// for varying
																// crossover and
																// magnitude of
																// mutations
		// one point crossover
		// switch to two point crossover if there are enough variables
		int initialParent = R.nextInt(2);
		boolean crossed = false;
		ArrayList<Chromosome> childChromosomes = new ArrayList<Chromosome>();
		for (int i = 0; i < A.getChromosomes().size(); i++) {
			Chromosome c;
			if (initialParent == 0) {
				c = A.getChromosomes().get(i);
			} else {
				c = B.getChromosomes().get(i);
			}
			// crossover
			if (R.nextDouble() > crossoverConstant && !crossed) {
				System.out.println("crossover...");
				initialParent = (initialParent + 1) % 2;
				crossed = true;
			}
			// mutation
			if (R.nextDouble() > mutationConstant) {
				Long m = c.getValue() + (R.nextInt(2) - 1);
				while (!(m < c.getBounds().get(1) && m > c.getBounds().get(0))) {
					m = c.getValue() + (R.nextInt(2) - 1);
				}
				c.setValue(m);
			}
			childChromosomes.add(c);
			// if(R.nextDouble() > scalingConstant){
			// System.out.println("Scaling...");
			// Double scale = R.nextDouble()*c.getBounds().get(1).doubleValue();
			// c.setValue(BigDecimal.valueOf(Math.floor(c.getValue()*scale-c.getBounds().get(0).doubleValue())).longValue());
			// }
		}

		return new Candidate(childChromosomes);
	}

	private ArrayList<Candidate> intializePopulation(int populationSize) {
		ArrayList<Candidate> candidateSolutions = new ArrayList<Candidate>();
		Random r = new Random();

		for (int i = 0; i < populationSize; i++) {
			ArrayList<Chromosome> chromosomes = getChromosomeList();
			for (int c = 0; c < chromosomes.size(); c++) {
				Chromosome chromosome = chromosomes.get(c);
				ArrayList<Long> bounds = chromosome.getBounds();
				Long shiftInterval = bounds.get(1) - bounds.get(0);
				chromosome.setValue(r.nextInt(shiftInterval.intValue())
						+ bounds.get(0));
			}
			candidateSolutions.add(new Candidate(chromosomes));
		}
		return candidateSolutions;
	}

	public static ArrayList<Chromosome> getChromosomeList() {
		ArrayList<Chromosome> chromosomes = new ArrayList<Chromosome>();
		// NgonSettings fpm = NgonSettings.getInstance();
		PRINTSettings fpm = PRINTSettings.getInstance();
		try {
			Long[] nbounds = { 3L, 9L };
			Long[] kClosestBounds = { 3L, 9L };
			Long[] distanceBinBounds = { 2L, 11L };
			Long[] sigmaBinBounds = { 2L, 11L };
			Long[] phiBinBounds = { 2L, 11L };
			Long[] rotationRegionsBounds = { 3L, 30L };

			//
			// Long[] nbounds = {3L,10L};
			// Long[] kClosestBounds = {3L,10L};
			// Long[] xBinBounds = {2L, 15L};
			// Long[] yBinBounds = {2L, 15L};
			// Long[] thetaBinBounds = {2L, 15L};
			//
			// chromosomes.add(new Chromosome(fpm.n(), 0L,"N",
			// fpm.n().getClass().getMethod("setValue", Long.class),
			// new ArrayList<Long>(Arrays.asList(nbounds))));
			// chromosomes.add(new Chromosome(fpm.kClosestMinutia(),
			// 0L,"k-closest Minutia",
			// fpm.kClosestMinutia().getClass().getMethod("setValue",
			// Long.class),
			// new ArrayList<Long>(Arrays.asList(kClosestBounds))));
			// chromosomes.add(new Chromosome(fpm.xBins(), 0L, "xBins",
			// fpm.xBins().getClass().getMethod("setValue", Long.class),
			// new ArrayList<Long>(Arrays.asList(xBinBounds))));
			// chromosomes.add(new Chromosome(fpm.yBins(), 0L, "yBins",
			// fpm.yBins().getClass().getMethod("setValue", Long.class),
			// new ArrayList<Long>(Arrays.asList(yBinBounds))));
			// chromosomes.add(new Chromosome(fpm.thetaBins(), 0L, "thetaBins",
			// fpm.thetaBins().getClass().getMethod("setValue", Long.class),
			// new ArrayList<Long>(Arrays.asList(thetaBinBounds))));
			//
			//

			chromosomes.add(new Chromosome(fpm.n(), 0L, "N", fpm.n().getClass()
					.getMethod("setValue", Long.class), new ArrayList<Long>(
					Arrays.asList(nbounds))));

			chromosomes.add(new Chromosome(fpm.kClosestMinutia(), 0L,
					"k-closest Minutia", fpm.kClosestMinutia().getClass()
							.getMethod("setValue", Long.class),
					new ArrayList<Long>(Arrays.asList(kClosestBounds))));

			chromosomes.add(new Chromosome(fpm.distanceBins(), 0L,
					"distanceBins", fpm.distanceBins().getClass()
							.getMethod("setValue", Long.class),
					new ArrayList<Long>(Arrays.asList(distanceBinBounds))));

			chromosomes.add(new Chromosome(fpm.sigmaBins(), 0L, "sigmaBins",
					fpm.sigmaBins().getClass()
							.getMethod("setValue", Long.class),
					new ArrayList<Long>(Arrays.asList(sigmaBinBounds))));

			chromosomes.add(new Chromosome(fpm.phiBins(), 0L, "phiBins", fpm
					.phiBins().getClass().getMethod("setValue", Long.class),
					new ArrayList<Long>(Arrays.asList(phiBinBounds))));

			chromosomes.add(new Chromosome(fpm.rotationRegions(), 0L,
					"rotationRegions", fpm.rotationRegions().getClass()
							.getMethod("setValue", Long.class),
					new ArrayList<Long>(Arrays.asList(rotationRegionsBounds))));
		} catch (Exception e) {

			e.printStackTrace();
		}
		return chromosomes;
	}

}
