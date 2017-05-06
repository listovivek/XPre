package com.quad.xpress.models.acceptRejectCount;

/**
 * Created by kural on 11/12/2016.
 */

public class AcceptRejectCount {

        private String status;

        private acceptData[] data;

        private String code;

        public String getStatus ()
        {
            return status;
        }

        public void setStatus (String status)
        {
            this.status = status;
        }

        public acceptData[] getData ()
        {
            return data;
        }

        public void setData (acceptData[] data)
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
