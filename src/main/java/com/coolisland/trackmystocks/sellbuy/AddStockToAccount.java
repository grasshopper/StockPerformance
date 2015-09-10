/**
 * 
 */
package com.coolisland.trackmystocks.sellbuy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.coolisland.trackmystocks.database.AccountBO;
import com.coolisland.trackmystocks.database.AccountDao;
import com.coolisland.trackmystocks.database.StockBO;
import com.coolisland.trackmystocks.database.StockDao;

/**
 * @author Silvio
 *
 */
public class AddStockToAccount {

	private static final int RETRY_CHOICE = -1;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BuyStock sell = new BuyStock();

		sell.run();
	}

	private void addStock(StockBO stock) {
		StockDao stockTickerDao;

		try {
			stockTickerDao = new StockDao();

			stockTickerDao.createStock(stock);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private AccountBO chooseAccount() {
		int choice = RETRY_CHOICE;
		AccountDao accountsDao = new AccountDao();
		AccountBO retValue = null;

		List<AccountBO> accounts = null;
		try {
			accounts = accountsDao.getPrimaryAccounts();

			if (accounts != null && accounts.size() > 0) {
				HashMap<Integer, String> accountMap = createMapFromAccounts(accounts);

				while (choice == RETRY_CHOICE) {
					System.out
							.println("Choose the account for which to buy a stock from the list below:");
					printAccountList(accounts);

					choice = getAccountChoice(accountMap);
				}

				for (AccountBO act : accounts) {
					if (act.getId().intValue() == choice) {
						retValue = act;
						break;
					}
				}

				System.out.println("User chose: " + choice + " "
						+ accountMap.get(new Integer(choice)));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return retValue;
	}

	private HashMap<Integer, String> createMapFromAccounts(
			List<AccountBO> accounts) {
		HashMap<Integer, String> accountIdMap = new HashMap<Integer, String>();

		if (accounts != null) {
			for (AccountBO act : accounts) {
				accountIdMap.put(new Integer(act.getId().intValue()),
						act.getName());
			}
		}

		return accountIdMap;
	}

	private int getAccountChoice(HashMap<Integer, String> accountMap) {
		int choice = RETRY_CHOICE;
		Scanner scanner = new Scanner(System.in);

		try {
			choice = scanner.nextInt();

			if (!accountMap.containsKey(choice)) {
				System.out.println("Bad choice. Try again");
				choice = RETRY_CHOICE;
			}

			// System.out.println("User entered: " + choice);
		} catch (InputMismatchException e) {
			System.out.println("Invalid Response");
		} finally {
			scanner.close();
		}

		return choice;
	}

	private ExchangeType getStockExchange() {
		ExchangeType exchange = null;
		String input = null;

		// print valid values for the user
		System.out.println("Enter the exchange:");
		System.out.println("\tValid values are:");
		System.out.println("\t\t" + ExchangeType.AMEX);

		while (exchange == null) {
			// get the exchange name from the user
			try {
				input = getStringInput("");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (input == null || input.length() == 0) {
				exchange = ExchangeType.getDefault();
			} else {
				// convert the user input into the type
				try {
					exchange = ExchangeType.valueOf(input);
				} catch (Exception e) {
					e.printStackTrace();
					exchange = null;
				}
			}
		}

		return exchange;
	}

	private String getStockName() {
		String name = null;

		while (name == null || name.length() == 0) {
			try {
				name = getStringInput("Enter the stock name");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return name;
	}

	private Boolean getStockOwn() {
		Boolean own = null;
		String input = null;

		while (own == null) {
			input = getYesNoInput("Do you own the stock?");

			if (input == null) {
				input = "Yes";
			}

			if ("Yes".equalsIgnoreCase(input)) {
				own = new Boolean(true);
			} else if ("No".equalsIgnoreCase(input)) {
				own = new Boolean(false);
			}
		}

		return own;
	}

	private String getStockSymbol() {
		String symbol = null;

		while (symbol == null || symbol.length() == 0) {
			try {
				symbol = getStringInput("Enter the stock symbol");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return symbol;
	}

	private Boolean getStockTrack() {
		Boolean track = false;
		String input = null;

		while (track == null) {
			input = getYesNoInput("Do you track this stock?");

			if (input == null) {
				input = "Yes";
			}

			if ("Yes".equalsIgnoreCase(input)) {
				track = new Boolean(true);
			} else if ("No".equalsIgnoreCase(input)) {
				track = new Boolean(false);
			}
		}

		return track;
	}

	private StockType getStockType() {
		StockType type = null;
		String input = null;

		// print valid values for the user
		System.out.println("Enter the stock type:");
		System.out.println("\tValid values are:");
		
		StockType allStockTypes = StockType.getFirst();
		StringBuffer options = new StringBuffer();
		while (allStockTypes != null) {
			options.append(allStockTypes.getStockType());
			options.append(", ");
			allStockTypes = allStockTypes.getNext();
		}
		System.out.println("\t\t" + options.toString());

		while (type == null) {
			// get the type name from the user
			try {
				input = getStringInput("");
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (input == null || input.length() == 0) {
				type = StockType.getDefault();
			} else {
				// convert the user input into the type
				try {
					type = StockType.valueFor(input);
				} catch (Exception e) {
					e.printStackTrace();
					type = null;
				}
			}
		}

		return type;
	}

	private String getStringInput(String inputMsg) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print(inputMsg);
		String s = br.readLine();

		return s;
	}

	private String getYesNoInput(String originalMessage) {
		String input = null;
		String message = new String(originalMessage);

		while (input == null) {
			try {
				input = getStringInput(message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (input == null) {
				input = "Yes";
			}
			else if (input.length() > 0) {
				if ("Yes".equalsIgnoreCase(input)
						|| "Y".equalsIgnoreCase(input)) {
					input = new String("Yes");
				} else if ("No".equalsIgnoreCase(input)
						|| "N".equalsIgnoreCase(input)) {
					input = new String("No");
				} else {
					message = new String(originalMessage + " (Enters Yes or No) ");
					
					input = null;
				}
			}
		}

		return null;
	}

	private void printAccountList(List<AccountBO> accounts) {
		for (AccountBO act : accounts) {
			System.out.println("\t" + act.getId() + ": " + act.getName());
		}
	}

	public void run() {

		try {
			// Which account does the user wish to add stocks to?
			AccountBO account = chooseAccount();

			// get stock information to create the stock
			String name = getStockName();
			String symbol = getStockSymbol();
			ExchangeType exchange = getStockExchange();
			Boolean own = getStockOwn();
			
			
			Boolean trackIt = null;
			if (own) {
				// if the user owns the stock, then we have to track it
				trackIt = new Boolean(true);
			} else {
				trackIt = getStockTrack();
			}
			
			StockType type = getStockType();

			StockBO newStock = new StockBO();
			newStock.setName(name);
			newStock.setExchange(exchange);
			newStock.setOwn(own);
			newStock.setSymbol(symbol);
			newStock.setTickerTypeId(type);
			newStock.setTrack(trackIt);
			newStock.setAccountId(account.getId());

			// add the stock to the DB
			addStock(newStock);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
