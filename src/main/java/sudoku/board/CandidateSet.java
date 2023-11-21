package sudoku.board;

import java.util.Arrays;
import java.util.Objects;

/**
 * The CandidateSet class represents the set of candidate values for a square in a Sudoku board.
 * <p>
 * Once created, a CandidateSet object has all legal possible candidate values depending on the size
 * passed to the constructor. For 9 by 9 Sudoku boards, a size of 9 is passed in the constructor.
 * <p>
 * The class uses a boolean array with a size 1 greater than the size passed in the constructor.
 * This is to facilitate accessing/modifying particular indexes of the array without having to
 * subtract 1. A boolean array is used to provide better performance than a Set or array of ints.
 * <p>
 * It is important to note that a CandidateSet object can only ever decrease in size - it is not
 * possible to insert elements back in to the set once removed. The class also contains a method
 * that immediately removes all candidate values except for the one provided as an argument (the
 * assignValue method).
 *
 * @author Savraj Bassi
 * @version 20/11/2023
 */

public class CandidateSet {
    private final boolean[] set;
    private int size;

    /**
     * Creates a CandidateSet object using the set and size of another instance. Only used by the
     * clone() method.
     *
     * @param set  The set of boolean values from the other CandidateSet object
     * @param size The size of the other CandidateSet object
     */
    private CandidateSet(boolean[] set, int size) {
        this.set = Arrays.copyOf(set, set.length);
        this.size = size;
    }

    /**
     * Creates a CandidateSet of the specified size with all candidates initially present.
     *
     * @param size The size of the CandidateSet
     */
    public CandidateSet(int size) {
        set = new boolean[size + 1];
        for (int i = 1; i <= size; i++) {
            set[i] = true;
        }
        this.size = size;
    }

    /**
     * Checks if this CandidateSet is empty.
     *
     * @return True if the CandidateSet is empty and false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the size of this CandidateSet.
     *
     * @return The size of the set
     */
    public int size() {
        return size;
    }

    /**
     * Returns the only candidate left in this CandidateSet instance. Should only be called when
     * the size of the CandidateSet is 1.
     *
     * @return The single value that this CandidateSet contains
     */
    public int getOnlyCandidate() {
        if (size != 1) {
            throw new IllegalStateException("Method getOnlyCandidate() invoked when there is not" +
                    "just 1 candidate remaining");
        }
        for (int i = 1; i <= set.length; i++) {
            if (set[i]) return i;
        }

        // Should never reach here
        return -1;
    }

    /**
     * Assigns a single value to this CandidateSet instance, effectively removing all candidates
     * except for the one passed as the argument. The value must be between 1 and the initial size
     * of the set.
     *
     * @param value The single value that this CandidateSet should be left with
     */
    public void assignValue(int value) {
        if (value >= set.length || value < 1) {
            throw new IllegalArgumentException("Attempted to assign " + value + " to a candidate " +
                    "set of size " + (set.length - 1));
        }
        Arrays.fill(set, false);
        set[value] = true;
        size = 1;
    }

    /**
     * Removes the specified candidate from the set of candidates.
     *
     * @param candidate The candidate to remove
     * @return True if the removal actually removed a candidate and false otherwise
     */
    public boolean remove(int candidate) {
        if (candidate >= set.length || candidate < 1) {
            throw new IllegalArgumentException("Attempted to remove " + candidate + " from a " +
                    "candidate set of size " + (set.length - 1));
        }
        if (set[candidate]) {
            set[candidate] = false;
            size--;
            return true;
        }
        return false;
    }

    /**
     * Returns an int array of all the remaining candidates of this CandidateSet instance.
     *
     * @return The candidates of this CandidateSet
     */
    public int[] getCandidates() {
        int[] candidates = new int[size];
        int index = 0;

        for (int i = 1; i < set.length; i++) {
            if (set[i]) {
                candidates[index] = i;
                index++;
            }
        }

        return candidates;
    }

    /**
     * Creates and returns a clone of this instance. Does not call super.clone() as a deep copy
     * must be created.
     *
     * @return A clone of this instance
     */
    @Override
    public CandidateSet clone() {
        return new CandidateSet(set, size);
    }

    /**
     * Returns a string representation of the CandidateSet object.
     *
     * @return A a string representation of the CandidateSet object
     */
    @Override
    public String toString() {
        return Arrays.toString(getCandidates());
    }

    /**
     * Checks if this CandidateSet object is equal to another object.
     *
     * @param o The object to be compared
     * @return True if the objects are equal and false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CandidateSet that = (CandidateSet) o;
        return size == that.size && Arrays.equals(set, that.set);
    }

    /**
     * Generates a hash code for the CandidateSet object.
     *
     * @return The hash code
     */
    @Override
    public int hashCode() {
        int result = Objects.hash(size);
        result = 31 * result + Arrays.hashCode(set);
        return result;
    }
}
