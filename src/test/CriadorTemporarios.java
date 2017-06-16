package test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import model.ClusterDetector;
import utils.Binarizador;
import utils.ImageUtils;

public class CriadorTemporarios {
	
	static private String pastaOrigem = "C:/Users/Usuario/Desktop/Mestrado/Dissertação/DataSetMMI/novoDataSet/ValidacaoCruzadaTodos/";
	//static private String pastaOrigem = "C:/Users/Usuario/Desktop/Mestrado/Dissertação/DataSetMMI/recortadas/todas/";
	//static private String pastaDestino = "C:/Users/Usuario/Desktop/Mestrado/Dissertação/DataSetMMI/recortadas/todas/";
	static private String pastaDestino = "C:/Users/Usuario/Desktop/Mestrado/Dissertação/DataSetMMI/discretizado/ValidacaoCruzadaTodos/";
	//static private String pastaDestino = "C:/Users/Usuario/Desktop/Mestrado/Dissertação/DataSetMMI/recortadas/todasDiscretizadas/";
	//static private String pastaDestino = "C:/Users/Usuario/Desktop/Mestrado/Dissertação/DataSetMMI/clusterizadas/";
	static private BufferedImage imagem; 
	
	
	public static void main(String[] args) throws IOException {
		File[] arquivos = new File(pastaOrigem).listFiles();
		
		//ClusterDetector clusterDetector = new ClusterDetector(20, false, 4, 8, 8);
		
		//clusterDetector.treinarOlhosEBoca("C:/Users/Usuario/Desktop/Mestrado/Dissertação/DataSetMMI/Olhos",
		//"C:/Users/Usuario/Desktop/Mestrado/Dissertação/DataSetMMI/Bocas");
		
		for(File arquivo: arquivos)
		{		
			imagem = ImageIO.read(arquivo);
			//imagem = Binarizador.colorirBinario(imagem);
			//imagem = ImageUtils.redimensionarImagem(imagem, imagem.getWidth()/4, imagem.getHeight()/4);
			//imagem = Binarizador.discretizarCores(imagem,8);
			//imagem = Binarizador.clusterizar(imagem);
			imagem = Binarizador.colorirDiscreto(imagem, 4, 8, 8);
			//imagem = tirarMoldura(imagem);
			  //imagem = ImageUtils.redimensionarImagem(imagem, 120, 160);
			//  imagem = clusterDetector.detectarFaces(imagem);
			ImageIO.write(imagem, "JPG", new File(pastaDestino+arquivo.getName()+".JPG"));
		}
	}
	
	public static BufferedImage tirarMoldura(BufferedImage imagem)
	{
		BufferedImage imagemRecortada = new BufferedImage(35*imagem.getWidth()/50, 35*imagem.getHeight()/70, imagem.getType());
		
		for(int y = 18*imagem.getHeight()/70; y < 53*imagem.getHeight()/70; y++)
		{
			for(int x = 8*imagem.getWidth()/50; x< 43*imagem.getWidth()/50; x++)
			{
				imagemRecortada.setRGB(x - 8*imagem.getWidth()/50, y - 18*imagem.getHeight()/70, imagem.getRGB(x, y));
			}
		}
		return imagemRecortada;
	}

}
