package thymeleafexamples.springsecurity.yandex;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Embeddable
public class Card {

    @Column(name = "first6")
    @Max(4)
    @Min(4)
    private Integer first6;

    @Column(name = "last4")
    @Max(4)
    @Min(4)
    private Integer last4;

    @Column(name = "expiry_month")
    @Max(2)
    @Min(1)
    private Integer expiryMonth;

    @Column(name = "expiry_year")
    @Max(4)
    @Min(4)
    private Integer expiryYear;

    @Column(name = "card_type")
    private String cardType;

    public Integer getFirst6() {
        return first6;
    }

    public void setFirst6(Integer first6) {
        this.first6 = first6;
    }

    public Integer getLast4() {
        return last4;
    }

    public void setLast4(Integer last4) {
        this.last4 = last4;
    }

    public Integer getExpiryMonth() {
        return expiryMonth;
    }

    public void setExpiryMonth(Integer expiryMonth) {
        this.expiryMonth = expiryMonth;
    }

    public Integer getExpiryYear() {
        return expiryYear;
    }

    public void setExpiryYear(Integer expiryYear) {
        this.expiryYear = expiryYear;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }
}
