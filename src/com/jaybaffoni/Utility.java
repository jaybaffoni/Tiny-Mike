package com.jaybaffoni;

import java.util.concurrent.ThreadLocalRandom;

public class Utility {
	
	public static boolean probability(double percent) {
		
		double rand = ThreadLocalRandom.current().nextDouble(0,100);
		/*if(rand < 1) {
			System.out.println(rand);
		}*/
		return rand <= percent;
		
	}

}
