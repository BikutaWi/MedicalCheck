package ch.hearc.medicalcheckwear.models

import java.sql.Timestamp

/*
* Project   : Medical Check Watch
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */


/**
 * Model which represents a notification
 * a notification is used to display some important information for our users
 * such as a treatment which has not been taken or a help request
 * each notification is composed of :
 * a type (help, treatment not taken,...)
 * some location coordinates to find the user if he needs help
 * a date when the notification was created
 * and a boolean (isclosed) which indicate if the notification has been treated (sent)
 */
class Notification {
    var id: Int? = null
    var iduser: Int? = null
    var notificationtype: NotificationType? = null
    var longitude: Double? = null
    var latitude: Double? = null
    var date: Timestamp? = null
    var isclosed: Boolean? = null
}