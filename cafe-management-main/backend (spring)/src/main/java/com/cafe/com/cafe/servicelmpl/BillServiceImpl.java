package com.cafe.com.cafe.servicelmpl;

import com.cafe.com.cafe.JWT.JwtFilter;
import com.cafe.com.cafe.constants.CafeConstants;
import com.cafe.com.cafe.dao.BillDao;
import com.cafe.com.cafe.modal.Bill;
import com.cafe.com.cafe.modal.Category;
import com.cafe.com.cafe.service.BillService;
import com.cafe.com.cafe.utils.CafeUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.io.IOUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Slf4j
@Service
public class BillServiceImpl implements BillService {

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    BillDao billDao;

    @Override
    public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
        log.info("Inside generateReport");
        try {
            String fileName; // uuid
            System.out.println("line 47");
            log.info("line 47");
            if (validateRequestMap(requestMap)) {
                log.info("line 48");
                // retrieves a certain bill stored in the database
                if (requestMap.containsKey("isGenerate") && !(Boolean) requestMap.get("isGenerate")) {
                    log.info("line 50");
                    fileName = (String) requestMap.get("uuid");
                }
                // creates a new bill
                else {
                    log.info("line 55");
                    fileName = CafeUtils.getUUID();
                    requestMap.put("uuid", fileName);
                    insertBill(requestMap);
                }

                String data = "Name: " + requestMap.get("name") + "\n" + "Contact Number:" + requestMap.get("contactNumber")
                        + "\n" + "Email:" + requestMap.get("email") + "\n" + "Payment Method:" + requestMap.get("paymentMethod");

                // create pdf document using itextpdf
                Document document = new Document();
                System.out.println("lines 69");
                PdfWriter.getInstance(document, new FileOutputStream(CafeConstants.STORE_LOCATION+"/"+fileName+".pdf")); // path to the folder storing the bills

                document.open(); // open pdf document for writing
                setRectangleInPdf(document);

                Paragraph chunk = new Paragraph(" Cafe CAFFEE", getFont("Header"));
                chunk.setAlignment(Element.ALIGN_CENTER);
                document.add(chunk);

                Paragraph paragraph = new Paragraph(data + "\n \n", getFont("Data"));
                document.add(paragraph);

                PdfPTable table = new PdfPTable(5); // table for the purchased items
                table.setWidthPercentage(100);
                addTableHeader(table);


                JSONArray jsonArray = CafeUtils.getJsonArrayFromString((String) requestMap.get("productDetails"));
                // for every value in the json array, add it to the table
                for (int i = 0; i < jsonArray.length(); i++) {
                    addRows(table, CafeUtils.getMapFromJson(jsonArray.getString(i)));
                }
                document.add(table);

                Paragraph footer = new Paragraph("\nTotal : " + requestMap.get("totalAmount") + "\n"
                + "Thank you for visiting  Cafe!", getFont("Data"));
                document.add(footer);

                document.close();
                return new ResponseEntity<>("{\"uuid\":\""+fileName+"\"}", HttpStatus.OK);
            }
            return CafeUtils.getResponseEntity(CafeConstants.MISSING_REQUIRED_DATA, HttpStatus.BAD_REQUEST);

        } catch (Exception ex) {
            log.info("line 102");
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // fills the table from data in the json array
    private void addRows(PdfPTable table, Map<String, Object> data) {
        log.info("Inside addRows");
        table.addCell((String) data.get("name"));
        table.addCell((String) data.get("category"));
        table.addCell((String) data.get("quantity"));
        table.addCell(Double.toString((Double) data.get("price")));
        table.addCell(Double.toString((Double) data.get("total")));
    }

    // generates each column's header
    private void addTableHeader(PdfPTable table) {
        log.info("Inside addTableHeader");
        Stream.of("Name", "Category", "Quantity", "Price", "Subtotal")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(1);
                    header.setPhrase(new Phrase(columnTitle));
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setHorizontalAlignment(Element.ALIGN_CENTER);
                    header.setVerticalAlignment(Element.ALIGN_CENTER);
                    table.addCell(header);
                });
    }

    private Font getFont(String type) {
        log.info("Inside getFont");
        switch (type) {
            case "Header":
                Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 18, BaseColor.BLACK);
                headerFont.setStyle(Font.BOLD);
                return headerFont;
            case "Data":
                Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 11, BaseColor.BLACK);
                dataFont.setStyle(Font.BOLD);
                return dataFont;
            default:
                return new Font();
        }
    }

    // creates the margin around the pdf document
    private void setRectangleInPdf(Document document) throws DocumentException {
        log.info("Inside setRectangleInPdf");
        Rectangle rectangle = new Rectangle(577, 825, 18, 15);
        rectangle.enableBorderSide(1);
        rectangle.enableBorderSide(2);
        rectangle.enableBorderSide(4);
        rectangle.enableBorderSide(8);
        rectangle.setBorderColor(BaseColor.BLACK);
        rectangle.setBorderWidth(1);
        document.add(rectangle);
    }

    // creates a new bill
    private void insertBill(Map<String, Object> requestMap) {
        try {
            Bill bill = new Bill();
            bill.setUuid((String) requestMap.get("uuid"));
            bill.setName((String) requestMap.get("name"));
            bill.setEmail((String) requestMap.get("email"));
            bill.setContactNumber((String) requestMap.get("contactNumber"));
            bill.setPaymentMethod((String) requestMap.get("paymentMethod"));
            bill.setTotal(Integer.parseInt((String) requestMap.get("totalAmount")));
            bill.setProductDetail((String) requestMap.get("productDetails"));
            bill.setCreatedBy(jwtFilter.getCurrentUser());
            billDao.save(bill); // save to db
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // validates the JSON input, must contain all columns in the bill database
    private boolean validateRequestMap(Map<String, Object> requestMap) {
        log.info("request map",requestMap);
        return requestMap.containsKey("name") &&
                requestMap.containsKey("contactNumber") &&
                requestMap.containsKey("email") &&
                requestMap.containsKey("paymentMethod") &&
                requestMap.containsKey("productDetails") &&
                requestMap.containsKey("totalAmount");

    }

    @Override
    public ResponseEntity<List<Bill>> getBills() {
        try {
            List<Bill> list = new ArrayList<>();

            if(jwtFilter.isAdmin()) {
                list = billDao.getAllBills(); // admins can access all bills
            }
            else {
                list = billDao.getBillByUserName(jwtFilter.getCurrentUser()); // users can only access the bills they created
            }
            return new ResponseEntity<>(list, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<List<Bill>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap) {
        log.info("Inside getPdf : requestMap {}", requestMap);
        try {
            byte[] byteArray = new byte[0]; // array of binary data which will be converted into ascii data in the ui

            // no specified uuid --> cannot retrieve
            if (!requestMap.containsKey("uuid") && validateRequestMap(requestMap)) {
                return  new ResponseEntity<>(byteArray, HttpStatus.BAD_REQUEST);
            }

            // get the file path from the specified uid
            String filePath = CafeConstants.STORE_LOCATION+"/"+(String) requestMap.get("uuid")+".pdf";

            if (CafeUtils.isFileExist(filePath)) {
                byteArray = getByteArray(filePath);
                return new ResponseEntity<>(byteArray, HttpStatus.OK);
            }
            else {
                // if the file path doesn't exist, generate a new bill for it
                requestMap.put("isGenerate", false);
                generateReport(requestMap);
                byteArray = getByteArray(filePath);
                return new ResponseEntity<>(byteArray, HttpStatus.OK);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // converts text in the pdf into a byte array
    private byte[] getByteArray(String filePath) throws Exception {
        File initialFile = new File(filePath);
        InputStream targetStream = new FileInputStream(initialFile);
        byte[] byteArray = IOUtils.toByteArray(targetStream); // writes each line as a byte array
        targetStream.close();
        return byteArray;
    }

    @Override
    public ResponseEntity<String> deleteBill(Integer id) {
        try {
            // only admins can delete bills
            if (jwtFilter.isAdmin()) {
                Optional optional = billDao.findById(id);
                if (!optional.isEmpty()) {
                    billDao.deleteById(id); // delete bill with the specified id
                    return CafeUtils.getResponseEntity(CafeConstants.BILL_DELETED, HttpStatus.OK);
                }
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_BILL, HttpStatus.OK);
            }
            return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
