//package com.opsmx.isd.register.controller;
//
//import com.opsmx.isd.register.entities.User;
//import com.opsmx.isd.register.repositories.UserRepository;
//import com.opsmx.isd.register.util.NonStreamingExcelExport;
//import com.opsmx.isd.register.util.StreamExcelExport;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.servlet.http.HttpServletResponse;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.Iterator;
//import java.util.List;
//
//@RestController
//@RequestMapping("/export")
//public class ExcelController {
//    private static final Logger logger = LoggerFactory.getLogger(ExcelController.class);
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @GetMapping("non-streaming-excel")
//    public ResponseEntity<byte[]> nonStreamingExcel(@RequestParam int columns, @RequestParam int rows,
//                                                    HttpServletResponse response) throws IOException {
//        logger.info("columns->" + columns + " rows->" + rows);
//
//        Long startTime = System.currentTimeMillis();
//        logger.info("start of Non Streaming Excel request");
//
//        NonStreamingExcelExport nonStreamingExcelExport = new NonStreamingExcelExport(columns, rows, new ArrayList<>());
//
//        Long endTime = System.currentTimeMillis();
//        logger.info("end of Non Streaming Excel request->" + (endTime - startTime) / 1000 + " seconds.");
//
//        return nonStreamingExcelExport.getResponseEntity();
//    }
//
//    @GetMapping("spreadsheet")
//    public ResponseEntity<byte[]> excel(@RequestParam("start") @DateTimeFormat(pattern = "dd-MM-yyyy")
//                                                    Date start, @RequestParam("end")
//    @DateTimeFormat(pattern = "dd-MM-yyyy") Date end, HttpServletResponse response) throws IOException {
//        Iterable<User> userList1 = userRepository.findAllByCreatedAtBetween(start, end);
//        Iterator<User> iterator = userList1.iterator();
//        List<User> userList = new ArrayList<>();
//        while (iterator.hasNext()) {
//            userList.add(iterator.next());
//        }
//        NonStreamingExcelExport nonStreamingExcelExport = new NonStreamingExcelExport(8, userList.size(),
//                userList);
//        Long endSheetTime = System.currentTimeMillis();
//        String attachmentName = endSheetTime + nonStreamingExcelExport.getFilename();
//        File tempFile = File.createTempFile("Spreadsheet", attachmentName, null);
//        FileOutputStream fos = new FileOutputStream(tempFile);
//        fos.write(nonStreamingExcelExport.getBytes());
//        String subject = "Spreadsheet";
//        String text = "Spreadsheet";
//        Long startTime = System.currentTimeMillis();
//        logger.info("start of Non Streaming Excel request");
//        Long endTime = System.currentTimeMillis();
//        logger.info("end of Non Streaming Excel request->" + (endTime - startTime) / 1000 + " seconds.");
//        return nonStreamingExcelExport.getResponseEntity();
//    }
//
//    @GetMapping("streaming-excel")
//    public ResponseEntity<byte[]> streamingExcel(@RequestParam int columns, @RequestParam int rows,
//                                                 HttpServletResponse response) throws IOException {
//        logger.info("columns->" + columns + " rows->" + rows);
//
//        Long startTime = System.currentTimeMillis();
//        logger.info("start of Streaming Excel request");
//
//        StreamExcelExport streamExcelExport = new StreamExcelExport(columns, rows, new ArrayList<>());
//
//        Long endTime = System.currentTimeMillis();
//        logger.info("end of Streaming Excel request->" + (endTime - startTime) / 1000 + " seconds.");
//        return streamExcelExport.getResponseEntity();
//    }
//}
