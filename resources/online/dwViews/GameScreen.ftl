<html>

	<head>
		<!-- Web page title -->
    	<title>GameScreen - Top Trumps</title>
    	
    	<!-- Import JQuery, as it provides functions you will probably find useful (see https://jquery.com/) -->
    	<script src="https://code.jquery.com/jquery-2.1.1.js"></script>
    	<script src="https://code.jquery.com/ui/1.11.1/jquery-ui.js"></script>
    	<link rel="stylesheet" href="https://code.jquery.com/ui/1.11.1/themes/flick/jquery-ui.css">

		<!-- Optional Styling of the Website, for the demo I used Bootstrap (see https://getbootstrap.com/docs/4.0/getting-started/introduction/) -->
		<link rel="stylesheet" href="http://dcs.gla.ac.uk/~richardm/TREC_IS/bootstrap.min.css">
    	<script src="http://dcs.gla.ac.uk/~richardm/vex.combined.min.js"></script>
    	<script>vex.defaultOptions.className = 'vex-theme-os';</script>
    	<link rel="stylesheet" href="http://dcs.gla.ac.uk/~richardm/assets/stylesheets/vex.css"/>
    	<link rel="stylesheet" href="http://dcs.gla.ac.uk/~richardm/assets/stylesheets/vex-theme-os.css"/>
    	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css">
		<style>
		  body{
		    width: 100%;
		    height: 100vh;
		    background: linear-gradient(-45deg, #ee7752, #e73c7e, #23a6d5, #23d5ab);
		    background-size: 400% 400%;
		    animation: gradientBG 15s ease infinite;
		    }
		       
		
		    @keyframes gradientBG {
		        0% {
		            background-position: 0% 50%;
		        }
		        50% {
		            background-position: 100% 50%;
		        }
		        100% {
		            background-position: 0% 50%;
		        }
		    }
            .navs{
                background-color: black;
                padding-top: 10px;
                padding-bottom: 10px;
                }
            .mb{
                margin-bottom: 20px
                }
            .card{
                padding:0!important;
                height:360px
                }
             .card-body {
                padding: 1rem;
            }
	  </style>
</head>

    <body onload="initalize();"> <!--  onload="initalize()" Call the initalize method when the page loads -->
    	
    	<div class="container">
    <div class="navs">
      <div class="container">
        <div class="nav-header">
          <span class="nav-brand" style="color:#fff">Top Trumps Game</span>
        </div><!--
        <div id="navbar" class="collapse navbar-collapse">
          <ul class="nav navbar-nav">
            <li class="active"><a href="#">Home</a></li>
            <li><a href="#about">About</a></li>
            <li><a href="#contact">Contact</a></li>
          </ul>
        </div>--><!--/.nav-collapse -->
      </div>
    </div>
    <div class="alert alert-info" role="alert" id="title">
      Loading...
    </div>
    <div class="mb"></div>
      <div class="row hide" id="op2">
        <a class="btn btn-success" onclick="round();">NEXT ROUND</a>
      </div>
      <div class="row" id="op1">
        <div class="col-md-4">
            <div class="alert alert-info" role="alert" id="top">
                  Loading...
            </div>
            <div id="btm"></div>
    		<div class="mb"></div>
            <div class="alert alert-info" style="display:none;" role="alert" id="small">
              Loading...
            </div>
        </div>
        <div class="col-md-8">
            <div id="rgt" class="row">
            </div>
        </div>
      </div>

    </div><!-- /.container -->
		
		<script type="text/javascript">
            var loaded = 0;
            /*<div class="card" style="width: 18rem;">
              <img src="http://lib.application.pub/public/pic/" class="card-img-top" alt="...">
              <div class="card-body">
                <p class="card-text">Some quick example text to build on the card title and make up the bulk of the card's content.</p>
              </div>
            </div>*/
            var aiplayers = 0;
            var players = [];
            var cate = [];
            var roundnum;
            function GetQueryString(name)
			{
			     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
			     var r = window.location.search.substr(1).match(reg);
			     if(r!=null)return  unescape(r[2]); return null;
			}
            function loadcate(){
                
                $.get('http://localhost:7777/toptrumps/game/attr',function(res){
                    cate = res;
                })
            }
            function loadgame(){
                roundnum = 0;
                ainum = GetQueryString("ainum");
                vals = ainum?'?aiplayer='+ainum:'?aiplayer=4';
                $.get('http://localhost:7777/toptrumps/game/new'+vals,function(res){
                    loaded = 1;
                    cate = res;
                    round();
                })
            }
            function genplayers(){
                players[0]='You';
                for(i=0;i<aiplayers;i++){
                    players[i+1]='AI Player '+(i+1);
                }
            }
            function selection(active){
                str='';
                if(active==0){
                    for(i=0;i<cate.length;i++){
                        str += '<button type="button" class="btn btn-success btn-lg btn-block" onclick="choose('+(i+1)+')">'+cate[i]+'</button>';
                    }
                    $('#title').html('"Waiting on you to select a category"');
                    $('#btm').html(str);
                }else{
                    choose(0);
                
                }
            }
            function showcard(player,color,left,data){
                data = data.split('#');
                str='';
                //console.log(data);
                for(i=0;i<cate.length;i++){
                    str=str+'<p class="card-text">'+cate[i]+' , '+data[i+1]+'</p>';
                }
                all = '<div class="card col-md-3" style=""> \
                <button type="button" class="btn btn-'+color+' btn-lg btn-block">'+player+'</button>\
                <p class="card-text" style="margin:0;padding:0.5rem">'+data[0]+'<span class="badge badge-primary" style="top: -0.1rem;position: relative;">'+(left>40?40:left)+'</span></p>\
              <img src="http://lib.application.pub/public/pic/'+data[0]+'.jpg" class="card-img-top" alt="" style="height:5rem"> \
              <div class="card-body"> '+str+'\
              </div></div>';
                return all;
                
            }
            function showwinner(winner,common){
                $('#op2').show();
                $('#op1').hide();
                if(winner=='draw'){
                    $('#title').html('"Round '+roundnum+': This round was a Draw, common pile now has '+common+' cards"');
                }else{
                    $('#title').html('"Round '+roundnum+': Player '+players[winner]+' won this round"');
                }
            }
            function choose(select){
                select = select;
                $.ajax({
                type:'post',
                dataType:'json',
                contentType:'application/json',
                url:"http://localhost:7777/toptrumps/game/round?cate="+select,
                data:JSON.stringify({Word:'qaqaa'}),
                success: function(data){
                    //alert(data);
                    str = '';
                    playercards = JSON.parse(data.playercards);console.log(aiplayers);
                    for(ij=0;ij<=aiplayers;){
                        i=ij;
                        console.log(data['player'+i]);console.log(data.status==i);
                        if(typeof(data['player'+i])!='undefined' && data['player'+i]!='#####'){
                            str = str+showcard(players[i],data.status==i?'success':'warning',playercards[i],data['player'+i]);
                        }
                        ij++;
                    }
                    $('#rgt').html(str);
                    
                    if(data.lose=='1'){
                        $('#small').show();
                        $('#small').html('You have Lost!');
                    }
                    if(data.end=='1'){
                        showwinner(data.status,data.common);
                        $('#op2').hide();
                        $('#op1').show();
                        //$('#title').html('You have Lost!');
                        $.get('http://localhost:7777/toptrumps/game/score',function(res){
                            strs = '';
                            
                            for(ii=0;ii<players.length;ii++){
                                if(ii==data.status){
                                    strs += '<p style="background-color: #fff;padding: 10px;border-radius: 5px;">The winner was '+players[ii]+', the winner';
                                }else{
                                    strs += '<p style="background-color: #fff;padding: 10px;border-radius: 5px;">'+players[ii]+' lost overall, but';
                                }
                                strs += ' won '+res[ii]+' rounds</p>';
                            }
                            $('#btm').html('<button type="button" class="btn btn-success btn-lg btn-block" onclick="window.location=\'/toptrumps/\'">RETURN TO THE SELECT SCREEN</button>'+strs);
                        },'json');
                        //'They selected "'+cate[data.choose-1]+'"</p>';
                    }else{
                        $('#btm').html('<p style="background-color: #fff;padding: 10px;border-radius: 5px;">They selected "'+cate[data.choose-1]+'"</p><button type="button" class="btn btn-success btn-lg btn-block" onclick="showwinner(\''+data.status+'\','+data.common+');">SHOW WINNER</button>');
                    }
                }
                });
            }
            function round(){
                $('#op1').show();
                $('#op2').hide();
                    $.get('http://localhost:7777/toptrumps/game/round',function(res){
                        if(res.round==1){
                            aiplayers = res.aiplayers;
                            genplayers();
                        }
                        if(res.nowcard!='####'){
                            str = showcard('You','success',res.havecard,res.nowcardname+'#'+res.nowcard);
                        }
                        roundnum = res.round;
                        $('#title').show();
                        $('#title').text('"Round '+res.round+': Players have drawn their cards"');
                        
                        $('#top').text('The active player is '+players[res.nowround]);
                        $('#btm').html('<button type="button" class="btn btn-success btn-lg btn-block" onclick="selection('+res.nowround+');">NEXT:CATEGORY SELECTION</button>');
                        $('#rgt').html(str);
                    },'json')
                    
            }
            function shownextbtn(){
            
                $('#op2').show();
                $('#op1').hide();
            }
        
		
			// Method that is called on page load
			function initalize() {
			
				// --------------------------------------------------------------------------
				// You can call other methods you want to run when the page first loads here
				// --------------------------------------------------------------------------
				loadgame();
                if(loaded!=1){
                
                }else{
                }
				// For example, lets call our sample methods
				//helloJSONList();
				//helloWord("Student");
				
			}
			/**/
			
			// -----------------------------------------
			// Add your other Javascript methods Here
			// -----------------------------------------
		
			// This is a reusable method for creating a CORS request. Do not edit this.
			function createCORSRequest(method, url) {
  				var xhr = new XMLHttpRequest();
  				if ("withCredentials" in xhr) {

    				// Check if the XMLHttpRequest object has a "withCredentials" property.
    				// "withCredentials" only exists on XMLHTTPRequest2 objects.
    				xhr.open(method, url, true);

  				} else if (typeof XDomainRequest != "undefined") {

    				// Otherwise, check if XDomainRequest.
    				// XDomainRequest only exists in IE, and is IE's way of making CORS requests.
    				xhr = new XDomainRequest();
    				xhr.open(method, url);

 				 } else {

    				// Otherwise, CORS is not supported by the browser.
    				xhr = null;

  				 }
  				 return xhr;
			}
		</script>
		
		<!-- Here are examples of how to call REST API Methods -->
		<script type="text/javascript">
		
			// This calls the helloJSONList REST method from TopTrumpsRESTAPI
			function helloJSONList() {
			
				// First create a CORS request, this is the message we are going to send (a get request in this case)
				var xhr = createCORSRequest('GET', "http://localhost:7777/toptrumps/helloJSONList"); // Request type and URL
				
				// Message is not sent yet, but we can check that the browser supports CORS
				if (!xhr) {
  					alert("CORS not supported");
				}

				// CORS requests are Asynchronous, i.e. we do not wait for a response, instead we define an action
				// to do when the response arrives 
				xhr.onload = function(e) {
 					var responseText = xhr.response; // the text of the response
					alert(responseText); // lets produce an alert
				};
				
				// We have done everything we need to prepare the CORS request, so send it
				xhr.send();		
			}
			
			// This calls the helloJSONList REST method from TopTrumpsRESTAPI
			function helloWord(word) {
			
				// First create a CORS request, this is the message we are going to send (a get request in this case)
				var xhr = createCORSRequest('GET', "http://localhost:7777/toptrumps/helloWord?Word="+word); // Request type and URL+parameters
				
				// Message is not sent yet, but we can check that the browser supports CORS
				if (!xhr) {
  					alert("CORS not supported");
				}

				// CORS requests are Asynchronous, i.e. we do not wait for a response, instead we define an action
				// to do when the response arrives 
				xhr.onload = function(e) {
 					var responseText = xhr.response; // the text of the response
					alert(responseText); // lets produce an alert
				};
				
				// We have done everything we need to prepare the CORS request, so send it
				xhr.send();		
			}

		</script>
		
		</body>
</html>