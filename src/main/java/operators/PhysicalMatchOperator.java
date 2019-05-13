package operators;



import com.google.common.base.Preconditions;

import java.io.Serializable;
import java.util.Objects;


public class PhysicalMatchOperator implements Operator, Serializable {
    private final String subRegex;
    // match left to right / match reverse regex
    private boolean matchOptOr;

    public PhysicalMatchOperator(String mainRegex) {
        Preconditions.checkNotNull(mainRegex);

        this.subRegex = mainRegex;
   }

    public String getSubRegex() {
        return subRegex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PhysicalMatchOperator that = (PhysicalMatchOperator) o;
        return subRegex.equals(that.subRegex);
    }


    @Override
    public int hashCode() {
        return Objects.hash(getDigest());
    }

    public String getDigest() {
        return "PhysicalMatchOperator(subRegex=" + subRegex + ")";
    }


}
