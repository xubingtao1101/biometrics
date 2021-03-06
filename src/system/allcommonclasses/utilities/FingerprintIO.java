package system.allcommonclasses.utilities;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import system.allcommonclasses.commonstructures.User;
import system.allcommonclasses.commonstructures.Users;
import system.allcommonclasses.modalities.Fingerprint;
import system.allcommonclasses.modalities.Minutia;


/**
 * This file should be run by itself, really only when creating a new serialized dataset
 * (ie, it shouldn't be used very often)
 * !!! Unless you adding a new FVC dataset, you probably shouldn't be looking at this code !!!
 *
 *
 */

public class FingerprintIO {

	public static void readAllFVC(){
		Users users;
		for(int year=2002; year<=2006; year = year+2){
			for(int db=1; db<=4; db++){
				users = readFVC(year, db);
				try{
					FileOutputStream fileOut = new FileOutputStream("datasets/fingerprint/FVC" + year + "DB" + db + ".ser");
					ObjectOutputStream out = new ObjectOutputStream(fileOut);
					out.writeObject(users);
					out.close();
					fileOut.close();
					System.out.println("Done");
				}catch(IOException exp){
					exp.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Run this function from the main method at the bottom to generate a training set and a holdout set from all 4 databases of year 2002
	 * NOTE: As of now you need to change 'year' manually
	 */
	public static void generateTrainingAndTestDatabases() {
		Users trainingUsers = new Users();
		Users testUsers = new Users();
		int year = 2002;
		for(int db=1; db<=4; db++){
			trainingUsers.users.addAll(readOddFVC(year, db).users); //add these to the existing training users set
			testUsers.users.addAll(readEvenFVC(year, db).users);	
		}
		//output test users
		try{
			FileOutputStream fileOut = new FileOutputStream("datasets/fingerprint/FVC" + year + "Testing.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(testUsers);
			out.close();
			fileOut.close();
			System.out.println("Testing Database Output Done");
		}catch(IOException exp){
			exp.printStackTrace();
		}
		//output training users
		try{
			FileOutputStream fileOut = new FileOutputStream("datasets/fingerprint/FVC" + year + "Training.ser");
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(trainingUsers);
			out.close();
			fileOut.close();
			System.out.println("Training Database Output Done");
		}catch(IOException exp){
			exp.printStackTrace();
		}
		
	}
	
	/**
	 * Run this function from the main method at the bottom to generate all 4 small databases of year 2002
	 * NOTE: As of now you need to change 'year' manually
	 */
	public static void generateSmallDatabases() {
		int year = 2002;
		for(int db=1; db<=4; db++){
			Users trainingUsers = new Users();
			trainingUsers.users.addAll(readTenFVC(year, db).users); //add these to the existing training users set
			try{
				FileOutputStream fileOut = new FileOutputStream("datasets/fingerprint/FVC" + year + db+"Small.ser");
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(trainingUsers);
				out.close();
				fileOut.close();
				System.out.println("Testing Database Output Done");
			}catch(IOException exp){
				exp.printStackTrace();
			}
		}
		//output test users

	}
	
	public static Fingerprint fingerprintFromFile(String file){
		Fingerprint fingerprint = new Fingerprint();
		
		try {
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream bis = new BufferedInputStream(fis);
			DataInputStream dis = new DataInputStream(bis);

			dis.readLine();
			dis.readLine();
			dis.readLine();
			dis.readLine();
			dis.readLine();
			
			Long index = 0L;
			
			while (dis.available() != 0) {
				String minutiaText = dis.readLine();
				Minutia minutia = new Minutia();

				//System.out.println(minutiaText);
				String[] parseIt = minutiaText.split(" ");
				
				minutia.setX(Long.valueOf(parseIt[0]));
				minutia.setY(Long.valueOf(parseIt[1]));
				minutia.setTheta(Long.valueOf(parseIt[2]));
				minutia.setIndex(index);
				index++;
				
				fingerprint.minutiae.add(minutia);
			}
			
			fis.close();
			bis.close();
			dis.close();
 
		} catch (IOException e) {
			e.printStackTrace();
		}
		//System.out.println(fingerprint);
		return fingerprint;
	}
	
	public static Users readFVC(int year, int db){
		Users users = new Users();
		ArrayList<Fingerprint> allFingerprints = new ArrayList<Fingerprint>();
		for(int id=1; id<=100; id++){
			User user = new User();
			user.id = id-1L;
			ArrayList<Fingerprint> fingerprints = new ArrayList<Fingerprint>();
			user.readings = fingerprints;
			
			String directoryPathForFVC = "/Users/thomaseffland/Documents/";
			
			for(int reading=1; reading<=8; reading++){
				String file = directoryPathForFVC + "CUBS_FP_DATA/FVC" +year+ "/DB" +db+ "/features/" 
						+ id +"_"+ reading + ".fp";
				fingerprints.add(fingerprintFromFile(file));
				allFingerprints.add(fingerprintFromFile(file));
			}

			//System.out.println("id: " + id);
			users.users.add(user);
			//System.out.println(user.readings);
		}		
		return users;
	}
	/**
	 * readEven and readOdd are just slight changes to the plain readFVC 
	 * (to get only the even or odd users... but you probably guessed that already)
	 * 
	 * @param year
	 * @param db
	 * @return
	 */
	public static Users readOddFVC(int year, int db){
		Users users = new Users();
		ArrayList<Fingerprint> allFingerprints = new ArrayList<Fingerprint>();
		for(int id=1; id< 100; id+=2){
			User user = new User();
			user.id = id-1L;
			ArrayList<Fingerprint> fingerprints = new ArrayList<Fingerprint>();
			user.readings = fingerprints;
			
			String directoryPathForFVC = "/Users/thomaseffland/Documents/";
			
			for(int reading=1; reading<=8; reading++){
				String file = directoryPathForFVC + "CUBS_FP_DATA/FVC" +year+ "/DB" +db+ "/features/" 
						+ id +"_"+ reading + ".fp";
				fingerprints.add(fingerprintFromFile(file));
				allFingerprints.add(fingerprintFromFile(file));
			}

			//System.out.println("id: " + id);
			users.users.add(user);
			//System.out.println(user.readings);
		}		
		return users;
	}
	public static Users readEvenFVC(int year, int db){
		Users users = new Users();
		ArrayList<Fingerprint> allFingerprints = new ArrayList<Fingerprint>();
		for(int id=2; id<=100; id+=2){
			User user = new User();
			user.id = id-1L;
			ArrayList<Fingerprint> fingerprints = new ArrayList<Fingerprint>();
			user.readings = fingerprints;
			
			String directoryPathForFVC = "/Users/thomaseffland/Documents/";
			
			for(int reading=1; reading<=8; reading++){
				String file = directoryPathForFVC + "CUBS_FP_DATA/FVC" +year+ "/DB" +db+ "/features/" 
						+ id +"_"+ reading + ".fp";
				fingerprints.add(fingerprintFromFile(file));
				allFingerprints.add(fingerprintFromFile(file));
			}

			//System.out.println("id: " + id);
			users.users.add(user);
			//System.out.println(user.readings);
		}		
		return users;
	}
	/**
	 * Genereate small db's for quick testing
	 * @param year
	 * @param db
	 * @return
	 */
	public static Users readTenFVC(int year, int db){
		Users users = new Users();
		ArrayList<Fingerprint> allFingerprints = new ArrayList<Fingerprint>();
		for(int id=1; id<=10; id++){
			User user = new User();
			user.id = id-1L;
			ArrayList<Fingerprint> fingerprints = new ArrayList<Fingerprint>();
			user.readings = fingerprints;
			
			String directoryPathForFVC = "/Users/thomaseffland/Documents/";
			
			for(int reading=1; reading<=8; reading++){
				String file = directoryPathForFVC + "CUBS_FP_DATA/FVC" +year+ "/DB" +db+ "/features/" 
						+ id +"_"+ reading + ".fp";
				fingerprints.add(fingerprintFromFile(file));
				allFingerprints.add(fingerprintFromFile(file));
			}

			//System.out.println("id: " + id);
			users.users.add(user);
			//System.out.println(user.readings);
		}		
		return users;
	}
	
	/**
	 * So this runs separately from 
	 * @param args
	 */
	public static void main(String[] args) {
		new FingerprintIO();
		//generateTrainingAndTestDatabases();
		generateSmallDatabases();
		//readAllFVC();
	}
}


