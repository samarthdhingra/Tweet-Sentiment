
$(document).ready(function(){
	$('updateUsername').submit(function() {
		$.ajax({
			url: 'Tweet',
			type: 'POST',
			dataType: 'json',
			data: $('#updateUsername').serialize(),
			success: function(data) {
				if(data.isValid) {
					$('displayName').html('your name is: ' + data.username);
					$('displayName').slideDown(500);
				}
				else {
					alert("Please enter  valid username...");
				}
			}
		});
		return false;
	});
});
