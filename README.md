<p align="center">
  <a href="https://ethioclicks.com" rel="noopener" target="_blank"><img width="150" src="https://avatars.githubusercontent.com/u/84285742?v=4" alt="ethioclicks logo"></a>
</p>
<h1 align="center">ethio clicks</h1>
<h2 align="center"> Spring Boot Simple Users Profile Manager CRUD Web App </h2>

The main objective of the spring boot project in this repository is to create a fullstack web application for implementing and testing spring boot as a backend to implement a File Upload and Download functionality. 

In this project we are going to create a simple demo of user profile manager web application that enables to create , edit , delete and update user profile information, each and every user profile consists of profile picture as well as a video file,which is our main target for implementing and testing the file upload and download functionality.


### We Used

Development Machine:- Linux(debian based Linux OS). <br />
Spring Web <br />
Thymeleaf <br />


### Main Landing Page of the web app

<hr>

![landingPage](https://user-images.githubusercontent.com/88676535/165318743-04a5a50f-36d4-4abd-becd-73446dd2a920.png)

<hr>

To demonstrate file uploading, we'll be building a typical Spring MVC application which consists of a `Controller`, a `Service` for backend processing, and `Thymeleaf` for view rendering , [thymeleaf](https://www.thymeleaf.org/documentation.html) is a modern server-side Java template engine.

The simplest way to start with a skeleton Spring Boot project, as always, is using Spring Initializer. Select your preferred version of Spring Boot and add the Web and Thymeleaf dependencies:

![spring starter](https://user-images.githubusercontent.com/88676535/167289063-22107abc-1997-4bab-9a8c-1e467076f6f3.png)

Spring provides a MultipartFile interface to handle HTTP multi-part requests for uploading files. Multipart-file requests break large files into smaller chunks which makes it efficient for file uploads.



### Configure and Increase the Spring Boot File Upload Size Limit

![properties](https://user-images.githubusercontent.com/88676535/166115921-bbde902c-415c-40ed-ae3f-29b891c23676.png)

by default spring boot allows uploading only upto 1MB size of files via multipart form and the default for `spring.servlet.multipart.max-request-size` is 10MB. Increasing the limit for max-file-size is probably a good idea since the default is very low, but be careful not to set it too high, which could overload your server.


so inorder to enable our web application allow uploading larger files which are greater than 1MB we must modify some configurations and override the default multipart upload size limit inside the spring boot configuration file , every spring boot project may modify the configuration file differently because of so many options available out there to use external configuration for defining the spring boot project property but each of them will allow us to modify the default values.

here we will use the application file, the application file will help in this scenario since in the  `application.properties`  each line is a single configuration used to set and modify the default value, depending on the version of spring boot, add different configurations to the application file, the file which is found inside `src>main>resource`. <br /> nevigate to `src>main>resource` from your project directory and you will get the the application file.

According to Your spring boot version append the following line of codes to this file , as an example let's increase the upload limit upto 2GB.

you can set the limits in `KB`,`MB`,`GB`...etc

#### Before Spring Boot 2.0:

```
spring.http.multipart.max-file-size=2048MB
spring.http.multipart.max-request-size=2048MB

```

#### For those using Spring Boot 2.0 and above (as of M1 release), the property names have changed to:

```

spring.servlet.multipart.max-file-size=2048MB
spring.servlet.multipart.max-request-size=2048MB

```

in this code carfully Note that the prefix have changed to `spring.servlet` instead of writing `spring.http` we have to use servlet after the dot.

#### There is also another option for those who prefer to use yaml configuration 

inside your application.yaml add the following line of code , this code will override the default limit for multipart file upload and enable it to accept uploading extra large size files. depending your goal you can set the value but here we will demonstrate by setting the upload limmit upto 2GB.


```

spring:
 servlet:
    multipart:
      max-file-size: 2048MB
      max-request-size: 2048MB
      
```  

### Image Upload Controller

```java

@Controller
public class ImageController {

    @PostMapping("/uploadImage")
    public String uploadImage(@RequestParam( name = "name" , required = false) String name , Model model , @RequestParam("image") MultipartFile multipartFile) throws IOException {

        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        System.out.println("Picture File Name: "+fileName);
        String uploadDir = "user-photos/"+name;
        System.out.println(name+"\'s Profile Picture Upload in Progress... ");
        StorageService.saveFile(uploadDir, fileName, multipartFile);
        model.addAttribute("userId" , numberOfProfiles);
        model.addAttribute("profilePic", baseUrl+"profile/"+name+"/"+fileName);
        model.addAttribute("name" , name);
        return "addNew";
    }

```

### Upload Photo 
![uploaad profile picture](https://user-images.githubusercontent.com/88676535/165319172-d8a55af3-0650-4e4d-8f6e-101f7cbd9ab4.png)

we have a simple thymleaf page with a form that maps directly to the /uploadImage URL with form method post and it's enctype is multipart/form-data with an input type as a File. the uploaded picture is saved inide a newly created folder by the name of a user that uploaded the piture and overall all of each user folders are created in  `user-photos` directory. 

![Screenshot_2022-05-08_03-30-01](https://user-images.githubusercontent.com/88676535/167301055-e36fc8b5-00ec-4d4c-969b-45a54c4f45e0.png)


![Screenshot_2022-05-08_03-29-03](https://user-images.githubusercontent.com/88676535/167300997-1f9beea0-5d87-4d19-9c33-398938ce89c9.png)


### Image Download Controller 

```java

 @RequestMapping("/userProfile/downloadPhoto/{id}")
    @ResponseBody

    public void downloadPic(@PathVariable int id , HttpServletResponse response) throws IOException {
        int index =  UserProfileController.getUserProfileIndex(id);
        String str = UserProfileController.getProfiles().get(index).getProfilePic();
        str = str.replace("http://localhost:8080/profile/" , "");
        String imageDir = str.split("/")[0];
        imageDir = "user-photos/"+imageDir;
        System.out.println("Image Directory: "+imageDir);
        String fileName = str.split("/")[1];
        System.out.println("FileName: "+fileName);
        if(index == 0 && id != 1){
            imageDir = "user-photos/";
            fileName = "user.png";
        }
        Resource res = StorageService.loadProfilePic(imageDir , fileName);
        System.out.println(fileName+" is being Downloaded...");
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
            System.out.println("Program Unable to Download the Image, Error Detail:- "+e.toString());
       }
    }
    
```

inside the View Detail page of specific user profile, hovering on the profile picture will make the download button to popup and clicking the download button will send request to the above endpoint with the the users id , and based on the provided id the above endpoint will search for the picture and it will return the picture data through the response File stream.

![image download](https://user-images.githubusercontent.com/88676535/167300058-aaf82e94-64c9-4b2c-b242-89195ff4556a.png)


### Custom Exception

There is a custom FileStorageException for any exception during the file upload process. It's a simple class that extends RuntimeException:


```java

package com.ethioclicks.userProfileCrud.exception;

public class FileStorageException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private String msg;

    public FileStorageException(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}

```
To be able to use the exception in the way that we did, Spring needs to know how to deal with it if it's encountered. For that, we've created an `AppExceptionHandler` which is annotated with `@ControllerAdvice` and has an `@ExceptionHandler` defined for `FileStorageException:`

```java

@ControllerAdvice
public class AppExceptionHandler {

    @ExceptionHandler(FileStorageException.class)
    public ModelAndView handleException(FileStorageException exception, RedirectAttributes redirectAttributes) {

        ModelAndView mav = new ModelAndView();
        mav.addObject("message", exception.getMsg());
        mav.setViewName("error");
        return mav;
    }
}

```



```html

<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
  <meta charset="UTF-8">
  <title>ERROR</title>
</head>
<body>
  <h1>Error!!!</h1>
  <div th:if="${message}">
    <h2 th:text="${message}"/>
  </div>
</body>
</html>

```


developed with ❤️ in ethioclicks
