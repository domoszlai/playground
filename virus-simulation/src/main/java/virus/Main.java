package virus;

import org.jgrapht.Graph;
import org.jgrapht.generate.WattsStrogatzGraphGenerator;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class Main {
    
    public static void main(String[] args) {

        Parameter p = new Parameter();
        
        // Generate a Watts-Strogatz small-world graph
        Graph<Person, DefaultEdge> graph = 
                new SimpleGraph<>(Person::new, DefaultEdge::new, false);
        var gen = new WattsStrogatzGraphGenerator<Person,DefaultEdge>(p.n,p.k,p.pr);
        gen.generateGraph(graph);
        
        var sim = new Simulation(graph,p);
        
        for(int i=0; i<100; i++)
        {
            sim.tick();
            System.out.println(sim.getTime()+": "+sim.getCurrentInfections()
                                            +", "+sim.getAllInfections()
                                            +", "+sim.getAllRecoveries()
                                            +", "+sim.getAllDeaths());
        }
    }
    
}
