package martexcorp.com.swiftblood.DonorList;

public class DonorList {

    private String pseudo;
    private String town;
    private String bloodGrp;
    private String tel;

    public DonorList(){}

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getBloodGrp() {
        return bloodGrp;
    }

    public void setBloodGrp(String bloodGrp) {
        this.bloodGrp = bloodGrp;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public DonorList(String pseudo, String town, String bloodGrp, String tel){

        this.pseudo=pseudo;
        this.town=town;
        this.bloodGrp=bloodGrp;
        this.tel=tel;
    }
}
