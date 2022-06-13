package ch.hearc.medicalcheck.models

/*
* Project   : Medical Check Mobile
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

class User() {
    var id: Int? = null
    var username: String? = null
    var password: String? = null
    var email: String? = null
    var firstname: String? = null
    var lastname: String? = null
    var picture: String? = null
    var iscarekeeper: Boolean = false
}