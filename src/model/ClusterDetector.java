package model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.imageio.ImageIO;

import utils.Binarizador;
import utils.ImageUtils;

public class ClusterDetector implements Serializable
{ 
  private static final long serialVersionUID = 1L;

  private int[][] ramsOlhoDireito;
  private int[][] ramsOlhoEsquerdo;
  private int[][] ramsBoca;
  private RedeNeural redeOlho;
  private RedeNeural redeBoca;
  private boolean discretization;
  private int luminanceLevel = 4;
  private int levelA = 8;
  private int levelB = 8;
  private int xOlhoDireito;
  private int yOlhoDireito;
  private int xOlhoEsquerdo;
  private int yOlhoEsquerdo;
  private int yBoca;
  
  private final int LARGURA_JANELA_OLHO  = 25;
  private final int ALTURA_JANELA_OLHO   = 15;
  private final int LARGURA_JANELA_BOCA  = 53;
  private final int ALTURA_JANELA_BOCA   = 24;
  private final int ALTURA_MINIMA_OLHO   = 40;
  private final int ALTURA_MAXIMA_OLHO   = 80;
  private final int ALTURA_MINIMA_BOCA   = 80;
  private final int ALTURA_MAXIMA_BOCA   = 120;
  
  public ClusterDetector(int numeroEntradasRAMs, 
		  boolean discretizacao, int luminanceLevel, int levelA, int levelB)
  {
	this.discretization = discretizacao;
	this.luminanceLevel = luminanceLevel;
	this.levelA = levelA;
	this.levelB = levelB;

	if(discretizacao)
	{
		redeOlho  = new RedeNeural(0, numeroEntradasRAMs, 
				LARGURA_JANELA_OLHO * ALTURA_JANELA_OLHO * 
				(luminanceLevel + levelA + levelB - 3) / numeroEntradasRAMs);
	    redeBoca  = new RedeNeural(0, numeroEntradasRAMs, 
	    		LARGURA_JANELA_BOCA * ALTURA_JANELA_BOCA * 
	    		(luminanceLevel + levelA + levelB - 3) / numeroEntradasRAMs);

	}
	else
	{
		redeOlho  = new RedeNeural(0, numeroEntradasRAMs, 
				LARGURA_JANELA_OLHO  * ALTURA_JANELA_OLHO  / numeroEntradasRAMs);
		redeBoca  = new RedeNeural(0, numeroEntradasRAMs, 
				LARGURA_JANELA_BOCA  * ALTURA_JANELA_BOCA  / numeroEntradasRAMs);

	}
	
  }

  public BufferedImage detectarFaces(BufferedImage imagem) throws IOException
  {
    int menorY;
    int largura;
    int altura;
    
    detectarClusters(imagem);
      
    menorY = yOlhoDireito > yOlhoEsquerdo ? yOlhoEsquerdo : yOlhoDireito;
    largura = xOlhoEsquerdo - xOlhoDireito;
    altura = yBoca - menorY;
   
    System.out.println(largura+3+","+ altura+5+","+ (xOlhoDireito-3)+","+ yOlhoDireito);
    BufferedImage face = ImageUtils.cropImage(imagem, largura+30, altura+50, xOlhoDireito, yOlhoDireito);
     
    return face;
  }
  
  public void detectarClusters(BufferedImage imagem) throws IOException
  {
    this.ramsOlhoDireito = 
    		new int[imagem.getWidth()/2 - LARGURA_JANELA_OLHO] 
    				[ALTURA_MAXIMA_OLHO - ALTURA_MINIMA_OLHO - ALTURA_JANELA_OLHO];
    this.ramsOlhoEsquerdo = 
    		new int[imagem.getWidth()/2 - LARGURA_JANELA_OLHO] 
    				[ALTURA_MAXIMA_OLHO - ALTURA_MINIMA_OLHO - ALTURA_JANELA_OLHO];
    this.ramsBoca = 
    		new int[imagem.getWidth() - LARGURA_JANELA_BOCA] 
    				[ALTURA_MAXIMA_BOCA - ALTURA_MINIMA_BOCA - ALTURA_JANELA_BOCA];
        
    detectCluster(imagem, this.redeOlho, "olhoDireito");
    
    detectCluster(imagem, this.redeOlho, "olhoEsquerdo");
    
    detectCluster(imagem, this.redeBoca, "boca");
  }
  
