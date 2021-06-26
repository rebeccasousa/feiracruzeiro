package com.feira.cruzeiro.utils;

import java.io.*;
import java.nio.file.*;
 
import org.springframework.web.multipart.MultipartFile;

import com.feira.cruzeiro.model.Evento;
import com.feira.cruzeiro.model.Loja;
 
public class FileUploadUtil {
     
    public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
         
        if (!Files.exists(uploadPath))	Files.createDirectories(uploadPath);
         
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {        
            throw new IOException("Could not save image file: " + fileName, ioe);
        }      
    }
    
    public static void createFile(Loja loja, Evento evento, MultipartFile multipartFile) throws IOException {
    	if(multipartFile.getOriginalFilename().isEmpty())
			return;
		
		String path = ""; 
		if(loja != null) path = "fotos-lojas/" + loja.getId();
		else path = "fotos-eventos/" + evento.getId();
		
        try {
			FileUploadUtil.saveFile(path, multipartFile.getOriginalFilename(), multipartFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public static void removerFile(Loja loja) {
		File file = new File("/fotos-lojas/" + loja.getId() + "/" + loja.getArquivo());
		file.delete();
	}
	
    public static void removerFile(Evento evento) {
		File file = new File("/fotos-eventos/" + evento.getId() + "/" + evento.getArquivo());
		file.delete();
	}
	
}