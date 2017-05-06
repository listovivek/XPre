package com.quad.xpress.models.receivedFiles.Plist_Emotion;

/**
 * Created by kural on 9/3/2016.
 */
public class Emotion {
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
