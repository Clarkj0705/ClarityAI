
public class TruthTable {

    // Generates a truth table for an AND expression
    public static void generateAndTable(
            String title, String... variables) {

        System.out.println("\n===== " + title + " =====");

        int numberOfVariables = variables.length;

        // Calculate number of possible combinations
        int rows = (int) Math.pow(2, numberOfVariables);

        // Print column headers
        for (String variable : variables) {
            System.out.printf("%-8s", variable);
        }

        System.out.println("Result");

        // Generate all combinations
        for (int row = 0; row < rows; row++) {

            boolean[] values =
                    new boolean[numberOfVariables];

            for (int col = 0; col < numberOfVariables; col++) {

                int divisor = (int) Math.pow(
                        2, numberOfVariables - col - 1);

                boolean value = (row / divisor) % 2 == 1;

                values[col] = value;

                System.out.printf("%-8s", value ? "T" : "F");
            }

            // Evaluate the expression using LogicEngine
            boolean result = LogicEngine.and(values);

            System.out.println(result ? "T" : "F");
        }
    }

    // Truth table for Scenario A
    public static void printCampusRouteTrainTable() {

        generateAndTable(
                "CAMPUS ROUTE TRUTH TABLE",
                "T", "F", "D", "W"
        );
    }

    // Truth table for Scenario D
    public static void printSecureStepsTable() {

        generateAndTable(
                "SECURE STEPS TRUTH TABLE",
                "P", "V", "L"
        );
    }

    // Demonstrates De Morgan's Law
    public static void printDeMorganTable() {

        System.out.println("\n===== DE MORGAN'S LAW =====");

        System.out.println(
                "P  V  L  !(P&&V&&L)  (!P||!V||!L)  Equal"
        );

        boolean allMatch = true;

        for (int row = 0; row < 8; row++) {

            boolean P = (row / 4) % 2 == 1;
            boolean V = (row / 2) % 2 == 1;
            boolean L = row % 2 == 1;

            boolean left = LogicEngine.not(
                    LogicEngine.and(P, V, L)
            );

            boolean right = LogicEngine.or(
                    LogicEngine.not(P),
                    LogicEngine.not(V),
                    LogicEngine.not(L)
            );

            boolean equal = left == right;

            if (!equal) {
                allMatch = false;
            }

            System.out.printf(
                    "%s  %s  %s  %-11s  %-13s  %s%n",
                    P ? "T" : "F",
                    V ? "T" : "F",
                    L ? "T" : "F",
                    left ? "T" : "F",
                    right ? "T" : "F",
                    equal
            );
        }

        System.out.println(
                "All rows equivalent: " + allMatch
        );
    }
}
