package edu.yu.cs.intro.simpleBank;

public class Patron {
    private final String firstName;
    private final String lastName;
    private final long socialSecurityNumber;
    private final String userName;
    private final String password;
    private BrokerageAccount brokerageAccount;
    private Account savingsAccount;

    protected Patron(String firstName, String lastName, long socialSecurityNumber, String userName, String password){
        this.firstName = firstName;
        this.lastName = lastName;
        this.socialSecurityNumber = socialSecurityNumber;
        this.userName = userName;
        this.password = password;
    }
    protected String getFirstName(){return firstName;}
    protected String getLastName(){return lastName;}
    protected String getUserName(){
        return this.userName;
    }
    protected String getPassword(){
        return this.password;
    }
    protected long getSocialSecurityNumber(){return socialSecurityNumber;}
    protected void addAccount(Account acct){
        if(acct instanceof BrokerageAccount){brokerageAccount = (BrokerageAccount)acct;}
        else{savingsAccount = acct;}
    }
    protected Account getAccount(long accountNumber) {
        if(accountNumber == savingsAccount.getAccountNumber()){return savingsAccount;}
        else if(accountNumber == brokerageAccount.getAccountNumber()){return brokerageAccount;}
        else{return null;}//if no account matches account number;
    }

    protected void setBrokerageAccount(BrokerageAccount account) {}
    protected void setSavingsAccount(Account account) {}
    protected BrokerageAccount getBrokerageAccount() {return brokerageAccount;}
    protected Account getSavingsAccount() {return savingsAccount;}

    /**
     * total cash in savings + total cash in brokerage + total value of shares in brokerage
     * return 0 if the patron doesn't have any accounts
     */
    protected double getNetWorth(){
        if(savingsAccount == null && brokerageAccount == null){return 0;}
        double total;
        if(savingsAccount == null){total = brokerageAccount.getTotalBalance();}
        if(brokerageAccount == null){total = savingsAccount.getAvailableBalance();}
        else{total = savingsAccount.getAvailableBalance() + brokerageAccount.getTotalBalance();}
        return total;
    }

    @Override
    public boolean equals(Object patron)
    {
        if(this.getSocialSecurityNumber() == ((Patron)patron).getSocialSecurityNumber()){ return true;}
        else {return false;}
    }
    @Override
    public String toString(){
        return firstName + " " + lastName + " " + socialSecurityNumber + " " + userName + " " + password;
    }
}
