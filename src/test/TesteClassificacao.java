package test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import org.junit.*;

import action.Classifier;
import model.ImageSetup;
import model.RedeNeuralComDiscriminadores;
import utils.Binarizador;
import utils.ImageUtils;

public class TesteClassificacao {

	String pathToTest = "/home/mgo/projetos/idphoto/";
	String pathToNN = pathToTest + "twoDiscriminatorsNN.serial";
	String[] okImg = {pathToTest + "1.jpg", pathToNN};
	String[] failImg = {pathToTest + "0.jpg", pathToNN};
	
	RedeNeuralComDiscriminadores nn;
	List<Boolean> input;
	

	public static void main(String[] args)
	{
		double[] num = {24, 7,  0,  0,  7,  2,  3,5,  37, 0,  0,  10, 1,  0,
				2,  1,  4,  0,  2,  2,  0,
				0,  2,  0,  9,  2,  0,  0,
				5,  6,  1,  0,  41, 1,  2,
				0,  7,  0,  0,  11, 21, 1,
				14,  3,  0, 1,  6,  1,  19};
		
		int soma = 0;
		
		for(int i=0; i<num.length;i++)
		{
			soma += num[i];
		}
		
		for(int i=0; i<num.length;i++)
		{
			System.out.println(num[i]*100/soma);
		}
	}
	
	@Test
	public void testOkImage() throws IOException {
    	
		input = Binarizador.binarizar(ImageUtils.dimensionarImagem(okImg[0], ImageSetup.WIDTH, ImageSetup.HEIGHT));
		nn = new RedeNeuralComDiscriminadores(pathToNN);
		Assert.assertEquals(nn.classificar(input, 0.2 ,0), 1);

	}
	
	@Test
	public void testFailImage() throws IOException {

		input = Binarizador.binarizar(ImageUtils.dimensionarImagem(failImg[0], ImageSetup.WIDTH, ImageSetup.HEIGHT));
		nn = new RedeNeuralComDiscriminadores(pathToNN);
		Assert.assertEquals(nn.classificar(input, 0.2 ,0), 0);
	}
	
	@Test
	public void testMain() throws IOException {
		ByteArrayOutputStream outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
		Classifier.main(okImg);
		Assert.assertEquals(outContent.toString(), "yes\n");
		System.setOut(null);
	}
}