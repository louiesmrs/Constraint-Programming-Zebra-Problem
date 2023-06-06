
package main.java;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
public class ConstraintDifferenceVarVar extends Constraint {
    
    Variable v1, v2;

    public ConstraintDifferenceVarVar(Variable v1, Variable v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    protected Constraint newVariable(Variable v) {
        return new ConstraintDifferenceVarVar(
                    (this.v1.hasThisName(v.name)) ? v : this.v1,
                    (this.v2.hasThisName(v.name)) ? v : this.v2);
    }

    public String toString() {
        String result = "";    
        result += this.v1 + " != " + this.v2 ;
        return result;
    }


    protected boolean isSatisfied() {
        return this.v1.d.vals.length == 1 && this.v2.d.vals.length == 1 && this.v1.d.vals[0] != this.v2.d.vals[0];
    }

    /**
     * checks if either domain has only 1 element.
     * If true creates a new ArrayList and removes only elements
     * of the domain that are common among both domains.
     * If the ArrayList has a length that is not equal to the original
     * domain ArrayList is converted to an Array and assigned to
     * the domain of the variable that did not have a length of 1 and returns true.
     * @return true if change in domain occurred false otherwise.
     */
    protected boolean reduce() {
        if (this.v1.d.vals.length == 1) {
            List<Integer> result = new ArrayList<Integer>();
            int valToRemove = this.v1.d.vals[0];
            for (int i : this.v2.d.vals) {
                if (i != valToRemove) {
                    result.add(i);
                }
            }
            boolean reduced = result.size() != this.v2.d.vals.length;
            this.v2.d.vals = result.stream().mapToInt(i -> i).toArray();
            return reduced;
        }

        if (this.v2.d.vals.length == 1) {
            List<Integer> result = new ArrayList<Integer>();
            int valToRemove = this.v2.d.vals[0];
            for (int i : this.v1.d.vals) {
                if (i != valToRemove) {
                    result.add(i);
                }
            }
            boolean reduced = result.size() != this.v1.d.vals.length;
            this.v1.d.vals = result.stream().mapToInt(i -> i).toArray();
            return reduced;
        }
        return false;
    }
    public Variable[] getVariables() {
        return new Variable[]{this.v1, this.v2};
    }

}
