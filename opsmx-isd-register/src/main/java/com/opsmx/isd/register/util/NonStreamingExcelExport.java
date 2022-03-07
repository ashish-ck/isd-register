package com.opsmx.isd.register.util;

import com.opsmx.isd.register.entities.User;

import java.util.List;

public class NonStreamingExcelExport extends ExcelExport implements DownloadableFile {

    public NonStreamingExcelExport(int columns, int rows, List<User> userList) {
        super(false);
        build(columns, rows, userList);
    }

    private void build(int columns, int rows, List<User> userList) {
        addDataToWorkbook(getXSSFWorkbook(), columns, rows, userList);
        addRichTextCell(getXSSFWorkbook());
    }

    @Override
    public String getFilename() {
        return "Export.xlsx";
    }
}