package utils;

public class ScoreResults
{
  private double verdadeiroPositivo;
  private double verdadeiroNegativo;
  private double falsoPositivo;
  private double falsoNegativo;
  private double incerteza;
  private double acerto;
  private double erro;
  private int[][] matrizErros;
  
	public ScoreResults()
	  {
	    verdadeiroPositivo = 0;
	    verdadeiroNegativo = 0;
	    falsoPositivo = 0;
	    falsoNegativo = 0;
	    acerto = 0;
	    erro = 0;
	    matrizErros = new int[7][7];
	    for(int i=0; i<7; i++)
	    {
	    	for(int j=0; j<7; j++)
	    	{
	    		matrizErros[i][j] = 0; 
	    	}
	    }
	  }
  
  public ScoreResults(
		  double verdadeiroPositivo, 
		  double verdadeiroNegativo, double 
		  falsoPositivo, 
		  double falsoNegativo)
  {
    this.verdadeiroPositivo = verdadeiroPositivo;
    this.verdadeiroNegativo = verdadeiroNegativo;
    this.falsoPositivo = falsoPositivo;
    this.falsoNegativo = falsoNegativo;
  }
  
  public double obterAcerto()
  {
    return (verdadeiroNegativo + verdadeiroNegativo) / 
    		(falsoNegativo + falsoPositivo + verdadeiroNegativo + 
    				verdadeiroPositivo + incerteza);
  }

  public double getVerdadeiroPositivo()
  {
    return verdadeiroPositivo;
  }

  public void incrementaVerdadeiroPositivo(double valor)
  {
    verdadeiroPositivo += valor;
  }

  public double getVerdadeiroNegativo()
  {
    return verdadeiroNegativo;
  }

  public void incrementaVerdadeiroNegativo(double valor)
  {
    verdadeiroNegativo += valor;
  }

  public double getFalsoPositivo()
  {
    return falsoPositivo;
  }

  public void incrementaFalsoPositivo(double valor)
  {
    falsoPositivo += valor;
  }

  public double getFalsoNegativo()
  {
    return falsoNegativo;
  }

  public void incrementaFalsoNegativo(double valor)
  {
    falsoNegativo += valor;
  }
  
  public double getIncerteza()
  {
    return incerteza;
  }
  
  public void incrementaIncerteza(double valor)
  {
    incerteza += valor;
  }

  public void setVerdadeiroPositivo(double verdadeiroPositivo)
  {
    this.verdadeiroPositivo = verdadeiroPositivo;
  }

  public void setVerdadeiroNegativo(double verdadeiroNegativo)
  {
    this.verdadeiroNegativo = verdadeiroNegativo;
  }

  public void setFalsoPositivo(double falsoPositivo)
  {
    this.falsoPositivo = falsoPositivo;
  }

  public void setFalsoNegativo(double falsoNegativo)
  {
    this.falsoNegativo = falsoNegativo;
  }
  
  public double getTamanhoLimbo(int numeroFotos)
  {
    return numeroFotos - (verdadeiroPositivo + verdadeiroNegativo + falsoPositivo + falsoNegativo);
  }
  
  public double getPrecision()
  {
    return verdadeiroPositivo / (verdadeiroPositivo + falsoPositivo);
  }
  
  public double getRecall()
  {
    return verdadeiroPositivo / (verdadeiroPositivo + falsoNegativo);
  }
  
  public double getFMeasure()
  {
    return this.getFScore(1);
  }
  
  public double getFScore(double peso)
  {
    if (this.getPrecision() == 0.0 || this.getRecall() == 0.0)
    {
      return 0.0;
    }
    
    return (1. + Math.pow(peso, 2)) * this.getPrecision() * this.getRecall() /
    		(Math.pow(peso, 2) * this.getPrecision() + this.getRecall());
  }
  
  public double getAcuracia()
  {
    return (verdadeiroPositivo + verdadeiroNegativo) /
    		(verdadeiroPositivo + falsoPositivo + verdadeiroNegativo + falsoNegativo);
  }
  
  public double getScore(int numFotos)
  {
    double c1 = 0.5;
    double c2 = 0.25;
    double c3 = 0.25;
    
    double k = c1*c2*c3;
    double z = (c1 + 1) * (c2 + 1) * (c3 + 1) - k;
    
    System.out.println(this.getFScore(0.5));
    System.out.println(this.getAcuracia());
    System.out.println(1.0 - this.getTamanhoLimbo(numFotos)/numFotos);
    System.out.println(k);
    System.out.println(z);
    System.out.println(((this.getFScore(0.5) * this.getAcuracia() + c1) * 
    		(this.getFScore(0.5) * (1.0 - this.getTamanhoLimbo(numFotos)/numFotos) + c2) *
            (this.getAcuracia() * (1.0 - this.getTamanhoLimbo(numFotos)/numFotos) + c3) - k) / z);
    
    return ((this.getFScore(0.5) * this.getAcuracia() + c1) * (this.getFScore(0.5) * 
    		(1.0 - this.getTamanhoLimbo(numFotos)/numFotos) + c2) *
            (this.getAcuracia() * (1.0 - this.getTamanhoLimbo(numFotos)/numFotos) + c3) - k) / z;
  }

	public double getAcerto() {
		return acerto;
	}
	
	public void setAcerto(double acerto) {
		this.acerto = acerto;
	}
	
	public void incrementaAcerto() {
		acerto++;
	}
	
	public void incrementaAcerto(double acerto) {
		this.acerto += acerto;
	}
	
	public double getErro() {
		return erro;
	}

	public void setErro(double erro) {
		this.erro = erro;
	}
	
	public void incrementaErro() {
		erro++;
	}
	
	public void incrementaErro(double erro) {
		this.erro += erro;
	}
	
	public int getMatrizErros(int x, int y) {
			return matrizErros[x][y];
	}

	public void incrementaMatrizErros(int x, int y) {
			matrizErros[x][y]++;
	}

}