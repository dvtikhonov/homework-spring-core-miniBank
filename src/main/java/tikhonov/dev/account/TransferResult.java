package tikhonov.dev.account;

/**
 * Итог перевода между счетами.
 *
 * @param sourceAccountId счет отправителя
 * @param targetAccountId счет получателя
 * @param amount          сумма, списанная у отправителя
 * @param commission      удержанная комиссия
 * @param received        сумма, зачисленная получателю
 * @param sameUser        перевод между счетами одного пользователя
 */
public record TransferResult(
        int sourceAccountId,
        int targetAccountId,
        int amount,
        int commission,
        int received,
        boolean sameUser
) {
}
