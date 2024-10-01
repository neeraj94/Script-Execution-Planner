package com.garentii.Script.Execution.Planner;

import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class ScriptExecutionPlannerTest {

    @Test
    void testSimpleDependencyChain() {
        List<VulnerabilityScript> scripts = List.of(
                new VulnerabilityScript(1, List.of(2, 3)),
                new VulnerabilityScript(2, List.of(3)),
                new VulnerabilityScript(3, Collections.emptyList())
        );
        List<Integer> expectedPlan = List.of(3, 2, 1);
        List<Integer> actualPlan = ScriptExecutionPlanner.getExecutionPlan(scripts);
        assertEquals(expectedPlan, actualPlan);
    }

    @Test
    void testNoDependencies() {
        List<VulnerabilityScript> scripts = List.of(
                new VulnerabilityScript(1, Collections.emptyList()),
                new VulnerabilityScript(2, Collections.emptyList()),
                new VulnerabilityScript(3, Collections.emptyList())
        );
        List<Integer> expectedPlan = List.of(1, 2, 3); // Order might vary, but any valid permutation is fine
        List<Integer> actualPlan = ScriptExecutionPlanner.getExecutionPlan(scripts);
        assertTrue(actualPlan.containsAll(expectedPlan) && actualPlan.size() == expectedPlan.size());
    }

    @Test
    void testMoreComplexDependencies() {
        List<VulnerabilityScript> scripts = List.of(
                new VulnerabilityScript(1, List.of(2)),
                new VulnerabilityScript(2, List.of(3)),
                new VulnerabilityScript(3, List.of(4)),
                new VulnerabilityScript(4, Collections.emptyList())
        );
        List<Integer> expectedPlan = List.of(4, 3, 2, 1);
        List<Integer> actualPlan = ScriptExecutionPlanner.getExecutionPlan(scripts);
        assertEquals(expectedPlan, actualPlan);
    }

    @Test
    void testIsolatedScript() {
        List<VulnerabilityScript> scripts = List.of(
                new VulnerabilityScript(1, List.of(2)),
                new VulnerabilityScript(2, List.of(3)),
                new VulnerabilityScript(3, Collections.emptyList()),
                new VulnerabilityScript(4, Collections.emptyList()) // isolated script
        );
        List<Integer> expectedPlan = List.of(4, 3, 2, 1); // Order might vary, but 4 should be first
        List<Integer> actualPlan = ScriptExecutionPlanner.getExecutionPlan(scripts);
        assertTrue(actualPlan.containsAll(expectedPlan) && actualPlan.size() == expectedPlan.size());
        assertTrue(actualPlan.contains(4));
    }

    @Test
    void testSingleScriptNoDependencies() {
        List<VulnerabilityScript> scripts = List.of(
                new VulnerabilityScript(1, Collections.emptyList())
        );
        List<Integer> expectedPlan = List.of(1);
        List<Integer> actualPlan = ScriptExecutionPlanner.getExecutionPlan(scripts);
        assertEquals(expectedPlan, actualPlan);
    }

    @Test
    void testCycleDependency() {
        List<VulnerabilityScript> scripts = List.of(
                new VulnerabilityScript(1, List.of(2)),
                new VulnerabilityScript(2, List.of(3)),
                new VulnerabilityScript(3, List.of(1)) // cycle
        );
        assertThrows(IllegalStateException.class, () -> ScriptExecutionPlanner.getExecutionPlan(scripts));
    }

    @Test
    void testEmptyScriptList() {
        List<VulnerabilityScript> scripts = Collections.emptyList();
        List<Integer> actualPlan = ScriptExecutionPlanner.getExecutionPlan(scripts);
        assertTrue(actualPlan.isEmpty());
    }

    @Test
    void testNullScriptList() {
        assertThrows(NullPointerException.class, () -> ScriptExecutionPlanner.getExecutionPlan(null));
    }
}