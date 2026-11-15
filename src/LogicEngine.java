
public class LogicEngine {

    // Logical AND
    public static boolean and(boolean... conditions) {

        for (boolean condition : conditions) {
            if (!condition) {
                return false;
            }
        }

        return true;
    }

    // Logical OR
    public static boolean or(boolean... conditions) {

        for (boolean condition : conditions) {
            if (condition) {
                return true;
            }
        }

        return false;
    }

    // Logical NOT
    public static boolean not(boolean condition) {
        return !condition;
    }
}
