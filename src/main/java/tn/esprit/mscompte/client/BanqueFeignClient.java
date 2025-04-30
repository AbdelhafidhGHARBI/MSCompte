package tn.esprit.mscompte.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import tn.esprit.mscompte.dto.BanqueDto;

@FeignClient(name = "ms-banque", url = "http://localhost:8082/api/banques")
public interface BanqueFeignClient {

    @GetMapping("/{id}")
    BanqueDto getBanqueById(@PathVariable("id") String id);

    @PutMapping("/{id}/comptes")
    BanqueDto addCompteToBanque(@PathVariable("id") String id, @RequestBody Long compteId);
}
