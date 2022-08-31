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
	// Indices are tasks
	public static ArrayList<String> tasksName = new ArrayList();
	
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
	public static boolean isBackendDone(int engineerID) {
		if(backendCurrentTask[engineerID] >= backendWorkload.length) return true;
		return false;
	}
	public static boolean isFrontendDone(int engineerID) {
		if(frontendCurrentTask[engineerID] >= frontendWorkload.length) return true;
		return false;
	}
	public static boolean isQADone(int engineerID) {
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
			BufferedReader bufferworkload= new BufferedReader(new FileReader(workloadCsvPath));
			BufferedReader bufferTasksheet= new BufferedReader(new FileReader(tasksheetCsvPath));
			while((workloadString = bufferworkload.readLine()) != null){
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
		// dates input
		ArrayList<ArrayList<Integer>> backendEngineersDates = new ArrayList();
		ArrayList<ArrayList<Integer>> frontendEngineersDates = new ArrayList();
		ArrayList<ArrayList<Integer>> QAEngineersDates = new ArrayList();
		
		HashMap<Integer, String[]> namesAndIDMap = new HashMap<>();

		int outputID = 0; // for knowing the outputID at the time of output
		ArrayList<Integer> backendOutputID = new ArrayList();// eg: back-end BE 0th person outputID. We'll know that which person has which output ID
		ArrayList<Integer> frontendOutputID = new ArrayList();
		ArrayList<Integer> QAOutputID = new ArrayList();
		
		try {
			for(String nullTest : tasksheet.get(0)) if(nullTest.isEmpty()) throw new Exception("Null value.");
			for(int i = 1; i < tasksheet.size(); i++) {
				ArrayList<ArrayList<Integer>> sameSpecializationEngineers = new ArrayList();
				
				String engineerName = tasksheet.get(i)[0];
				if(engineerName.isEmpty()) throw new Exception("Null value.");
				
				if(tasksheet.get(i)[1].equals("BE")) { // for backend
					sameSpecializationEngineers = backendEngineersDates;
					namesAndIDMap.put(outputID, new String[] {engineerName, "BE"});
					backendOutputID.add(outputID++);
				}
				
				else if(tasksheet.get(i)[1].equals("FE")) { // for front-end
					sameSpecializationEngineers = frontendEngineersDates;
					namesAndIDMap.put(outputID, new String[] {engineerName, "FE"});
					frontendOutputID.add(outputID++);
				}
				
				else if(tasksheet.get(i)[1].equals("QA")) { // for QA
					sameSpecializationEngineers = QAEngineersDates;
					namesAndIDMap.put(outputID, new String[] {engineerName, "QA"});
					QAOutputID.add(outputID++);
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
		
		int totalBackendEngineers = backendEngineersDates.size();
		int totalFrontendEngineers = frontendEngineersDates.size();
		int totalQAEngineers = QAEngineersDates.size();
		
		int totalEngineers = totalBackendEngineers + totalFrontendEngineers + totalQAEngineers;
		
		backendCurrentTask = new int[totalBackendEngineers];
		frontendCurrentTask = new int[totalFrontendEngineers];
		QACurrentTask = new int[totalQAEngineers];
		
		// busy or not at specific time
		boolean[] isBackendBusy = new boolean[totalBackendEngineers];
		boolean[] isFrontendBusy = new boolean[totalFrontendEngineers];
		boolean[] isQualityBusy = new boolean[totalQAEngineers];
		
		boolean frontendBacklogIsEmpty = true; // check if there is any backlog availbale or not.
		boolean QABacklogIsEmpty = true;
			
		ArrayList<Integer> backlogBackend = new ArrayList();
		ArrayList<Integer> backlogFrontend = new ArrayList();
		ArrayList<Integer> backlogQuality = new ArrayList();
		ArrayList<Integer> backlogSample = new ArrayList();
		
		int totalNumberOfDates = backendEngineersDates.get(0).size(); // total no. of dates
		String[][] output = new String[totalEngineers][totalNumberOfDates];
		for(String[] empty : output) {
			Arrays.fill(empty, "");	// // fill output array with empty string. why? because it was adding up null values.
		}
		
		// back-end backlog
		for(int i=0; i<tasksName.size(); i++) {
			backlogBackend.add(i);
		}
		
		for(int date=0; date<totalNumberOfDates; date++) { // Iterating every date
			// For Back-end
			for(int id=0; id<totalBackendEngineers; id++) {
				boolean isBackendDone = isBackendDone(id);
				
				int outputId = backendOutputID.get(id);
				
				if(!isBackendDone) {
					fillOutput( backendEngineersDates.get(id), isBackendDone, true /* because nothing previous */, backendTaskDoneOnDateMap, backendGarbageMap, 
							backendWorkload, backendCurrentTask, id, outputId /* for FE outputArray */, date, backendSpecializationPosition, backlogBackend, 
							backlogFrontend, isBackendBusy, true, output);
				}
				else output[outputId][date] += "8 Hr Spare";
			}
			// For Front-end
			for(int id=0; id<totalFrontendEngineers; id++) { // id is for which back-end engineer you have chosen.
				boolean isFrontendDone = isFrontendDone(id);
				
				int outputId = frontendOutputID.get(id);
				
				if(backlogFrontend.size() != 0 && frontendBacklogIsEmpty) { // notAdded is to fill the backlogs
					frontendBacklogIsEmpty = false;
					Arrays.fill(frontendCurrentTask, backlogFrontend.get(0));
				}
				
				if(!isFrontendDone) {
					fillOutput( frontendEngineersDates.get(id), isFrontendDone, isBeTaskDone(frontendCurrentTask[id]), frontendTaskDoneOnDateMap,
							backendTaskDoneOnDateMap, frontendWorkload, frontendCurrentTask, id, outputId, date, frontendSpecializationPosition,
							backlogFrontend, backlogQuality, isFrontendBusy, false, output);
				}
				else output[outputId][date] += "8 Hr Spare";
			}
			// For Quality 
			for(int id=0; id<totalQAEngineers; id++) {
				boolean isQADone = isQADone(id);
				
				int outputId = QAOutputID.get(id);
				
				if(backlogQuality.size() != 0 && QABacklogIsEmpty) { // notAdded is to fill the backlogs
					QABacklogIsEmpty = false;
					Arrays.fill(QACurrentTask, backlogQuality.get(0));
				}
				
				if(!isQADone) {
					fillOutput( QAEngineersDates.get(id), isQADone, isFeTaskDone(QACurrentTask[id]), QATaskDoneOnDateMap, frontendTaskDoneOnDateMap,
							QAWorkload, QACurrentTask, id, outputId, date, QASpecializationPosition, backlogQuality, backlogSample, isQualityBusy, false, output);
				}
				else output[outputId][date] += "8 Hr Spare";
			}
		}
		
		String outputElements = Arrays.toString(tasksheet.get(0));
		outputElements = outputElements.substring(1, outputElements.length()-1);
		
		ArrayList<String> outputString = new ArrayList();
		outputString.add(outputElements);
		
		for(int index = 0; index < output.length; index++) {
			String[] outputt = output[index];
			outputElements = Arrays.toString(outputt);
			outputElements = outputElements.substring(1, outputElements.length()-1);
			
			String engineerName = namesAndIDMap.get(index)[0];
			String specialization = namesAndIDMap.get(index)[1];
			
			outputString.add(engineerName + ", " + specialization + ", " + outputElements);
		}
		boolean allWorkIsDone = true;
		for(int i=0; i<totalBackendEngineers; i++) if(!isBackendDone(i)) allWorkIsDone = false;
		for(int i=0; i<totalFrontendEngineers; i++) if(!isFrontendDone(i)) allWorkIsDone = false;
		for(int i=0; i<totalQAEngineers; i++) if(!isQADone(i)) allWorkIsDone = false;
		
		try {
			if(!allWorkIsDone) {
				throw new Exception("Work not done.");
			}
		} catch(Exception e) {
			System.err.println("Exception: ");
			System.err.println("Given input of engineers were not able to complete the task by the provided time frame.");
			System.err.println("Kindly provide the engineers with more availability.");
			return;
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

		if(spareHour == 8) output[outputId][date] +=  "8 Hr Spare";
		else if(spareHour == 4){
			output[outputId][date] += (workForDay[0]);
			if(workForDay[0]=="") output[outputId][date] += "4 Hr Spare : " + workForDay[1];
			else output[outputId][date] += " : " + spareHour + " Hr Spare";
		}
		else {
			if(workForDay[0].equals(workForDay[1])) output[outputId][date] += (workForDay[0]);
			else output[outputId][date] += (workForDay[0]+ " : " + workForDay[1]);
		}	
	}
}
