  function deleteUser(className){

      let confirmDelete = confirm("Do You Really Want to Delete this User? \n The User is Going to be Deleted Click Ok to Confirm");
      if(confirmDelete){
        let deleteBtn = document.querySelector(".deleteBtn"+className);
        deleteBtn.click();
      }
  }