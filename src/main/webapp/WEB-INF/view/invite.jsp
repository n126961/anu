<%@ include file="header.jsp"%>



<div class="container blue top-bottom-space">
<p class="display-6">Invite Friends</p>
<p>Please note that you can invite only 10 of your friends, and your invitation code will expire post sending 10 invitations. 
So please share carefully.</p>
<p>This limitation is added just make sure that all people on this platform are known to someone in person. 
In this crucial time we all require a genuine help and network of trustworthy peoples.</p>

<p class="display-6 ">Lets build together.</p>
<div id="inviteCode">Invite code</div>

</div>

<script>

function generateInvitation(){
	
	var str = "<h3>Your invitation code is <u>"+localStorage.getItem("i")+"-"+localStorage.getItem("o")+"</u>.</h3>";
	$("#inviteCode").html(str);	
}

generateInvitation();
</script>
<%@ include file="footer.jsp"%>