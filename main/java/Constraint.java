package main.java;

public abstract class Constraint {

    public String toString() {

        return "";
    }

    protected boolean isSatisfied() {

        return true;
    }


    protected boolean reduce() {
        return true;
    }

    protected Constraint newVariable(Variable v) {
        return this;
    }

    protected Variable[] getVariables() {
        return new Variable[]{};
    }
}
