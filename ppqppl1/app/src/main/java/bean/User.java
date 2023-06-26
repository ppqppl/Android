package bean;

public class User {
    String account;
    String pwd;
    String name;
    String phonenum;
    String ID;
    String authority;

    public User(){}

    public User(String account, String pwd, String name, String phonenum,String ID,String authority){
        this.account = account;
        this.pwd = pwd;
        this.name = name;
        this.phonenum = phonenum;
        this.ID = ID;
        this.authority = authority;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
