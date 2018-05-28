package com.baidan.controller;

import com.baidan.pojo.User;
import com.baidan.service.UserService;
import com.baidan.utils.HttpXmlClient;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.collections.map.HashedMap;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.baidan.utils.HttpXmlClient.SHA1;

@Controller
@RequestMapping("/user")
public class UserController {

    private Logger logger = Logger.getLogger(UserController.class);

    @Resource(name = "UserService")
    private UserService userService;

    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    @RequestMapping("/find")
    @ResponseBody
    public String find(User user, HttpServletRequest request) {

        Map<String, Object> map = new HashedMap();
        System.out.println("你已通过springMVC进入controller方法。。。。");
        logger.info("你已通过springMVC进入controller方法。。。。");
        User loginuser = userService.findByUsernameAndPwd(user.getUsername(), user.getPassword());
        JSONArray array;
        String jsonstr;

        if (loginuser != null) {
            //map.put("result", "success");
             array = JSONArray.fromObject(loginuser);
             jsonstr = array.toString();
            return  jsonstr;
        } else {
            map.put("result", "fail");
            array = JSONArray.fromObject(map);
            jsonstr = array.toString();
        }
        return jsonstr;
    }

    @RequestMapping("/success")
    public String success() {
        System.out.println("登录成功。。。。");
        logger.info("登录成功。。。。");
        return "success";
    }


