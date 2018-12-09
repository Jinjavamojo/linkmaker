

	<meta name="_csrf" th:content="${_csrf.token}"/>
	<meta name="_csrf_header" th:content="${_csrf.headerName}"/>


	var tk = $("meta[name='_csrf']").attr("content");
	var hh = $("meta[name='_csrf_header']").attr("content");
	$(document).ajaxSend(function(e,xhr,options) {
	   xhr.setRequestHeader(hh, tk);
	});

	function sendReportRequest() {

		$.ajax({
        type: "POST",
        url: "/[(${base})]/report",
        data: "name=" + "test" + "&education=" + "test2",
        success: function(response){
        },

        error: function(e){
            alert('Error: ' + e);
            }
        });
	}


    $( "#txt_download" ).click(function( event ) {
		sendReportRequest();
        /*var request = new XMLHttpRequest();
        request.open('POST', '/report', true);
        request.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded; charset=UTF-8');
        request.responseType = 'blob';

        request.onload = function() {
      // Only handle status code 200
          if(request.status === 200) {
            // Try to find out the filename from the content disposition `filename` value
            var disposition = request.getResponseHeader('content-disposition');
            var matches = /"([^"]*)"/.exec(disposition);
            var filename = (matches != null && matches[1] ? matches[1] : 'file.pdf');

            // The actual download
            var blob = new Blob([request.response], { type: 'application/pdf' });
            var link = document.createElement('a');
            link.href = window.URL.createObjectURL(blob);
            link.download = filename;

            document.body.appendChild(link);

            link.click();

            document.body.removeChild(link);
          }

          // some error handling should be done here...
        };*/
    });
