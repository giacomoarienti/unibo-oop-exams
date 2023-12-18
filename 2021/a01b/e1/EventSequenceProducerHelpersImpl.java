package a01b.e1;

import java.util.*;
import java.util.function.*;

public class EventSequenceProducerHelpersImpl implements EventSequenceProducerHelpers {

    private <E> List<Pair<Double, E>> makeList(final EventSequenceProducer<E> sequence) {
        final List<Pair<Double, E>> list = new ArrayList<>();
        try {
            while(true) {
                list.add(
                    sequence.getNext()
                );
            }
        } catch(final Exception e) { }
        return list;
    }

    @Override
    public <E> EventSequenceProducer<E> fromIterator(final Iterator<Pair<Double, E>> iterator) {
        return () -> {
            if(iterator.hasNext()) {
                return iterator.next();
            }
            throw new NoSuchElementException();
        };
    }

    @Override
    public <E> List<E> window(
        final EventSequenceProducer<E> sequence,
        final double fromTime,
        final double toTime
    ) {
        
        return makeList(sequence)
            .stream()
            .filter(e -> e.get1() <= toTime && e.get1() >= fromTime)
            .map(e -> e.get2())
            .toList();
    }

    @Override
    public <E> Iterable<E> asEventContentIterable(final EventSequenceProducer<E> sequence) {
        return () -> {
            return new Iterator<E>() {
                
                private E el = null;

                @Override
                public boolean hasNext() {
                    try {
                        el = sequence.getNext().get2();
                    } catch(final NoSuchElementException e) {
                        return false;
                    }
                    return true;
                }

                @Override
                public E next() {
                    return el;
                }
                
            };
        };
    }

    @Override
    public <E> Optional<Pair<Double, E>> nextAt(final EventSequenceProducer<E> sequence, final double time) {
        final var list = makeList(sequence)
            .stream()
            .filter(e -> e.get1() >= time)
            .toList();
        return list.isEmpty() ? Optional.empty() : Optional.of(list.get(0));
    }

    @Override
    public <E> EventSequenceProducer<E> filter(final EventSequenceProducer<E> sequence, final Predicate<E> predicate) {
        return () -> {
            final var el = sequence.getNext();
            if(predicate.test(el.get2())) {
                return el;
            }
            throw new NoSuchElementException();
        };
    }

}
