package a02b.e1;

import java.util.*;
import java.util.function.*;

public class CursorHelpersImpl implements CursorHelpers {

    @Override
    public <X> Cursor<X> fromNonEmptyList(final List<X> list) {
        return new CursorImpl<X>(
            (index) -> list.get(index),
            (index) -> index < list.size() - 1
        );
    }

    @Override
    public Cursor<Integer> naturals() {
        return new CursorImpl<Integer>(
            (index) -> index,
            (index) -> true
        );
    }

    @Override
    public <X> Cursor<X> take(final Cursor<X> input, final int max) {
        return new CursorImpl<X>(
            (index) -> input.getElement(),
            (index) -> index < max - 1 && input.advance()
        );
    }

    @Override
    public <X> void forEach(final Cursor<X> input, final Consumer<X> consumer) {
        consumer.accept(input.getElement());
        while(input.advance()) {
            consumer.accept(input.getElement());
        }
    }

    @Override
    public <X> List<X> toList(final Cursor<X> input, final int max) {
        final List<X> list = new ArrayList<>();
        final var cursor = this.take(input, max);
        this.forEach(cursor, e -> list.add(e));
        return list;
    }

    private class CursorImpl<X> implements Cursor<X> {
        private final Function<Integer, X> getElement;
        private final Predicate<Integer> advance;
        private int index = 0;

        public CursorImpl(final Function<Integer, X> getElement, final Predicate<Integer> advance) {
            this.getElement = getElement;
            this.advance = advance;
        }

        @Override
        public X getElement() {
            return this.getElement.apply(index);
        }

        @Override
        public boolean advance() {
            if(this.advance.test(index)) {
                index++;
                return true;
            }
            return false;
        }
        
    };
}
