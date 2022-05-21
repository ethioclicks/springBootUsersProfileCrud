package com.ethioclicks.userProfileCrud.model;


import java.util.ArrayList;
import java.util.List;

public class UserProfile {
  private  int userId;
  private String userName;
  private String profilePic;
  private Integer age;
  private String gender;
  private String userVid;
  private List<String> archivedFiles = new ArrayList<>();
  private List<Invoice> userFiles = new ArrayList<>();


  public String getUserVid() {
    return userVid;
  }

  public void setUserVid(String userVid) {
    this.userVid = userVid;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public String getProfilePic() {
    return profilePic;
  }

  public void setProfilePic(String profilePic) {
    this.profilePic = profilePic;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public List<Invoice> getUserFiles() {
    return userFiles;
  }

  public void addToUserFiles(Invoice newfile) {

    if(newfile != null){
      this.userFiles.add(newfile);
    }
    else{
      System.out.println("Adding a new File has Failed!");
    }
  }

  public List<String> getArchivedFiles() {
    return archivedFiles;
  }

  public void addToArchivedFiles(String archivedFile) {

    if(!archivedFile.isEmpty()){
      archivedFiles.add(archivedFile);
    }
    else{
      System.out.println("Adding a new Archive File Failed!");
    }
  }

  public void setUserFiles(List<Invoice> userFiles) {
    this.userFiles = userFiles;
  }

  public UserProfile(int id , String userName, String profilePic, Integer age, String gender) {
    this.userId = id;
    this.userName = userName;
    this.profilePic = profilePic;
    this.age = age;
    this.gender = gender;
    this.userVid = "http://localhost:8080/profile/defaultVideo.mp4";
  }
}
