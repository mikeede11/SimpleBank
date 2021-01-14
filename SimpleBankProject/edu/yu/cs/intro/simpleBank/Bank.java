package edu.yu.cs.intro.simpleBank;

import edu.yu.cs.intro.simpleBank.exceptions.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Bank {

    private Set<Patron> bankPatrons;
    private Map<String,Double> stocksSymbolToPrice;
    /**
     * transaction history is no longer stored in each patron object. Instead, the bank maintains a Map of transactions, mapping each Patron to the List of transactions that the given patron has executed.
     */
    private Map<Patron,List<Transaction>> txHistoryByPatron;
    protected final double transactionFee;
    private int accountNumber;

    private static Bank INSTANCE;

    public Bank(double transactionFee){
        this.transactionFee = transactionFee;
        INSTANCE = this;
        accountNumber = 0;
        bankPatrons = new HashSet<Patron>();
        stocksSymbolToPrice = new HashMap<String,Double>();
        txHistoryByPatron = new HashMap<Patron,List<Transaction>>();
        //initialize your collections here
    }

    public static Bank getBank(){
        return INSTANCE;
    }

    /**
     * Lists a new stock with the given symbol at the given price
     * @return false if the stock was previously listed, true if it was added as a result of this call
     */
    protected boolean addNewStockToMarket(String tickerSymbol, double sharePrice){
        if(stocksSymbolToPrice.get(tickerSymbol) == null){//if the stock value is not null then there is already a stock and the stock wont be added thus false.
            stocksSymbolToPrice.put(tickerSymbol, sharePrice);
            System.out.println(stocksSymbolToPrice.keySet() + " " + stocksSymbolToPrice.get("GOOG"));
            return true;
        }
        else{return false;}
        //if the stock is already listed, return false
        //otherwise, add the key-value pair to the stocksSymbolToPrice map and return true;
    }

    /**
     * @return the stock price for the given stock ticker symbol. Return 0 if there is no such stock.
     */
    public double getStockPrice(String symbol){
        if(stocksSymbolToPrice.get(symbol) == null){return 0;}
        else{return stocksSymbolToPrice.get(symbol);}
    }

    /**
     * @return a set the stock ticker symbols listed in this bank
     */
    public Set<String> getAllStockTickerSymbols(){
        return stocksSymbolToPrice.keySet();
    }

    /**
     * @return the total number of shares of the given stock owned by all patrons combined
     * if there is no such Stock or if the tickerSymbol is empty or null, return 0
     */
    public int getNumberOfOutstandingShares(String tickerSymbol){
        if(stocksSymbolToPrice.get(tickerSymbol) == null || tickerSymbol.equals("") || tickerSymbol == null){return 0;}
        int total = 0;
        for(Patron patron : bankPatrons)
        {
            if(patron.getBrokerageAccount() == null){}
            else{
                    total += patron.getBrokerageAccount().getNumberOfShares(tickerSymbol);// in no stock return 0(on get num shares method)
                }
        }
        return total;
    }

    /**
     * @return the total number of shares of the given stock owned by all patrons combined multiplied by the price per share
     * if there is no such Stock or if the tickerSymbol is empty or null, return 0
     */
    public int getMarketCapitalization(String tickerSymbol){
        int total = 0;
        if(stocksSymbolToPrice.get(tickerSymbol) == null){return 0;}//if this stocks not on the market then lo kol shecayn we have 0 shares.
        total = (int)(getNumberOfOutstandingShares(tickerSymbol) * stocksSymbolToPrice.get(tickerSymbol));
        return total;
    }

    /**
     * @return all the cash in all savings accounts added up
     */
    public double getTotalSavingsInBank(){
        int total = 0;
        for(Patron patron : bankPatrons)
        {
            if(patron.getSavingsAccount() == null){}
            else{
            total += patron.getSavingsAccount().getAvailableBalance();
            }
        }
        return total;
    }

    /**
     * @return all the cash in all brokerage accounts added up
     */
    public double getTotalBrokerageCashInBank(){
        int total = 0;
         for(Patron patron : bankPatrons)
        {
            if(patron.getBrokerageAccount() == null){}
            else{
            total += patron.getBrokerageAccount().getAvailableBalance();
            }
        }
        return total;
    }

    /**
     * Creates a new Patron in the bank.
     */
    public void createNewPatron(String firstName, String lastName, long socialSecurityNumber, String userName, String password){
        Patron p = new Patron(firstName, lastName, socialSecurityNumber, userName, password);
        bankPatrons.add(p);
        for(Patron patron : bankPatrons){
        System.out.print("PATRON " + patron);
        }
    }

    /**
     * @return the account number of the opened account
     */
    private Patron logIn( long socialSecurityNumber, String userName, String password)throws AuthenticationException
    {
        Patron user = null;
        for(Patron p : bankPatrons)
        {
            //if(p == null){break;}
            if(socialSecurityNumber == p.getSocialSecurityNumber())
            {
                if(p.getUserName().equals(userName) && p.getPassword().equals(password))
                { 
                   user = p;
                }
            }
        }
        if(user == null){throw new AuthenticationException();}
            
        return user;
    }
    public long openSavingsAccount( long socialSecurityNumber, String userName, String password) {
        
        try{
            Patron p = logIn(socialSecurityNumber, userName, password);
            Account newSavsAcc = new Account(accountNumber + 1);
            accountNumber += 1; //this make sense?
            p.addAccount(newSavsAcc);
            return newSavsAcc.getAccountNumber();//or just our instance variable account num.
        }catch(AuthenticationException e)
        {
            System.out.println("Sorry log in failed");
            return 0;}
    }

    /**
     * @return the account number of the opened account
     */
    public long openBrokerageAccount(long socialSecurityNumber, String userName, String password) {
        try{
        Patron p = logIn(socialSecurityNumber, userName, password);
        BrokerageAccount newBrokAcc = new BrokerageAccount(accountNumber + 1);
        accountNumber += 1; //this make sense?
        p.addAccount(newBrokAcc);
        return newBrokAcc.getAccountNumber();
        }catch(AuthenticationException e)
        {
            System.out.println("Sorry log in failed");
            return 0;}
    }
    private void updateTxHist(Patron p, Transaction t)
    {
         if(txHistoryByPatron.get(p) == null){//if you dont have a Tx Hist its b/c you have no Txs.
            List<Transaction> newTxHist = new ArrayList<>(Arrays.asList(t));
            //newTxHist.add(t);
            txHistoryByPatron.put(p, newTxHist);
        }
        else{
            List<Transaction> updatedTxHist = txHistoryByPatron.get(p);
            updatedTxHist.add(t);
            txHistoryByPatron.put(p, updatedTxHist);
        }
    }

    /**
     * Deposit cash into the given savings account
     */
    public void depositCashIntoSavings(long socialSecurityNumber, String userName, String password, double amount){
        try{
        Patron p = logIn(socialSecurityNumber, userName, password);
        Transaction newDep = new Transaction(p, Transaction.TRANSACTION_TYPE.DEPOSIT, p.getSavingsAccount(), amount);
        newDep.execute();
        System.out.println(p.getSavingsAccount().getAvailableBalance());
        updateTxHist(p, newDep);
        }catch(AuthenticationException e){System.out.println("Sorry log in failed");}
        catch(UnauthorizedActionException e){System.out.println("Sorry that account does not exist or this Patron does not own this account");}
        catch(InsufficientAssetsException e){}//ASSUME DEPSOIT ALWAYS MORE THAN TXFEE.
    }
    /**
 * transfer cash from the patron's savings account to his brokerage account
 * throws AuthenticationException if the SS#, username, and password don't match a bank patron
 * throws UnauthorizedActionException if the given patron does not have both a savings account and a brokerage account
 * throws InsufficientAssetsException if that amount of money is not present in the savings account
 */
public void transferFromSavingsToBrokerage(long socialSecurityNumber, String userName, String password, double amount) throws AuthenticationException,UnauthorizedActionException, InsufficientAssetsException {
    Patron p = logIn(socialSecurityNumber, userName, password);
    Transaction fromSavings = new Transaction(p, Transaction.TRANSACTION_TYPE.WITHDRAW, p.getSavingsAccount(), amount);
    Transaction toBrokerage = new Transaction(p, Transaction.TRANSACTION_TYPE.DEPOSIT, p.getBrokerageAccount(), amount); 
    fromSavings.execute();
    toBrokerage.execute();
    System.out.println(p.getSavingsAccount().getAvailableBalance());
    System.out.println(p.getBrokerageAccount().getAvailableBalance());
    updateTxHist(p, fromSavings);
    updateTxHist(p,toBrokerage);
}

/**
 * transfer cash from the patron's savings account to his brokerage account
 * throws AuthenticationException if the SS#, username, and password don't match a bank patron
 * throws UnauthorizedActionException if the given patron does not have both a savings account and a brokerage account
 * throws InsufficientAssetsException if that amount of money is not present in CASH in the brokerage account
 */
public void transferFromBrokerageToSavings(long socialSecurityNumber, String userName, String password, double amount) throws AuthenticationException,UnauthorizedActionException, InsufficientAssetsException{
    Patron p = logIn(socialSecurityNumber, userName, password);
    Transaction fromBrokerage = new Transaction(p, Transaction.TRANSACTION_TYPE.WITHDRAW, p.getBrokerageAccount(), amount);
    Transaction toSavings = new Transaction(p, Transaction.TRANSACTION_TYPE.DEPOSIT, p.getSavingsAccount(), amount); 
    fromBrokerage.execute();
    toSavings.execute();
    System.out.println(p.getBrokerageAccount().getAvailableBalance());
    System.out.println(p.getSavingsAccount().getAvailableBalance());
    updateTxHist(p, fromBrokerage);
    updateTxHist(p,toSavings);
}

    /**
     * withdraw cash from the patron's savings account
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * throw UnauthorizedActionException if the given patron does not have a savings account
     * throw InsufficientAssetsException if that amount of money is not present the savings account
     */
    public void withdrawCashFromSavings(long socialSecurityNumber, String userName, String password, double amount) throws AuthenticationException,UnauthorizedActionException,InsufficientAssetsException{
        Patron p = logIn(socialSecurityNumber, userName, password);
        if(p.getSavingsAccount() == null){throw new UnauthorizedActionException();}
        Transaction newWithDraw = new Transaction(p, Transaction.TRANSACTION_TYPE.WITHDRAW, p.getSavingsAccount(), amount);
        newWithDraw.execute();
        System.out.println(p.getSavingsAccount().getAvailableBalance());
        updateTxHist(p, newWithDraw);
    }
    /**
     * withdraw cash from the patron's brokerage account
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * throw UnauthorizedActionException if the given patron does not have a brokerage account
     * throw InsufficientAssetsException if that amount of CASH is not present the brokerage account
     */
    public void withdrawCashFromBrokerage(long socialSecurityNumber, String userName, String password, double amount) throws AuthenticationException,UnauthorizedActionException,InsufficientAssetsException{
        Patron p = logIn(socialSecurityNumber, userName, password);
        if(p.getBrokerageAccount() == null){throw new UnauthorizedActionException();}
        Transaction newWithDraw = new Transaction(p, Transaction.TRANSACTION_TYPE.WITHDRAW, p.getBrokerageAccount(), amount);
        newWithDraw.execute();
        updateTxHist(p, newWithDraw);
    }

    /**
     * check how much cash the patron has in his brokerage account
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * throw UnauthorizedActionException if the given patron does not have a brokerage account
     */
    public double checkCashInBrokerage(long socialSecurityNumber, String userName, String password) throws AuthenticationException,UnauthorizedActionException{
        Patron p = logIn(socialSecurityNumber, userName, password);
        if(p.getBrokerageAccount() == null){throw new UnauthorizedActionException();}
        return p.getBrokerageAccount().getAvailableBalance();
    }
    /**
     * check the total value of the patron's brokerage account
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * throw UnauthorizedActionException if the given patron does not have a brokerage account
     */
    public double checkTotalBalanceBrokerage(long socialSecurityNumber, String userName, String password) throws AuthenticationException,UnauthorizedActionException{
        Patron p = logIn(socialSecurityNumber, userName, password);
        if(p.getBrokerageAccount() == null){throw new UnauthorizedActionException();}
        return p.getBrokerageAccount().getTotalBalance();
    }
    /**
     * check how much cash the patron has in his savings account
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * throw UnauthorizedActionException if the given patron does not have a savings account
     */
    public double checkBalanceSavings(long socialSecurityNumber, String userName, String password) throws AuthenticationException,UnauthorizedActionException{
        Patron p = logIn(socialSecurityNumber, userName, password);
        if(p.getSavingsAccount() == null){throw new UnauthorizedActionException();}
        return p.getSavingsAccount().getAvailableBalance();
    }

    /**
     * buy shares of the given stock
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * throw UnauthorizedActionException if the given patron does not have a brokerage account
     * throw InsufficientAssetsException if the required amount of CASH is not present in the brokerage account
     */
    public void purchaseStock(long socialSecurityNumber, String userName, String password, String tickerSymbol, int shares) throws AuthenticationException,UnauthorizedActionException,InsufficientAssetsException{
        Patron p = logIn(socialSecurityNumber, userName, password);
        if(p.getBrokerageAccount() == null){throw new UnauthorizedActionException();}
        Transaction newPurchase = new Transaction(p, Transaction.TRANSACTION_TYPE.BUYSTOCK, p.getBrokerageAccount(), shares);
        newPurchase.setStockSymbol(tickerSymbol);
        newPurchase.execute();//throws an IAE from buyShares
        System.out.println(p.getBrokerageAccount().getNumberOfShares("GOOG"));
        System.out.println(p.getBrokerageAccount().getNumberOfShares("APPL"));
        updateTxHist(p, newPurchase);
    }

    /**
     * sell shares of the given stock
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * throw UnauthorizedActionException if the given patron does not have a brokerage account
     * throw InsufficientAssetsException if the patron does not have the given number of shares of the given stock
     */
    public void sellStock(long socialSecurityNumber, String userName, String password, String tickerSymbol, int shares) throws AuthenticationException,UnauthorizedActionException,InsufficientAssetsException{
        Patron p = logIn(socialSecurityNumber, userName, password);
        if(p.getBrokerageAccount() == null){throw new UnauthorizedActionException();}
        Transaction newSale = new Transaction(p, Transaction.TRANSACTION_TYPE.SELLSTOCK, p.getBrokerageAccount(), shares);
        newSale.setStockSymbol(tickerSymbol);
        newSale.execute();//throws an IAE from sellShares
        System.out.println(p.getBrokerageAccount().getNumberOfShares("GOOG"));
        System.out.println(p.getBrokerageAccount().getNumberOfShares("APPL"));
        updateTxHist(p, newSale);
    }

    /**
     * check the net worth of the patron
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     * return 0 if the patron doesn't have any accounts
     */
    public double getNetWorth(long socialSecurityNumber, String userName, String password) throws AuthenticationException{
        Patron p = logIn(socialSecurityNumber, userName, password);
        return p.getNetWorth();
    }

    /**
     * Get the transaction history on all of the patron's accounts, i.e. the transaction histories of both the savings account and
     * brokerage account (whichever of the two exist), combined. The merged history should be sorted in time order, from oldest to newest.
     * If the patron has no transactions in his history, return an array of length 0.
     * throw AuthenticationException if the SS#, username, and password don't match a bank patron
     */
    public Transaction[] getTransactionHistory(long socialSecurityNumber, String userName, String password) throws AuthenticationException{
        Patron p = logIn(socialSecurityNumber, userName, password);
        if(txHistoryByPatron.get(p) == null){
            Transaction[] tx = new Transaction[0];
            return tx;
        }
        //Transaction[] tx = new Transaction[100];
        Transaction[] tx = (txHistoryByPatron.get(p)).toArray(new Transaction[0]);

        return organizeHistoryByTime(tx);
    }
    private Transaction[] organizeHistoryByTime(Transaction[] history)
    {
        Transaction laterTx;
        Transaction[] chronologicalHistory = new Transaction[history.length];
        chronologicalHistory[0] = history[0];
        for(int i = 0; i < history.length; i++){
            for( int c = i + 1; c < history.length; c++)
                {
                if(history[i].getTime() > history[c].getTime()){
                    laterTx = history[i];
                    history[i] = history[c];
                    history[c] = laterTx;
                }
            }
        }
        return history;
    }
}

    /*class SortByTime implements Comparator<Transaction>
    {
        @Override
        public int compare(Transaction a, Transaction b)
        {
            long timeA = a.getTime();
            long timeB = b.getTime();
            if(timeA < timeB){return -1;}
            if(timeA == timeB){return 0;}
            else{return 1;}
        }
    }*/
    
