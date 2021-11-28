package martexcorp.com.swiftblood.BankItem;

public class BankItem {

    private String bank_name, bank_region,bank_number,bankKey;
    private String lastUpdated;

    public BankItem (){}

    public BankItem (String bank_name, String bank_region, String bank_number, String lastUpdated, String key){

        this.bank_name = bank_name;
        this.bank_region = bank_region;
        this.bank_number = bank_number;
        this.lastUpdated = lastUpdated;
        this.bankKey = key;

    }

    public String getBank_name() { return bank_name; }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getBank_region() {
        return bank_region;
    }

    public void setBank_region(String bank_region) {
        this.bank_region = bank_region;
    }

    public String getBank_number() {
        return bank_number;
    }

    public void setBank_number(String bank_number) {
        this.bank_number = bank_number;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getBankKey() {
        return bankKey;
    }

    public void setBankKey(String bankKey) {
        this.bankKey = bankKey;
    }

}
