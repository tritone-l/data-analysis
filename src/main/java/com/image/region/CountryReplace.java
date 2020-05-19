package com.image.region;

public class CountryReplace {
    public static String countryConvert(String source) {
        if (source.equals("Russian Federation")) {
            return "Russia";
        }

        if (source.equals("South Korea")) {
            return "Korea";
        }
        if (source.equals("North Korea")) {
            return "Dem. Rep. Korea";
        }

        if (source.equals("Democratic Republic of the Congo")) {
            return "Dem. Rep. Congo";
        }

        if (source.equals("South Sudan")) {
            return "S. Sudan";
        }

        if (source.equals("Central African Republic")) {
            return "Central African Rep.";
        }

        if(source.equals("Czechia")){
            return "Czech Rep.";
        }

        if(source.equals("Equatorial Guinea")){
            return "Eq. Guinea";
        }

        if(source.equals("Dominican Republic")){
            return "Dominican Rep.";
        }

        if(source.equals("Laos")){
            return "Lao PDR";
        }

        if(source.equals("Bosnia and Herzegovina")){
            return "Bosnia and Herz.";
        }

        if(source.equals("Solomon Islands")){
            return "Solomon Is.";
        }
        if(source.equals("Antigua and Barbuda")){
            return "Antigua and Barb.";
        }
        if(source.equals("Saint Vincent and the Grenadines")){
            return "St. Vin. and Gren.";
        }
        if(source.equals("Virgin Islands, U.S.")){
            return "U.S. Virgin Is.";
        }

        if(source.equals("Cabo Verde")){
            return "Cape Verde";
        }

        return source;
    }
}
