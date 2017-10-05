package actors;

import play.PlayInternal;
import messages.RoomMessage;
import messages.UserConnection;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
//import akka.actor.AbstractPersistentActor;
import java.util.*;
import messages.*;

public class ChatRoom extends UntypedActor {

	@Override
	public void onReceive(Object message) throws Exception {
		
		if (message instanceof UserConnection) {
			UserConnection userConnection = (UserConnection) message;
			ActorRef roomActor = (getContext().getChild(userConnection.roomName));
						//			    ChatMessageSender cms = rooms.get(userConnection.roomName);
			List<RoomMessage> historyMsgs = RoomMessage.loadHistoryMessages(userConnection.roomName);
			PlayInternal.logger().info("load msgs[0] : " + userConnection.roomName + historyMsgs.get(0).message);
			
			if(roomActor != null) {
				PlayInternal.logger().info("Use existing actor : " + userConnection.roomName);
				//				rooms.get(roomName).add(userName);

			} else {
			    roomActor = (getContext().actorOf(Props.create(ChatMessageSender.class),userConnection.roomName));
				PlayInternal.logger().info("Create new actor : " + userConnection.roomName);

				//				cms = roomActor;
				//				rooms.put(userConnection.roomName, roomActor);

				/*Conversation c = Conversation.getConversation(userConnection.roomName);
				if (c != null)
				    rooms.put(c.roomName, c);
				else
				    rooms.put(userConnection.roomName, new Conversation(userConnection.roomName));
				*/
			}
			
			roomActor.tell(userConnection, getSender());
		} else if (message instanceof RoomMessage) {
			RoomMessage roomMessage = (RoomMessage) message;

						ActorRef roomActor = getContext().getChild(roomMessage.roomName);
						//			ChatMessageSender cms = rooms.get(roomMessage.roomName);
			if(roomActor != null) {
				roomActor.tell(roomMessage, null);
				//cms.msgs.add(roomMessage);
			}

RoomMessage.saveMessage(roomMessage);
//rooms.get(roomMessage.roomName).msgs.add(roomMessage);						
		}

		/*else if (message instanceof Persistent){
		    Persistent persistent = (Persistent)message;
		    this.persistentId = roomName;
		    
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


    
    public static HashMap<String, ChatMessageSender > rooms = new HashMap< String, ChatMessageSender >();  // roomName: [userName1, userName2...]

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
