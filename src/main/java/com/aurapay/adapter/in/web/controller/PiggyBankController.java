package com.aurapay.adapter.in.web.controller;

import com.aurapay.application.dto.piggybank.CreatePiggyBankRequest;
import com.aurapay.application.dto.piggybank.CreatePiggyBankResponse;
import com.aurapay.application.dto.piggybank.DepositToPiggyBankRequest;
import com.aurapay.application.dto.piggybank.DepositToPiggyBankResponse;
import com.aurapay.application.dto.piggybank.ListPiggyBanksResponse;
import com.aurapay.application.dto.piggybank.WithdrawFromPiggyBankRequest;
import com.aurapay.application.dto.piggybank.WithdrawFromPiggyBankResponse;
import com.aurapay.application.dto.piggybank.PiggyBankYieldHistoryResponse;
import com.aurapay.domain.port.in.piggybank.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cofrinhos")
public class PiggyBankController {

    private final CreatePiggyBankUseCase createPiggyBankUseCase;
    private final ListPiggyBanksUseCase listPiggyBanksUseCase;
    private final DepositToPiggyBankUseCase depositToPiggyBankUseCase;
    private final WithdrawFromPiggyBankUseCase withdrawFromPiggyBankUseCase;
    private final GetPiggyBankYieldHistoryUseCase getPiggyBankYieldHistoryUseCase;
    private final DeletePiggyBankUseCase deletePiggyBankUseCase;

    public PiggyBankController(CreatePiggyBankUseCase createPiggyBankUseCase,
                               ListPiggyBanksUseCase listPiggyBanksUseCase,
                               DepositToPiggyBankUseCase depositToPiggyBankUseCase,
                               WithdrawFromPiggyBankUseCase withdrawFromPiggyBankUseCase,
                               GetPiggyBankYieldHistoryUseCase getPiggyBankYieldHistoryUseCase, DeletePiggyBankUseCase deletePiggyBankUseCase) {
        this.createPiggyBankUseCase = createPiggyBankUseCase;
        this.listPiggyBanksUseCase = listPiggyBanksUseCase;
        this.depositToPiggyBankUseCase = depositToPiggyBankUseCase;
        this.withdrawFromPiggyBankUseCase = withdrawFromPiggyBankUseCase;
        this.getPiggyBankYieldHistoryUseCase = getPiggyBankYieldHistoryUseCase;
        this.deletePiggyBankUseCase = deletePiggyBankUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreatePiggyBankResponse create(@RequestBody CreatePiggyBankRequest request) {
        return createPiggyBankUseCase.execute(request);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ListPiggyBanksResponse list(@RequestParam Long customerId) {
        return listPiggyBanksUseCase.execute(customerId);
    }

    @PostMapping("/{piggyBankId}/deposit")
    @ResponseStatus(HttpStatus.OK)
    public DepositToPiggyBankResponse deposit(@PathVariable Long piggyBankId,
                                              @RequestBody DepositToPiggyBankRequest request) {
        request.setPiggyBankId(piggyBankId);
        return depositToPiggyBankUseCase.execute(request);
    }

    @PostMapping("/{piggyBankId}/withdraw")
    @ResponseStatus(HttpStatus.OK)
    public WithdrawFromPiggyBankResponse withdraw(@PathVariable Long piggyBankId,
                                                  @RequestBody WithdrawFromPiggyBankRequest request) {
        request.setPiggyBankId(piggyBankId);
        return withdrawFromPiggyBankUseCase.execute(request);
    }

    @GetMapping("/{piggyBankId}/yield")
    @ResponseStatus(HttpStatus.OK)
    public PiggyBankYieldHistoryResponse yieldHistory(@PathVariable Long piggyBankId) {
        return getPiggyBankYieldHistoryUseCase.execute(piggyBankId);
    }

    @DeleteMapping("/{piggyBankId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long piggyBankId, @RequestParam Long customerId) {
        deletePiggyBankUseCase.execute(piggyBankId, customerId);
    }
}
