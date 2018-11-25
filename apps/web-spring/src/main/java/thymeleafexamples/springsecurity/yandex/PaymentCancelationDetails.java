package thymeleafexamples.springsecurity.yandex;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class PaymentCancelationDetails {

    @Column(name = "party")
    private String party;

    @Column(name = "reason")
    private String reason;

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
