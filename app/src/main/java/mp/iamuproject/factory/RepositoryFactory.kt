package mp.iamuproject.factory

import android.content.Context
import mp.iamuproject.dao.HNSqlHelper

fun getHNRepository(context: Context?) = HNSqlHelper(context)