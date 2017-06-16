package model;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Discriminador implements Serializable
{
  private static final long serialVersionUID = 1L;

  private RAM[] rams;
  private int numeroEntradasRAMs;
  private int tamanhoEntrada;
  
  public Discriminador(int numeroEntradasRAMs, int tamanhoEntrada)
  {
    this.numeroEntradasRAMs = numeroEntradasRAMs;
    this.tamanhoEntrada = tamanhoEntrada;
    inicializarRAMs();
  }
  
  public void treina(List<Boolean> entradaReorganizada)
  {
    int posicaoAtual = 0;
    
    for(int i=0; i<rams.length; i++)
    {
      List<Boolean> bloco = new ArrayList<Boolean>();
      
      for (int j = posicaoAtual; j < Math.min(posicaoAtual + numeroEntradasRAMs, entradaReorganizada.size()); j++)
      {
        bloco.add(entradaReorganizada.get(j));
      }      
      posicaoAtual += numeroEntradasRAMs;
      
      int valorEntrada = RedeNeuralComDiscriminadores.convertToInt(bloco);
      
      rams[i].increment(valorEntrada);
    }
  }
  
  public int classificar(List<Boolean> entradaReorganizada, int bleaching)
  {
    int saida = 0;
    
    int posicaoAtual = 0;
    for(int i = 0; i < rams.length; i++)
    {
      List<Boolean> bloco = new ArrayList<Boolean>();
      
      //System.out.println("Classificando discriminadores:"+i+"/"+rams.length);
      
      for (int j = posicaoAtual; j < Math.min(posicaoAtual + numeroEntradasRAMs, entradaReorganizada.size()); j++)
      {
    	//System.out.println("No "+j+"loop");
        bloco.add(entradaReorganizada.get(j));
      }      
      //System.out.println("Passou do laço");
      posicaoAtual += numeroEntradasRAMs;
            
      int valorEntrada = RedeNeuralComDiscriminadores.convertToInt(bloco);
  
      if (rams[i].containsKey(valorEntrada))
      {
        int saidaRAM = rams[i].get(valorEntrada);
        
        if (saidaRAM >= bleaching)
        {
          saida++;
        }
      }
    }
    
    return saida;
  }
  
  private void inicializarRAMs()
  {
    int numeroRAMs = (int) Math.ceil((double)tamanhoEntrada/(double)numeroEntradasRAMs);
    rams = new RAM[numeroRAMs];
    for (int i = 0; i < numeroRAMs; i++)
    {
      rams[i] = new RAM(numeroEntradasRAMs);
    }
  }
   
  public List<Boolean> precriarImagensMentais(int bleaching)
  {
	  List<Boolean> entrada = new ArrayList<Boolean>();
	  
	    for(int i=0; i<rams.length; i++)
	    {
	    	for(int j=0; j < rams[i].getRam().size(); j++)
	    	{
	    		entrada.add(rams[i].get(j)>bleaching);
	    		//System.out.println("ram "+ i +"na posição"+ j + ":" + rams[i].get(j));
	    	}
	    }
	    
	  //  System.out.println("acerto:"+acerto+"/"+rams.length*rams[0].getRam().size());
	    
	    //System.out.println("quantidade de rams:"+rams.length+";"+"tamanho das rams:"+rams[0].getRam().size()+";"+rams.length*rams[0].getRam().size());
	    
	    return entrada;
  }
  
  public BufferedImage criarImagensMentais(List<Boolean> entrada)
  {
    //BufferedImage imagemMental = new BufferedImage(ImageSetup.WIDTH_EMOTION, ImageSetup.HEIGHT_EMOTION, BufferedImage.TYPE_BYTE_BINARY);
    BufferedImage imagemMental = new BufferedImage(120, 160, BufferedImage.TYPE_BYTE_BINARY);
     
    for(int x=0; x<imagemMental.getWidth(); x++)
    {
      for(int y=0; y<imagemMental.getHeight(); y++)
      {
        Color cor = null;
        
        if((y*imagemMental.getWidth() + x)>=entrada.size()){break;}
        
        if (entrada.get(y*imagemMental.getWidth() + x))
        {
          cor = Color.WHITE;
        }
        else
        {
          cor = Color.BLACK;
        }
        
        imagemMental.setRGB(x, y, cor.getRGB());
      }
    }
    
    return imagemMental;
  }
  
  public List<Boolean> gerarInput()
  {
    Boolean[] entrada = new Boolean[tamanhoEntrada];
    
    Random random = new Random();
    int posicaoAtual = 0;

    for(int i = 0; i < rams.length; i++)
    {
      Map<Integer, Integer> ram = rams[i].getRam();
      int numeroDeAcessos = 0;
      
      for(Integer valor: ram.values())
      {
        numeroDeAcessos += valor;
      }
      int randomNumAc = random.nextInt(numeroDeAcessos);
      
      System.out.println("Numero de acessos:"+randomNumAc);
      System.out.println("tamanho da ram:"+ram.keySet().size());
      
      int chaveEscolhida = -1;
      for(Integer chave : ram.keySet())
      {
        randomNumAc -= ram.get(chave);
        
        System.out.println("chave:"+chave);
        
        if(randomNumAc<0)
        {
          chaveEscolhida = chave;
          break;
        }
      }
      
      String listaBool = Integer.toBinaryString(chaveEscolhida);
      
      System.out.println(listaBool+"chave:"+chaveEscolhida);
      
      while (listaBool.length() < Math.min(numeroEntradasRAMs, entrada.length - posicaoAtual))
      {
        listaBool = "0" + listaBool;
      }
      System.out.println(listaBool);
      
      for(int j = listaBool.length() - 1; j > -1; j--)
      {
        if (listaBool.charAt(j)=='1')
        {
          entrada[posicaoAtual + listaBool.length() - j - 1] = true;
          //System.out.println("Na moral");
        }
        else
        {
          entrada[posicaoAtual + listaBool.length() - j - 1] = false;          
          //System.out.println("Cao");
        }
      }
      posicaoAtual += listaBool.length();
    }
      
    return Arrays.asList(entrada);
  }
}