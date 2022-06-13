package ch.hearc.medicalcheck.models

/*
* Project   : Medical Check Mobile
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

class Follow {
    var id: Int? = null
    var iduserpatient: Int? = null
    var userpatient: User? = null
    var idusercarekeeper: Int? = null
    var usercarekeeper: User? = null
    var relationship: String? = null
}