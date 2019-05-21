package plan.triat;

import com.google.common.collect.ImmutableList;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.stream.Collectors.toSet;

public class TraitSet implements Serializable {

    public static TraitSet of(Trait... traits) {
        return of(Arrays.asList(traits));
    }

    public static TraitSet of(List<Trait> traits) {
        return new TraitSet(traits);
    }

    private final ImmutableList<Trait> traits;

    private TraitSet(List<Trait> traits) {
        checkArgument(traits.stream().map(t -> t.getClass()).collect(toSet()).size() == traits.size());
        this.traits = ImmutableList.copyOf(traits);
    }

    public <T extends Trait> T get(TraitDef<T> traitDef) {
        for (Trait trait : traits) {
            if (trait.getClass().equals(traitDef.getTraitClass())) {
                //noinspection unchecked
                return (T) trait;
            }
        }
        throw new RuntimeException("trait " + traitDef.getTraitName() + " not found");
    }

    public List<Trait> getTraits() {
        return this.traits;
    }

    public TraitSet replace(Trait newTrait) {
        if (this.traits.contains(newTrait)) {
            return this;
        }
        ImmutableList.Builder<Trait> builder = ImmutableList.builder();
        this.traits.forEach(t -> {
            if (t.getClass() == newTrait.getClass()) {
                builder.add(newTrait);
            } else {
                builder.add(t);
            }
        });
        return of(builder.build());
    }

    public boolean satisfy(TraitSet other) {
        return this.traits.stream().allMatch(t -> t.satisfy(other.get(t.getTraitDef())));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TraitSet traitSet = (TraitSet) o;
        return Objects.equals(traits, traitSet.traits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(traits);
    }

    @Override
    public String toString() {
        return "TraitSet{" +
                "traits=" + traits +
                '}';
    }
}
