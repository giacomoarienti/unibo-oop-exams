package a01a.e1;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SubsequenceCombinerFactoryImpl implements SubsequenceCombinerFactory {

    @Override
    public SubsequenceCombiner<Integer, Integer> tripletsToSum() {
        return (final List<Integer> list) -> {
            return this.splitListInTriplets(list).stream().map(
                    (final List<Integer> l) -> l.stream().reduce(Integer::sum).get()).toList();
        };
    }

    @Override
    public <X> SubsequenceCombiner<X, List<X>> tripletsToList() {
        return (final List<X> list) -> {
            return this.splitListInTriplets(list);
        };
    }

    @Override
    public SubsequenceCombiner<Integer, Integer> countUntilZero() {
        return (final List<Integer> list) -> {
            final List<Integer> countedItems = new ArrayList<>();
            int lastZero = 0;
            for (int i = 0; i < list.size(); i++) {
                final int el = list.get(i);
                /* if elements is zero or at the end of list */
                if (el == 0 || i == list.size() - 1) {
                    /* if end of list */
                    if (el != 0) {
                        i++;
                    }
                    countedItems.add(i - lastZero);
                    lastZero = i + 1;
                }
            }
            return countedItems;
        };
    }

    @Override
    public <X, Y> SubsequenceCombiner<X, Y> singleReplacer(Function<X, Y> function) {
        return (final List<X> list) -> {
            return list.stream().map(function).toList();
        };
    }

    @Override
    public SubsequenceCombiner<Integer, List<Integer>> cumulateToList(int threshold) {
        return (final List<Integer> list) -> {
            final List<List<Integer>> cumulatedList = new ArrayList<>();
            int currentSum = 0;
            int lastIndex = 0;
            for (int i = 0; i < list.size(); i++) {
                final int el = list.get(i);
                currentSum += el;
                if (currentSum >= threshold || i == list.size() - 1) {
                    cumulatedList.add(list.subList(lastIndex, i + 1));
                    lastIndex = i + 1;
                    currentSum = 0;
                }
            }
            return cumulatedList;
        };
    }

    private <X> List<List<X>> splitListInTriplets(final List<X> inputList) {
        final List<List<X>> newList = new ArrayList<>();
        List<X> currList = new ArrayList<>();
        /* iterate trought list */
        int i = 0;
        for (; i < inputList.size(); i++) {
            final X el = inputList.get(i);
            currList.add(el);
            /* every 3 elements or at list end */
            if ((i + 1) % 3 == 0 || i == inputList.size() - 1) {
                /* is the current list is not null, add to the return list */
                newList.add(currList);
                currList = new ArrayList<>();
            }
        }
        return newList;
    }
}
