package ch.uzh.ifi.hase.soprafs24.rest.dto;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EmailMessageDTO {
    private String fromEmail = "groot-root@plantparent.ch";
    private String fromName = "Groot Root";
    private String replyToEmail = "nordinbensalem.dari@uzh.ch";
    private String toEmail;
    private String subject = "You have plants that need attention!";
    private String textPart;

    @JsonProperty("From")
    public Map<String, String> getFrom() {
        Map<String, String> from = new HashMap<>();
        from.put("Email", fromEmail);
        from.put("Name", fromName);
        return from;
    }

    @JsonProperty("ReplyTo")
    public Map<String, String> getReplyTo() {
        Map<String, String> replyTo = new HashMap<>();
        replyTo.put("Email", replyToEmail);
        return replyTo;
    }

    @JsonProperty("To")
    public List<Map<String, String>> getTo() {
        Map<String, String> to = new HashMap<>();
        to.put("Email", toEmail);
        return Collections.singletonList(to);
    }

    public void setToEmail(String userEmail) {
        this.toEmail = userEmail;
    }

    /*public String getToEmail() {
        return toEmail;
    }*/

    @JsonProperty("Subject")
    public String getSubject() {
        return subject;
    }

    @JsonProperty("TextPart")
    public String getTextPart() {
        return textPart;
    }

    public void setTextPart(String message) {
        this.textPart = message;
    }

}
