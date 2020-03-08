package virus;

public class Parameter {
    // Number of people
    public int n = 100000;
    // Direct connections: avarage number of people one person might interact 
    // with per tick e.g. family, friends, colleagues
    public int k = 50;
    // Rewiring rate: 0.001 to 0.1
    public double pr = 0.001;
    // Probability of interaction per distance
    // First: direct connections
    // Second: direct connections of direct connections
    // ...
    public double[] pi = {0.1,0.001,0.0001};
    // Probability of interaction per distance after quarantine
    public double[] piq = {0.0001};
    // Probability of transmission per interaction
    public double pt = 0.1;
    // The length of an infection (nr ticks). After that the patient dies or
    // recovers and becomes immune.
    public int infectionLength = 10;
    // Latency of the virus (nr ticks). After that first symptomps appear
    // and the infectod person is quarantined
    public int latency = 5;
    // Death rate
    public double deathRate = 0.04;
}
