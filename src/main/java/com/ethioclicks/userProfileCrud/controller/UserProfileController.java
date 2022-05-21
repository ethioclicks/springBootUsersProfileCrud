package com.ethioclicks.userProfileCrud.controller;

import com.ethioclicks.userProfileCrud.model.Invoice;
import com.ethioclicks.userProfileCrud.model.UserProfile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Controller
public class UserProfileController {
    public static List<UserProfile> getProfiles() {
        return profiles;
    }

    private static List<UserProfile> profiles = new ArrayList<>();
    private static boolean isInitialized = false;
    public static int numberOfProfiles = 0;
    private static String profilePic = "http://localhost:8080/storage/user.png";
    public static String baseUrl = "http://localhost:8080/";

    private static List<UserProfile> init() {
        Invoice paulosPdfFile = new Invoice(0 , "Paulos's PDF Document" , "1","Paulos's PDF Document.txt");
        Invoice paulosWordFile = new Invoice(1, "Paulos's Word Document" , "1","Paulos's Word Document.txt");
        Invoice frePdfDoc = new Invoice(0 , "My Daily Expense" , "1","My Daily Expense.txt");
        Invoice freWordDoc = new Invoice(1, "Ethio Telecom Budget" , "1","Ethio Telecom Budget.txt");

        UserProfile paulos = new UserProfile( 2,"Paulos Yibelo", baseUrl+"storage/Paulos Yibelo/paulos yibelo.jpg" , 6 , "male");
        paulos.addToUserFiles(paulosPdfFile);
        paulos.addToUserFiles(paulosWordFile);

        UserProfile frehiwot = new UserProfile( 1,"CEO Frehiwot Tamiru" , baseUrl+"storage/CEO Frehiwot Tamiru/fire.jpg" , 6 ,"female");
        frehiwot.addToUserFiles(frePdfDoc);
        frehiwot.addToUserFiles(freWordDoc);

        profiles.add(frehiwot);
        profiles.add(paulos);
       return profiles;
    }

    public static int getUserProfileIndex(int userId){
        int index =0;
        for( int i = 0; i < UserProfileController.getProfiles().size() ;  i++)
        {
            if(UserProfileController.getProfiles().get(i).getUserId() == userId){
                index = i;
            }
        }

        return index;
    }
    private static List<UserProfile> addNewProfile(UserProfile newProfile) {
        if(newProfile != null)
        profiles.add(newProfile);
        return profiles;
    }

    private static List<UserProfile> updateProfile( int userId , UserProfile updateUser){
        int index = getUserProfileIndex(userId);
        profiles.set(index , updateUser);
        return profiles;
    }

    @GetMapping("/")
    public String getAllProfiles(Model model){
        if(profiles.isEmpty() && !isInitialized){
            init();
            isInitialized = true;
            numberOfProfiles += 3;
        }

        model.addAttribute("allProfiles" , profiles );
        return "index";
    }

    @GetMapping("/userProfile/viewDetail/{id}")
    public String viewDetailPage(@PathVariable int id , Model model){

        int index = getUserProfileIndex(id);
        List<Invoice> files = getProfiles().get(index).getUserFiles();

        for(int i = 0; i< getProfiles().get(index).getUserFiles().size(); i++){
            System.out.println("File "+(i+1)+" -> "+ getProfiles().get(index).getUserFiles().get(i).getInvoiceName());
        }

        model.addAttribute("userFiles" , getProfiles().get(index).getUserFiles());
        model.addAttribute("user", getProfiles().get(index));
        model.addAttribute("numberOfFiles", getProfiles().get(index).getUserFiles().size());
        model.addAttribute("selectedFilestoArchive",InvoicesController.selectedFilestoArchive);
        return "viewDetail";
    }

    @GetMapping("addNew")
    public String addNew(Model model){
        model.addAttribute("userId" , numberOfProfiles);
        model.addAttribute("profilePic", profilePic);

        return "addNew";
    }

    @PostMapping( value= "addNewProfile" )
    public String addProfile(@ModelAttribute UserProfile newProfile , Model model){

        numberOfProfiles += 1;
        // reset the default image back for a new user in the future
        profilePic = "http://localhost:8080/storage/user.png";
        addNewProfile(newProfile);
        return "redirect:/";
    }

   @DeleteMapping("/userProfile/deleteProfile/{id}")
    public String deleteProfile(@PathVariable int id , Model model){
        deleteProfile(id);
        return "redirect:/";

   }

   private void deleteProfile(int id) {
       int i = getUserProfileIndex(id);
       System.out.println("Removing: " + profiles.get(i).getUserName() + " age: " + profiles.get(i).getAge());
       profiles.remove(i);
       System.out.println("Removed Successfully");
   }

    // edit and update user profile
    @GetMapping("/userProfile/edit/{id}")
    public String editProfile(Model model , @PathVariable int id){
        int index = getUserProfileIndex(id);
        model.addAttribute("title" , "Edit "+profiles.get(index).getUserName()+"'s Profile info");
        model.addAttribute("user", profiles.get(index));
        Boolean isMale = false;
        if(profiles.get(index).getGender().equals("male")){
            isMale= true;
        }
        model.addAttribute("isMale" , isMale);
        model.addAttribute("isFemale" , !isMale);
        return "editProfile";
    }

    @PostMapping("/userProfile/update/{id}")
    public String updateProfile(@ModelAttribute UserProfile updatedUser  , @PathVariable int id){

       System.out.println("Gender: "+updatedUser.getGender());
       updateProfile(id , updatedUser);
       return "redirect:/";
    }
}