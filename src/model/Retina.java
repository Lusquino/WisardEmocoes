package model;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Retina implements Serializable
{
  private static final long serialVersionUID = 1L;

  private Map<Integer, Integer> retina;
  
  private OutputStream out;
  
  public Retina(int inputLength)
  {
    retina = new HashMap<Integer, Integer>();
    for(int i = 0; i < inputLength; i++)
    {
      retina.put(i, i);
    }

    Random rnd = new Random();
    for(int i = 0; i < inputLength; i++)
    {
      int randomInd = rnd.nextInt(inputLength);
      
      int temp = retina.get(i);
      retina.put(i, retina.get(randomInd));
      retina.put(randomInd, temp);
    }  
    
    try {
		out = new FileOutputStream(new File("C:/Users/Usuario/Desktop/Mestrado/Dissertação/DatasetMMI/erros.txt"));
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
  
  public List<Boolean> organize(List<Boolean> input) throws IOException
  {
    List<Boolean> organizedInput = new ArrayList<Boolean>();
    
   // System.out.println("Tamanho do input: "+ input.size());
    //System.out.println("Tamanho da retina: "+ retina.keySet().size());
    for (int i = 0; i < retina.keySet().size(); i++)
    {
    	//if(i>=retina.size()){System.out.println("Vai dar caoooooooo");}
    	//out.write((i+"/"+retina.keySet().size()+"/n/n").getBytes());
    	int temp1 = retina.get(i);
    	boolean temp2 = input.get(temp1);
      organizedInput.add(temp2);
      //System.out.println("Adicionei na lista reorganizada: "+ input.get(retina.get(i)));
    }
    
    return organizedInput;
  }
  
  public List<Boolean> reorganize(List<Boolean> inputOrganizado)
  {
    List<Boolean> input = inputOrganizado;
    
    double verdadeiro = 0, falso = 0;
    
    for(int i=0;i<inputOrganizado.size();i++)
    {
 	   if(inputOrganizado.get(i)){verdadeiro++;}
 	   if(!inputOrganizado.get(i)){falso++;}
    }
    System.out.println(verdadeiro+";"+falso);
    
    System.out.println("retina:"+retina.keySet().size()+"; lista:"+inputOrganizado.size());
    for (int i = 0; i < retina.keySet().size(); i++)
    {
      input.set(retina.get(i), inputOrganizado.get(i));
    }
    
    return input; 
  }
  
  public List<Boolean> sort(List<Boolean> input)
  {
    Boolean[] sortedInput = new Boolean[input.size()];
    
    for(int i = 0; i < retina.keySet().size(); i++)
    {
      sortedInput[retina.get(i)] = input.get(i);
    }
    
    return Arrays.asList(sortedInput);
  }
}