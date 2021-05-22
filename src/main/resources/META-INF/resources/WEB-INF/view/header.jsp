<!DOCTYPE html>
<html>
<head>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<title>Insert title here</title>

<script src="css/jquery.min.js"></script>
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/custom.css" rel="stylesheet">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<script async defer crossorigin="anonymous" src="https://connect.facebook.net/en_US/sdk.js"></script>
<!-- <script src="css/jquery.min.js"></script>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-wEmeIV1mKuiNpC+IOBjI7aAzPcEZeedi5yW5f2yOq55WWLwNGmvvx4Um1vskeMj0" crossorigin="anonymous">
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-p34f1UUtsS3wqzfto5wAAmdvj+osOnFyQFpp4Ua3gs/ZVWx6oOypYoCJhGGScy+8" crossorigin="anonymous"></script>
 -->

<script>
 
 
 function checkFacebookLoginStatusCallback(response) {  // Called with the results from FB.getLoginStatus().
	    console.log(' Checking Facebook Login Status');
	    console.log(response);                   // The current login status of the person.
	    if (response.status === 'connected') {   // Logged into your webpage and Facebook.
	      getUserProfile();  
	    } else {                                 // Not logged into your webpage or we are unable to tell.
	      document.getElementById('status').innerHTML = 'Please login with Facebook into this webpage.';
	    }
	  }
 
 function checkLoginState() {               // Called when a person is finished with the Login Button.
	    FB.getLoginStatus(function(response) {   // See the onlogin handler
	      checkFacebookLoginStatusCallback(response);
	    });
	  }

	    FB.getLoginStatus(function(response) {   // Called after the JS SDK has been initialized.
	      checkFacebookLoginStatusCallback(response);        // Returns the login status.
	    });

	     function getUserProfile() {                      // Get User Profile using Facebook Graph API after login.
	    console.log('Welcome!  Fetching your profile information.... ');
	    FB.api('/me', function(response) {
	      console.log('Successful login for: ' + response.name);
	      document.getElementById('status').innerHTML =
	        'Thanks for logging in, ' + response.name + '!';
	    });
	  }
 
 var lat;
 var lon;
 var init;
 var varFrom;
 
 function checkUserSession(){
	 //localStorage.clear();
	 i = localStorage.getItem('i');
	 if(i == undefined || i == null ||i == "undefined" || i =="0" || i==0){
		 //User is not valid show a message of login on page and set default values
		 setUserSession(0,0,0);
		 $("#logoutId").hide();
		 $("#loginId").show();
	 }else{
		 $("#logoutId").show();
		 $("#loginId").hide();
	 }
	 fetchCurrentLocation();
	 
 }
 
 function setUserSession(passC,passI,passO){
	 localStorage.setItem('c', passC);
	 localStorage.setItem('i', passI);
	 localStorage.setItem('o', passO);
	 
 }
 
 
 function fetchCurrentLocation(){
	 
	 
	 if (navigator.geolocation) {
			navigator.geolocation.getCurrentPosition(setPosition);
		} else {
			Alert("Geolocation is not supported by this browser. Please use latest Chrome, Firefox, Opera or IE");
		}
 }
 
 function setPosition(position){
	 
	 lat = position.coords.latitude;
	 lon = position.coords.longitude;
	 localStorage.setItem('lat',lat);
	 localStorage.setItem('lon',lon);
	 $("#data").html("");
	 
	 fetchOpenRequests(0);
	 
 }
 
 function logout(){
		
	 var c = localStorage.getItem("c");
	 var i = localStorage.getItem("i");
	 var o = localStorage.getItem("o");
	 var reqStr = "{\"c\":"+c+",\"i\":"+i+",\"o\":"+o+"}";
	//	var reqStr = "{\"c\":222222222222,\"i\":2,\"o\":222222}";

		var jsonData = JSON.parse(reqStr);
		
		$.post("/logout", jsonData).done(function(data) {
			setUserSession(0,0,0);
			localStorage.clear();
			showLoginMessage();
		}).fail(function(res) {
			localStorage.clear();
			alert("Failed " + res)
		});

	}
 
 
 </script>
</head>
<body onload="generateInitialPageView()">
<script>
	 windows.fbAsyncInit = function(){
		 FB.init({
			 appId:'159649879369291',cookie:true,xfbml:true,version:'v10.0'
		 });
		 FB.AppEvents.logPageView();
	 };
	 
	 (function (d,s,id){
		 var js, fjs=d.getElementsByTagName(s)[0];
		 if(d.getElementById(id)){return;}
		 js=d.createElement(s);js.id=id;
		 js.src="https://connect.facebook.net/en_US/sdk.js";
		 fjs.parentNode.insertBefore(js,fjs);
	 }(document,'script','facebook-jssdk'));
</script>
	<nav class="navbar navbar-expand-lg navbar-dark bg-primary bg-gradient">
		<div class="container-fluid">
			<a class="navbar-brand right-border padding-right-1" href="/">MINUS</a>
			<button class="navbar-toggler" type="button"
				data-bs-toggle="collapse" data-bs-target="#navbarNav"
				aria-controls="navbarNav" aria-expanded="false"
				aria-label="Toggle navigation">
				<span class="navbar-toggler-icon"></span>
			</button>
			<div class="collapse navbar-collapse" id="navbarNav">
				<ul class="navbar-nav">
					<li class="nav-item"><a class="nav-link active"
						aria-current="page" href="/">Home</a></li>

					<li class="nav-item"><a class="nav-link" href="/about-us">About</a>
					</li>

				</ul>

				<ul class="nav navbar-nav ms-auto">
					<li class="nav-item right-border"><a
						class="nav-link navbar-right" href="#" tabindex="-1"
						aria-disabled="true" onClick="fetchCurrentLocation()">Refresh
							Location</a></li>
					<li id="logoutId" class="nav-item "><a
						class="nav-link navbar-right" href="#" tabindex="-1"
						aria-disabled="true" onClick="logout()">Logout</a></li>
					<li id="loginId" class="nav-item "><a
						class="nav-link navbar-right" href="#" tabindex="-1"
						aria-disabled="true" onClick="showLoginPopUp()">Login</a></li>
				</ul>
			</div>
		</div>
	</nav>