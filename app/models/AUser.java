package models;


import java.util.*;
import java.util.regex.Pattern;

import play.modules.mongodb.jackson.MongoDB;
import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.ObjectId;
import net.vz.mongodb.jackson.DBQuery;

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

    public static void update(AUser u) {
        coll.updateById(u.id, u);
    }

    public static AUser verifyUser(String email, String password) {
        AUser u = null;
        try {
            u = coll.findOne(DBQuery.is("email", email));
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

    public static List<AUser> searchGuider(String keyword, Integer limit, Integer page) {
        return coll.find(DBQuery.regex("city_and_country", Pattern.compile(".*" + keyword + ".*",
                Pattern.CASE_INSENSITIVE))).limit(limit).skip(limit * (page - 1)).toArray();
    }

    public static List<AUser> getStarGuiders()
    {
	List<AUser> guiders = new ArrayList<AUser>();
	/*
	AUser g1 = AUser.coll.findOneById("59febdfae4b0321df4d111f8");
	AUser g2 = AUser.coll.findOneById("111111111111111111111111");
	AUser g3 = AUser.coll.findOneById("10c8d3518be761e8fdbf2e5a");
	*/
	AUser g1 = AUser.coll.findOneById("5a045a17e4b05602646333dd");
	AUser g2 = AUser.coll.findOneById("5a1108b2e4b0e90f7d037dcd");
	AUser g3 = AUser.coll.findOneById("5a11109ee4b0e90f7d037dce");
	   
	guiders.add(g1);
	guiders.add(g2);
	guiders.add(g3);

	return guiders;
    }


	
    @Id
    @ObjectId
    public String id;
    public String name;
    

    public String email;
    public String password;
    public double balance=0.0;


    public String type = ""; //GUIDER or TRAVELLER
    public String type_work = ""; //STUDNET or EMPLOYEE
    public String gender = "";
    public int age = 0;
    public String city_and_country = ""; // city country in one field
    public String locationIndex; // 导游城市索引
    public String employer = "";
    public String major = ""; //专业
    public String jobtitle = "";
    public String birth_day = "";
    public String wechat = "";
    public String degree = ""; //最高学历
    public String industry = ""; //所在行业

    public String img_passport = ""; //证件照
    public String img_theme = ""; //top big image
    public String img_profile = "";  //image left //[id].profile.[GUID].jpg
    public String img_degree = ""; //学历证书照
    public String img_validate = ""; //验证照
    public ArrayList<String> imgs_travel = new ArrayList<String>(); //旅行照片
    public List<String> imgs_about = new ArrayList<>(); //关于这座城市的我
    public List<String> imgs_introduce = new ArrayList<>(); //我眼中的这座城市照片
    
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

	    public List<Comment> comments = new ArrayList<Comment>();


    public static String GUIDER = "GUIDER";
    public static String TRAVELLER = "TRAVELLER";

    public static String STUDENT = "STUDENT";
    public static String EMPLOYEE = "EMPLOYEE";
}
