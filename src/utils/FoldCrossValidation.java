package utils;

import model.*;
import utils.Binarizador;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

public class FoldCrossValidation      
{
  private static int larguraFoto = 120;
  private static int alturaFoto = 160;
  
  private static final int NIVEL_LUMINANCIA = 4;
  private static final int NIVEL_A = 8;
  private static final int NIVEL_B = 8;
  private static final String pasta = "C:/Users/Usuario/Desktop/Mestrado/Dissertação/DataSetMMI/recortadas/todasBinarizadas/";
  //private static final String pasta = "C:/Users/Usuario/Desktop/Mestrado/Dissertação/DataSetMMI/recortadas/todasDiscretizadas/";
    
  private static int quantidadeBlocos = 10;
  private static int quantidadeFotosBloco = 26;
  
  
  private static File[] arquivos;
  
  private static int[][] blocos;
  private static int[][] classificadosErrados = new int[7][7];
  private static int[] classificadosCorretos = new int[7];
  
  private static List<Integer> excluidos = new ArrayList<Integer>();
  
  public static void validacaoSimples(int numeroEntradasRAM, int tamanhoEntradaRAMs, double confianca, int bleaching, OutputStream out) throws Exception
  { 
	 for(int i = 0; i<7; i++)
	  {
		  for(int j = 0; j<7; j++)
		  {
			  classificadosErrados[i][j] = 0;
		  }
	  }

	  BufferedImage imagem;
	  List<Boolean> entrada = new ArrayList<Boolean>();
	  
	  arquivos = new File(pasta).listFiles();
	  
	  blocos = new int[quantidadeBlocos][quantidadeFotosBloco];
	  
	  for(int i=0; i < quantidadeBlocos; i++)
	  {
		  for(int j=0; j < quantidadeFotosBloco; j++)
		  {
			  sortearImagem(i, j);
		  }
	  }
	  
	  RedeNeuralComDiscriminadores rede;
	  int emocao;
	  double erros = 0;
	  double errosTotal = 0;
	  int classificacao;
	  
	  for(int i=0; i < quantidadeBlocos; i++)
	  {
		  rede = new RedeNeuralComDiscriminadores(numeroEntradasRAM, tamanhoEntradaRAMs , 7);

		  for(int j=0; j < quantidadeBlocos; j++)
		  {
			  if(i!=j)
			  {
				//  System.out.println("Estou treinando:"+i+";"+j);

				  for(int k=0; k < quantidadeFotosBloco; k++)
				  {
					  imagem = obterImagem(j, k);
					  imagem = ImageUtils.redimensionarImagem(imagem, larguraFoto, alturaFoto);
					  emocao = obterEmocao(j, k);
					  //entrada = Binarizador.binarizar(imagem);
					  //entrada = Binarizador.discretizar(imagem, NIVEL_LUMINANCIA, NIVEL_A, NIVEL_B);
					  //entrada = Binarizador.discretizar(imagem);
		
					  
					  entrada = geradorBooleano(imagem);
					  //entrada = Binarizador.transformarClusters(imagem);
					  rede.treinar(entrada, emocao);
				  }
			  }
		  }
		  
		  for(int j=0; j < quantidadeFotosBloco; j++)
		  {
//			  System.out.println("Estou classificando:" + i + ";" + j);

			  imagem = obterImagem(i, j);
			  imagem = ImageUtils.redimensionarImagem(imagem, larguraFoto, alturaFoto);
			  //entrada = Binarizador.obterDiscretizado(imagem);
			  //entrada = Binarizador.binarizar(imagem);
			  entrada = geradorBooleano(imagem);
			  //entrada = Binarizador.discretizar(imagem, NIVEL_LUMINANCIA, NIVEL_A, NIVEL_B);
			  //entrada = Binarizador.transformarClusters(imagem);
			  
			  //System.out.println("Estou aqui 1");
			  
			  
			//  out.write(("Vou classificar uma foto").getBytes());
			  classificacao = rede.classificar(entrada, confianca, bleaching);
			  
			  //System.out.println("Estou aqui 2");
			  
			  //System.out.println("Classificou:"+ classificacao + "; classificado como:" + obterEmocao(i, j) +
				//	  "; " + arquivos[blocos[i][j]].getName());
			  

			  if(classificacao != obterEmocao(i, j))
			  {
				  erros++;
				  classificadosErrados[obterEmocao(i, j)][classificacao]++;
			  }
			  else
			  {
				  classificadosCorretos[classificacao]++;
			  }
		  }
		  
		  //System.out.println("Bloco: "+i+" - erros:"+ (erros*100/quantidadeFotosBloco)+"%");
		  errosTotal += erros;
		  erros = 0;
	  }
	  
	  //System.out.println("Porcentagem total de erros:" + errosTotal/quantidadeFotosBloco*quantidadeBlocos);
	  out.write(("Porcentagem total de erros:" + errosTotal/quantidadeFotosBloco*quantidadeBlocos+"\n\n").getBytes());
	  
	  for(int i = 0; i<7; i++)
	  {
		//  System.out.println("Classificados errados: "+ emocaoReversa(i)+" ");
		  
		  for(int j = 0; j<7; j++)
		  {
			  if(i==j){out.write((classificadosCorretos[i] + " ").getBytes());}
			  else{out.write((classificadosErrados[i][j] + "  ").getBytes());}
			 // System.out.println("Achou que era" + emocaoReversa(j) + ":" + classificadosErrados[i][j] + "/" + quantidadeBlocos*quantidadeBlocos + ";");
		  }
		 out.write(("\n").getBytes());
	  }
	  
  }

  
  public static void validacaoDiscretizado(int numeroEntradasRAM, int tamanhoEntradaRAMs, double confianca, int bleaching, OutputStream out) throws Exception
  { 
	 /* for(int i = 0; i<7; i++)
	  {
		  for(int j = 0; j<7; j++)
		  {
			  classificadosErrados[i][j] = 0;
		  }
	  }*/

	  BufferedImage imagem;
	  List<Boolean> entrada = new ArrayList<Boolean>();
	  
	  arquivos = new File(pasta).listFiles();
	  
	  blocos = new int[quantidadeBlocos][quantidadeFotosBloco];
	  
	  for(int i=0; i < quantidadeBlocos; i++)
	  {
		  for(int j=0; j < quantidadeFotosBloco; j++)
		  {
			  sortearImagem(i, j);
		  }
	  }
	  
	  RedeNeuralComDiscriminadores rede;
	  int emocao;
	  double erros = 0;
	  double errosTotal = 0;
	  int classificacao;
	  
	  for(int i=0; i < quantidadeBlocos; i++)
	  {
		  rede = new RedeNeuralComDiscriminadores(numeroEntradasRAM, tamanhoEntradaRAMs , 7);

		  for(int j=0; j < quantidadeBlocos; j++)
		  {
			  if(i!=j)
			  {
				  for(int k=0; k < quantidadeFotosBloco; k++)
				  {
					  imagem = obterImagem(j, k);
					  imagem = ImageUtils.redimensionarImagem(imagem, larguraFoto, alturaFoto);
					  emocao = obterEmocao(j, k);
					  entrada = Binarizador.discretizar(imagem, NIVEL_LUMINANCIA, NIVEL_A, NIVEL_B);
					  rede.treinar(entrada, emocao);
				  }
			  }
		  }
		  
		  for(int j=0; j < quantidadeFotosBloco; j++)
		  {
			  imagem = obterImagem(i, j);
			  imagem = ImageUtils.redimensionarImagem(imagem, larguraFoto, alturaFoto);

			  entrada = Binarizador.discretizar(imagem, NIVEL_LUMINANCIA, NIVEL_A, NIVEL_B);
			 
			  classificacao = rede.classificar(entrada, confianca, bleaching);
			  
			  if(classificacao != obterEmocao(i, j))
			  {
				  erros++;
				//  classificadosErrados[obterEmocao(i, j)][classificacao]++;
			  }
		  }  
		  errosTotal += erros;
		  erros = 0;
	  }	  
	  out.write(("Porcentagem total de erros:" + errosTotal/quantidadeFotosBloco*quantidadeBlocos+"\n\n").getBytes()); 
  }
  public static void sortearImagem(int i, int j)
  {
	  Random rand = new Random();
	  int foto = rand.nextInt(quantidadeBlocos*quantidadeFotosBloco);
	  if(!excluidos.contains(foto))
	  {
		  blocos[i][j] = foto;
		 // System.out.println(foto+"/n");
		  excluidos.add(foto);
	  }
	  else
	  {
		  sortearImagem(i, j);
	  }
  }
  
