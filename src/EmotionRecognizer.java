import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;

public class EmotionRecognizer {

	/* TODO Finale Checks:
	 * Kommentare entfernen
	 * Einheitliche Bezeichner (z.B. e ist immer Emotionion und nicht mal Emotion und mal Emotionen-Liste)
	 * Was public, was private, was ... ???
	 * Konventionen (Gro§-/Kleinschreibung)
	 * richtige Verwendung von Frame/Takt/Beat,...
	 * unnštige Exceptions entfernen
	 */
	public static void main(String[] args) throws	IOException, NoSuchFieldException, 
													SecurityException, IllegalArgumentException, 
													NoSuchMethodException, IllegalAccessException, 
													InvocationTargetException {
		
		EvidenceManager EM = new EvidenceManager(1);
		
		EM.getEmotionManager().loadDefault();
		
		// TODO: Folgende Zeile NUR fuer debugging! 
		EM.getMeasurementManager().measurementReader("/Users/sophie/Projekte/eese/dempster/EmotionRecognition/E_020/E_020.csv");
		
		menu(EM);
	}
	
	public static void menu(EvidenceManager em) throws	IOException, SecurityException, 
														IllegalArgumentException, 
														NoSuchMethodException, 
														IllegalAccessException, 
														InvocationTargetException, 
														NoSuchFieldException {
		
		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
		String input = null;
		
		
		while(true) {
			
			System.out.println(" ------------------------------------------------------");
			System.out.println("| Menu:                                                |");
			System.out.println("| (1) Load CSV                                         |");
			System.out.println("| (2) Set Tolerance (Current: "
									+ String.format("%1.1f", em.getAccuracy()) 
															  + ")                     |");
			System.out.println("| (3) Emotion Recognition with Details (for one frame) |");
			System.out.println("| (4) Emotion Recognition for all Measurements         |");
			System.out.println("| (5) Prove Plausibility for a Single Emotion          |");
			System.out.println(" ------------------------------------------------------");
			
			input = console.readLine();
			
			while (true) {
				
				if		(input.equals("1")) { loadCSV(em);		break; }
				else if	(input.equals("2")) { changeAcc(em);	break; }
				else if (input.equals("3")) { erDetailed(em);	break; }
				else if (input.equals("4")) { 
					
					em.dempsterShaferForAll(em.getMeasurementManager().getMeasurements());
					break; 
				}
				else if (input.equals("5")) { proveEmotion(em);	break; }
				else {
					
					System.out.println("Please type the number of the action you want!");
					input = console.readLine();
				}
			}
			
			System.out.println();
		}
	}
	
	public static void loadCSV (EvidenceManager em) throws 	IOException, SecurityException, 
															IllegalArgumentException, 
															NoSuchMethodException, 
															IllegalAccessException, 
															InvocationTargetException, 
															NoSuchFieldException {
		
		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
		String input = "";
		
		System.out.println("\nINFO: This will not add measurements, but replace the previous CSV");
		System.out.print("Enter Path or (a) to abort: ");
		
		while (true) {
			
			input = console.readLine();
			
			try {
				
				if (input.equals("a")) return;
				em.getMeasurementManager().measurementReader(input);
				break;
			} catch (FileNotFoundException e) {
				
				System.out.println("Cannot find this file, please enter a correct path!");
			}	
		}
		
		System.out.println("Successfully Loaded.");
	}

	public static void changeAcc (EvidenceManager em) throws SecurityException, IllegalArgumentException, IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
		
		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
		String input ="";
		float acc = 0;
		
		System.out.println("\nEnter tolerance value or (a) to abort: ");
		
		while (true) {
			
			input = console.readLine();
			
			try {

				if (input.equals("a")) return;
				acc = Float.parseFloat(input);
				em.setAccuracy(acc);
				break;
			} catch (Exception e) {
				
				System.out.println("You have to enter a valid number between 0 and 3!");
			}
		}
	}
	
	public static void erDetailed (EvidenceManager em) throws	IOException, SecurityException, 
																IllegalArgumentException, 
																NoSuchMethodException, 
																IllegalAccessException, 
																InvocationTargetException, 
																NoSuchFieldException {
				
		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
		String input = ""; 
		int index = 0;
		
		if (em.getMeasurementManager().getMeasurements().isEmpty()) {
			
			System.out.println("\nYou have no measurements. Please load a CSV with your measurements."
					+ "\nIf you have done this already your file might have the wrong format."
					+ "\nIn this case please read the documentation to see what format is needed.");
			
			loadCSV(em);
			erDetailed(em);
			return;
		}
		
		System.out.println("\nEnter frame number or (a) to abort: ");
		
		while (true) {
			
			input = console.readLine();
			
			try {

				if (input.equals("a")) return;
				index = Integer.parseInt(input);
				Measurement m = em.getMeasurementManager().getMeasurement(index);
				em.dempsterShaferDetailed(m);
				break;
			} catch (Exception e) {
				
				System.out.println("You don't have a frame with the ID #" + index + " in your data, "
						+ "please enter a valid frame number!");
			}
		}
	}
	
	public static void proveEmotion (EvidenceManager em) throws IOException {
		
		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
		String input = "";
		
		System.out.println("\nEnter the name of the emotion you want to prove or (a) to abort: ");
		
		while (true) {
			
			input = console.readLine();
			
			try {

				if (input.equals("a")) return;
				em.dempsterShaferProveEmotion(em.getMeasurementManager().getMeasurements(), input);
				break;
			} catch (Exception e) {
				
				System.out.println("You have to enter one of the following emotions:");
				for (Emotion emotion : em.getEmotionManager().getEmotions()) {
				
					System.out.print(emotion.getName() + " ");
				}
				System.out.println();
			}
		}
	}
}
