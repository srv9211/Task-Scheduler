import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Task {
	public static ArrayList<String> tasksName = new ArrayList();
	
	public static boolean notAddedFeBacklog = true;
	public static boolean notAddedQaBacklog = true;
	
	// garbage map for previous work for back-end which will never come to use as there was no previous work for be
	public static HashMap<Integer, int[]> backendGarbageMap = new HashMap<>();
	// maps for the record which task is done at which day, so that we know next person can't do the same task this day.
	public static HashMap<Integer, int[]> backendTaskDoneOnDateMap = new HashMap<>();
	public static HashMap<Integer, int[]> frontendTaskDoneOnDateMap = new HashMap<>();
	public static HashMap<Integer, int[]> QATaskDoneOnDateMap = new HashMap<>();
	
	// pointer which will tell us about which person is doing which task currently. Integer not taken when passed to the function as it doesn't change the value of static Integer.
	public static int[] backendCurrentTask , frontendCurrentTask , QACurrentTask;
	public static int[]  backendSpecializationPosition = {0}, frontendSpecializationPosition = {0}, QASpecializationPosition = {0}; // total be people
	
	public static int[] backendWorkload;
	public static int[] frontendWorkload;
	public static int[] QAWorkload;

	// METHODS==================================================================================
	public static boolean isBeDone(int engineerID) {
		if(backendCurrentTask[engineerID] >= backendWorkload.length) return true;
		return false;
	}
	public static boolean isFeDone(int engineerID) {
		if(frontendCurrentTask[engineerID] >= frontendWorkload.length) return true;
		return false;
	}
	public static boolean isQaDone(int engineerID) {
		if(QACurrentTask[engineerID] >= QAWorkload.length) return true;
		return false;
	}
	public static boolean isBeTaskDone(int task) {
		if(backendWorkload[task] == 0) return true;
		return false;
	}
	public static boolean isFeTaskDone(int task) {
		if(frontendWorkload[task] == 0) return true;
		return false;
	}
	public static String addOutput(int task) {
		return tasksName.get(task);
	}
	
	// INPUT=========================================================
	public static ArrayList<String[]> workloadSheet = new ArrayList();
	public static ArrayList<String[]> tasksheet = new ArrayList();
	
	public static void readingInput() {
		String workloadCsvPath = "/Users/Sourav.Sharma/Downloads/Project_estimation.csv";
		String tasksheetCsvPath ="/Users/Sourav.Sharma/Downloads/Tasksheet.csv";
		
		String workloadString = "";
		String tasksheetString = "";
		
		try {
			BufferedReader buffworkloadload= new BufferedReader(new FileReader(workloadCsvPath));
			BufferedReader bufferTasksheet= new BufferedReader(new FileReader(tasksheetCsvPath));
			while((workloadString = buffworkloadload.readLine()) != null){
				String[] values= workloadString.split(",");
				workloadSheet.add(values);
			}
			while((tasksheetString = bufferTasksheet.readLine()) != null){
				String[] values= tasksheetString.split(",");
				tasksheet.add(values);
			}
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void writingOutput(ArrayList<String> output) {
		String fileName = "/Users/Sourav.Sharma/Downloads/Output.csv";
		try {
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName));
			for(String line : output) {
				bufferedWriter.write(line);
				bufferedWriter.newLine();
			}
			bufferedWriter.close();
		}
		catch(IOException ex) {
			System.out.println("Error in writing the file");
			ex.printStackTrace();
		}
	}
	
	// MAIN===========================================================
	public static void main(String[] args) {
		readingInput();	// buffer input
		// workload for each specialization. Every task is defined by index and stored in tasksName arrayList.
		backendWorkload = new int[workloadSheet.size()-1]; 
		frontendWorkload = new int[workloadSheet.size()-1];
		QAWorkload = new int[workloadSheet.size()-1];
		
		// work-sheet input
		try {
			for(int i = 0; i < workloadSheet.get(0).length; i++) {
				if(workloadSheet.get(0)[i].isEmpty())  throw new Exception("Null value or negative value.");
			}
			for(int i = 1; i < workloadSheet.size(); i++) {
				String epicName = workloadSheet.get(i)[0];
				String taskName = workloadSheet.get(i)[1];
				
				tasksName.add(taskName);
				for(int j = 2; j < workloadSheet.get(i).length; j++) {
					int daysForWork = (int)(Float.parseFloat(workloadSheet.get(i)[j])*2);
					if(epicName.isEmpty() || taskName.isEmpty() || daysForWork < 0) throw new Exception("Null value or negative value.");
					if(j==2) {
						backendWorkload[i-1] = daysForWork;
					}
					else if(j==3) {
						frontendWorkload[i-1] = daysForWork;
					}
					else if(j==4) {
						QAWorkload[i-1] = daysForWork;
					}
				}
			}
		}
		catch(Exception e) {
			System.err.println("Exception: \nError in loading the workloadsheet.");
			System.err.println("Please provide the proper input in CSV format, null and negative values are not accepted.");
			return;
		}
		
		ArrayList<Integer> backendOutputID = new ArrayList();
		ArrayList<Integer> frontendOutputID = new ArrayList();
		ArrayList<Integer> QAOutputID = new ArrayList();
		
		// dates input
		// index 0 => BE, 1 => FE, 2 => QA
		ArrayList<ArrayList<Integer>> backendPeopleDates = new ArrayList();
		ArrayList<ArrayList<Integer>> frontendPeopleDates = new ArrayList();
		ArrayList<ArrayList<Integer>> QAPeopleDates = new ArrayList();
		
		HashMap<Integer, String[]> namesAndIDMap = new HashMap<>();
		
		String string1 = Arrays.toString(tasksheet.get(0));
		string1 = string1.substring(1, string1.length()-1);
		
		int outputTestID = 0;
		
		try {
			for(String nullTest : tasksheet.get(0)) if(nullTest.isEmpty()) throw new Exception("Null value.");
			
			for(int i = 1; i < tasksheet.size(); i++) {
				ArrayList<ArrayList<Integer>> sameSpecializationEngineers = new ArrayList();
				
				String engineerName = tasksheet.get(i)[0];
				if(engineerName.isEmpty()) throw new Exception("Null value.");
				
				if(tasksheet.get(i)[1].equals("BE")) {
					sameSpecializationEngineers = backendPeopleDates;
					namesAndIDMap.put(outputTestID, new String[] {engineerName, "BE"});
					backendOutputID.add(outputTestID++);
				}
				
				else if(tasksheet.get(i)[1].equals("FE")) { 
					sameSpecializationEngineers = frontendPeopleDates;
					namesAndIDMap.put(outputTestID, new String[] {engineerName, "FE"});
					frontendOutputID.add(outputTestID++);
				}
				
				else if(tasksheet.get(i)[1].equals("QA")) {
					sameSpecializationEngineers = QAPeopleDates;
					namesAndIDMap.put(outputTestID, new String[] {engineerName, "QA"});
					QAOutputID.add(outputTestID++);
				}
				else throw new Exception("Not correct skill.");
				
				ArrayList<Integer> insideEngineer = new ArrayList();
				
				for(int j = 2; j < tasksheet.get(i).length; j++) {
					int input = (int)(Float.parseFloat(tasksheet.get(i)[j])*2);
					if(input < 0) throw new Exception("Negative value.");
					insideEngineer.add(input);
				}
				sameSpecializationEngineers.add(insideEngineer);
			}
		}
		catch(Exception e) {
			System.err.println("Exception: \nError in loading the tasksheet.");
			System.err.println("Please provide the proper input in CSV format, null and negative values are not accepted.");
			return;
		}
		
		int totalBackendPeople = backendPeopleDates.size();
		int totalFrontendPeople = frontendPeopleDates.size();
		int totalQAPeople = QAPeopleDates.size();
		
		int totalPeople = totalBackendPeople + totalFrontendPeople + totalQAPeople;
		
		backendCurrentTask = new int[totalBackendPeople];
		frontendCurrentTask = new int[totalFrontendPeople];
		QACurrentTask = new int[totalQAPeople];
		
		// busy or not at specific time
		boolean[] isBackendBusy = new boolean[totalBackendPeople];
		boolean[] isFrontendBusy = new boolean[totalFrontendPeople];
		boolean[] isQualityBusy = new boolean[totalQAPeople];
			
		ArrayList<Integer> backlogBackend = new ArrayList();
		ArrayList<Integer> backlogFrontend = new ArrayList();
		ArrayList<Integer> backlogQuality = new ArrayList();
		ArrayList<Integer> backlogSample = new ArrayList();
		
		int totalNumberOfDates = backendPeopleDates.get(0).size();
		
		String[][] output = new String[totalPeople][totalNumberOfDates];
		
		// fill output array with empty string
		for(String[] empty : output) {
			Arrays.fill(empty, "");
		}
		
		// back-end backlog
		for(int i=0; i<tasksName.size(); i++) {
			backlogBackend.add(i);
		}
		
		for(int date=0; date<totalNumberOfDates; date++) {
			// For Back-end
			for(int id=0; id<totalBackendPeople; id++) {
				boolean isBeDone = isBeDone(id);
				
				int outputId = backendOutputID.get(id);
				
				if(!isBeDone) {
					fillOutput( backendPeopleDates.get(id), isBeDone, true /* because nothing previous */, backendTaskDoneOnDateMap, backendGarbageMap, 
							backendWorkload, backendCurrentTask, id, outputId /* for FE outputArray */, date, backendSpecializationPosition, backlogBackend, 
							backlogFrontend, isBackendBusy, true, output);
				}
				else output[outputId][date] += "Spare 8hr day";
			}
			// For Front-end
			for(int id=0; id<totalFrontendPeople; id++) { // id is for which back-end engineer you have chosen.
				boolean isFeDone = isFeDone(id);
				
				int outputId = frontendOutputID.get(id);
				
				if(backlogFrontend.size() != 0 && notAddedFeBacklog) { // notAdded is to fill the backlogs
					notAddedFeBacklog = false;
					Arrays.fill(frontendCurrentTask, backlogFrontend.get(0));
				}
				
				if(!isFeDone) {
					fillOutput( frontendPeopleDates.get(id), isFeDone, isBeTaskDone(frontendCurrentTask[id]), frontendTaskDoneOnDateMap,
							backendTaskDoneOnDateMap, frontendWorkload, frontendCurrentTask, id, outputId, date, frontendSpecializationPosition,
							backlogFrontend, backlogQuality, isFrontendBusy, false, output);
				}
				else output[outputId][date] += "Spare 8hr day";
			}
			// For Quality 
			for(int id=0; id<totalQAPeople; id++) {
				boolean isQaDone = isQaDone(id);
				
				int outputId = QAOutputID.get(id);
				
				if(backlogQuality.size() != 0 && notAddedQaBacklog) { // notAdded is to fill the backlogs
					notAddedQaBacklog = false;
					Arrays.fill(QACurrentTask, backlogQuality.get(0));
				}
				
				if(!isQaDone) {
					fillOutput( QAPeopleDates.get(id), isQaDone, isFeTaskDone(QACurrentTask[id]), QATaskDoneOnDateMap, frontendTaskDoneOnDateMap,
							QAWorkload, QACurrentTask, id, outputId, date, QASpecializationPosition, backlogQuality, backlogSample, isQualityBusy, false, output);
				}
				else output[outputId][date] += "Spare 8hr day";
			}
		}
		
		ArrayList<String> outputString = new ArrayList();
		outputString.add(string1);
		
		for(int index = 0; index < output.length; index++) {
			String[] outputt = output[index];
			string1 = Arrays.toString(outputt);
			string1 = string1.substring(1, string1.length()-1);
			
			String engineerName = namesAndIDMap.get(index)[0];
			String specialization = namesAndIDMap.get(index)[1];
			
			outputString.add(engineerName + ", " + specialization + ", " + string1);
		}
		
		// OUTPUT
		writingOutput(outputString);
		for(String s : outputString)
			System.out.println(s);
		
	}
	
	public static boolean isWorkDoneOnSameDayFunction(boolean personIsBackend, boolean isPreviousEngineerDone, HashMap<Integer, int[]> previousEngineerMap
			, int[] currentEngineerTask, int halfDay, int id, int date) {
		boolean isWorkDoneOnSameDay = true;
		if(personIsBackend) {
			isWorkDoneOnSameDay = false; // because of be, as it's not dependent on any other
		}
		else if(isPreviousEngineerDone) {
			if(previousEngineerMap.get(currentEngineerTask[id])[0] == date) {
				if(previousEngineerMap.get(currentEngineerTask[id])[1] != halfDay) {
					if(halfDay > previousEngineerMap.get(currentEngineerTask[id])[1])
						isWorkDoneOnSameDay = false; // because of previous		
				}
			}
			else {
				isWorkDoneOnSameDay = false; // because of previous							
			}
		}
		return isWorkDoneOnSameDay;
	}

	public static void fillOutput( ArrayList<Integer> engineerDatesSchedule, boolean isCurrentEngineerDone, boolean isPreviousEngineerDone, HashMap<Integer, int[]> currentEngineerMap, 
			HashMap<Integer, int[]> previousEngineerMap, int[] workload, int[] currentEngineerTask, int id, int outputId, int date, int[] currentSpecializationPosition, 
			ArrayList<Integer> backlog, ArrayList<Integer> backlogOfNextEngineer, boolean[] isEngineerBusy, boolean personIsBackend, String[][] output) {
		
		// ALL(BE, FE, QA);
		int spareHour = 0; // for calculation of spare hours of the engineer.
		
		String[] workForDay = new String[]{"", ""};
		
		for(int halfDay = 1; halfDay <= engineerDatesSchedule.get(date); halfDay++) {
			
			if(!isCurrentEngineerDone) {
				
				if(!isEngineerBusy[id]) {
					if(currentSpecializationPosition[0]<backlog.size()) {
						currentEngineerTask[id] = backlog.get(currentSpecializationPosition[0]++); // wholecurr is the pointer which maintains pointer to backlog
						isEngineerBusy[id] = true;
					}
					else {
						spareHour += 4;
						continue;
					}
					
					// for task done on same day or not by previous engineer 
					boolean isWorkDoneOnSameDay = isWorkDoneOnSameDayFunction(personIsBackend, isPreviousEngineerDone, previousEngineerMap
							, currentEngineerTask, halfDay, id, date);
					// check for isPrevWorkDoneOnSameDay
					if(isWorkDoneOnSameDay) {
						spareHour += 4;
						continue;
					}
				}
				else {
					boolean isWorkDoneOnSameDay = isWorkDoneOnSameDayFunction(personIsBackend, isPreviousEngineerDone, previousEngineerMap
							, currentEngineerTask, halfDay, id, date);
					if(isWorkDoneOnSameDay) { // check for isPrevWorkDoneOnSameDay
						spareHour += 4;
						continue;
					}
				}
				
				String out = addOutput(currentEngineerTask[id]); // extracting the output
				workForDay[halfDay-1] += out;		// filling the output			
				workload[currentEngineerTask[id]]--;	// subtracting the work-day
				
				if(workload[currentEngineerTask[id]] == 0) {
					backlogOfNextEngineer.add(currentEngineerTask[id]); // backlog add-on for next engineer
					
					currentEngineerMap.put(currentEngineerTask[id], new int[]{date, halfDay}); // adding the dates when task is done
					
					if(currentSpecializationPosition[0] >= backlog.size()) {
						currentEngineerTask[id] = currentSpecializationPosition[0];
					}
					isEngineerBusy[id] = false; // making person free
				}
				
			} else {
				spareHour += 4;
			}
		}
//		OUTPUT FILLING
		// leave 
		if(engineerDatesSchedule.get(date) == 0) output[outputId][date] += "Not Available";
		else if(engineerDatesSchedule.get(date) == 1) workForDay[1] += "Not Avail";

		if(spareHour == 8) output[outputId][date] += "Spare " + spareHour + "hr day";
		else if(spareHour == 4){
			output[outputId][date] += (workForDay[0]);
			if(workForDay[0]=="") output[outputId][date] += "Spare 4 hr day : " + workForDay[1];
			else output[outputId][date] += " : Spare " + spareHour + "hr day";
		}
		else {
			if(workForDay[0].equals(workForDay[1])) output[outputId][date] += (workForDay[0]);
			else output[outputId][date] += (workForDay[0]+ " : " + workForDay[1]);
		}	
	}
}
