
console.log("This is jS")

const toggleSideBar = () =>{

    if($(".side-bar").is(":visible")){

        //close the side bar

        $(".side-bar").css("display","none")
        $(".content").css("margin-left","1%")
    }
    else{

        $(".side-bar").css("display","block")
        $(".content").css("margin-left","20%")

    }
};

const search =()=>{
    // console.log("seraching")

    let query = $("#search-input").val();

    if(query==''){

        $(".search-result").hide();
    }
    else{

        console.log(query);
        let url = 'http://localhost:8080/search/'+query;

        fetch(url)
           .then((response)=>{

             return response.json();
           })
           .then((data)=>{

            let text = `<div class = 'list-group'>`

            data.forEach(contact => {
                
                
                text+=`<a href='/user/${contact.cid}/contacts' class='list-group-item list-group-action' > ${contact.name} </a>`
            });

            text+=`</div>`;

            $(".search-result").html(text);
            $(".search-result").show();


           });

       
    }
}