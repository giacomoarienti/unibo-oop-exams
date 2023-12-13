package a03a.e1;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class ParserFactoryImpl implements ParserFactory {

    @Override
    public <X> Parser<X> fromFinitePossibilities(final Set<List<X>> acceptedSequences) {
        return new Parser<X>() {
            private final List<List<X>> sequences = new ArrayList<>(acceptedSequences);

            @Override
            public boolean accept(final Iterator<X> iterator) {
                final Iterable<X> iterable = () -> iterator;
                final List<X> list = StreamSupport.stream(iterable.spliterator(), false).toList();
                return sequences.contains(list);
            }

        };
    }

    @Override
    public <X> Parser<X> fromGraph(final X x0, final Set<Pair<X, X>> transitions, final Set<X> acceptanceInputs) {
        return fromParserWithInitial(
            x0,
            new Parser<X>() {
                private final Set<Pair<X, X>> trans = transitions;
                private final Set<X> inputs = acceptanceInputs;

                @Override
                public boolean accept(final Iterator<X> iterator) {
                    X prev = x0;
                    while (iterator.hasNext()) {
                        final X el = iterator.next();
                        if (!trans.contains(makePair(prev, el))) {
                            return false;
                        }
                        prev = el;
                    }
                    return inputs.contains(prev);
                }
            }
        );
    }

    @Override
    public <X> Parser<X> fromIteration(final X x0, final Function<X, Optional<X>> next) {
        return new Parser<X>() {
            private X number = x0;
            private final Function<X, Optional<X>> getNext = next;

            @Override
            public boolean accept(final Iterator<X> iterator) {
                Optional<X> optional = Optional.of(number);
                while (iterator.hasNext() && optional.isPresent()) {
                    number = optional.get();
                    if(!iterator.next().equals(number)) {
                        return false;
                    }
                    optional = getNext.apply(number);
                }
                return true;
            }
            
        };
    }

    @Override
    public <X> Parser<X> recursive(final Function<X, Optional<Parser<X>>> nextParser, final boolean isFinal) {
        return new Parser<X>() {
            private Function<X, Optional<Parser<X>>> parser = nextParser;

            @Override
            public boolean accept(final Iterator<X> iterator) {
                if (!iterator.hasNext()) {
                    return isFinal;
                }
                final var opt = parser.apply(iterator.next());
                return opt.isPresent() ? opt.get().accept(iterator) : false;
            }

        };
    }

    @Override
    public <X> Parser<X> fromParserWithInitial(final X x, final Parser<X> parser) {
        return new Parser<X>() {
            private X first = x;

            @Override
            public boolean accept(final Iterator<X> iterator) {
                if(iterator.hasNext() && iterator.next().equals(first)) {
                    return parser.accept(iterator);
                }
                return false;
            }
            
        };
    }

    private <X> Pair<X,X> makePair(final X x, final X y) {
        return new Pair<X,X>(x, y);
    }

}
