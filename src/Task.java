// add an arraylist or array for the backlogs that if any task come first for frontend so it can choose

import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;

public class Task {
	public static boolean notAddedFeBacklog = true;
	public static boolean notAddedQaBacklog = true;

	
	public static ArrayList<Integer> backlogBackend = new ArrayList();
	public static ArrayList<Integer> backlogFrontend = new ArrayList();
	public static ArrayList<Integer> backlogQuality = new ArrayList();
	public static ArrayList<Integer> backlogSample = new ArrayList();

	
	// test map for previous work for backend which will never come to use
	public static HashMap<Integer, Integer> testMap = new HashMap<>();
	
	// maps for the record which task is done at which day, so that we know next person can't do the same task this day.
	public static HashMap<Integer, Integer> beHm = new HashMap<>();
	public static HashMap<Integer, Integer> feHm = new HashMap<>();
	public static HashMap<Integer, Integer> qaHm = new HashMap<>();
	
	// pointer which will tell about which person is doing which task currently. Int not taken when passed to the function it doesn't change the value of static int
	public static int[] beCurr = {0, 0}, feCurr = {0, 0}, qaCurr = {0, 0};
	public static int[]  wholeBeCurr = {0}, wholeFeCurr = {0}, wholeQaCurr = {0}; // total be people
	
	public static int[] beWork = {4, 2, 1};
	public static int[] feWork = {1, 2, 2};
	public static int[] qaWork = {1, 1, 1};
	
	public static String[] tasks = {"Hero Banner   "   , "Featured News ", "Contact Us    "};
	
//	public static boolean[] BeBusy = {false, false}; // for checking be person is busy or not

	// Indices
	// 0 -> Hero banner
	// 1 -> News
	// 2 -> Contact us
																														
	public static int[] beDates =  {1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0};
	public static int[] be2Dates = {1, 1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1};
	public static int[] feDates =  {1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1, 0, 1, 1, 0};
	public static int[] fe2Dates = {0, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1};
	public static int[] qaDates =  {0, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1};
	public static int[] qa2Dates =  {0, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1};

	
	public static int[][] allbeDates = {beDates, be2Dates}, allfeDates = {feDates, fe2Dates}, allqaDates = {qaDates, qa2Dates};
	
	
	public static String[][] output = new String[6][beDates.length]; // output
	
	public static boolean[] beBusy = {false, false};
	public static boolean[] feBusy = {false, false};
	public static boolean[] qaBusy = {false, false};

	
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
		
		backlogBackend.add(0);
		backlogBackend.add(1);
		backlogBackend.add(2);
		
//		0 is for Hero Banner
//		1 is for Featured News
//		2 is for Contact Us
		
		for(int i=0; i<beDates.length; i++) {
			
//			DO CTRL Z IF NOT GETTING
			for(int id=0; id<allbeDates.length; id++) {
				boolean isBeDone = isBeDone(id);
				
				int outputId = 0; 
				if(id != 0) outputId = 3; // erId for output
				
				if(!isBeDone) {
					
					fillOutput( allbeDates[id], isBeDone, true /* because nothing previous */, testMap, beWork, beCurr,
							id, outputId /* for FE outputArray */, beHm, i, wholeBeCurr, backlogBackend, backlogFrontend, beBusy);
				}
				else output[outputId][i] = "Spare day     ";
			}
			
			for(int id=0; id<allfeDates.length; id++) {
				boolean isFeDone = isFeDone(id);
				
				int outputId = 1; 
				if(id != 0) outputId = 4; // erId for output
				
				if(backlogFrontend.size() != 0 && notAddedFeBacklog) {
					notAddedFeBacklog = false;
					Arrays.fill(feCurr, backlogFrontend.get(0));
				}
				
				if(!isFeDone) {
					fillOutput( allfeDates[id], isFeDone, isBeTaskDone(feCurr[id]), beHm, feWork, feCurr,
							id, outputId, feHm, i, wholeFeCurr, backlogFrontend, backlogQuality, feBusy);
				}
				else {
					output[outputId][i] = "Spare day     ";
				}
			}
//			
			for(int id=0; id<allqaDates.length; id++) {
				boolean isQaDone = isQaDone(id);
				
				int erId = 2; 
				if(id != 0) erId = 5; // erId for output
				
				if(backlogQuality.size() != 0 && notAddedQaBacklog) {
					notAddedQaBacklog = false;
					Arrays.fill(qaCurr, backlogQuality.get(0));
				}
				
				if(!isQaDone) {
					fillOutput( allqaDates[id], isQaDone, isFeTaskDone(qaCurr[id]), feHm, qaWork, qaCurr,
							id, erId, qaHm, i, wholeQaCurr, backlogQuality, backlogSample, qaBusy);
				}
				else output[erId][i] = "Spare day     ";
			}
		}
		
		System.out.println(backlogQuality);

		System.out.println("BE0: " + Arrays.toString(output[0]));
		System.out.println("BE1: " + Arrays.toString(output[3]) + "\n");
		
//		System.out.println(Arrays.toString(beWork));
//		System.out.println(backlogFrontend);
		
		System.out.println("FE0: " + Arrays.toString(output[1]));
		System.out.println("FE1: " + Arrays.toString(output[4]) + "\n");
		
		System.out.println("QA0: " + Arrays.toString(output[2]));
		System.out.println("QA1: " + Arrays.toString(output[5]) + "\n");

//		
//		for(int key : beHm.keySet()) {
//			System.out.println(key + " " + beHm.get(key));
//		}
		
	}
	
	public static void fillOutput( int[] erDates, boolean isErDone, boolean isPrevErDone, HashMap<Integer, Integer> prevErMap, int[] erWork, 
			int[] erCurrTask, int id, int outputId, HashMap<Integer, Integer> erMap, int i, int[] wholeErCurr, ArrayList<Integer> backlog,
			ArrayList<Integer> backlogOfNextEr, boolean[] erBusy) {
		// ALL
		
		if(erDates[i] == 1) {
			if(!isErDone) {
				boolean isPrevWorkDoneButNotSameDay = false;
				
				if((outputId == 0 || outputId == 3) || (isPrevErDone && prevErMap.get(erCurrTask[id]) != i) ) {
					isPrevWorkDoneButNotSameDay = true; // because of be, as it's not dependent on any other
//					if(outputId==4) System.out.println(i + " " + isPrevWorkDoneButNotSameDay);
				}
				
				if(isPrevWorkDoneButNotSameDay) {
					if(!erBusy[id]) {
						if(wholeErCurr[0]<backlog.size()) {
							erCurrTask[id] = backlog.get(wholeErCurr[0]++); // wholecurr is the pointer which maintains pointer to backlog
							erBusy[id] = true;
							
						} else {
							output[outputId][i] = "Spare day     ";
							return;
						}
					}
					
					String out = addOutput(erCurrTask[id]);
					
					
					output[outputId][i] = out;						
					erWork[erCurrTask[id]]--;						
					if(erWork[erCurrTask[id]] == 0) {
						backlogOfNextEr.add(erCurrTask[id]); // backlog addon for next engineer
						
						erMap.put(erCurrTask[id], i);
						if(wholeErCurr[0] >= backlog.size()) {
							erCurrTask[id] = wholeErCurr[0];
						}
						erBusy[id] = false;
					}
				} else {
//					System.out.print(i + " ");
					output[outputId][i] = "Spare day     ";
				}		
			} else {
				output[outputId][i] = "Spare day     ";
			}						
		} else {
			// leave 
			output[outputId][i] = "Not Avail     ";
		}
		
	} 
	
}



