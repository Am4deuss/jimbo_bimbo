package se.jimboagency.bookingsystem;

public class User {
    private String userName;
    private String employeeName;
    private String password;

    public User(String userName, String employeeName, String password) {
        this.userName = userName;
        this.employeeName = employeeName;
        this.password = password;
    }

    public boolean getPassword(String passArg){
        if(passArg.equals(this.password)){
            return true;
        }
        else{return false;}
    }

    public String getUserName(){
        return this.userName;
    }
    public String getEmployeeName(){
        return this.employeeName;
    }

}
