package main.java;

public class Domain {

    int[] vals;


    public Domain(int[] vals) {
        this.vals = vals;
    }


    public Domain(Domain d2) {
        vals = new int[d2.vals.length];
        for(int i = 0; i < vals.length; i++)
            this.vals[i] = d2.vals[i];
    }


    public void delete(int index) {
        int[] newArr = new int[vals.length - 1]; // new array with one less element

        int j = 0; // index for new array
        for (int i = 0; i < vals.length; i++) {
            if (i != index) {
                newArr[j] = vals[i]; // copy element to new array
                j++; // increment index for new array
            }
        }

        this.vals = newArr;
    }

    /**
     * @return
     */
    public String toString() {
        String result  = "{";
        for (int i = 0; i < vals.length; i++)
            result += vals[i];
        result += "}";
        return result;
    }

    /**
     * @return
     */
    public Domain[] split() {
        if(this.vals.length > 1) {
            int pos = this.vals.length / 2;
            int[] a = new int[pos];
            int[] b = new int[this.vals.length-pos];
            System.arraycopy(this.vals, 0, a, 0, pos);
            System.arraycopy(this.vals, pos, b, 0, this.vals.length - pos);
            return new Domain[]{new Domain(a), new Domain(b)};
        }

        return new Domain[]{this};
    }

    /**
     * @return
     */
    public boolean isEmpty() {
        return this.vals.length == 0;
    }

    /**
     * @return
     */
    private boolean equals(Domain d2) {
        return this.vals.equals(d2.vals);
    }

    /**
     * @return
     */
    public boolean  isReducedToOnlyOneValue() {
        return this.vals.length == 1;
    }



}
