package kr.pe.paran.faq.domain.model

import kotlinx.serialization.Serializable


@Serializable
data class FaqData(
    val prompt: String,
    val completion: String
)
