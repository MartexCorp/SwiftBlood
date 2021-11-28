package martexcorp.com.swiftblood.BloodBankTable;

public class TableRowItem {

    String blood_group;
    String whole_blood, rbc, plasma, platelets, summation;

    public TableRowItem() {
    }

    public TableRowItem(String blood_group, String whole_blood, String rbc, String plasma, String platelets, String summation){

        this.blood_group = blood_group;
        this.whole_blood = whole_blood;
        this.rbc = rbc;
        this.plasma = plasma;
        this.platelets = platelets;
        this.summation = summation;
    }

    public String getBlood_group() {
        return blood_group;
    }

    public void setBlood_group(String blood_group) {
        this.blood_group = blood_group;
    }

    public String getWhole_blood() {
        return whole_blood;
    }

    public void setWhole_blood(String whole_blood) {
        this.whole_blood = whole_blood;
    }

    public String getRbc() {
        return rbc;
    }

    public void setRbc(String rbc) {
        this.rbc = rbc;
    }

    public String getPlasma() {
        return plasma;
    }

    public void setPlasma(String plasma) {
        this.plasma = plasma;
    }

    public String getPlatelets() {
        return platelets;
    }

    public void setPlatelets(String platelets) {
        this.platelets = platelets;
    }

    public String getSummation() {
        return summation;
    }

    public void setSummation(String summation) {
        this.summation = summation;
    }
}
