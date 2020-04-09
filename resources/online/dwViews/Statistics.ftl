<html>

	<head>
		<!-- Web page title -->
    	<title>Statistics - Top Trumps</title>
    	
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
    <div class="mb"></div>
    <div class="row container">
        <div class="card" style="width: 100%;">
          <div class="card-header" onclick="window.location='game'">
            New Game
          </div>
          <ul class="list-group list-group-flush">
            <li class="list-group-item">Number of Games:<span id="total"><i class="fa fa-spinner fa-spin"></i></span></li>
            <li class="list-group-item">Number of Human wins:<span id="human"><i class="fa fa-spinner fa-spin"></i></span></li>
            <li class="list-group-item">Number of AI wins:<span id="ai"><i class="fa fa-spinner fa-spin"></i></span></li>
            <li class="list-group-item">Average Draws per game:<span id="drwas"><i class="fa fa-spinner fa-spin"></i></span></li>
            <li class="list-group-item">Longest Game:<span id="longest"><i class="fa fa-spinner fa-spin"></i></span></li>
          </ul>
        </div>
    </div>

    </div><!-- /.container -->
		
		<script type="text/javascript">
            var loaded = 0;
            /*<div class="card" style="width: 18rem;">
              <img src="..." class="card-img-top" alt="...">
              <div class="card-body">
                <p class="card-text">Some quick example text to build on the card title and make up the bulk of the card's content.</p>
              </div>
            </div>*/
            var aiplayers = 0;
            var players = [];
            var cate = [];
            var roundnum;
            function getStats(){
                $.get('http://localhost:7777/toptrumps/game/stats',function (res){
                    $('#total').html(res[0]);
                    $('#human').html(res[1]);
                    $('#ai').html(res[2]);
                    $('#drwas').html(res[3]);
                    $('#longest').html(res[4]);
                
                },'json');
            
            }
			// Method that is called on page load
			function initalize() {
			
				// --------------------------------------------------------------------------
				// You can call other methods you want to run when the page first loads here
				// --------------------------------------------------------------------------
				getStats();
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