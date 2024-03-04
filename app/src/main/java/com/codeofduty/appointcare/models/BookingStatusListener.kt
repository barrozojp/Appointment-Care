package com.codeofduty.appointcare.activities

import android.app.AlertDialog

interface BookingStatusListener {
    fun onUpdateBookingStatus(patientId: String, status: String, loadingDialog: AlertDialog)
}
