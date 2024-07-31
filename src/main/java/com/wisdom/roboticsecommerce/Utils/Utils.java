package com.wisdom.roboticsecommerce.Utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.wisdom.roboticsecommerce.ExceptionHandler.CustomException;
import lombok.SneakyThrows;

import org.hibernate.validator.internal.engine.messageinterpolation.parser.MessageDescriptorFormatException;

import org.modelmapper.ModelMapper;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static final Path CURRENT_FOLDER = Paths.get(System.getProperty("user.dir"));
    private static final String PHONE_NUMBER_REGEX = "^\\d{10}$";
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    public static void isValid(MultipartFile image){
        String[] o = {".png",".JPEG","JPG","jpg"};
        boolean isvali = false;
        for (String img : o){
            if (image.getOriginalFilename().endsWith(img)){
                isvali = true;
            }
        }
        if (!isvali){
            throw new MessageDescriptorFormatException("Định dạng ảnh không đúng");
        }
    }
    public static void validatePhoneNumber(String phoneNumber) {
        Pattern pattern = Pattern.compile(PHONE_NUMBER_REGEX);
        Matcher matcher = pattern.matcher(phoneNumber);
        if (!matcher.matches()){
            throw new RuntimeException("Số điện thoại không đúng định dạng");
        }
        matcher.matches();
    }

    public static void validateEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()){
            throw new RuntimeException("Email không hợp lệ");
        }
        matcher.matches();
    }
    public static void checkSize(MultipartFile file){
        long maxSize =  500 * 1024; // Giới hạn dung lượng tối đa (500kb)
        long fileSize = file.getSize();
        if (fileSize > maxSize) {
            throw new MessageDescriptorFormatException("Dung lượng ảnh vượt quá giới hạn.");
        }
    }

    @SneakyThrows
    public static String imageName(MultipartFile image){
        Path staticPath = Paths.get("static");
        Path imagePath = Paths.get("images");
        if(!Files.exists(Utils.CURRENT_FOLDER.resolve(staticPath).resolve(imagePath))){
            Files.createDirectories(Utils.CURRENT_FOLDER.resolve(staticPath).resolve(imagePath));
        }
        Path file = Utils.CURRENT_FOLDER.resolve(staticPath).resolve(imagePath).resolve(Objects.requireNonNull(image.getOriginalFilename()));
        try(OutputStream os = Files.newOutputStream(file)) {
            os.write(image.getBytes());
        }
        String req = "http://localhost:8081/" + imagePath.resolve(image.getOriginalFilename());
        return req.replace("\\","/");
    }

    public static ModelMapper modelMapper(Boolean skipNull, Boolean skipStringEmpty) {
        ModelMapper modelMapperInit = new ModelMapper();
        modelMapperInit.getConfiguration().setSkipNullEnabled(skipNull);
        if (skipStringEmpty) {
            modelMapperInit.getConfiguration().setPropertyCondition(context ->
                    !(context.getSource() instanceof String) || !((String) context.getSource()).isEmpty());
        }

        return modelMapperInit;
    }

    public static JsonNode convertToJson (Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.convertValue(object, JsonNode.class);
            return jsonNode;
        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
    }

    public static JsonNode jsonNodeFindByField(JsonNode sourceJsonNode, String fieldName, String fieldValue) {
        if (sourceJsonNode.isArray()) {
            ArrayNode arrayNode = (ArrayNode) sourceJsonNode;

            for (JsonNode node : arrayNode) {
                if (node.has(fieldName) && node.get(fieldName).asText().equals(fieldValue)) {
                    return node;
                }
            }

            System.out.println("Không tìm thấy entity có giá trị mong muốn.");
        } else {
            System.out.println("entities không phải là ArrayNode.");
        }

        return null;
    }
}
