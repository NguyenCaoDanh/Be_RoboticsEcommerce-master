package com.wisdom.roboticsecommerce.Utils;

import com.wisdom.roboticsecommerce.Model.ResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

public class ResponseUtil {
    public static ResponseEntity<?> success(Object data) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.setStatus("success");
        responseModel.setTimestamp(LocalDate.now().toString());
        responseModel.setMessage(data);

        return ResponseEntity.status(HttpStatus.OK).body(responseModel);
    }

    public static ResponseEntity<?> fail(String data) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.setStatus("fail");
        responseModel.setTimestamp(LocalDate.now().toString());
        responseModel.setMessage(data);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseModel);
    }

    public static ResponseEntity<?> error() {
        ResponseModel responseModel = new ResponseModel();
        responseModel.setStatus("error");
        responseModel.setTimestamp(LocalDate.now().toString());
        responseModel.setMessage("Internal Server Error");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseModel);
    }
}
