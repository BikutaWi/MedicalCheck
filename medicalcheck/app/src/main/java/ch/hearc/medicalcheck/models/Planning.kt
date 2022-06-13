package ch.hearc.medicalcheck.models

import java.time.DayOfWeek

/*
* Project   : Medical Check Mobile
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

class Planning {
    var id: Int? = null
    var idmedicine: Int? = null
    var medicine: Medicine? = null
    var day: DayOfWeek? = null
    var time: String? = null
    var quantity: Int? = null
}