package com.codeofduty.appointcare.activities

import android.app.AlertDialog

interface BookingStatusListener {
    fun onUpdateBookingStatus(status: String, loadingDialog: AlertDialog, bookingId: String)
}
