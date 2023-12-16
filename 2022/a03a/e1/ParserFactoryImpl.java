package a03a.e1;

import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class ParserFactoryImpl implements ParserFactory {

    @Override
    public <X> Parser<X> fromFinitePossibilities(final Set<List<X>> acceptedSequences) {
        final List<List<X>> sequences = new ArrayList<>(acceptedSequences);
        return (it) -> {
            final Iterable<X> iterable = () -> it;
            final List<X> list = StreamSupport.stream(iterable.spliterator(), false).toList();
            return sequences.contains(list);
        };
    }

    @Override
    public <X> Parser<X> fromGraph(final X x0, final Set<Pair<X, X>> transitions, final Set<X> acceptanceInputs) {
        return fromParserWithInitial(
            x0,
            (it) -> {
                X prev = x0;
                while (it.hasNext()) {
                    final X el = it.next();
                    if (!transitions.contains(makePair(prev, el))) {
                        return false;
                    }
                    prev = el;
                }
                return acceptanceInputs.contains(prev);
            }
        );
    }

    @Override
    public <X> Parser<X> fromIteration(final X x0, final Function<X, Optional<X>> next) {
        return (it) -> {
            Optional<X> optional = Optional.of(x0);
            while (it.hasNext() && optional.isPresent()) {
                X number = optional.get();
                System.out.println(number);
                if(!it.next().equals(number)) {
                    System.out.println("exit");
                    return false;
                }
                optional = next.apply(number);
            }
            return !it.hasNext() && optional.isEmpty();
        };
    }

    @Override
    public <X> Parser<X> recursive(final Function<X, Optional<Parser<X>>> nextParser, final boolean isFinal) {
        return (it) -> {
            if (!it.hasNext()) {
                return isFinal;
            }
            final var opt = nextParser.apply(it.next());
            return opt.isPresent() ? opt.get().accept(it) : false;
        };
    }

    @Override
    public <X> Parser<X> fromParserWithInitial(final X x, final Parser<X> parser) {
        return (it) -> {
            if(it.hasNext() && it.next().equals(x)) {
                return parser.accept(it);
            }
            return false;            
        };
    }

    private <X> Pair<X,X> makePair(final X x, final X y) {
        return new Pair<X,X>(x, y);
    }

}
