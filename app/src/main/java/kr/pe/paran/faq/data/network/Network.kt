package kr.pe.paran.faq.data.network

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kr.pe.paran.faq.common.utils.Logcat
import kr.pe.paran.faq.domain.model.FaqData
import org.jsoup.Jsoup

class Network {

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(json = Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
            })
        }
        install(Logging)
    }

    private val json = Json { ignoreUnknownKeys = true }

    val headers: HeadersBuilder.() -> Unit = {
        append(HttpHeaders.Origin, "https://ermsweb.kt.com")
        append(HttpHeaders.Host, "ermsweb.kt.com")
        append(HttpHeaders.Referrer, "https://ermsweb.kt.com/pc/faq/faqList.do")
    }
    suspend fun search(index: Int): List<FaqData> {

        val faqUrl = "https://ermsweb.kt.com/pc/faq/faqListCateAjax.do?pageNo=$index"
        var response = client.request(faqUrl) {
            method = HttpMethod.Get
            contentType(ContentType.Application.FormUrlEncoded)
        }

        val document = Jsoup.parseBodyFragment(response.bodyAsText())

        Logcat.i(document.toString())
        document.select(".accordion-trigger").forEach {
            val kbid = it.attr("kbid")
            val question = it.selectFirst(".qna")?.text()
            val answerUrl = "https://ermsweb.kt.com/selectContents"
            response = client.request(answerUrl) {
                method = HttpMethod.Post
                headers {
                    headers()
                }
                contentType(ContentType.Application.Json)
                setBody("""{"kbId":"$kbid"}""")
            }
            val answer = Jsoup.parse(response.bodyAsText()).text()
            Logcat.i("$kbid : $question : $answer")
        }

        return emptyList()
    }

}