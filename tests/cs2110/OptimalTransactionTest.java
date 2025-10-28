package cs2110;

import static org.junit.jupiter.api.Assertions.*;

import cs2110.Trading.BuySellTransaction;
import java.util.Random;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public abstract class OptimalTransactionTest {

    /**
     * This method should be used in the tests in place of a call to `optimalTransaction1()` or
     * `optimalTransaction2()`. It allows us to run the same set of tests for both versions of this
     * method (using a technique we'll learn about soon called the "template method pattern").
     */
    abstract BuySellTransaction optimalTransaction(int[] prices);

    @DisplayName("WHEN the price increases between each time step, THEN the optimal transaction "
            + "is to buy at the first time and sell at the last time.")
    @Test
    void testIncreasing() {
        int[] prices = new int[100];
        for (int i = 0; i < 100; i++) {
            prices[i] = i;
        }
        assertEquals(new BuySellTransaction(0, 99), optimalTransaction(prices));
    }

    @DisplayName("WHEN the price decreases between each time step, THEN the optimal transaction "
            + "is to buy and sell at the two consecutive time steps with the smallest decrease.")
    @Test
    void testDecreasing() {
        int[] prices = new int[100];
        for (int i = 0; i < 50; i++) {
            prices[i] = 200 - 2 * i;
        }
        for (int i = 50; i < 100; i++) {
            prices[i] = 201 - 2 * i;
        }
        assertEquals(new BuySellTransaction(49, 50), optimalTransaction(prices));
    }

    /**
     * Checks that the given transaction `t` has the maximum profit among all possible transactions
     * for the given `prices`.
     */
    void assertOptimalTransaction(int[] prices, BuySellTransaction t) {
        int tProfit = prices[t.sellTime()] - prices[t.purchaseTime()];

        for (int i = 0; i < prices.length; i++) { // purchase time
            for (int j = i + 1; j < prices.length; j++) { // sell time
                assertTrue(prices[j] - prices[i] <= tProfit);
            }
        }
    }

    @DisplayName("WHEN the optimal transaction is not unique, THEN any of these optimal "
            + "transactions may be returned.")
    @Test
    void testNonUnique() {
        int[] prices = new int[]{1, 1, 2, 3, 4, 5, 6, 7, 8, 8};
        assertOptimalTransaction(prices, optimalTransaction(prices));
    }

    // These tests do *not* completely cover the `optimalTransaction#()` methods. You're encouraged
    // to do add further tests to verify the correctness of your solution. You are not required to
    // submit the tests that you write.
}

class OptimalTransaction1Test extends OptimalTransactionTest {

    @Override
    BuySellTransaction optimalTransaction(int[] prices) {
        return Trading.optimalTransaction1(prices);
    }
}

class OptimalTransaction2Test extends OptimalTransactionTest {

    @Override
    BuySellTransaction optimalTransaction(int[] prices) {
        return Trading.optimalTransaction2(prices);
    }

}
