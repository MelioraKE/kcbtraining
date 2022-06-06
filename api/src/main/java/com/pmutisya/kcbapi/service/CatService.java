package com.pmutisya.kcbapi.service;

import com.pmutisya.kcbapi.domain.Cat;
import com.pmutisya.kcbapi.repository.CatRepository;
import com.pmutisya.kcbapi.service.dto.RegistryResponseDTO;
import com.pmutisya.kcbapi.service.dto.UserLocationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CatService {
    private final RestTemplate restTemplate = new RestTemplate();
    private String locationURL = "http://212.47.252.61:30576/api/location";
    private String registryEndpoint = "http://51.15.211.168/api/animalregistry.php";
    private final Logger logger = LoggerFactory.getLogger(CatService.class);

    private final CatRepository catRepository;

    public CatService(CatRepository catRepository) {
        this.catRepository = catRepository;
    }


    public Cat save(Cat cat){
        logger.info("Request to save cat :{}", cat);

        // get the client IP address
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String ipAddress = request.getRemoteAddr();
        logger.info("Extracted ip : {}", ipAddress);

        UserLocationDTO locationDTO = getUserLocation(ipAddress);
        cat.setCreatorCountryName(locationDTO.getCountryName());
        cat.setCreatorIpAddress(ipAddress);

        cat = catRepository.save(cat);

        sendCatToRegistry(cat);

        return cat;
    }

    public UserLocationDTO getUserLocation(String ipAddress){
        //add the ip to our location url
        String userLocationURL = locationURL + "/"+ipAddress;

        ResponseEntity<UserLocationDTO> responseEntity = restTemplate.getForEntity(userLocationURL, UserLocationDTO.class);

        UserLocationDTO responseBody = responseEntity.getBody();

        logger.info("Received response : {}", responseBody);
        return responseBody;
    }

    public void sendCatToRegistry(Cat cat){
        logger.info("Request to send cat : {} to animal registry", cat);

        ResponseEntity<RegistryResponseDTO> responseEntity = restTemplate.postForEntity(registryEndpoint, cat,
                RegistryResponseDTO.class);
        logger.info("Successfully registered with animal registry. Response : {}", responseEntity.getBody());
    }

    public Optional<Cat> findById(Integer id){
        logger.info("Request to find cat with id : {}", id);
        return catRepository.findById(id);
    }

    public List<Cat> findAll(){
        logger.info("Request to find all cats");
        return catRepository.findAll();
    }
}
