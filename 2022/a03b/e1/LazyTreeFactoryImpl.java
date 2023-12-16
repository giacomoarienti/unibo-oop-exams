package a03b.e1;

import java.util.*;
import java.util.function.*;

public class LazyTreeFactoryImpl implements LazyTreeFactory {

    @Override
    public <X> LazyTree<X> constantInfinite(final X value) {
        return new LazyTreeImpl<X>(
            value,
            (t) -> t.root(),
            (t) -> t.root()
        );
    }

    private <X> Optional<Pair<X, X>> getPair(final LazyTree<X> tree, final Map<X, Pair<X, X>> map) {
        if(tree.hasRoot() && map.containsKey(tree.root())) {
            return Optional.of(map.get(tree.root()));
        }
        return Optional.empty();
    }

    @Override
    public <X> LazyTree<X> fromMap(final X value, final Map<X, Pair<X, X>> map) {
        return new LazyTreeImpl<X>(
            value,
            (t) -> getPair(t, map).isPresent() ? getPair(t, map).get().getX() : null,
            (t) -> getPair(t, map).isPresent() ? getPair(t, map).get().getY() : null
        );
    }

    @Override
    public <X> LazyTree<X> cons(
        final Optional<X> value, 
        final Supplier<LazyTree<X>> leftSupp, 
        final Supplier<LazyTree<X>> rightSupp
    ) {
        return new LazyTreeConsImpl<X>(
            value.orElse(null),
            (t) -> leftSupp.get(),
            (t) -> rightSupp.get()
        );
    }

    private class LazyTreeConsImpl<X> implements LazyTree<X> {
        private final Optional<X> root;
        private final UnaryOperator<LazyTree<X>> left;
        private final UnaryOperator<LazyTree<X>> right;
        
        public LazyTreeConsImpl(
            final X value,
            final UnaryOperator<LazyTree<X>> left,
            final UnaryOperator<LazyTree<X>> right
        ) {
            this.root = Optional.ofNullable(value);
            this.left = left;
            this.right = right;
        }

        @Override
        public boolean hasRoot() {
            return root.isPresent();
        }

        @Override
        public X root() {
            return root.get();
        }

        @Override
        public LazyTree<X> left() {
            return left.apply(this);
        }

        @Override
        public LazyTree<X> right() {
            return right.apply(this);
        }
    }

    @Override
    public <X> LazyTree<X> fromTwoIterations(
        final X value, 
        final UnaryOperator<X> leftOp, 
        final UnaryOperator<X> rightOp
    ) {
        return new LazyTreeImpl<X>(
            value,
            (t) -> leftOp.apply(t.root()),
            (t) -> rightOp.apply(t.root())
        );
    }
    
    @Override
    public <X> LazyTree<X> fromTreeWithBound(final LazyTree<X> tree, final int bound) {
        return new LazyTreeWithBoundImpl<>(tree, bound);
    }

    private class LazyTreeWithBoundImpl<X> implements LazyTree<X> {
        final int max;
        final LazyTree<X> tree;

        public LazyTreeWithBoundImpl(final LazyTree<X> tree, final int bound) {
            this.tree = tree;
            this.max = bound;
        }

        @Override
        public boolean hasRoot() {
            return max > 0 && tree.hasRoot();
        }

        @Override
        public X root() {
            if(hasRoot()) {
                return tree.root();
            }
            return null;
        }

        @Override
        public LazyTree<X> left() {
            if(this.max > 0) {
                return new LazyTreeWithBoundImpl<>(tree.left(), max-1);
            }
            return null;
        }

        @Override
        public LazyTree<X> right() {
            if(this.max > 0) {
                return new LazyTreeWithBoundImpl<>(tree.right(), max-1);
            }
            return null;
        }
        
    }

    private class LazyTreeImpl<X> implements LazyTree<X> {
        private final Optional<X> root;
        private final Function<LazyTree<X>, X> left;
        private final Function<LazyTree<X>, X> right;
        
        public LazyTreeImpl(
            final X value,
            final Function<LazyTree<X>, X> left,
            final Function<LazyTree<X>, X> right
        ) {
            this.root = Optional.ofNullable(value);
            this.left = left;
            this.right = right;
        }

        @Override
        public boolean hasRoot() {
            return root.isPresent();
        }

        @Override
        public X root() {
            return root.get();
        }

        @Override
        public LazyTree<X> left() {
            return new LazyTreeImpl<X>(left.apply(this), left, right);
        }

        @Override
        public LazyTree<X> right() {
            return new LazyTreeImpl<X>(right.apply(this), left, right);
        }
    }
}
