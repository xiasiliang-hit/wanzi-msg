package actors;


import com.fasterxml.jackson.databind.node.ArrayNode;
import models.CmdNode;
import play.PlayInternal;
import play.mvc.Result;
import messages.RoomMessage;
import messages.UserConnection;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
//import akka.actor.AbstractPersistentActor;
import java.util.*;

import messages.*;
import play.twirl.api.Html;
import views.*;
import views.html.*;


public class Bot extends UntypedActor {


    private static String TOGGLE = "zz";
    private static boolean ready = true;

    public static String name = "走走ZZ";
    public static String id = "FFFFFFFF";

    private Html hotel_response = Html.apply("");
    private Html fligt_response = Html.apply("");
    private String welcome = "你好，我是走走ZZ，试试输入 @@";

    private String enable = "zz 来了";
    private String disable = "zz 滚了";

    private String flight_ask_date = "";
    private String flight_ask_origin = "";
    private String flight_ask_destination = "";

    private String unknown = "zz 正在学习中";

    @Override
    public void onReceive(Object message) throws Exception {

        PlayInternal.logger().info("bot receive msg");
        if (message instanceof UserConnection) {
            UserConnection userConnection = (UserConnection) message;
            ActorRef roomActor = getContext().getChild(userConnection.conversationId);
            //	List<RoomMessage> historyMsgs = RoomMessage.loadHistoryMessages(userConnection.conversationId);
            //      PlayInternal.logger().info("load msgs[0] : " + userConnection.conversationId + historyMsgs.get(0).message);

            if (roomActor != null) {
                PlayInternal.logger().info("Use existing actor in bot : " + userConnection.conversationId);

            } else {
                roomActor = (getContext().actorOf(Props.create(ChatMessageSender.class), userConnection.conversationId));
                PlayInternal.logger().info("Create new actor in bot: " + userConnection.conversationId);
            }

            roomActor.tell(userConnection, getSender());
        } else if (message instanceof RoomMessage) {

            RoomMessage roomMessage = (RoomMessage) message;
            ActorRef roomActor = getContext().getChild(roomMessage.conversationId);
            PlayInternal.logger().info("Bot : conversationId:" + roomMessage.conversationId);

            RoomMessage rm = new RoomMessage(roomMessage.conversationId, Bot.id, Bot.name, Bot.id, this.reply(roomMessage.message));


            if (roomActor != null && rm.message != null) {
                roomActor.tell(rm, null);
                RoomMessage.saveMessage(rm);
            } else {
                PlayInternal.logger().info("actor none : ");
            }


        } else {
            unhandled(message);
        }
    }

    //    private HashMap<String, String> cmd_url = new HashMap<String, String>(["@dbahn": "https://www.bahn.com/en/view/index.shtml" ]);

    private String reply(String ask) {
        ask = ask.trim().toLowerCase();
        CmdNode cmd = CmdNode.findByCmd(ask);
    /*
    if (ask.equals(Bot.TOGGLE) )
	    {
		if (ready == false)
		    {
			ready = true;
		
		    }
		else
		    {
			ready = false;
		    }
	    }
	*/
        if (ready == true) {
            if (ask.contains("@booking")) {
                return views.html.booking.render().toString();
            } else if (cmd != null) {
                String msg = "";
                if (cmd.getCl().equals("leaf")) {
                    msg = "<a href=\"" + cmd.getHref() + "\" class=\"link\">" + cmd.getName() + "</a>";
                } else {
                    List<CmdNode> cmdList = CmdNode.findByCmdLeaf(ask);
                    for (CmdNode c : cmdList) {
                        msg += "<a href=\"" + c.getHref() + "\" class=\"link\">" + c.getName() + "</a>";
                    }
                }
                return msg;
            } else if (ask.contains("@")) {
                return views.html.knowledgequery.render(ask.substring(1), "AIzaSyCD2UQ1lNnpgOO8AlyQemW4IpoNj9GnjPA").toString();
                //List<Entity> ens = learn(ask);
                //			return views.html.entities.render(ens).toString();
            }

        } else  //not ready
        {
            return null;
        }
        return null;
    }
    /*
    private void learn(String ask)
    {
	KnowledgeQuery.query(ask.substring(1));
	}
    */
}
