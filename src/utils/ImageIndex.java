package utils;

public class ImageIndex
{
  private String identificacao;
  private boolean padrao;
  private boolean ok;
  private boolean neutro;
  private boolean feliz;
  private boolean triste;
  private boolean medo;
  private boolean raiva;
  private boolean surpresa;
  private boolean repulsa;
  public static final int quantidadeIndicesForaPadrao = 400;
  public static final int quantidadeIndicesPadrao = 400;
  public static final int quantidadeIndicesEmocoes = 186;
  
  public ImageIndex(String identificacao)
  {
    this.identificacao = identificacao;
    neutro = false;
    feliz = false;
    triste = false;
    medo = false;
    raiva = false;
    surpresa = false;
    repulsa = false;
  }
  
  public String getIdentificacao()
  {
    return identificacao;
  }
  
  public void setIdentificacao(String identificacao)
  {
    this.identificacao = identificacao;
  }
  
  public boolean isPadrao()
  {
    return padrao;
  }
  
  public void setPadrao(boolean padrao)
  {
    this.padrao = padrao;
  }

  public boolean isOk()
  {
    return ok;
  }

  public void setOk(boolean ok)
  {
    this.ok = ok;
  }

  public boolean isNeutro() {
	// TODO Auto-generated method stub
	  return neutro;
  }
  
  public void setNeutro(boolean neutro)
  {
    this.neutro = neutro;
  }

  public boolean isFeliz()
  {
    return feliz;
  }

  public void setFeliz(boolean feliz)
  {
    this.feliz = feliz;
  }

  public boolean isTriste()
  {
    return triste;
  }

  public void setTrsite(boolean triste)
  {
    this.triste = triste;
  }

  public boolean isMedo()
  {
    return medo;
  }

  public void setMedo(boolean medo)
  {
    this.medo = medo;
  }

  public boolean isRaiva()
  {
    return raiva;
  }

  public void setRaiva(boolean raiva)
  {
    this.raiva = raiva;
  }

  public boolean isSurpresa()
  {
    return surpresa;
  }

  public void setSurpresa(boolean surpresa)
  {
    this.surpresa = surpresa;
  }

  public boolean isRepulsa()
  {
    return repulsa;
  }

  public void setRepulsa(boolean repulsa)
  {
    this.repulsa = repulsa;
  }
}