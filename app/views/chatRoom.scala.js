@(conversationId:String, userId:String, anotherId:String)

var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket;
var chatSocket = new WS("@routes.Application.chat(conversationId, userId, session().get("username"), anotherId).webSocketURL(request)"
)
;
var handleReturnKey = function (e) {
    if (e.charCode == 13 || e.keyCode == 13) {
        e.preventDefault()
        //sendMessage();
    }
};
var receiveEvent = function (event) {
    var data = JSON.parse(event.data);
    // Create the message element
    if (data.type == "message") {
        var el = $('<div class="message"><span></span><p></p></div>');
        $("span", el).text(data.user);   //+ "  " + data.dateTime);
        $("p", el).html(data.message);
        $(el).addClass(data.kind);
        $('#msg-content').append(el);
        $('#msg_end').click();
        $('#states').empty().append("当前在线");
    } else if (data.type == "states"){
        var html = "";
        if (data.states == "1"){
            html += "当前在线";
        } else if (data.states == "0"){
            html += "当前离线";
        }

        $('#states').empty().append(html);

    }
};

function sendMessage() {
    send(JSON.stringify({text: $("#talk").val()}));
    $("#talk").val('');
}

function send(msg) {
    waitForSocketConnection(chatSocket, function () {
        chatSocket.send(msg);
    });
    $('#msg_end').click();
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


function init() {
    $("#onChat").show();
    testWebSocket();
}

function testWebSocket() {
    chatSocket.onopen = function (evt) {
    };
    chatSocket.onclose = function (evt) {
    };
    chatSocket.onmessage = function (evt) {
        receiveEvent(evt)
    };
    chatSocket.onerror = function (evt) {
    };
}

window.addEventListener("load", init, false);

var handleReturnKey = function (e) {
    if (e.charCode == 13 || e.keyCode == 13) {
        e.preventDefault();
        sendMessage();
    }
}

$("#talk").keypress(handleReturnKey);
chatSocket.onmessage = receiveEvent;


window.onload = function () {
    $("#messages").scrollTop = 999999 + "px";
    $.ajax({
        url: "/room/getRooms",    //请求的url地址
        dataType: "json",   //返回格式为json
        async: true,//请求是否异步，默认为异步，这也是ajax重要特性
        data: {"userId": "@userId"},    //参数值
        type: "POST",   //请求方式

        success: function (data) {
            var html = "";
            for (var i in data) {
                html = html + '<a class="btn btn-info button" href="/room?conversationId='+data[i].chatId+'&userId=@Html(userId)&anotherId='+data[i].chatId.replace("@Html(userId)", "")+'">'+data[i].name+'</a>';
            }
            $("#chatRooms").empty().append(html);

            //请求成功时处理
        }

    });
};


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
