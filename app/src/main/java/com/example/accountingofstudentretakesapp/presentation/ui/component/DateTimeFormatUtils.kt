package com.example.accountingofstudentretakesapp.presentation.ui.component

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun formatIsoDateTimeToHuman(isoDateTime: String): String {
    if (isoDateTime.isBlank()) return ""
    return try {
        val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val date = isoFormat.parse(isoDateTime) ?: return isoDateTime
        val humanFormat = SimpleDateFormat("dd.MM.yyyy, HH:mm", Locale.getDefault())
        humanFormat.format(date)
    } catch (_: Exception) {
        isoDateTime
    }
}

fun formatDateTimeToIso(dateMillis: Long?, hour: Int, minute: Int): String {
    if (dateMillis == null) return ""
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = dateMillis
    calendar.set(Calendar.HOUR_OF_DAY, hour)
    calendar.set(Calendar.MINUTE, minute)
    calendar.set(Calendar.SECOND, 0)
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    return format.format(calendar.time)
}

