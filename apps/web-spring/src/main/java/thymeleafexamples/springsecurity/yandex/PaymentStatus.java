package thymeleafexamples.springsecurity.yandex;

public enum PaymentStatus {

    PENDING("pending"), //платеж создан но не завершен, ожидает подтверждения пользователем( в теле запроса есть confirmation)
    WAITING_FOR_CAPTURE("waiting_for_capture"), //подтвержден и ожидает действий сервера
    CANCELED("canceled"), //финальный неизменяемый статус
    SUCCEEDED("succeeded"),//платеж подтвержден и деньги придут на р/c
    NOT_FOUND("not_found");// внутреннее статус для нашей системы, обозначающий несуществующий платеж

    String value;

    PaymentStatus(String value) {
        this.value = value;
    }
}
