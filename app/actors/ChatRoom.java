package actors;

import play.PlayInternal;
import messages.RoomMessage;
import messages.UserConnection;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
//import akka.actor.AbstractPersistentActor;
import java.util.*;

public class ChatRoom extends UntypedActor {

    @Override
    public void onReceive(Object message) throws Exception {

        if (message instanceof UserConnection) {
            UserConnection userConnection = (UserConnection) message;
            ActorRef roomActor = (getContext().getChild(userConnection.conversationId));
            //ChatMessageSender cms = rooms.get(userConnection.conversationId);
            List<RoomMessage> historyMsgs = RoomMessage.loadHistoryMessages(userConnection.conversationId);
            //PlayInternal.logger().info("load msgs[0] : " + userConnection.conversationId + historyMsgs.get(0).message);

            if (roomActor != null) {
                PlayInternal.logger().info("Use existing actor : " + userConnection.conversationId);
                //				rooms.get(conversationId).add(userId);

            } else {
                roomActor = (getContext().actorOf(Props.create(ChatMessageSender.class), userConnection.conversationId));
                PlayInternal.logger().info("Create new actor : " + userConnection.conversationId);

                //				cms = roomActor;
                //				rooms.put(userConnection.conversationId, roomActor);

				/*Conversation c = Conversation.getConversation(userConnection.conversationId);
                if (c != null)
				    rooms.put(c.conversationId, c);
				else
				    rooms.put(userConnection.conversationId, new Conversation(userConnection.conversationId));
				*/
            }

            roomActor.tell(userConnection, getSender());
        } else if (message instanceof RoomMessage) {
            RoomMessage roomMessage = (RoomMessage) message;

            ActorRef roomActor = getContext().getChild(roomMessage.conversationId);
            //			ChatMessageSender cms = rooms.get(roomMessage.conversationId);
	    roomMessage.message  = filter(roomMessage.message);
            if (roomActor != null) {
                roomActor.tell(roomMessage, null);
                //cms.msgs.add(roomMessage);
            }

            RoomMessage.saveMessage(roomMessage);
//rooms.get(roomMessage.conversationId).msgs.add(roomMessage);
        }

		/*else if (message instanceof Persistent){
            Persistent persistent = (Persistent)message;
		    this.persistentId = conversationId;
		    
		    Object payload = persistent;
		    Long sequenceNr = persistent.sequenceNr();
		}
		*/

        else {
            unhandled(message);
        }
    }

    /*
    @Override
    public String persistenceId() {
	return persistenceId;
    }
    */

    private String filter(String message)
    {
	String re = message;
	String regex = "[0-9]{10,20}";

	//	re.replaceAll(regex, "********");
	//	re.replaceAll("wechat", "");
	//re.replaceAll("微信","");
	
	if (re.length()>=10 && re.matches(regex) )
	    {
		re = "**********"; 
	    }

	return re;
    }
    

    public static HashMap<String, ChatMessageSender> rooms = new HashMap<String, ChatMessageSender>();  // conversationId: [userName1, userName2...]

    //ArrayList<Conversation> cs = new ArrayList<Conversation> ();


    //    private static JacksonDBCollection<ChatRoom, String> coll = MongoDB.getCollection("chatroom", ChatRoom.class, String.class);
    /*
    public static void saveConversation(Conversation c) {
	Conversation.coll.save(c); 
    }
    */
    /*
    private static getConverstation() {
	AUser u = AUser.coll.findOneById(id);
    }
    */


    //    static private ArrayList<RoomMessage> msgs = new ArrayList<RoomMessage>();
    //static private 
}
