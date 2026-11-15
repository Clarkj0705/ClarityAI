
import java.util.Scanner;

public class SecureSteps {

    private final Scanner input;

    // Constructor
    public SecureSteps(Scanner input) {
        this.input = input;
    }

    // Runs Scenario D
    public void run() {

        System.out.println("\n===== CLARITYAI =====");
        System.out.println("Scenario D: SecureSteps");
        System.out.println("Fictional Login Simulator");
        System.out.println("Do not enter real passwords or codes.");

        // STEP 1: Collect fictional login conditions
        System.out.print(
                "\nDid the password check pass? (true/false): "
        );
        boolean P = input.nextBoolean();

        System.out.print(
                "Did second verification pass? (true/false): "
        );
        boolean V = input.nextBoolean();

        System.out.print(
                "Is the account unlocked? (true/false): "
        );
        boolean L = input.nextBoolean();

        // STEP 2: Evaluate propositional logic
        // AccessGranted = P AND V AND L

        boolean accessGranted = LogicEngine.and(P, V, L);

        // Use NOT to check whether access was denied
        boolean accessDenied = LogicEngine.not(accessGranted);

        // Use OR to check whether any condition failed
        boolean anyFailedCheck = LogicEngine.or(
                LogicEngine.not(P),
                LogicEngine.not(V),
                LogicEngine.not(L)
        );

        // STEP 3: Display logic results
        System.out.println("\n===== LOGIC RESULTS =====");

        System.out.println("P - Password Passed: " + P);
        System.out.println("V - Verification Passed: " + V);
        System.out.println("L - Account Unlocked: " + L);

        System.out.println();
        System.out.println("Logical Expression: P && V && L");

        System.out.println("Access Granted: " + accessGranted);
        System.out.println("Access Denied: " + accessDenied);
        System.out.println("Any Failed Check: " + anyFailedCheck);

        // STEP 4: Demonstrate De Morgan's Law
        // NOT(P AND V AND L) = NOT P OR NOT V OR NOT L

        System.out.println("\n===== LOGICAL EQUIVALENCE =====");

        System.out.println(
                "De Morgan's Law Verified: "
                        + (accessDenied == anyFailedCheck)
        );

        // STEP 5: Explain the evaluation
        System.out.println("\n===== EXPLANATION =====");

        if (accessGranted) {
            System.out.println(
                    "All required security conditions passed."
            );
        } else {

            if (!P) {
                System.out.println(
                        "- Password check failed."
                );
            }

            if (!V) {
                System.out.println(
                        "- Second verification failed."
                );
            }

            if (!L) {
                System.out.println(
                        "- Account is locked."
                );
            }
        }

        // STEP 6: Display the security decision
        System.out.println("\n===== DECISION =====");

        if (!L) {
            System.out.println("ACCESS DENIED");
            System.out.println(
                    "Next step: Contact support or use account recovery."
            );

        } else if (!P) {
            System.out.println("ACCESS DENIED");
            System.out.println(
                    "Next step: Retry the fictional password check."
            );

        } else if (!V) {
            System.out.println("ACCESS DENIED");
            System.out.println(
                    "Next step: Use verification recovery."
            );

        } else {
            System.out.println("ACCESS GRANTED");
            System.out.println(
                    "All security requirements are satisfied."
            );
        }

        // STEP 7: Generate the truth table
        TruthTable.printSecureStepsTable();

        // Demonstrate De Morgan's Law for every combination
        TruthTable.printDeMorganTable();

        // STEP 8: Generate the expression tree
        // Includes preorder, inorder, and postorder traversals

        ExpressionTree.showAndTree(
                "SECURE STEPS - ACCESS EXPRESSION TREE",
                new String[]{"P", "V", "L"},
                new boolean[]{P, V, L}
        );

        // STEP 9: Build the decision tree
        DecisionTree.Node securityTree = DecisionTree.sequence(

                new String[]{
                        "Is the account unlocked (L)?",
                        "Did the password check pass (P)?",
                        "Did second verification pass (V)?"
                },

                new boolean[]{
                        L,
                        P,
                        V
                },

                new String[]{
                        "ACCESS DENIED: Use account recovery or contact support.",
                        "ACCESS DENIED: Retry the fictional password check.",
                        "ACCESS DENIED: Use verification recovery."
                },

                "ACCESS GRANTED: All security checks passed."
        );

        // Display the complete decision tree and chosen path
        DecisionTree.show(
                "SECURE STEPS DECISION TREE",
                securityTree
        );

// STEP 10: Build the SecureSteps knowledge graph

        KnowledgeGraph securityGraph = new KnowledgeGraph();

// Connect user and account
        securityGraph.addRelationship(
                "User",
                "requests access to",
                "Account"
        );

// Account security requirements
        securityGraph.addRelationship(
                "Account",
                "requires",
                "Password Check"
        );

        securityGraph.addRelationship(
                "Account",
                "requires",
                "Second Verification"
        );

        securityGraph.addRelationship(
                "Account",
                "checks",
                "Account Lock Status"
        );

// Password verification result
        securityGraph.addRelationship(
                "Password Check",
                P ? "passed" : "failed",
                "Access Decision"
        );

// Second verification result
        securityGraph.addRelationship(
                "Second Verification",
                V ? "passed" : "failed",
                "Access Decision"
        );

// Account lock status
        securityGraph.addRelationship(
                "Account Lock Status",
                L ? "unlocked" : "locked",
                "Access Decision"
        );

// Overall access decision
        securityGraph.addRelationship(
                "Account",
                accessGranted ? "access granted" : "access denied",
                "Access Decision"
        );

// Account recovery relationship
        securityGraph.addRelationship(
                "Account",
                "offers recovery through",
                "Help Desk"
        );

// Display the security knowledge graph
        securityGraph.printAdjacencyList(
                "SECURE STEPS KNOWLEDGE GRAPH"
        );

    }
}
