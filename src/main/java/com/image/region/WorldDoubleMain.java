/**
 * Alipay.com Inc. Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.image.region;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import sun.awt.OSInfo;
import sun.awt.OSInfo.OSType;

import static org.apache.commons.io.FileUtils.readFileToString;

/**
 * @author tritone
 * @version $Id: DataTransferMain.java, v 0.1 2019年03月23日 23:26 tritone Exp $
 */
public class WorldDoubleMain {
    // 下面这个值代表组数，比如 =8，就是160个国家分成8组，每组20个国家一个颜色

    private static int groupNum = 7;
    private static String worldHtml;
    private static String pre;

    static {
        if (OSInfo.getOSType().equals(OSType.WINDOWS)) {
            pre = "src\\main\\resources\\region\\";
        } else {
            pre = "src/main/resources/region/";
        }
        try {
            worldHtml = FileUtils.readFileToString(new File(
                pre + "_world_double.html"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //public static Double percentRed = 0.10;

    public static String countryDataTemplate = "{name: \"%s\", value: %d},\n";

    public static String colorTemplate = "{start: %s, end:%s}";


    private class Unit{
        String region;
        Integer dead;
        Integer all;

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public Integer getDead() {
            return dead;
        }

        public void setDead(Integer dead) {
            this.dead = dead;
        }

        public Integer getAll() {
            return all;
        }

        public void setAll(Integer all) {
            this.all = all;
        }
    }

    public static void main(String[] argv) throws IOException {
        File file = new File(pre + "world_double_data.txt");
        DecimalFormat df = new DecimalFormat("#.0000");
        List<String> countrys = getCountryList();
        String dataStr = readFileToString(file);

        if(dataStr.contains("\r\n")){
            dataStr = dataStr.replace("\r\n","\n");
        }
        List<String> regionList = Arrays.asList(dataStr.split("\n"));
        regionList = regionList.subList(1, regionList.size());

        StringBuilder allDataSb = new StringBuilder();
        StringBuilder deadDataSb = new StringBuilder();
        StringBuilder colorSb = new StringBuilder();

        Integer[] valueMax = {0};
        List<Integer> allDatas = new ArrayList<>();

        final Integer[] valueMin = {99999999};

        regionList.forEach(r -> {
            String region = r.split(",")[0];
            if (region.charAt(0) == '\"') {
                region = region.substring(1);
            }
            if (region.charAt(region.length() - 1) == '\"') {
                region = region.substring(0, region.length() - 1);
            }
            region = CountryReplace.countryConvert(region);
            if (!countrys.contains(region)) {
                return;
            }
            Integer dead = Integer.valueOf(r.split(",")[2]);
            Integer all = Integer.valueOf(r.split(",")[1]);

            allDatas.add(all);
            if (all > valueMax[0]) {
                valueMax[0] = all;
            }
            if (all < valueMin[0]) {
                valueMin[0] = all;
            }
            allDataSb.append(String.format(countryDataTemplate, region, all));
            deadDataSb.append(String.format(countryDataTemplate,region,dead));
        });

        calColor(allDatas,colorSb);


        System.out.println("MaxValue = " + df.format(valueMax[0]) + "\n");
        System.out.println("MinValue = " + df.format(valueMin[0]) + "\n");

        worldHtml = worldHtml.replace("\"COLORLIST\"", colorSb.toString());
        worldHtml = worldHtml.replace("\"ALLDATA\"", allDataSb.toString());
        worldHtml = worldHtml.replace("\"DEADDATA\"",deadDataSb.toString());
        FileUtils.writeStringToFile(new File(pre + "world_double.html"), worldHtml);

    }

    private static void calColor(List<Integer> allDatas, StringBuilder colorSb){
        allDatas.sort((r1, r2) -> {
            if (r1 > r2) {
                return 1;
            }
            if (r2 > r1) {
                return -1;
            }
            return 0;
        });

        for (int i = 0; i < groupNum; i++) {
            int index = allDatas.size() / groupNum * i;
            int indexNext = allDatas.size() / groupNum * (i + 1);
            Integer indexValue = allDatas.get(index);
            Integer indexNextValue = i == groupNum - 1 ? allDatas.get(allDatas.size() - 1) : allDatas.get(indexNext);
            colorSb.append(String.format(colorTemplate, indexValue, indexNextValue));
            colorSb.append(",");
        }
    }

    private static List<String> getCountryList() {
        List<String> result = new ArrayList<>();
        try {
            String countryStr = readFileToString(new File(pre + "country.txt"));
            Arrays.stream(countryStr.split("\n")).forEach(r -> {
                String r2 = r.split("@@@")[0];
                result.add(r2);
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}