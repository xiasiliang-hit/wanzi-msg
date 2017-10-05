package messages;

import java.io.Serializable;
import java.util.*;
import play.modules.mongodb.jackson.MongoDB;
import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.ObjectId;
import net.vz.mongodb.jackson.DBQuery;
import net.vz.mongodb.jackson.DBCursor;
import com.mongodb.DBObject;
import org.codehaus.jackson.annotate.JsonProperty;


public class RoomMessage  implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @ObjectId
	public String id ;
	public String roomName;
	public String userName;
	public String message;

	public String another;

	public RoomMessage(){}
    
	public RoomMessage(String roomName, String userName, String message) {
		this.roomName = roomName;
		this.userName = userName;
		this.message = message;
		//		this.id = ObjectId();
	}

    private static JacksonDBCollection<RoomMessage, String> coll = MongoDB.getCollection("chatroom", RoomMessage.class, String.class);
    
    public static void saveMessage(RoomMessage m)
    {
	RoomMessage.coll.save(m);
    }

    public static List<RoomMessage> loadHistoryMessages(String roomname)
    {
	return RoomMessage.coll.find(DBQuery.is("roomName", roomname)).toArray();
    }

    
    /*
    public static ArrayList<RoomMessage> getConversation(String username)
    {
	RoomMessage.coll.find(DBQuery.equals("userName", username));
    }
    */
}
