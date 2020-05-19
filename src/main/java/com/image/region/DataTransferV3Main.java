/**
 * Alipay.com Inc. Copyright (c) 2004-2019 All Rights Reserved.
 */
package com.image.region;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.apache.commons.io.FileUtils.readFileToString;

/**
 * @author tritone
 * @version $Id: DataTransferMain.java, v 0.1 2019年03月23日 23:26 tritone Exp $
 */
public class DataTransferV3Main {

    // 下面这个值代表组数，比如 =8，就是160个国家分成8组，每组20个国家一个颜色
    public static int groupNum = 7;

    public  static String worldHtml;

    static {
        try {
            worldHtml = FileUtils.readFileToString(new File(
                        "src/main/resources/region/_V3world.html"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String countryDataTemplate = "{name: \"%s\", value: %s},\n";

    public static String colorTemplate = "{start: %s, end:%s}";

    public static void main(String[] argv) throws IOException {
        File file = new File("src/main/resources/region/data.txt");
        String dataStr = readFileToString(file);

        List<String> countrys = getCountryList();

        List<String> regionList = Arrays.asList(dataStr.split("\n"));
        regionList = regionList.subList(1, regionList.size());
        StringBuilder countryListDataSb = new StringBuilder();

        StringBuilder colorSb = new StringBuilder();

        Double[] valueMax = {0.0};
        List<Double> values = new ArrayList<>();

        final Double[] valueMin = {9999999999.0};

        List<Double> finalValues = values;
        regionList.stream().forEach(r -> {

            int numblerStart = r.lastIndexOf(",");
            String region = r.substring(0, numblerStart);

            if (region.charAt(0) == '\"') {
                region = region.substring(1, region.length());
            }
            if (region.charAt(region.length() - 1) == '\"') {
                region = region.substring(0, region.length() - 1);
            }
            region = CountryReplace.countryConvert(region);

            if (!countrys.contains(region) ) {
                return;
            }

            Double value = Double.valueOf(r.substring(numblerStart + 1, r.length()));

            finalValues.add(value);
            if (value > valueMax[0]) {
                valueMax[0] = value;
            }
            if (value < valueMin[0]) {
                valueMin[0] = value;
            }
            countryListDataSb.append(String.format(countryDataTemplate, region, value));
        });

        values.sort(new Comparator<Double>() {
            @Override
            public int compare(Double r1, Double r2) {
                if (r1 > r2) {
                    return 1;
                }
                if (r2 > r1) {
                    return -1;
                }
                return 0;
            }
        });

        for (int i = 0; i < groupNum; i++) {
            int index = values.size() / groupNum * i;
            int indexNext = values.size() / groupNum * (i + 1);
            if(values.size()-1 < index){
                break;
            }

            Double indexValue =  values.get(index);
            Double indexNextValue = i == groupNum - 1 ? values.get(values.size() - 1) : values.get(indexNext);
            colorSb.append(String.format(colorTemplate, indexValue, indexNextValue));
            colorSb.append(",");
        }
        DecimalFormat df = new DecimalFormat("#.0000");
        System.out.println("MaxValue = " + df.format(valueMax[0]) + "\n");
        System.out.println("MinValue = " + df.format(valueMin[0]) + "\n");

        worldHtml = worldHtml.replace("\"COLORLIST\"", colorSb.toString());
        worldHtml = worldHtml.replace("\"COUNTRYLIST\"", countryListDataSb.toString());

        FileUtils.writeStringToFile(new File("src/main/resources/region/world.html"), worldHtml);

    }



    public static List<String> getCountryList() {
        List<String> result = new ArrayList<>();
        try {
            String countryStr = readFileToString(new File("src/main/resources/region/country.txt"));

            //List<Country> countries = JSONArray.parseArray(countryStr,Country.class);

            Arrays.asList(countryStr.split("\n")).stream().forEach(r -> {
                String r2 = r.split("@@@")[0];
                result.add(r2);
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}