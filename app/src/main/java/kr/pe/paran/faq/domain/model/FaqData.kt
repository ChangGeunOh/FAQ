package kr.pe.paran.faq.domain.model

@kotlinx.serialization.Serializable
data class FaqData(
    val kbId: String,
    val question: String,
    val answer: String
)
