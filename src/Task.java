// add an arraylist or array for the backlogs that if any task come first for frontend so it can choose

import java.util.Arrays;
import java.util.HashMap;

public class Task {
	
	// test map for previous work for backend which will never come to use
	public static HashMap<Integer, Integer> testMap = new HashMap<>();
	
	// maps for the record which task is done at which day, so that we know next person can't do the same task this day.
	public static HashMap<Integer, Integer> beHm = new HashMap<>();
	public static HashMap<Integer, Integer> feHm = new HashMap<>();
	public static HashMap<Integer, Integer> qaHm = new HashMap<>();
	
	// pointer which will tell about which person is doing which task currently. Int not taken when passed to the function it doesn't change the value of static int
	public static int[] beCurr = {0, 1}, feCurr = {0, 1}, qaCurr = {0, 1};
	public static int[]  wholeBeCurr = {2}, wholeFeCurr = {1}, wholeQaCurr = {2}; // total be people
	
	public static int[] beWork = {1, 1, 2};
	public static int[] feWork = {1, 2, 2};
	public static int[] qaWork = {1, 1, 1};
	
	public static String[] tasks = {"Hero Banner   "   , "Featured News ", "Contact Us    "};
	
//	public static boolean[] BeBusy = {false, false}; // for checking be person is busy or not

	// Indices
	// 0 -> Hero banner
	// 1 -> News
	// 2 -> Contact us
																														
	public static int[] beDates =  {1, 1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0};
	public static int[] be2Dates = {1, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1};
	public static int[] feDates =  {1, 1, 0, 1, 1, 1, 1, 1, 0, 0, 1, 0, 1, 1, 0};
	public static int[] qaDates =  {0, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1};
	public static int[] qa2Dates = {0, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1};
	
	public static int[][] allbeDates = {beDates, be2Dates}, allfeDates = {feDates}, allqaDates = {qaDates, qa2Dates};
	
	
	public static String[][] output = new String[5][beDates.length]; // output
	
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
		
//		0 is for Hero Banner
//		1 is for Featured News
//		2 is for Contact Us
		
		for(int i=0; i<beDates.length; i++) {
			
//			DO CTRL Z IF NOT GETTING
			for(int id=0; id<allbeDates.length; id++) {
				boolean isBeDone = isBeDone(id);
				
				int erId = 0; 
				if(id != 0) erId = 3; // erId for output
				
				if(!isBeDone) {
					fillOutput( allbeDates[id], isBeDone, true /* because nothing previous */, testMap, beWork, beCurr, id, erId /* for FE outputArray */, beHm, i, wholeBeCurr);
				}
				else output[erId][i] = "Spare day     ";
			}
			
			for(int id=0; id<allfeDates.length; id++) {
				boolean isFeDone = isFeDone(id);
				
				int erId = 1; 
				if(id != 0) erId = 4; // erId for output
				
				if(!isFeDone) fillOutput( allfeDates[id], isFeDone, isBeTaskDone(feCurr[id]), beHm, feWork, feCurr, id, erId, feHm, i, wholeFeCurr);
				else output[erId][i] = "Spare day     ";
			}
			
			for(int id=0; id<allqaDates.length; id++) {
				boolean isQaDone = isQaDone(id);
				
				int erId = 2; 
				if(id != 0) erId = 4; // erId for output
				
				if(!isQaDone) fillOutput( allqaDates[id], isQaDone, isFeTaskDone(qaCurr[id]), feHm, qaWork, qaCurr, id, erId, qaHm, i, wholeQaCurr);
				else output[erId][i] = "Spare day     ";
			}
			

//			boolean isBeDone = isBeDone();
//			if(!isBeDone) fillOutput( beDates, isBeDone, true /* because nothing previous */, testMap, beWork, beCurr, 0, beHm, i);
//			else output[0][i] = "Spare day     ";
	
////			 FE
//			boolean isFeDone = isFeDone(0);
//			if(!isFeDone) fillOutput( allfeDates[0], isFeDone, isBeTaskDone(feCurr[0]), beHm, feWork, feCurr, 0, 1, feHm, i, wholeFeCurr);
//			else output[1][i] = "Spare day     ";
//			
//			isFeDone = isFeDone(1);
//			if(!isFeDone) fillOutput( allfeDates[1], isFeDone, isBeTaskDone(feCurr[1]), beHm, feWork, feCurr, 1, 4, feHm, i, wholeFeCurr);
//			else output[4][i] = "Spare day     ";

//			 QA
//			boolean isQaDone = isQaDone();
//			if(!isQaDone) fillOutput(qaDates, isQaDone, isFeTaskDone(qaCurr[0]), feHm, qaWork, qaCurr, 0, 2, qaHm, i, wholeQaCurr);
//			else output[2][i] = "Spare day     ";
		}

		System.out.println("BE0: " + Arrays.toString(output[0]));
		System.out.println("BE1: " + Arrays.toString(output[3]) + "\n");
		System.out.println("FE0: " + Arrays.toString(output[1]) + "\n");
		System.out.println("QA0: " + Arrays.toString(output[2]) + "\n");
		System.out.println("QA2: " + Arrays.toString(output[4]));
		
	}
	
	public static void fillOutput( int[] erDates, boolean isErDone, boolean isPrevErDone, HashMap<Integer, Integer> prevErMap, int[] erWork, 
			int[] erCurrTask, int id, int erId, HashMap<Integer, Integer> erMap, int i, int[] wholeErCurr) {
		// ALL

		if(erDates[i] == 1) {
			if(!isErDone) {
				boolean isPrevWorkDoneButNotSameDay = false;
				
				if((erId == 0 || erId == 3) || (isPrevErDone && prevErMap.get(erCurrTask[id]) != i) ) isPrevWorkDoneButNotSameDay = true; // because of be, as it's not dependent on any other
				
				if(isPrevWorkDoneButNotSameDay) {
					if( erWork[erCurrTask[id]] != 0) {
						String out = addOutput(erCurrTask[id]);
						
						output[erId][i] = out;						
						erWork[erCurrTask[id]]--;						
						if(erWork[erCurrTask[id]] == 0) {

							erMap.put(erCurrTask[id], i);
							erCurrTask[id] = wholeErCurr[0]++; // this is because to keep record where whole be task stand and helps individual be person
//							erCurrTask[id]++;
						}
					}
				} else {
					output[erId][i] = "Spare day     ";
				}		
			} else {
				output[erId][i] = "Spare day     ";
			}						
		} else {
			// leave 
			output[erId][i] = "Not Avail     ";
		}
		
	} 
	
}



