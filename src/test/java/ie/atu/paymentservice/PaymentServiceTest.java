package ie.atu.paymentservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private PaymentRepository paymentRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePayment() {
        Payment payment = new Payment(null, 1L, 100.0, "PENDING");
        when(paymentRepository.save(payment)).thenReturn(payment);

        Payment createdPayment = paymentService.createPayment(payment);

        assertNotNull(createdPayment);
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void testGetPaymentById() {
        Payment payment = new Payment(1L, 1L, 100.0, "COMPLETED");
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));

        Payment result = paymentService.getPaymentById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(paymentRepository, times(1)).findById(1L);
    }

    @Test
    void testGetPaymentById_NotFound() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            paymentService.getPaymentById(1L);
        });

        assertEquals("Payment not found with ID: 1", exception.getMessage());
    }

    @Test
    void testGetAllPayments() {
        List<Payment> payments = Arrays.asList(
                new Payment(1L, 1L, 100.0, "PENDING"),
                new Payment(2L, 2L, 200.0, "COMPLETED")
        );
        when(paymentRepository.findAll()).thenReturn(payments);

        List<Payment> result = paymentService.getAllPayments();

        assertEquals(2, result.size());
        verify(paymentRepository, times(1)).findAll();
    }

    @Test
    void testUpdatePayment() {
        Payment existingPayment = new Payment(1L, 1L, 100.0, "PENDING");
        Payment updatedDetails = new Payment(null, 1L, 150.0, "COMPLETED");

        when(paymentRepository.findById(1L)).thenReturn(Optional.of(existingPayment));
        when(paymentRepository.save(existingPayment)).thenReturn(existingPayment);

        Payment result = paymentService.updatePayment(1L, updatedDetails);

        assertEquals(150.0, result.getAmount());
        assertEquals("COMPLETED", result.getStatus());
    }

    @Test
    void testDeletePayment() {
        Payment payment = new Payment(1L, 1L, 100.0, "PENDING");
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(payment));

        paymentService.deletePayment(1L);

        verify(paymentRepository, times(1)).delete(payment);
    }
}
