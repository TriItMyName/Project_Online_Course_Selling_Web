package SpringBootBE.BackEnd.controller;

import SpringBootBE.BackEnd.Service.PaymentService;
import SpringBootBE.BackEnd.dto.MomoCallbackResponse;
import SpringBootBE.BackEnd.dto.MomoPaymentRequest;
import SpringBootBE.BackEnd.dto.MomoPaymentResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.argThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MomoPaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private MomoPaymentController momoPaymentController;

    @Test
    void createPayment_WhenValidRequest_ReturnsOk() {
        MomoPaymentRequest request = new MomoPaymentRequest();
        request.setUserId(1);
        request.setCourseIds(List.of(2, 3));

        MomoPaymentResponse expected = new MomoPaymentResponse();
        expected.setMessage("Khởi tạo thanh toán thành công.");
        expected.setResultCode(0);
        when(paymentService.createMomoPayment(request)).thenReturn(expected);

        ResponseEntity<?> response = momoPaymentController.createPayment(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
        verify(paymentService).createMomoPayment(request);
    }

    @Test
    void createPayment_WhenInvalidRequest_ReturnsBadRequestErrorBody() {
        MomoPaymentRequest request = new MomoPaymentRequest();
        when(paymentService.createMomoPayment(request)).thenThrow(new IllegalArgumentException("Thiếu userId."));

        ResponseEntity<?> response = momoPaymentController.createPayment(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<?, ?> body = assertInstanceOf(Map.class, response.getBody());
        assertEquals("Thiếu userId.", body.get("message"));
        assertFalse((Boolean) body.get("success"));
    }

    @Test
    void createPayment_WhenGatewayError_ReturnsBadGatewayErrorBody() {
        MomoPaymentRequest request = new MomoPaymentRequest();
        when(paymentService.createMomoPayment(request)).thenThrow(new IllegalStateException("MoMo API lỗi."));

        ResponseEntity<?> response = momoPaymentController.createPayment(request);

        assertEquals(HttpStatus.BAD_GATEWAY, response.getStatusCode());
        Map<?, ?> body = assertInstanceOf(Map.class, response.getBody());
        assertEquals("MoMo API lỗi.", body.get("message"));
        assertFalse((Boolean) body.get("success"));
    }

    @Test
    void handleReturn_DelegatesToService() {
        Map<String, String> callbackData = Map.of("orderId", "MOMO_1_ABC", "requestId", "req-1");
        MomoCallbackResponse expected = new MomoCallbackResponse();
        expected.setStatus("SUCCESS");
        when(paymentService.handleMomoReturn(callbackData)).thenReturn(expected);

        ResponseEntity<MomoCallbackResponse> response = momoPaymentController.handleReturn(callbackData);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expected, response.getBody());
        verify(paymentService).handleMomoReturn(callbackData);
    }

    @Test
    void handleIpn_ConvertsBodyValuesToStringBeforeDelegating() {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("orderId", "MOMO_1_ABC");
        body.put("resultCode", 0);
        body.put("transId", 123456789L);
        body.put("message", null);

        Map<String, Object> ipnResponse = Map.of("resultCode", 0, "message", "OK");
        when(paymentService.handleMomoIpn(org.mockito.ArgumentMatchers.anyMap())).thenReturn(ipnResponse);

        ResponseEntity<Map<String, Object>> response = momoPaymentController.handleIpn(body);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ipnResponse, response.getBody());

        verify(paymentService).handleMomoIpn(argThat(delegatedData -> {
            assertNotNull(delegatedData);
            return "MOMO_1_ABC".equals(delegatedData.get("orderId"))
                    && "0".equals(delegatedData.get("resultCode"))
                    && "123456789".equals(delegatedData.get("transId"))
                    && "".equals(delegatedData.get("message"));
        }));
    }
}
