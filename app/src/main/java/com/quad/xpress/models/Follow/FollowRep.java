package com.quad.xpress.models.Follow;

/**
 * Created by kural on 2/10/2017.
 */

public class FollowRep {

        private String status;

        private FollowData[] data;

        private String code;

        public String getStatus ()
        {
            return status;
        }

        public void setStatus (String status)
        {
            this.status = status;
        }

        public FollowData[] getData ()
        {
            return data;
        }

        public void setData (FollowData[] data)
        {
            this.data = data;
        }

        public String getCode ()
        {
            return code;
        }

        public void setCode (String code)
        {
            this.code = code;
        }

        @Override
        public String toString()
        {
            return "ClassPojo [status = "+status+", data = "+data+", code = "+code+"]";
        }

}
