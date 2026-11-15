import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ClarityAIUI extends JFrame {

    // Five result views for each scenario
    private static class Results {
        JTextArea recommendation;
        JTextArea truthTable;
        JTextArea expressionTree;
        JTextArea decisionTree;
        JTextArea knowledgeGraph;
        JTabbedPane tabs = new JTabbedPane();

        Results() {
            recommendation = addTab("Recommendation");
            truthTable = addTab("Truth Table");
            expressionTree = addTab("Expression Tree");
            decisionTree = addTab("Decision Tree");
            knowledgeGraph = addTab("Knowledge Graph");
        }

        private JTextArea addTab(String name) {
            JTextArea area = new JTextArea("Enter information, then click Evaluate.");
            area.setEditable(false);
            area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
            area.setMargin(new Insets(12, 12, 12, 12));
            tabs.addTab(name, new JScrollPane(area));
            return area;
        }
    }

    public ClarityAIUI() {
        super("ClarityAI - Explainable Decision Support");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 820);
        setMinimumSize(new Dimension(800, 620));
        setLocationRelativeTo(null);

        JTabbedPane scenarios = new JTabbedPane();
        scenarios.addTab("Scenario A - CampusRoute", createCampusPanel());
        scenarios.addTab("Scenario D - SecureSteps", createSecurityPanel());
        add(scenarios);
    }

    private JPanel createCampusPanel() {
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = constraints();

        JTextField budget = field(form, c, "Budget ($)", "10");
        JTextField deadline = field(form, c, "Minutes until deadline", "60");
        JCheckBox goodWalking = check(form, c, "Walking conditions acceptable", true);

        heading(form, c, "Train");
        JCheckBox trainOperating = check(form, c, "Train operating", true);
        JTextField trainFare = field(form, c, "Train fare ($)", "5");
        JTextField trainTime = field(form, c, "Train travel time (minutes)", "30");
        JTextField trainWalk = field(form, c, "Train walking time (minutes)", "5");

        heading(form, c, "Bus");
        JCheckBox busOperating = check(form, c, "Bus operating", true);
        JTextField busFare = field(form, c, "Bus fare ($)", "2");
        JTextField busTime = field(form, c, "Bus travel time (minutes)", "40");
        JTextField busWalk = field(form, c, "Bus walking time (minutes)", "5");

        heading(form, c, "Backup Ride");
        JCheckBox rideAvailable = check(form, c, "Backup ride available", true);
        JTextField rideFare = field(form, c, "Backup ride fare ($)", "12");
        JTextField rideTime = field(form, c, "Backup ride time (minutes)", "20");

        JLabel priorityLabel = new JLabel("Recommendation priority");
        JComboBox<String> priority = new JComboBox<>(
                new String[]{"Lowest Cost", "Shortest Time"});
        addRow(form, c, priorityLabel, priority);

        Results results = new Results();
        JButton evaluate = button("Evaluate CampusRoute");
        addFullRow(form, c, evaluate);

        evaluate.addActionListener(event -> {
            try {
                double b = nonnegativeDouble(budget, "Budget");
                int d = nonnegativeInt(deadline, "Deadline");
                boolean W = goodWalking.isSelected();
                boolean T = trainOperating.isSelected();
                boolean B = busOperating.isSelected();
                boolean R = rideAvailable.isSelected();

                double tf = nonnegativeDouble(trainFare, "Train fare");
                double bf = nonnegativeDouble(busFare, "Bus fare");
                double rf = nonnegativeDouble(rideFare, "Backup ride fare");
                int tt = nonnegativeInt(trainTime, "Train time");
                int tw = nonnegativeInt(trainWalk, "Train walking time");
                int bt = nonnegativeInt(busTime, "Bus time");
                int bw = nonnegativeInt(busWalk, "Bus walking time");
                int rt = nonnegativeInt(rideTime, "Backup ride time");

                long trainTotal = (long) tt + tw;
                long busTotal = (long) bt + bw;
                boolean Ft = tf <= b, Dt = trainTotal <= d;
                boolean Fb = bf <= b, Db = busTotal <= d;
                boolean Fr = rf <= b, Dr = rt <= d;
                boolean trainOK = LogicEngine.and(T, Ft, Dt, W);
                boolean busOK = LogicEngine.and(B, Fb, Db);
                boolean rideOK = LogicEngine.and(R, Fr, Dr);
                boolean any = LogicEngine.or(trainOK, busOK, rideOK);
                boolean lowestCost = priority.getSelectedIndex() == 0;

                String best = "None";
                double bestScore = Double.MAX_VALUE;
                if (trainOK) {
                    best = "Train";
                    bestScore = lowestCost ? tf : trainTotal;
                }
                if (busOK) {
                    double score = lowestCost ? bf : busTotal;
                    if (score < bestScore) {
                        best = "Bus";
                        bestScore = score;
                    }
                }
                if (rideOK) {
                    double score = lowestCost ? rf : rt;
                    if (score < bestScore) {
                        best = "Backup Ride";
                        bestScore = score;
                    }
                }

                StringBuilder explanation = new StringBuilder();
                explanation.append("CAMPUSROUTE - EVALUATION\n\n");
                explanation.append("Train eligible: ").append(trainOK).append('\n');
                explanation.append("Bus eligible: ").append(busOK).append('\n');
                explanation.append("Backup ride eligible: ").append(rideOK).append("\n\n");
                if (!T) explanation.append("Train: service unavailable.\n");
                if (!Ft) explanation.append("Train: over budget.\n");
                if (!Dt) explanation.append("Train: too late.\n");
                if (!W) explanation.append("Train: walking conditions unacceptable.\n");
                if (!B) explanation.append("Bus: service unavailable.\n");
                if (!Fb) explanation.append("Bus: over budget.\n");
                if (!Db) explanation.append("Bus: too late.\n");
                if (!R) explanation.append("Backup ride: unavailable.\n");
                if (!Fr) explanation.append("Backup ride: over budget.\n");
                if (!Dr) explanation.append("Backup ride: too late.\n");
                explanation.append("\nRECOMMENDATION: ").append(best).append('\n');
                if (any) {
                    explanation.append("Reason: ")
                            .append(lowestCost ? "lowest fare" : "shortest time")
                            .append(" among eligible options.\n");
                } else {
                    explanation.append("No transportation option meets all requirements.\n");
                }
                explanation.append("\nRules:\n")
                        .append("Train = T AND F_train AND D_train AND W\n")
                        .append("Bus   = B AND F_bus AND D_bus\n")
                        .append("Ride  = R AND F_ride AND D_ride\n")
                        .append("Assumption: weather restricts train walking only.\n")
                        .append("Times include walking for train and bus.\n");
                results.recommendation.setText(explanation.toString());

                results.truthTable.setText(capture(() ->
                        TruthTable.printCampusRouteTrainTable()));

                results.expressionTree.setText(capture(() -> {
                    ExpressionTree.showAndTree("TRAIN EXPRESSION TREE",
                            new String[]{"T", "F", "D", "W"},
                            new boolean[]{T, Ft, Dt, W});
                    ExpressionTree.showAndTree("BUS EXPRESSION TREE",
                            new String[]{"B", "F_bus", "D_bus"},
                            new boolean[]{B, Fb, Db});
                    ExpressionTree.showAndTree("BACKUP RIDE EXPRESSION TREE",
                            new String[]{"R", "F_ride", "D_ride"},
                            new boolean[]{R, Fr, Dr});
                }));

                DecisionTree.Node trainTree = DecisionTree.sequence(
                        new String[]{"Train operating?", "Train within budget?",
                                "Train on time?", "Walking acceptable?"},
                        new boolean[]{T, Ft, Dt, W},
                        new String[]{"Train unavailable", "Train over budget",
                                "Train too late", "Unsafe walking"}, "Train eligible");
                DecisionTree.Node busTree = DecisionTree.sequence(
                        new String[]{"Bus operating?", "Bus within budget?", "Bus on time?"},
                        new boolean[]{B, Fb, Db},
                        new String[]{"Bus unavailable", "Bus over budget", "Bus too late"},
                        "Bus eligible");
                DecisionTree.Node rideTree = DecisionTree.sequence(
                        new String[]{"Ride available?", "Ride within budget?", "Ride on time?"},
                        new boolean[]{R, Fr, Dr},
                        new String[]{"Ride unavailable", "Ride over budget", "Ride too late"},
                        "Ride eligible");
                final String chosen = best;
                DecisionTree.Node finalTree = DecisionTree.question(
                        "Any eligible option?", any,
                        DecisionTree.question("Lowest cost priority?", lowestCost,
                                DecisionTree.result("Recommend " + chosen + " (lowest cost)"),
                                DecisionTree.result("Recommend " + chosen + " (shortest time)")),
                        DecisionTree.result("No eligible transportation"));
                results.decisionTree.setText(capture(() -> {
                    DecisionTree.show("TRAIN DECISION TREE", trainTree);
                    DecisionTree.show("BUS DECISION TREE", busTree);
                    DecisionTree.show("BACKUP RIDE DECISION TREE", rideTree);
                    DecisionTree.show("FINAL RECOMMENDATION", finalTree);
                }));

                KnowledgeGraph graph = new KnowledgeGraph();
                String budgetNode = String.format("Budget $%.2f", b);
                String deadlineNode = "Deadline " + d + " minutes";
                graph.addRelationship("Student", "travels to", "Campus");
                graph.addRelationship("Student", "has", budgetNode);
                graph.addRelationship("Student", "must arrive within", deadlineNode);
                graph.addRelationship("Student", "considers", "Train");
                graph.addRelationship("Student", "considers", "Bus");
                graph.addRelationship("Student", "considers", "Backup Ride");
                addTransportEdges(graph, "Train", T, Ft, Dt, trainOK,
                        tf, trainTotal, budgetNode, deadlineNode);
                graph.addRelationship("Train", W ? "walking acceptable" : "walking unacceptable", "Weather");
                addTransportEdges(graph, "Bus", B, Fb, Db, busOK,
                        bf, busTotal, budgetNode, deadlineNode);
                addTransportEdges(graph, "Backup Ride", R, Fr, Dr, rideOK,
                        rf, rt, budgetNode, deadlineNode);
                graph.addRelationship("Recommendation", "selects", any ? best : "None");
                results.knowledgeGraph.setText(capture(() ->
                        graph.printAdjacencyList("CAMPUSROUTE KNOWLEDGE GRAPH")));
                results.tabs.setSelectedIndex(0);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(),
                        "Check Your Input", JOptionPane.ERROR_MESSAGE);
            }
        });

        return splitPanel(form, results.tabs);
    }

    private JPanel createSecurityPanel() {
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = constraints();
        heading(form, c, "Fictional login simulator - do not enter credentials");
        JCheckBox P = check(form, c, "Password check passed", true);
        JCheckBox V = check(form, c, "Second verification passed", true);
        JCheckBox L = check(form, c, "Account is unlocked", true);
        Results results = new Results();
        JButton evaluate = button("Evaluate SecureSteps");
        addFullRow(form, c, evaluate);

        evaluate.addActionListener(event -> {
            boolean p = P.isSelected(), v = V.isSelected(), l = L.isSelected();
            boolean granted = LogicEngine.and(p, v, l);
            boolean denied = LogicEngine.not(granted);
            boolean anyFailed = LogicEngine.or(
                    LogicEngine.not(p), LogicEngine.not(v), LogicEngine.not(l));

            StringBuilder result = new StringBuilder("SECURESTEPS - EVALUATION\n\n");
            result.append("P - Password passed: ").append(p).append('\n');
            result.append("V - Verification passed: ").append(v).append('\n');
            result.append("L - Account unlocked: ").append(l).append("\n\n");
            result.append("AccessGranted = P AND V AND L\n");
            result.append("Access granted: ").append(granted).append('\n');
            result.append("Access denied: ").append(denied).append('\n');
            result.append("De Morgan equivalent: ").append(anyFailed).append('\n');
            result.append("Equivalence confirmed: ").append(denied == anyFailed).append("\n\n");
            if (granted) {
                result.append("ACTION: ACCESS GRANTED\nAll three checks passed.\n");
            } else {
                result.append("ACTION: ACCESS DENIED\n");
                if (!l) result.append("Account locked: contact support or use recovery.\n");
                if (!p) result.append("Password check failed: retry simulated check.\n");
                if (!v) result.append("Verification failed: use verification recovery.\n");
            }
            result.append("\nLearning simulator only; not a real login system.\n");
            results.recommendation.setText(result.toString());

            results.truthTable.setText(capture(() -> {
                TruthTable.printSecureStepsTable();
                TruthTable.printDeMorganTable();
            }));
            results.expressionTree.setText(capture(() ->
                    ExpressionTree.showAndTree("SECURESTEPS EXPRESSION TREE",
                            new String[]{"P", "V", "L"},
                            new boolean[]{p, v, l})));
            DecisionTree.Node tree = DecisionTree.sequence(
                    new String[]{"Account unlocked?", "Password check passed?",
                            "Second verification passed?"},
                    new boolean[]{l, p, v},
                    new String[]{"DENIED: account recovery or support",
                            "DENIED: retry fictional password check",
                            "DENIED: verification recovery"},
                    "ACCESS GRANTED");
            results.decisionTree.setText(capture(() ->
                    DecisionTree.show("SECURESTEPS DECISION TREE", tree)));

            KnowledgeGraph graph = new KnowledgeGraph();
            graph.addRelationship("User", "requests access to", "Account");
            graph.addRelationship("Account", "requires", "Password Check");
            graph.addRelationship("Account", "requires", "Second Verification");
            graph.addRelationship("Account", "checks", "Account Lock Status");
            graph.addRelationship("Password Check", p ? "passed" : "failed", "Access Decision");
            graph.addRelationship("Second Verification", v ? "passed" : "failed", "Access Decision");
            graph.addRelationship("Account Lock Status", l ? "unlocked" : "locked", "Access Decision");
            graph.addRelationship("Account", granted ? "granted" : "denied", "Access Decision");
            graph.addRelationship("Account", "offers recovery through", "Help Desk");
            results.knowledgeGraph.setText(capture(() ->
                    graph.printAdjacencyList("SECURESTEPS KNOWLEDGE GRAPH")));
            results.tabs.setSelectedIndex(0);
        });
        return splitPanel(form, results.tabs);
    }

    private static void addTransportEdges(KnowledgeGraph graph, String name,
                                          boolean operating, boolean withinBudget,
                                          boolean onTime, boolean eligible,
                                          double fare, long totalTime,
                                          String budgetNode, String deadlineNode) {
        graph.addRelationship(name, operating ? "available" : "unavailable", "Campus");
        graph.addRelationship(name, "fare", String.format("%s $%.2f", name, fare));
        graph.addRelationship(name, "total time", name + " " + totalTime + " minutes");
        graph.addRelationship(name, withinBudget ? "within budget" : "over budget", budgetNode);
        graph.addRelationship(name, onTime ? "meets deadline" : "misses deadline", deadlineNode);
        graph.addRelationship(name, eligible ? "eligible" : "ineligible", "Recommendation");
    }

    private static GridBagConstraints constraints() {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(4, 7, 4, 7);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.WEST;
        return c;
    }

    private static void addRow(JPanel form, GridBagConstraints c,
                               JComponent label, JComponent component) {
        c.gridx = 0;
        c.weightx = 0.0;
        form.add(label, c);
        c.gridx = 1;
        c.weightx = 1.0;
        form.add(component, c);
        c.gridy++;
    }

    private static JTextField field(JPanel form, GridBagConstraints c,
                                    String label, String value) {
        JTextField textField = new JTextField(value, 12);
        addRow(form, c, new JLabel(label), textField);
        return textField;
    }

    private static JCheckBox check(JPanel form, GridBagConstraints c,
                                   String label, boolean checked) {
        JCheckBox box = new JCheckBox(label, checked);
        c.gridx = 0;
        c.gridwidth = 2;
        c.weightx = 1;
        form.add(box, c);
        c.gridwidth = 1;
        c.gridy++;
        return box;
    }

    private static void heading(JPanel form, GridBagConstraints c, String title) {
        JLabel label = new JLabel(title);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 15f));
        c.gridx = 0;
        c.gridwidth = 2;
        form.add(label, c);
        c.gridwidth = 1;
        c.gridy++;
    }

    private static JButton button(String label) {
        JButton button = new JButton(label);
        button.setFont(button.getFont().deriveFont(Font.BOLD, 14f));
        return button;
    }

    private static void addFullRow(JPanel form, GridBagConstraints c, JComponent component) {
        c.gridx = 0;
        c.gridwidth = 2;
        c.weightx = 1;
        form.add(component, c);
        c.gridwidth = 1;
        c.gridy++;
    }

    private static JPanel splitPanel(JPanel form, JTabbedPane tabs) {
        JScrollPane formScroll = new JScrollPane(form);
        formScroll.setBorder(BorderFactory.createTitledBorder("Scenario Inputs"));
        formScroll.getVerticalScrollBar().setUnitIncrement(14);
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, formScroll, tabs);
        split.setResizeWeight(0.50);
        split.setDividerLocation(355);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(split, BorderLayout.CENTER);
        return panel;
    }

    private static double nonnegativeDouble(JTextField field, String label) {
        try {
            double value = Double.parseDouble(field.getText().trim());
            if (!Double.isFinite(value) || value < 0)
                throw new NumberFormatException();
            return value;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(label + " must be a nonnegative number.");
        }
    }

    private static int nonnegativeInt(JTextField field, String label) {
        try {
            int value = Integer.parseInt(field.getText().trim());
            if (value < 0) throw new NumberFormatException();
            return value;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(label + " must be a nonnegative whole number.");
        }
    }

    // Adapt existing console-printing classes for the Swing output tabs
    private static String capture(Runnable task) {
        PrintStream saved = System.out;
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try (PrintStream captured = new PrintStream(output)) {
            System.setOut(captured);
            task.run();
        } finally {
            System.setOut(saved);
        }
        return output.toString();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ClarityAIUI().setVisible(true));
    }
}
