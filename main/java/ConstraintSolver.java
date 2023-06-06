package main.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class ConstraintSolver {

    private Domain dom;
    private List<Variable> variableSet;
    private List<Constraint> constraintSet;


    public ConstraintSolver() {
        this.variableSet = new ArrayList<Variable>();
        this.constraintSet = new ArrayList<Constraint>();
    }
    public ConstraintSolver(List<Variable> vSet , List<Constraint> cSet) {
        this.variableSet = vSet;
        this.constraintSet = cSet;
    }

    public String toString() {
        //print variable
        for(int i = 0; i < variableSet.size(); i++)
            System.out.println(variableSet.get(i));
        System.out.println("");
        //print constraints
        for(int i = 0; i < constraintSet.size(); i++)
            System.out.println(constraintSet.get(i));
        return "";
    }

    private void parse(String fileName) {
        try {
            File inputFile = new File(fileName);
            Scanner scanner = new Scanner(inputFile);
            
            while (scanner.hasNextLine()) {
                String currentLine = scanner.nextLine();

                if(currentLine.startsWith("Domain-")) {
                    //this is our domain - i.e. a datastructure that contains values and can be updated, played with etc.
                    String s = currentLine.replace("Domain-","");
                    String[] array = s.split(","); 
                    int[] vals = new int[array.length];
                    for(int i = 0; i < array.length; i++) {
                        vals[i] = Integer.parseInt(array[i]);
                    }
                    dom = new Domain(vals);
                } else if (currentLine.startsWith("Var-")) {
                    //this is the code for every variable (a name and a domain)
                    String regexPattern = "\\(|\\)|\\ ";
                    String s = currentLine.replace("Var-","").replaceAll(regexPattern, "");
                    Variable var = new Variable(s, dom); 
                    variableSet.add(var);
                } else if (currentLine.startsWith("Cons-")) {
                    //this is the code for the constraints
                    //ConstraintEqualityVarPlusCons:
                    if (currentLine.startsWith("Cons-eqVPC")) {
                        String regexPattern = "\\(|\\)|\\ ";
                        String s = currentLine.replace("Cons-eqVPC(","").replaceAll(regexPattern, "");
                        String[] values = s.split("="); 
                        String[] values2 = values[1].split("\\+");
                        String val1Name = values[0];
                        String val2Name = values2[0];
                        Variable v1 = null;
                        Variable v2 = null;
                        for (Variable element : variableSet) {
                            if (element.hasThisName(val1Name)) {
                                v1 = element;
                            } else if(element.hasThisName(val2Name)) {
                                v2 = element; 
                            }
                        }
                        ConstraintEqualityVarPlusCons eq = new ConstraintEqualityVarPlusCons(v1, v2, Integer.parseInt(values2[1]), false);
                        constraintSet.add(eq);
                    } else if(currentLine.startsWith("Cons-eqVC")) {
                        String regexPattern = "\\(|\\)|\\ ";
                        String s = currentLine.replace("Cons-eqVC(","").replaceAll(regexPattern, "");
                        String[] values = s.split("="); 
                        String val1Name = values[0];
                        Variable v1 = null;
                        for (Variable element : variableSet) {
                            if (element.hasThisName(val1Name)) {
                                v1 = element;
                            }
                        }
                        ConstraintEqualityVarCons eq = new ConstraintEqualityVarCons(v1, Integer.parseInt(values[1]));
                        constraintSet.add(eq);

                    } else if(currentLine.startsWith("Cons-eqVV")) {
                        String regexPattern = "\\(|\\)|\\ ";
                        String s = currentLine.replace("Cons-eqVV(","").replaceAll(regexPattern, "");
                        String[] values = s.split("="); 
                        String val1Name = values[0];
                        String val2Name = values[1];
                        Variable v1 = null;
                        Variable v2 = null;
                        for (Variable element : variableSet) {
                            if (element.hasThisName(val1Name)) {
                                v1 = element;
                            } else if(element.hasThisName(val2Name)) {
                                v2 = element; 
                            }
                        }
                        ConstraintEqualityVarVar eq = new ConstraintEqualityVarVar(v1, v2);
                        constraintSet.add(eq);
                    } else if(currentLine.startsWith("Cons-abs")) {
                        String regexPattern = "\\(|\\)|\\ ";
                        String s = currentLine.replace("Cons-abs(","").replaceAll(regexPattern, "");
                        String[] values = s.split("="); 
                        String[] values2 = values[0].split("-");
                        String val1Name = values2[0];
                        String val2Name = values2[1];
                        Variable v1 = null;
                        Variable v2 = null;
                        for (Variable element : variableSet) {
                            if (element.hasThisName(val1Name)) {
                                v1 = element;
                            } else if(element.hasThisName(val2Name)) {
                                v2 = element; 
                            }
                        }
                        ConstraintEqualityVarPlusCons eq = new ConstraintEqualityVarPlusCons(v1, v2, Integer.parseInt(values[1]), true);
                        constraintSet.add(eq);
                    } else {
                        String regexPattern = "\\(|\\)|\\ ";
                        String s = currentLine.replace("Cons-diff(","").replaceAll(regexPattern, "");
                        String[] values = s.split(","); 
                        String val1Name = values[0];
                        String val2Name = values[1];
                        Variable v1 = null;
                        Variable v2 = null;
                        for (Variable element : variableSet) {
                            if (element.hasThisName(val1Name)) {
                                v1 = element;
                            } else if(element.hasThisName(val2Name)) {
                                v2 = element; 
                            }
                        }
                        ConstraintDifferenceVarVar eq = new ConstraintDifferenceVarVar(v1, v2);
                        constraintSet.add(eq);
                    }
                    
                }

            }

            scanner.close();
            
        } catch (FileNotFoundException e) {
            System.out.println("Error.");
            e.printStackTrace();
        }
    }

    /**
     * Iterates through all constraints.
     * If any return true change variableSet to match the new domains
     * of the variable from the constraint reduced and return true.
     * @return true if any constraint reduced, false otherwise
     */
    public boolean reduce() {
        boolean flag= false;
        for (Constraint c : this.constraintSet) {
            if (c.reduce()) {
                Variable[] v = c.getVariables();
                for (Variable var : this.variableSet) {
                    if (v.length > 1) {
                        if (Objects.equals(var.name, v[0].name)) {
                            var.d.vals = v[0].d.vals;
                        } else if (Objects.equals(var.name, v[1].name)) {
                            var.d.vals = v[1].d.vals;
                        }
                    } else {
                        if (Objects.equals(var.name, v[0].name)) {
                            var.d.vals = v[0].d.vals;
                        }
                    }
                }
                flag = true;
            }

        }
        return flag;

    }
    public static ArrayList<String> printAnswer(String filename) {
        ConstraintSolver problem = new ConstraintSolver();
        problem.parse(filename);
        System.out.println(problem);
        List<ConstraintSolver> solutions = problem.solve();
        if (solutions.size() > 0) {
            System.out.println("The problem can be solved\n");
            for(ConstraintSolver solve : solutions) {
                System.out.println(solve);
            }
        } else {
            System.out.println("The problem cannot be solved\n" + problem);
        }
        return format(solutions);

    }

    public static ArrayList<String> format(List<ConstraintSolver> sol) {
        ArrayList<String> result = new ArrayList<String>();
        for(ConstraintSolver c : sol) {
            for (Variable v : c.variableSet) {
                result.add("Sol-" + v.toStringSol());
            }
        }
        return result;
    }

    public static void main(String[] args) {
        ArrayList<String> solutions = printAnswer("data.txt");
        System.out.println(solutions);
    }


    /**
     * Are all the constraints satisfied
     * @return true if all constraints are met
     */
    public boolean isSatisfied() {
        for (Constraint c : this.constraintSet) {
            if (!c.isSatisfied()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Are any domains empty
     * @return
     */
    public boolean isEmptyDomain() {
        for (Variable v : this.variableSet) {
            if (v.d.isEmpty()) {
                return true;
            }
        }
        return false;
    }



    public List<ConstraintSolver> solve() {
        int dSize = this.dom.vals.length;
        List<ConstraintSolver> solutions = findSolutions(this, new ArrayList<ConstraintSolver>(),
                dSize);
        System.out.println("The problem has " + solutions.size() + " solutions\n");
        return solutions;
    }

    /**
     * Creates new constraintSet ArrayList, iterates through current constraintSet
     * changing the variables to point to the new variables passed into the function.
     * @param vSet new variableSet, whose variables the returned constraintSet will point to
     * @return new constraintSet which points to new variables rather than old set.
     */
    public List<Constraint> getNewConstraints(List<Variable> vSet) {
        List<Constraint> cSet = new ArrayList<>();
        for(Constraint c : this.constraintSet) {
                Variable[] v = c.getVariables();
                Variable v1 = null;
                    if(v.length > 1) {
                        Variable v2 = null;
                            if (c instanceof ConstraintEqualityVarVar) {
                                for (Variable element : vSet) {
                                    if (element.hasThisName(v[0].name)) {
                                        v1 = element;
                                    } else if (element.hasThisName(v[1].name)) {
                                        v2 = element;
                                    }

                                }
                                cSet.add(new ConstraintEqualityVarVar(v1, v2));

                            } else if(c instanceof ConstraintDifferenceVarVar) {
                                for (Variable element : vSet) {
                                    if (element.hasThisName(v[0].name)) {
                                        v1 = element;
                                    } else if(element.hasThisName(v[1].name)) {
                                        v2 = element;
                                    }

                                }
                                cSet.add(new ConstraintDifferenceVarVar(v1, v2));
                            } else if(c instanceof ConstraintEqualityVarPlusCons) {
                                for (Variable element : vSet) {
                                    if (element.hasThisName(v[0].name)) {
                                        v1 = element;
                                    } else if (element.hasThisName(v[1].name)) {
                                        v2 = element;
                                    }

                                }
                                cSet.add(new ConstraintEqualityVarPlusCons(v1, v2, ((ConstraintEqualityVarPlusCons) c).cons,
                                        ((ConstraintEqualityVarPlusCons) c).abs));
                            }
                    } else {
                        if(c instanceof ConstraintEqualityVarCons) {
                            for (Variable element : vSet) {
                                if (element.hasThisName(v[0].name)) {
                                    v1 = element;
                                }
                            }
                            cSet.add(new ConstraintEqualityVarCons(v1, ((ConstraintEqualityVarCons) c).v2));

                    }
                }
            }
        return cSet;
    }

    /**
     * First reduces current problem until either:
     * all constraints can no longer be reduced.
     * An empty domain is found.
     * A solution is found i.e., all constraints are satisfied.
     * After this select a variable to split, split its domain in two,
     * create a new variableSet and constraintSet for both the new left
     * and right variables. Recursively call itself, first with the right
     * variable whose solutions, becomes the solutions for the recursive call of the left variable.
     * @param problem current Zebra problem to be solved
     * @param solutions current solutions found
     * @param dSize the domain size to be passed into selectVariable()
     * @return list of solution to the problem parsed
     */
    private static List<ConstraintSolver> findSolutions (ConstraintSolver problem, List<ConstraintSolver> solutions,  int dSize) {

        boolean keepGoing = true;
        while (keepGoing) {
            keepGoing = problem.reduce();
            if (problem.isEmptyDomain()) {
                return solutions;
            }
            if (problem.isSatisfied()) {
                System.out.println("Added solution to total solutions:" + solutions.size());
                solutions.add(problem);
                return solutions;
            }
        }

        Variable v = selectVariable(problem.variableSet, dSize);

        if (v == null) {
            //no variable was selectable return
            return solutions;
        }

        System.out.println("Variable: " + v );
        List<Variable> left = new ArrayList<>(problem.variableSet);
        left.remove(v);

        left = left.stream().map(v1 -> new Variable(v1.name, v1.d)).collect(Collectors.toList());
        List<Variable> right = left.stream().map(v1 -> new Variable(v1.name, v1.d)).collect(Collectors.toList());

        Domain[] newDomains = v.d.split();
        Variable leftVar = new Variable(v.name, newDomains[0]);
        left.add(leftVar);

        Variable rightVar = new Variable(v.name, newDomains[1]);
        right.add(rightVar);
        List<Constraint> leftConstraints = problem.getNewConstraints(left);
        List<Constraint> rightConstraints = problem.getNewConstraints(right);
        return findSolutions(new ConstraintSolver(left, leftConstraints),
                findSolutions(new ConstraintSolver(right, rightConstraints), solutions,  dSize),  dSize);

    }

    /**
     * Select variable with domain of least length, which is greater then 1.
     * If a domain of length 2 is found it is immediately returned.
     * Returns null if all variables have a length of 1 or 0.
     * @param variableSet variable set of current problem
     * @param domSize domain size of problem parsed
     * @return Returns null if all variables have a length of 1 or 0, else returns domain of least size.
     */
    static Variable selectVariable (List<Variable> variableSet, int domSize) {
        // try selecting the variable with the smallest domain
        Variable selected = null;
        for (Variable v : variableSet) {
            if (v.d.vals.length <= domSize && v.d.vals.length > 1) {
                selected = v;
                domSize = v.d.vals.length;
                if(v.d.vals.length == 2) {return selected;}
            }
        }
        return selected;
    }

}
