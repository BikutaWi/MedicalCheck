package ch.hearc.medicalcheck.models

import java.sql.Timestamp

/*
* Project   : Medical Check Mobile
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

class Medicine {
    var id: Int? = null
    var iduser: Int? = null
    var user: User? = null
    var name: String? = null
    var begindate: Timestamp? = null
    var enddate: Timestamp? = null
    var dose: String? = null
}