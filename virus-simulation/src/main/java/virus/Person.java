package virus;

public class Person {
    private int infected = -1;   
    private boolean immune = false;   
    
    public boolean isInfected()
    {
        return infected != -1;
    }
    
    public boolean isImmune()
    {
        return immune;
    }
    
    public int getInfectionTime()
    {
        return infected;
    }
    
    public void clearInfection()
    {
        this.infected = -1;
    }

    public void markImmune()
    {
        this.immune = true;
    }
    
    public void markInfected(int time)
    {
        this.infected = time;
    }
}
