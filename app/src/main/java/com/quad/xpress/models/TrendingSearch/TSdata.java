package com.quad.xpress.models.TrendingSearch;

/**
 * Created by kural on 3/3/2017.
 */

public class TSdata {

        private TsRecords[] Records;

        private String last;

        public TsRecords[] getRecords ()
        {
            return Records;
        }

    public void setRecords (TsRecords[] Records)
    {
        this.Records = Records;
    }

    public String getLast ()
    {
        return last;
    }

    public void setLast (String last)
    {
        this.last = last;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Records = "+Records+", last = "+last+"]";
    }
}
