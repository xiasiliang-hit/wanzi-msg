@(conversationId: String, userId: String, anotherId: String)

var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket;
var chatSocket = new WS("@routes.Application.chat(conversationId, userId, session().get("username"), anotherId).webSocketURL(request)");
var handleReturnKey = function (e) {
    if (e.charCode == 13 || e.keyCode == 13) {
        e.preventDefault()
        //sendMessage();
    }
};
var receiveEvent = function (event) {
    var data = JSON.parse(event.data);
    // Create the message element
    var el = $('<div class="message"><span></span><p></p></div>');
    $("span", el).text(data.user);   //+ "  " + data.dateTime);
    $("p", el).html(data.message);
    $(el).addClass(data.kind);
    $('#msg-content').append(el);
    $("#messages").scrollTop = 999999+"px";
};

function sendMessage() {
    send(JSON.stringify({text: $("#talk").val()}));
    $("#talk").val('');
}

function send(msg) {
    waitForSocketConnection(chatSocket, function () {
        chatSocket.send(msg);
    });
}

function waitForSocketConnection(socket, callback) {
    setTimeout(
        function () {
            if (socket.readyState === 1) {
                if (callback !== undefined) {
                    callback();
                }
                return;
            } else {
                waitForSocketConnection(socket, callback);
            }
        }, 5);
}
$(window).resize(function () {
//	$('#talk').height($(window).height() );
    $('#talk').width($(window).width() - 200);
    //$('#send')
});

// $(window).onloadend(function () {
//     chatSocket.onmessage = receiveEvent;
//     $("#onChat").show();
//     $("#talk").keypress(handleReturnKey);
// });



function init()
{
    $("#onChat").show();
    testWebSocket();
}

function testWebSocket()
{
    chatSocket.onopen = function(evt) {  };
    chatSocket.onclose = function(evt) {  };
    chatSocket.onmessage = function(evt) { receiveEvent(evt) };
    chatSocket.onerror = function(evt) { };
}

window.addEventListener("load", init, false);

var handleReturnKey = function(e) {
    if(e.charCode == 13 || e.keyCode == 13) {
	e.preventDefault();
	sendMessage();
    }
}

$("#talk").keypress(handleReturnKey);

chatSocket.onmessage = receiveEvent;

//$(document).ready(function() {
//    $('.link').bind('click', function() {


//	$.ajax({
//	    url: $(this).attr("lk")
//	    success:function(data) {
//		$(this).html(data);
//	    }
//	});
//    });
//});
