package ch.uzh.ifi.hase.soprafs24.rest.dto;

public class CaretakerPostDTO {

    public Long caretakerId;
    public Long currentUserId;

    public Long getCaretakerId() {
        return caretakerId;
    }
    
    public void setCaretakerId(Long caretakerId) {
        this.caretakerId = caretakerId;
    }

    public Long getCurrentUserId() {
        return currentUserId;
    }
    
    public void setCurrentUserId(Long currentUserId) {
        this.currentUserId = currentUserId;
    }
}
