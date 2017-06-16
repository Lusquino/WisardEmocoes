package test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import utils.FoldCrossValidation;
import utils.ImageIndex;
import utils.ScoreResults;

public class TesteValidacaoCruzada {
	private static String pasta = "C:/Users/Usuario/Desktop/Mestrado/Dissertação/";

	public static void main(String[] args) throws Exception {
		//int numeroEntradasRAM = 1;
		int tamanhoEntradaRAMs = 19200;
		double[] confianca = { 0.01, 0.05, 0.1, 0.3 };
		//int bleaching = 0;
		int index = 0;
		File file = new File(pasta + "/dataSetMMI/resultados6.txt");
		OutputStream out = new FileOutputStream(file);

		FoldCrossValidation.validacaoSimples(30, tamanhoEntradaRAMs, 0.01,
				4 , out);
				
		  //FoldCrossValidation.validacaoDiscretizado(50, tamanhoEntradaRAMs, 0.01,
			//                     			0 , out);
		
		/*for (int numeroEntradasRAMs = 5; numeroEntradasRAMs <= 15; numeroEntradasRAMs += 3) {
			out.write(("Número de Entradas nas RAMs: " + numeroEntradasRAMs + "\n\n").getBytes());
			for (int bleaching = 0; bleaching < 5; bleaching++) {
				out.write(("Bleaching: " + bleaching + "\n\t").getBytes());
					//out.write(("Confiança " + confianca[i] + "\n\t").getBytes());
					out.write(("Experimento #" + index + "\n").getBytes());
					FoldCrossValidation.validacaoSimples(numeroEntradasRAMs, tamanhoEntradaRAMs, 0.01,
							bleaching, out);
					index++;
			}
		}*/
		
		/*
		 * try { int ramsMinimo = 1; int ramsMaximo = 10;
		 * 
		 * int melhorNumeroEntradaRAMs = ramsMinimo; double melhorConfianca =
		 * 0.1;
		 * 
		 * File file = new File(pasta + "/DataSetMMI/discriminador.txt");
		 * OutputStream out = new FileOutputStream(file);
		 * 
		 * int numFotos = ImageIndex.quantidadeIndicesPadrao +
		 * ImageIndex.quantidadeIndicesForaPadrao;
		 * 
		 * ScoreResults melhorResultado = new ScoreResults(0, 0,
		 * ImageIndex.quantidadeIndicesForaPadrao,
		 * ImageIndex.quantidadeIndicesPadrao);
		 * 
		 * System.out.println("Score inicial = " +
		 * melhorResultado.getScore(numFotos) + "\n\n");
		 * 
		 * for(int numeroEntradaRAMs = ramsMinimo; numeroEntradaRAMs <=
		 * ramsMaximo; numeroEntradaRAMs++) { for (double confianca = 0.03;
		 * confianca < 0.3; confianca += 0.05) { out.write(("\nCONFIANCA "+
		 * confianca + "\n").getBytes()); System.out.println("\n(" + confianca +
		 * ", " + numeroEntradaRAMs + "):");
		 * 
		 * ScoreResults resultado =
		 * FoldCrossValidation.validacaoCruzadaComDiscrimininadores(
		 * numeroEntradaRAMs, confianca, out);
		 * 
		 * if (resultado.getScore(numFotos) >
		 * melhorResultado.getScore(numFotos)) { melhorNumeroEntradaRAMs =
		 * numeroEntradaRAMs; melhorConfianca = confianca; melhorResultado =
		 * resultado; }
		 * 
		 * out.write(("Score atualizado = " + melhorResultado.getScore(numFotos)
		 * + "\n\n").getBytes()); System.out.println("Score atualizado = " +
		 * melhorResultado.getScore(numFotos) + "\n\n");
		 * 
		 * } }
		 * 
		 * String mensagem = "(" + melhorConfianca + ", " +
		 * melhorNumeroEntradaRAMs + "):" + "\n\tFotos: " + numFotos +
		 * ", Limbo: " + melhorResultado.getTamanhoLimbo(numFotos) + ", " +
		 * "\n\tVP = " + melhorResultado.getVerdadeiroPositivo() + ", " +
		 * "\n\tFP = " + melhorResultado.getFalsoPositivo() + ", " + "\n\tVN = "
		 * + melhorResultado.getVerdadeiroNegativo() + ", " + "\n\tFN = " +
		 * melhorResultado.getFalsoNegativo() + ", " + "\n\tPrecision = " +
		 * melhorResultado.getPrecision() + ", " + "\n\tRecall = " +
		 * melhorResultado.getRecall() + ", " + "\n\tF-Measure = " +
		 * melhorResultado.getFMeasure() + ", " + "\n\tAcurácia = " +
		 * melhorResultado.getAcuracia() + ", " + "\n\tScore = " +
		 * melhorResultado.getScore(numFotos) + ", " +
		 * "\n\tNão classificados = " + (100.0 *
		 * melhorResultado.getTamanhoLimbo(numFotos) / numFotos) + "%";
		 * 
		 * out.write(("\n\n\n" + mensagem + "\n").getBytes());
		 * System.out.println("\n\n\n" + mensagem + "\n");
		 * 
		 * out.close(); } catch (Exception e) { e.printStackTrace();
		 * StackTraceElement[] stacktrace = e.getStackTrace();
		 * 
		 * String mensagemStackTrace = e.getMessage() + "\n"; for (int i = 0; i
		 * < stacktrace.length; i++) { mensagemStackTrace +=
		 * stacktrace[i].toString() + "\n"; } mensagemStackTrace +=
		 * e.getCause().toString();
		 * 
		 * e.printStackTrace(); System.exit(1); }
		 */
	}
}