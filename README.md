Overview
This repository provides a Java-based script execution planner that effectively handles scripts with dependencies. It constructs a dependency graph, performs topological sorting to determine the correct execution order, and generates an execution plan.

Key Features
Dependency Graph Construction: Creates a directed graph representing the dependencies between scripts.
Topological Sorting: Uses Kahn's algorithm to determine the optimal execution order, preventing circular dependencies.
Execution Plan Generation: Generates a list of script IDs in the correct execution sequence.
Error Handling: Handles potential issues like circular dependencies, empty graphs, and invalid script IDs.
Boundary Conditions: Addresses edge cases and ensures robust functionality.
Usage
Create VulnerabilityScript Objects:

Instantiate VulnerabilityScript objects with their respective script IDs and dependency lists.
Generate Execution Plan:

Call the createExecutionPlan method from the VulnerabilityScriptExecutionPlan class, passing the list of VulnerabilityScript objects as input.
Execute Scripts:

Iterate through the generated execution plan and execute scripts in the specified order.
Example
Java
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<VulnerabilityScript>   
 scripts = new ArrayList<>();
        scripts.add(new VulnerabilityScript(1, List.of(2, 3)));
        scripts.add(new VulnerabilityScript(2, List.of()));
        scripts.add(new VulnerabilityScript(3, List.of()));

        List<Integer> executionPlan = VulnerabilityScriptExecutionPlan.createExecutionPlan(scripts);
        System.out.println("Execution Plan: " + executionPlan);
    }
}
Use code with caution.

Dependencies
Java 8 or later
