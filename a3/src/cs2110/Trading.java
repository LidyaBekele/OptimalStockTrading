package cs2110;

import java.util.Random;

/**
 * Contains methods for computing the optimal achievable profit of a stock transaction based on its
 * price history in a given time window.
 */
public class Trading {

    /**
     * Represents a stock transaction in which a share is purchased at the `purchaseTime` and sold
     * at the `sellTime`. Requires that `purchaseTime < sellTime`.
     */
    record BuySellTransaction(int purchaseTime, int sellTime) {

    }

    /**
     * Returns the profit earned through the given `BuySellTransaction t` for the given `prices`
     * array.
     */
    static int profit(int[] prices, BuySellTransaction t) {
        return prices[t.sellTime()] - prices[t.purchaseTime()];
    }

    /**
     * Returns the *index* of the maximum value in `prices(i..]`. Requires that `0 <= i <
     * prices.length-1`.
     */
    static int argmaxTail(int[] prices, int i) {
        int max = i;
        int j = i + 1;
        /*
         * Loop invariant: max is the index of the largest element in prices[i..j).
         */
        while (j < prices.length) {
            if (prices[j] > prices[max]) {
                max = j;
            }
            j++;
        }
        return max;
    }

    /**
     * Returns a BuySellTransaction with the maximum achievable profit for the given `prices`
     * window.
     */
    static BuySellTransaction optimalTransaction1(int[] prices) {
        BuySellTransaction opt = new BuySellTransaction(0, argmaxTail(prices, 1));
        int i = 1;
        /*
         * Loop invariant: opt references a `Transaction` among all those with `purchaseTime` in
         * `[0..i)` with the maximum achievable profit.
         */
        while (i < prices.length - 2) {
            BuySellTransaction j = new BuySellTransaction(i, argmaxTail(prices, i + 1));
            if (profit(prices, j) >= profit(prices, opt))
                opt = j;
            i++;
        }
        return opt;
    }

    /**
     * Returns a BuySellTransaction with the maximum achievable profit for the given `prices`
     * window.
     */
    static BuySellTransaction optimalTransaction2(int[] prices) {
        BuySellTransaction opt = new BuySellTransaction(0, 1);
        int min = 0;
        int i = 2;
        /*
         * Loop invariants: 1) opt references a 'Transaction' among all those with 'sellTime' in [1..i-1]
         * with the maximum achievable profit. 2) min is the index of the minimum price in prices[0..i-2].
         */
        while (i < prices.length) {
            if (prices[i - 1] < prices[min])
                min = i - 1;
            BuySellTransaction j = new BuySellTransaction(min, i);
            if (profit(prices, j) > profit(prices, opt))
                opt = j;
            i++;
        }
        return opt;
    }

    public static void main(String[] args) {
        int[] naturalVals = {100000, 200000, 300000, 400000, 500000, 600000, 700000, 800000, 900000, 1000000};
        Random random = new Random();
        int i = 0;
        while (i < naturalVals.length){
            int [] prices = new int[naturalVals[i]];
            for (int j = 0; j < prices.length; j++) {
                prices[j] = random.nextInt(300, 350);
            }
            long startTime1 = System.nanoTime();
            optimalTransaction1(prices);
            long endTime1 = System.nanoTime();
            long elapsedTimeMs1 = (endTime1-startTime1)/1000000;
            System.out.println(elapsedTimeMs1);

            long startTime2 = System.nanoTime();
            optimalTransaction2(prices);
            long endTime2 = System.nanoTime();
            long elapsedTimeMs2 = (endTime2-startTime2)/1000000;
            System.out.println(elapsedTimeMs2);

            i++;
        }

    }
}