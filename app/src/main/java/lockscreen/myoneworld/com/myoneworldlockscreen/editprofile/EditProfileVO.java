package lockscreen.myoneworld.com.myoneworldlockscreen.editprofile;

public class EditProfileVO {
    private String firstName;
    private String lastName;
    private String birthday;
    private String email;
    private String country;
    private String phoneNumber;
    private String address;
    private String userProfileID;
    private String dealer;

    public String getUserProfileID() {
        return userProfileID;
    }

    public void setUserProfileID(String userProfileID) {
        this.userProfileID = userProfileID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirtday(String birtday) {
        this.birthday = birtday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDealer() {
        return dealer;
    }

    public void setDealer(String dealer) {
        this.dealer = dealer;
    }
}
