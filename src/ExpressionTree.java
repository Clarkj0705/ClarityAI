
import java.util.HashMap;
import java.util.Map;

public class ExpressionTree {

    // Each Node stores a proposition or the AND operator.
    private static class Node {
        String value;
        Node left;
        Node right;

        Node(String value) {
            this.value = value;
        }

        Node(String value, Node left, Node right) {
            this.value = value;
            this.left = left;
            this.right = right;
        }
    }

    // Generate and display an AND expression tree
    public static void showAndTree(String title,
                                   String[] names,
                                   boolean[] values) {

        if (names.length == 0 || names.length != values.length) {
            throw new IllegalArgumentException(
                    "Provide matching proposition names and values.");
        }

        // Build the expression tree
        Node root = new Node(names[0]);

        for (int i = 1; i < names.length; i++) {
            root = new Node("AND", root, new Node(names[i]));
        }

        // Store actual proposition values
        Map<String, Boolean> inputs = new HashMap<>();

        for (int i = 0; i < names.length; i++) {
            inputs.put(names[i], values[i]);
        }

        System.out.println("\n===== " + title + " =====");
        System.out.println("Expression: " + expression(root));
        System.out.println("Tree (displayed sideways):");

        printTree(root, 0);

        StringBuilder pre = new StringBuilder();
        StringBuilder in = new StringBuilder();
        StringBuilder post = new StringBuilder();

        preorder(root, pre);
        inorder(root, in);
        postorder(root, post);

        System.out.println("Preorder:  " + pre.toString().trim());
        System.out.println("Inorder:   " + in.toString().trim());
        System.out.println("Postorder: " + post.toString().trim());

        System.out.println("Result:    " + evaluate(root, inputs));
    }

    // Generate a parenthesized logical expression
    private static String expression(Node node) {

        if (node.left == null && node.right == null) {
            return node.value;
        }

        return "(" + expression(node.left) + " AND "
                + expression(node.right) + ")";
    }

    // Print the tree sideways
    private static void printTree(Node node, int depth) {

        if (node == null) {
            return;
        }

        printTree(node.right, depth + 1);

        for (int i = 0; i < depth; i++) {
            System.out.print("    ");
        }

        System.out.println(node.value);

        printTree(node.left, depth + 1);
    }

    // Preorder: Root, Left, Right
    private static void preorder(Node node, StringBuilder output) {

        if (node == null) {
            return;
        }

        output.append(node.value).append(" ");

        preorder(node.left, output);
        preorder(node.right, output);
    }

    // Inorder: Left, Root, Right
    private static void inorder(Node node, StringBuilder output) {

        if (node == null) {
            return;
        }

        inorder(node.left, output);

        output.append(node.value).append(" ");

        inorder(node.right, output);
    }

    // Postorder: Left, Right, Root
    private static void postorder(Node node, StringBuilder output) {

        if (node == null) {
            return;
        }

        postorder(node.left, output);
        postorder(node.right, output);

        output.append(node.value).append(" ");
    }

    // Evaluate the tree using actual input values
    private static boolean evaluate(Node node,
                                    Map<String, Boolean> inputs) {

        if (node.left == null && node.right == null) {
            return inputs.get(node.value);
        }

        return evaluate(node.left, inputs)
                && evaluate(node.right, inputs);
    }
}
