function popupDisplay(open){
        if(open){

                 var p = document.querySelector(".photo-popup");
                 p.style.visibility = "visible";
                 var userName = document.getElementById("userName");
                 document.getElementById("name").value = userName.value;

            }else{

             var p = document.querySelector(".photo-popup");
             p.style.visibility = "hidden";
            }
        }
              function popupDisplayVideo(open){
                if(open){

                         var p = document.querySelector(".video-popup");
                         p.style.visibility = "visible";

                    }else{

                     var p = document.querySelector(".video-popup");
                     p.style.visibility = "hidden";
                    }
                }