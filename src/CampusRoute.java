
import java.util.Scanner;

public class CampusRoute {

    private final Scanner input;

    // Constructor
    public CampusRoute(Scanner input) {
        this.input = input;
    }

    // Runs Scenario A
    public void run() {

        System.out.println("\n===== CLARITYAI =====");
        System.out.println("Scenario A: CampusRoute");
        System.out.println("Campus Transportation Decision System");

        // STEP 1: General transportation information
        System.out.print("\nEnter your budget: $");
        double budget = input.nextDouble();

        System.out.print("Minutes until arrival deadline: ");
        int deadline = input.nextInt();

        System.out.print("Are walking conditions acceptable? (true/false): ");
        boolean W = input.nextBoolean();

        // STEP 2: Train information
        System.out.println("\n--- TRAIN ---");

        System.out.print("Is the train operating? (true/false): ");
        boolean T = input.nextBoolean();

        System.out.print("Train fare: $");
        double trainFare = input.nextDouble();

        System.out.print("Train travel time (minutes): ");
        int trainTime = input.nextInt();

        System.out.print("Train walking time (minutes): ");
        int trainWalk = input.nextInt();

        // STEP 3: Bus information
        System.out.println("\n--- BUS ---");

        System.out.print("Is the bus operating? (true/false): ");
        boolean B = input.nextBoolean();

        System.out.print("Bus fare: $");
        double busFare = input.nextDouble();

        System.out.print("Bus travel time (minutes): ");
        int busTime = input.nextInt();

        System.out.print("Bus walking time (minutes): ");
        int busWalk = input.nextInt();

        // STEP 4: Backup ride information
        System.out.println("\n--- BACKUP RIDE ---");

        System.out.print("Is a backup ride available? (true/false): ");
        boolean R = input.nextBoolean();

        System.out.print("Backup ride fare: $");
        double rideFare = input.nextDouble();

        System.out.print("Backup ride time (minutes): ");
        int rideTime = input.nextInt();

        // STEP 5: Calculate travel times
        int trainTotal = trainTime + trainWalk;
        int busTotal = busTime + busWalk;

        // Define Boolean propositions
        boolean F_train = trainFare <= budget;
        boolean D_train = trainTotal <= deadline;

        boolean F_bus = busFare <= budget;
        boolean D_bus = busTotal <= deadline;

        boolean F_ride = rideFare <= budget;
        boolean D_ride = rideTime <= deadline;

        // Evaluate transportation eligibility using LogicEngine
        boolean trainEligible = LogicEngine.and(
                T, F_train, D_train, W
        );

        boolean busEligible = LogicEngine.and(
                B, F_bus, D_bus
        );

        boolean rideEligible = LogicEngine.and(
                R, F_ride, D_ride
        );

        // Check whether any transportation option qualifies
        boolean anyEligible = LogicEngine.or(
                trainEligible,
                busEligible,
                rideEligible
        );

        // STEP 6: Display logic results
        System.out.println("\n===== LOGIC RESULTS =====");

        System.out.println("Train Eligible: " + trainEligible);
        System.out.println("Bus Eligible: " + busEligible);
        System.out.println("Ride Eligible: " + rideEligible);
        System.out.println("Any Option Eligible: " + anyEligible);

        // Explain which conditions passed or failed
        System.out.println("\n===== EXPLANATION =====");

        // Train explanations
        if (!T) {
            System.out.println("Train: Service is not operating.");
        }

        if (!F_train) {
            System.out.println("Train: Fare exceeds budget.");
        }

        if (!D_train) {
            System.out.println("Train: Arrival misses deadline.");
        }

        if (!W) {
            System.out.println(
                    "Train: Walking conditions are unacceptable."
            );
        }

        // Bus explanations
        if (!B) {
            System.out.println("Bus: Service is not operating.");
        }

        if (!F_bus) {
            System.out.println("Bus: Fare exceeds budget.");
        }

        if (!D_bus) {
            System.out.println("Bus: Arrival misses deadline.");
        }

        // Backup ride explanations
        if (!R) {
            System.out.println("Backup ride: Ride is unavailable.");
        }

        if (!F_ride) {
            System.out.println("Backup ride: Fare exceeds budget.");
        }

        if (!D_ride) {
            System.out.println("Backup ride: Arrival misses deadline.");
        }

        // Explain successful conditions
        if (trainEligible) {
            System.out.println("Train: All conditions passed.");
        }

        if (busEligible) {
            System.out.println("Bus: All conditions passed.");
        }

        if (rideEligible) {
            System.out.println("Backup ride: All conditions passed.");
        }

        // STEP 7: Select transportation priority
        System.out.println("\nChoose your priority:");
        System.out.println("1. Lowest Cost");
        System.out.println("2. Shortest Time");
        System.out.print("Enter choice (1 or 2): ");

        int priority = input.nextInt();

        if (priority != 1 && priority != 2) {
            System.out.println(
                    "Invalid priority. Enter 1 or 2 next time."
            );
            return;
        }

        // Find the best eligible transportation option
        String bestOption = "None";
        double bestScore = Double.MAX_VALUE;

        // Compare train
        if (trainEligible) {
            bestOption = "Train";

            bestScore = (priority == 1)
                    ? trainFare : trainTotal;
        }

        // Compare bus
        if (busEligible) {
            double score = (priority == 1)
                    ? busFare : busTotal;

            if (score < bestScore) {
                bestScore = score;
                bestOption = "Bus";
            }
        }

        // Compare backup ride
        if (rideEligible) {
            double score = (priority == 1)
                    ? rideFare : rideTime;

            if (score < bestScore) {
                bestScore = score;
                bestOption = "Backup Ride";
            }
        }

        // Display final recommendation
        System.out.println("\n===== RECOMMENDATION =====");

        if (LogicEngine.not(anyEligible)) {

            System.out.println(
                    "No transportation option qualifies."
            );

            System.out.println(
                    "Review your budget, service, and deadline."
            );

        } else {

            System.out.println("Recommended: " + bestOption);

            if (priority == 1) {
                System.out.println(
                        "Reason: Lowest cost among eligible options."
                );
            } else {
                System.out.println(
                        "Reason: Shortest time among eligible options."
                );
            }
        }

        // STEP 8: Generate the CampusRoute truth table
        TruthTable.printCampusRouteTrainTable();

        // STEP 9: Generate expression trees and traversals

        // Train expression tree
        ExpressionTree.showAndTree(
                "CAMPUS ROUTE - TRAIN EXPRESSION TREE",
                new String[]{"T", "F", "D", "W"},
                new boolean[]{T, F_train, D_train, W}
        );

        // Bus expression tree
        ExpressionTree.showAndTree(
                "CAMPUS ROUTE - BUS EXPRESSION TREE",
                new String[]{"B", "F_bus", "D_bus"},
                new boolean[]{B, F_bus, D_bus}
        );

        // Backup ride expression tree
        ExpressionTree.showAndTree(
                "CAMPUS ROUTE - RIDE EXPRESSION TREE",
                new String[]{"R", "F_ride", "D_ride"},
                new boolean[]{R, F_ride, D_ride}
        );

        // STEP 10: Build the transportation decision trees

        // Train decision tree
        DecisionTree.Node trainTree = DecisionTree.sequence(

                new String[]{
                        "Is the train operating?",
                        "Is the train fare within budget?",
                        "Will the train meet the deadline?",
                        "Are walking conditions acceptable?"
                },

                new boolean[]{
                        T,
                        F_train,
                        D_train,
                        W
                },

                new String[]{
                        "Train unavailable.",
                        "Train fare exceeds budget.",
                        "Train arrives too late.",
                        "Walking conditions are unacceptable."
                },

                "Train is eligible."
        );

        DecisionTree.show(
                "CAMPUS ROUTE - TRAIN DECISION TREE",
                trainTree
        );

        // Bus decision tree
        DecisionTree.Node busTree = DecisionTree.sequence(

                new String[]{
                        "Is the bus operating?",
                        "Is the bus fare within budget?",
                        "Will the bus meet the deadline?"
                },

                new boolean[]{
                        B,
                        F_bus,
                        D_bus
                },

                new String[]{
                        "Bus unavailable.",
                        "Bus fare exceeds budget.",
                        "Bus arrives too late."
                },

                "Bus is eligible."
        );

        DecisionTree.show(
                "CAMPUS ROUTE - BUS DECISION TREE",
                busTree
        );

        // Backup ride decision tree
        DecisionTree.Node rideTree = DecisionTree.sequence(

                new String[]{
                        "Is a backup ride available?",
                        "Is the ride fare within budget?",
                        "Will the ride meet the deadline?"
                },

                new boolean[]{
                        R,
                        F_ride,
                        D_ride
                },

                new String[]{
                        "Backup ride unavailable.",
                        "Backup ride fare exceeds budget.",
                        "Backup ride arrives too late."
                },

                "Backup ride is eligible."
        );

        DecisionTree.show(
                "CAMPUS ROUTE - RIDE DECISION TREE",
                rideTree
        );

        // STEP 11: Build the final recommendation decision tree
        DecisionTree.Node finalTree = DecisionTree.question(

                "Is any transportation option eligible?",
                anyEligible,

                DecisionTree.question(
                        "Is lowest cost the priority?",
                        priority == 1,

                        DecisionTree.result(
                                "Recommend " + bestOption +
                                        ": Lowest cost among eligible options."
                        ),

                        DecisionTree.result(
                                "Recommend " + bestOption +
                                        ": Shortest time among eligible options."
                        )
                ),

                DecisionTree.result(
                        "No transportation option qualifies."
                )
        );

        // Display final decision tree
        DecisionTree.show(
                "CAMPUS ROUTE - FINAL RECOMMENDATION",
                finalTree
        );

        // STEP 12: Build the CampusRoute knowledge graph

        KnowledgeGraph campusGraph = new KnowledgeGraph();

        // Create nodes for student constraints
        String budgetNode = String.format("Budget: $%.2f", budget);
        String deadlineNode = "Time Available: " + deadline + " minutes";

        // Student relationships
        campusGraph.addRelationship(
                "Student", "travels to", "Campus"
        );

        campusGraph.addRelationship(
                "Student", "has", budgetNode
        );

        campusGraph.addRelationship(
                "Student", "must arrive within", deadlineNode
        );

        campusGraph.addRelationship(
                "Student", "considers", "Train"
        );

        campusGraph.addRelationship(
                "Student", "considers", "Bus"
        );

        campusGraph.addRelationship(
                "Student", "considers", "Backup Ride"
        );

        // Train relationships
        campusGraph.addRelationship(
                "Train",
                "fare",
                String.format("Train Fare: $%.2f", trainFare)
        );

        campusGraph.addRelationship(
                "Train",
                T ? "service operating" : "service unavailable",
                "Campus"
        );

        campusGraph.addRelationship(
                "Train",
                F_train ? "within budget" : "over budget",
                budgetNode
        );

        campusGraph.addRelationship(
                "Train",
                D_train ? "meets deadline" : "misses deadline",
                deadlineNode
        );

        campusGraph.addRelationship(
                "Train",
                W ? "walking acceptable" : "walking unacceptable",
                "Weather"
        );

        campusGraph.addRelationship(
                "Train",
                trainEligible ? "eligible" : "ineligible",
                "Recommendation"
        );

        // Bus relationships
        campusGraph.addRelationship(
                "Bus",
                "fare",
                String.format("Bus Fare: $%.2f", busFare)
        );

        campusGraph.addRelationship(
                "Bus",
                B ? "service operating" : "service unavailable",
                "Campus"
        );

        campusGraph.addRelationship(
                "Bus",
                F_bus ? "within budget" : "over budget",
                budgetNode
        );

        campusGraph.addRelationship(
                "Bus",
                D_bus ? "meets deadline" : "misses deadline",
                deadlineNode
        );

        campusGraph.addRelationship(
                "Bus",
                busEligible ? "eligible" : "ineligible",
                "Recommendation"
        );

// Backup ride relationships
        campusGraph.addRelationship(
                "Backup Ride",
                "fare",
                String.format("Ride Fare: $%.2f", rideFare)
        );

        campusGraph.addRelationship(
                "Backup Ride",
                R ? "available" : "unavailable",
                "Campus"
        );

        campusGraph.addRelationship(
                "Backup Ride",
                F_ride ? "within budget" : "over budget",
                budgetNode
        );

        campusGraph.addRelationship(
                "Backup Ride",
                D_ride ? "meets deadline" : "misses deadline",
                deadlineNode
        );

        campusGraph.addRelationship(
                "Backup Ride",
                rideEligible ? "eligible" : "ineligible",
                "Recommendation"
        );

// Connect the final recommendation
        campusGraph.addRelationship(
                "Recommendation",
                "selects",
                anyEligible ? bestOption : "No eligible transportation"
        );

// Display the CampusRoute knowledge graph
        campusGraph.printAdjacencyList(
                "CAMPUS ROUTE KNOWLEDGE GRAPH"
        );

    }
}
