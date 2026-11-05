package com.gps_g6.bestbiddertcg.feature.auction.presentation.create

import java.time.LocalDateTime
import java.time.temporal.ChronoUnit


/**
 * Redondea hacia arriba al siguiente bloque de 15 minutos (:00, :15, :30, :45).
 * El backend rechaza horas de inicio que no estén alineadas.
 * TODO: revisar
 */
internal fun LocalDateTime.snapUpToQuarterHour(): LocalDateTime {
    val startOfHour = truncatedTo(ChronoUnit.HOURS)
    val quarters = (minute + 14) / 15
    return startOfHour.plusMinutes(quarters * 15L)
}

/**
 * Primera franja de 15 minutos que queda en el futuro.
 */
internal fun defaultStartDateTime(): LocalDateTime {
    return LocalDateTime.now().plusMinutes(1).snapUpToQuarterHour()
}