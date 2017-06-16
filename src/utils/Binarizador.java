package utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Binarizador
{
  private static final int THRESHOLD_BINARIZACAO = 127;
  
  public static List<Boolean> binarizar(BufferedImage imagem)
  {
    List<Boolean> binarizacao = new ArrayList<Boolean>();
    
    Color cor;
    double luminanciaMedia = 0;
    
    /*for(int x=0; x<imagem.getWidth(); x++)
    {
      for(int y=0; y<imagem.getHeight(); y++)
      {
        cor = new Color(imagem.getRGB(x, y));
        binarizacao.add((cor.getAlpha() > 123));
        System.out.println("Alpha:"+cor.getAlpha());
      }
    }*/
    
    
    for(int x=0; x<imagem.getWidth(); x++)
    {
      for(int y=0; y<imagem.getHeight(); y++)
      {
        cor = new Color(imagem.getRGB(x, y));
        luminanciaMedia += obterLuminancia(cor);
      }
    }
    
    luminanciaMedia /= imagem.getWidth() * imagem.getHeight();
    
    for(int x=0; x<imagem.getWidth(); x++)
    {
      for(int y=0; y<imagem.getHeight(); y++)
      {
        cor = new Color(imagem.getRGB(x, y));
        
        double luminancia = obterLuminancia(cor);
        //System.out.println("luminanciaMedia:"+1.5*luminanciaMedia+"; luminancia:"+luminancia);
        
        binarizacao.add(luminancia >= 1.5*luminanciaMedia);
      }
    }
    
   /* for(int i = 0; i < binarizacao.size(); i++){
    	System.out.println(binarizacao.get(i)+";");
    }
    System.out.println("test");*/
    
    return binarizacao;
  }
  
  public static BufferedImage colorirBinario(BufferedImage imagem)
  {
	BufferedImage imagemBinarizada = new BufferedImage(imagem.getWidth(), imagem.getHeight(), imagem.getType());
    
    Color cor;
    double luminanciaMedia = 0;
        
    for(int x=0; x<imagem.getWidth(); x++)
    {
      for(int y=0; y<imagem.getHeight(); y++)
      {
        cor = new Color(imagem.getRGB(x, y));
        luminanciaMedia += obterLuminancia(cor);
      }
    }
    
    luminanciaMedia /= imagem.getWidth() * imagem.getHeight();
    
    for(int x=0; x<imagem.getWidth(); x++)
    {
      for(int y=0; y<imagem.getHeight(); y++)
      {
        cor = new Color(imagem.getRGB(x, y));
        
        double luminancia = obterLuminancia(cor);
        
        if(luminancia >= 0.7*luminanciaMedia)
        {
        	imagemBinarizada.setRGB(x, y, Color.BLACK.getRGB());
        }
        else
        {
        	imagemBinarizada.setRGB(x, y, Color.WHITE.getRGB());
        }
      }
    }
        
    return imagemBinarizada;
  }
  
  public static List<Boolean> discretizar(BufferedImage imagem, int nivelLuminancia, int nivelA, int nivelB, String foto)
  {
    List<Boolean> discretizacao = new ArrayList<Boolean>();
    
    BufferedImage imagemDiscretizada = ImageUtils.discretizarCores("C:/Users/Usuario/Desktop/Mestrado/Dissertação/DataSetMMI/", foto, imagem, nivelLuminancia, nivelA, nivelB);
    
    for(int x=0; x<imagemDiscretizada.getWidth(); x++)
    {
      for(int y=0; y<imagemDiscretizada.getHeight(); y++)
      {
        Color cor = new Color(imagem.getRGB(x, y));
        int[] niveisLab = ImageUtils.getLABLevels(cor, nivelLuminancia, nivelA, nivelB);
        
        for (int i = 0; i < nivelLuminancia - 1; i++)
        {
          discretizacao.add(niveisLab[0] > i);
        }
        for (int i = 0; i < nivelA - 1; i++)
        {
          discretizacao.add(niveisLab[1] > i);
        }
        for (int i = 0; i < nivelB - 1; i++)
        {
          discretizacao.add(niveisLab[2] > i);
        }
      }
    }
    
    return discretizacao;
  }
 
  
  public static List<Boolean> discretizar(BufferedImage imagem, int nivelLuminancia, int nivelA, int nivelB)
  {
    List<Boolean> discretizacao = new ArrayList<Boolean>();
      
    for(int x=0; x<imagem.getWidth(); x++)
    {
      for(int y=0; y<imagem.getHeight(); y++)
      {
        Color cor = new Color(imagem.getRGB(x, y));
        
        int[] niveisLab = ImageUtils.getLABLevels(cor, nivelLuminancia, nivelA, nivelB);
        
        for (int i = 0; i < nivelLuminancia - 1; i++)
        {
          discretizacao.add(niveisLab[0] > i);
        }
        for (int i = 0; i < nivelA - 1; i++)
        {
          discretizacao.add(niveisLab[1] > i);
        }
        for (int i = 0; i < nivelB - 1; i++)
        {
          discretizacao.add(niveisLab[2] > i);
        }
      }
    }
    
    return discretizacao;
  }
  
  public static BufferedImage colorirDiscreto(BufferedImage imagem, int nivelLuminancia, int nivelA, int nivelB)
  {    
    return ImageUtils.discretizarCores("","", imagem, nivelLuminancia, nivelA, nivelB);
  }
  
  private static double obterLuminancia(Color cor)
  {
    return 0.2126 * cor.getRed() + 0.7152 * cor.getGreen() + 0.0722 * cor.getBlue();
  }
  
  public static List<Boolean> binarizarEscalaDeCinza(BufferedImage imagem)
  {
    List<Boolean> binarizacao = new ArrayList<Boolean>();
    
    Color cor;
    int r,g,b;
    
    for(int x=0; x<imagem.getWidth(); x++)
    {
      for(int y=0; y<imagem.getHeight(); y++)
      {
        cor = new Color(imagem.getRGB(x, y));
        r = cor.getRed();
        g = cor.getGreen();
        b = cor.getBlue();
        
        binarizacao.add((r + g + b) / 3 >= THRESHOLD_BINARIZACAO);
      }
    }
    
    return binarizacao;
  }
  
  public static BufferedImage discretizarCores(BufferedImage imagem, int divisoes)
  {
	  BufferedImage imagemDiscretizada = new BufferedImage(imagem.getWidth(), imagem.getHeight(), imagem.getType());
	  double corMax = 0;
	  double[] espacos = new double[divisoes];
	  Color cor;
	  double corMedia;
	  double luminanciaMedia = 0;
	 
	  
	  for(int y=0; y < imagem.getHeight(); y++)
	  {
		  for(int x=0; x < imagem.getWidth(); x++)
		  {
			  cor = new Color(imagem.getRGB(x, y));
			  
			  //System.out.println("imagemRGB:"+imagem.getRGB(x, y));
			  
			  corMedia = cor.getBlue()+cor.getRed()+cor.getGreen()/3;
		      luminanciaMedia += obterLuminancia(cor);
			  //System.out.println("corMedia:"+ corMedia);
			  
			  if(corMedia>corMax)
			  {
				  corMax = corMedia;
			  }
		  }
	  }
	  
	  System.out.println("corMax:" + corMax);
	  
	  for(int i =0; i < espacos.length; i++)
	  {
		  espacos[i] = (i+1)*corMax/divisoes;
//		  System.out.println("espaco"+i+":"+espacos[i]);
	  }
	  
	  luminanciaMedia /= imagem.getHeight()*imagem.getWidth();
	  	  
	  for(int y=0; y < imagem.getHeight(); y++)
	  {
		  for(int x=0; x < imagem.getWidth(); x++)
		  {
			  cor = new Color(imagem.getRGB(x, y));
			  corMedia = cor.getBlue()+cor.getRed()+cor.getGreen()/3;
			  
			  double luminancia = obterLuminancia(cor);

			  if(luminancia >= 0.7 * luminanciaMedia)
			  {
				  imagemDiscretizada.setRGB(x, y, Color.BLACK.getRGB());
			  }
			  else
			  {
				  for(int i =0; i < espacos.length; i++)
				  {
					  if(corMedia<espacos[i])
					  {
						  imagemDiscretizada.setRGB(x, y, (int) espacos[i]);
						  break;
					  }
				  }
			  }
		  }
	  }
	  
	  return imagemDiscretizada;
  }
  
  public static List<Boolean> obterDiscretizado(BufferedImage imagem)
  {
	    Color cor;
	    double luminanciaMedia = 0;
	    int corMedia = 0;
	    ArrayList<Boolean> discretizacao = new ArrayList<Boolean>();
	    
	        
	    for(int x=0; x<imagem.getWidth(); x++)
	    {
	      for(int y=0; y<imagem.getHeight(); y++)
	      {
	        cor = new Color(imagem.getRGB(x, y));
	        luminanciaMedia += obterLuminancia(cor);
	        corMedia += cor.getRGB();
	      }
	    }
	    
	    luminanciaMedia /= imagem.getWidth() * imagem.getHeight();
	    corMedia /= imagem.getWidth() * imagem.getHeight();
	    
	    for(int x=0; x<imagem.getWidth(); x++)
	    {
	      for(int y=0; y<imagem.getHeight(); y++)
	      {
	        cor = new Color(imagem.getRGB(x, y));
	        double luminancia = obterLuminancia(cor);
	        discretizacao.add(cor.getRGB()>=corMedia);
	        discretizacao.add(luminancia>=luminanciaMedia);
	      }
	    }
	        
	    return discretizacao;
  }
  
  public static BufferedImage clusterizar(BufferedImage imagem)
  {
	  BufferedImage imagemClusterizada = new BufferedImage(imagem.getWidth(), imagem.getHeight(), imagem.getType());
	  double luminanciaMedia = 0;	 

	  for(int y=0; y < imagem.getHeight(); y++)
	  {
		  for(int x=0; x < imagem.getWidth(); x++)
		  {
			  Color cor = new Color(imagem.getRGB(x, y));
		      luminanciaMedia += obterLuminancia(cor);
		  }
	  }
	  luminanciaMedia /= imagem.getWidth()*imagem.getHeight();
	  
	  for(int x=0; x<imagem.getWidth(); x++)
	  {
		  for(int y=0; y<imagem.getHeight(); y++)
		  {
		     Color cor = new Color(imagem.getRGB(x, y));
		     double luminancia = obterLuminancia(cor);
		     
		     if(luminancia >= 0.7*luminanciaMedia)
		     {
		    	 imagemClusterizada.setRGB(x, y, Color.BLACK.getRGB());
		     }
		     else
		     {
		    	 if((cor.getBlue()>cor.getGreen())&&(cor.getBlue()>cor.getRed()))
		    	 {
		    		 imagemClusterizada.setRGB(x, y, Color.BLUE.getRGB());
		    	 }
		    	 if((cor.getRed()>cor.getGreen())&&(cor.getRed()>cor.getBlue()))
				 {
		    		 imagemClusterizada.setRGB(x, y, Color.RED.getRGB());
			   	 }
		    	 if((cor.getGreen()>cor.getBlue())&&(cor.getGreen()>cor.getRed()))
				 {
		    		 imagemClusterizada.setRGB(x, y, Color.GREEN.getRGB());
		    	 }
		    	 if((cor.getBlue()>cor.getGreen())&&(cor.getBlue()==cor.getRed()))
				 {
			   		 imagemClusterizada.setRGB(x, y, Color.CYAN.getRGB());
				 }
		    	 if((cor.getBlue()>cor.getRed())&&(cor.getBlue()==cor.getGreen()))
				 {
					 imagemClusterizada.setRGB(x, y, Color.MAGENTA.getRGB());
			   	 }
		    	 if((cor.getGreen()>cor.getBlue())&&(cor.getGreen()==cor.getRed()))
		    	 {
		    		 imagemClusterizada.setRGB(x, y, Color.YELLOW.getRGB());
				 }
		    	 if((cor.getBlue()==cor.getGreen())&&(cor.getBlue()==cor.getRed()))
			   	 {
		    		 imagemClusterizada.setRGB(x, y, Color.WHITE.getRGB());
		    	 }
		     }
		  }
	  }
	  return imagemClusterizada;
  }
  
  public static List<Boolean> transformarClusters(BufferedImage imagem)
  {
	  List<Boolean> entrada = new ArrayList<Boolean>();
	  
	  for(int x=0; x<imagem.getWidth(); x++)
	  {
		  for(int y=0; y<imagem.getHeight(); y++)
		  {
			  int cor = imagem.getRGB(x, y);
			  
			  if(cor == Color.BLACK.getRGB())
			  {
				  entrada.add(false); entrada.add(false); entrada.add(false);
			  }
			  if(cor == Color.RED.getRGB())
			  {
				  entrada.add(true); entrada.add(false); entrada.add(false);
			  }
			  if(cor == Color.BLUE.getRGB())
			  {
				  entrada.add(false); entrada.add(true); entrada.add(false);
			  }
			  if(cor == Color.GREEN.getRGB())
			  {
				  entrada.add(false); entrada.add(false); entrada.add(true);
			  }
			  if(cor == Color.CYAN.getRGB())
			  {
				  entrada.add(true); entrada.add(true); entrada.add(false);
			  }
			  if(cor == Color.YELLOW.getRGB())
			  {
				  entrada.add(true); entrada.add(false); entrada.add(true);
			  }
			  if(cor == Color.MAGENTA.getRGB())
			  {
				  entrada.add(false); entrada.add(true); entrada.add(true);
			  }
			  if(cor == Color.WHITE.getRGB())
			  {
				  entrada.add(true); entrada.add(true); entrada.add(true);
			  }  
		  }
	  }
	  
	  return entrada;
  }
}