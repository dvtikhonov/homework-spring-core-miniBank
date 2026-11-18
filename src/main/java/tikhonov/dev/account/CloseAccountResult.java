package tikhonov.dev.account;

/**
 * Итог закрытия счета.
 *
 * @param closedAccountId   закрытый счет
 * @param targetAccountId   счет, на который перенесен остаток
 * @param transferredAmount перенесенный остаток
 */
public record CloseAccountResult(
        int closedAccountId,
        int targetAccountId,
        int transferredAmount
) {
}
