package com.example.smartindiahackathon.ui.epubReader

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.OpenableColumns
import android.text.TextUtils
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.smartindiahackathon.R
import java.io.*

class FileChooser : AppCompatActivity() {
    var adapter: ArrayAdapter<String>? = null
    var firstTime = false
    private val BUFFER_SIZE = 1024 * 2
    private val IMAGE_DIRECTORY = "/demonuts_upload_gallery"
    //SQLiteDatabase db = null;
    //private static String DBNAME = "BOOKMARKS.db";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.file_chooser_layout)


        val selectPdfResult = registerForActivityResult(ActivityResultContracts.GetContent()) {
            it?.let {
                val I = Intent(this, EpubReaderMainActivity::class.java)
                I.putExtra("epub_location", it.toString())
                startActivity(I)
                finish()
            }


        }
        showFileChooser()
    }

        private fun showFileChooser() {
        val intent = Intent()
        intent.type = "application/epub+zip"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select EPUB"), 100)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            // Get the Uri of the selected file
            val uri = data!!.data
            val uriString = uri.toString()
            val myFile = File(uriString)
            val path: String = getFilePathFromURI(this, uri)
            Log.d("ioooo", path)
          //  pdfPath = path
            val I = Intent(this, EpubReaderMainActivity::class.java)
            I.putExtra("epub_location",path)
            startActivity(I)
            finish()
            //******************find the acutal path*************//
//            var displayName: String? = null
//            if (uriString.startsWith("content://")) {
//                var cursor: Cursor? = null
//                try {
//                    cursor = this.contentResolver.query(uri!!, null, null, null, null)
//                    if (cursor != null && cursor.moveToFirst()) {
//                        displayName =
//                            cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
//                        txtAddPdf.text = displayName
//                    }
//                } finally {
//                    cursor?.close()
//                }
//            } else if (uriString.startsWith("file://")) {
//                displayName = myFile.name
//                //filePath=displayName
//                txtAddPdf.text = displayName
//            }


        }

        super.onActivityResult(requestCode, resultCode, data)
    }

    fun getFilePathFromURI(context: Context, contentUri: Uri?): String {
        //copy file and send new file path
        val fileName = getFileName(contentUri)
        val wallpaperDirectory = File(
            applicationContext.filesDir.path + IMAGE_DIRECTORY
        )
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs()
        }
        if (!TextUtils.isEmpty(fileName)) {
            val copyFile = File(wallpaperDirectory.toString() + File.separator + fileName)
            // create folder if not exists
            copy(context, contentUri, copyFile)
            return copyFile.absolutePath
        }
        return ""
    }

    fun getFileName(uri: Uri?): String? {
        if (uri == null) return null
        var fileName: String? = null
        val path = uri.path
        val cut = path!!.lastIndexOf('/')
        if (cut != -1) {
            fileName = path.substring(cut + 1)
        }
        return fileName
    }

    fun copy(context: Context, srcUri: Uri?, dstFile: File?) {
        try {
            val inputStream = context.contentResolver.openInputStream(srcUri!!) ?: return
            val outputStream: OutputStream = FileOutputStream(dstFile)
            copystream(inputStream, outputStream)
            inputStream.close()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    @Throws(Exception::class, IOException::class)
    fun copystream(input: InputStream?, output: OutputStream?): Int {
        val buffer = ByteArray(BUFFER_SIZE)
        val `in` = BufferedInputStream(input, BUFFER_SIZE)
        val out = BufferedOutputStream(output, BUFFER_SIZE)
        var count = 0
        var n = 0
        try {
            while (`in`.read(buffer, 0, BUFFER_SIZE).also { n = it } != -1) {
                out.write(buffer, 0, n)
                count += n
            }
            out.flush()
        } finally {
            try {
                out.close()
            } catch (e: IOException) {
                Log.e(e.message, e.toString())
            }
            try {
                `in`.close()
            } catch (e: IOException) {
                Log.e(e.message, e.toString())
            }
        }
        return count
    }

    //Opens File Picker
    private fun pickFile(selectPdfResult: ActivityResultLauncher<String>) {
        try {
            selectPdfResult.launch("application/epub+zip")
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "No File Picker Found", Toast.LENGTH_SHORT).show()
            // Log.e(TAG, "pickFile: ${e.message}")
        }
    }

}




