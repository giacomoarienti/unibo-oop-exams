package a04.e1;

import java.util.*;
import java.util.function.*;

public class BankAccountFactoryImpl implements BankAccountFactory {

    private final class BankAccountImpl implements BankAccount {
        private static final int INITIAL_BALANCE = 0;
        
        private int balance;
        private boolean blocked;

        private final Optional<UnaryOperator<Integer>> feeFunction;
        private final Optional<Predicate<Integer>> allowedCredit;
        private final Optional<UnaryOperator<Integer>> rateFunction;
        private final Optional<BiPredicate<Integer, Integer>> blockingPolicy;

        public BankAccountImpl(
            final UnaryOperator<Integer> feeFunction,
            final Predicate<Integer> allowedCredit,
            final UnaryOperator<Integer> rateFunction,
            final BiPredicate<Integer, Integer> blockingPolicy
        ) {
            this.feeFunction = Optional.ofNullable(feeFunction);
            this.allowedCredit = Optional.ofNullable(allowedCredit);
            this.rateFunction = Optional.ofNullable(rateFunction);
            this.blockingPolicy = Optional.ofNullable(blockingPolicy);
            /* init default values */
            this.balance = INITIAL_BALANCE;
            this.blocked = false;
        }

        public BankAccountImpl() {
            this(null, null, null, null);
        }

        public BankAccountImpl(
            final UnaryOperator<Integer> feeFunction
        ) {
            this(feeFunction, null, null, null);
        }

        public BankAccountImpl(
            final Predicate<Integer> allowedCredit,
            final UnaryOperator<Integer> rateFunction
        ) {
            this(null, allowedCredit, rateFunction, null);
        }

        public BankAccountImpl(
            final BiPredicate<Integer, Integer> blockingPolicy
        ) {
            this(null, null, null, blockingPolicy);
        }

        public BankAccountImpl(
            final UnaryOperator<Integer> feeFunction,
            final Predicate<Integer> allowedCredit,
            final UnaryOperator<Integer> rateFunction
        ) {
            this(feeFunction, allowedCredit, rateFunction, null);
        }

        @Override
        public int getBalance() {
            return balance;
        }

        @Override
        public void deposit(final int amount) {
            if(amount <= 0) {
                throw new IllegalArgumentException("invalid amount");
            }
            balance += amount;
        }

        private int calculateFeee(final int amount) {
            if(feeFunction.isPresent()) {
                return feeFunction.get().apply(amount);
            }
            return 0;
        }

        private boolean isCreditAllowed(final int amount) {
            return allowedCredit.isPresent() && 
                allowedCredit.get().test(
                    Math.abs(balance - amount)
                );
        }

        private int calculateRate(final int amount) {
            if(rateFunction.isPresent()) {
                return rateFunction.get().apply(amount);
            }
            return 0;
        }

        private boolean isBlocking(final int amount) {
            if(blocked) {
                return true;
            }
            if(blockingPolicy.isPresent()) {
                blocked = blockingPolicy.get().test(amount, balance);
                return blocked;
            }
            return false;
        }

        @Override
        public boolean withdraw(int amount) {
            if(amount <= 0) {
                throw new IllegalArgumentException("invalid amount");
            }
            amount += calculateFeee(amount);
            if(isBlocking(amount)) {
                return false;
            }
            /* check if total amount is withdrawable */
            if(balance < amount) {
                if(isCreditAllowed(amount)) {
                    amount += calculateRate(amount);
                } else {
                    return false;
                }
            }
            balance -= amount;
            return true;
        }
    }

    @Override
    public BankAccount createBasic() {
        return new BankAccountImpl();   
    }

    @Override
    public BankAccount createWithFee(final UnaryOperator<Integer> feeFunction) {
        return new BankAccountImpl(feeFunction);
    }

    @Override
    public BankAccount createWithCredit(Predicate<Integer> allowedCredit, UnaryOperator<Integer> rateFunction) {
        return new BankAccountImpl(allowedCredit, rateFunction);
    }

    @Override
    public BankAccount createWithBlock(BiPredicate<Integer, Integer> blockingPolicy) {
        return new BankAccountImpl(blockingPolicy);
    }

    @Override
    public BankAccount createWithFeeAndCredit(
        final UnaryOperator<Integer> feeFunction,
        final Predicate<Integer> allowedCredit,
        final UnaryOperator<Integer> rateFunction) {
        return new BankAccountImpl(feeFunction, allowedCredit, rateFunction);
    }

}
