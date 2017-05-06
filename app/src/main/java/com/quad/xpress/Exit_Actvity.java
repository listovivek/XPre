package com.quad.xpress;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by kural on 28/4/17.
 */

public class Exit_Actvity extends Activity{


        @Override protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);

            if(android.os.Build.VERSION.SDK_INT >= 21)
            {
                finishAndRemoveTask();
            }
            else
            {
                finish();
            }
        }

        public static void exitApplicationAnRemoveFromRecent(Context mContext)
        {
            Intent intent = new Intent(mContext, Exit_Actvity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK  | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_NO_ANIMATION);

            mContext.startActivity(intent);
        }

}
