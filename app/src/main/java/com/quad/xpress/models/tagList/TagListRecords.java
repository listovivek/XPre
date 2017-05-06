package com.quad.xpress.models.tagList;

/**
 * Created by kural on 7/20/2016.
 */
public class TagListRecords {
        private String count;

        private String isUserChecked;

        private String emotion;

        public String getCount ()
        {
            return count;
        }

        public void setCount (String count)
        {
            this.count = count;
        }

        public String getIsUserChecked ()
        {
            return isUserChecked;
        }

        public void setIsUserChecked (String isUserChecked)
        {
            this.isUserChecked = isUserChecked;
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
            return "ClassPojo [count = "+count+", isUserChecked = "+isUserChecked+", emotion = "+emotion+"]";
        }
    }



