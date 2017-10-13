package models;

import net.vz.mongodb.jackson.DBQuery;
import net.vz.mongodb.jackson.Id;
import net.vz.mongodb.jackson.JacksonDBCollection;
import net.vz.mongodb.jackson.ObjectId;
import play.modules.mongodb.jackson.MongoDB;


import java.util.List;
import java.util.regex.Pattern;

public class CmdNode {

    @Id
    @ObjectId
    private String id;
    private String name;
    private String cmds;
    private String cl;
    private String href;
    private String link;
    private String parentCmds;

    private static JacksonDBCollection<CmdNode,String> coll = MongoDB.getCollection("cmd",CmdNode.class,String.class);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCmds() {
        return cmds;
    }

    public void setCmds(String cmds) {
        this.cmds = cmds;
    }

    public String getCl() {
        return cl;
    }

    public void setCl(String cl) {
        this.cl = cl;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentCmds() {
        return parentCmds;
    }

    public void setParentCmds(String parentCmds) {
        this.parentCmds = parentCmds;
    }

    /**
     * 保存Cmd
     * @param cmd
     */
    public static void save(CmdNode cmd){
        coll.save(cmd);
    }

    /**
     * 通过id删除一个命令
     * @param id
     */
    public static void deleteById(String id){
        CmdNode cmd = coll.findOneById(id);
        if (cmd != null){
            coll.remove(cmd);
        }
    }

    /**
     * 修改一个cmd
     * @param cmd
     */
    public static void update(CmdNode cmd){
        coll.updateById(cmd.getId(),cmd);
    }

    /**
     * 查出所有的cmd
     * @return cmdList
     */
    public static List<CmdNode> findAll() {
        return coll.find().toArray();
    }

    /**
     * 批量保存
     * @param cmdNodeList
     */
    public static void saveList(List<CmdNode> cmdNodeList){
        for (CmdNode node: cmdNodeList) {
            coll.save(node);
        }
    }

    /**
     * 通过命令查找
     * @param cmd
     * @return
     */
    public static CmdNode findByCmd(String cmd){
        String regex = ".*?\\" + cmd +  ".*";
        return coll.findOne(DBQuery.regex("cmds", Pattern.compile(regex)));
    }

    /**
     * 通过命令查找子命令
     * @param cmd
     * @return
     */
    public static List<CmdNode> findByCmdLeaf(String cmd){
        String regex = ".*?\\" + cmd +  ".*";
        return coll.find(DBQuery.regex("parentCmds", Pattern.compile(regex))).toArray();
    }
}
