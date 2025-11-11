package com.autoresponder.app.database

import android.annotation.SuppressLint
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.autoresponder.app.model.Message
import java.text.SimpleDateFormat
import java.util.Date

class DatabaseHelper(context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {
    
    companion object {
        private const val TAG = "DatabaseHelper"
        private const val DATABASE_NAME = "autoresponder.db"
        private const val DATABASE_VERSION = 2
        const val TABLE_MESSAGES = "messages"
        const val TABLE_PRESETS = "presets"
        const val COLUMN_ID = "_id"
        const val COLUMN_SENDER = "sender"
        const val COLUMN_MESSAGE = "message"
        const val COLUMN_TIMESTAMP = "timestamp"
        const val COLUMN_REPLY = "reply"
        const val COLUMN_PLATFORM = "platform"
        const val COLUMN_KEYWORD = "keyword"
        const val COLUMN_IS_CASE_SENSITIVE = "is_case_sensitive"
        const val COLUMN_IS_EXACT_MATCH = "is_exact_match"

        private const val TABLE_MESSAGES_CREATE = """
            CREATE TABLE $TABLE_MESSAGES (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_SENDER TEXT,
                $COLUMN_MESSAGE TEXT,
                $COLUMN_TIMESTAMP TEXT,
                $COLUMN_REPLY TEXT,
                $COLUMN_PLATFORM TEXT
            )
        """
        
        private const val TABLE_PRESETS_CREATE = """
            CREATE TABLE $TABLE_PRESETS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_KEYWORD TEXT,
                $COLUMN_REPLY TEXT,
                $COLUMN_IS_CASE_SENSITIVE INTEGER DEFAULT 0,
                $COLUMN_IS_EXACT_MATCH INTEGER DEFAULT 0
            )
        """
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(TABLE_MESSAGES_CREATE)
        db.execSQL(TABLE_PRESETS_CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL(TABLE_PRESETS_CREATE)
        }
    }

    fun insertMessage(sender: String, message: String, timestamp: String, reply: String, platform: String = "whatsapp") {
        val db = writableDatabase
        val values = android.content.ContentValues().apply {
            put(COLUMN_SENDER, sender)
            put(COLUMN_MESSAGE, message)
            put(COLUMN_TIMESTAMP, timestamp)
            put(COLUMN_REPLY, reply)
            put(COLUMN_PLATFORM, platform)
        }
        db.insert(TABLE_MESSAGES, null, values)
        db.close()
    }

    @SuppressLint("SimpleDateFormat")
    fun deleteOldMessages() {
        val db = writableDatabase
        try {
            val currentDate = SimpleDateFormat("yyyy-MM-dd").format(Date())
            val whereClause = "$COLUMN_TIMESTAMP < ?"
            val whereArgs = arrayOf("$currentDate 00:00:00")
            db.delete(TABLE_MESSAGES, whereClause, whereArgs)
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting old messages", e)
        } finally {
            db.close()
        }
    }

    /**
     * Delete messages older than specified hours
     * @param hours Number of hours to keep messages
     */
    @SuppressLint("SimpleDateFormat")
    fun deleteMessagesOlderThan(hours: Int) {
        val db = writableDatabase
        try {
            val calendar = java.util.Calendar.getInstance()
            calendar.add(java.util.Calendar.HOUR_OF_DAY, -hours)
            val cutoffTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.time)
            
            val whereClause = "$COLUMN_TIMESTAMP < ?"
            val whereArgs = arrayOf(cutoffTime)
            val deletedCount = db.delete(TABLE_MESSAGES, whereClause, whereArgs)
            Log.d(TAG, "Deleted $deletedCount messages older than $hours hours")
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting old messages", e)
        } finally {
            db.close()
        }
    }

    /**
     * Get all messages grouped by sender
     */
    fun getAllMessages(): List<Message> {
        val messages = mutableListOf<Message>()
        val db = readableDatabase
        var cursor: android.database.Cursor? = null

        try {
            val query = "SELECT * FROM $TABLE_MESSAGES ORDER BY $COLUMN_TIMESTAMP DESC"
            cursor = db.rawQuery(query, null)

            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                    val sender = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SENDER))
                    val message = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MESSAGE))
                    val timestamp = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIMESTAMP))
                    val reply = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REPLY))
                    val platform = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PLATFORM))

                    messages.add(Message(id, sender, message, timestamp, reply, platform))
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting all messages", e)
        } finally {
            cursor?.close()
            db.close()
        }

        return messages
    }

    /**
     * Get distinct senders from messages
     */
    fun getDistinctSenders(): List<String> {
        val senders = mutableListOf<String>()
        val db = readableDatabase
        var cursor: android.database.Cursor? = null

        try {
            val query = "SELECT DISTINCT $COLUMN_SENDER FROM $TABLE_MESSAGES ORDER BY $COLUMN_SENDER"
            cursor = db.rawQuery(query, null)

            if (cursor.moveToFirst()) {
                do {
                    val sender = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SENDER))
                    senders.add(sender)
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting distinct senders", e)
        } finally {
            cursor?.close()
            db.close()
        }

        return senders
    }

    /**
     * Delete a specific message by ID
     */
    fun deleteMessage(id: Int) {
        val db = writableDatabase
        try {
            db.delete(TABLE_MESSAGES, "$COLUMN_ID = ?", arrayOf(id.toString()))
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting message", e)
        } finally {
            db.close()
        }
    }

    /**
     * Delete all messages from a specific sender
     */
    fun deleteMessagesBySender(sender: String) {
        val db = writableDatabase
        try {
            db.delete(TABLE_MESSAGES, "$COLUMN_SENDER = ?", arrayOf(sender))
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting messages by sender", e)
        } finally {
            db.close()
        }
    }

    /**
     * Delete all messages
     */
    fun deleteAllMessages() {
        val db = writableDatabase
        try {
            db.delete(TABLE_MESSAGES, null, null)
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting all messages", e)
        } finally {
            db.close()
        }
    }

    fun getChatHistoryBySender(sender: String, platform: String = "whatsapp"): List<Message> {
        val messages = mutableListOf<Message>()
        val db = readableDatabase
        var cursor: android.database.Cursor? = null

        try {
            val query = """
                SELECT * FROM $TABLE_MESSAGES 
                WHERE $COLUMN_SENDER = ? AND $COLUMN_PLATFORM = ?
                ORDER BY $COLUMN_TIMESTAMP DESC 
                LIMIT 7
            """
            cursor = db.rawQuery(query, arrayOf(sender, platform))

            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                    val message = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MESSAGE))
                    val timestamp = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIMESTAMP))
                    val reply = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REPLY))
                    val platformName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PLATFORM))

                    messages.add(Message(id, sender, message, timestamp, reply, platformName))
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting chat history", e)
        } finally {
            cursor?.close()
            db.close()
        }

        return messages
    }

    fun getAllMessagesBySender(sender: String, platform: String = "whatsapp"): List<Message> {
        val messages = mutableListOf<Message>()
        val db = readableDatabase
        var cursor: android.database.Cursor? = null

        try {
            val query = """
                SELECT * FROM $TABLE_MESSAGES 
                WHERE $COLUMN_SENDER = ? AND $COLUMN_PLATFORM = ?
                ORDER BY $COLUMN_TIMESTAMP DESC
            """
            cursor = db.rawQuery(query, arrayOf(sender, platform))

            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                    val message = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MESSAGE))
                    val timestamp = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TIMESTAMP))
                    val reply = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REPLY))
                    val platformName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PLATFORM))

                    messages.add(Message(id, sender, message, timestamp, reply, platformName))
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting all messages", e)
        } finally {
            cursor?.close()
            db.close()
        }

        return messages
    }

    fun getTotalRepliesCount(): Int {
        val db = readableDatabase
        var cursor: android.database.Cursor? = null
        var count = 0

        try {
            cursor = db.rawQuery("SELECT COUNT(*) FROM $TABLE_MESSAGES", null)
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting total replies count", e)
        } finally {
            cursor?.close()
            db.close()
        }

        return count
    }

    @SuppressLint("SimpleDateFormat")
    fun getTodayRepliesCount(): Int {
        val db = readableDatabase
        var cursor: android.database.Cursor? = null
        var count = 0

        try {
            val currentDate = SimpleDateFormat("yyyy-MM-dd").format(Date())
            val query = "SELECT COUNT(*) FROM $TABLE_MESSAGES WHERE $COLUMN_TIMESTAMP >= ?"
            cursor = db.rawQuery(query, arrayOf("$currentDate 00:00:00"))
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting today replies count", e)
        } finally {
            cursor?.close()
            db.close()
        }

        return count
    }

    // Preset methods
    fun insertPreset(keyword: String, reply: String, isCaseSensitive: Boolean = false, isExactMatch: Boolean = false): Long {
        val db = writableDatabase
        val values = android.content.ContentValues().apply {
            put(COLUMN_KEYWORD, keyword)
            put(COLUMN_REPLY, reply)
            put(COLUMN_IS_CASE_SENSITIVE, if (isCaseSensitive) 1 else 0)
            put(COLUMN_IS_EXACT_MATCH, if (isExactMatch) 1 else 0)
        }
        val result = db.insert(TABLE_PRESETS, null, values)
        db.close()
        return result
    }

    fun updatePreset(id: Int, keyword: String, reply: String, isCaseSensitive: Boolean, isExactMatch: Boolean) {
        val db = writableDatabase
        val values = android.content.ContentValues().apply {
            put(COLUMN_KEYWORD, keyword)
            put(COLUMN_REPLY, reply)
            put(COLUMN_IS_CASE_SENSITIVE, if (isCaseSensitive) 1 else 0)
            put(COLUMN_IS_EXACT_MATCH, if (isExactMatch) 1 else 0)
        }
        db.update(TABLE_PRESETS, values, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
    }

    fun deletePreset(id: Int) {
        val db = writableDatabase
        db.delete(TABLE_PRESETS, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
    }

    fun getAllPresets(): List<com.autoresponder.app.model.Preset> {
        val presets = mutableListOf<com.autoresponder.app.model.Preset>()
        val db = readableDatabase
        var cursor: android.database.Cursor? = null

        try {
            cursor = db.rawQuery("SELECT * FROM $TABLE_PRESETS ORDER BY $COLUMN_ID DESC", null)
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                    val keyword = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_KEYWORD))
                    val reply = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REPLY))
                    val isCaseSensitive = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_CASE_SENSITIVE)) == 1
                    val isExactMatch = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_EXACT_MATCH)) == 1

                    presets.add(com.autoresponder.app.model.Preset(id, keyword, reply, isCaseSensitive, isExactMatch))
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting presets", e)
        } finally {
            cursor?.close()
            db.close()
        }

        return presets
    }

    fun findMatchingPreset(message: String): com.autoresponder.app.model.Preset? {
        val db = readableDatabase
        var cursor: android.database.Cursor? = null

        try {
            cursor = db.rawQuery("SELECT * FROM $TABLE_PRESETS", null)
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                    val keyword = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_KEYWORD))
                    val reply = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_REPLY))
                    val isCaseSensitive = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_CASE_SENSITIVE)) == 1
                    val isExactMatch = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_EXACT_MATCH)) == 1

                    val messageToCheck = if (isCaseSensitive) message else message.lowercase()
                    val keywordToCheck = if (isCaseSensitive) keyword else keyword.lowercase()

                    val matches = if (isExactMatch) {
                        messageToCheck == keywordToCheck
                    } else {
                        messageToCheck.contains(keywordToCheck)
                    }

                    if (matches) {
                        return com.autoresponder.app.model.Preset(id, keyword, reply, isCaseSensitive, isExactMatch)
                    }
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error finding matching preset", e)
        } finally {
            cursor?.close()
            db.close()
        }

        return null
    }
}

