//package com.spring.core.error;
//
//import com.spring.core.dto.ErrorResponseDto;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.HttpStatusCode;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.FieldError;
//import org.springframework.validation.ObjectError;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.context.request.WebRequest;
//import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
//
//import java.security.NoSuchAlgorithmException;
//import java.time.LocalDateTime;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//
//@ControllerAdvice
//public class CustomerGlobalException extends ResponseEntityExceptionHandler {
////        @ExceptionHandler(Exception.class)
////        public ResponseEntity<ErrorResponseDto> globalExceptionHandler(Exception ex, WebRequest request){
////            ErrorResponseDto response= new ErrorResponseDto(
////                    request.getDescription(false),
////                    HttpStatus.INTERNAL_SERVER_ERROR,
////                    ex.getMessage(),
////                    LocalDateTime.now()
////            );
////            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
////        }
////    @ExceptionHandler(NoSuchAlgorithmException.class)
////    public ResponseEntity<ErrorResponseDto> handleNoSuchAlgorithm(NoSuchAlgorithmException ex, WebRequest request) {
////        ErrorResponseDto error = new ErrorResponseDto(
////                request.getDescription(false),
////                HttpStatus.INTERNAL_SERVER_ERROR,
////                ex.getMessage(),
////                LocalDateTime.now()
////        );
////        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
////    }
//
//        @ExceptionHandler(AlreadyExistException.class)
//        public ResponseEntity<ErrorResponseDto> alreadyExistException(AlreadyExistException ex, WebRequest request){
//            ErrorResponseDto response= new ErrorResponseDto(
//            request.getDescription(false),
//            HttpStatus.BAD_REQUEST,
//                    ex.getMessage(),
//                    LocalDateTime.now()
//            );
//            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
//        }
////        @ExceptionHandler(ResourceNotFoundException.class)
////        public ResponseEntity<ErrorResponseDto> resourceNotFound(ResourceNotFoundException ex, WebRequest request){
////            ErrorResponseDto response= new ErrorResponseDto(
////                    request.getDescription(false),
////                    HttpStatus.NOT_FOUND,
////                    ex.getMessage(),
////                    LocalDateTime.now()
////            );
////            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
////        }
//
////        @Override
////        @ExceptionHandler(MethodArgumentNotValidException.class)
////        protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
////            Map<String, String> validationError = new HashMap<>();
////            List<ObjectError> validationErrList = ex.getBindingResult().getAllErrors();
////
////            validationErrList.forEach(error ->{
////                String fieldName= ((FieldError)error).getField();
////                String validationMsg= error.getDefaultMessage();
////                validationError.put(fieldName,validationMsg);
////            });
////
////            return new ResponseEntity<>(validationError,HttpStatus.BAD_REQUEST);
////        }
////
//        @ExceptionHandler(IllegalArgumentException.class)
//        public ResponseEntity<ErrorResponseDto> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
//        	ErrorResponseDto errorResponse = new ErrorResponseDto(
//        			request.getDescription(false),
//                    HttpStatus.BAD_REQUEST,
//                    ex.getMessage(),
//                    LocalDateTime.now()
//            );
//
//            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
//        }
////
////        @ExceptionHandler(ErrorHandler.class)
////        public ResponseEntity<ErrorResponseDto> customError(ErrorHandler ex, WebRequest request){
////        	ErrorResponseDto response= new ErrorResponseDto(
////                    request.getDescription(false),
////                    HttpStatus.INTERNAL_SERVER_ERROR,
////                    ex.getMessage(),
////                    LocalDateTime.now()
////            );
////            return new ResponseEntity<>(response,HttpStatus.INTERNAL_SERVER_ERROR);
////        }
////    @ExceptionHandler(MethodArgumentNotValidException.class)
////    public ResponseEntity<Object> handleMethodArgumentNotValid(
////            MethodArgumentNotValidException ex) {
////
////        Map<String, String> errors = new HashMap<>();
////        ex.getBindingResult().getFieldErrors().forEach(error ->
////                errors.put(error.getField(), error.getDefaultMessage()));
////
////        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
////    }
//////
//        @ExceptionHandler(CustomErrorMessage.class)
//        public ResponseEntity<ErrorResponseDto> customErrorHandler(CustomErrorMessage ex, WebRequest request){
//        	ErrorResponseDto response= new ErrorResponseDto(
//                    request.getDescription(false),
//                    HttpStatus.BAD_REQUEST,
//                    ex.getMessage(),
//                    LocalDateTime.now()
//            );
//            return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
//        }
////
//////
//
//}
