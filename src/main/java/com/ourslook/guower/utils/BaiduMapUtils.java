package com.ourslook.guower.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

/**
 * 百度地图、位置相关工具类
 * baiduConfig.properties
 *
 * @author dazer
 * @date 2018/5/7 下午8:41
 */
@SuppressWarnings("all")
public class BaiduMapUtils {

    /**
     * 百度地图AK
     */
    private static String baiduMapAK;
    /**
     * 根据地址获取经纬度的请求地址
     */
    private static String baiduMapUrlGeocoder;
    /**
     * 根据经纬度和半径获取范围内的信息（周边检索）
     */
    private static String baiduMapUrlSearchcoder;

    /**
     * 页面根据经纬度获取地图并定位的请求地址
     */
    private static String baiduMapUrlJs;

    static {
        ResourceBundle bundle = ResourceBundle.getBundle("baiduConfig");
        baiduMapAK = bundle.getString("baidu.map.ak");
        baiduMapUrlGeocoder = bundle.getString("baidu.map.url.geocoder");
        baiduMapUrlSearchcoder = bundle.getString("baidu.map.url.searchcoder");
        baiduMapUrlJs = bundle.getString("baidu.map.url.js");
    }

    /**
     * 返回输入地址的经纬度坐标
     *
     * @param address 地址
     * @return key lng(经度),lat(纬度)
     * @throws
     * @Title: getGeocoderLatitude
     * @Description: 根据地址，获得经纬度坐标
     */
    public static Map<String, BigDecimal> getGeocoderLatitude(String address) {
        BufferedReader in = null;
        try {
            //将地址转换成utf-8的16进制
            address = URLEncoder.encode(address, "UTF-8");
            URL tirc = new URL(baiduMapUrlGeocoder + "?address=" + address + "&output=json&ak=" + baiduMapAK);

            in = new BufferedReader(new InputStreamReader(tirc.openStream(), "UTF-8"));
            String res;
            StringBuilder sb = new StringBuilder("");
            while ((res = in.readLine()) != null) {
                sb.append(res.trim());
            }
            String str = sb.toString();
            Map<String, BigDecimal> map = null;
            if (StringUtils.isNotEmpty(str)) {
                int lngStart = str.indexOf("lng\":");
                int lngEnd = str.indexOf(",\"lat");
                int latEnd = str.indexOf("},\"precise");
                if (lngStart > 0 && lngEnd > 0 && latEnd > 0) {
                    String lng = str.substring(lngStart + 5, lngEnd);
                    String lat = str.substring(lngEnd + 7, latEnd);
                    map = new HashMap<String, BigDecimal>();
                    map.put("lat", new BigDecimal(lat));
                    map.put("lng", new BigDecimal(lng));
                    return map;
                }
            }
           /* JSONArray jsonArray =  JSONArray.parseArray(str);
            if (jsonArray.size()>0) {
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                String lng = jsonObject.getJSONObject("result").getJSONObject("location").getString("lng");
                String lat = jsonObject.getJSONObject("result").getJSONObject("location").getString("lat");
                map.put("lat", BigDecimal.valueOf(Long.valueOf(lat)));
                map.put("lng", BigDecimal.valueOf(Long.valueOf(lng)));
            }*/
            /*JSONObject jsonObject = JSONObject.parseObject(str);
            String lng = jsonObject.getJSONObject("result").getJSONObject("location").getString("lng");
            String lat = jsonObject.getJSONObject("result").getJSONObject("location").getString("lat");
            map.put("lat", lat);
            map.put("lng", lng);*/

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 计算两点之间距离
     *
     * @param start
     * @param end
     * @return 米
     */
    public static double getDistance(Location start, Location end) {
        double lat1 = (Math.PI / 180) * start.getLat();
        double lat2 = (Math.PI / 180) * end.getLat();

        double lon1 = (Math.PI / 180) * start.getLng();
        double lon2 = (Math.PI / 180) * end.getLng();
        //地球半径
        double R = 6371;
        /**
         * 两点间距离 km，如果想要米的话，结果*1000就可以了
         */
        double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1)) * R;
        return d * 1000;
    }

    /**
     * 经纬度
     *
     * @author Administrator
     */
    public class Location {

        private Double lng;
        private Double lat;

        public Double getLng() {
            return lng;
        }

        public void setLng(Double lng) {
            this.lng = lng;
        }

        public Double getLat() {
            return lat;
        }

        public void setLat(Double lat) {
            this.lat = lat;
        }

        @Override
        public String toString() {
            return "Location [lng=" + lng + ", lat=" + lat + "]";
        }
    }

