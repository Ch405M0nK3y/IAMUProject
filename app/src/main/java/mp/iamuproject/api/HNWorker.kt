package mp.iamuproject.api

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class HNWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams){
    override fun doWork(): Result {
        HNFetcher(context).fetchItems(20)
        return Result.success()
    }
}