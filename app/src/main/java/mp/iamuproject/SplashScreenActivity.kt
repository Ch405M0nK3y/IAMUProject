package mp.iamuproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import mp.iamuproject.api.HNWorker
import mp.iamuproject.databinding.ActivitySplashScreenBinding
import mp.iamuproject.framework.applyAnimation
import mp.iamuproject.framework.callDelayed
import mp.iamuproject.framework.getBooleanPreference
import mp.iamuproject.framework.isOnline
import mp.iamuproject.framework.startActivity

private const val DELAY = 3000L
const val DATA_IMPORTED = "mp.iamuproject.data_imported"
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startAnimations()
        redirect()
    }

    private fun startAnimations() {
        binding.tvSplash.applyAnimation(R.anim.fade_in)
        binding.ivSplash.applyAnimation(R.anim.slide_down)
    }

    private fun redirect() {

        if (getBooleanPreference(DATA_IMPORTED)) {
            callDelayed(DELAY) { startActivity<HostActivity>() }

        } else {

            if (isOnline()) {

                WorkManager.getInstance(this).apply {
                    enqueueUniqueWork(
                        DATA_IMPORTED,
                        ExistingWorkPolicy.KEEP,
                        OneTimeWorkRequest.Companion.from(HNWorker::class.java)
                    )
                }

            } else {
                binding.tvSplash.text = getString(R.string.no_internet)
                callDelayed(DELAY) { finish() }
            }
        }
    }

}