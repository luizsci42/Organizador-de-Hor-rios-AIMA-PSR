package aima.gui.demo.search;

import aima.core.environment.nqueens.NQueensBoard;
import aima.core.environment.nqueens.NQueensBoard.Config;
import aima.core.environment.nqueens.NQueensFunctions;
import aima.core.environment.nqueens.NQueensGenAlgoUtil;
import aima.core.environment.nqueens.QueenAction;
import aima.core.search.framework.Metrics;
import aima.core.search.framework.SearchForActions;
import aima.core.search.framework.problem.Problem;
import aima.core.search.framework.qsearch.GraphSearch;
import aima.core.search.framework.qsearch.GraphSearch4e;
import aima.core.search.framework.qsearch.TreeSearch;
import aima.core.search.informed.AStarSearch;
import aima.core.search.local.*;
import aima.core.search.uninformed.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Predicate;

/**
 * Demonsrates how different search algorithms perform on the NQueens problem.
 * @author Ruediger Lunde
 * @author Ravi Mohan
 */

public class NQueensDemo {

	private static final int boardSize = 8;

	public static void main(String[] args) {
		// startNQueensDemo();
		// Problem<NQueensBoard, QueenAction> problem = NQueensFunctions.createIncrementalFormulationProblem(boardSize);
		SearchForActions<NQueensBoard, QueenAction> breadthFirstSearch = new BreadthFirstSearch<>(new GraphSearch<>());
		SearchForActions<NQueensBoard, QueenAction> uniformCostSearch = new UniformCostSearch<>(new GraphSearch<>());
		SearchForActions<NQueensBoard, QueenAction> depthFirstSearch = new DepthFirstSearch<>(new GraphSearch<>());
		SearchForActions<NQueensBoard, QueenAction> depthLimitedSearch = new DepthLimitedSearch<>(boardSize);
		SearchForActions<NQueensBoard, QueenAction> iterativeDeepeningSearch = new IterativeDeepeningSearch<>();

		List<SearchForActions> agentesBusca = new ArrayList<>();
		agentesBusca.add(breadthFirstSearch);
		agentesBusca.add(uniformCostSearch);
		agentesBusca.add(depthFirstSearch);
		agentesBusca.add(depthLimitedSearch);
		agentesBusca.add(iterativeDeepeningSearch);

		for(SearchForActions<NQueensBoard, QueenAction> agenteBusca : agentesBusca) {
			// Definimos as medidas que nos interessam
			double nosExpandidos = 0, custoCaminho = 0, tamanhoMaximoFila = 0, tamanhoFila = 0;
			int tamanhoTabuleiro = boardSize;

			// Executamos cada agente 32 vezes
			for(int i = 0; i < 32; i++) {
				Metrics metricas = resolverNRainhas(agenteBusca, tamanhoTabuleiro);
				nosExpandidos += metricas.getDouble("nodesExpanded");
				custoCaminho += metricas.getDouble("pathCost");
				tamanhoMaximoFila += metricas.getDouble("maxQueueSize");
				tamanhoFila += metricas.getDouble("queueSize");

				if(tamanhoTabuleiro >= 11) {
					tamanhoTabuleiro = 8;
				}
				tamanhoTabuleiro++;
			}

			double mediaNosExpandidos = nosExpandidos / 32;
			double mediaCustoCaminho = custoCaminho / 32;
			double mediaTamanhoMaxFila = tamanhoMaximoFila / 32;
			double mediaTamanhoFila = tamanhoFila / 32;

			System.out.println("\nMédia de nós expandidos: " + Double.toString(mediaNosExpandidos));
			System.out.println("Media de custo do caminho: " + Double.toString(mediaCustoCaminho));
			System.out.println("Média do tamanho máximo da fila: " + Double.toString(mediaTamanhoMaxFila));
			System.out.println("Média do tamanho da fila: " + Double.toString(mediaTamanhoFila));
		}
	}

	private static void startNQueensDemo() {
		solveNQueensWithDepthFirstSearch();
		solveNQueensWithUniformCost();
		solveNQueensWithBreadthFirstSearch();
		solveNQueensWithRecursiveDLS();
		solveNQueensWithIterativeDeepeningSearch();
		// solveNQueensWithAStarSearch();
		// solveNQueensWithAStarSearch4e();
		// solveNQueensWithSimulatedAnnealingSearch();
		// solveNQueensWithHillClimbingSearch();
		// solveNQueensWithGeneticAlgorithmSearch();
		// solveNQueensWithRandomWalk();
	}

