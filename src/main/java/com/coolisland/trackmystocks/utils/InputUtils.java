package com.coolisland.trackmystocks.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.InputMismatchException;

public class InputUtils {
	public static final int RETRY_CHOICE = -1;
	public static final int CANCEL_CHOICE = 0;

	/**
	 * 
	 * @param appChoices
	 * @return
	 */
	public static int getChoice(HashMap<Integer, String> appChoices) {
		Integer choice = RETRY_CHOICE;

////		try {
////			System.in.
////			if (System.in.available() != 0) {
////				if (System.in.markSupported()) {
////					System.in.reset();
////				}
////			}
////		} catch (IOException e1) {
////			// TODO Auto-generated catch block
////			e1.printStackTrace();
////		}
//
//		Scanner scanner = new Scanner(System.in);
//		String inputLine = "";
//		try {
//			if (scanner != null) {
////				if (scanner.hasNextInt()) {
////					choice = new Integer(scanner.nextInt());
////				}
////				while (!scanner.hasNextLine()) {
////					;
////				}
//				
////				if (scanner.hasNextLine()) {
//					inputLine = scanner.nextLine();
//					
//					choice = Integer.parseInt(inputLine);
////				}
//			}
//
//		} catch (NoSuchElementException nsee) {
//			;
//		} catch (NumberFormatException nfe) {
//			System.out.println(nfe);
//			System.out.println("Input: " + inputLine);
//			nfe.printStackTrace();
//		} catch (Exception e) {
//			System.out.println(e);
//			e.printStackTrace();
//		} finally {
//			scanner.close();
//		}
//
		
		InputStreamReader inputStreamReader = new InputStreamReader(System.in);  
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);  

		String inputLine = null;
		try {
			inputLine = bufferedReader.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
		try {
			choice = Integer.parseInt(inputLine);
		} catch (NumberFormatException nfe) {
			System.out.println(nfe);
			System.out.println("Input: " + inputLine);
			nfe.printStackTrace();
		}
		
		try {
			if (CANCEL_CHOICE != choice && !appChoices.containsKey(choice)) {
				System.out.println("Bad choice. Try again");
				choice = RETRY_CHOICE;
			}

			// System.out.println("User entered: " + choice);
		} catch (InputMismatchException e) {
			System.out.println("Invalid Response");
		}

		return choice.intValue();	
	}

}
