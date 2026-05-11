package com.aurapay.application.usecase;

import com.aurapay.application.dto.PixPaymentResponse;
import com.aurapay.common.exception.BusinessException;
import com.aurapay.config.MercadoPagoProperties;
import com.aurapay.domain.model.PixPayment;
import com.aurapay.domain.port.in.GetPixPaymentUseCase;
import com.aurapay.domain.port.in.ProcessPixWebhookUseCase;
import com.aurapay.domain.port.in.SimulatePixApprovalUseCase;
import com.aurapay.domain.port.out.PixPaymentRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class SimulatePixApprovalUseCaseImpl implements SimulatePixApprovalUseCase {

    private final PixPaymentRepositoryPort pixPaymentRepository;
    private final ProcessPixWebhookUseCase processPixWebhookUseCase;
    private final GetPixPaymentUseCase getPixPaymentUseCase;
    private final MercadoPagoProperties properties;

    public SimulatePixApprovalUseCaseImpl(
            PixPaymentRepositoryPort pixPaymentRepository,
            ProcessPixWebhookUseCase processPixWebhookUseCase,
            GetPixPaymentUseCase getPixPaymentUseCase,
            MercadoPagoProperties properties
    ) {
        this.pixPaymentRepository = pixPaymentRepository;
        this.processPixWebhookUseCase = processPixWebhookUseCase;
        this.getPixPaymentUseCase = getPixPaymentUseCase;
        this.properties = properties;
    }

    @Override
    public PixPaymentResponse execute(Long pixPaymentId) {
        if (!properties.isMock()) {
            throw new BusinessException("Simulação só é permitida quando mercado-pago.mock=true");
        }

        PixPayment payment = pixPaymentRepository.findById(pixPaymentId)
                .orElseThrow(() -> new BusinessException("Pagamento Pix não encontrado: " + pixPaymentId));

        processPixWebhookUseCase.execute(payment.getMercadoPagoPaymentId());
        return getPixPaymentUseCase.execute(pixPaymentId);
    }
}