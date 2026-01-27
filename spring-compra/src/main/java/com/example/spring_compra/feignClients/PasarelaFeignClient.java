package com.example.spring_compra.feignClients;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.example.spring_compra.response.PasarelaPagoResponse;
import com.example.spring_compra.dto.PasarelaPagoDto;
@FeignClient(name = "pasarela", url = "http://lucabanking.us-east-1.elasticbeanstalk.com/pasarela")
public interface PasarelaFeignClient {
    @PostMapping("/compra")
    PasarelaPagoResponse compra(@RequestBody PasarelaPagoDto dto);
}
