package com.useruser.foodscanner.utils

import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFRow

fun creteCells(headerRow: XSSFRow, names: List<String>, cellStyle: XSSFCellStyle) {
    names.forEachIndexed { index, name ->
        val cell = headerRow.createCell(index)
        cell.setCellValue(name)
        cell.cellStyle = cellStyle

    }
}

