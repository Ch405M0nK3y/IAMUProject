package mp.iamuproject

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import mp.iamuproject.framework.setBooleanPreference
import mp.iamuproject.framework.startActivity

class HNReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        context.setBooleanPreference(DATA_IMPORTED)
        context.startActivity<HostActivity>()
    }
}