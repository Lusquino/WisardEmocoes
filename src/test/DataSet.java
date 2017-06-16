package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.*;

public class DataSet {
	
	static private String pastaPrincipal = "C:/Users/Usuario/Desktop/Mestrado/Dissertação/DataSetMMI";
	static private FileReader fis;
	static private BufferedReader bufferedReader;
	static private StringBuilder buffer = new StringBuilder();
	static private String line = "";
	static private File baseFolder, file;
	static private File[] files;

	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String pasta;
		int num;
			
		for(int i = 1; i<494; i++){
		/*	// Cria channel na origem
			num = 2400+i;
			pasta=pastaPrincipal+"/Sessions/"+num+"/";
			FileChannel oriChannel = new FileInputStream(pasta).getChannel();
			// Cria channel no destino
			FileChannel destChannel = new FileOutputStream(pastaPrincipal+"/novoDataSet/").getChannel();
			// Copia conteúdo da origem no destino
			destChannel.transferFrom(oriChannel, 0, oriChannel.size());

			// Fecha channels
			oriChannel.close();
			destChannel.close();
			
			
		}*/
			num = 2400+270+i;
			pasta=pastaPrincipal+"/Sessions/"+num+"/";
            
			file = new File(pasta);
			int j =0;
			System.out.println(pasta+ " ");
			for (File f : file.listFiles()) {
		        String fileName = f.getName();
		        System.out.println(fileName);
		        System.out.println(j+"/"+file.listFiles().length);
		        j++;
		        if(fileName.contains(".xml")){break;}
		        Files.move(f.toPath(), Paths.get(pastaPrincipal, fileName));
		    }
			
			/*files = new File(pasta);
             
            for(int j = 0; j <files.length; j++){
            	System.out.println("To no "+ files[j].getName());
            }
           
            for (int j = 0; j < files.length; j++) {
            	file = new File(pastaPrincipal+"/"+files[j].getName());
            	
            	if(file.getName().contains("xml")){break;} 
                
            	System.out.println("Teste: "+file.getName());
            	
            	files[j].renameTo(file);     
               
                System.out.println(files[j].getAbsolutePath());
            }*/
		}
	}
}