	private static Metrics resolverNRainhas(SearchForActions<NQueensBoard, QueenAction> agenteBusca, int tamanhoTabuleiro) {
		Problem<NQueensBoard, QueenAction> problem = NQueensFunctions.createIncrementalFormulationProblem(tamanhoTabuleiro);
		// Problem<NQueensBoard, QueenAction> problem = NQueensFunctions.createCompleteStateFormulationProblem(boardSize, Config.QUEEN_IN_EVERY_COL);
		Optional<List<QueenAction>> actions = agenteBusca.findActions(problem);

		// actions.ifPresent(qActions -> qActions.forEach(System.out::println));
		// System.out.println(agenteBusca.getMetrics());

		return agenteBusca.getMetrics();
	}

	private static void solveNQueensWithDepthFirstSearch() {
		System.out.println("\n--- NQueensDemo DFS ---");

		Problem<NQueensBoard, QueenAction> problem = NQueensFunctions.createIncrementalFormulationProblem(boardSize);
		SearchForActions<NQueensBoard, QueenAction> search = new DepthFirstSearch<>(new TreeSearch<>());
		Optional<List<QueenAction>> actions = search.findActions(problem);

		actions.ifPresent(qActions -> qActions.forEach(System.out::println));
		System.out.println(search.getMetrics());
	}

	private static void solveNQueensWithUniformCost() {
		System.out.println("\n--- Uniform Cost ---");

		Problem<NQueensBoard, QueenAction> problem = NQueensFunctions.createIncrementalFormulationProblem(boardSize);
		SearchForActions<NQueensBoard, QueenAction> search = new UniformCostSearch<>(new TreeSearch<>());
		Optional<List<QueenAction>> actions = search.findActions(problem);

		actions.ifPresent(qActions -> qActions.forEach(System.out::println));
		System.out.println(search.getMetrics());
	}

	private static void solveNQueensWithBreadthFirstSearch() {
		System.out.println("\n--- NQueensDemo BFS ---");

		Problem<NQueensBoard, QueenAction> problem = NQueensFunctions.createIncrementalFormulationProblem(boardSize);
		SearchForActions<NQueensBoard, QueenAction> search = new BreadthFirstSearch<>(new GraphSearch<>());
		Optional<List<QueenAction>> actions = search.findActions(problem);

		actions.ifPresent(qActions -> qActions.forEach(System.out::println));
		System.out.println(search.getMetrics());
	}

	private static void solveNQueensWithAStarSearch() {
		System.out.println("\n--- NQueensDemo A* (complete state formulation, graph search 3e) ---");

		Problem<NQueensBoard, QueenAction> problem = NQueensFunctions.createCompleteStateFormulationProblem
				(boardSize, Config.QUEENS_IN_FIRST_ROW);
		SearchForActions<NQueensBoard, QueenAction> search = new AStarSearch<>
				(new GraphSearch<>(), NQueensFunctions::getNumberOfAttackingPairs);
		Optional<List<QueenAction>> actions = search.findActions(problem);

		actions.ifPresent(qActions -> qActions.forEach(System.out::println));
		System.out.println(search.getMetrics());
	}

	private static void solveNQueensWithAStarSearch4e() {
		System.out.println("\n--- NQueensDemo A* (complete state formulation, graph search 4e) ---");

		Problem<NQueensBoard, QueenAction> problem = NQueensFunctions.createCompleteStateFormulationProblem
				(boardSize, Config.QUEENS_IN_FIRST_ROW);
		SearchForActions<NQueensBoard, QueenAction> search = new AStarSearch<>
				(new GraphSearch4e<>(), NQueensFunctions::getNumberOfAttackingPairs);
		Optional<List<QueenAction>> actions = search.findActions(problem);

		actions.ifPresent(qActions -> qActions.forEach(System.out::println));
		System.out.println(search.getMetrics());
	}

	private static void solveNQueensWithRecursiveDLS() {
		System.out.println("\n--- NQueensDemo recursive DLS ---");

		Problem<NQueensBoard, QueenAction> problem = NQueensFunctions.createIncrementalFormulationProblem(boardSize);
		SearchForActions<NQueensBoard, QueenAction> search = new DepthLimitedSearch<>(boardSize);
		Optional<List<QueenAction>> actions = search.findActions(problem);

		actions.ifPresent(qActions -> qActions.forEach(System.out::println));
		System.out.println(search.getMetrics());
	}

	private static void solveNQueensWithIterativeDeepeningSearch() {
		System.out.println("\n--- NQueensDemo Iterative DS ---");

		Problem<NQueensBoard, QueenAction> problem = NQueensFunctions.createIncrementalFormulationProblem(boardSize);
		SearchForActions<NQueensBoard, QueenAction> search = new IterativeDeepeningSearch<>();
		Optional<List<QueenAction>> actions = search.findActions(problem);

		actions.ifPresent(qActions -> qActions.forEach(System.out::println));
		System.out.println(search.getMetrics());
	}

