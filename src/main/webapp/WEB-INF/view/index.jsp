<%@ include file="header.jsp"%>


<!-- Display all near by requests -->

<script type="text/javascript">

	var pageNumber = 1;
	var categories;
	var isPop = false;
	
	function fetchNearMeOpenRequests() {
		$.get("/near-me?x=" + localStorage.getItem("lat") + "&y="
				+ localStorage.getItem("lon") + "&page=" + pageNumber, function(
				response, status) {
			$("#myModal").hide();
			if (response.length > 0) {
				displayNearMeRequests(response);
			}else{
				$("#data").append("<h4 class='padding-half'>Currently you don't have any open requests</h4>");
			}
		});
	}
	
	function getTimeElapsed(ms, ems){
		var mins = (ms/1000 - ems)/60; 
		if(mins <= 1){
			return "Created Just Now";
		}else if(mins<60){
			return "Created "+Math.round(mins)+" Minutes back";
		}else if(mins < 1440){
			return "Created "+Math.round(mins/60)+" Hours back";
		}else{
			return "<span class='text-danger'>Created "+Math.round(mins/1440)+" Days back</span>";
		}
	}

	function displayNearMeRequests(reqs) {
		
		isPop = false;
		var i;
		var html = "";
		var ms = Date.now();
		for (i = 0; i < reqs.length; i++) {
			html = html
					+ "<div class='card pointer' onClick='fetchChats("
					+ reqs[i].requestId
					+ ")' style='width: 80%;'><div class='card-body'><h5 class='card-title'>"
					+ reqs[i].subCategoryName
					+ "</h5><i class=\"pull-right\">"+getTimeElapsed(ms, reqs[i].timeCreated)+"</i><p class='card-text'><h6 class='card-subtitle mb-2 text-muted'>"
					+ reqs[i].categoryName
					+ "</h6>"
					+ reqs[i].message
					+ "</p></div></div>";
		}
		html = html
				+ "<button id='reqButton"
				+ pageNumber
				+ "' type='button' class='btn btn-warning top-bottom-space' onClick='fetchOpenRequests("
				+ pageNumber + ")'>Fetch more requests</button>";
		$("#data").append(html);

	}

	function fetchOpenRequests(pageNo) {

		pageNumber = pageNo;
		pageNumber = pageNumber + 1;
		if ($("#reqButton" + pageNo) != undefined
				&& $("#reqButton" + pageNo) != null) {
			$("#reqButton" + pageNo).fadeOut("slow", "linear", fetchNearMeOpenRequests());
		}

	}

	function fetchRequestDetails(requestId, chats) {
		$("#myModal").show();
		isPop = true;

		$.get("/req-details?requestId=" + requestId,
				function(response, status) {

					if (response != null && response != undefined) {
						displayRequestDetails(response, chats);
						
					}
				});
	}
	
	function updateLastRead(currentLastRead){
		if(localStorage.getItem("lastRead") < currentLastRead){
			localStorage.setItem("lastRead", currentLastRead);
		}
	}

	function fetchChats(requestId) {

		$.get("/get-responses?requestId=" + requestId,
				function(response, status) {
					var chats = "";
					if (response != null && response != undefined) {
						chats = "<p>";
						var i;
						for (i = 0; i < response.length; i++) {
							updateLastRead(response[i].chatId);
							chats = chats + (i + 1) + ") "
									+ response[i].message + "<br>";
						}
						chats = chats + "</p>";

						//$("#chatsData").html(chats);

					}
					fetchRequestDetails(requestId, chats);
				});

	}

	function displayRequestDetails(details, chats) {

		var inputForm = "<form name='newResponse'><div class='mb-3'><label for='responseTextArea' class='form-label'>Add Response</label><textarea class='form-control' id='responseTextArea' rows='3'></textarea></div></div></form>";
		var displayData = "<div class='modal-header'><h5 class='modal-title' id='exampleModalLabel'>"
				+ details.categoryName
				+ "</h5><button type='button' class='close' data-dismiss='modal' aria-label='Close'  onClick='hidePopUp()'><span aria-hidden='true'>&times;</span></button></div><div class='modal-body'><h5>"
				+ details.subCategoryName
				+ "</h5><p>"
				+ details.message
				+ "</p><div id='#responsePlaceholder'>"
				+ inputForm
				+ "</div></div><div class='modal-footer'><button type='button' class='btn btn-secondary' data-dismiss='modal' onClick='hidePopUp()'>Close</button><button type='button' class='btn btn-primary' onClick='markComplete("
				+ details.requestId
				+ ")'>Mark Complete</button><button type='button' class='btn btn-primary' onClick='addResponse("
				+ details.requestId
				+ ")'>Add Response</button></div><h5 class='modal-title leadSpace' id='exampleModalLabel'>All Responses</h5><div id='#chatsData' class='leadSpace'>"
				+ chats + "</div>";
		$("#reqData").html(displayData);
	}

	function addResponse(requestId) {
		var message = $("#responseTextArea").val();
		var c = localStorage.getItem("c");
		var i = localStorage.getItem("i");
		var o = localStorage.getItem("o");
		var reqStr = "{\"c\":"+c+",\"i\":"+i+",\"o\":"+o+",\"message\":\""+message+"\",\"requestId\":"+requestId+"}";
		var jsonData = JSON.parse(reqStr);
		$.post("/add-response", jsonData).done(function(response) {
			if (response != undefined || response != null) {
				hidePopUp();
				alert("Thanks for your time, your response will be shared with Requestor.");
			}
		}).fail(function(res) {
			alert("Failed " + res)
		});

	}

	function markComplete(requestId) {
		var c = localStorage.getItem("c");
		var i = localStorage.getItem("i");
		var o = localStorage.getItem("o");
		var reqStr = "{\"userId\":"+c+",\"i\":"+i+",\"o\":"+o+",\"isOpen\":false, \"requestId\":"+requestId+"}";

		var jsonData = JSON.parse(reqStr);
		console.log(jsonData);
		$.post("/close-request", jsonData).done(function(data) {
			alert("success " + data)
		}).fail(function(res) {
			alert("Failed " + res)
		});

	}

	

	function hidePopUp() {
		if (isPop) {
			$("#myModal").hide();
		}
	}

	function navigateTo(newLocation) {
		document.location.href = newLocation;
	}

	function closeDisplayForm(){
		$("#myModal").hide();
		$("#reqData").html("");
	}
	
	function displayCreateCreation() {

		var formDisplay = "<div class='container top-bottom-space'><form name='newReq' method='post'><div class='col-lg-4'><select class='form-select' id='categoryId'aria-label='Select Category'><option selected value='None'>Please select one of the category</option><option value='Ambulance'>Ambulance</option><option value='Medicines'>Medicines</option><option value='Oxygen'>Oxygen</option><option value='Corona Test'>Corona Test</option><option value='Hospital Beds'>Hospital Beds</option><option value='Vaccine'>Vaccine</option><option value='Others'>Others</option></select></div><div class='mb-3 col-lg-8'><label for='subCategoryId' class='form-label'>Title</label> <input class='form-control ' id='subCategoryId' aria-describedby='emailHelp' required maxlength='120'><div id='subCategoryIdd' class='form-text'>Summaries your request in 120 letters</div></div><div class='mb-3 col-lg-8'><label for='message' class='form-label'>Message Details</label><textarea class='form-control' id='message' rows='3' aria-describedby='detailsHelp' required maxlength='500'></textarea><div id='detailsHelp' class='form-text'>Add details about the request you made. Keep your request within 500 letters.</div></div><input type='hidden' id='x' value='0.00'> <input type='hidden' id='y' value='0.00'><button type='submit' class='btn btn-primary' onClick='captureFormData()'>Create Request</button>&nbsp&nbsp<button class='btn btn-primary' onClick='closeDisplayForm()'>Close Window</button></form></div>";
		$("#myModal").show();
		$("#reqData").html(formDisplay);
	}

	function captureFormData() {
		if ($("#categoryId").val() == "None") {
			alert("Please select the Category of help, it will enable speedy response.");
			return;
		}
		var c = localStorage.getItem("c");
		var i = localStorage.getItem("i");
		var o = localStorage.getItem("o");
		var reqStr = "{\"message\":\"" + $("#message").val()
				+ "\",\"categoryName\":\"" + $("#categoryId").val()
				+ "\",\"subCategoryName\":\"" + $("#subCategoryId").val()
				+ "\",\"userId\":"+c+",\"i\":"+i+",\"o\":"+o+",\"isOpen\":true,\"x\":"
				+ localStorage.getItem("lat") + ",\"y\":" + localStorage.getItem("lon") + "}";

		var jsonData = JSON.parse(reqStr);
		console.log(jsonData);
		$.post("/request-help", jsonData).done(function(data) {
			alert("success " + data)
		}).fail(function(res) {
			alert("Failed " + res)
		});

		/* 
		$.post( "http://localhost:8090/add/help", JSON.parse(reqStr))
		  .done(function( data ) {
		    alert( "Request Created: " + data );
		  }); */
	}

	function fetchMyRequests() {
		$.get("http://localhost:8090/user-requests?c="+localStorage.getItem("c")+"&i="+localStorage.getItem("i")+"&o="+localStorage.getItem("o"), function(response,
				status) {
			chatsCount(response);
			
		});

	}
	
	function chatsCount(previousData){
		var c = localStorage.getItem("c");
		var i = localStorage.getItem("i");
		var o = localStorage.getItem("o");
		var lastRead = localStorage.getItem("lastRead");
		if(lastRead == null){
			lastRead = 0;
		}
		var reqStr = "{\"c\":"+c+",\"i\":"+i+",\"o\":"+o+",\"lastRead\":"+lastRead+"}";
		var jsonData = JSON.parse(reqStr);		
		$.post("/unread-chats", jsonData).done(function(data) {
			displayMyRequests(previousData, data);
		}).fail(function(res) {
			alert("Failed " + res)
		});
	}

	function getCountOfChats(data, requestId){
		if(data == null || data==undefined || data.length == 0){
			return "";
		}else{
			var i;
			for(i = 0;i<data.length;i++){
				if(data[i].requestId == requestId){
					return " <span class='badge bg-danger'>"+data[i].chatCount+" new messages</span>";
				}
			}
		}
		return "";
	}
	
	function displayMyRequests(previousData, data) {
		var display = "<div class='row'><div class='col-md-8'><p class='display-6'>My Help Requests</p></div><div class='col-md-4'><button type='button' class='btn btn-warning' style='margin-top: 1rem' onClick='displayCreateCreation()'>Raise a Request</button></div></div>";
		if (previousData != null && previousData != undefined) {
			if(previousData.length == 0){
				
				$("#myRequests").html("<p class='lead top-bottom-space padding-half'>If you are in problem and need any help then please raise a request so that others can help.</p>");
				return;
			}
			var i;
			display = display
					+ "<table class='table'><thead><tr><th scope='col'>#</th><th scope='col'>Category</th><th scope='col'>Title</th><th scope='col'>Status</th></tr></thead><tbody>";
			for (i = 0; i < previousData.length; i++) {
				display = display + "<tr class='pointer' onClick='fetchChats("
						+ previousData[i].requestId + ")'><td scope='row'>" + (i + 1)
						+ "</td><td>" + previousData[i].categoryName + "</td><td>"
						+ previousData[i].subCategoryName + "</td>";
						if(previousData[i].isOpen == true){
							display = display + "<td>Open"+getCountOfChats(data, previousData[i].requestId)+"</td></tr>";	
						}else{
							display = display + "<td>Completed</td></tr>";
						}
						
			}
			dispaly = display + "</tbody></table>";

			$("#myRequests").html(dispaly);
		}

	}
	
	function showLoginMessage(){
		if(localStorage.getItem("i") == 0){
			$("#loginMessage").show();
			$("#openRequests").hide();
			$("#myRequests").hide();
			
		}else{
			$("#loginMessage").hide();
		}
	}
	
	function generateInitialPageView(){
		if(init == undefined){
			checkUserSession();
			showLoginMessage();
			fetchMyRequests();
		}
		init = "init";
		
	}
	
		
	function sendOtp(){
		var reqStr = "{\"c\":"+$("#mobileNumber").val()+"}";

		var jsonData = JSON.parse(reqStr);
		
		$.post("/send-otp", jsonData).done(function(data) {
			alert("OTP sent");
			showLoginWithOTP($("#mobileNumber").val());
		}).fail(function(res) {
			alert("Failed " + res)
		});
	}
	
	function postLoginHandler(email,fName,lName){
		var reqStr = "{\"e\":"+mobNo+"}";
		
		var jsonData = JSON.parse(reqStr);
		
		$.post("/manage-user", jsonData).done(function(data) {
			var resJsonData = JSON.parse(data);
			if(resJsonData.m == undefined){
				setUserSession(resJsonData.c, resJsonData.i,resJsonData.o,fName,lName);
				showLoginMessage();
				closeDisplayForm();	
			}else{
				alert(resJsonData.m);
			}
			
		}).fail(function(res) {
			alert("Failed " + res)
		});
	}
	
	function validateOtp(){
		alert("validation");
		var reqStr = "{\"c\":"+$("#userMobileNumber").val()+",\"o\":"+$("#userOtp").val()+"}";

		var jsonData = JSON.parse(reqStr);
		
		$.post("/validate-otp", jsonData).done(function(data) {
			var resJsonData = JSON.parse(data);
			if(resJsonData.m == undefined){
				setUserSession(resJsonData.c, resJsonData.i,resJsonData.o);
				showLoginMessage();
				closeDisplayForm();	
			}else{
				alert(resJsonData.m);
			}
			
		}).fail(function(res) {
			alert("Failed " + res)
		});
	}
	
	function showLoginWithOTP(mobNumber){
		var formDisplay = "<div class='container top-bottom-space'><form name='loginWithOtpPage' method='post'><div class='mb-3 col-lg-8'><label for='userMobileNumber' class='form-label'>Mobile Number</label> <input class='form-control ' id='userMobileNumber' aria-describedby='mobHelp' required maxlength='10' value='"+mobNumber+"'><div id='mobHelp' class='form-text'>Enter your mobile number with out country code</div></div><div class='mb-3 col-lg-8'><label for='userOtp' class='form-label'>Enter 6 digit OTP</label><input class='form-control' id='userOtp' required maxlength='6'></div><button class='btn btn-primary' onClick='closeDisplayForm()'>Close Window</button><input type='button' class='btn btn-primary' onClick='validateOtp()'value='Validate OTP'></form></div>";
		$("#myModal").show();
		$("#reqData").html(formDisplay);
	}
	
	function showLoginPopUp(){
		var formDisplay = "<div class='container top-bottom-space'><form name='loginPage' method='post'><div class='mb-3 col-lg-8'><label for='mobileNumber' class='form-label'>Mobile Number</label> <input class='form-control ' id='mobileNumber' aria-describedby='mobHelp' required maxlength='10'><div id='mobHelp' class='form-text'>Enter your mobile number with out country code</div></div><button class='btn btn-primary' onClick='closeDisplayForm()'>Close Window</button><input type='button' class='btn btn-primary' onClick='sendOtp()' value='Send OTP'></form></div>";
		$("#myModal").show();
		$("#reqData").html(formDisplay);	
	}
	
	
	
</script>
<br>

<div id="loginMessage" class="container pink">
	<p class="fs-6 text-danger text-center fw-bold pointer"
		onClick="checkLoginState()">Your are not logged in, click here to
		login with facebook. <fb:login-button scope="public_profile,email" onlogin="checkLoginState();"></fb:login-button></p>


</div>
 
<div class="container blue">

	
	<div id="myRequests">
	<div class="row"><div class="col-md-8"><p class="display-6">My Help Requests</p></div><div class="col-md-4"><button type='button' class='btn btn-warning' style='margin-top: 1rem' onClick='displayCreateCreation()'>Raise a Request</button></div></div>
		<h4 class="padding-half">Currently you don't have any open requests</h4>
	</div>
</div>


<div id="openRequests" class="container pink">
	<p class="display-6">Near By Open Help Requests</p>


	<div id="data"></div>
	<div id="myModal" class="overlay dialog">
		<div class="modal-dialog">
			<div id="reqData" class="modal-content"></div>
		</div>
	</div>
</div>
<%@ include file="footer.jsp"%>