package SpringBootBE.BackEnd.controller;

import SpringBootBE.BackEnd.Service.PaymentService;
import SpringBootBE.BackEnd.dto.MomoCallbackResponse;
import SpringBootBE.BackEnd.dto.MomoPaymentRequest;
import SpringBootBE.BackEnd.dto.MomoPaymentResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment/momo")
@CrossOrigin
public class MomoPaymentController {

    private final PaymentService paymentService;

    public MomoPaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createPayment(@RequestBody MomoPaymentRequest request) {
        try {
            MomoPaymentResponse response = paymentService.createMomoPayment(request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.badRequest().body(errorBody(exception.getMessage()));
        } catch (IllegalStateException exception) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(errorBody(exception.getMessage()));
        }
    }

    @GetMapping("/callback")
    public ResponseEntity<MomoCallbackResponse> handleReturn(@RequestParam Map<String, String> callbackData) {
        return ResponseEntity.ok(paymentService.handleMomoReturn(callbackData));
    }

    @PostMapping({"/ipn", "/callback"})
    public ResponseEntity<Map<String, Object>> handleIpn(@RequestBody Map<String, Object> callbackData) {
        return ResponseEntity.ok(paymentService.handleMomoIpn(toStringMap(callbackData)));
    }

    private Map<String, Object> errorBody(String message) {
        LinkedHashMap<String, Object> body = new LinkedHashMap<>();
        body.put("message", message);
        body.put("success", false);
        return body;
    }

    private Map<String, String> toStringMap(Map<String, Object> body) {
        LinkedHashMap<String, String> values = new LinkedHashMap<>();
        if (body != null) {
            body.forEach((key, value) -> values.put(key, value == null ? "" : String.valueOf(value)));
        }
        return values;
    }
}