	private static void solveNQueensWithSimulatedAnnealingSearch() {
		System.out.println("\n--- NQueensDemo Simulated Annealing ---");

		Problem<NQueensBoard, QueenAction> problem =
				NQueensFunctions.createCompleteStateFormulationProblem(boardSize, Config.QUEENS_IN_FIRST_ROW);
		SimulatedAnnealingSearch<NQueensBoard, QueenAction> search =
				new SimulatedAnnealingSearch<>(NQueensFunctions::getNumberOfAttackingPairs,
						new Scheduler(20, 0.045, 100));
		Optional<List<QueenAction>> actions = search.findActions(problem);

		actions.ifPresent(qActions -> qActions.forEach(System.out::println));
		System.out.println(search.getMetrics());
		System.out.println("Final State:\n" + search.getLastState());
	}

	private static void solveNQueensWithHillClimbingSearch() {
		System.out.println("\n--- NQueensDemo HillClimbing ---");

		Problem<NQueensBoard, QueenAction> problem =
				NQueensFunctions.createCompleteStateFormulationProblem(boardSize, Config.QUEENS_IN_FIRST_ROW);
		HillClimbingSearch<NQueensBoard, QueenAction> search = new HillClimbingSearch<>
				(n -> -NQueensFunctions.getNumberOfAttackingPairs(n));
		Optional<List<QueenAction>> actions = search.findActions(problem);

		actions.ifPresent(qActions -> qActions.forEach(System.out::println));
		System.out.println(search.getMetrics());
		System.out.println("Final State:\n" + search.getLastState());
	}

	private static void solveNQueensWithGeneticAlgorithmSearch() {
		System.out.println("\n--- NQueensDemo GeneticAlgorithm ---");

		FitnessFunction<Integer> fitnessFunction = NQueensGenAlgoUtil.getFitnessFunction();
		Predicate<Individual<Integer>> goalTest = NQueensGenAlgoUtil.getGoalTest();
		// Generate an initial population
		Set<Individual<Integer>> population = new HashSet<>();
		for (int i = 0; i < 50; i++)
			population.add(NQueensGenAlgoUtil.generateRandomIndividual(boardSize));

		GeneticAlgorithm<Integer> ga = new GeneticAlgorithm<>(boardSize,
				NQueensGenAlgoUtil.getFiniteAlphabetForBoardOfSize(boardSize), 0.15);

		// Run for a set amount of time
		Individual<Integer> bestIndividual = ga.geneticAlgorithm(population, fitnessFunction, goalTest, 1000L);
		System.out.println("Max time 1 second, Best Individual:\n"
				+ NQueensGenAlgoUtil.getBoardForIndividual(bestIndividual));
		System.out.println("Board Size      = " + boardSize);
		System.out.println("# Board Layouts = " + (new BigDecimal(boardSize)).pow(boardSize));
		System.out.println("Fitness         = " + fitnessFunction.apply(bestIndividual));
		System.out.println("Is Goal         = " + goalTest.test(bestIndividual));
		System.out.println("Population Size = " + ga.getPopulationSize());
		System.out.println("Iterations      = " + ga.getIterations());
		System.out.println("Took            = " + ga.getTimeInMilliseconds() + "ms.");

		// Run till goal is achieved
		bestIndividual = ga.geneticAlgorithm(population, fitnessFunction, goalTest, 0L);
		System.out.println("");
		System.out.println("Max time unlimited, Best Individual:\n" +
				NQueensGenAlgoUtil.getBoardForIndividual(bestIndividual));
		System.out.println("Board Size      = " + boardSize);
		System.out.println("# Board Layouts = " + (new BigDecimal(boardSize)).pow(boardSize));
		System.out.println("Fitness         = " + fitnessFunction.apply(bestIndividual));
		System.out.println("Is Goal         = " + goalTest.test(bestIndividual));
		System.out.println("Population Size = " + ga.getPopulationSize());
		System.out.println("Itertions       = " + ga.getIterations());
		System.out.println("Took            = " + ga.getTimeInMilliseconds() + "ms.");
	}

	// Here, this trivial algorithm outperforms the genetic search approach as described in the textbook!
	private static void solveNQueensWithRandomWalk() {
		System.out.println("\n--- NQueensDemo RandomWalk ---");
		NQueensBoard board;
		int i = 0;
		long startTime = System.currentTimeMillis();
		do {
			i++;
			board = new NQueensBoard(boardSize, Config.QUEEN_IN_EVERY_COL);
		} while (board.getNumberOfAttackingPairs() > 0);
		long stopTime = System.currentTimeMillis();
		System.out.println("Solution found after generating " + i + " random configurations ("
				+ (stopTime - startTime) + " ms).");
	}
}
