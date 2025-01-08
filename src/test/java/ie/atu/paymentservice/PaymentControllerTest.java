package ie.atu.paymentservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PaymentControllerTest {

    @InjectMocks
    private PaymentController paymentController;

    @Mock
    private PaymentService paymentService;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(paymentController).build();
    }

    @Test
    void testCreatePayment() throws Exception {
        Payment payment = new Payment(null, 1L, 100.0, "PENDING");
        when(paymentService.createPayment(any())).thenReturn(payment);

        mockMvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payment)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rideId").value(1L))
                .andExpect(jsonPath("$.amount").value(100.0))
                .andExpect(jsonPath("$.status").value("PENDING"));

        verify(paymentService, times(1)).createPayment(any());
    }

    @Test
    void testGetAllPayments() throws Exception {
        when(paymentService.getAllPayments()).thenReturn(Arrays.asList(
                new Payment(1L, 1L, 100.0, "PENDING"),
                new Payment(2L, 2L, 200.0, "COMPLETED")
        ));

        mockMvc.perform(get("/api/payments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].status").value("COMPLETED"));

        verify(paymentService, times(1)).getAllPayments();
    }

    @Test
    void testDeletePayment() throws Exception {

        mockMvc.perform(delete("/api/payments/1"))
                .andExpect(status().isNoContent());

        verify(paymentService, times(1)).deletePayment(1L);
    }
}
