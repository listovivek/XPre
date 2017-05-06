package com.quad.xpress.models.Follow;

/**
 * Created by kural on 2/10/2017.
 */

public class FollowData {

        private String message;

        public String getMessage ()
        {
            return message;
        }

        public void setMessage (String message)
        {
            this.message = message;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [message = "+message+"]";
        }



}
