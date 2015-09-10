package com.coolisland.trackmystocks.stockquotes;

import java.util.HashMap;
import java.util.Set;

import com.coolisland.trackmystocks.database.CheckDatabaseConnection;
import com.coolisland.trackmystocks.sellbuy.AddStockToAccount;
import com.coolisland.trackmystocks.sellbuy.BuyStock;
import com.coolisland.trackmystocks.sellbuy.SellStock;
import com.coolisland.trackmystocks.utils.InputUtils;
import com.coolisland.trackmystocks.yahoo.PopulateHistoricalPrices;

public class StockQuotesDriver {

	private static final int RETRY_CHOICE = -1;

	private static final int POPULATE_HISTORICAL_PRICE_CHOICE = 1;
	private static final String POPULATE_HISTORICAL_PRICES_MSG = "Populate Historical Prices";

	private static final int CALCULATE_MOVING_AVERAGES_CHOICE = 2;
	private static final String CALCULATE_MOVING_AVERAGES_MSG = "Calculate Moving Averages";

	private static final int BUY_STOCK_CHOICE = 3;
	private static final String BUY_STOCK_MSG = "Buy Stock";

	private static final int SELL_STOCK_CHOICE = 4;
	private static final String SELL_STOCK_MSG = "Sell Stock";

	private static final int EXIT_CHOICE = 0;
	private static final String EXIT_MSG = "Exit";

	private static final int CHECK_DB_CHOICE = 5;
	private static final String CHECK_DB_MSG = "Check Database Connection";

	private static final int ADD_STOCK_CHOICE = 6;
	private static final String ADD_STOCK_MSG = "Add New Stock Option to Account";

	/**
	 * 
	 * @param appChoices
	 * @return
	 */
//	private int getChoice(HashMap<Integer, String> appChoices) {
//		Integer choice = RETRY_CHOICE;
//		
//		try {
//			if (System.in.available() != 0) {
//				if (System.in.markSupported()) {
//					System.in.reset();
//				}
//			}
//		} catch (IOException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
//		Scanner scanner = new Scanner(System.in);
//
//		try {
//			if (scanner != null) {
//				if (scanner.hasNextInt()) {
//					System.out.println("has next");
//					
//					choice = new Integer(scanner.nextInt());
//				}
//			}
//			
//		} catch (NoSuchElementException nsee) {
//			;
//		} catch (Exception e) {
//			System.out.println(e);
//			e.printStackTrace();
//		} finally {
//			scanner.close();
//		}
//
//		try {
//			if (!appChoices.containsKey(choice)) {
//				System.out.println("Bad choice. Try again");
//				choice = RETRY_CHOICE;
//			}
//
//			// System.out.println("User entered: " + choice);
//		} catch (InputMismatchException e) {
//			System.out.println("Invalid Response");
//		}
//
//		return choice.intValue();
//	}

	private void printMenuChoiceList(HashMap<Integer, String> menuChoices) {

		if (!menuChoices.isEmpty()) {
			System.out.println();
			System.out.println("What would you like to do?");

			Set<Integer> keys = menuChoices.keySet();
			for (Integer key : keys) {
				String description = menuChoices.get(key);

				System.out.println("\t" + key + ". " + description);
			}
		}
	}

	public void chooseAppToRun() {
		int choice = RETRY_CHOICE;

		HashMap<Integer, String> appChoices = new HashMap<Integer, String>();
		appChoices.put(POPULATE_HISTORICAL_PRICE_CHOICE, POPULATE_HISTORICAL_PRICES_MSG);
		appChoices.put(CALCULATE_MOVING_AVERAGES_CHOICE, CALCULATE_MOVING_AVERAGES_MSG);
		appChoices.put(BUY_STOCK_CHOICE, BUY_STOCK_MSG);
		appChoices.put(SELL_STOCK_CHOICE, SELL_STOCK_MSG);
		appChoices.put(CHECK_DB_CHOICE, CHECK_DB_MSG);
		appChoices.put(ADD_STOCK_CHOICE, ADD_STOCK_MSG);
		appChoices.put(EXIT_CHOICE, EXIT_MSG);

		try {
			while (true) {
				while (choice == RETRY_CHOICE) {
					printMenuChoiceList(appChoices);

					choice = InputUtils.getChoice(appChoices);
				}

				System.out.println("User chose: " + choice + " " + appChoices.get(new Integer(choice)) + "\n");

				switch (choice) {
				case POPULATE_HISTORICAL_PRICE_CHOICE:
					PopulateHistoricalPrices historicalPrices = new PopulateHistoricalPrices();
					historicalPrices.updateAllHistory();

					break;
				case CALCULATE_MOVING_AVERAGES_CHOICE:
					MovingAverages averages = new MovingAverages();
					averages.getAll200DayAverages();

					break;
				case BUY_STOCK_CHOICE:
					BuyStock buy = new BuyStock();
					buy.run();

					break;
				case SELL_STOCK_CHOICE:
					SellStock sell = new SellStock();
					sell.run();

					break;

				case CHECK_DB_CHOICE:
					CheckDatabaseConnection test = new CheckDatabaseConnection();
					test.run();

					break;
					
				case ADD_STOCK_CHOICE:
					AddStockToAccount add = new AddStockToAccount();
					add.run();

					break;
					
				case EXIT_CHOICE:
					return;

				default:
					break;
				}

				choice = RETRY_CHOICE;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		StockQuotesDriver driver = new StockQuotesDriver();

		driver.chooseAppToRun();
	}
}
