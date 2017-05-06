package com.quad.xpress.models.receivedFiles.register;

/**
 * Created by kural on 9/29/2016.
 */

public class DataCountry {
    private String id;

    private String un_region;

    private String iso;

    private String un_subregion;

    private String iso3;

    private String country_name;

    private String ph_code;

    public String getPh_code() {
        return ph_code;
    }

    public void setPh_code(String ph_code) {
        this.ph_code = ph_code;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

    public String getUn_region ()
    {
        return un_region;
    }

    public void setUn_region (String un_region)
    {
        this.un_region = un_region;
    }

    public String getIso ()
    {
        return iso;
    }

    public void setIso (String iso)
    {
        this.iso = iso;
    }

    public String getUn_subregion ()
    {
        return un_subregion;
    }

    public void setUn_subregion (String un_subregion)
    {
        this.un_subregion = un_subregion;
    }

    public String getIso3 ()
    {
        return iso3;
    }

    public void setIso3 (String iso3)
    {
        this.iso3 = iso3;
    }

    public String getCountry_name ()
    {
        return country_name;
    }

    public void setCountry_name (String country_name)
    {
        this.country_name = country_name;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", un_region = "+un_region+", iso = "+iso+", un_subregion = "+un_subregion+", iso3 = "+iso3+", country_name = "+country_name+"]";
    }
}
