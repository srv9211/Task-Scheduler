import java.util.Arrays;
import java.util.HashMap;

public class Task {
	
	public static HashMap<Integer, Integer> beHm = new HashMap<>();
	public static HashMap<Integer, Integer> feHm = new HashMap<>();
	public static HashMap<Integer, Integer> qaHm = new HashMap<>();
		
	public static HashMap<String, HashMap<String, Integer>> hm = new HashMap<>();
	
	public static HashMap<String, Integer> backendMap = new HashMap<>();
	public static HashMap<String, Integer> frontendMap = new HashMap<>();
	public static HashMap<String, Integer> qualityMap = new HashMap<>();
	
	public static int beCurr = 0;
	public static int[] feCurr = {0}, qaCurr = {0};
	
	public static int[] beWork = {1, 1, 2};
	public static int[] feWork = {3, 2, 2};
	public static int[] qaWork = {1, 1, 2};
	
	String[] backendPeople = {"be1Dates", "be2Dates"};
	String[] frontendPeople = {"feDates"};
	String[] qualityPeople = {"qaDates"};
	
	public static int[] backendTask = new int[2];
	
	boolean[] taskDone = new boolean[beWork.length];

	// Indices
	// 0 -> Hero banner
	// 1 -> News
	// 2 -> Contact us
	
	public static int[] beDates = {1, 1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 0};
	public static int[] feDates =  {1, 1, 0, 1, 1, 1, 1, 1, 0, 0, 1, 0, 1, 1, 0};
	public static int[] qaDates =  {0, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1};
	public static int[] be2Dates = {0, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1};
	
	public static String[][] output = new String[3][beDates.length]; // output
	
	public static boolean isBeDone() {
		if(beCurr == beWork.length) return true;
		return false;
	}
	
	public static boolean isFeDone() {
		if(feCurr[0] == feWork.length) return true;
		return false;
	}
	
	public static boolean isQaDone() {
		if(qaCurr[0] == qaWork.length) return true;
		return false;
	}
	
	//
	public static boolean isBeDone(int task) {
		if(beWork[task] == 0) return true;
		return false;
	}
	
	public static boolean isFeDone(int task) {
		if(feWork[task] == 0) return true;
		return false;
	}
	
	public static String addOutput(int task) {
		if(task==0)
			return "Hero Banner   ";
		else if(task==1)
			return "Featured News ";
		else
			return "Contact Us    ";
	}

