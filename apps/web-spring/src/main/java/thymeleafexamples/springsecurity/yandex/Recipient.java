package thymeleafexamples.springsecurity.yandex;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Recipient {

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "gateway_id")
    private String gatewayId;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getGatewayId() {
        return gatewayId;
    }

    public void setGatewayId(String gatewayId) {
        this.gatewayId = gatewayId;
    }
}
