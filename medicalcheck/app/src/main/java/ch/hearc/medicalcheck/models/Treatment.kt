package ch.hearc.medicalcheck.models

import java.sql.Timestamp

/*
* Project   : Medical Check Mobile
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

class Treatment {
    var id: Int? = null
    var idplanning: Int? = null
    var planning: Planning? = null
    var date: Timestamp? = null
    var istaken: Boolean? = null
}