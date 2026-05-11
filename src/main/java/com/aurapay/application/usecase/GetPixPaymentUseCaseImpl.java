package com.aurapay.application.usecase;

import com.aurapay.application.dto.PixPaymentResponse;
import com.aurapay.common.exception.BusinessException;
import com.aurapay.domain.model.PixPayment;
import com.aurapay.domain.port.in.GetPixPaymentUseCase;
import com.aurapay.domain.port.out.PixPaymentRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class GetPixPaymentUseCaseImpl implements GetPixPaymentUseCase {

    private final PixPaymentRepositoryPort pixPaymentRepository;

    public GetPixPaymentUseCaseImpl(PixPaymentRepositoryPort pixPaymentRepository) {
        this.pixPaymentRepository = pixPaymentRepository;
    }

    @Override
    public PixPaymentResponse execute(Long id) {
        if (id == null) {
            throw new BusinessException("id é obrigatório");
        }

        PixPayment payment = pixPaymentRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Pagamento Pix não encontrado: " + id));

        return new PixPaymentResponse(
                payment.getId(),
                payment.getCustomerId(),
                payment.getWalletId(),
                payment.getAmount(),
                payment.getStatus(),
                payment.getMercadoPagoPaymentId(),
                payment.getQrCode(),
                payment.getQrCodeBase64(),
                payment.getTicketUrl(),
                payment.getCreatedAt(),
                payment.getUpdatedAt()
        );
    }
}