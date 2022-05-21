package com.ethioclicks.userProfileCrud.controller;

import com.ethioclicks.userProfileCrud.model.Invoice;
import com.ethioclicks.userProfileCrud.model.UserProfile;
import com.ethioclicks.userProfileCrud.services.StorageService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.ethioclicks.userProfileCrud.controller.UserProfileController.*;
import static java.lang.Integer.parseInt;

@Controller
public class InvoicesController {

    public static String selectedFilestoArchive = "";
    //todo: enable to archive only selected files by the user

    @PostMapping("/userProfile/uploadFile")
    public String uploadVideo(@RequestParam( name = "id" , required = false) int id , @RequestParam(name="userFile") MultipartFile multipartFile , Model model) throws IOException {
        int index = getUserProfileIndex(id);
        String name = UserProfileController.getProfiles().get(index).getUserName();
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        System.out.println("File Name: "+fileName);
        String uploadDir = "user-files/"+name+"/file";

        System.out.println("Store File: "+uploadDir);
        System.out.println("Name: "+fileName);

        StorageService.saveFile(uploadDir, fileName, multipartFile);
        System.out.println(name+" Uploaded a new Video");
        String url = baseUrl+"storage/"+name+"/file/"+fileName;
        Invoice file = new Invoice();
        file.setId(getProfiles().get(index).getUserFiles().size());


        file.setInvoiceName(fileName);

        LocalDate localDate = LocalDate.now();
        System.out.println(localDate);
        file.setInvoiceDate(localDate.toString());
        file.setInvoiceFile(url);

        UserProfileController.getProfiles().get(index).addToUserFiles(file);
        System.out.println("Generated File Link: ' "+url+" '");

        model.addAttribute("redirectUrl" , "/userProfile/viewDetail/"+id);
        return "uploaded";
    }

    @RequestMapping("/userProfile/downloadZipFile/{id}")
    @ResponseBody
    public void downloadZipFile(@PathVariable("id") int id , @RequestParam(required = false) String selectedFiles, HttpServletResponse response , Model model) throws IOException {

        int index = getUserProfileIndex(id);
        String userDir = "user-files/"+getProfiles().get(index).getUserName()+"/file";
        String fileName;
        int selectedFilesLength = selectedFiles.split(" ").length;
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader("Content-Disposition", "attachment; filename=\""+selectedFilesLength+"-invoice-files.zip\"");

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
        ZipOutputStream zipOutputStream = new ZipOutputStream(bufferedOutputStream);


        for( int i=0; i< selectedFilesLength; i++){

            int fileIndex =parseInt(selectedFiles.split(" ")[i]);
            System.out.println("Fetching File: "+getProfiles().get(index).getUserFiles().get(fileIndex).getInvoiceFile());
            Resource res = StorageService.loadProfileFile(userDir , getProfiles().get(index).getUserFiles().get(fileIndex).getInvoiceFile());

            File file = res.getFile();
            zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
            FileInputStream fileInputStream = new FileInputStream(file);
            IOUtils.copy(fileInputStream, zipOutputStream);

            fileInputStream.close();
            zipOutputStream.closeEntry();
        }

        if (zipOutputStream != null) {
            zipOutputStream.finish();
            zipOutputStream.flush();
            IOUtils.closeQuietly(zipOutputStream);
        }
        IOUtils.closeQuietly(bufferedOutputStream);
        IOUtils.closeQuietly(byteArrayOutputStream);


        try{

            BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());

            String fileOutput="/home/subadev/IdeaProjects/spring boot users profile crud/user-files/"+getProfiles().get(index).getUserName()+"/ArchivedFile/"+selectedFilesLength+"-invoice-files.zip";

            if(new File(fileOutput).exists()){
                System.out.println("Zip File Exist");
            }else{
                Files.createFile(Paths.get(fileOutput));
            }


            try (FileOutputStream fos = new FileOutputStream(new File(fileOutput) , false)) {
                fos.write(byteArrayOutputStream.toByteArray());
            }
            getProfiles().get(index).addToArchivedFiles(fileOutput);
            FileInputStream fis = new FileInputStream(new File(fileOutput));
            int len;
            byte[] buf = new byte[1024];
            while((len = fis.read(buf)) > 0) {
                bos.write(buf,0,len);
            }
            bos.close();
            response.flushBuffer();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}