package com.ethioclicks.userProfileCrud.controller;

import com.ethioclicks.userProfileCrud.services.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;

import static com.ethioclicks.userProfileCrud.controller.UserProfileController.baseUrl;
import static com.ethioclicks.userProfileCrud.controller.UserProfileController.getUserProfileIndex;


@Controller
public class VideoController {

  @PostMapping("/userProfile/uploadVideo")
    public String uploadVideo(@RequestParam( name = "userId" , required = false) int userId  , @RequestParam(name="userVideo") MultipartFile multipartFile , Model model) throws IOException {
        int index = getUserProfileIndex(userId);

        String name = UserProfileController.getProfiles().get(index).getUserName();
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        System.out.println("File Name: "+fileName);
        String uploadDir = "user-photos/"+name+"/video";
        StorageService.saveFile(uploadDir, fileName, multipartFile);


        System.out.println(name+" Uploaded a new Video");
        String url = baseUrl+"profile/"+name+"/video/"+fileName;
        UserProfileController.getProfiles().get(index).setUserVid(url);
        System.out.println("Generated Video Link: ' "+url+" '");

        model.addAttribute("redirectUrl" , "/userProfile/viewDetail/"+userId);
        return "uploaded";
    }


    @RequestMapping("/userProfile/downloadVideo/{id}")
    @ResponseBody

    public void downloadVideo(@PathVariable int id , HttpServletResponse response) throws IOException {
        int index = getUserProfileIndex(id);
        String userDir;
        String fileName;

        if(UserProfileController.getProfiles().get(index).getUserVid().endsWith("defaultVideo.mp4")){
            userDir = "user-photos/";
            fileName = "defaultVideo.mp4";
        }else{
            String str = UserProfileController.getProfiles().get(index).getUserVid();
            str = str.replace("http://localhost:8080/profile/" , "");
            userDir = str.split("/")[0];

            userDir = "user-photos/"+userDir+"/video";
            System.out.println("Image Directory: "+userDir);

            fileName = str.split("/")[2];
            System.out.println("FileName: "+fileName);
        }

        if(index == 0 && id != 1){
            userDir = "user-photos/";
            fileName = "defaultVideo.mp4";
        }

        Resource res = StorageService.loadProfilePic(userDir , fileName);
        System.out.println(UserProfileController.getProfiles().get(index).getUserName()+"'s Video is Being Downloaded...");

        response.setHeader("Content-Disposition", "attachment; filename=" +fileName);
        response.setHeader("Content-Transfer-Encoding", "binary");
        try{

            BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
            FileInputStream fis = new FileInputStream(res.getFile());
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
    @GetMapping("/fileManager")
    public String getFileManager(Model model){


        model.addAttribute("allProfiles" , UserProfileController.getProfiles());
        return "FileManager";
    }
}
