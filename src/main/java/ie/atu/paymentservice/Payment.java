package ie.atu.paymentservice;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Ride ID cannot be null")
    private Long rideId;

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be greater than zero")
    private Double amount;

    @NotBlank(message = "Status is required")
    @Pattern(regexp = "PENDING|COMPLETED|FAILED", message = "Status must be PENDING, COMPLETED, or FAILED")
    private String status;
}
