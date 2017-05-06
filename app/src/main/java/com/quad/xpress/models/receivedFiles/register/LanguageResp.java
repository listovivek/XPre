package com.quad.xpress.models.receivedFiles.register;

/**
 * Created by kural on 9/29/2016.
 */

public class LanguageResp {

        private String status;

        private LanguageData[] data;

        private String code;

        public String getStatus ()
        {
            return status;
        }

        public void setStatus (String status)
        {
            this.status = status;
        }

        public LanguageData[] getData ()
        {
            return data;
        }

        public void setData (LanguageData[] data)
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
