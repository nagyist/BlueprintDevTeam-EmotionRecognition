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
	 */
	public static void main(String[] args) throws	IOException, NoSuchFieldException, 
													SecurityException, IllegalArgumentException, 
													NoSuchMethodException, IllegalAccessException, 
													InvocationTargetException {
		
		EvidenceManager em = new EvidenceManager(1);

		em.EmotionManager.loadDefault();
		
		// TODO: Folgende Zeile nur fuer debugging! 
		em.MeasurementManager.measurementReader("/Users/sophie/Projekte/eese/dempster/EmotionRecognition/E_020/E_020.csv");
		
		menu(em);
	}
	
	public static void menu(EvidenceManager em) throws IOException, SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
		
		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
		String input = null;
		
		System.out.println(" ------------------------------------------------------");
		System.out.println("| Menu:                                                |");
		System.out.println("| (1) Load CSV                                         |");
		System.out.println("| (2) Emotion Recognition with Details (for one frame) |");
		System.out.println("| (3) Emotion Recognition for all Measurements         |");
		System.out.println(" ------------------------------------------------------");
		input = console.readLine();
		
		while (!input.equals("1") && !input.equals("2") && !input.equals("3") && !input.equals("4")) {
			
			System.out.println("Please type the number of the action you want ... ");
			input = console.readLine();
		}
		
		if(input.equals("1")) loadCSV(em);
		if(input.equals("2")) erDetailed(em);
		if(input.equals("3")) erAll(em);
	}
	
	public static void loadCSV (EvidenceManager em) throws IOException, SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
		
		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
		String input = null;
		
		System.out.println("\nLoad CSV");
		System.out.println("INFO: This will replace the previous CSV");
		System.out.print("Enter Path or (a) to abort: ");
		input = console.readLine();
		
		if (input.equals("a")) {
			
			System.out.println("\n");
			menu(em);
		} else {
			
			try {
			
				em.MeasurementManager.measurementReader(input);
			} catch (FileNotFoundException e) {
				
				System.out.println("****** ERROR: Cannot find this file! ******");
				loadCSV(em);
			}
		}

	}

	public static void erDetailed (EvidenceManager em) throws IOException, SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
				
		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
		int index = 0;
		
		if (em.MeasurementManager.getMeasurements().isEmpty()) {
			
			System.out.println("You have no measurements. Please load a CSV with your measurements."
					+ "\nIf you have done this already your file might have the wrong format."
					+ "\nIn this case please read the documentation to see what format is needed.");
			
			loadCSV(em);
		}
		
		System.out.println("Enter frame number or (a) to abort: ");
		index = Integer.parseInt(console.readLine());
		
		try {
			
			Measurement m = em.MeasurementManager.getMeasurement(index);
			em.dempsterShaferDetailed(m);
		} catch (Exception e) {
			
			System.out.println("You don't have frame #" + index + " in your data!");
			erDetailed(em);
		}
		
		System.out.println("\n");
		menu(em);
	}
	
	public static void erAll (EvidenceManager em) throws SecurityException, IllegalArgumentException, IOException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
		
		em.dempsterShaferForAll(em.MeasurementManager.getMeasurements());
		
		System.out.println("\n");
		menu(em);
	}
}
