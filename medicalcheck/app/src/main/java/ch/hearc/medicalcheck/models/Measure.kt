package ch.hearc.medicalcheck.models

import java.sql.Timestamp

/*
* Project   : Medical Check Mobile
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

class Measure {
    var id: Int? = null
    var iduser: Int? = null
    var heartrate: Int? = null
    var date: Timestamp? = null
}