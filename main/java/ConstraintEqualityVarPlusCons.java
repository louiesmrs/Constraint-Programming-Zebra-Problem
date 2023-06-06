package main.java;

public class ConstraintEqualityVarPlusCons extends Constraint {
    
    Variable v1, v2;
    int cons;
    Boolean abs;

    public ConstraintEqualityVarPlusCons(Variable v1, Variable v2, int cons, Boolean abs){
        this.v1 = v1;
        this.v2 = v2;
        this.cons = cons;
        this.abs = abs;
    }

    protected Constraint newVariable(Variable v) {
        return new ConstraintEqualityVarPlusCons(
                (this.v1.hasThisName(v.name)) ? v : this.v1,
                (this.v2.hasThisName(v.name)) ? v : this.v2, cons, abs);
    }

    public String toString() {
        String result = "";
        if(!abs) {
            result += this.v1 + " = " + this.v2 + " + " + this.cons;
        } else {
            result += this.v1 + " - " + this.v2 + " = " + this.cons;
        }
        return result;

    }
    protected boolean isSatisfied() {
        if(!abs) {
            if (this.v1.d.vals.length == 1 && this.v2.d.vals.length == 1) {
                return this.v1.d.vals[0] == (this.v2.d.vals[0] + 1);
            }
        } else {
            if (this.v1.d.vals.length == 1 && this.v2.d.vals.length == 1) {
                return Math.abs(this.v1.d.vals[0] - this.v2.d.vals[0]) == 1;
            }
        }
        return false;

    }

    protected boolean isSatisfiable() {
        if(!abs) {
            for (int i : this.v1.d.vals) {
                for (int j : this.v2.d.vals) {
                    if (i == (j + this.cons)) {
                        return true;
                    }
                }
            }
        } else {
            for (int i : this.v1.d.vals) {
                for (int j : this.v2.d.vals) {
                    if (Math.abs(i-j) == this.cons) {
                        return true;
                    }
                }
            }
        }
         return false;
    }

    /**checks if both variable domains are not empty and that they are satisfiable
     * (i.e., there are elements in the domains that satisfy the given equation,
     * either the constraint is absolute or not). If true, keep elements in the domains
     * that are satisfy the given equation both domains. At the end of the returns true
     * if a change in domains has occurred.
     *
     * @return true if change in domain occurred, false otherwise
     */
    protected boolean reduce() {

        if (!this.v1.d.isEmpty() && !this.v2.d.isEmpty() && this.isSatisfiable()) {
            for (int i = 0; i < this.v1.d.vals.length; i++) {
                Boolean flag = false;
                if (!abs) {
                    for (int j = 0; j < this.v2.d.vals.length; j++) {
                        if (this.v1.d.vals[i] == this.v2.d.vals[j] + this.cons)
                            flag = true;
                    }
                    if (!flag) {
                        v1.d.delete(i);
                        return true;

                    }
                } else {
                    if (this.v2.d.vals.length == 1) {
                        if (Math.abs(this.v1.d.vals[i] - this.v2.d.vals[0]) == this.cons && this.v1.d.vals.length != 1) {
                            this.v1.d.vals = new int[]{this.v1.d.vals[i]};
                            return true;
                        }
                    }

                }
            }

            //from d2
            for (int i = 0; i < this.v2.d.vals.length; i++) {
                Boolean flag = false;
                if (!abs) {
                    for (int j = 0; j < this.v1.d.vals.length; j++) {
                        if (this.v2.d.vals[i] == this.v1.d.vals[j] - this.cons)
                            flag = true;
                    }
                    if (!flag) {
                        v2.d.delete(i);
                        return true;
                    }
                } else {
                    if (this.v1.d.vals.length == 1) {
                        if (Math.abs(this.v1.d.vals[0] - this.v2.d.vals[i]) == this.cons && this.v2.d.vals.length != 1) {
                            this.v2.d.vals = new int[]{this.v2.d.vals[i]};
                            return true;
                        }
                    }

                }
            }
            return false;
        }
        return false;
    }
    public Variable[] getVariables() {
        return new Variable[]{this.v1, this.v2};
    }

}
