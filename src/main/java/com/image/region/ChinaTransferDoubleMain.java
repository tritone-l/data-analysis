package com.image.region;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;

import static org.apache.commons.io.FileUtils.readFileToString;

/**
 * @author tritone
 * @version $Id: ChinaTransferDoubleMain.java, v 0.1 2020年02月29日 00:34 tritone Exp $
 */
public class ChinaTransferDoubleMain {

    public static String provinceList = "北京,Beijing\n"
        + "上海,Shangjai\n"
        + "天津,Tianjin\n"
        + "重庆,Chongqing\n"
        + "香港,Hongkong\n"
        + "澳门,Macao\n"
        + "安徽,Anhui\n"
        + "福建,Fujian\n"
        + "广东,Guangdong\n"
        + "广西,Guangxi\n"
        + "贵州,Guizhou\n"
        + "甘肃,Gansu\n"
        + "海南,Hainan\n"
        + "河北,Hebei\n"
        + "河南,Henan\n"
        + "黑龙江,Heilongjiang\n"
        + "湖北,Hubei\n"
        + "湖南,Hunan\n"
        + "吉林,Jilin\n"
        + "江苏,Jiangsu\n"
        + "江西,Jiangxi\n"
        + "辽宁,Liaoning\n"
        + "内蒙古,Neimenggu\n"
        + "宁夏,Ningxia\n"
        + "青海,Qinghai\n"
        + "陕西,Shanxi\n"
        + "山西,Shanxi\n"
        + "山东,Shandong\n"
        + "四川,Sichuan\n"
        + "台湾,Taiwan\n"
        + "西藏,Tibet\n"
        + "新疆,Xinjiang\n"
        + "云南,Yunnan\n"
        + "浙江,Zhejiang\n"
        + "南海,Nanhai"
        ;


    public static String provinceDataTemplate = "{name: \"%s\", value: %s},\n";
    public static void main(String[] args) throws IOException {
        String dir = "src\\main\\resources\\region\\china\\";
        File file = new File(dir+"china_double.txt");

        String dataStr = readFileToString(file);

        List<String> regionList = Arrays.asList(dataStr.split("\n"));
        regionList = regionList.subList(1, regionList.size());
        StringBuilder mapDataSb = new StringBuilder();

        StringBuilder deadDataSb = new StringBuilder();

        regionList.stream().forEach(r -> {

            String  province = r.split(",")[0];
            String  value = r.split(",")[2];
            String dead = r.split(",")[1];
            mapDataSb.append(String.format(provinceDataTemplate, province, value));
            deadDataSb.append(String.format(provinceDataTemplate,province,dead));
        });

        String chinaHtml = FileUtils.readFileToString(new File(dir+"_china_double.html"));
        chinaHtml = chinaHtml.replace("\"XDATA\"", mapDataSb.toString());
        chinaHtml = chinaHtml.replace("\"DEADDATA\"", deadDataSb.toString());


        String chinaJs= FileUtils.readFileToString(new File(dir+"_china.js"));


        for(String cell:provinceList.split("\n")){
            String cn = cell.split(",")[0];
            String en = cell.split(",")[1];

            chinaHtml = chinaHtml.replace(cn,en);
            chinaJs = chinaJs.replace(cn,en);
        }

        FileUtils.writeStringToFile(new File(dir+"china_double.html"), chinaHtml);
        FileUtils.writeStringToFile(new File(dir+"china.js"), chinaJs);

    }
}