package edu.yu.cs.intro.simpleBank;

import edu.yu.cs.intro.simpleBank.exceptions.InsufficientAssetsException;
import java.util.Map;
import java.util.HashMap;

public class BrokerageAccount extends Account {
    private Map<String,Integer> stocksToNumberOfShares;

    protected BrokerageAccount(long accountNumber) {
        super(accountNumber);
        stocksToNumberOfShares = new HashMap<String,Integer>();
    }

    protected int getNumberOfShares(String stock){
        if(stocksToNumberOfShares.get(stock) == null){return 0;}
        else {return stocksToNumberOfShares.get(stock);}
    }
    /**
     * Buy the given amount of the given stock. Must have enough cash in the account to purchase them.
     * If there is enough cash, reduce cash and increase shares of the given stock
     * If there is not enough cash, throw an InsufficientAssetsException
     */
    protected void buyShares(String stock, int shares) throws InsufficientAssetsException {
        if(this.getAvailableBalance() < Bank.getBank().getStockPrice(stock) * shares){throw new InsufficientAssetsException();}
        this.withdrawCash(Bank.getBank().getStockPrice(stock) * shares);//operator precedence
        //now put the stock and shares in map.
        if(stocksToNumberOfShares.get(stock) == null){stocksToNumberOfShares.put(stock, shares);}
        else{stocksToNumberOfShares.put(stock, stocksToNumberOfShares.get(stock) + shares);}
    }

    /**
     * Sell the given amount of the given stock. Must have enough shares in the account to sell.
     * If there are enough shares, reduce shares and increase cash.
     * If there are not enough shares, throw an InsufficientAssetsException
     */
    protected void sellShares(String stock, int shares) throws InsufficientAssetsException{
        if(stocksToNumberOfShares.get(stock) < shares){throw new InsufficientAssetsException();}
        this.depositCash(Bank.getBank().getStockPrice(stock) * shares);//Operator Precedence
        stocksToNumberOfShares.put(stock, stocksToNumberOfShares.get(stock) - shares);
    }

    /**
     * this method must return the total amount of cash + the total market value of all stocks owned.
     * The market value of a single stock is determined by multiplying the share price of the stock times the number of shares owned
     * @return
     */
    protected double getTotalBalance(){
        return this.getAvailableBalance() + this.getMarketValue();
    }
    protected double getMarketValue(){
        double total = 0;
        if(stocksToNumberOfShares == null){return total;}
        for(String stock : stocksToNumberOfShares.keySet())
        {
            total += stocksToNumberOfShares.get(stock) *  Bank.getBank().getStockPrice(stock);
        }
        return total;
    }
}