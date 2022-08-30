import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Task {
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
	public static int[] beCurr = {0, 0, 0}, feCurr = {0, 0}, qaCurr = {0, 0};
	public static int[]  wholeBeCurr = {0}, wholeFeCurr = {0}, wholeQaCurr = {0}; // total be people
	
	public static int[] beWork = {4, 3, 2};
	public static int[] feWork = {3, 4, 3};
	public static int[] qaWork = {2, 2, 1};
	
//	public static ArrayList<Integer> backendWorkload, frontendWorkload, QAWorkload;
	
	public static String[] tasks = {"Hero Banner   "   , "Featured News ", "Contact Us    "};
	
//	public static ArrayList<String> taskName = new ArrayList();

	// Indices
	// 0 -> Hero banner
	// 1 -> News
	// 2 -> Contact us
	
//	public static ArrayList<ArrayList<Integer>> allEngineerDates = new ArrayList();
																														
	public static int[] beDates =  {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	public static int[] be2Dates = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	public static int[] be3Dates =  {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	
	public static int[] feDates =  {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	public static int[] fe2Dates = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	
	public static int[] qaDates =  {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	public static int[] qa2Dates =  {0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
	
	public static int[][] allbeDates = {beDates, be2Dates, be3Dates}, allfeDates = {feDates, fe2Dates}, allqaDates = {qaDates, qa2Dates};
	
	
	public static String[][] output = new String[7][beDates.length]; // output
	
	public static boolean[] isBackendBusy = {false, false, false};
	public static boolean[] isFrontendBusy = {false, false};
	public static boolean[] isQualityBusy = {false, false};

	
	// methods
	
	public static boolean isBeDone(int beId) {
		if(beCurr[beId] >= beWork.length) return true;
		return false;
	}
	
	public static boolean isFeDone(int feId) {
		if(feCurr[feId] >= feWork.length) return true;
		return false;
	}
	
	public static boolean isQaDone(int qaId) {
		if(qaCurr[qaId] >= qaWork.length) return true;
		return false;
	}
	
	//
	public static boolean isBeTaskDone(int task) {
		if(beWork[task] == 0) return true;
		return false;
	}
	
	public static boolean isFeTaskDone(int task) {
		if(feWork[task] == 0) return true;
		return false;
	}
	
	public static String addOutput(int task) {
		return tasks[task];
	}
	
	// main
	
	public static void main(String[] args) {
		// buffer input
//		readingInput();
		
		backlogBackend.add(0);
		backlogBackend.add(1);
		backlogBackend.add(2);
		
//		backendWorkload = new ArrayList();
//		QAWorkload = new ArrayList();
//		frontendWorkload = new ArrayList();
//		
//		for(int i = 1; i < workloadSheet.size(); i++) {
//			for(int j = 1; j < workloadSheet.get(i).length; j++) {
//				if(j==1) taskName.add(workloadSheet.get(i)[j]);
//				else if(j==2) backendWorkload.add(Integer.parseInt(workloadSheet.get(i)[j]));
//				else if(j==3) frontendWorkload.add(Integer.parseInt(workloadSheet.get(i)[j]));
//				else if(j==4) QAWorkload.add(Integer.parseInt(workloadSheet.get(i)[j]));
//			}
//		}
//		
//		allEngineerDates.add(new ArrayList<Integer>)
//		
//		for(int i = 1; i < tasksheet.size(); i++) {
//			tasksheet.get(1)[1]
//			for(int j = 1; j < tasksheet.get(i).length; j++) {
//				if(j==1) taskName.add(workloadSheet.get(i)[j]);
//				else if(j==2) backendWorkload.add(Integer.parseInt(workloadSheet.get(i)[j]));
//				else if(j==3) frontendWorkload.add(Integer.parseInt(workloadSheet.get(i)[j]));
//				else if(j==4) QAWorkload.add(Integer.parseInt(workloadSheet.get(i)[j]));
//			}
//		}
		
		
//		0 is for Hero Banner
//		1 is for Featured News
//		2 is for Contact Us
		
//		workloadSheet
		
		for(int date=0; date<beDates.length; date++) {
			
			// For Back-end
			for(int id=0; id<allbeDates.length; id++) {
				boolean isBeDone = isBeDone(id);
				
				int outputId = 0; 
				if(id == 1) outputId = 1;
				else if(id == 2) outputId = 6;// erId for output
				
				if(!isBeDone) {
					fillOutput( allbeDates[id], isBeDone, true /* because nothing previous */, backendMap, backendGarbageMap, beWork, beCurr,
							id, outputId /* for FE outputArray */, date, wholeBeCurr, backlogBackend, backlogFrontend, isBackendBusy);
				}
				else output[outputId][date] = "Spare day     ";
			}
			
			// For Front-end
			for(int id=0; id<allfeDates.length; id++) {
				boolean isFeDone = isFeDone(id);
				
				int outputId = 2; 
				if(id != 0) outputId = 3; // erId for output
				
				if(backlogFrontend.size() != 0 && notAddedFeBacklog) {
					notAddedFeBacklog = false;
					Arrays.fill(feCurr, backlogFrontend.get(0));
				}
				
				if(!isFeDone) {
					fillOutput( allfeDates[id], isFeDone, isBeTaskDone(feCurr[id]), frontendMap, backendMap, feWork, feCurr,
							id, outputId, date, wholeFeCurr, backlogFrontend, backlogQuality, isFrontendBusy);
				}
				else output[outputId][date] = "Spare day     ";
			}
			
			// For Quality 
			for(int id=0; id<allqaDates.length; id++) {
				boolean isQaDone = isQaDone(id);
				
				int outputId = 4; 
				if(id != 0) outputId = 5; // erId for output
				
				if(backlogQuality.size() != 0 && notAddedQaBacklog) {
					notAddedQaBacklog = false;
					Arrays.fill(qaCurr, backlogQuality.get(0));
				}
				
				if(!isQaDone) {
					fillOutput( allqaDates[id], isQaDone, isFeTaskDone(qaCurr[id]), qualityMap, frontendMap, qaWork, qaCurr,
							id, outputId, date, wholeQaCurr, backlogQuality, backlogSample, isQualityBusy);
				}
				else output[outputId][date] = "Spare day     ";
			}
		}
		
		System.out.println("BE0: " + Arrays.toString(output[0]));
		System.out.println("BE1: " + Arrays.toString(output[1]));
		System.out.println("BE2: " + Arrays.toString(output[6]) + "\n");
		
		
		System.out.println("FE0: " + Arrays.toString(output[2]));
		System.out.println("FE1: " + Arrays.toString(output[3]) + "\n");
		
		System.out.println("QA0: " + Arrays.toString(output[4]));
		System.out.println("QA1: " + Arrays.toString(output[5]) + "\n");

//		
		
		
	}
	
	public static void fillOutput( int[] erDates, boolean isCurrentEngineerDone, boolean isPreviousEngineerDone, HashMap<Integer, Integer> currentEngineerMap, 
			HashMap<Integer, Integer> previousEngineerMap, int[] erWork, int[] currentEngineerTask, int id, int outputId, int date, int[] wholeErCurr, 
			ArrayList<Integer> backlog, ArrayList<Integer> backlogOfNextEngineer, boolean[] isEngineerBusy) {
		
		// ALL(BE, FE, QA);
		if(erDates[date] == 1) {
			if(!isCurrentEngineerDone) {
				
				if(!isEngineerBusy[id]) {
					if(wholeErCurr[0]<backlog.size()) {
						currentEngineerTask[id] = backlog.get(wholeErCurr[0]++); // wholecurr is the pointer which maintains pointer to backlog
						isEngineerBusy[id] = true;
					}
					else {
						output[outputId][date] = "Spare day     ";
						return;
					}
					
					// for task done on same day or not by previous engineer 
					boolean isWorkDoneOnSameDay = true;
					if(outputId == 0 || outputId == 1 || outputId == 6) {
						isWorkDoneOnSameDay = false; // because of be, as it's not dependent on any other
					}
					else if(isPreviousEngineerDone && previousEngineerMap.get(currentEngineerTask[id]) != date) {
						isWorkDoneOnSameDay = false; // because of previous
					}
					// check for isPrevWorkDoneOnSameDay
					if(isWorkDoneOnSameDay) {
						output[outputId][date] = "Spare day     ";
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
				output[outputId][date] = "Spare day     ";
			}						
		} else {
			// leave 
			output[outputId][date] = "Not Avail     ";
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