  public static BufferedImage obterImagem(int i, int j) throws IOException
  {
	  return ImageIO.read(arquivos[blocos[i][j]]);
  }
  
  public static int obterEmocao(int i, int j)
  {
	  String nome = arquivos[blocos[i][j]].getName();
	  
	  if(nome.contains("eutro"))
	  {
		  return 0;
	  }
	  if(nome.contains("eliz"))
	  {
		  return 1;
	  }
	  if(nome.contains("riste"))
	  {
		  return 2;
	  }
	  if(nome.contains("edo"))
	  {
		  return 3;
	  }
	  if(nome.contains("aiva"))
	  {
		  return 4;
	  }
	  if(nome.contains("epulsa"))
	  {
		  return 5;
	  }
	  if(nome.contains("urpresa"))
	  {
		  return 6;
	  }
	  return 0;
  }
  
  public static String emocaoReversa(int i)
  {
	  if(i == 0)
	  {
		  return "Neutro";
	  }
	  if(i == 1)
	  {
		  return "Feliz";
	  }
	  if(i == 2)
	  {
		  return "Triste";
	  }
	  if(i == 3)
	  {
		  return "Medo";
	  }
	  if(i == 4)
	  {
		  return "Raiva";
	  }
	  if(i == 5)
	  {
		  return "Repulsa";
	  }
	  if(i == 6)
	  {
		  return "Surpresa";
	  }
	  return "Neutro";
  }
    
