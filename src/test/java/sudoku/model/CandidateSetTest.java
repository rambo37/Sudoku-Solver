package sudoku.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CandidateSetTest {

    @Test
    public void testInitialisation() {
        CandidateSet set = new CandidateSet(9);
        assertEquals(set.size(), 9);
        int[] expectedCandidates = {1, 2, 3, 4, 5, 6, 7, 8, 9};
        assertArrayEquals(set.getCandidates(), expectedCandidates);

        CandidateSet set2 = new CandidateSet(16);
        assertEquals(set2.size(), 16);
        int[] expectedCandidates2 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
        assertArrayEquals(set2.getCandidates(), expectedCandidates2);
    }

    @Test
    public void testAssignValue() {
        CandidateSet set = new CandidateSet(9);
        set.assignValue(5);
        int[] expectedCandidates = {5};
        assertArrayEquals(set.getCandidates(), expectedCandidates);
        assertEquals(set.size(), 1);

        CandidateSet set2 = new CandidateSet(16);
        set2.assignValue(16);
        int[] expectedCandidates2 = {16};
        assertArrayEquals(set2.getCandidates(), expectedCandidates2);
        assertEquals(set2.size(), 1);
    }

    @Test
    public void testAssignIllegalValues() {
        CandidateSet set = new CandidateSet(9);
        CandidateSet set2 = new CandidateSet(16);
        assertThrows(IllegalArgumentException.class, () -> set.assignValue(-1));
        assertThrows(IllegalArgumentException.class, () -> set.assignValue(0));
        assertThrows(IllegalArgumentException.class, () -> set.assignValue(10));

        assertThrows(IllegalArgumentException.class, () -> set2.assignValue(-1));
        assertThrows(IllegalArgumentException.class, () -> set2.assignValue(0));
        assertThrows(IllegalArgumentException.class, () -> set2.assignValue(17));
    }

    @Test
    public void testRemoveLargestValue() {
        CandidateSet set = new CandidateSet(9);
        boolean removed = set.remove(9);
        assertTrue(removed);
        int[] expectedCandidates = {1, 2, 3, 4, 5, 6, 7, 8};
        assertArrayEquals(set.getCandidates(), expectedCandidates);
        assertEquals(set.size(), 8);

        CandidateSet set2 = new CandidateSet(16);
        boolean removed2 = set2.remove(16);
        assertTrue(removed2);
        int[] expectedCandidates2 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        assertArrayEquals(set2.getCandidates(), expectedCandidates2);
        assertEquals(set2.size(), 15);
    }

    @Test
    public void testRemoveSmallestValue() {
        CandidateSet set = new CandidateSet(9);
        boolean removed = set.remove(1);
        assertTrue(removed);
        int[] expectedCandidates = {2, 3, 4, 5, 6, 7, 8, 9};
        assertArrayEquals(set.getCandidates(), expectedCandidates);
        assertEquals(set.size(), 8);

        CandidateSet set2 = new CandidateSet(16);
        boolean removed2 = set2.remove(1);
        assertTrue(removed2);
        int[] expectedCandidates2 = {2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
        assertArrayEquals(set2.getCandidates(), expectedCandidates2);
        assertEquals(set2.size(), 15);
    }

    @Test
    public void testRemoveMultipleValues() {
        CandidateSet set = new CandidateSet(9);
        boolean removed = set.remove(3);
        assertTrue(removed);
        int[] expectedCandidates = {1, 2, 4, 5, 6, 7, 8, 9};
        assertArrayEquals(set.getCandidates(), expectedCandidates);
        assertEquals(set.size(), 8);

        removed = set.remove(7);
        assertTrue(removed);
        int[] expectedCandidates2 = {1, 2, 4, 5, 6, 8, 9};
        assertArrayEquals(set.getCandidates(), expectedCandidates2);
        assertEquals(set.size(), 7);

        // Trying to remove an element that is no longer in the set returns false and changes
        // nothing
        removed = set.remove(3);
        assertFalse(removed);
        assertArrayEquals(set.getCandidates(), expectedCandidates2);
        assertEquals(set.size(), 7);
    }

    @Test
    public void testRemoveIllegalValues() {
        CandidateSet set = new CandidateSet(9);
        CandidateSet set2 = new CandidateSet(16);

        assertThrows(IllegalArgumentException.class, () -> set.remove(-1));
        assertThrows(IllegalArgumentException.class, () -> set.remove(0));
        assertThrows(IllegalArgumentException.class, () -> set.remove(10));

        assertThrows(IllegalArgumentException.class, () -> set2.remove(-1));
        assertThrows(IllegalArgumentException.class, () -> set2.remove(0));
        assertThrows(IllegalArgumentException.class, () -> set2.remove(17));
    }

    @Test
    public void testIsEmpty() {
        CandidateSet set = new CandidateSet(9);
        assertFalse(set.isEmpty());
        for (int i = 1; i <= 8; i++) {
            set.remove(i);
            assertFalse(set.isEmpty());
        }

        set.remove(9);
        assertTrue(set.isEmpty());


        CandidateSet set2 = new CandidateSet(16);
        assertFalse(set2.isEmpty());
        for (int i = 1; i <= 15; i++) {
            set2.remove(i);
            assertFalse(set2.isEmpty());
        }

        set2.remove(16);
        assertTrue(set2.isEmpty());
    }

    @Test
    public void testGetOnlyCandidate() {
        CandidateSet set = new CandidateSet(9);
        for (int i = 1; i <= 8; i++) {
            set.remove(i);
        }
        assertEquals(set.getOnlyCandidate(), 9);


        CandidateSet set2 = new CandidateSet(16);
        for (int i = 1; i <= 16; i++) {
            if (i != 5) set2.remove(i);
        }
        assertEquals(set2.getOnlyCandidate(), 5);
    }

    @Test
    public void testGetOnlyCandidateOnMultipleElementSet() {
        CandidateSet set = new CandidateSet(9);
        assertThrows(IllegalStateException.class, set::getOnlyCandidate);

        for (int i = 1; i <= 7; i++) {
            set.remove(i);
            assertThrows(IllegalStateException.class, set::getOnlyCandidate);
        }

        CandidateSet set2 = new CandidateSet(16);
        assertThrows(IllegalStateException.class, set2::getOnlyCandidate);

        for (int i = 1; i <= 14; i++) {
            set2.remove(i);
            assertThrows(IllegalStateException.class, set2::getOnlyCandidate);
        }
    }

    @Test
    public void testCloneFreshSet() {
        CandidateSet set = new CandidateSet(9);
        CandidateSet clone = set.clone();

        // The clone should be a deep copy
        assertNotSame(set, clone);
        assertEquals(set, clone);


        CandidateSet set2 = new CandidateSet(9);
        CandidateSet clone2 = set2.clone();

        assertNotSame(set2, clone2);
        assertEquals(set2, clone2);
    }

    @Test
    public void testCloneModifiedSet() {
        CandidateSet set = new CandidateSet(9);
        set.remove(5);
        set.remove(3);
        set.remove(7);

        CandidateSet clone = set.clone();
        assertNotSame(set, clone);
        assertEquals(set, clone);


        CandidateSet set2 = new CandidateSet(16);
        set2.remove(5);
        set2.remove(3);
        set2.remove(7);

        CandidateSet clone2 = set2.clone();
        assertNotSame(set2, clone2);
        assertEquals(set2, clone2);
    }

    @Test
    public void testEquals() {
        CandidateSet set = new CandidateSet(9);
        CandidateSet set2 = new CandidateSet(9);

        assertEquals(set, set2);

        for (int i = 1; i <= 7; i++) {
            set.remove(i);
            assertNotEquals(set, set2);
            set2.remove(i);
            assertEquals(set, set2);
        }

        set.assignValue(9);
        assertNotEquals(set, set2);
        set2.assignValue(9);
        assertEquals(set, set2);
    }
}
