package model;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RAM implements Serializable
{
  private static final long serialVersionUID = 1L;

  private Map<Integer, Integer> ram;
  
  public Map<Integer, Integer> getRam()
  {
    return ram;
  }

  public RAM()
  {
    ram = new HashMap<Integer, Integer>();
  }
  
  public RAM(int numeroEntradaRAMs)
  {
    ram = new HashMap<Integer, Integer>(numeroEntradaRAMs);
    for(int i=0; i<numeroEntradaRAMs;i++)
    {
    	ram.put(i,0);
    }
  }
  
  public void increment(int position)
  {
	//System.out.println("A posição é: "+position);
    if (!ram.containsKey(position))
    {
      ram.put(position, 0);
      //System.out.println("Pus 0 em "+position);
    }
    
    ram.put(position, ram.get(position) + 1);
    //System.out.println("Pus "+ram.get(position) + 1+ " em "+position);
  }
  
  public Set<Integer> getPositions()
  {
    return ram.keySet();
  }
  
  public int get(int position)
  {
    return ram.get(position);
  }
  
  public boolean containsKey(int position)
  {
    return ram.keySet().contains(position);
  }
}