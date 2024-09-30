package com.garentii.Script.Execution.Planner;
import java.util.*;

public class ScriptExecutionPlanner {

    // Method to plan the execution of scripts
    public static List<Integer> getExecutionPlan(List<VulnerabilityScript> scripts) {
        // Map to store scriptId -> list of dependent scriptIds
        Map<Integer, List<Integer>> dependencyGraph = new HashMap<>();
        // Map to store the in-degree (number of dependencies) of each script
        Map<Integer, Integer> inDegree = new HashMap<>();

        // Initialize the graph and in-degree map
        for (VulnerabilityScript script : scripts) {
            dependencyGraph.put(script.getScriptId(), new ArrayList<>());
            inDegree.put(script.getScriptId(), 0);
        }

        // Build the graph
        for (VulnerabilityScript script : scripts) {
            int scriptId = script.getScriptId();
            for (int dependency : script.getDependencies()) {
                // Add an edge from the dependency to the current script
                dependencyGraph.get(dependency).add(scriptId);
                // Increase the in-degree of the current script
                inDegree.put(scriptId, inDegree.get(scriptId) + 1);
            }
        }

        // Topological sort using Kahn's algorithm (BFS approach)
        List<Integer> executionOrder = new ArrayList<>();
        Queue<Integer> queue = new LinkedList<>();

        // Add all scripts with zero in-degree (i.e., no dependencies) to the queue
        for (int scriptId : inDegree.keySet()) {
            if (inDegree.get(scriptId) == 0) {
                queue.add(scriptId);
            }
        }

        // Process each script in the queue
        while (!queue.isEmpty()) {
            int currentScript = queue.poll();
            executionOrder.add(currentScript);

            // Reduce the in-degree of all scripts that depend on the current script
            for (int dependentScript : dependencyGraph.get(currentScript)) {
                inDegree.put(dependentScript, inDegree.get(dependentScript) - 1);
                // If a script has no more dependencies, add it to the queue
                if (inDegree.get(dependentScript) == 0) {
                    queue.add(dependentScript);
                }
            }
        }

        // If not all scripts are processed, there is a cycle
        if (executionOrder.size() != scripts.size()) {
            throw new IllegalStateException("Cycle detected in the dependencies.");
        }

        return executionOrder;
    }
    public static void main(String[] args) {
        // Test Case 1: Simple dependency chain
        List<VulnerabilityScript> scripts1 = Arrays.asList(
                new VulnerabilityScript(1, Arrays.asList(2, 3)),
                new VulnerabilityScript(2, Arrays.asList(3)),
                new VulnerabilityScript(3, Collections.emptyList())
        );
        System.out.println("Execution Plan for scripts 1: " + getExecutionPlan(scripts1));

        // Test Case 2: No dependencies
        List<VulnerabilityScript> scripts2 = Arrays.asList(
                new VulnerabilityScript(1, Collections.emptyList()),
                new VulnerabilityScript(2, Collections.emptyList()),
                new VulnerabilityScript(3, Collections.emptyList())
        );
        System.out.println("Execution Plan for scripts 2: " + getExecutionPlan(scripts2));

        // Test Case 3: More complex dependencies
        List<VulnerabilityScript> scripts3 = Arrays.asList(
                new VulnerabilityScript(1, Arrays.asList(2)),
                new VulnerabilityScript(2, Arrays.asList(3)),
                new VulnerabilityScript(3, Arrays.asList(4)),
                new VulnerabilityScript(4, Collections.emptyList())
        );
        System.out.println("Execution Plan for scripts 3: " + getExecutionPlan(scripts3));

        // Test Case 4: Isolated script with no dependencies
        List<VulnerabilityScript> scripts4 = Arrays.asList(
                new VulnerabilityScript(1, Arrays.asList(2)),
                new VulnerabilityScript(2, Arrays.asList(3)),
                new VulnerabilityScript(3, Collections.emptyList()),
                new VulnerabilityScript(4, Collections.emptyList()) // isolated script
        );
        System.out.println("Execution Plan for scripts 4: " + getExecutionPlan(scripts4));

        // Test Case 5: Edge case: single script with no dependencies
        List<VulnerabilityScript> scripts5 = Arrays.asList(
                new VulnerabilityScript(1, Collections.emptyList())
        );
        System.out.println("Execution Plan for scripts 5: " + getExecutionPlan(scripts5));

        // Test Case 6: Script with cycle dependency
        List<VulnerabilityScript> scripts6 = Arrays.asList(
                new VulnerabilityScript(1, Arrays.asList(1)),
                new VulnerabilityScript(2, Arrays.asList(3)),  // Cycle here between 1 and 2
                new VulnerabilityScript(3, Arrays.asList(2)),
                new VulnerabilityScript(4, Collections.emptyList()) // isolated script
        );
        try {
            System.out.println("Execution Plan for scripts 6: " + getExecutionPlan(scripts6));
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());  // Expect "Cycle detected in the dependencies."
        }
        // Test Case 7: Script with cycle dependency
        List<VulnerabilityScript> scripts7 = Arrays.asList(
                new VulnerabilityScript(1, Arrays.asList(1)) // isolated script
        );
        try {
            System.out.println("Execution Plan for scripts 7: " + getExecutionPlan(scripts6));
        } catch (IllegalStateException e) {
            System.out.println(e.getMessage());  // Expect "Cycle detected in the dependencies."
        }
    }
}
