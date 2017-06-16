package test;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import model.RedeNeuralComDiscriminadores;
import utils.ImageUtils;

public class GeradorImagensMentais {
	
	private static final String pasta = "C:/Users/Usuario/Desktop/Mestrado/Dissertação/DataSetMMI/binarizado/ValidacaoCruzada/";
	private static final String pastaDestino = "C:/Users/Usuario/Desktop/Mestrado/Dissertação/DataSetMMI/mentais/";

	
	private static File[] arquivos;
	
	private static RedeNeuralComDiscriminadores rede;
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		rede = new RedeNeuralComDiscriminadores(10, 19200, 7);
		
		arquivos = new File(pasta).listFiles();
		
		for(int i=0; i<arquivos.length; i++)
		{
			BufferedImage imagem = ImageIO.read(arquivos[i]);
			imagem = ImageUtils.redimensionarImagem(imagem, 120, 160);
			List<Boolean> entrada = geradorBooleano(imagem);
			//System.out.println(arquivos[i].getName());
			rede.treinar(entrada, obterEmocao(arquivos[i].getName()));
		}
		
		int bleaching = 7;

		BufferedImage[] imagens = rede.createMentalImage(bleaching);
		
		for(int i =0; i<imagens.length; i++)
		{
			ImageIO.write(imagens[i], "JPG", new File(pastaDestino+"mentais"+i+".JPG"));
		}
	}
	
	public static int obterEmocao(String nome)
	{		  
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
	
	public static List<Boolean> geradorBooleano(BufferedImage imagem)
	  {
		  List<Boolean> entrada = new ArrayList<Boolean>();
		  
		  for(int i = 0; i < imagem.getWidth(); i++)
		  {
			  for(int j = 0; j < imagem.getHeight(); j++)
			  {
				  entrada.add(imagem.getRGB(i, j) == Color.WHITE.getRGB());
				 // System.out.println((imagem.getRGB(i, j) == Color.WHITE.getRGB())+";");
			  }
		  }
		  
		  return entrada;
	  }
}
