package model;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import utils.Binarizador;

public class RedeNeuralComPreProcessamento
{
  private static final int NIVEL_LUMINANCIA = 4;
  private static final int NIVEL_A = 8;
  private static final int NIVEL_B = 8;
  private static final int BLEACHING = 0;
  
  private RedeNeuralComDiscriminadores binarizada;
  private RedeNeuralComDiscriminadores discretizada;
  private ClusterDetector detector;
  private double confianca;
  private double confiancaPreProcessamento;
  
  public RedeNeuralComPreProcessamento(int entradasRAMs, double confianca, double confiancaPreProcessamento) throws FileNotFoundException
  {
    binarizada = new RedeNeuralComDiscriminadores(entradasRAMs, ImageSetup.WIDTH * ImageSetup.HEIGHT, 2);
    discretizada = new RedeNeuralComDiscriminadores(entradasRAMs, ImageSetup.WIDTH_FACE * ImageSetup.HEIGHT_FACE, 2);
    detector = new ClusterDetector(entradasRAMs, true, NIVEL_LUMINANCIA, NIVEL_A, NIVEL_B);
    this.confianca = confianca;
    this.confiancaPreProcessamento = confiancaPreProcessamento;
  }
  
  public RedeNeuralComPreProcessamento(int entradasRAMsBinarizada, int entradasRAMsDiscretizada, double confianca, double confiancaPreProcessamento) throws FileNotFoundException
  {
    binarizada = new RedeNeuralComDiscriminadores(entradasRAMsBinarizada, ImageSetup.WIDTH * ImageSetup.HEIGHT, 2);
    discretizada = new RedeNeuralComDiscriminadores(entradasRAMsDiscretizada, ImageSetup.WIDTH_FACE * ImageSetup.HEIGHT_FACE, 2);
    detector = new ClusterDetector(entradasRAMsDiscretizada, true, NIVEL_LUMINANCIA, NIVEL_A, NIVEL_B);
    this.confianca = confianca;
    this.confiancaPreProcessamento = confiancaPreProcessamento;
  }
  
  public RedeNeuralComDiscriminadores getBinarizada()
  {
    return binarizada;
  }

  public RedeNeuralComDiscriminadores getDiscretizada()
  {
    return discretizada;
  }

  public void train(BufferedImage imagemMentalBinarizadaPadrao,   BufferedImage imagemMentalBinarizadaForaPadrao,
                     BufferedImage imagemMentalDiscretizadaPadrao, BufferedImage imagemMentalDiscretizadaForaPadrao,
                     BufferedImage imagemMentalOlho, BufferedImage imagemMentalBoca) throws IOException
  {
    List<Boolean> entrada;
    
    entrada = Binarizador.binarizar(imagemMentalBinarizadaPadrao);
    binarizada.treinar(entrada, 0);
   
    entrada = Binarizador.binarizar(imagemMentalBinarizadaForaPadrao);
    binarizada.treinar(entrada, 1);
    
    detector.treinarOlhosEBoca(imagemMentalOlho, imagemMentalBoca);
    
   // entrada = Binarizador.discretizar(imagemMentalDiscretizadaPadrao, NIVEL_LUMINANCIA, NIVEL_A, NIVEL_B);
    discretizada.treinar(entrada, 0);
    
    //entrada = Binarizador.discretizar(imagemMentalDiscretizadaForaPadrao, NIVEL_LUMINANCIA, NIVEL_A, NIVEL_B);
    discretizada.treinar(entrada, 1);
  }
  
  public int classify(BufferedImage imagem) throws IOException
  {
    BufferedImage face;
    List<Boolean> entrada;
    int classificacao;
    
    entrada = Binarizador.binarizar(imagem);
    classificacao = binarizada.classificar(entrada, confiancaPreProcessamento, BLEACHING);
    
    if(classificacao == 1)
    {
      face = detector.detectarFaces(imagem);
      entrada = Binarizador.discretizar(face, NIVEL_LUMINANCIA, NIVEL_A, NIVEL_B,"");
      classificacao = discretizada.classificar(entrada, confianca, 0);
    }
    
    return classificacao;
  }
}