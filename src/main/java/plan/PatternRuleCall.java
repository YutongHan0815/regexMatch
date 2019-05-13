package plan;

import com.google.common.collect.ImmutableMap;
import operators.Operator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



public class PatternRuleCall implements RuleCall, Serializable {

    private final RegexPlanner regexPlanner;
    private static int nextId = 0;
    public final int id;
    protected final PatternNode mainPattern;
    protected Map<PatternNode, List<PatternNode>> nodeListMap;
    public List<Operator> matchedOperators;
    //private final List<PatternNode> parents;


    private PatternRuleCall(RegexPlanner planner, PatternNode mainPattern, Map<PatternNode, List<PatternNode>> nodeListMap,
                            List<Operator> matchedOperators) {
        this.id = nextId++;
        this.mainPattern = mainPattern;
        this.nodeListMap = nodeListMap;
        this.matchedOperators = matchedOperators;
        //this.parents = parents;
        this.regexPlanner = planner;
    }
    public PatternRuleCall(RegexPlanner planner, PatternNode mainPattern) {
        this(planner, mainPattern, ImmutableMap.of(), new ArrayList<Operator>());
    }


    @Override
    public <T extends Operator> T getMatchedOperator(int ordinal) {
        return null;
    }

    @Override
    public void transformTo(Operator equivalentOperator) {


    }

    @Override
    public void transformTo(SetNode setNode) {

    }
}
