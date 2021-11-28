package martexcorp.com.swiftblood.BoardRequest;

/**
 * Created by Martex on 8/12/2018.
 */

public class BoardRequest {
    private String name;
    private String bloodGrp;
    private String region;
    private String address;
    private String tel;
    private String time;
    private String msg;



    public BoardRequest() {}

    public BoardRequest(String name,String bloodGrp,String region,String address, String tel,String time,String msg) {
        this.name = name;
        this.bloodGrp = bloodGrp;
        this.region = region;
        this.address = address;
        this.tel = tel;
        this.time = time;
        this.msg = msg;


    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBloodGrp() {
        return bloodGrp;
    }

    public void setBloodGrp(String bloodGrp) {
        this.bloodGrp = bloodGrp;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}