	public static void main(String[] args) {
		
		backendMap.put("Hero Banner  ", 1);
		backendMap.put("Featured News", 2);
		backendMap.put("Contact Us   ", 3);
		
		frontendMap.put("Hero Banner  ", 1);
		frontendMap.put("Featured News", 2);
		frontendMap.put("Contact Us   ", 1);
		
		qualityMap.put("Hero Banner  ", 1);
		qualityMap.put("Featured News", 1);
		qualityMap.put("Contact Us   ", 1);
		
		
//		0 is for Hero Banner
//		1 is for Featured News
//		2 is for Contact Us
		
		
		
		
		
		
		for(int i=0; i<beDates.length; i++) {
			
			// BE
			if(beDates[i] == 1) {
				if(!isBeDone()) {    // no need. figure something out
					
					if( beWork[beCurr] != 0 ) {
						
						String out = addOutput(beCurr);
						output[0][i] = out;

						beWork[beCurr]--;
						
						if(beWork[beCurr] == 0) {
							beHm.put(beCurr, i);
							beCurr++;
						}
						
					}
					
				}
				else {
					output[0][i] = "Spare Day     ";
				}			
			}
			else {
				// leave
				output[0][i] = "Not Avail     ";
			}
			
			// FE
			boolean isFeDone = isFeDone();
			if(!isFeDone) fillOutput( feDates, isFeDone, isBeDone(feCurr[0]), beHm, feWork, feCurr, 1, feHm, i);
			else output[1][i] = "Spare day     ";
			
//			if(feDates[i] == 1) {
//				if(!isFeDone()) {   			
//					if( isBeDone(feCurr[0]) && beHm.get(feCurr[0]) != i ) {				
//						if( feWork[feCurr[0]] != 0 ) {					
//							String out = addOutput(feCurr[0]);
//							output[1][i] = out;					
//							feWork[feCurr[0]]--;					
//							if(feWork[feCurr[0]] == 0) {
//								feHm.put(feCurr[0], i);
//								feCurr[0]++;
//							}
//						}
//						else {
//							feCurr[0]++;
//						}				
//					}
//					else {
//						output[1][i] = "Spare day     ";
//					}			
//				}
//				else {
//					output[1][i] = "Spare day     ";
//				}				
//			}
//			else {
//				// leave
//				output[1][i] = "Not Avail     ";
//			}
			
			
			// QA
			boolean isQaDone = isQaDone();
			if(!isQaDone) fillOutput(qaDates, isQaDone, isFeDone(qaCurr[0]), feHm, qaWork, qaCurr, 2, qaHm, i);
			else output[2][i] = "Spare day     ";
			
//			if(qaDates[i] == 1) {
//				
//				if(!isQaDone() ) {   
//					
//					if( isFeDone(qaCurr) && feHm.get(qaCurr) != i ) {
//						
//						if( qaWork[qaCurr] != 0 ) {
//							
//							String out = addOutput(qaCurr);
//							output[2][i] = out;
//							
//							qaWork[qaCurr]--;
//							
//							if(qaWork[qaCurr] == 0) {
//								qaCurr++;
//							}
//						}
//						else {
//							qaCurr++;
//						}
//						
//					}
//					else {
//						output[2][i] = "Spare day     ";
//					}
//								
//				}
//				else {
//					output[2][i] = "Spare day     ";
//				}
//				
//			}
//			else {
//				// leave
//				output[2][i] = "Not Avail     ";
//			}	
		}

		System.out.println("BE: " + Arrays.toString(output[0]));
		System.out.println("FE: " + Arrays.toString(output[1]));
		System.out.println("QA: " + Arrays.toString(output[2]));
		
	}
	
//	if(feDates[i] == 1) {
//		if(!isFeDone()) {   			
//			if( isBeDone(feCurr) && beHm.get(feCurr) != i ) {				
//				if( feWork[feCurr] != 0 ) {					
//					String out = addOutput(feCurr);
//					output[1][i] = out;					
//					feWork[feCurr]--;					
//					if(feWork[feCurr] == 0) {
//						feHm.put(feCurr, i);
//						feCurr++;
//					}
//				}
//				else {
//					feCurr++;
//				}				
//			}
//			else {
//				output[1][i] = "Spare day";
//			}			
//		}
//		else {
//			output[1][i] = "Spare day";
//		}				
//	}
//	else {
//		// leave
//		output[1][i] = "Not Avail";
//	}
//	fillOutput( feDates, isFeDone(), isBeDone(feCurr), beHm, feWork, feCurr, 1, feHm, i);
	public static void fillOutput( int[] erDates, boolean isErDone, boolean isPrevErDone, HashMap<Integer, Integer> prevErMap, int[] erWork, 
			int[] erCurrTask, int erId, HashMap<Integer, Integer> erMap, int i) {
		// FE
		if(erDates[i] == 1) {
			if(!isErDone) {   				
				if( isPrevErDone && prevErMap.get(erCurrTask[0]) != i ) {					
					if( erWork[erCurrTask[0]] != 0 ) {						
						String out = addOutput(erCurrTask[0]);
						
						output[erId][i] = out;						
						erWork[erCurrTask[0]]--;						
						if(erWork[erCurrTask[0]] == 0) {
							erMap.put(erCurrTask[0], i);
							erCurrTask[0]++;
						}
					}
					else {
						erCurrTask[0]++;
					}				
				}
				else {
					output[erId][i] = "Spare day     ";
				}			
			}
			else {
				output[erId][i] = "Spare day     ";
			}						
		}
		else {
			// leave 
			output[erId][i] = "Not Avail     ";
		}
		
	}
	
}

//FE fillOutput( feDates, isFeDone(), isBeDone(feCurr), beHm, feWork, feCurr, 1, feHm, i);
			
// QA fillOutput( qaDates, isQaDone(), isFeDone(qaCurr), feHm, qaWork, qaCurr, 2, qaHm, i);