package edu.yu.cs.intro.simpleBank;

import edu.yu.cs.intro.simpleBank.exceptions.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Arrays;
import java.util.ArrayList;
public class UseBank{
	public static void main(String[] args){
	Bank create = new Bank(5);
	Bank theBank = Bank.getBank();
	if(theBank.addNewStockToMarket("GOOG", 55.0)){System.out.println("Stock added to Market");}//<-----
	else{System.out.println("stock not added. The stock should already be on the market");}
	if(theBank.addNewStockToMarket("GOOG", 55.0)){System.out.println("Stock added to Market");}
	else{System.out.println("stock not added. The stock should already be on the market");}//<----
	System.out.println(theBank.getStockPrice("GOOG"));//should return 55
	System.out.println(theBank.getStockPrice("POOP"));//should return 0
	theBank.addNewStockToMarket("APPL", 75.0);
	theBank.addNewStockToMarket("TSLA", 120.0);
	System.out.println(theBank.getAllStockTickerSymbols());
	theBank.createNewPatron("Michael", "Edelman", 111111111, "MikeEde11", "cars88" );
	theBank.createNewPatron("Nathan", "Edelman", 222222222, "YoyoMan98", "cars87" );
	theBank.createNewPatron("Yitzy", "Wilens", 333333333, "Super8", "SuperCool" );
	System.out.println("\n" + theBank.openSavingsAccount(111111111, "MikeEde11", "cars88"));//should print out 1 for Acct #
	System.out.println("\n" + theBank.openBrokerageAccount(111111111, "MikeEde11", "cars88"));//should be 2
	theBank.depositCashIntoSavings(111111111, "MikeEde11", "cars88", 10000);//cash in savings should be 9995
	//javac Account.java Bank.java BrokerageAccount.java BrokerageAccount.java exceptions\InsufficientAssetsException.java exceptions\AuthenticationException.java exceptions\UnauthorizedActionException.java Patron.java Transaction.java UseBank.java
	try{
	theBank.withdrawCashFromSavings(111111111, "MikeEde11", "cars88", 100);//Should be 9890
	theBank.transferFromSavingsToBrokerage(111111111, "MikeEde11", "cars88", 5000);//broke = 4995, Savings = 4885
	theBank.transferFromBrokerageToSavings(111111111, "MikeEde11", "cars88", 1000);//broke =3990, Savings = 5880
	theBank.withdrawCashFromBrokerage(111111111, "MikeEde11", "cars88", 500);//Broke=3485
	theBank.purchaseStock(111111111, "MikeEde11", "cars88", "GOOG", 5);//Broke = 3315 Print 5 0
	theBank.purchaseStock(111111111, "MikeEde11", "cars88", "APPL", 2);//Broke = 3160 Pirnt 5 2
	for(int i = 0; i <10000000; i++){}
	System.out.println(theBank.checkCashInBrokerage(111111111, "MikeEde11", "cars88"));//Broke= 3160(checks for both this and method above)
	System.out.println(theBank.checkTotalBalanceBrokerage(111111111, "MikeEde11", "cars88"));//should put stocks in before = 3475
	System.out.println(theBank.checkBalanceSavings(111111111, "MikeEde11", "cars88"));//should be savings = 5880.
	theBank.sellStock(111111111, "MikeEde11", "cars88", "GOOG", 1);// Brokecash = 3045? Broke total = 3470// 4 2
	System.out.println(theBank.checkTotalBalanceBrokerage(111111111, "MikeEde11", "cars88"));
	System.out.println(theBank.checkCashInBrokerage(111111111, "MikeEde11", "cars88"));//3100
	System.out.println(theBank.getNetWorth(111111111, "MikeEde11", "cars88"));//9350

	System.out.println(theBank.getNumberOfOutstandingShares("GOOG"));// 4
	System.out.println(theBank.getMarketCapitalization("GOOG"));// 220
	System.out.println(theBank.getTotalSavingsInBank());//5880
	System.out.println(theBank.getTotalBrokerageCashInBank());//3100
	for(Transaction t : theBank.getTransactionHistory(111111111, "MikeEde11", "cars88"))
	{
		System.out.println(t.toString());
	}
	//System.out.println(theBank.getTransactionHistory(111111111, "MikeEde11", "cars88"));
	}catch(AuthenticationException e){System.out.println("A");}
	catch(UnauthorizedActionException e){System.out.println("B");}
	catch(InsufficientAssetsException e){System.out.println("C");}

	}
}
