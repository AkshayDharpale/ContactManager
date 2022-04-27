console.log("console is working");

const toggleSidebar = () => {
    if($(".sidebar").is(":visible")){

        $(".sidebar").css("display", "none");
        $(".content").css("margin-left", "0%")


    }else{
        $(".sidebar").css("display", "block");
        $(".content").css("margin-left", "23%")

    }
}

const search = ()=>{
    let a = document.querySelector("#search-input").value;

    if(a === ""){
        console.log("hide query is running");
        $(".search-result").hide();

    }else{
         console.log(a);
         let url = `http://localhost:8080/search/${a}`;

         fetch(url).then((response)=>{

             return response.json();

         }).then((data)=>{
         console.log(data);

           let text = `<div class='list-group'>`;

           data.forEach((element) => {
                text += `<a href='/user/ContactDetails/${element.id}'  class='list-group-item list-group-action '> ${element.name}</a>`;
          });

              text += `</div>`;
               $(".search-result").html(text);
                $(".search-result").show();
          });

    }

}