package edu.architect_711.wordsapp.controller;

import edu.architect_711.wordsapp.model.dto.account.*;
import edu.architect_711.wordsapp.service.account.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Account", description = "Endpoints for managing accounts")
@SecurityRequirement(name = "BasicAuth")
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @Operation(summary = "Get an account")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Account found",
                    content = @Content(schema = @Schema(implementation = AccountDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Account not found"
            )
    })
    @GetMapping(consumes = "*/*", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<AccountDto> get() {
        var account = accountService.get();

        return account.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(
            summary = "Login and return Base64-encoded auth token",
            operationId = "public",
            security = @SecurityRequirement(name = "")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Token returned",
                    content = @Content(schema = @Schema(implementation = AuthBaseTokenResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Account not found"
            )
    })
    @PostMapping(value = "/login64", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthBaseTokenResponse> login64(@RequestBody AccountLoginRequest accountLoginRequest) {
        return ResponseEntity.ok(accountService.login64(accountLoginRequest));
    }

    @Operation(
            summary = "Create a new account and return Base64-encoded auth token",
            operationId = "public"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Account created and token returned",
                    content = @Content(schema = @Schema(implementation = AuthBaseTokenResponse.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Account already exists"
            )
    })
    @SecurityRequirements
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthBaseTokenResponse> save(@RequestBody SaveAccountDto accountDto) {
        return ResponseEntity.ok(accountService.save64(accountDto));
    }

    // TODO change password, and verification by email

    @Operation(summary = "Update existing account")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Updated successfully",
                    content = @Content(schema = @Schema(implementation = AccountDto.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Account not found"
            )
    })
    @PutMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public AccountDto update(@RequestBody UpdateAccountRequest accountDto) {
        return accountService.update(accountDto);
    }

    @Operation(summary = "Delete account", description = "Delete an account even if it doesn't exist")
    @ApiResponse(
            responseCode = "200",
            description = "Account deleted successfully"
    )
    @DeleteMapping
    public void delete() {
        accountService.delete();
    }

}
