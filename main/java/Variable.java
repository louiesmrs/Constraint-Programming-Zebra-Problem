package main.java;

import java.util.Arrays;

public class Variable implements Comparable<Variable> {

    String name;
    Domain d;

    public Variable(String name, Domain d) {
        this.name = name;
        this.d = new Domain(d);
    }

    public String toString() {
        return this.name + d;
    }

    public String toStringSol() { return this.name + "-" + Arrays.toString(d.vals)
            .replace("[", "").replace("]", "");}


    @Override
    public int compareTo(Variable v1) {
        return Integer.compare(this.d.vals.length, v1.d.vals.length);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Variable)) { // check if the object is an instance of the class
            return false;
        }

        Variable other = (Variable) obj; // cast the object to the class

        return this.name.equals(other.name); // compare the string fields for equality
    }

    public boolean hasThisName(String s) {
        if (this.name.equals(s))
            return true;
        return false;
    }


}