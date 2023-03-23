package kr.pe.paran.faq.present.screen

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kr.pe.paran.faq.common.utils.Utils
import kr.pe.paran.faq.data.network.Network
import kr.pe.paran.faq.domain.model.FaqData
import java.io.File

class MainViewModel : ViewModel() {

    private val network = Network()

    private var _pageNo = MutableStateFlow(0)
    val pageNo = _pageNo.asStateFlow()

    fun startCrawling(context: Context) {
        // pagNo : 1.. 137
        _pageNo.value = 0
        val faqList = mutableListOf<FaqData>()
        viewModelScope.launch {
            // 1.. 137
            (1..137).forEach {
                _pageNo.value = it
                val list = network.search(it)
                faqList.addAll(list)
            }

            val data = Json.encodeToString(faqList)
            saveFile(context = context, data)
        }
    }


    private fun saveFile(context: Context, data: String) {
        viewModelScope.launch {
            val outputFile = File(context.cacheDir, "kt_faq.json")
            outputFile.writeText(data)
            Utils.shareFile(context = context, file = outputFile)
        }
        _pageNo.value = 0
    }
}