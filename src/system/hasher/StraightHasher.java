package system.hasher;

import java.math.BigInteger;
import java.util.ArrayList;

import system.allcommonclasses.*;
import system.allcommonclasses.commonstructures.Template;
import system.allcommonclasses.modalities.Biometric;
import system.allcommonclasses.transformations.*;

/**
 * 
 * Hashes the set using a standard hash function and scores by set intersection.
 *
 */
public class StraightHasher extends Hasher {

	@Override
	public Template hashEnrollTemplate(Template template) {
		
		Transformation hashFunction = new SHA2();
		for(BigInteger bigInt : template.getHashes()){
			bigInt = hashFunction.transform(bigInt); //FIXME
		}
		
		return template;
	}
	

	@Override
	public ArrayList<Template> hashTestTemplates(ArrayList<Template> templates) {
		
		Transformation hashFunction = new SHA2();
		for(Template template : templates){
			for(BigInteger bigInt : template.getHashes()){
				bigInt = hashFunction.transform(bigInt); //FIXME 
			}
		}
		
		return templates;
	}

	@Override
	public Double compareTemplates(Template enrolledTemplate, ArrayList<Template> testTemplates) {
	// returns to maximum set intersection between the enrolled template and a test template
		Double maxScore = Double.NEGATIVE_INFINITY;
		Double score;
		for(Template template : testTemplates){
			score = 0.0;
			for(BigInteger hash : template.getHashes()){
				if(enrolledTemplate.getHashes().contains(hash)){
					score += 1.0;
				}
			}
			if(score > maxScore){
				maxScore = score;
			}
		}
		return maxScore;
	}


	// TODO Jesse - Straight Hasher Indexing
	
}
