package com.useruser.foodscanner.viewmodel

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Environment
import android.util.Log.d
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import com.google.zxing.Result
import com.useruser.foodscanner.R
import com.useruser.foodscanner.adapters.ProductListAdapter
import com.useruser.foodscanner.data.models.*
import com.useruser.foodscanner.utils.*
import com.useruser.foodscanner.view.QrScannerActivity
import kotlinx.coroutines.*
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class QrScannerViewModel(appContext: Application) : AndroidViewModel(appContext) {

    private val _activityCallbacks: MutableLiveData<Command> = MutableLiveData()
    val activityCallbacks: LiveData<Command> = _activityCallbacks

    val productAdapter = ProductListAdapter(mutableListOf())
    val productListVisible = MutableLiveData<Int>()
    val countLayoutVisible = MutableLiveData<Int>()

    private var countView: View? = null

    val productList = mutableListOf<ProductModel>()
    val product = MutableLiveData<ProductModel?>()

    init {
        productAdapter.itemDeleteCallback = { productModel, position -> deleteProduct(productModel, position) }
        productListVisible.value = View.INVISIBLE
        countLayoutVisible.value = View.VISIBLE
    }

    fun backArrowClick(view: View) {
        _activityCallbacks.value = Command(COMMAND_BACK_PRESSED)
    }

    fun countClick(view: View) {
        if (changeSelectedCountViewColor(countView as CardView?, view as CardView?)) {
            countView = view
        } else {
            countView = null
        }
    }

    fun submitResultClick(view: View) {
        product.value?.let {
            _activityCallbacks.value = Command(COMMAND_RESTART_QR_SCANNER)
            if (isProductCountSelected(countView as CardView?)) {
                it.count = countView?.let { getCount(it) }!!
            }
            productList.add(0, it)
            productListChanged()
            showListLayout()
            if (calculateAllPrice(productList) > 0) {
                product.value = ProductModel("", calculateAllPrice(productList), "dr", 1)
            } else {
                product.value = null
            }
        }

    }

    private fun deleteProduct(productModel: ProductModel, position: Int) {
        productList.remove(productModel)
        productListChanged()

        if (calculateAllPrice(productList) > 0) {
            product.value = ProductModel("", calculateAllPrice(productList), "dr", 0)
        } else {
            product.value = null
        }
    }

    fun doneClick(view: View) {
        val activity = view.context as QrScannerActivity
        if (!activity.permissionChecked(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            activity.requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_CODE_WRITE_EXTERNAL_STORAGE)
            return
        }

        GlobalScope.launch {
            val path = async { writeInFile(productList) }.await()
            launch(Dispatchers.Main) {
                Toast.makeText(getApplication(), "File downloaded" + "\n${path}", Toast.LENGTH_SHORT).show()
                _activityCallbacks.value = Command(COMMAND_BACK_PRESSED)
            }

        }

    }

    fun handleQrResult(context: Context, result: Result?) {
        productListVisible.value = View.INVISIBLE
        countView?.let { changeCardSelectedColor(it as CardView, R.color.color_btn_scanning) }

        val qrText = result?.text
        if (isProductQR(qrText!!)) {
            product.value = parsingProductQr(qrText)
            showCountLayout()
        } else {
            _activityCallbacks.value = Command(COMMAND_RESTART_QR_SCANNER)
            resetValues()
            Toast.makeText(context, context.getString(R.string.qr_is_not_product), Toast.LENGTH_LONG).show()
        }

    }

    private fun productListChanged() {
        val productDiffUtilCallback = ProductDiffUtilCallback(productAdapter.products, productList)
        val productDiffResult = DiffUtil.calculateDiff(productDiffUtilCallback)
        productAdapter.changeProductData(productList)
        productDiffResult.dispatchUpdatesTo(productAdapter)
    }

    private fun getCount(countView: View): Int {
        val productCount = when (countView.id) {
            R.id.cardView1 -> 1
            R.id.cardView2 -> 2
            R.id.cardView3 -> 3
            R.id.cardView4 -> 4
            R.id.cardView5 -> 5
            R.id.cardView6 -> 6
            R.id.cardView7 -> 7
            R.id.cardView8 -> 8
            R.id.cardView9 -> 9
            R.id.cardView10 -> 10
            else -> 1
        }

        return productCount
    }

    fun isProductCountSelected(cardView: CardView?): Boolean {
        cardView?.apply {
            when (cardBackgroundColor.defaultColor) {
                Color.WHITE -> return false
                else -> return true
            }
        }

        return false
    }

    private fun changeSelectedCountViewColor(oldView: CardView?, newView: CardView?): Boolean {
        return if (oldView == newView) {
            newView?.let { changeCardSelectedColor(it, R.color.color_btn_scanning) }
            false
        } else {

            oldView?.let { changeCardSelectedColor(it, R.color.color_btn_scanning) }
            newView?.let { changeCardSelectedColor(it, Color.WHITE) }
            true
        }

    }

    private fun changeCardSelectedColor(cardView: CardView, color: Int) {
        when (color) {
            Color.WHITE -> {
                cardView.apply {
                    setCardBackgroundColor(context.getColor(R.color.color_btn_scanning))
                    when (getCount(this)) {
                        1 -> (findViewById<TextView>(R.id.textView1)).setTextColor(Color.WHITE)
                        2 -> (findViewById<TextView>(R.id.textView2)).setTextColor(Color.WHITE)
                        3 -> (findViewById<TextView>(R.id.textView3)).setTextColor(Color.WHITE)
                        4 -> (findViewById<TextView>(R.id.textView4)).setTextColor(Color.WHITE)
                        5 -> (findViewById<TextView>(R.id.textView5)).setTextColor(Color.WHITE)
                        6 -> (findViewById<TextView>(R.id.textView6)).setTextColor(Color.WHITE)
                        7 -> (findViewById<TextView>(R.id.textView7)).setTextColor(Color.WHITE)
                        8 -> (findViewById<TextView>(R.id.textView8)).setTextColor(Color.WHITE)
                        9 -> (findViewById<TextView>(R.id.textView9)).setTextColor(Color.WHITE)
                        10 -> (findViewById<TextView>(R.id.textView10)).setTextColor(Color.WHITE)
                    }
                }

            }

            R.color.color_btn_scanning -> {
                cardView.apply {
                    setCardBackgroundColor(Color.WHITE)
                    when (getCount(this)) {
                        1 -> (findViewById<TextView>(R.id.textView1)).setTextColor(context.getColor(R.color.color_btn_scanning))
                        2 -> (findViewById<TextView>(R.id.textView2)).setTextColor(context.getColor(R.color.color_btn_scanning))
                        3 -> (findViewById<TextView>(R.id.textView3)).setTextColor(context.getColor(R.color.color_btn_scanning))
                        4 -> (findViewById<TextView>(R.id.textView4)).setTextColor(context.getColor(R.color.color_btn_scanning))
                        5 -> (findViewById<TextView>(R.id.textView5)).setTextColor(context.getColor(R.color.color_btn_scanning))
                        6 -> (findViewById<TextView>(R.id.textView6)).setTextColor(context.getColor(R.color.color_btn_scanning))
                        7 -> (findViewById<TextView>(R.id.textView7)).setTextColor(context.getColor(R.color.color_btn_scanning))
                        8 -> (findViewById<TextView>(R.id.textView8)).setTextColor(context.getColor(R.color.color_btn_scanning))
                        9 -> (findViewById<TextView>(R.id.textView9)).setTextColor(context.getColor(R.color.color_btn_scanning))
                        10 -> (findViewById<TextView>(R.id.textView10)).setTextColor(context.getColor(R.color.color_btn_scanning))
                    }
                }
            }
        }

    }

    private fun calculateAllPrice(products: List<ProductModel>): Long {
        var price = 0L
        products.forEach { price += (it.price * it.count) }
        return price
    }

    private fun resetValues() {
        showCountLayout()
        countView?.let {
            changeCardSelectedColor(it as CardView, R.color.color_btn_scanning)
        }
        countView = null
        product.value = null
    }

    private fun showCountLayout() {
        productListVisible.value = View.INVISIBLE
        countLayoutVisible.value = View.VISIBLE
        countView = null
    }

    private fun showListLayout() {
        productListVisible.value = View.VISIBLE
        countLayoutVisible.value = View.INVISIBLE
    }

    suspend fun writeInFile(products: MutableList<ProductModel>): String = withContext(Dispatchers.IO) {
        val fileName = "${getDate("dd.MM.yyyy")}.xlsx"
        val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
        val file = File(dir, fileName)

        if (file.exists()) {
            val fileInputStream = FileInputStream(file)
            val workBook = XSSFWorkbook(fileInputStream)
            val sheet = workBook.getSheet("Products")
            val headerFont = workBook.createFont()
            headerFont.bold = true
            headerFont.fontHeightInPoints = 14
            headerFont.color = IndexedColors.RED.index

            val headerCellStyle = workBook.createCellStyle()
            headerCellStyle.setFont(headerFont)

            val dateFormat = "dd-MM-yyyy"
            val timeFormat = "HH:mm:ss"
            var productsPosition = 0
            val rowIterator = sheet.iterator()
            while (rowIterator.hasNext()) {
                if (rowIterator.next().zeroHeight) {
                    break
                }
                productsPosition++

            }
            d("QrScannerViewModel", "---------------------------------------${productsPosition}-------------------")

            products.forEachIndexed { index, product ->
                val row = sheet.createRow(index + productsPosition)
                row.createCell(0).setCellValue(product.name)
                row.createCell(1).setCellValue("${product.count}")
                row.createCell(2).setCellValue("${product.price} ${product.priceType}")
                row.createCell(3).setCellValue(getDate(dateFormat, product.date))
                row.createCell(4).setCellValue(getDate(timeFormat, product.date))
                row.createCell(5).setCellValue("${product.price * product.count}  ${product.priceType}")
            }


            fileInputStream.close()
            val fileOutputStream = FileOutputStream(file)
            workBook.write(fileOutputStream)
            fileOutputStream.close()

        } else {
            val workBook = XSSFWorkbook()
            val sheet = workBook.createSheet("Products")
            val headerFont = workBook.createFont()
            headerFont.bold = true
            headerFont.fontHeightInPoints = 14
            headerFont.color = IndexedColors.RED.index

            val headerCellStyle = workBook.createCellStyle()
            headerCellStyle.setFont(headerFont)

            val dateFormat = "dd-MM-yyyy"
            val timeFormat = "HH:mm:ss"
            val productsPosition = 1

            val headerRow = sheet.createRow(0)
            creteCells(headerRow, listOf("NAME", "QUNTITY", "PRICE", "DATE", "TIME", "TOTAL"), headerCellStyle)

            products.forEachIndexed { index, product ->
                val row = sheet.createRow(index + productsPosition)
                row.createCell(0).setCellValue(product.name)
                row.createCell(1).setCellValue("${product.count}")
                row.createCell(2).setCellValue("${product.price} ${product.priceType}")
                row.createCell(3).setCellValue(getDate(dateFormat, product.date))
                row.createCell(4).setCellValue(getDate(timeFormat, product.date))
                row.createCell(5).setCellValue("${product.price * product.count}  ${product.priceType}")
            }

            val fileOutputStream = FileOutputStream(file)
            workBook.write(fileOutputStream)
            fileOutputStream.close()

        }

        val contentUri = Uri.fromFile(file)
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        mediaScanIntent.data = contentUri
        getApplication<Application>().sendBroadcast(mediaScanIntent)

        return@withContext file.absolutePath
    }

}