    /**
     * @ResponseBody 该注解可以把map自动转换成json格式给前台
     */
    @RequestMapping(value = "/ajax", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Map ajax(HttpServletRequest request) {

        Map mapJson = new HashMap();
        String person;
        String v = request.getParameter("v");
        if ("1".equals(v)) {
            person = "姓名：张三，年龄：24，性别：男，出生地：广西南宁";
            mapJson.put("person", person);
        }
        if ("2".equals(v)) {
            person = "姓名：李四，年龄：30，性别：男，出生地：广西百色";
            mapJson.put("person", person);
        }
        if ("3".equals(v)) {
            person = "姓名：王五，年龄29，性别：男，出生地：广西桂林";
            mapJson.put("person", person);
        }
        return mapJson;
    }


    @RequestMapping(value = "/getWeiSn", produces = "application/json")
    @ResponseBody
    public Map getWeiSn(HttpServletRequest request,@RequestParam(required = false, value = "url") String url,@RequestParam(required = false, value = "test") String test) {
        String tests = request.getParameter("test");
        System.out.println("tests========="+tests);

        Map textMap =  request.getParameterMap();
        Set keSet=textMap.entrySet();
        for(Iterator itr=keSet.iterator();itr.hasNext();){
            Map.Entry me=(Map.Entry)itr.next();
            Object ok=me.getKey();
            Object ov=me.getValue();
            String[] value=new String[1];
            if(ov instanceof String[]){
                value=(String[])ov;
            }else{
                value[0]=ov.toString();
            }

            for(int k=0;k<value.length;k++){
                System.out.println(ok+"============"+value[k]);
            }
        }
        //HttpSession session = request.getSession();

        if (url == "" || url == null) {
            Map datas = new HashMap();
            datas.put("data", "");
            datas.put("code", "-3003");
            datas.put("msg", "url参数为空");
            return datas;
        }

        //long time = new Date().getTime();
      /*  Object time_session = session.getAttribute("time");
        if(time_session == null){
            session.setAttribute("time_session",0);
        }

        if(Long.parseLong((String) session.getAttribute("time"))< (System.currentTimeMillis() / 1000)){

        }
        System.out.println("time1-----------"+Long.parseLong((String) session.getAttribute("time")));
        System.out.println("time2-----------"+Long.toString(System.currentTimeMillis() / 1000));
        System.out.println("==================================================");*/


        // long time1 = Long.parseLong((String) session.getAttribute("time"));

        //access_token
        //Map<String, String> params = new HashMap<String, String>();
        /*params.put("appid","wx79432370f8a334fb");
		params.put("grant_type","client_credential");
		params.put("secret","53fbe8f8ba49e9ef3d832825af321c0a");*/
        String xml = HttpXmlClient.get("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&secret=53fbe8f8ba49e9ef3d832825af321c0a&appid=wx79432370f8a334fb");
        //String xml = HttpXmlClient.get("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx9445ad8221232877&secret=a76b2c1f7eccf2130c8b41c64962551c");
        JSONObject jsonMap = JSONObject.fromObject(xml);

        Map<String, String> map = new HashMap<String, String>();
        Iterator<String> it = jsonMap.keys();
        while (it.hasNext()) {
            String key = (String) it.next();
            String u = jsonMap.get(key).toString();
            map.put(key, u);
        }
        System.out.println("-----------------------" + map);
        String access_token = map.get("access_token");
        //session.setAttribute("access_token", access_token);
        System.out.println("将重新获取的access_token存入在session中......");
        System.out.println("access_token=" + access_token);

        //ticket
        Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", access_token);
        params.put("type", "jsapi");
        xml = HttpXmlClient.post("https://api.weixin.qq.com/cgi-bin/ticket/getticket", params);
        jsonMap = JSONObject.fromObject(xml);
        map = new HashMap<String, String>();
        it = jsonMap.keys();
        while (it.hasNext()) {
            String key = (String) it.next();
            String u = jsonMap.get(key).toString();
            map.put(key, u);
        }
        String jsapi_ticket = map.get("ticket");
        //session.setAttribute("jsapi_ticket", jsapi_ticket);
        System.out.println("将重新获取的jsapi_ticket存入在session中......");
        System.out.println("jsapi_ticket=" + jsapi_ticket);

        //signature
        String noncestr = UUID.randomUUID().toString();
        String timestamp = Long.toString(System.currentTimeMillis() / 1000);
        //url = "http://creadit.tritonsfs.com:8081/pro/7xi.html";
        String str = "jsapi_ticket=" + jsapi_ticket +
                "&noncestr=" + noncestr +
                "&timestamp=" + timestamp +
                "&url=" + url;
        //sha1
        String signature = SHA1(str);
        System.out.println("noncestr=" + noncestr);
        System.out.println("timestamp=" + timestamp);
        System.out.println("signature=" + signature);
        //最终获得调用微信js接口验证需要的三个参数noncestr、timestamp、signature
        List list = new ArrayList();
        Map mapJson = new HashMap();
        mapJson.put("access_token", access_token);
        mapJson.put("jsapi_ticket", jsapi_ticket);
        mapJson.put("nonceStr", noncestr);
        mapJson.put("timestamp", timestamp);
        mapJson.put("signature", signature);
        mapJson.put("url", url);
        //list.add(mapJson);

        /*Map mapJson2 = new HashMap();
        mapJson2.put("jsapi_ticket","d");
        mapJson2.put("noncestr","d");
        mapJson2.put("timestamp","d");
        mapJson2.put("signature","d");
        list.add(mapJson2);
        */

        Map mapJsons = new HashMap();
        mapJsons.put("data", mapJson);
        mapJsons.put("code", "0000");
        mapJsons.put("msg", "操作成功！");
        return mapJsons;
    }

    @RequestMapping(value = "/getJson", produces = "application/json")
    @ResponseBody
    public Map getJson(@RequestParam(required = false, value = "stringId") String stringId) {
        Map datas = new HashMap();
        Map datasMap = new HashMap();

        if (stringId == "" || stringId == null) {
            datas.put("data", datasMap);
            datas.put("code", "-3003");
            datas.put("msg", "url参数为空");
            return datas;
        }

        char charName = '\u006B';
        int m = 7, n = 7;
        int a  = 2 * ++m; //16
        int b  = 2 * n++; //14
        try {
            double c  = 10/1;
            datasMap.put("operation",c);
        } catch (Exception e) {
            e.printStackTrace();
        }
        datasMap.put("char",charName);
        datasMap.put("a",a);
        datasMap.put("b",b);
        datas.put("data", datasMap);
        datas.put("code", "0000");
        datas.put("msg", "操作成功！");

        return datas;
    }

    public static void main(String[] args) {
        String k = "\uD862\uDECE";
        System.out.println(k);
        //fnTest();
    }

    public static void fnTest(){
        int a = 9;
        ArrayList list = new ArrayList();
        Map o = new HashedMap();
        o.put("key","value");
        list.add(o);

        System.out.println(list);
    }
}