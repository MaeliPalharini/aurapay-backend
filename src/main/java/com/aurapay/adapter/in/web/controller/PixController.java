package com.aurapay.adapter.in.web.controller;

import com.aurapay.application.dto.CreatePixKeyRequest;
import com.aurapay.application.dto.CreatePixPaymentRequest;
import com.aurapay.application.dto.CreatePixPaymentResponse;
import com.aurapay.application.dto.MercadoPagoWebhookPayload;
import com.aurapay.application.dto.PixKeyResponse;
import com.aurapay.application.dto.PixPaymentResponse;
import com.aurapay.domain.model.PixPayment;
import com.aurapay.domain.port.in.CreatePixKeyUseCase;
import com.aurapay.domain.port.in.CreatePixPaymentUseCase;
import com.aurapay.domain.port.in.GetPixPaymentUseCase;
import com.aurapay.domain.port.in.ListPixKeysByCustomerUseCase;
import com.aurapay.domain.port.in.ProcessPixWebhookUseCase;
import com.aurapay.domain.port.in.SimulatePixApprovalUseCase;
import com.aurapay.domain.port.out.PixPaymentRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pix")
public class PixController {

    private static final Logger log = LoggerFactory.getLogger(PixController.class);

    private final CreatePixPaymentUseCase createPixPaymentUseCase;
    private final GetPixPaymentUseCase getPixPaymentUseCase;
    private final ProcessPixWebhookUseCase processPixWebhookUseCase;
    private final SimulatePixApprovalUseCase simulatePixApprovalUseCase;
    private final PixPaymentRepositoryPort pixPaymentRepository;
    private final CreatePixKeyUseCase createPixKeyUseCase;
    private final ListPixKeysByCustomerUseCase listPixKeysByCustomerUseCase;

    public PixController(
            CreatePixPaymentUseCase createPixPaymentUseCase,
            GetPixPaymentUseCase getPixPaymentUseCase,
            ProcessPixWebhookUseCase processPixWebhookUseCase,
            SimulatePixApprovalUseCase simulatePixApprovalUseCase,
            PixPaymentRepositoryPort pixPaymentRepository,
            CreatePixKeyUseCase createPixKeyUseCase,
            ListPixKeysByCustomerUseCase listPixKeysByCustomerUseCase
    ) {
        this.createPixPaymentUseCase = createPixPaymentUseCase;
        this.getPixPaymentUseCase = getPixPaymentUseCase;
        this.processPixWebhookUseCase = processPixWebhookUseCase;
        this.simulatePixApprovalUseCase = simulatePixApprovalUseCase;
        this.pixPaymentRepository = pixPaymentRepository;
        this.createPixKeyUseCase = createPixKeyUseCase;
        this.listPixKeysByCustomerUseCase = listPixKeysByCustomerUseCase;
    }

    @PostMapping("/payments")
    @ResponseStatus(HttpStatus.CREATED)
    public CreatePixPaymentResponse createPayment(@RequestBody CreatePixPaymentRequest request) {
        return createPixPaymentUseCase.execute(request);
    }

    @GetMapping("/payments/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PixPaymentResponse getPayment(@PathVariable Long id) {
        return getPixPaymentUseCase.execute(id);
    }

    @PostMapping("/webhook")
    @ResponseStatus(HttpStatus.OK)
    public void receiveWebhook(@RequestBody(required = false) MercadoPagoWebhookPayload payload) {
        if (payload == null || payload.getData() == null) {
            log.info("Webhook recebido sem payload válido; respondendo 200 mesmo assim.");
            return;
        }
        log.info("Webhook recebido: type={}, action={}, paymentId={}",
                payload.getType(), payload.getAction(), payload.getData().getId());
        processPixWebhookUseCase.execute(payload.getData().getId());
    }

    @PostMapping("/payments/{id}/simulate-approval")
    @ResponseStatus(HttpStatus.OK)
    public PixPaymentResponse simulateApproval(@PathVariable Long id) {
        return simulatePixApprovalUseCase.execute(id);
    }

