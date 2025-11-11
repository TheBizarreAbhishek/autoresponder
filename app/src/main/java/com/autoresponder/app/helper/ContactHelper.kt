package com.autoresponder.app.helper

import android.content.Context
import android.provider.ContactsContract
import android.util.Log

object ContactHelper {
    private const val TAG = "ContactHelper"

    /**
     * Get contact name from phone number
     * Returns the contact name if found, otherwise returns the phone number
     */
    fun getContactName(context: Context, phoneNumber: String): String {
        return try {
            val contacts = context.contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                arrayOf(
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER
                ),
                "${ContactsContract.CommonDataKinds.Phone.NUMBER} LIKE ?",
                arrayOf("%${phoneNumber.replace(" ", "").replace("-", "").replace("+", "")}%"),
                null
            )

            contacts?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val nameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                    if (nameIndex >= 0) {
                        val name = cursor.getString(nameIndex)
                        if (!name.isNullOrBlank()) {
                            return name
                        }
                    }
                }
            }

            // If no contact found, return the phone number
            phoneNumber
        } catch (e: SecurityException) {
            Log.e(TAG, "Permission denied to read contacts", e)
            phoneNumber
        } catch (e: Exception) {
            Log.e(TAG, "Error getting contact name", e)
            phoneNumber
        }
    }

    /**
     * Check if a string is a phone number (contains digits)
     */
    fun isPhoneNumber(text: String): Boolean {
        return text.any { it.isDigit() }
    }

    /**
     * Normalize phone number for comparison
     */
    fun normalizePhoneNumber(phoneNumber: String): String {
        return phoneNumber.replace(" ", "")
            .replace("-", "")
            .replace("+", "")
            .replace("(", "")
            .replace(")", "")
    }
}

