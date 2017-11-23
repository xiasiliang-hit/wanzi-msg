package controllers;

import messages.NotifyAll;
import messages.RoomMessage;
import messages.UserConnection;
import models.AUser;
import models.CmdNode;
import play.libs.F.Callback;
import play.libs.F.Callback0;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;
import views.html.chatRoom;
import views.html.index;
import actors.ChatCluster;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Application extends Controller {
	
    public static Result index() {
        return ok(index.render());
    }

    public static Result chatRoom(String conversationId, String userId, String anotherId) {
		if(userId == "")
		{
			userId = play.Configuration.root().getString("newuserid");
		}
        if (userId == null || userId.trim().equals("") || anotherId == null || anotherId.trim().equals("")) {
			flash("error", "Please choose a valid username.");
            return redirect(routes.Application.index());
        }
        //测试用session数据
        AUser user = AUser.getUserById(userId);
        session("username", user.name);
        session("userId", user.id);
        session("userType", user.type);
        //对方信息
        AUser another = AUser.getUserById(anotherId);

        return ok(chatRoom.render(conversationId, userId, another, RoomMessage.loadHistoryMessages(conversationId)));
        //return ok(chatRoom.render(conversationId, userId, anotherId));
    }

    public static Result chatRoomJs(final String conversationId, final String userId, final String anotherId) {

        return ok(views.js.chatRoom.render(conversationId, userId, anotherId));
    }

    public static WebSocket<JsonNode> chat(final String conversationId, final String userId, final String username, final String anotherId) {
        return new WebSocket<JsonNode>() {
            public void onReady(WebSocket.In<JsonNode> in, final WebSocket.Out<JsonNode> out) {
                ChatCluster.userConnetion((new UserConnection(conversationId, userId, out, messages.UserConnection.ConnectionType.CONNECTED)));
                in.onMessage(new Callback<JsonNode>() {
                    public void invoke(JsonNode event) {
                        ChatCluster.sendMessage(new NotifyAll(new RoomMessage(conversationId, userId, username, anotherId, event.get("text").asText())));
                    }
                });
                in.onClose(new Callback0() {
                    public void invoke() {
                        ChatCluster.userConnetion((new UserConnection(conversationId, userId, out, messages.UserConnection.ConnectionType.DISCONNECTED)));
                    }
                });
            }
        };
    }

    public static Result getChatRooms(){
        Map<String, String[]> formData = request().body().asFormUrlEncoded();
        String userId = formData.get("userId")[0];
        List<Map<String, Object>> chatList = new ArrayList<>();
        List<String> roomList = RoomMessage.getRooms(userId);
        //roomList.forEach((room)->{});
        for (String room : roomList) {
            AUser u = AUser.getUserById(room.replace(userId, ""));
            if (u != null){
                Map<String, Object> chat = new HashMap<>();
                chat.put("chatId", room);
                chat.put("name", u.name);
                chatList.add(chat);
            }
        }
        return ok(Json.toJson(chatList));
    }

    public static Result test() {
        List<CmdNode> cmds = CmdNode.findByCmdLeaf("@zz");

       return ok();
    }
}

    
