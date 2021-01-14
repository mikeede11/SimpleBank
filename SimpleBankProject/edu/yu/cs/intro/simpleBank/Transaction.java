package edu.yu.cs.intro.simpleBank;

import edu.yu.cs.intro.simpleBank.exceptions.InsufficientAssetsException;
import edu.yu.cs.intro.simpleBank.exceptions.UnauthorizedActionException;

/**
 * class for modeling deposits, withdrawals, buying & selling stock
 */
public class Transaction {
    private final double amount;
    private final Account account;
    private final Patron patron;
    private final TRANSACTION_TYPE txType;
    private long time;
    private String stockSymbol;
    protected enum TRANSACTION_TYPE{
        DEPOSIT,
        WITHDRAW,
        BUYSTOCK,
        SELLSTOCK}

    /**
     * @param patron the Patron who wishes to execute the transaction
     * @param type the type of transaction the patron wishes to execute
     * @param account the account on which the Patron wishes to execute the transaction
     * @throws UnauthorizedActionException if the patron does not own the account, OR if given transaction type
     * can not be executed on the given type of account (e.g. you can't sell stocks from a savings account)
     * @throws IllegalArgumentException 1) if the TRANSACTION_TYPE is buying or selling stock and the amount is not a whole number 2) if the amount is not >0
     */
    protected Transaction(Patron patron, TRANSACTION_TYPE type, Account account, double amount) throws UnauthorizedActionException{
        if(!(patron.getSavingsAccount().equals(account) || patron.getBrokerageAccount().equals(account))){throw new UnauthorizedActionException();}
        if((type.equals(TRANSACTION_TYPE.BUYSTOCK) || type.equals(TRANSACTION_TYPE.SELLSTOCK)) && (!(account instanceof BrokerageAccount))){throw new UnauthorizedActionException();}
        if((type.equals(TRANSACTION_TYPE.BUYSTOCK) || type.equals(TRANSACTION_TYPE.SELLSTOCK)) && ((amount - ((int)(amount))) > 0 )){throw new IllegalArgumentException();}
        if(amount < 0){throw new IllegalArgumentException();}
        this.patron = patron;
        this.txType = type;
        this.account = account;
        this.amount = amount;
    }

    /**time the tx took place, from System.currentTimeMillis()*/
    public long getTime(){
        return this.time;
    }
    protected void setStockSymbol(String symbol){
        this.stockSymbol = symbol;
    }
    public double getAmount() {
        return this.amount;
    }
    public Account getAccount(){
        return this.account;
    }
    public Patron getPatron(){
        return this.patron;
    }
    /**
     * Most do the following:
     * 1) based on the value of txType, check that all the instance variables that are required are set to valid values
     *      a) buying or selling stock requires that stockSymbol be set, and that amount be a whole number
     *      b) deposit or withdrawal requires amount be set
     * 2) executes the transaction, and sets the transaction time
     * @throws InsufficientAssetsException if there is not enough cash in the account to execute the transaction,
     *    OR if the Patron does not own as many shares in the given stock as he is attempting to sell
     */
    protected void execute() throws InsufficientAssetsException{
        switch (this.txType){
            case WITHDRAW:
                if(this.account.getAvailableBalance() < this.amount){
                    throw new InsufficientAssetsException();
                }
                this.account.withdrawCash(this.amount);
                this.time = System.currentTimeMillis();
                break;
            case DEPOSIT:
                this.account.depositCash(this.amount);
                this.time = System.currentTimeMillis();
                break;
            case BUYSTOCK:
                if(stockSymbol == null){throw new IllegalArgumentException();}//is this  ok?
                ((BrokerageAccount)account).buyShares(this.stockSymbol, (int)amount);
                this.time = System.currentTimeMillis();
                break;
            case SELLSTOCK:
                if(stockSymbol == null){throw new IllegalArgumentException();}
                ((BrokerageAccount)account).sellShares(this.stockSymbol, (int)amount);
            //etc.
        }
    }
        
       /* class SortByTime implements Comparator<Transaction>
        {
        public int compare(Transaction a, Transaction b)
        {
            long timeA = a.getTime();
            long timeB = b.getTime();
            if(timeA < timeB){return -1;}
            if(timeA == timeB){return 0;}
            else{return 1;}
        }
    }*/
    @Override
    public String toString(){
        return time + " " + txType + " " + patron;
    }
    
}