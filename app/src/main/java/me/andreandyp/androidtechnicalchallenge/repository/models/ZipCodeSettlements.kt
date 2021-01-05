package me.andreandyp.androidtechnicalchallenge.repository.models

data class ZipCodeSettlements(
    val locality: String,
    val federalEntity: String,
    val settlements: List<String>,
    val municipality: String
)