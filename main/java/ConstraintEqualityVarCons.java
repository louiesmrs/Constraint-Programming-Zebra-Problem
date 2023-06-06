package main.java;
import java.util.Arrays;
public class ConstraintEqualityVarCons extends Constraint {
    
    Variable v1;
    int v2;

    public ConstraintEqualityVarCons(Variable v1, int v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    protected Constraint newVariable(Variable v) {
        return new ConstraintEqualityVarCons((this.v1.hasThisName(v.name)) ? v : this.v1 , v2);
    }


    public String toString() {
        String result = "";    
        result += this.v1 + " = " + this.v2 ;
        return result;
    }

    protected boolean isSatisfied() {
        for(int i : this.v1.d.vals ) {
            if(i == this.v2) {
                return true;
            }
        }
        return false;
    }

    /** checks if variable domain is
     * checks if both variable domains are not empty and
     * that they are satisfiable (i.e., if they have a domain in common).
     * If true the domain of the variable is given a new int Array with only the constant and returns true.
     *
     * @return true if change in domain, false otherwise
     */
    protected boolean reduce() {
        if (!this.v1.d.isEmpty() && this.isSatisfied() ) {
            if (!Arrays.equals(this.v1.d.vals, new int[]{this.v2})) {
                this.v1.d.vals = new int[]{this.v2};
                return true;
            }
            return false;
        }
        return false;
    }
    public Variable[] getVariables() {
        return new Variable[]{this.v1};
    }

}
