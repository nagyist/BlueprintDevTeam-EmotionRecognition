import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import com.sun.tools.internal.ws.wscompile.AbortException;

public class EmotionRecognizer {

	private static EvidenceManager EM = new EvidenceManager(1);
	private static BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
	private static String input ="";
	
	/* TODO Finale Checks:
	 * Kommentare entfernen
	 */
	public static void main(String[] args) {
		
		
		
		EM.getEmotionManager().loadDefault();
		
		//TODO: Folgende Zeile NUR fuer debugging! 
		try {EM.getMeasurementManager().measurementReader("../EmotionRecognition/E_020/E_020.csv");} catch (IOException e) {}
		
		menu();
	}
	
	public static void menu() {
		
		String tmp_input;
		
		while(true) {
			
			System.out.println(" -------------------------------------------------------------------");
			System.out.println("| Menu:                                                             |");
			System.out.println("| (1) Load CSV                                                      |");
			System.out.println("| (2) Set Tolerance (Current: "
									+ String.format("%1.1f", EM.getTolerance()) 
															  + ")                                  |");
			System.out.println("| (3) Emotion Recognition for one Frame (with Details)              |");
			System.out.println("| (4) Emotion Recognition for all Frames                            |");
			System.out.println("| (5) Prove Plausibility for a Single Emotion                       |");
			System.out.println("|                                                                   |");
			System.out.println("| INFO: (a) will abort all submenus and bring you back to this one. |");
			System.out.println(" -------------------------------------------------------------------");
			
			readLine();
			
			while (true) {
				
				if		(input.equals("1")) { loadCSV();   break; } 
				else if	(input.equals("2")) { changeTolerance(); break; }
				
				else if (input.equals("3") || input.equals("4") || input.equals("5")) {
					
					tmp_input = input;
					
					try {
						
						checkMeasurements();
						if 		(tmp_input.equals("3"))	erDetailed();
						else if (tmp_input.equals("4")) EM.dempsterShaferForAll(EM.getMeasurementManager().getMeasurements());
						else if (tmp_input.equals("5")) proveEmotion();
					} catch (AbortException e) {}
					
					break;
				}
				
				else {
					
					System.out.println("Please type the number of the action you want!");
					readLine();
				}
			}
			
			System.out.println();
		}
	}
	
	public static void loadCSV () {
		
		System.out.println("\nINFO: This will not add measurements, but replace the previous CSV");
		System.out.print("Enter Path or (a) to abort and return to menu: ");
		
		while (true) {
			
			try {
				
				input = console.readLine();
				if (input.equals("a")) return;
				EM.getMeasurementManager().measurementReader(input);
				break;
			} catch (IllegalArgumentException e) {
				
				System.out.println("This file does not comply with the requirements.\n"
						+ "It must be a CSV file with the following format:\n"
						+ "1. A header line for your orientation (will be ignored in this program)\n"
						+ "2. The following lines are representing the single frames and must be"
						+ "like this: ID;Speed;Pitch;Intensity whereas \n"
						+ "\t\t- ID is an integer value \n "
						+ "\t\t- Speed is a float value \n"
						+ "\t\t- Pitch and Intensity values are one of the following Strings:\n"
						+ "\t\t\t > 'sehr niedrig' or 'very low' \n"
						+ "\t\t\t > 'niedrig' or 'low' \n"
						+ "\t\t\t > 'normal'\n"
						+ "\t\t\t > 'hoch' or 'high' \n"
						+ "\t\t\t > 'sehr hoch' or 'very high' \n"
						+ "Please enter a correct path!");
			} catch (Exception e) {
				
				System.out.println("Cannot find this file, please enter a correct path!");
			}
		}
		
		System.out.println("Successfully Loaded.");
	}

	public static void changeTolerance () {
		
		System.out.println("\nEnter tolerance value or (a) to abort and return to menu: ");
		
		while (true) {
			
			readLine();
			
			try {

				if (input.equals("a")) return;
				EM.setTolerance(Integer.parseInt(input));
				break;
			} catch (Exception e) {
				
				System.out.println("You have to enter a valid integer number between 0 and 3!");
			}
		}
	}
	
	public static void erDetailed () {
	
		System.out.println("\nEnter frame number or (a) to abort and return to menu: ");
		
		while (true) {
			
			readLine();
			
			try {

				if (input.equals("a")) return;
				Measurement m = EM.getMeasurementManager().getMeasurement(Integer.parseInt(input) - 1);
				EM.dempsterShaferDetailed(m);
				break;
			} catch (Exception e) {
				
				System.out.println("You don't have a frame with the ID #" + (Integer.parseInt(input) - 1) + " in your data, "
						+ "please enter a valid frame number!");
			}
		}
	}
	
	public static void proveEmotion () {
		
		System.out.println("\nEnter the name of the emotion you want to prove or (a) to abort and return to menu: ");
		
		while (true) {
			
			readLine();
			
			try {

				if (input.equals("a")) return;
				EM.dempsterShaferProveEmotion(EM.getMeasurementManager().getMeasurements(), input);
				break;
			} catch (Exception e) {
				
				System.out.println("You have to enter one of the following emotions:");
				for (Emotion emotion : EM.getEmotionManager().getEmotions()) {
				
					System.out.print(emotion.getName() + " ");
				}
				System.out.println();
			}
		}
	}
	
	public static void checkMeasurements() throws AbortException {
		
		if(input.equals("a")) throw new AbortException();
		
		if (EM.getMeasurementManager().getMeasurements().isEmpty()) {
			
			System.out.println("\nYou have no measurements. Please load a CSV with your measurements."
					+ "\nIf you have done this already your file might have the wrong format."
					+ "\nIn this case please read the documentation to see what format is needed.");
			
			loadCSV();
			checkMeasurements();
			return;
		}
	}
	
	public static void readLine () {
		
		try {
			
			input = console.readLine();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
}
