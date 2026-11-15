
public class DecisionTree {

    // Represents a question or a final decision
    public static class Node {

        private final String text;
        private final boolean answer;
        private final Node yesBranch;
        private final Node noBranch;
        private final boolean leaf;

        private Node(String text, boolean answer,
                     Node yesBranch, Node noBranch,
                     boolean leaf) {

            this.text = text;
            this.answer = answer;
            this.yesBranch = yesBranch;
            this.noBranch = noBranch;
            this.leaf = leaf;
        }
    }

    // Create a question with YES and NO branches
    public static Node question(String text,
                                boolean answer,
                                Node yesBranch,
                                Node noBranch) {

        return new Node(
                text, answer, yesBranch, noBranch, false
        );
    }

    // Create a final result (leaf node)
    public static Node result(String text) {

        return new Node(
                text, false, null, null, true
        );
    }

    // Build a sequence of decision questions
    public static Node sequence(String[] questions,
                                boolean[] answers,
                                String[] failures,
                                String success) {

        if (questions.length == 0 ||
                questions.length != answers.length ||
                questions.length != failures.length) {

            throw new IllegalArgumentException(
                    "Question, answer, and failure arrays must match."
            );
        }

        Node tree = result(success);

        // Build the tree backward
        for (int i = questions.length - 1; i >= 0; i--) {

            tree = question(
                    questions[i],
                    answers[i],
                    tree,
                    result(failures[i])
            );
        }

        return tree;
    }

    // Display tree and evaluate current inputs
    public static String show(String title, Node root) {

        System.out.println("\n===== " + title + " =====");

        System.out.println("Full Decision Tree:");
        printTree(root, "");

        System.out.println("\nPath for Current Inputs:");

        Node current = root;

        while (!current.leaf) {

            System.out.println(
                    current.text + " -> " +
                            (current.answer ? "YES" : "NO")
            );

            if (current.answer) {
                current = current.yesBranch;
            } else {
                current = current.noBranch;
            }
        }

        System.out.println("Final Action: " + current.text);

        return current.text;
    }

    // Recursively display the complete tree
    private static void printTree(Node node, String indent) {

        if (node.leaf) {
            System.out.println(
                    indent + "ACTION: " + node.text
            );
            return;
        }

        System.out.println(
                indent + "QUESTION: " + node.text
        );

        System.out.println(indent + "  YES:");
        printTree(node.yesBranch, indent + "    ");

        System.out.println(indent + "  NO:");
        printTree(node.noBranch, indent + "    ");
    }
}
