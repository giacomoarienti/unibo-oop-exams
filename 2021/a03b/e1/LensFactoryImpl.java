package a03b.e1;

import java.util.*;
import java.util.function.*;

public class LensFactoryImpl implements LensFactory {

    private <E> List<E> copyAndSet(final int i, final E a, final List<E> s) {
        final var newList = new ArrayList<>(s);
        newList.set(i, a);
        return newList;
    }

    @Override
    public <E> Lens<List<E>, E> indexer(final int i) {
        return generic(
            (s) -> s.get(i),
            (a, s) -> copyAndSet(i, a, s)  
        );
    }

    @Override
    public <E> Lens<List<List<E>>, E> doubleIndexer(final int i, final int j) {
        return compose(indexer(i), indexer(j));
    }

    @Override
    public <A, B> Lens<Pair<A, B>, A> left() {
        return generic(
            (s) -> s.get1(),
            (a, s) -> new Pair<>(a, s.get2())
        );
    }

    @Override
    public <A, B> Lens<Pair<A, B>, B> right() {
        return generic(
            (s) -> s.get2(),
            (a, s) -> new Pair<>(s.get1(), a)
        );    
    }

    @Override
    public <A, B, C> Lens<List<Pair<A, Pair<B, C>>>, C> rightRightAtPos(final int i) {
        return compose(indexer(i), compose(right(), right()));
    }

    private <I, O> Lens<I, O> generic(
        final Function<I, O> getter,
        final BiFunction<O, I, I> setter
    ) {
        return new Lens<I,O>() {

            @Override
            public O get(final I s) {
                return getter.apply(s);
            }

            @Override
            public I set(final O a, final I s) {
                return setter.apply(a, s);
            }
            
        };
    }

    private <S, A, B> Lens<S, B> compose(final Lens<S, A> l1, final Lens<A, B> l2) {
        return new Lens<S,B>() {

            @Override
            public B get(final S s) {
                return l2.get(l1.get(s));
            }

            @Override
            public S set(B a, S s) {
                return l1.set(l2.set(a, l1.get(s)), s);
            }
            
        };
	}

}