    @PostMapping("/keys")
    @ResponseStatus(HttpStatus.CREATED)
    public PixKeyResponse createKey(@RequestBody CreatePixKeyRequest request) {
        return createPixKeyUseCase.execute(request);
    }

    @GetMapping("/keys")
    @ResponseStatus(HttpStatus.OK)
    public List<PixKeyResponse> listKeys(@RequestParam Long customerId) {
        return listPixKeysByCustomerUseCase.execute(customerId);
    }

    @GetMapping(value = "/mock/ticket/{mpId}", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String mockTicketPage(@PathVariable String mpId) {
        Long pixId = pixPaymentRepository.findByMercadoPagoPaymentId(mpId)
                .map(PixPayment::getId)
                .orElse(null);
        return renderMockTicketPage(mpId, pixId);
    }

    private String renderMockTicketPage(String mpId, Long pixId) {
        String simulateButton = (pixId != null)
                ? """
                    <button onclick="pay()" id="payBtn" class="btn">Confirmar pagamento</button>
                    <p id="status" class="status"></p>
                    <script>
                      async function pay() {
                        const btn = document.getElementById('payBtn');
                        const status = document.getElementById('status');
                        btn.disabled = true;
                        btn.textContent = 'Processando...';
                        try {
                          const res = await fetch('/pix/payments/%d/simulate-approval', { method: 'POST' });
                          if (!res.ok) throw new Error('Falha ao confirmar pagamento');
                          const data = await res.json();
                          status.textContent = '✓ Pagamento APROVADO! Volte ao app.';
                          status.className = 'status ok';
                          btn.style.display = 'none';
                        } catch (e) {
                          status.textContent = '✗ ' + e.message;
                          status.className = 'status error';
                          btn.disabled = false;
                          btn.textContent = 'Tentar novamente';
                        }
                      }
                    </script>
                  """.formatted(pixId)
                : """
                    <p class="hint">Para confirmar o pagamento, volte ao AuraPay e clique em <strong>"Já paguei"</strong>.</p>
                  """;

        return """
                <!DOCTYPE html>
                <html lang="pt-BR">
                <head>
                  <meta charset="UTF-8">
                  <title>Mock Mercado Pago — AuraPay</title>
                  <style>
                    body { font-family: system-ui, -apple-system, sans-serif; max-width: 480px;
                           margin: 40px auto; padding: 24px; background: #f5f5f5; }
                    .card { background: white; border-radius: 12px; padding: 32px;
                            box-shadow: 0 2px 8px rgba(0,0,0,0.06); }
                    h1 { color: #009ee3; margin-top: 0; }
                    .badge { display: inline-block; padding: 4px 10px; border-radius: 999px;
                             background: #fff3cd; color: #856404; font-size: 12px; font-weight: 600; }
                    code { background: #eee; padding: 2px 6px; border-radius: 4px; font-size: 13px; }
                    p { color: #444; line-height: 1.5; }
                    .btn { background: #009ee3; color: white; border: none; padding: 12px 24px;
                           border-radius: 8px; font-size: 16px; font-weight: 600; cursor: pointer;
                           margin-top: 16px; width: 100%%; }
                    .btn:disabled { background: #999; cursor: not-allowed; }
                    .status { margin-top: 16px; font-weight: 600; padding: 12px; border-radius: 6px; }
                    .status.ok { background: #d4edda; color: #155724; }
                    .status.error { background: #f8d7da; color: #721c24; }
                    .hint { color: #666; font-style: italic; }
                  </style>
                </head>
                <body>
                  <div class="card">
                    <span class="badge">SIMULAÇÃO</span>
                    <h1>Cobrança Pix Mock</h1>
                    <p>Esta é uma página de simulação do Mercado Pago para o AuraPay.</p>
                    <p>ID do pagamento MP: <code>%s</code></p>
                    %s
                  </div>
                </body>
                </html>
                """.formatted(mpId, simulateButton);
    }
}