import java.io.*;
import java.net.URL;
import java.util.Map;
import java.util.HashMap;
import java.net.URLConnection;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;

public class BaiduAPI {
    private static String ak = "5ef2641d89438a6e708db122820cf1d2";

    public static Map<String, String> testPost(String x, String y) throws IOException {
        URL url = new URL("http://api.map.baidu.com/geocoder?" + ak + "=您的密钥" +
                "&callback=renderReverse&location=" + x
                + "," + y + "&output=json");
        URLConnection connection = url.openConnection();
        /**
         * 然后把连接设为输出模式。URLConnection通常作为输入来使用，比如下载一个Web页。
         * 通过把URLConnection设为输出，你可以把数据向你个Web页传送。下面是如何做：
         */
        connection.setDoOutput(true);
        OutputStreamWriter out = new OutputStreamWriter(connection
                .getOutputStream(), "utf-8");
//        remember to clean up
        out.flush();
        out.close();
//        一旦发送成功，用以下方法就可以得到服务器的回应：
        String res;
        InputStream l_urlStream;
        l_urlStream = connection.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                l_urlStream,"UTF-8"));
        StringBuilder sb = new StringBuilder("");
        while ((res = in.readLine()) != null) {
            sb.append(res.trim());
        }
        String str = sb.toString();
        //System.out.println(str);
        Map<String,String> map = null;
        if(StringUtils.isNotEmpty(str)) {
            int addStart = str.indexOf("formatted_address\":");
            int addEnd = str.indexOf("\",\"business");
            if(addStart > 0 && addEnd > 0) {
                String address = str.substring(addStart+20, addEnd);
                map = new HashMap<String,String>();
                map.put("address", address);
                return map;
            }
        }

        return null;

    }
    public static Map<String, String> testPost(String addr) throws IOException {
        //"http://api.map.baidu.com/geocoder/v2/?address=+"+百度大厦+"&output=json&ak=E4805d16520de693a3fe707cdc962045&callback=showLocation"
        URL url = new URL("http://api.map.baidu.com/geocoder/v2/?address="+addr+"&output=json&ak="+ak);
//         System.out.println(url);
        URLConnection connection = url.openConnection();
        /**
         * 然后把连接设为输出模式。URLConnection通常作为输入来使用，比如下载一个Web页。
         * 通过把URLConnection设为输出，你可以把数据向你个Web页传送。下面是如何做：
         */
        connection.setDoOutput(true);
        OutputStreamWriter out = new OutputStreamWriter(connection
                .getOutputStream(), "utf-8");
//        remember to clean up
        out.flush();
        out.close();
//        一旦发送成功，用以下方法就可以得到服务器的回应：
        String res;
        InputStream l_urlStream;
        l_urlStream = connection.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                l_urlStream,"UTF-8"));
        StringBuilder sb = new StringBuilder("");
        while ((res = in.readLine()) != null) {
            sb.append(res.trim());
        }
        String str = sb.toString();
       // System.out.println(str);
       // JSONObject jb = JSONObject.fromObject(str);
        // str="["+str+"]";

        JSONObject  jsonArray3 = JSONObject .fromObject(str);
        Map<String,String> map = null;
        if( jsonArray3.getString("status").equals("0")) {
            JSONObject  jsonArray2 = JSONObject .fromObject(jsonArray3.getJSONObject("result"));
            JSONObject location1 =jsonArray2.getJSONObject("location");
            CoordTransform c = new CoordTransform();
            double x=Double.valueOf(location1.getString("lng"));
            double y=Double.valueOf(location1.getString("lat"));
            System.out.print(addr+","+location1.get("lng")+","+location1.get("lat")+",");
            c.Convert_Fouction(x,y);
           // map.put("lng", location1.get("lng"));
        }
//        System.out.println(test);
//        if(==0){

     //System.out.println(jsonArray3.size());
        //  System.out.println(str);



        //return map;
        return null;

    }
    public static void readTxtFile(String filePath){
        try {
            String encoding="GBK";
            File file=new File(filePath);
            if(file.isFile() && file.exists()){ //判断文件是否存在
                InputStreamReader read = new InputStreamReader(
                        new FileInputStream(file),encoding);//考虑到编码格式
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt = null;
                while((lineTxt = bufferedReader.readLine()) != null){
                    //System.out.print(lineTxt+" ");
                    Map<String, String> json1 = BaiduAPI.testPost(lineTxt);
                }
                read.close();
            }else{
                System.out.println("找不到指定的文件");
            }
        } catch (Exception e) {
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }

    }
    public static void main(String[] args) throws IOException {
           Map<String, String> json = BaiduAPI.testPost("22.897241460364", "108.3111820845");
         //  Map<String, String> json = BaiduAPI.testPost("22.804357938267", "108.30728298943");

           // Map<String, String> json1 = BaiduAPI.testPost("百度大厦");
            System.out.println("address :" + json.get("address"));

        //}
   String filePath = "D:\\zb.txt";
//      "res/";
     readTxtFile(filePath);

    }
}
