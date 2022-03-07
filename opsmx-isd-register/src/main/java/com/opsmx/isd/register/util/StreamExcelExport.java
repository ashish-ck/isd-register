package com.opsmx.isd.register.util;

import com.opsmx.isd.register.entities.User;

import java.util.List;

public class StreamExcelExport extends ExcelExport {

    public StreamExcelExport(int columns, int rows, List<User> userList) {
        super(true);
        build(columns, rows, userList);
    }

    private void build(int columns, int rows, List<User> userList) {
        addDataToWorkbook(getSXSSFWorkbook(), columns, rows, userList);
        addRichTextCell(getSXSSFWorkbook());
    }

    @Override
    public String getFilename() {
        return "Export.xlsx";
    }
}
