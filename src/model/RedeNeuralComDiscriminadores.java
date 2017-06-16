package model;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.List;

public class RedeNeuralComDiscriminadores implements Serializable
{
  private static final long serialVersionUID = 1L;
  private Retina retina;
  private Discriminador[] discriminadores;
  private int[] classificacaoDiscriminadores;
  
  public RedeNeuralComDiscriminadores(int numeroEntradasRAMs, int tamanhoEntrada, int numeroDiscriminadores) throws FileNotFoundException
  {
    retina = new Retina(tamanhoEntrada);
   // System.out.println("Tamanho da entrada: "+ tamanhoEntrada);
    discriminadores = new Discriminador[numeroDiscriminadores];
    classificacaoDiscriminadores = new int[numeroDiscriminadores];
    
    for(int i = 0; i < discriminadores.length; i++)
    {
      discriminadores[i] = new Discriminador(numeroEntradasRAMs, tamanhoEntrada);
    }
  }
  
  public RedeNeuralComDiscriminadores(String pathToParent)
  {
    ObjectInputStream in = null;
    RedeNeuralComDiscriminadores nn = null;

    try 
    {
        FileInputStream fist = new FileInputStream(pathToParent);
        in = new ObjectInputStream(fist);
        nn = (RedeNeuralComDiscriminadores
        		) in.readObject();
        in.close();
    } catch (IOException ex) 
    {
        ex.printStackTrace();
    } catch (ClassNotFoundException ex) 
    {
        ex.printStackTrace();
    }
    
    retina = nn.retina;
    discriminadores = nn.discriminadores;
    classificacaoDiscriminadores = nn.classificacaoDiscriminadores;
  }
  
  public Discriminador[] getDiscriminadores()
  {
    return discriminadores;
  }

  public void treinar(List<Boolean> entrada, int discriminador) throws IOException
  {
    List<Boolean> entradaReorganizada = retina.organize(entrada);
   /* int verdade=0, verdadeReorganizada=0;
    
    if(entrada.size() > entradaReorganizada.size())
    {
    	System.out.println("Já deu ruim");
    }
    
    for(int i=0; i<entrada.size(); i++)
    {
    	if(entrada.get(i))
    	{
    		verdade++;
    	}
    	if(entradaReorganizada.get(i))
    	{
    		verdadeReorganizada++;
    	}
    }
    
    if(verdade != verdadeReorganizada)
    {
    	System.out.println("Me perdi");
    }*/
   // System.out.println(entrada.size());
   // System.out.println(entradaReorganizada.size());
    discriminadores[discriminador].treina(entradaReorganizada);
  }
  
  public int classificar(List<Boolean> entrada, double confianca, int bleaching) throws IOException
  {
    List<Boolean> entradaReorganizada = retina.organize(entrada);
    
    for(int i = 0; i < discriminadores.length; i++)
    {
//    	System.out.println("Classifiquei o discriminador:"+i);
      classificacaoDiscriminadores[i] = 
    		  discriminadores[i].classificar(entradaReorganizada, bleaching);
  //  System.out.println("discriminador "+i+" "+ classificacaoDiscriminadores[i]);
    }
    
    int classe = 0;
        
    //System.out.println("Hora da verdade");
    
    if(confianca > calcularConfianca())
    {
     //System.out.println("Nao esta confiante");
      try {
          classe = classificar(entrada, confianca, bleaching+1);
      } catch (Exception e) {
		// TODO: handle exception
    	  System.out.println(e.getMessage());
      }
    }
    else
    {
      classe = obterClasse(classificacaoDiscriminadores, obterMelhorResultado(classificacaoDiscriminadores));
    }
    
    return classe;
  }
  
  public BufferedImage[] createMentalImage(int bleaching)
  {
    BufferedImage[] imagensMentais = new BufferedImage[discriminadores.length];
    
    for(int i = 0; i < discriminadores.length; i++)
    {
      List<Boolean> entrada = discriminadores[i].precriarImagensMentais(bleaching);
      entrada = retina.reorganize(entrada);
      imagensMentais[i] = discriminadores[i].criarImagensMentais(entrada);
    }
    
    return imagensMentais;
  }
  
  public static int convertToInt(List<Boolean> bloco)
  {
    int valor = 0;
    
    for (int i = 0; i < bloco.size(); i++)
    {
      if (bloco.get(i))
      {
        valor += (int) Math.pow(2, i);
      }
    }
    
    return valor;
  }
  
  public double calcularConfianca()
  {
    int melhorResultado = obterMelhorResultado(classificacaoDiscriminadores);
    int segundoMelhorResultado = obterSegundoMelhorResultado(classificacaoDiscriminadores);
    
    if(melhorResultado == 0)
    {
      return 0;
    }
    return ((double)melhorResultado - (double)segundoMelhorResultado)/(double)melhorResultado;
  }
  
  public int obterClasse(int[] classificacaoDiscriminadores, int valor)
  {
    for(int i = 0; i < classificacaoDiscriminadores.length; i++)
    {
      if(classificacaoDiscriminadores[i] == valor)
      {
        return i;
      }
    }
    return classificacaoDiscriminadores.length-1;
  }
  
  public int obterMelhorResultado(int[] valores)
  {
    int melhorResultado = 0;
    
    for(int i = 0; i < valores.length; i++)
    {
      if(valores[i] > melhorResultado)
      {
        melhorResultado = valores[i];
      }
    }
    
    return melhorResultado;
  }
  
  public int obterSegundoMelhorResultado(int[] valores)
  {
    int[] novosValores = new int[discriminadores.length-1];
    int melhorResultado = obterMelhorResultado(valores);
    int j = 0;
    boolean empate = false;
    
   // System.out.println("Entrei no segundo melhor");
    
    for(int i = 0; i < valores.length; i++)
    {
      if((valores[i] == melhorResultado)&&(empate==false))
      {
        i++;
        empate = true;
      }
      
      if(i < valores.length)
      {
        novosValores[j] = valores[i];
        j++;
      }
    }
    
 //   System.out.println("Vou sair do segundo melhor");

    
    return obterMelhorResultado(novosValores);
  }
  
}