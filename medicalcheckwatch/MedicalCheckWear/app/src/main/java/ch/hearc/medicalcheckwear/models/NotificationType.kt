package ch.hearc.medicalcheckwear.models

/*
* Project   : Medical Check Watch
* Authors   : William Bikuta, Milán Cerviño, Ilyas Boillat, David Oktay
* Date      : 28.01.2022
* Class     : INF3dlm-a
* */

/**
 * Enum which represents each notification type
 * TREATMENT_NOT_TAKEN is triggered when a user forget to take his treatment
 * HELP is triggered when a user explicitly ask for help
 */
enum class NotificationType {
    TREATMENT_NOT_TAKEN,
    HELP;
}