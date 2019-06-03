package edu.ics.uci.regex;

import edu.ics.uci.optimizer.OptimizerPlanner;
import edu.ics.uci.optimizer.operator.SubsetNode;
import edu.ics.uci.optimizer.rule.RuleSet;
import edu.ics.uci.regex.regexMatcher.RegexParser;

import static edu.ics.uci.optimizer.triat.ConventionDef.CONVENTION_TRAIT_DEF;

public class OptimizerPlanTest {


    public static void main(String[] args) {

        String regex = "[0-9]+(?:st|nd|rd|th)";
        RegexParser regexParser = new RegexParser(regex);
        SubsetNode root = regexParser.parser();

        OptimizerPlanner optimizerPlanner = OptimizerPlanner.create();
        optimizerPlanner.addTraitDef(CONVENTION_TRAIT_DEF);

        RuleSet.DEFAULT_RULES.forEach(rule -> optimizerPlanner.addRule(rule));
        optimizerPlanner.setRoot(root);
        //optimizerPlanner.optimize();
        System.out.println(optimizerPlanner.getAndOrTree().getOperators().size());
        System.out.println(optimizerPlanner.getAndOrTree().getSets().size());

    }
}
