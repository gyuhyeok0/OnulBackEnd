package onul.restapi.member.controller;

import onul.restapi.member.service.DeleteAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/delete")
public class DeleteAccountController {

    private final DeleteAccountService deleteAccountService;

    public DeleteAccountController(DeleteAccountService deleteAccountService) {
        this.deleteAccountService = deleteAccountService;
    }

    @PostMapping(value = "/deleteAccount", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> deleteAccount(@RequestParam("memberId") String memberId) {
        System.out.println("회원 탈퇴 요청 받음: " + memberId);

        try {
            deleteAccountService.deleteAccount(memberId);
            return ResponseEntity.ok(Collections.singletonMap("status", "success"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("status", "fail"));
        }
    }
}
