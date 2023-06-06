package main.java;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;

public class ConstraintEqualityVarVar extends Constraint {
    
    Variable v1, v2;

    public ConstraintEqualityVarVar(Variable v1, Variable v2) {
        this.v1 = v1;
        this.v2 = v2;
    }

    protected Constraint newVariable(Variable v) {
        return new ConstraintEqualityVarVar(
                (this.v1.hasThisName(v.name)) ? v : this.v1,
                (this.v2.hasThisName(v.name)) ? v : this.v2);
    }


    public String toString() {
        String result = "";    
            result += this.v1 + " = " + this.v2 ;
        return result;
    }

        protected boolean isSatisfied() {
//
           return this.v1.d.vals.length == 1 && this.v2.d.vals.length == 1 && this.v1.d.vals[0] == this.v2.d.vals[0];
        }
        protected boolean isSatisfiable(){

            for(int i : this.v1.d.vals) {
                for(int j : this.v2.d.vals) {
                    if(i == j) {
                        return true;
                    }
                }
            }
            return false;
        }

    /**checks if both variable domains are not empty and that they are satisfiable
     * (i.e., if they have a domain in common). If true creates a new ArrayList and
     * adds only elements of the domain that are common among both domains.
     * At the end of the function the ArrayList is converted to an Array and
     * assigned to the domain of each variable.
     *
     * @return true if domains changed. false if else.
     */
    protected boolean reduce() {


        if (!this.v1.d.isEmpty() && !this.v2.d.isEmpty() && isSatisfiable()) {
            if (!Arrays.equals(this.v1.d.vals, this.v2.d.vals)) {
                List<Integer> result = new ArrayList<Integer>();
                for (int i : this.v1.d.vals) {
                    for (int j : this.v2.d.vals) {
                        if (i == j) {
                            result.add(i);

                        }
                    }
                }

                this.v1.d.vals = result.stream().mapToInt(i -> i).toArray();
                this.v2.d.vals =this.v1.d.vals;
                return true;

            }
            return false;
        }
        return false;

    }

    public Variable[] getVariables() {
        return new Variable[]{this.v1, this.v2};
    }

}
