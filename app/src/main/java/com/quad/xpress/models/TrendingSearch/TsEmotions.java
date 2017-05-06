package com.quad.xpress.models.TrendingSearch;

/**
 * Created by kural on 3/3/2017.
 */

public class TsEmotions {
    private String count;

    private String emotion;

    public String getCount ()
    {
        return count;
    }

    public void setCount (String count)
    {
        this.count = count;
    }

    public String getEmotion ()
    {
        return emotion;
    }

    public void setEmotion (String emotion)
    {
        this.emotion = emotion;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [count = "+count+", emotion = "+emotion+"]";
    }
}
