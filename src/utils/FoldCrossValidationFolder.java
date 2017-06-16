package utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.*;
import utils.Binarizador;
import utils.ImageUtils;

public class FoldCrossValidationFolder    
{
  private static int larguraFoto = 120;
  private static int alturaFoto = 160;
  private static final int NIVEL_LUMINANCIA = 4;
  private static final int NIVEL_A = 8;
  private static final int NIVEL_B = 8;
  
 /* public static ScoreResults[] validacaoCruzadaComUmDiscriminadorBinarizada(
		  String folder, int bleaching, int numeroEntradasRAMs)
  {
    RedeNeural[] redes = new RedeNeural[20];
    ScoreResults[] resultados = new ScoreResults[20];
    int tamanhoEntrada = larguraFoto * alturaFoto;
	  
	for(int i = 0; i < redes.length; i++)
	{
	  redes[i] = new RedeNeural(bleaching, numeroEntradasRAMs, tamanhoEntrada);
	}
	
	for(int i = 0; i < redes.length; i++)
	{
	  resultados[i] = new ScoreResults();
	}
	  
	for(int i = 0; i < redes.length; i++)
	{
	  for(int j = 1; j <= redes.length; j++)
      {
		if((i == j - 1)||(i == j)||((i == redes.length - 1)&&(j == 1)))
		{
		  continue;
		}
			  
		String bloco = new String(folder + "bloco " + j + "/");
		File[] arquivos = new File(bloco).listFiles();
		BufferedImage imagem;
		List<Boolean> entrada;
		 
		for(File arquivo: arquivos)
		{
		  if(arquivo.getName().contains("Thumbs"))
		  {
		    continue;
		  }
		  
		  imagem = ImageUtils.redimensionarImagem(
				  ImageUtils.loadImage(bloco + arquivo.getName()),
				  larguraFoto,
				  alturaFoto);
		  entrada = Binarizador.binarizar(imagem);
		  redes[i].train(entrada);
		}
	  }
    }
	
	for(int i = 0; i < redes.length; i++)
	{
      ArrayList<Integer> resultadosPadrao = new ArrayList<Integer>();
	  ArrayList<Integer> resultadosForaPadrao = new ArrayList<Integer>();
	  
	  for(int j = 1; j <= redes.length; j++)
	  {
		if(i == j-1)
		{
		  String bloco = new String(folder + "bloco " + j + "/");
		  File[] arquivos = new File(bloco).listFiles();
		  BufferedImage imagem;
		  List<Boolean> entrada;
		  int classificacao;
			 
		  for(File arquivo: arquivos)
		  {
			if(arquivo.getName().contains("Thumbs"))
			{
		      continue;
			}
			  
			imagem = ImageUtils.redimensionarImagem(
					ImageUtils.loadImage(bloco + arquivo.getName()),
					larguraFoto,
					alturaFoto);
			
			entrada = Binarizador.binarizar(imagem);
			
			classificacao = redes[i].classify(entrada);
			
			if(arquivo.getName().contains("padrao"))
			{
			  resultadosPadrao.add(classificacao);
			}
			else
			{
			  resultadosForaPadrao.add(classificacao);
			}
		  }
		}
	  }
	  redes[i].setThresholdInferior(obtemMaiorValor(resultadosForaPadrao));
	  redes[i].setThresholdSuperior(obtemMenorValor(resultadosPadrao));
		
	  if(redes[i].getThresholdSuperior() <= redes[i].getThresholdInferior())
	  {
	    int thresholdSuperiorAnterior = redes[i].getThresholdSuperior();
	    redes[i].setThresholdSuperior(redes[i].getThresholdInferior());
	    redes[i].setThresholdInferior(thresholdSuperiorAnterior);
	  }
	}
	
	for(int i = 0; i < redes.length; i++)
	{
	  for(int j = 1; j <= redes.length; j++)
	  {
	    if((i == j+1)||((i == redes.length-1)&&(j == 1)))
	    {
	      String bloco = new String(folder + "bloco " + j + "/");
		  File[] arquivos = new File(bloco).listFiles();
		  BufferedImage imagem;
		  List<Boolean> entrada;
		  int classificacao;
		  
		  for(File arquivo : arquivos)
		  {
			if(arquivo.getName().contains("Thumbs"))
			{
			  continue;
			}
			
			imagem = ImageUtils.redimensionarImagem(ImageUtils.loadImage(bloco + arquivo.getName()),larguraFoto,alturaFoto);
			entrada = Binarizador.binarizar(imagem);
			classificacao = redes[i].classify(entrada);
			
			if(arquivo.getName().contains("padrao"))
			{
			  if(classificacao >= redes[i].getThresholdSuperior())
			  {
				resultados[i].incrementaVerdadeiroPositivo(1);
			  }
			  else
			  {
				if(classificacao <= redes[i].getThresholdInferior())
				{
				  resultados[i].incrementaFalsoNegativo(1);
				}
				else
				{
				  resultados[i].incrementaIncerteza(1);
				}
			  }
			}
			else
			{
			  if(classificacao >= redes[i].getThresholdSuperior())
			  {
				resultados[i].incrementaFalsoPositivo(1);
			  }
			  else
			  {
				if(classificacao <= redes[i].getThresholdInferior())
				{
				  resultados[i].incrementaVerdadeiroNegativo(1);
				}
				else
				{
				  resultados[i].incrementaIncerteza(1);
				}
			  }
			}
		  }
	    }
	  }
	}
	
	return resultados;
  }
  
  public static ScoreResults[] validacaoCruzadaComUmDiscriminadorDiscretizada(
		  String folder, int bleaching, int numeroEntradasRAMs)
  {
    RedeNeural[] redes = new RedeNeural[20];
    ScoreResults[] resultados = new ScoreResults[20];
    int tamanhoEntrada = larguraFoto * alturaFoto * ((NIVEL_LUMINANCIA - 1) + (NIVEL_A - 1) + (NIVEL_B - 1));
	  
	for(int i = 0; i < redes.length; i++)
	{
	  redes[i] = new RedeNeural(bleaching, numeroEntradasRAMs, tamanhoEntrada);
	}
	
	for(int i = 0; i < redes.length; i++)
	{
	  resultados[i] = new ScoreResults();
	}
	  
	for(int i = 0; i < redes.length; i++)
	{
	  for(int j = 1; j <= redes.length; j++)
      {
		if((i == j-1)||(i == j)||((i == redes.length - 1)&&(j == 1)))
		{
		  continue;
		}
			  
		String bloco = new String(folder + "bloco " + j + "/");
		File[] arquivos = new File(bloco).listFiles();
		BufferedImage imagem;
		List<Boolean> entrada;
		 
		for(File arquivo: arquivos)
		{
		  if(arquivo.getName().contains("Thumbs"))
		  {
		    continue;
		  }
			
		  imagem = ImageUtils.redimensionarImagem(
				  ImageUtils.loadImage(bloco + arquivo.getName()), 
				  larguraFoto, 
				  alturaFoto);
		  
		  entrada = Binarizador.discretizar(imagem, NIVEL_LUMINANCIA, NIVEL_A, NIVEL_B, folder);
		  
		  redes[i].train(entrada);
		}
	  }
    }
	
	for(int i = 0; i < redes.length; i++)
	{
	  ArrayList<Integer> resultadosPadrao = new ArrayList<Integer>();
	  ArrayList<Integer> resultadosForaPadrao = new ArrayList<Integer>();
	  
	  for(int j = 1; j <= redes.length; j++)
	  {
		if(i == j-1)
		{ 
		  String bloco = new String(folder + "bloco " + j + "/");
		  File[] arquivos = new File(bloco).listFiles();
		  BufferedImage imagem;
		  List<Boolean> entrada;
		  int classificacao;
			 
		  for(File arquivo: arquivos)
		  {
			if(arquivo.getName().contains("Thumbs"))
			{
			  continue;
			}
			  
			imagem = ImageUtils.redimensionarImagem(
					ImageUtils.loadImage(bloco + arquivo.getName()), larguraFoto, alturaFoto);
			
			entrada = Binarizador.discretizar(imagem, NIVEL_LUMINANCIA, NIVEL_A, NIVEL_B, folder);
			
			classificacao = redes[i].classify(entrada);
			
			if(arquivo.getName().contains("padrao"))
			{
			  resultadosPadrao.add(classificacao);
			}
			else
			{
			  resultadosForaPadrao.add(classificacao);
			}		
		  }
		}
	  }
	  redes[i].setThresholdInferior(obtemMaiorValor(resultadosForaPadrao));
	  redes[i].setThresholdSuperior(obtemMenorValor(resultadosPadrao));
	  
	  if(redes[i].getThresholdSuperior() <= redes[i].getThresholdInferior())
	  {
		int thresholdSuperiorAnterior = redes[i].getThresholdSuperior();
		redes[i].setThresholdSuperior(redes[i].getThresholdInferior());
	    redes[i].setThresholdInferior(thresholdSuperiorAnterior);
	  }
	}
	
	for(int i = 0; i < redes.length; i++)
	{
	  for(int j = 1; j <= redes.length; j++)
	  {
	    if((i == j+1)||((i == redes.length-1)&&(j == 1)))
	    {
	      String bloco = new String(folder + "bloco " + j + "/");
		  File[] arquivos = new File(bloco).listFiles();
		  BufferedImage imagem;
		  List<Boolean> entrada;
		  int classificacao;
		  
		  for(File arquivo : arquivos)
		  {
			if(arquivo.getName().contains("Thumbs"))
			{
			  continue;
			}
			
			imagem = ImageUtils.redimensionarImagem(
					ImageUtils.loadImage(bloco + arquivo.getName()),
					larguraFoto,
					alturaFoto);
			
			entrada = Binarizador.discretizar(imagem, NIVEL_LUMINANCIA, NIVEL_A, NIVEL_B, folder);
			
			classificacao = redes[i].classify(entrada);
			
			if(arquivo.getName().contains("padrao"))
			{
			  if(classificacao >= redes[i].getThresholdSuperior())
			  {
				resultados[i].incrementaVerdadeiroPositivo(1);
			  }
			  else
			  {
				if(classificacao <= redes[i].getThresholdInferior())
				{
				  resultados[i].incrementaFalsoNegativo(1);
				}
				else
				{
				  resultados[i].incrementaIncerteza(1);
				}
			  }
			}
			else
			{
			  if(classificacao >= redes[i].getThresholdSuperior())
			  {
				resultados[i].incrementaFalsoPositivo(1);
			  }
			  else
			  {
				if(classificacao <= redes[i].getThresholdInferior())
				{
				  resultados[i].incrementaVerdadeiroNegativo(1);
				}
				else
				{
				  resultados[i].incrementaIncerteza(1);
				}
			  }
			}
		  }
	    }
	  }
	}
	
	return resultados;
  }
  
  public static ScoreResults[] validacaoCruzadaComDoisDiscriminadoresBinarizada(String folder, int numeroEntradasRAMs, double confianca) throws IOException
  {
    RedeNeuralComDiscriminadores[] redes = new RedeNeuralComDiscriminadores[20];
    ScoreResults[] resultados = new ScoreResults[20];
    int tamanhoEntrada = larguraFoto * alturaFoto;
	  
	for(int i = 0; i < redes.length; i++)
	{
	  redes[i] = new RedeNeuralComDiscriminadores(numeroEntradasRAMs, tamanhoEntrada, 2);
	}
	
	for(int i = 0; i < redes.length; i++)
	{
	  resultados[i] = new ScoreResults();
	}
	  
	for(int i = 0; i < redes.length; i++)
	{
	  for(int j = 1; j <= redes.length; j++)
      {
		if(i == j-1)
		{
		  continue;
		}
			  
		String bloco = new String(folder + "bloco " + j + "/");
		File[] arquivos = new File(bloco).listFiles();
		BufferedImage imagem;
		List<Boolean> entrada;
		 
		for(File arquivo: arquivos)
		{
	      if(arquivo.getName().contains("Thumbs"))
		  {
		    continue;
		  }
	      
	      imagem = ImageUtils.redimensionarImagem(ImageUtils.loadImage(bloco + arquivo.getName()),larguraFoto,alturaFoto);
		  entrada = Binarizador.binarizar(imagem);
		  
		  if(arquivo.getName().contains("padrao"))
		  {
			redes[i].treinar(entrada, 0);
		  }
		  else
		  {
		    redes[i].treinar(entrada, 1);
		  }
		}
	  }
    }
	
	for(int i = 0; i < redes.length; i++)
	{
	  for(int j = 1; j <= redes.length; j++)
	  {
	    if(i == j-1)
	    {
	      String bloco = new String(folder + "bloco " + j + "/");
		  File[] arquivos = new File(bloco).listFiles();
		  BufferedImage imagem;
		  List<Boolean> entrada;
		  int classificacao;
		  
		  for(File arquivo : arquivos)
		  {
	        if(arquivo.getName().contains("Thumbs"))
	        {
		      continue;
			}
	        
	        imagem = ImageUtils.redimensionarImagem(
	        		ImageUtils.loadImage(bloco + arquivo.getName()),
	        		larguraFoto,
	        		alturaFoto);
	        
			entrada = Binarizador.binarizar(imagem);
			
			classificacao = redes[i].classificar(entrada, confianca, 0);
			
			if(arquivo.getName().contains("padrao"))
			{
			  if(classificacao == 0)
			  {
				resultados[i].incrementaVerdadeiroPositivo(1);
			  }
			  else
			  {
				if(classificacao == 1)
				{
				  resultados[i].incrementaFalsoNegativo(1);
				}
				else
				{
				  resultados[i].incrementaIncerteza(1);
				}
			  }
			}
			else
			{
			  if(classificacao == 0)
			  {
				resultados[i].incrementaFalsoPositivo(1);
			  }
			  else
			  {
				if(classificacao == 1)
				{
				  resultados[i].incrementaVerdadeiroNegativo(1);
				}
				else
				{
				  resultados[i].incrementaIncerteza(1);
				}
			  }
			}
		  }
	    }
	  }
	}
	
	return resultados;
  } 
  
  public static ScoreResults[] validacaoCruzadaComDoisDiscriminadoresDiscretizada(
		  String folder, int numeroEntradasRAMs, double confianca) throws IOException
  {
    RedeNeuralComDiscriminadores[] redes = new RedeNeuralComDiscriminadores[20];
    ScoreResults[] resultados = new ScoreResults[20];
    int tamanhoEntrada = larguraFoto * alturaFoto * ((NIVEL_LUMINANCIA - 1) + (NIVEL_A - 1) + (NIVEL_B - 1));
	  
	for(int i = 0; i < redes.length; i++)
	{
	  redes[i] = new RedeNeuralComDiscriminadores(numeroEntradasRAMs, tamanhoEntrada, 2);
	}
	
	for(int i = 0; i < redes.length; i++)
	{
	  resultados[i] = new ScoreResults();
	}
	  
	for(int i = 0; i < redes.length; i++)
	{
	  for(int j = 1; j <= redes.length; j++)
      {
		if(i == j-1)
		{
		  continue;
		}
			  
		String bloco = new String(folder + "bloco " + j + "/");
		File[] arquivos = new File(bloco).listFiles();
		BufferedImage imagem;
		List<Boolean> entrada;
		 
		for(File arquivo: arquivos)
		{
	      if(arquivo.getName().contains("Thumbs"))
	      {
	        continue;
		  }
	      
	      imagem = ImageUtils.redimensionarImagem(ImageUtils.loadImage(bloco + arquivo.getName()),larguraFoto,alturaFoto);
		  entrada = Binarizador.discretizar(imagem, NIVEL_LUMINANCIA, NIVEL_A, NIVEL_B, folder);
		  
		  if(arquivo.getName().contains("padrao"))
		  {
			redes[i].treinar(entrada, 0);
		  }
		  else
		  {
		    redes[i].treinar(entrada, 1);
		  }
		}
	  }
    }
	
	for(int i = 0; i < redes.length; i++)
	{
	  for(int j = 1; j <= redes.length; j++)
	  {
	    if(i == j-1)
	    {
	      String bloco = new String(folder + "bloco " + j + "/");
		  File[] arquivos = new File(bloco).listFiles();
		  BufferedImage imagem;
		  List<Boolean> entrada;
		  int classificacao;
		  
		  for(File arquivo : arquivos)
		  {
		    if(arquivo.getName().contains("Thumbs"))
		    {
			  continue;
			}
		    
		    imagem = ImageUtils.redimensionarImagem(
		    		ImageUtils.loadImage(bloco + arquivo.getName()),
		    		larguraFoto,alturaFoto);
		    
			entrada = Binarizador.discretizar(imagem, NIVEL_LUMINANCIA, NIVEL_A, NIVEL_B, folder);
			
			classificacao = redes[i].classificar(entrada, confianca, 0);
			
			if(arquivo.getName().contains("padrao"))
			{
			  if(classificacao == 0)
			  {
				resultados[i].incrementaVerdadeiroPositivo(1);
			  }
			  else
			  {
				if(classificacao == 1)
				{
				  resultados[i].incrementaFalsoNegativo(1);
				}
				else
				{
				  resultados[i].incrementaIncerteza(1);
				}
			  }
			}
			else
			{
			  if(classificacao == 0)
			  {
				resultados[i].incrementaFalsoPositivo(1);
			  }
			  else
			  {
				if(classificacao == 1)
				{
				  resultados[i].incrementaVerdadeiroNegativo(1);
				}
				else
				{
				  resultados[i].incrementaIncerteza(1);
				}
			  }
			}
		  }
	    }
	  }
	}
	
	return resultados;
  }
  
  public static ScoreResults[] validacaoCruzadaComSeteDiscriminadoresBinarizada(String folder, int numeroEntradasRAMs, double confianca) throws IOException
  {
    RedeNeuralComDiscriminadores[] redes = new RedeNeuralComDiscriminadores[20];
    ScoreResults[] resultados = new ScoreResults[20];
    int tamanhoEntrada = larguraFoto * alturaFoto;
	  
	for(int i = 0; i < redes.length; i++)
	{
	  redes[i] = new RedeNeuralComDiscriminadores(numeroEntradasRAMs, tamanhoEntrada, 2);
	}
	
	for(int i = 0; i < redes.length; i++)
	{
	  resultados[i] = new ScoreResults();
	}
	  
	for(int i = 0; i < redes.length; i++)
	{
	  for(int j = 1; j <= redes.length; j++)
      {
		if(i == j-1)
		{
		  continue;
		}
			  
		String bloco = new String(folder + "bloco " + j + "/");
		File[] arquivos = new File(bloco).listFiles();
		BufferedImage imagem;
		List<Boolean> entrada;
		 
		for(File arquivo: arquivos)
		{
	      if(arquivo.getName().contains("Thumbs"))
		  {
		    continue;
		  }
	      
	      imagem = ImageUtils.redimensionarImagem(ImageUtils.loadImage(bloco + arquivo.getName()),larguraFoto,alturaFoto);
		  entrada = Binarizador.binarizar(imagem);
		  
		  if(arquivo.getName().contains("padrao"))
		  {
			redes[i].treinar(entrada, 0);
		  }
		  else
		  {
		    redes[i].treinar(entrada, 1);
		  }
		}
	  }
    }
	
	for(int i = 0; i < redes.length; i++)
	{
	  for(int j = 1; j <= redes.length; j++)
	  {
	    if(i == j-1)
	    {
	      String bloco = new String(folder + "bloco " + j + "/");
		  File[] arquivos = new File(bloco).listFiles();
		  BufferedImage imagem;
		  List<Boolean> entrada;
		  int classificacao;
		  
		  for(File arquivo : arquivos)
		  {
	        if(arquivo.getName().contains("Thumbs"))
	        {
		      continue;
			}
	        
	        imagem = ImageUtils.redimensionarImagem(
	        		ImageUtils.loadImage(bloco + arquivo.getName()),
	        		larguraFoto,
	        		alturaFoto);
	        
			entrada = Binarizador.binarizar(imagem);
			
			classificacao = redes[i].classificar(entrada, confianca, 0);
			
			if(arquivo.getName().contains("padrao"))
			{
			  if(classificacao == 0)
			  {
				resultados[i].incrementaVerdadeiroPositivo(1);
			  }
			  else
			  {
				if(classificacao == 1)
				{
				  resultados[i].incrementaFalsoNegativo(1);
				}
				else
				{
				  resultados[i].incrementaIncerteza(1);
				}
			  }
			}
			else
			{
			  if(classificacao == 0)
			  {
				resultados[i].incrementaFalsoPositivo(1);
			  }
			  else
			  {
				if(classificacao == 1)
				{
				  resultados[i].incrementaVerdadeiroNegativo(1);
				}
				else
				{
				  resultados[i].incrementaIncerteza(1);
				}
			  }
			}
		  }
	    }
	  }
	}
	
	return resultados;
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
  }*/
}