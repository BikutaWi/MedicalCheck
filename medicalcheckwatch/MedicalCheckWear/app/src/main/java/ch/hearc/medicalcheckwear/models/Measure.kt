package ch.hearc.medicalcheckwear.models

import java.sql.Timestamp

/*
* Project   : Medical Check Watch
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

/**
 * Model which represents a mesure
 * a mesure is used to display some information about a heart rate
 * a measure is attached to a user, a heartrate and a date when the measure has been recorded
 */
class Measure {
    var id: Int? = null
    var iduser: Int? = null
    var heartrate: Int? = null
    var date: Timestamp? = null
}