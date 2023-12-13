package a01b.e1;

import java.util.*;
import java.util.stream.*;
import java.util.function.*;

public class FlattenerFactoryImpl implements FlattenerFactory {

    private class FlattenerImpl<X, Y> implements Flattener<X, Y> {
        private final Function<Stream<List<X>>, List<Y>> func;

        public FlattenerImpl(final Function<Stream<List<X>>, List<Y>> func) {
            this.func = func;
        }

        @Override
        public List<Y> flatten(final List<List<X>> list) {
            return func.apply(list.stream());
        }

    }

    @Override
    public Flattener<Integer, Integer> sumEach() {
        return new FlattenerImpl<>(
            (stream) -> stream.map((innerList) -> innerList.stream().reduce(Integer::sum))
                .map(value -> value.isPresent() ? value.get() : 0)
                .toList()
        );
    }

    @Override
    public <X> Flattener<X, X> flattenAll() {
        return new Flattener<X,X>() {
            @Override
            public List<X> flatten(final List<List<X>> list) {
               return list.stream().flatMap((innerList) -> innerList.stream()).toList();
            }
        };
    }

    @Override
    public Flattener<String, String> concatPairs() {
        return new Flattener<String,String>() {

            @Override
            public List<String> flatten(final List<List<String>> list) {
                return list.stream()
                .collect(
                    Collectors.groupingBy((innerList) -> list.indexOf(innerList) / 2)
                ).values()
                .stream()
                .map(
                    entry -> entry.stream()
                        .map(
                            value -> value.stream().collect(Collectors.joining()) 
                        )
                        .collect(Collectors.joining()) 
                ).toList();
            }
            
        };
    }

    @Override
    public <I, O> Flattener<I, O> each(Function<List<I>, O> mapper) {
        return new Flattener<I,O>() {
            @Override
            public List<O> flatten(final List<List<I>> list) {
                return list.stream()
                    .map(mapper::apply)
                    .toList();
            }
        };
    }

    @Override
    public Flattener<Integer, Integer> sumVectors() {
        return new Flattener<Integer,Integer>() {

            @Override
            public List<Integer> flatten(final List<List<Integer>> list) {
                return list.stream().reduce(
                    new ArrayList<>(List.of(0, 0, 0)),
                    (result, element) -> FlattenerFactoryImpl.sumVectors(result, element)
                );
            }
            
        };
    }

    private static List<Integer> sumVectors(final List<Integer> vec1, final List<Integer> vec2) {
        final var result = Arrays.asList(new Integer[vec1.size()]);
        for(int i = 0; i < vec1.size(); i++) {
            result.set(i, vec1.get(i) + vec2.get(i));
        }
        return result;
    }

}
