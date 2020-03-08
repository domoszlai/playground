package virus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.traverse.BreadthFirstIterator;

public class Simulation {

    private final Random rand = new Random();
    private final Graph<Person, DefaultEdge> graph;
    private final HashSet<Person> infectedPeople = new HashSet<Person>();
    private final Parameter param;
    private int time = 0;
    
    public static int allInfections = 1;
    public static int allDeaths = 0;
    public static int allRecoveries = 0;
    
    public Simulation(Graph<Person, DefaultEdge> graph, Parameter p)
    {
        this.graph = graph;
        this.param = p;
        
        // Add patient zero
        infectedPeople.add(graph.vertexSet().stream().findAny().get());
    }
        
    public int getTime()
    {
       return time; 
    }
    
    public int getAllInfections()
    {
        return allInfections;
    }

    public int getCurrentInfections()
    {
        return allInfections-allDeaths-allRecoveries;
    }
    
    public int getAllDeaths()
    {
        return allDeaths;
    }

    public int getAllRecoveries()
    {
        return allRecoveries;
    }
    
    public int[] tick()
    {
        time++;
        
        List<Person> newInfections = new ArrayList<>();
        List<Person> newDeaths = new ArrayList<>();
        List<Person> newRecoveries = new ArrayList<>();
        
        // Take all infected people one by one... 
        for(var infectedPerson :  infectedPeople)
        {
            var pi = infectedPerson.getInfectionTime()+param.latency<time 
                        ? param.piq : param.pi;
            
            var iter = new BreadthFirstIterator<Person, DefaultEdge>(graph, infectedPerson);
            iter.next(); // Drop first element, which is infectedPerson itdelf
            
            // ... and iterate over all acquaintances in order of distance
            // to infect them
            while (iter.hasNext())
            {
                var p = iter.next();
                // The distance from the current infected person
                // 1: direct connection
                // 2: direct conection of a direct connection
                // ...
                var d = iter.getDepth(p); 
                
                // If we are too deep in the graph, skip to the next infected person 
                if(d > pi.length)
                {
                    break;
                }
                
                // If the person is already infected or immune, skip to the next connection
                if(p.isInfected() || p.isImmune())
                {
                    continue;
                }
                
                // Let's make someone sick
                if(rand.nextDouble() <= pi[d-1] * param.pt)
                {
                    p.markInfected(time);
                    newInfections.add(p);
                }
            }

            // Finally, if the person is infected long enough
            // decide on his/her fate
            if(infectedPerson.getInfectionTime()+param.infectionLength<time)
            {
                if(rand.nextDouble()<=param.deathRate)
                {
                    graph.removeVertex(infectedPerson);
                    newDeaths.add(infectedPerson);
                }
                else
                {
                    infectedPerson.clearInfection();
                    infectedPerson.markImmune();
                    newRecoveries.add(infectedPerson);
                }
            }
        }
                        
        infectedPeople.removeAll(newRecoveries);
        infectedPeople.removeAll(newDeaths);
        infectedPeople.addAll(newInfections);
        
        allInfections += newInfections.size();
        allDeaths += newDeaths.size();
        allRecoveries += newRecoveries.size();
        
        return new int[]{newInfections.size(), newRecoveries.size(), newDeaths.size()};
    }
}