  public BufferedImage detectCluster(BufferedImage imagem, RedeNeural rede, String identificacaoCluster) throws IOException
  {
    BufferedImage janela;
    List<Boolean> entrada;
    int[][] rams;
    int larguraJanela;
    int alturaJanela;
    int x0;
    int y0;
    
    if(identificacaoCluster.contains("olho"))
    {
      rams = identificacaoCluster.contains("Direito") ? ramsOlhoDireito : ramsOlhoEsquerdo;
      x0 = identificacaoCluster.contains("Esquerdo") ? imagem.getWidth() /2 : 0;
      larguraJanela = LARGURA_JANELA_OLHO;
      alturaJanela = ALTURA_JANELA_OLHO;
      y0 = ALTURA_MINIMA_OLHO;
    }
    else
    {
      rams = ramsBoca;
      larguraJanela = LARGURA_JANELA_BOCA;
	  alturaJanela = ALTURA_JANELA_BOCA;
	  x0 = 0;
	  y0 = ALTURA_MINIMA_BOCA;
    }
    //TODO, better performance
    
    for(int i = 0; i < rams.length; i = i + 2)
    {      
      for(int j = 0; j < rams[i].length; j = j + 2)
      {
        janela = ImageUtils.cropImage(imagem, larguraJanela, alturaJanela, i + x0, j + y0);
        
        if(discretization)
        {
          entrada = Binarizador.discretizar(janela, luminanceLevel, levelA, levelB,"");
        }
        else
        {
          entrada = Binarizador.binarizar(janela);
        }
       
        rams[i][j] = rede.classify(entrada);
      }
    }
    
    int[] cluster = detectBestCluster(rams);
    
    if(identificacaoCluster == "olhoDireito")
    {
      xOlhoDireito = cluster[0] + x0;
      yOlhoDireito = cluster[1] + y0;
    }
    else
    {
      if(identificacaoCluster == "olhoEsquerdo")
      {
        xOlhoEsquerdo = cluster[0] + larguraJanela + x0;
    	yOlhoEsquerdo = cluster[1] + y0;
      }
      else
      {
    	yBoca = cluster[1] + alturaJanela + y0;
      }
    }
    
    return ImageUtils.cropImage(imagem, larguraJanela, alturaJanela, cluster[0] + x0, cluster[1] + y0);
  }
  
  /**
   * Detect cluster with more active neurons.
   * 
   * @param rams
   * @return
   */
  public int[] detectBestCluster(int[][] rams)
  {
    int melhorResultado = 0;
    int x = 0;
    int y = 0;
    
    //TODO better performance
    for(int i = 0; i < rams.length; i++)
    {
      for(int j = 0; j < rams[i].length; j++)
      {
        if(rams[i][j] > melhorResultado)
        {
          melhorResultado = rams[i][j];
          x = i;
          y = j;
        }
      }
    }
    int[] cluster = {x, y};
    
    return cluster;
  }
  
  public void treinarOlhosEBoca(BufferedImage imagemMentalOlho, BufferedImage imagemMentalBoca) throws IOException
  {
    List<Boolean> entrada;
    
    entrada = Binarizador.discretizar(imagemMentalOlho, luminanceLevel, levelA, levelB,"");
    redeOlho.train(entrada);
    
   entrada = Binarizador.discretizar(imagemMentalBoca, luminanceLevel, levelA, levelB,"");
    redeBoca.train(entrada);
  }
  
  public void treinarOlhosEBoca(String imagensOlho, String imagensBoca) throws IOException
  {
    List<Boolean> entrada;
    File[] arquivos = new File(imagensOlho).listFiles();
    BufferedImage imagem;
    
    for(File arquivo:arquivos)
    {
    	imagem = ImageIO.read(arquivo);
        entrada = Binarizador.discretizar(imagem, luminanceLevel, levelA, levelB,"");
        redeOlho.train(entrada);
    }
    
    arquivos = new File(imagensBoca).listFiles();
    
    for(File arquivo:arquivos)
    {
    	imagem = ImageIO.read(arquivo);
        entrada = Binarizador.discretizar(imagem, luminanceLevel, levelA, levelB,"");
        redeBoca.train(entrada);
    } 
 }
  
  public RedeNeural getRedeOlho()
  {
    return redeOlho;
  }

  public void setRedeOlho(RedeNeural redeOlho)
  {
    this.redeOlho = redeOlho;
  }

  public RedeNeural getRedeBoca()
  {
    return redeBoca;
  }

  public void setRedeBoca(RedeNeural redeBoca)
  {
    this.redeBoca = redeBoca;
  }

  public boolean isDiscretizacao()
  {
    return discretization;
  }

  public void setDiscretizacao(boolean discretizacao)
  {
    this.discretization = discretizacao;
  }
  public int getLuminanceLevel()
  {
    return luminanceLevel;
  }

  public void setLuminanceLevel(int luminanceLevel)
  {
    this.luminanceLevel = luminanceLevel;
  }

  public int getLevelA()
  {
    return levelA;
  }

  public void setLevelA(int levelA)
  {
    this.levelA = levelA;
  }

  public int getLevelB()
  {
    return levelB;
  }

  public void setLevelB(int levelB)
  {
    this.levelB = levelB;
  }

}