  public static ImageIndex[][] separaIndicesEmBlocos(ImageIndex[] indices)
  {
    int tamanhoBloco = indices.length / quantidadeBlocos;
    int quantidadeFotosExcedentes = indices.length % quantidadeBlocos;
    
    ImageIndex[][] blocos = new ImageIndex[quantidadeBlocos][];
    int indicesEmBloco = 0;
    
    for(int i = 0; i < quantidadeBlocos; i++)
    {
      int tamanho = tamanhoBloco;
      
      if (quantidadeFotosExcedentes > 0)
      {
        tamanho++;
        quantidadeFotosExcedentes--;
      }
      
      blocos[i] = new ImageIndex[tamanho];
      
      for (int j = 0; j < tamanho; j++)
      {
        blocos[i][j] = indices[indicesEmBloco];
        indicesEmBloco++;
      }
    }
    
    return blocos;
  }
	  
  public static int obtemMaiorValor(List<Integer> lista)
  {
    int maiorValor = lista.get(0);
	  
	for(int valor : lista)
	{
	  if(valor > maiorValor)
	  {
	    maiorValor = valor;
	  }
	}
	  
	return maiorValor;
  }
  
  public static int obtemMenorValor(List<Integer> lista)
  {
    int menorValor = lista.get(0);
	  
	for(int valor : lista)
	{
	  if(valor < menorValor)
	  {
	    menorValor = valor;
	  }
    }
	  
	return menorValor;
  }
  
  public static List<Boolean> geradorBooleano(BufferedImage imagem)
  {
	  List<Boolean> entrada = new ArrayList<Boolean>();
	  
	  for(int i = 0; i < imagem.getWidth(); i++)
	  {
		  for(int j = 0; j < imagem.getHeight(); j++)
		  {
			  entrada.add(imagem.getRGB(i, j) == Color.WHITE.getRGB());
		  }
	  }
	  
	  return entrada;
  }
}