package models;

import net.vz.mongodb.jackson.DBQuery;
import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.ObjectId;
import play.modules.mongodb.jackson.MongoDB;

import java.util.ArrayList;
import java.util.List;

//@Entity
//@Table(name="alluser")
public class AUser {

    public AUser() {
    }

    public AUser(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public static List<AUser> all() {
        return AUser.coll.find().toArray();
    }

    public static void create(AUser u) {
        AUser.coll.save(u);
    }

    public static AUser getUserById(String id) {
        return AUser.coll.findOneById(id);
    }

    public static AUser verifyUser(String email, String password) {
        AUser u = null;
        try {
            u = AUser.coll.findOne(DBQuery.is("email", email));
        } catch (Exception e) {
            return null;
        }
        if (u != null && u.password != null) {
            if (u.password.equals(password))
                return u;
            else
                return null;
        } else
            return null;
    }

    public static void delete(String id) {
        AUser u = AUser.coll.findOneById(id);
        if (u != null)
            AUser.coll.remove(u);
    }

    public static void removeAll() {
        AUser.coll.drop();
    }


    //    private static final long serialVersionUID = 1L;
    private static JacksonDBCollection<AUser, String> coll = MongoDB.getCollection("auser", AUser.class, String.class);

    @Id
    @ObjectId
    public String id;
    public String name;


    public String email;
    public String password;


    public String type = ""; //GUIDER or TRAVELLER
    public String type_work = ""; //STUDNET or EMPLOYEE
    public String gender = "";

    public String city_and_country = ""; // city country in one field
    public String employer = "";
    public String major = ""; //专业
    public String jobtitle = "";
    public String birth_day = "";
    public String wechat = "";
    public String degree = ""; //最高学历
    public String industry = ""; //所在行业

    public String img_passport = "";
    public String img_theme = ""; //top big image
    public String img_profile = "";  //image left //[id].profile.[GUID].jpg
    public ArrayList<String> imgs_travel = new ArrayList<String>();

    public String traveltitle = ""; //导游主题
    public String traveldisc = ""; //导游简介
    public String priority_service = ""; //优先接待
    public String local_lan_proficiency = ""; //当地语言能力
    public String en_proficiency = ""; //英语能力
    public String joinlength = ""; //加入走走的时长
    public String citylength = ""; //导游城市居住时长
    public String birthplace = ""; //家乡

    public String guider_price = ""; //徒步旅行收费
    public String guiderdrive_price = ""; //五座车收费
    public String guiderpickup_price = ""; //五座车接机收费


    public static String GUIDER = "GUIDER";
    public static String TRAVELLER = "TRAVELLER";

    public static String STUDENT = "STUDENT";
    public static String EMPLOYEE = "EMPLOYEE";
}
