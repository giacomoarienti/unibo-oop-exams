package a01a.e1;

import java.util.*;
import java.util.function.*;

public class AcceptorFactoryAdvancedImpl implements AcceptorFactory {

    @Override
    public Acceptor<String, Integer> countEmptyStringsOnAnySequence() {
        return generalised(
            0,
            (e, s) -> !e.isEmpty() ? Optional.of(++s) : Optional.of(s),
            (s) -> Optional.of(s)
        );
    }

    @Override
    public Acceptor<Integer, String> showAsStringOnlyOnIncreasingSequences() {
        return new Acceptor<Integer,String>() {

            private Integer last = null;
            private String comulate = "";

            @Override
            public boolean accept(final Integer e) {
                if(!Objects.isNull(last) && last > e) {
                    comulate = "";
                    return false;
                }
                last = e;
                if(!comulate.isEmpty()) {
                    comulate += ":";
                }
                comulate += String.valueOf(e);
                return true;
            }

            @Override
            public Optional<String> end() {
                if(comulate.isEmpty()) {
                    return Optional.empty();
                }
                return Optional.of(comulate);
            }
            
        };
    }

    @Override
    public Acceptor<Integer, Integer> sumElementsOnlyInTriples() {
        return generalised(
            new ArrayList<Integer>(),
            (e, s) -> Optional.of(s),
            (s) -> s.size() != 3 ? 
                Optional.empty() : 
                Optional.of(
                    s.stream()
                        .mapToInt(Integer::intValue)
                        .sum()
                ) 
        );
    }

    @Override
    public <E, O1, O2> Acceptor<E, Pair<O1, O2>> acceptBoth(final Acceptor<E, O1> a1, final Acceptor<E, O2> a2) {
        return new Acceptor<E,Pair<O1,O2>>() {

            @Override
            public boolean accept(final E e) {
                return a1.accept(e) & a2.accept(e);
            }

            @Override
            public Optional<Pair<O1, O2>> end() {
                final var res1 = a1.end();
                final var res2 = a2.end();
                if(res1.isEmpty() || res2.isEmpty()) {
                    return Optional.empty();
                }
                return Optional.of(new Pair<>(res1.get(), res2.get()));
            }
            
        };
    }

    @Override
    public <E, O, S> Acceptor<E, O> generalised(
        final S initial,
        final BiFunction<E, S, Optional<S>> stateFun,
        final Function<S, Optional<O>> outputFun) {
        return new Acceptor<E,O>() {

            private S state = initial; 

            @Override
            public boolean accept(final E e) {
                final var newState = stateFun.apply(e, state);
                if(newState.isPresent()) {
                    state = newState.get();
                    return true;
                }
                return false;
            }

            @Override
            public Optional<O> end() {
                return outputFun.apply(state);
            }
            
        };
    }

}
