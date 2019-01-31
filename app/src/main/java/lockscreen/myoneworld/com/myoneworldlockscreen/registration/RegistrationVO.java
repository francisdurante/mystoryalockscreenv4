package lockscreen.myoneworld.com.myoneworldlockscreen.registration;

public class RegistrationVO {
    private String firstName;
    private String lastName;
    private String email;
    private String country;
    private String contact;
    private String password;
    private String birthday;
    private String address;
    private String socialID;
    private String registrationStatusMessage;

    public String getRegistrationStatusMessage() {
        return registrationStatusMessage;
    }

    public void setRegistrationStatusMessage(String registrationStatusMessage) {
        this.registrationStatusMessage = registrationStatusMessage;
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSocialID() {
        return socialID;
    }

    public void setSocialID(String socialID) {
        this.socialID = socialID;
    }
}
