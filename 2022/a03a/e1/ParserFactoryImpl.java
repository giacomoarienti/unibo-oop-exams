package a03a.e1;

import java.util.*;
import java.util.function.*;

public class ParserFactoryImpl implements ParserFactory {

    @Override
    public <X> Parser<X> fromFinitePossibilities(Set<List<X>> acceptedSequences) {
        return new Parser<X>() {
            private final List<List<X>> sequences = new ArrayList<>(acceptedSequences);

            @Override
            public boolean accept(final Iterator<X> iterator) {
                final List<Integer> validSequences = new ArrayList<>();
                for(int i = 0; i < sequences.size(); i++) {
                    validSequences.add(i);
                }
                int index = 0;
                while(iterator.hasNext() && !validSequences.isEmpty()) {
                    final var el = iterator.next();
                    /* iterate trought all list */
                    System.out.println(index);
                    final var it = validSequences.iterator();
                    while(it.hasNext()) {
                        final var list = this.sequences.get(it.next());
                        if(index < list.size()) {
                            if(!list.get(index).equals(el)) {
                                it.remove();
                            }
                        } else {
                            it.remove();
                        }
                    }
                    index++;
                }
                final var finalIndex = index;
                return !validSequences.stream()
                    .map(i -> sequences.get(i))
                    .filter(list -> list.size() == finalIndex)
                    .toList()
                    .isEmpty();
            }
            
        };
    }

    @Override
    public <X> Parser<X> fromGraph(X x0, Set<Pair<X, X>> transitions, Set<X> acceptanceInputs) {
        return new Parser<X>() {
            private final Set<Pair<X, X>> trans = transitions;
            private final Set<X> inputs = acceptanceInputs;
            private int X firstX = x0;

            @Override
            public boolean accept(final Iterator<X> iterator) {
                boolean second = false;
                if(!iterator.hasNext() || iterator.next() != x0) {
                    return false;
                }
                X lastEl = null;
                while(iterator.hasNext()) {
                    final X el = iterator.next();
                    System.out.println(el + " " + second);
                    final boolean localSec = second;
                    /* filter pairs */
                    if(
                        trans.stream()
                            .filter(pair -> {
                                final var e = localSec ? pair.getY() : pair.getX();
                                return e.equals(el);
                            })
                            .toList()
                            .isEmpty()
                    ) {
                        return inputs.contains(el);
                    }
                    second = !second;
                    lastEl = el;
                }
                System.out.println("AWDAWDAWD " + lastEl);
                return inputs.contains(lastEl);
            }
        };
    }

    @Override
    public <X> Parser<X> fromIteration(X x0, Function<X, Optional<X>> next) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromIteration'");
    }

    @Override
    public <X> Parser<X> recursive(Function<X, Optional<Parser<X>>> nextParser, boolean isFinal) {
        return new Parser<X>() {
            private Function<X, Optional<Parser<X>>> parser = nextParser;

            @Override
            public boolean accept(final Iterator<X> iterator) {
                if(!iterator.hasNext()) {
                    return isFinal;
                }
                /* use nextParser */
                final var el = iterator.next();
                final var newParser = parser.apply(el);
                if(newParser.isPresent()) {
                    return newParser.get().accept(iterator);
                }
                return false;
            }
            
        };
    }

    @Override
    public <X> Parser<X> fromParserWithInitial(X x, Parser<X> parser) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'fromParserWithInitial'");
    }

}
