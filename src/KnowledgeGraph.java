
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class KnowledgeGraph {

    // Represents a labeled connection between entities
    private static class Edge {

        String relationship;
        String target;

        Edge(String relationship, String target) {
            this.relationship = relationship;
            this.target = target;
        }
    }

    // Store nodes and their outgoing relationships
    private final Map<String, List<Edge>> adjacencyList =
            new LinkedHashMap<>();

    // STEP 1: Add a node to the knowledge graph
    public void addNode(String name) {

        adjacencyList.putIfAbsent(
                name,
                new ArrayList<>()
        );
    }

    // STEP 2: Add a labeled relationship
    public void addRelationship(
            String from,
            String relationship,
            String to) {

        // Ensure both nodes exist
        addNode(from);
        addNode(to);

        // Create an edge from one node to another
        adjacencyList.get(from).add(
                new Edge(relationship, to)
        );
    }

    // STEP 3: Display the adjacency list
    public void printAdjacencyList(String title) {

        System.out.println("\n===== " + title + " =====");

        for (Map.Entry<String, List<Edge>> entry :
                adjacencyList.entrySet()) {

            System.out.println(entry.getKey() + ":");

            if (entry.getValue().isEmpty()) {
                System.out.println(
                        "  (no outgoing relationships)"
                );
            }

            for (Edge edge : entry.getValue()) {

                System.out.println(
                        "  --[" + edge.relationship +
                                "]--> " + edge.target
                );
            }
        }
    }
}
