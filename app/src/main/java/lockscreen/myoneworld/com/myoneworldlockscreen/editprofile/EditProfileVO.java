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
    private String oldPassword;
    private String newPassword;
    private String facebookKey;
    private String googleKey;
    private String twitterKey;
    private boolean isChangePassword;

    public String getFacebookKey() {
        return facebookKey;
    }

    public void setFacebookKey(String facebookKey) {
        this.facebookKey = facebookKey;
    }

    public String getGoogleKey() {
        return googleKey;
    }

    public void setGoogleKey(String googleKey) {
        this.googleKey = googleKey;
    }

    public String getTwitterKey() {
        return twitterKey;
    }

    public void setTwitterKey(String twitterKey) {
        this.twitterKey = twitterKey;
    }

    public boolean isChangePassword() {
        return isChangePassword;
    }

    public void setChangePassword(boolean changePassword) {
        isChangePassword = changePassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

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
