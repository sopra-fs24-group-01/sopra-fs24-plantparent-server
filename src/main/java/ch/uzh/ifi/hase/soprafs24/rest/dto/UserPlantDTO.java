package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class UserPlantDTO {
    private String userEmail;
    private String message;


    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
