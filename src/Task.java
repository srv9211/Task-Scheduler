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
	public static ArrayList<String> taskName = new ArrayList();
	
	public static boolean notAddedFeBacklog = true;
	public static boolean notAddedQaBacklog = true;
	
	// garbage map for previous work for back-end which will never come to use as there was no previous work for be
	public static HashMap<Integer, int[]> backendGarbageMap = new HashMap<>();
	// maps for the record which task is done at which day, so that we know next person can't do the same task this day.
	public static HashMap<Integer, int[]> backendMap = new HashMap<>();
	public static HashMap<Integer, int[]> frontendMap = new HashMap<>();
	public static HashMap<Integer, int[]> qualityMap = new HashMap<>();
	
	// pointer which will tell us about which person is doing which task currently. Integer not taken when passed to the function as it doesn't change the value of static Integer.
	public static int[] beCurr , feCurr , qaCurr;
	public static int[]  wholeBeCurr = {0}, wholeFeCurr = {0}, wholeQaCurr = {0}; // total be people
	
	public static int[] backendWorkload;
	public static int[] frontendWorkload;
	public static int[] QAWorkload;
	
	public static String[][] output; // output
	
	public static boolean[] isBackendBusy;
	public static boolean[] isFrontendBusy;
	public static boolean[] isQualityBusy;
	
	// METHODS==================================================================================
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
	
	// INPUT=========================================================
	
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
		// buffer input
		readingInput();
		

		ArrayList<Integer> backlogBackend = new ArrayList();
		ArrayList<Integer> backlogFrontend = new ArrayList();
		ArrayList<Integer> backlogQuality = new ArrayList();
		ArrayList<Integer> backlogSample = new ArrayList();
		
		backendWorkload = new int[workloadSheet.size()-1];
		frontendWorkload = new int[workloadSheet.size()-1];
		QAWorkload = new int[workloadSheet.size()-1];
		
		// work-sheet input
		for(int i = 1; i < workloadSheet.size(); i++) {
			taskName.add(workloadSheet.get(i)[1]);
			for(int j = 2; j < workloadSheet.get(i).length; j++) {
				if(j==2) {
					backendWorkload[i-1] = (int)(Float.parseFloat(workloadSheet.get(i)[j])*2);
				}
				else if(j==3) {
					frontendWorkload[i-1] = (int)(Float.parseFloat(workloadSheet.get(i)[j])*2);
				}
				else if(j==4) {
					QAWorkload[i-1] = (int)(Float.parseFloat(workloadSheet.get(i)[j])*2);
				}
			}
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
			for(int i = 1; i < tasksheet.size(); i++) {
				ArrayList<ArrayList<Integer>> hi = new ArrayList();
				
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
					insideEngineer.add((int)(Float.parseFloat(tasksheet.get(i)[j])*2));
				}
				hi.add(insideEngineer);
			}
		}
		catch(Exception e) {
			System.err.println("Please provide the proper input in CSV format, null values won't be accepted and with proper data types.");
			return;
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
		
		// fill output array with ""
		for(String[] s : output) {
			Arrays.fill(s, "");
		}
		
		// back-end backlog
		for(int i=0; i<taskName.size(); i++) {
			backlogBackend.add(i);
		}
		
		for(int date=0; date<totalNumberOfDates; date++) {
			// For Back-end
			for(int id=0; id<totalBackendPeople; id++) {
				boolean isBeDone = isBeDone(id);
				
				int outputId = backendOutputID.get(id);
				
				
				if(!isBeDone) {
					fillOutput( backendPeopleDates.get(id), isBeDone, true /* because nothing previous */, backendMap, backendGarbageMap, backendWorkload, beCurr,
							id, outputId /* for FE outputArray */, date, wholeBeCurr, backlogBackend, backlogFrontend, isBackendBusy, true);
				}
				else output[outputId][date] += "Spare 8hr day";
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
				else output[outputId][date] += "Spare 8hr day";
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
				else output[outputId][date] += "Spare 8hr day";
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
		
		// OUTPUT
		writingOutput(outputString);
		for(String s : outputString)
			System.out.println(s);
		
	}

	public static void fillOutput( ArrayList<Integer> erDates, boolean isCurrentEngineerDone, boolean isPreviousEngineerDone, HashMap<Integer, int[]> currentEngineerMap, 
			HashMap<Integer, int[]> previousEngineerMap, int[] erWork, int[] currentEngineerTask, int id, int outputId, int date, int[] wholeErCurr, 
			ArrayList<Integer> backlog, ArrayList<Integer> backlogOfNextEngineer, boolean[] isEngineerBusy, boolean personIsBackend) {
		
		// ALL(BE, FE, QA);
		int spareHour = 0;
		
		String[] writeOut = new String[]{"", ""};
		
		for(int halfDay = 1; halfDay <= erDates.get(date); halfDay++) {
			
			if(!isCurrentEngineerDone) {
				
				if(!isEngineerBusy[id]) {
					if(wholeErCurr[0]<backlog.size()) {
						currentEngineerTask[id] = backlog.get(wholeErCurr[0]++); // wholecurr is the pointer which maintains pointer to backlog
						isEngineerBusy[id] = true;
					}
					else {
						spareHour += 4;
						continue;
					}
					
					// for task done on same day or not by previous engineer 
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
					// check for isPrevWorkDoneOnSameDay
					if(isWorkDoneOnSameDay) {
						spareHour += 4;
						continue;
					}
				}
				else {
					boolean isWorkDoneOnSameDay = true;
					if(personIsBackend) {
						isWorkDoneOnSameDay = false; // because of be, as it's not dependent on any other
					}
					else if(isPreviousEngineerDone) {
						
						if(previousEngineerMap.get(currentEngineerTask[id])[0] == date) {
							if(previousEngineerMap.get(currentEngineerTask[id])[1] != halfDay) {
								isWorkDoneOnSameDay = false; // because of previous		
							}
						}
						else {
							isWorkDoneOnSameDay = false; // because of previous							
						}
						
					}
					// check for isPrevWorkDoneOnSameDay
					if(isWorkDoneOnSameDay) {
						spareHour += 4;
						continue;
					}
				}
				
				String out = addOutput(currentEngineerTask[id]); // extracting the output
				writeOut[halfDay-1] += out;		// filling the output			
				erWork[currentEngineerTask[id]]--;	// subtracting the work-day
				
				if(erWork[currentEngineerTask[id]] == 0) {
					backlogOfNextEngineer.add(currentEngineerTask[id]); // backlog add-on for next engineer
					
					currentEngineerMap.put(currentEngineerTask[id], new int[]{date, halfDay}); // adding the dates when task is done
					
					if(wholeErCurr[0] >= backlog.size()) {
						currentEngineerTask[id] = wholeErCurr[0];
					}
					isEngineerBusy[id] = false; // making person free
				}
				
			} else {
				spareHour += 4;
			}
		}
		
//		OUTPUT FILLING
		
		// leave 
		if(erDates.get(date) == 0) output[outputId][date] += "Not Available";
		else if(erDates.get(date) == 1) writeOut[1] += "Not Avail";

		if(spareHour == 8) output[outputId][date] += "Spare " + spareHour + "hr day";
		else if(spareHour == 4){
			output[outputId][date] += (writeOut[0]);
			if(writeOut[0]=="") output[outputId][date] += "Spare 4 hr day : " + writeOut[1];
			else output[outputId][date] += " : Spare " + spareHour + "hr day";
			
		}
		else {
			if(writeOut[0].equals(writeOut[1])) output[outputId][date] += (writeOut[0]);
			else output[outputId][date] += (writeOut[0]+ " : " + writeOut[1]);
		}
		
	}

}
