package com.replicacia.rest.security.service.impl;

import com.replicacia.exception.ResourceExistsException;
import com.replicacia.model.AppUser;
import com.replicacia.repo.UserRepository;
import com.replicacia.rest.security.dto.UserRegisterRequestDTO;
import com.replicacia.rest.security.dto.UserRegisterResponseDTO;
import com.replicacia.rest.security.service.UserService;
import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ModelMapper mapper;
    @Value("${login.maxretry}")
    private int maxRetry;
    @Value("${otp.service.host}")
    private String otpServiceHost;
    @Value("${otp.service.send_api}")
    private String otpServiceSendApi;

    @Autowired
    private RestTemplate restTemplate;


    /*
     * save user.
     */
    @Override
    public AppUser save(AppUser appUser) {
        setNewUserData(appUser);
        Optional<AppUser> optionalAppUser=userRepository.findByUsername(appUser.getUsername());
        if(optionalAppUser.isPresent()){
            throw new ResourceExistsException("Username: " + appUser.getUsername()+ " is not available. Please use another");
        }
        Optional<AppUser> optionalAppUser1 = userRepository.findByEmail(appUser.getEmail());

        if(optionalAppUser1.isPresent()){
            throw new ResourceExistsException("EmailId: " + appUser.getEmail() + " is already registered with us. PLease use alternate EmailId");
        }
        appUser.setRoles(appUser.getRoles()!= null ? appUser.getRoles() : new HashSet<>());
        //setDefaultRoles(appUser.getRoles());
        AppUser user = userRepository.save(appUser);
        //sendNotification(appUser,"welcome");
        return user;
    }


    /*
     * update user.
     */
    @Override
    public AppUser update(AppUser appUser) {
        if (appUser.getPasswordChanged()) {
            appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        }
       // appUser.setSuperUser(false);
        AppUser user =  userRepository.save(appUser);
        //sendNotification(appUser,"update");
        return user;
    }

    /**
     * set details for new users
     * 
     * @param appUser
     */
    private void setNewUserData(AppUser appUser) {
        if (appUser.getUserId() == null) {
            appUser.setLoginAttempts(maxRetry);
            appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
            appUser.setIsEmailVerified(Boolean.FALSE);
            appUser.setActive(Boolean.FALSE);
        }
    }

    /**
     * find all users by active status
     */
    @Override
    public List<AppUser> findAllByActive(Boolean active) {
        return userRepository.findByActiveIs(active);
    }

    @Override
    public AppUser findAllByUserName(String userName) {
        return userRepository.findByUsername(userName).orElse(null);
    }

    @Override
    public Optional<AppUser> findById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<AppUser> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    @Override
    public Optional<AppUser> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserRegisterResponseDTO register(UserRegisterRequestDTO userRegisterRequestDTO) {
        AppUser appUser = this.mapper.map(userRegisterRequestDTO, AppUser.class);
        AppUser savedUser  = this.save(appUser);
        return this.mapper.map(savedUser, UserRegisterResponseDTO.class);
    }

    @Override
    public ResponseEntity<String> forgotPassword(String emailId){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        HttpEntity<String> httpEntity = new HttpEntity<>(emailId, headers);
        return restTemplate.exchange(URI.create(otpServiceHost + otpServiceSendApi), HttpMethod.POST, httpEntity, String.class);
    }

}
