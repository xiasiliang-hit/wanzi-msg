package utils;

import com.google.gson.*;
import models.CmdNode;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JsonLoad {
    public static String jsonStr = "";
    public static String path = "public/json/cmd.json";
    public static List<CmdNode> cmdTree = new ArrayList<>();
    public void loadCmdJson(){
        BufferedReader br ;
        try {
            br = new BufferedReader(new FileReader(path));
            String jsonLine;
            while ((jsonLine = br.readLine()) != null){
                jsonStr += jsonLine;
            }
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public CmdNode getCmdNode() {
        loadCmdJson();
        jsonStr = jsonStr.replace("\t","");
        jsonStr = jsonStr.replace(" ","");
        jsonStr = jsonStr.trim();
        JsonObject obj = new JsonParser().parse(jsonStr).getAsJsonObject();
        CmdNode cmdTree = new CmdNode();
        setCmdTree(cmdTree,obj,"");
        return cmdTree;
    }
    public void setCmdTree(CmdNode cmdNode, JsonObject cmdJson, String parentCmds){
        cmdNode.setName(cmdJson.get("name").getAsString());
        cmdNode.setCl(cmdJson.get("cl").getAsString());
        cmdNode.setCmds(cmdJson.get("cmds").getAsString());
        cmdNode.setHref(cmdJson.get("href").getAsString());
        cmdNode.setLink(cmdJson.get("link").getAsString());
        cmdNode.setParentCmds(parentCmds);
        cmdTree.add(cmdNode);
        JsonArray subJson = cmdJson.get("sub").getAsJsonArray();
        if (subJson.size() > 0){
            String pCmds = cmdNode.getCmds();
            for(JsonElement e : subJson){
                JsonObject obj = e.getAsJsonObject();
                CmdNode subNode = new CmdNode();
                setCmdTree(subNode,obj,pCmds);
            }
        }
    }

}
