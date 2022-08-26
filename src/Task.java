import java.util.Arrays;
import java.util.HashMap;

public class Task {
	
	public static int beCurr = 0, feCurr = 0, qaCurr = 0;
	
	public static int[] be = {2, 1, 2};
	public static int[] fe = {1, 2, 3};
	public static int[] qa = {1, 1, 1};
	
	// Indices
	// 0 -> Hero banner
	// 1 -> News
	// 2 -> Contact us
	
	public static int[] beDates = {1, 1, 0, 1, 1, 1, 0, 1, 1, 1, 1, 1, 1, 1};
	public static int[] feDates = {1, 1, 0, 1, 1, 1, 1, 1, 0, 0, 1, 0, 1, 1};
	public static int[] qaDates = {0, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 1, 1, 1};
	
	public static boolean isBeDone() {
		if(beCurr == be.length) return true;
		return false;
	}
	
	public static boolean isFeDone() {
		if(feCurr == fe.length) return true;
		return false;
	}
	
	public static boolean isQaDone() {
		if(qaCurr == qa.length) return true;
		return false;
	}
	
	//
	public static boolean isBeDone(int task) {
		if(be[task] == 0) return true;
		return false;
	}
	
	public static boolean isFeDone(int task) {
		if(fe[task] == 0) return true;
		return false;
	}

	public static void main(String[] args) {
		HashMap<Integer, Integer> beHm = new HashMap<>();
		HashMap<Integer, Integer> feHm = new HashMap<>();
		
		
		int[][] output = new int[3][14];
		
		for(int i=0; i<beDates.length; i++) {
			// BE
			if(beDates[i] == 1) {
				if(!isBeDone()) {    // no need. figure something out
					
					if( be[beCurr] != 0 ) {
						output[0][i] = 1;

						be[beCurr]--;
						
						if(be[beCurr] == 0) {
							beHm.put(beCurr, i);
							beCurr++;
						}
						
					}
					
				}
				else {
					output[0][i] = 2;
				}			
			}
			else {
				// leave
				output[0][i] = 0;
			}
			
			// FE
			if(feDates[i] == 1) {
				
				if(!isFeDone()) {   
					
					if( isBeDone(feCurr) && beHm.get(feCurr) != i ) {
						
						
						if( fe[feCurr] != 0 ) {
							output[1][i] = 1;
							
							
							
							fe[feCurr]--;
							
							if(fe[feCurr] == 0) {
								feHm.put(feCurr, i);
								feCurr++;
							}
						}
						else {
							feCurr++;
						}
						
					}
					else {
						output[1][i] = 2;
					}			
				}
				else {
					output[1][i] = 2;
				}
				
				
			}
			else {
				// leave
				output[1][i] = 0;
			}
			
			// QA
			if(qaDates[i] == 1) {
				
				if(!isQaDone() ) {   
					
					if( isFeDone(qaCurr) && feHm.get(qaCurr) != i ) {
						
						if( qa[qaCurr] != 0 ) {
							output[2][i] = 1;
							qa[qaCurr]--;
							
							if(qa[qaCurr] == 0) {
								qaCurr++;
							}
						}
						else {
							qaCurr++;
						}
						
					}
					else {
						output[2][i] = 2;
					}
								
				}
				else {
					output[2][i] = 2;
				}
				
			}
			else {
				// leave
				output[2][i] = 0;
			}	
		}

		System.out.println("BE: " + Arrays.toString(output[0]));
		System.out.println("FE: " + Arrays.toString(output[1]));
		System.out.println("QA: " + Arrays.toString(output[2]));
		
	}
}