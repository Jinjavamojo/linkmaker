package thymeleafexamples.springsecurity.yandex;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class AuthorizationDetails {

    @Column(name = "rrn")
    private String rrn;

    @Column(name = "auth_code")
    private String authCode;

    public String getRrn() {
        return rrn;
    }

    public void setRrn(String rrn) {
        this.rrn = rrn;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }
}
