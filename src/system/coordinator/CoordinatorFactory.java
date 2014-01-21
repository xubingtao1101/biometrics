package system.coordinator;


import system.allcommonclasses.commonstructures.RawScores;
import system.allcommonclasses.commonstructures.Users;
import system.allcommonclasses.indexingstructure.IndexingStructure;
import system.allcommonclasses.indexingstructure.IndexingStructureFactory;
import system.allcommonclasses.settings.GlobalSettings;
import system.coordinator.tests.TestGeneratorFactory;
import system.hasher.Hasher;
import system.hasher.HasherFactory;

public class CoordinatorFactory {
	 	
	public static Coordinator makeCoordinator(Users users){
		
		Hasher hasher = HasherFactory.makeHasher();
		
		Coordinator firstCoordinator = new CoordinatorFactory().new BaseCoordinator(hasher, users);
		Coordinator tempCoordinator;
	
		switch(matchingCoordinatorEnumerator.valueOf(GlobalSettings.getInstance().getMatchingCoordinator())){
			case DEFAULTTESTING:
				firstCoordinator = addToFront(new DefaultTesting(hasher, users, TestGeneratorFactory.makeTestGenerator()), firstCoordinator);
				break;		
			case DEFAULTTESTINGPREQUANTIZED:
				firstCoordinator = addToFront(new DefaultTestingPrequantized(hasher, users, TestGeneratorFactory.makeTestGenerator()), firstCoordinator);
				break;
			case NONE:
				break;				
			default:
				System.out.println("You didn't provide an appropriate matching coordinator");
				break;
		}		
		
		switch(indexingCoordinatorEnumerator.valueOf(GlobalSettings.getInstance().getIndexingCoordinator())){
			case INDEXING:
				firstCoordinator = addToFront(new IndexTesting(hasher, users, IndexingStructureFactory.makeIndexingStructure()), firstCoordinator);
				break;		
			case NONE:
				break;				
			default:
				System.out.println("You didn't provide an appropriate indexing coordinator");
				break;
		}	
		
		switch(histogramCoordinatorEnumerator.valueOf(GlobalSettings.getInstance().getHistogramCoordinator())){
			case HISTOGRAM:
				firstCoordinator = addToFront(new Histogram(hasher, users), firstCoordinator);
				break;		
			case NONE:
				break;				
			default:
				System.out.println("You didn't provide an appropriate histogram coordinator");
				break;
		}	
		
		
		return firstCoordinator;
	}
	
	
	
	
	private static Coordinator addToFront(Coordinator coordinatorToAdd, Coordinator frontCoordinator){
		coordinatorToAdd.nextCoordinator = frontCoordinator;
		return coordinatorToAdd;
	}
	
	private enum matchingCoordinatorEnumerator{
		DEFAULTTESTING, DEFAULTTESTINGPREQUANTIZED, NONE;
	}
	
	private enum indexingCoordinatorEnumerator{
		INDEXING, NONE;
	}
	
	private enum histogramCoordinatorEnumerator{
		HISTOGRAM, NONE;
	}
	
	

	
	private class BaseCoordinator extends Coordinator{

		public BaseCoordinator(Hasher hasher, Users users) {
			super(hasher, users);
		}

		@Override
		public RawScores run() {
			return new RawScores();
		}

	}
}
