package ch.hearc.medicalcheck.models

import java.sql.Timestamp

/*
* Project   : Medical Check Mobile
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

class Notification {
    var id: Int? = null
    var iduser: Int? = null
    var idusertonotify: Int? = null
    var notificationtype: NotificationType? = null
    var date: Timestamp? = null
    var longitude: Double? = null
    var latitude: Double? = null
    var isclosed: Boolean? = null

}