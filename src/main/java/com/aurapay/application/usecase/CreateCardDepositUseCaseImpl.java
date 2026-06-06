package com.aurapay.application.usecase;

import com.aurapay.application.dto.CardDepositRequest;
import com.aurapay.application.dto.CardDepositResponse;
import com.aurapay.application.dto.MercadoPagoCardCharge;
import com.aurapay.common.exception.BusinessException;
import com.aurapay.domain.model.Customer;
import com.aurapay.domain.model.PixStatus;
import com.aurapay.domain.model.Wallet;
import com.aurapay.domain.model.WalletTransaction;
import com.aurapay.domain.port.in.CreateCardDepositUseCase;
import com.aurapay.domain.port.out.CustomerRepositoryPort;
import com.aurapay.domain.port.out.MercadoPagoPort;
import com.aurapay.domain.port.out.WalletRepositoryPort;
import com.aurapay.domain.port.out.WalletTransactionRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Recarga da carteira via cartão de crédito usando o Mercado Pago.
 *
 * O número do cartão NUNCA chega aqui: o front tokeniza o cartão com o SDK do
 * Mercado Pago e envia apenas o token. O pagamento com cartão é síncrono — o MP
 * já responde aprovado/recusado, então creditamos a carteira na hora (sem webhook).
 */
@Service
public class CreateCardDepositUseCaseImpl implements CreateCardDepositUseCase {

    private static final Logger log = LoggerFactory.getLogger(CreateCardDepositUseCaseImpl.class);

    private final WalletRepositoryPort walletRepository;
    private final WalletTransactionRepositoryPort walletTransactionRepository;
    private final CustomerRepositoryPort customerRepository;
    private final MercadoPagoPort mercadoPagoPort;

    public CreateCardDepositUseCaseImpl(
            WalletRepositoryPort walletRepository,
            WalletTransactionRepositoryPort walletTransactionRepository,
            CustomerRepositoryPort customerRepository,
            MercadoPagoPort mercadoPagoPort
    ) {
        this.walletRepository = walletRepository;
        this.walletTransactionRepository = walletTransactionRepository;
        this.customerRepository = customerRepository;
        this.mercadoPagoPort = mercadoPagoPort;
    }

    @Override
    @Transactional
    public CardDepositResponse execute(CardDepositRequest request) {
        if (request == null || request.getCustomerId() == null) {
            throw new BusinessException("customerId é obrigatório");
        }
        if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("amount deve ser maior que zero");
        }
        if (request.getCardToken() == null || request.getCardToken().isBlank()) {
            throw new BusinessException("cardToken é obrigatório");
        }
        if (request.getPaymentMethodId() == null || request.getPaymentMethodId().isBlank()) {
            throw new BusinessException("paymentMethodId é obrigatório");
        }

        Wallet wallet = walletRepository.findByCustomerId(request.getCustomerId())
                .orElseThrow(() -> new BusinessException("Carteira não encontrada para o cliente"));

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new BusinessException("Cliente não encontrado"));

        int installments = (request.getInstallments() != null && request.getInstallments() > 0)
                ? request.getInstallments()
                : 1;

        String description = "Recarga AuraPay - " + customer.getFullName();

        MercadoPagoCardCharge charge = mercadoPagoPort.createCardPayment(
                request.getAmount(),
                request.getCardToken(),
                request.getPaymentMethodId(),
                installments,
                description,
                customer.getEmail(),
                customer.getDocumentNumber()
        );

        boolean approved = charge.getStatus() == PixStatus.APPROVED;

        if (approved) {
            wallet.setBalance(wallet.getBalance().add(request.getAmount()));
            wallet = walletRepository.save(wallet);
            walletTransactionRepository.save(WalletTransaction.cardDeposit(
                    wallet.getCustomerId(), wallet.getId(), request.getAmount()));
            log.info("Carteira {} creditada em {} via cartão (mpId={}).",
                    wallet.getId(), request.getAmount(), charge.getMercadoPagoPaymentId());
        } else {
            log.info("Pagamento com cartão NÃO aprovado (status={}, detalhe={}). Carteira não creditada.",
                    charge.getStatus(), charge.getStatusDetail());
        }

        return new CardDepositResponse(
                charge.getStatus(),
                charge.getStatusDetail(),
                approved,
                request.getAmount(),
                wallet.getCustomerId(),
                wallet.getId(),
                wallet.getBalance(),
                charge.getLast4(),
                charge.getBrand(),
                charge.getMercadoPagoPaymentId()
        );
    }
}