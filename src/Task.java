import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Task {
	public static ArrayList<ArrayList<Integer>> backendPeopleDates, frontendPeopleDates, QAPeopleDates;
	
	public static ArrayList<String> taskName = new ArrayList();
	
	public static boolean notAddedFeBacklog = true;
	public static boolean notAddedQaBacklog = true;

	
	public static ArrayList<Integer> backlogBackend = new ArrayList();
	public static ArrayList<Integer> backlogFrontend = new ArrayList();
	public static ArrayList<Integer> backlogQuality = new ArrayList();
	public static ArrayList<Integer> backlogSample = new ArrayList();

	
	// garbage map for previous work for back-end which will never come to use as there was no previous work for be
	public static HashMap<Integer, Integer> backendGarbageMap = new HashMap<>();
	// maps for the record which task is done at which day, so that we know next person can't do the same task this day.
	public static HashMap<Integer, Integer> backendMap = new HashMap<>();
	public static HashMap<Integer, Integer> frontendMap = new HashMap<>();
	public static HashMap<Integer, Integer> qualityMap = new HashMap<>();
	
	// pointer which will tell us about which person is doing which task currently. Integer not taken when passed to the function as it doesn't change the value of static Integer.
	public static int[] beCurr , feCurr , qaCurr;
	public static int[]  wholeBeCurr = {0}, wholeFeCurr = {0}, wholeQaCurr = {0}; // total be people
	
	public static int[] backendWorkload;
	public static int[] frontendWorkload;
	public static int[] QAWorkload;
	
	// Indices
	// 0 -> Hero banner
	// 1 -> News
	// 2 -> Contact us
	
	public static String[][] output; // output
	
	public static boolean[] isBackendBusy;
	public static boolean[] isFrontendBusy;
	public static boolean[] isQualityBusy;

	
	// methods
	
	public static boolean isBeDone(int beId) {
		if(beCurr[beId] >= backendWorkload.length) return true;
		return false;
	}
	
	public static boolean isFeDone(int feId) {
		if(feCurr[feId] >= frontendWorkload.length) return true;
		return false;
	}
	
	public static boolean isQaDone(int qaId) {
		if(qaCurr[qaId] >= QAWorkload.length) return true;
		return false;
	}
	
	//
	public static boolean isBeTaskDone(int task) {
		if(backendWorkload[task] == 0) return true;
		return false;
	}
	
	public static boolean isFeTaskDone(int task) {
		if(frontendWorkload[task] == 0) return true;
		return false;
	}
	
	public static String addOutput(int task) {
		return taskName.get(task);
	}
	
	// main
	
	public static void main(String[] args) {
		// buffer input
		readingInput();
		
		backendWorkload = new int[workloadSheet.size()-1];
		frontendWorkload = new int[workloadSheet.size()-1];
		QAWorkload = new int[workloadSheet.size()-1];
		
		// work-sheet input
		for(int i = 1; i < workloadSheet.size(); i++) {
			taskName.add(workloadSheet.get(i)[1]);
			for(int j = 2; j < workloadSheet.get(i).length; j++) {
				if(j==2) {
					backendWorkload[i-1] = Integer.parseInt(workloadSheet.get(i)[j]);
				}
				else if(j==3) {
					frontendWorkload[i-1] = Integer.parseInt(workloadSheet.get(i)[j]);
				}
				else if(j==4) {
					QAWorkload[i-1] = Integer.parseInt(workloadSheet.get(i)[j]);
				}
			}
		}
		

		ArrayList<Integer> backendOutputID = new ArrayList();
		ArrayList<Integer> frontendOutputID = new ArrayList();
		ArrayList<Integer> QAOutputID = new ArrayList();
		
		// dates input
		// index 0 => BE, 1 => FE, 2 => QA
		backendPeopleDates = new ArrayList<ArrayList<Integer>>();
		frontendPeopleDates = new ArrayList<ArrayList<Integer>>();
		QAPeopleDates = new ArrayList<ArrayList<Integer>>();
		
		HashMap<Integer, String[]> namesAndIDMap = new HashMap<>();
		
		String string1 = Arrays.toString(tasksheet.get(0));
		string1 = string1.substring(1, string1.length()-1);
		
		int outputTestID = 0;
		for(int i = 1; i < tasksheet.size(); i++) {
			ArrayList<ArrayList<Integer>> hi = new ArrayList<ArrayList<Integer>>();
			
			if(tasksheet.get(i)[1].equals("BE")) {
				hi = backendPeopleDates;
				namesAndIDMap.put(outputTestID, new String[] {tasksheet.get(i)[0], "BE"});
				backendOutputID.add(outputTestID++);
			}
			
			else if(tasksheet.get(i)[1].equals("FE")) { 
				hi = frontendPeopleDates;
				namesAndIDMap.put(outputTestID, new String[] {tasksheet.get(i)[0], "FE"});
				frontendOutputID.add(outputTestID++);
			}
			
			else if(tasksheet.get(i)[1].equals("QA")) {
				hi = QAPeopleDates;
				namesAndIDMap.put(outputTestID, new String[] {tasksheet.get(i)[0], "QA"});
				QAOutputID.add(outputTestID++);
			}
			
			ArrayList<Integer> insideEngineer = new ArrayList();
			
			for(int j = 2; j < tasksheet.get(i).length; j++) {
				insideEngineer.add(Integer.parseInt(tasksheet.get(i)[j]));
			}
			hi.add(insideEngineer);
		}
		

		
		int totalBackendPeople = backendPeopleDates.size();
		int totalFrontendPeople = frontendPeopleDates.size();
		int totalQAPeople = QAPeopleDates.size();
		
		int totalPeople = totalBackendPeople + totalFrontendPeople + totalQAPeople;
		
		beCurr = new int[totalBackendPeople];
		feCurr = new int[totalFrontendPeople];
		qaCurr = new int[totalQAPeople];
		
		// busy or not at specific time
		isBackendBusy = new boolean[totalBackendPeople];
		isFrontendBusy = new boolean[totalFrontendPeople];
		isQualityBusy = new boolean[totalQAPeople];
		
		int totalNumberOfDates = backendPeopleDates.get(0).size();
		
		output = new String[totalPeople][totalNumberOfDates];
		
		// back-end backlog
		for(int i=0; i<taskName.size(); i++) {
			backlogBackend.add(i);
		}
//		
		for(int date=0; date<totalNumberOfDates; date++) {
			// For Back-end
			for(int id=0; id<totalBackendPeople; id++) {
				boolean isBeDone = isBeDone(id);
				
				int outputId = backendOutputID.get(id);
				
				
				if(!isBeDone) {
					fillOutput( backendPeopleDates.get(id), isBeDone, true /* because nothing previous */, backendMap, backendGarbageMap, backendWorkload, beCurr,
							id, outputId /* for FE outputArray */, date, wholeBeCurr, backlogBackend, backlogFrontend, isBackendBusy, true);
				}
				else output[outputId][date] = "Spare day";
			}
			
			// For Front-end
			for(int id=0; id<totalFrontendPeople; id++) {
				boolean isFeDone = isFeDone(id);
				
				int outputId = frontendOutputID.get(id);
				
				if(backlogFrontend.size() != 0 && notAddedFeBacklog) {
					notAddedFeBacklog = false;
					Arrays.fill(feCurr, backlogFrontend.get(0));
				}
				
				if(!isFeDone) {
					fillOutput( frontendPeopleDates.get(id), isFeDone, isBeTaskDone(feCurr[id]), frontendMap, backendMap, frontendWorkload, feCurr,
							id, outputId, date, wholeFeCurr, backlogFrontend, backlogQuality, isFrontendBusy, false);
				}
				else output[outputId][date] = "Spare day     ";
			}
			
			// For Quality 
			for(int id=0; id<totalQAPeople; id++) {
				boolean isQaDone = isQaDone(id);
				
				int outputId = QAOutputID.get(id);
				
				if(backlogQuality.size() != 0 && notAddedQaBacklog) {
					notAddedQaBacklog = false;
					Arrays.fill(qaCurr, backlogQuality.get(0));
				}
				
				if(!isQaDone) {
					fillOutput( QAPeopleDates.get(id), isQaDone, isFeTaskDone(qaCurr[id]), qualityMap, frontendMap, QAWorkload, qaCurr,
							id, outputId, date, wholeQaCurr, backlogQuality, backlogSample, isQualityBusy, false);
				}
				else output[outputId][date] = "Spare day";
			}
		}
		
		ArrayList<String> outputString = new ArrayList();
		outputString.add(string1);
		
		for(int index = 0; index < output.length; index++) {
			String[] outputt = output[index];
			string1 = Arrays.toString(outputt);
			string1 = string1.substring(1, string1.length()-1);
			
			String name = namesAndIDMap.get(index)[0];
			String specialization = namesAndIDMap.get(index)[1];
			
			outputString.add(name + ", " + specialization + ", " + string1);
		}
		for(String s : outputString)
			System.out.println(s);
		
		
	}

	public static void fillOutput( ArrayList<Integer> erDates, boolean isCurrentEngineerDone, boolean isPreviousEngineerDone, HashMap<Integer, Integer> currentEngineerMap, 
			HashMap<Integer, Integer> previousEngineerMap, int[] erWork, int[] currentEngineerTask, int id, int outputId, int date, int[] wholeErCurr, 
			ArrayList<Integer> backlog, ArrayList<Integer> backlogOfNextEngineer, boolean[] isEngineerBusy, boolean personIsBackend) {
		
		// ALL(BE, FE, QA);
		if(erDates.get(date) == 1) {
			if(!isCurrentEngineerDone) {
				
				if(!isEngineerBusy[id]) {
					if(wholeErCurr[0]<backlog.size()) {
						currentEngineerTask[id] = backlog.get(wholeErCurr[0]++); // wholecurr is the pointer which maintains pointer to backlog
						isEngineerBusy[id] = true;
					}
					else {
						output[outputId][date] = "Spare day";
						return;
					}
					
					// for task done on same day or not by previous engineer 
					boolean isWorkDoneOnSameDay = true;
					if(personIsBackend) {
						isWorkDoneOnSameDay = false; // because of be, as it's not dependent on any other
					}
					else if(isPreviousEngineerDone && previousEngineerMap.get(currentEngineerTask[id]) != date) {
						isWorkDoneOnSameDay = false; // because of previous
					}
					// check for isPrevWorkDoneOnSameDay
					if(isWorkDoneOnSameDay) {
						output[outputId][date] = "Spare day";
						return;
					}
				}
				
				String out = addOutput(currentEngineerTask[id]); // extracting the output
				output[outputId][date] = out;		// filling the output			
				erWork[currentEngineerTask[id]]--;	// subtracting the work-day	
				
				if(erWork[currentEngineerTask[id]] == 0) {
					backlogOfNextEngineer.add(currentEngineerTask[id]); // backlog add-on for next engineer
					
					currentEngineerMap.put(currentEngineerTask[id], date); // adding the dates when task is done
					
					if(wholeErCurr[0] >= backlog.size()) {
						currentEngineerTask[id] = wholeErCurr[0];
					}
					isEngineerBusy[id] = false; // making person free
				}

			} else {
				output[outputId][date] = "Spare day";
			}						
		} else {
			// leave 
			output[outputId][date] = "Not Available";
		}
		
	}
	
	public static ArrayList<String[]> workloadSheet = new ArrayList();
	public static ArrayList<String[]> tasksheet = new ArrayList();
	
	public static void readingInput() {
		String workloadCsvPath = "/Users/Sourav.Sharma/Downloads/Project_estimation.csv";
		String tasksheetCsvPath ="/Users/Sourav.Sharma/Downloads/Tasksheet.csv";
		
		String workloadString = "";
		String tasksheetString = "";
		
		try {
			BufferedReader bufferWorkload= new BufferedReader(new FileReader(workloadCsvPath));
			BufferedReader bufferTasksheet= new BufferedReader(new FileReader(tasksheetCsvPath));
			
			while((workloadString = bufferWorkload.readLine()) != null){
				// 18001020123
				String[] values= workloadString.split(",");
				workloadSheet.add(values);
			}
			
			while((tasksheetString = bufferTasksheet.readLine()) != null){
				String[] values= tasksheetString.split(",");
				tasksheet.add(values);
			}
			
		} catch(FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
