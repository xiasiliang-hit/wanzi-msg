package messages;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

import com.mongodb.BasicDBObject;

import net.vz.mongodb.jackson.*;
import play.modules.mongodb.jackson.MongoDB;


public class RoomMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @ObjectId
    public String id;
    public String conversationId;
    public String userId;
    public String userName;
    public String message;
    public String dateTime;
    public String anotherId;

    public RoomMessage() {
    }

    public RoomMessage(String conversationId, String userId, String userName, String anotherId, String message) { //$$danny: no need for userName
        this.conversationId = conversationId;
        this.userId = userId;
        this.message = message;
        this.userName = userName;
        this.anotherId = anotherId;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        this.dateTime = sdf.format(new Date());
        //this.id = ObjectId();
    }

    private static JacksonDBCollection<RoomMessage, String> coll = MongoDB.getCollection("chatroom", RoomMessage.class, String.class);

    public static void saveMessage(RoomMessage m) {

        RoomMessage.coll.save(m);
    }

    public static List<RoomMessage> loadHistoryMessages(String conversationId) {
        DBCursor<RoomMessage> result = RoomMessage.coll.find(DBQuery.is("conversationId", conversationId)).limit(10).sort(new BasicDBObject("dateTime", -1));
        //result = result.sort(new BasicDBObject("dateTime", 1));
        return result.toArray();
    }

    
    /*
    public static ArrayList<RoomMessage> getConversation(String username)
    {
	RoomMessage.coll.find(DBQuery.equals("userId", username));
    }
    */
}
