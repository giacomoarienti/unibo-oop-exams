package a03a.e1;

import java.util.*;
import java.util.function.*;

public class DecisionChainFactoryImpl implements DecisionChainFactory {

    @Override
    public <A, B> DecisionChain<A, B> oneResult(final B b) {
        return new DecisionChain<A,B>() {
            @Override
            public Optional<B> result(final A a) {
                return Optional.of(b);
            }

            @Override
            public DecisionChain<A, B> next(A a) {
                return this;
            }
        };
    }
    
    @Override
    public <A, B> DecisionChain<A, B> simpleTwoWay(
        final Predicate<A> predicate,
        final B positive, 
        final B negative
    ) {
        return twoWay(predicate, oneResult(positive), oneResult(negative));
    }

    @Override
    public <A, B> DecisionChain<A, B> enumerationLike(final List<Pair<A, B>> mapList, final B defaultReply) {
        /* transform mapList to a decision chain */
        final List<Pair<Predicate<A>, B>> cases = new ArrayList<>();
        mapList.forEach((pair) -> {
            cases.add(
                new Pair<Predicate<A>,B>(
                    (a) -> a.equals(pair.get1()),
                    pair.get2()
                )
            );
        });
        return switchChain(cases, defaultReply);
    }

    @Override
    public <A, B> DecisionChain<A, B> twoWay(
        final Predicate<A> predicate,
        final DecisionChain<A, B> positive,
        final DecisionChain<A, B> negative
    ) {
        return new DecisionChain<A,B>() {
            @Override
            public Optional<B> result(A a) {
                return Optional.empty();
            }

            @Override
            public DecisionChain<A, B> next(A a) {
                return predicate.test(a) ? positive : negative;
            }
        };
    }

    @Override
    public <A, B> DecisionChain<A, B> switchChain(List<Pair<Predicate<A>, B>> cases, B defaultReply) {
       return new DecisionChain<A,B>() {
            @Override
            public Optional<B> result(final A a) {
                if(cases.size() > 0) {
                    final var pair = cases.get(0);
                    if(pair.get1().test(a)) {
                        return Optional.of(pair.get2());
                    }
                    return Optional.empty();
                }
                return Optional.of(defaultReply);
            }

            @Override
            public DecisionChain<A, B> next(final A a) {
                if(result(a).isEmpty()) {
                    final var newMap = new ArrayList<>(cases);
                    newMap.remove(0);
                    return switchChain(newMap, defaultReply);
                }
                return this;
            }
       };
    }

}
