public interface Algorithm {
    public static class Solution {
        int[] assignations;
        int value;
    }

    public abstract Solution Solve(Problem problem);
}
