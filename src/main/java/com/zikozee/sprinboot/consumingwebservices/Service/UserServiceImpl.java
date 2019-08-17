package com.zikozee.sprinboot.consumingwebservices.Service;

import com.zikozee.sprinboot.consumingwebservices.Entity.Address;
import com.zikozee.sprinboot.consumingwebservices.Entity.User;
import com.zikozee.sprinboot.consumingwebservices.Entity.UserList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private RestTemplate restTemplate;

//    //Field Injection
//    @Value("${crm.rest.url}")
    private String url;

    @Autowired //we used constructor injection
    public UserServiceImpl(RestTemplate myRestTemplate, @Value("${crm.rest.url}")String crmRestUrl){
        restTemplate = myRestTemplate;
        url= crmRestUrl;
        LOGGER.info("loaded property: crm.rest,url=" + crmRestUrl);
    }

    @Override
    public List<User> getUserList(){
        LOGGER.info("in userList: Calling REST API " + url);

        //make REST call
        ResponseEntity<List<User>> responseEntity =
                restTemplate.exchange(url, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<User>>(){});

        // get the list  of users from response
        List<User> users = responseEntity.getBody();
        LOGGER.info("in userList: users " + users);
        return users;
    }

    @Override
    public List<User> alternativeUserList() {
       //Some APIs will return a top-level object that contains the list of employees instead of returning the list directly.
        UserList response = restTemplate.getForObject(
                url,
                UserList.class);
        List<User> users = response.getUsers();
        return users;
    }

    @Override
    public User getUser(int id) {
        LOGGER.info("in getUser: Calling REST API " + url);

        //make REST call
        User user = restTemplate.
                getForObject(url + "/" + id, User.class);

        LOGGER.info("in getUser: users" + user);
        return user;
    }

    @Override
    public List<Address> getAddressList() {
        LOGGER.info("in getAddressList: Calling REST API " + url);
        List<Address> addresses = new ArrayList<>();
        
        for(User user : getUserList()){
            addresses.add(user.getAddress());
        }
        LOGGER.info("in getAddressList: addresses" + addresses);
        return addresses;
    }

    @Override
    public Address getUserAddress(int userId) {
        LOGGER.info("in getUserAddress: Calling REST API " + url);
        Address myAddress = null;
        for(User user : getUserList()){
            if(user.getId() == userId){
                myAddress=  getUserList().get(userId).getAddress();
            }
        }
        LOGGER.info("in getUserAddress: myAddress " + myAddress);
        return myAddress;
    }

    @Override
    public void saveUser(User user) {
        LOGGER.info("in saveUser: Calling REST API " + url);

        int userId = user.getId();

        //make REST call
        if(userId ==0){
            //add user
            restTemplate.postForEntity(url, user, String.class);
        }else{
            //update user
            restTemplate.put(url, user);
        }
        LOGGER.info("in saveUser(): success");
    }

    @Override
    public void deleteUser(int userId) {
        LOGGER.info("in deleteUser: Calling REST API " + url);
        //make REST call
        restTemplate.delete(url + "/" + userId);
        LOGGER.info("in deleteUser(): deleted customer userId = " + userId);
    }

//    @Override
//    public void delete() {
//        LOGGER.info("in deleteUser: Calling REST API " + url);
//        restTemplate.delete(url);
//        LOGGER.info("in deleteUser(): deleted all users");
//    }


}
