package com.example.savefileeditor

import android.app.Activity
import android.os.Bundle
import android.net.Uri
import android.content.Intent
import android.view.Gravity
import android.widget.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.zip.ZipInputStream

class MainActivity : Activity() {
    private lateinit var status: TextView
    private lateinit var details: TextView
    private lateinit var editor: EditText
    private lateinit var export: Button
    private var editable = false
    private var jsonMode = false
    private var mode = "unknown"
    private var filename = "edited.txt"

    override fun onCreate(savedInstanceState: Bundle?) { super.onCreate(savedInstanceState); buildUi() }

    private fun buildUi() {
        val root = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(18,16,18,14); setBackgroundColor(0xFFF5F7FA.toInt()) }
        root.addView(TextView(this).apply { text = "Save File Editor"; textSize = 25f; setTextColor(0xFF182230.toInt()) })
        root.addView(TextView(this).apply { text = "Offline • Original files are never overwritten"; textSize = 13f; setTextColor(0xFF667085.toInt()); setPadding(0,4,0,12) })
        root.addView(Button(this).apply { text = "Open save / data file"; setOnClickListener { openFile() } })
        status = TextView(this).apply { text = "Choose a file to inspect its format."; setPadding(0,10,0,6); textSize = 14f }
        details = TextView(this).apply { textSize = 13f; setPadding(0,0,0,8) }
        editor = EditText(this).apply {
            textSize = 13f; gravity = Gravity.TOP or Gravity.START; typeface = android.graphics.Typeface.MONOSPACE
            minLines = 8; setPadding(12,12,12,12); hint = "Readable text / JSON appears here"
            background = android.graphics.drawable.GradientDrawable().apply { setColor(-1); setStroke(1,0xFFD0D5DD.toInt()); cornerRadius = 12f }
        }
        root.addView(status); root.addView(details)
        root.addView(ScrollView(this).apply { addView(editor) }, LinearLayout.LayoutParams(-1,0,1f))
        export = Button(this).apply { text = "Export edited copy"; isEnabled = false; setOnClickListener { exportFile() } }
        root.addView(export)
        root.addView(TextView(this).apply {
            text = "Safety: this starter does not execute Python pickle data or rewrite unknown binary saves. Ren'Py saves are inspected only; format-specific editing must be implemented and tested separately."
            textSize = 12f; setTextColor(0xFF667085.toInt()); setPadding(0,8,0,0)
        })
        setContentView(root)
    }

    private fun openFile() {
        startActivityForResult(Intent(Intent.ACTION_OPEN_DOCUMENT).apply { addCategory(Intent.CATEGORY_OPENABLE); type = "*/*" }, 100)
    }

    @Deprecated("Starter uses legacy result callback for simplicity")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK || data?.data == null) return
        val uri = data.data!!
        if (requestCode == 100) loadFile(uri)
        if (requestCode == 101) writeFile(uri)
    }

    private fun loadFile(uri: Uri) {
        try {
            filename = queryName(uri) ?: "edited.txt"
            val bytes = contentResolver.openInputStream(uri)?.use { it.readBytes() } ?: error("Unable to read file")
            val zip = bytes.size >= 4 && bytes[0] == 0x50.toByte() && bytes[1] == 0x4B.toByte()
            val sample = bytes.take(minOf(bytes.size, 4096))
            val textLike = sample.none { it == 0.toByte() } && sample.count { (it.toInt() and 255) < 9 } < 3
            if (zip) {
                mode = "zip"; editable = false; jsonMode = false
                status.text = "Detected ZIP archive — read-only in this version"
                details.text = "File: $filename\nSize: ${bytes.size} bytes\n\n${zipEntries(bytes)}"
                editor.setText("Archive was not modified. For Ren'Py saves, the 'log' entry may contain serialized game state. This starter does not rewrite that state.")
                editor.isEnabled = false; export.isEnabled = false
            } else if (textLike) {
                val raw = bytes.toString(Charsets.UTF_8)
                jsonMode = isJson(raw)
                mode = if (jsonMode) "json" else "text"
                val display = if (jsonMode) prettyJson(raw) ?: raw else raw
                editable = true; status.text = if (jsonMode) "Detected JSON — editable" else "Detected readable text — editable"
                details.text = "File: $filename\nSize: ${bytes.size} bytes\nExport will create a new file."
                editor.setText(display); editor.isEnabled = true; export.isEnabled = true
            } else {
                mode = "binary"; editable = false; jsonMode = false
                status.text = "Detected binary / unknown format — read-only"
                details.text = "File: $filename\nSize: ${bytes.size} bytes\nFirst 512 bytes shown as hexadecimal."
                editor.setText(hex(bytes)); editor.isEnabled = false; export.isEnabled = false
            }
        } catch (e: Exception) {
            status.text = "Could not open file"; details.text = e.message ?: "Unknown error"; editor.setText(""); export.isEnabled = false
        }
    }

    private fun zipEntries(bytes: ByteArray): String {
        val rows = mutableListOf<String>()
        try {
            ZipInputStream(bytes.inputStream()).use { z ->
                var e = z.nextEntry; var count = 0
                while (e != null && count++ < 200) {
                    rows.add("• ${e.name}" + if (e.size >= 0) " (${e.size} bytes)" else "")
                    e = z.nextEntry
                }
            }
        } catch (e: Exception) { rows.add("Entry scan failed: ${e.message}") }
        if (rows.any { it.contains("renpy_version") } && rows.any { it.contains("log") }) rows.add("\nLikely Ren'Py save archive.")
        return "Archive entries:\n" + rows.joinToString("\n")
    }

    private fun isJson(s: String): Boolean {
        val t=s.trim(); if (!((t.startsWith("{") && t.endsWith("}")) || (t.startsWith("[") && t.endsWith("]")))) return false
        return try { if (t.startsWith("{")) JSONObject(t) else JSONArray(t); true } catch (_: Exception) { false }
    }
    private fun prettyJson(s: String): String? = try { if (s.trim().startsWith("{")) JSONObject(s).toString(2) else JSONArray(s).toString(2) } catch (_: Exception) { null }

    private fun exportFile() {
        if (!editable) return
        val name = if (filename.contains(".")) filename.substringBeforeLast(".") + "-edited." + filename.substringAfterLast(".") else "$filename-edited"
        startActivityForResult(Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE); type = if (jsonMode) "application/json" else "text/plain"; putExtra(Intent.EXTRA_TITLE,name)
        },101)
    }
    private fun writeFile(uri: Uri) {
        val content = editor.text.toString()
        if (jsonMode && !isJson(content)) { Toast.makeText(this,"Invalid JSON; fix syntax before exporting.",Toast.LENGTH_LONG).show(); return }
        try {
            contentResolver.openOutputStream(uri,"wt")?.use { it.write(content.toByteArray(Charsets.UTF_8)) } ?: error("Could not write output")
            Toast.makeText(this,"Edited copy exported.",Toast.LENGTH_LONG).show()
        } catch (e: Exception) { Toast.makeText(this,"Export failed: ${e.message}",Toast.LENGTH_LONG).show() }
    }
    private fun queryName(uri: Uri): String? = try {
        contentResolver.query(uri,arrayOf(android.provider.OpenableColumns.DISPLAY_NAME),null,null,null)?.use { if(it.moveToFirst()) it.getString(0) else null }
    } catch (_: Exception) { null }
    private fun hex(b: ByteArray): String {
        val out=StringBuilder(); val n=minOf(b.size,512)
        for(i in 0 until n step 16) { out.append(String.format("%08X  ",i)); for(j in i until minOf(i+16,n)) out.append(String.format("%02X ",b[j].toInt() and 255)); out.append('\n') }
        if(b.size>n) out.append("\n… ${b.size-n} more bytes omitted.")
        return out.toString()
    }
}