    private static double EARTH_RADIUS = 6378.137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 根据两个位置的经纬度，来计算两地的距离（单位为KM）
     * 参数为String类型
     *
     * @param lat1Str 用户经度
     * @param lng1Str 用户纬度
     * @param lat2Str 商家经度
     * @param lng2Str 商家纬度
     * @return
     */
    public static String getDistance(String lat1Str, String lng1Str, String lat2Str, String lng2Str) {
        Double lat1 = Double.parseDouble(lat1Str);
        Double lng1 = Double.parseDouble(lng1Str);
        Double lat2 = Double.parseDouble(lat2Str);
        Double lng2 = Double.parseDouble(lng2Str);

        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double difference = radLat1 - radLat2;
        double mdifference = rad(lng1) - rad(lng2);
        double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(difference / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(mdifference / 2), 2)));
        distance = distance * EARTH_RADIUS;
        distance = Math.round(distance * 10000) / 10000;
        String distanceStr = distance + "";
        distanceStr = distanceStr.
                substring(0, distanceStr.indexOf("."));

        return distanceStr;
    }

    /**
     * 获取当前用户一定距离以内的经纬度值
     * 单位米 return minLat
     * 最小经度 minLng
     * 最小纬度 maxLat
     * 最大经度 maxLng
     * 最大纬度 minLat
     */
    @SuppressWarnings("all")
    public static Map getAround(String latStr, String lngStr, String raidus) {
        Map map = new HashMap();

        Double latitude = Double.parseDouble(latStr);// 传值给经度
        Double longitude = Double.parseDouble(lngStr);// 传值给纬度

        Double degree = (24901 * 1609) / 360.0; // 获取每度
        double raidusMile = Double.parseDouble(raidus);

        Double mpdLng = Double.parseDouble((degree * Math.cos(latitude * (Math.PI / 180)) + "").replace("-", ""));
        Double dpmLng = 1 / mpdLng;
        Double radiusLng = dpmLng * raidusMile;
        //获取最小经度
        Double minLat = longitude - radiusLng;
        // 获取最大经度
        Double maxLat = longitude + radiusLng;

        Double dpmLat = 1 / degree;
        Double radiusLat = dpmLat * raidusMile;
        // 获取最小纬度
        Double minLng = latitude - radiusLat;
        // 获取最大纬度
        Double maxLng = latitude + radiusLat;

        map.put("minLat", minLat + "");
        map.put("maxLat", maxLat + "");
        map.put("minLng", minLng + "");
        map.put("maxLng", maxLng + "");

        return map;
    }

    /*public static void main(String[] args) {
        //济南国际会展中心经纬度：117.11811  36.68484
        //趵突泉：117.00999000000002  36.66123
        System.out.println(getDistance("117.1", "36.68484", "117.11", "36.66123"));

        System.out.println(getAround("117.11811", "36.68484", "13000"));
        //117.01028712333508(Double), 117.22593287666493(Double),
        //36.44829619896034(Double), 36.92138380103966(Double)
        System.out.println(getGeocoderLatitude("西安市西安交通大学"));
    }*/

    /**
     * 根据经纬度，获取归属区域面名称
     *
     * @param lng 经度
     * @param lat 纬度
     * @return name
     * @throws
     * @Title: getNameByLatLng
     * @Description: 根据经纬度，获取归属区域面名称
     */
    public static String getNameByLatLng(String lng,String lat) {
        BufferedReader in = null;
        try {
            //将地址转换成utf-8的16进制
            String lngLat = lat+","+lng;
            lngLat = URLEncoder.encode(lngLat, "UTF-8");
            URL tirc = new URL(baiduMapUrlGeocoder + "?location=" + lngLat + "&output=json&ak=" + baiduMapAK);

            in = new BufferedReader(new InputStreamReader(tirc.openStream(), "UTF-8"));
            String res;
            StringBuilder sb = new StringBuilder("");
            while ((res = in.readLine()) != null) {
                sb.append(res.trim());
            }
            String str = sb.toString();
            JSONObject jsonObject = JSONObject.parseObject(str);
            String name = jsonObject.getJSONObject("result").getJSONArray("poiRegions").getJSONObject(0).getString("name");
            return name;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 根据经纬度，获取结构化地址信息（eg:param:"34.26651949082673,108.88404764925538",return:"陕西省西安市莲湖区丰镐西路"）
     *
     * @param lng 经度
     * @param lat 纬度
     * @return formattedAddress 结构化地址
     * @throws
     * @Title: getFormattedAddressByLatLng
     * @Description: 根据经纬度，获取结构化地址信息
     */
    public static String getFormattedAddressByLatLng(String lng,String lat) {
        BufferedReader in = null;
        try {
            //将地址转换成utf-8的16进制
            String lngLat = lat+","+lng;
            lngLat = URLEncoder.encode(lngLat, "UTF-8");
            URL tirc = new URL(baiduMapUrlGeocoder + "?location=" + lngLat + "&output=json&ak=" + baiduMapAK);

            in = new BufferedReader(new InputStreamReader(tirc.openStream(), "UTF-8"));
            String res;
            StringBuilder sb = new StringBuilder("");
            while ((res = in.readLine()) != null) {
                sb.append(res.trim());
            }
            String str = sb.toString();
            JSONObject jsonObject = JSONObject.parseObject(str);
            String formattedAddress = jsonObject.getJSONObject("result").getString("formatted_address");
            return formattedAddress;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 根据经纬度，获取该地址所属行政区域信息
     *
     * @param lng 经度
     * @param lat 纬度
     * @return addressComponent 所属行政信息
     * @throws
     * @Title: getFormattedAddressByLatLng
     * @Description: 根据经纬度，获取该地址所属行政区域信息
     */
    public static Map<String,String> getAddressComponentByLatLng(String lng,String lat) {
        BufferedReader in = null;
        try {
            //将地址转换成utf-8的16进制
            String lngLat = lat+","+lng;
            lngLat = URLEncoder.encode(lngLat, "UTF-8");
            URL tirc = new URL(baiduMapUrlGeocoder + "?location=" + lngLat + "&output=json&ak=" + baiduMapAK);

            in = new BufferedReader(new InputStreamReader(tirc.openStream(), "UTF-8"));
            String res;
            StringBuilder sb = new StringBuilder("");
            while ((res = in.readLine()) != null) {
                sb.append(res.trim());
            }
            String str = sb.toString();
            JSONObject jsonObject = JSONObject.parseObject(str);
            //所属国家
            String country = jsonObject.getJSONObject("result").getJSONObject("addressComponent").getString("country");
            //国家的代码
            String country_code = jsonObject.getJSONObject("result").getJSONObject("addressComponent").getString("country_code");
            //省
            String province = jsonObject.getJSONObject("result").getJSONObject("addressComponent").getString("province");
            //市
            String city = jsonObject.getJSONObject("result").getJSONObject("addressComponent").getString("city");
            //所属的级别
            String city_level = jsonObject.getJSONObject("result").getJSONObject("addressComponent").getString("city_level");
            //区县名
            String district = jsonObject.getJSONObject("result").getJSONObject("addressComponent").getString("district");
            //乡镇名
            String town = jsonObject.getJSONObject("result").getJSONObject("addressComponent").getString("town");
            //行政区划代码
            String adcode = jsonObject.getJSONObject("result").getJSONObject("addressComponent").getString("adcode");
            //街道名（指的是行政区域意义上的街道）
            String street = jsonObject.getJSONObject("result").getJSONObject("addressComponent").getString("street");


            Map<String,String> map = new HashMap();
            map.put("country",country);
            map.put("country_code",country_code);
            map.put("province",province);
            map.put("city",city);
            map.put("city_level",city_level);
            map.put("district",district);
            map.put("town",town);
            map.put("adcode",adcode);
            map.put("street",street);
            return map;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 根据中心点的经纬度和半径，获取该范围内的地铁站信息
     *
     * @param lng 经度
     * @param lat 纬度
     * @param radius 半径（单位：米、整数）
     * @throws
     * @Title: getSubwayByLatLngAnd
     * @Description: 根据中心点的经纬度和半径，获取该范围内的地铁站信息
     */
    public static List<Map> getSubwayByLatLngAnd(String lng,String lat,String radius) {
        BufferedReader in = null;
        try {
            //将地址转换成utf-8的16进制z3.3
            String lngLat = lat+","+lng;
            lngLat = URLEncoder.encode(lngLat, "UTF-8");
            URL tirc = new URL(baiduMapUrlSearchcoder + "search?query=地铁站" + "&location="+lngLat+"&radius="+radius+"&output=json&ak=" + baiduMapAK);

            in = new BufferedReader(new InputStreamReader(tirc.openStream(), "UTF-8"));
            String res;
            StringBuilder sb = new StringBuilder("");
            while ((res = in.readLine()) != null) {
                sb.append(res.trim());
            }
            String str = sb.toString();

            JSONArray jsonArray = JSONArray.parseArray("results");
            List<Map> list = new ArrayList<>();
            for (int i=0;i<jsonArray.size();i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                //地铁站名称
                String name = jsonObject.getString("name");
                //地铁站所属地铁线名称
                String address = jsonObject.getString("address");
                //纬度
                String subwayLat = jsonObject.getJSONObject("location").getString("lat");
                //经度
                String subwayLng = jsonObject.getJSONObject("location").getString("lng");
                Map<String,String> map = new HashMap<>();
                map.put("name",name);
                map.put("address",address);
                map.put("subwayLat",subwayLat);
                map.put("subwayLng",subwayLng);
                list.add(map);
            }

            return list;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


}
