package ch.hearc.medicalcheckwear.models

/*
* Project   : Medical Check Watch
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */


/**
 * Model which represents a user
 * a user is either a carekeeper or a patient
 * a carekeeper check if their patient are ok
 * a patient can ask for help if it's needed
 * each user has some useful information to indentify them such as firstname, login info (username, password),...
 */
class User  {
    var id: Int? = null
    var username: String? = null
    var password: String? = null
    var email: String? = null
    var firstname: String? = null
    var lastname: String? = null
    var picture: String? = null
    var iscarekeeper = false
}