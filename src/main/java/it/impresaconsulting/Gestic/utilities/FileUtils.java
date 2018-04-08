package it.impresaconsulting.Gestic.utilities;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FileUtils {

    private static final String SELEZIONA_UN_FILE       = "Seleziona il file da allegare";
    private static final String UPLOADED_FOLDER         = "./documentazione";


    public ResponseEntity<byte[]> getFile(String filename) throws IOException{
        HttpHeaders headers = new HttpHeaders();
        if(filename.endsWith(".pdf")) {
            headers.setContentType(MediaType.parseMediaType("application/pdf"));
        }else if(filename.endsWith(".docx")) {
            headers.setContentType(MediaType.parseMediaType("application/docx"));
        }else if(filename.endsWith(".doc")) {
            headers.setContentType(MediaType.parseMediaType("application/doc"));
        }else if(filename.endsWith(".xlsx")) {
            headers.setContentType(MediaType.parseMediaType("application/xlsx"));
        }else if(filename.endsWith(".xls")) {
            headers.setContentType(MediaType.parseMediaType("application/xls"));
        }else if(filename.endsWith(".ppt")) {
            headers.setContentType(MediaType.parseMediaType("application/ppt"));
        }else if(filename.endsWith(".pptx")) {
            headers.setContentType(MediaType.parseMediaType("application/pptx"));
        }
        headers.add("content-disposition", "inline;filename=" + filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        Path path = Paths.get(filename);
        byte[] data = Files.readAllBytes(path);
        ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(data, headers, HttpStatus.OK);
        return response;
    }

    public ResponseEntity<?> uploadFileOnServer(String cliente, String pratica, String step, MultipartFile[] uploadfiles){
        String uploadedFileName = Arrays.stream(uploadfiles).map(x -> x.getOriginalFilename())
                .filter(x -> !StringUtils.isEmpty(x)).collect(Collectors.joining(" , "));
        if (StringUtils.isEmpty(uploadedFileName)) {
            return new ResponseEntity(SELEZIONA_UN_FILE, HttpStatus.OK);
        }
        try {
            saveUploadedFiles(Arrays.asList(uploadfiles), cliente, pratica, step);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(UPLOADED_FOLDER + "/" + cliente + "/" + pratica + "/" + step + "/" + uploadedFileName, HttpStatus.OK);
    }

    private void saveUploadedFiles(List<MultipartFile> files, String cliente, String pratica, String step) throws IOException {
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue;
            }
            byte[] bytes = file.getBytes();
            File directory = new File(String.valueOf(UPLOADED_FOLDER));
            if(!directory.exists()) {
                directory.mkdir();
            }
            String dinamicFolder = UPLOADED_FOLDER + "/" + cliente + "/";
            directory = new File(String.valueOf(dinamicFolder));
            if(!directory.exists()) {
                directory.mkdir();
            }
            dinamicFolder = dinamicFolder + pratica + "/";
            directory = new File(String.valueOf(dinamicFolder));
            if(!directory.exists()) {
                directory.mkdir();
            }
            dinamicFolder = dinamicFolder + step + "/";
            directory = new File(String.valueOf(dinamicFolder));
            if(!directory.exists()) {
                directory.mkdir();
            }

            Path path = Paths.get(dinamicFolder + file.getOriginalFilename());
            Files.write(path, bytes);
        }
    }

}
