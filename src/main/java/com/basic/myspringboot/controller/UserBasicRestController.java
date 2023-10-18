package com.basic.myspringboot.controller;

import com.basic.myspringboot.entity.User;
import com.basic.myspringboot.exception.BusinessException;
import com.basic.myspringboot.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserBasicRestController {
    @Autowired
    private UserRepository userRepository;

    @PostMapping
    //@RequestBody Java Object -> JSON으로 변환된 데이터를 응답(response) body에 담아 주는 역할
    public User create(@RequestBody User user) {
        return userRepository.save(user);   //save = insert
    }

    @GetMapping
    public List<User> getUsers() {
        return userRepository.findAll();
    }
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id){
        Optional<User> optionalUser = userRepository.findById(id);
//        if(optionalUser.isPresent()){
//            User user = optionalUser.get();
//            return user;
//        }
        //orElseThrow(Supplier) Supplier의 추상매서드가 T get()
        User user = optionalUser.orElseThrow(()->new BusinessException("User Not Found", HttpStatus.NOT_FOUND));
        return user;
    }

    @GetMapping("/email/{email}")
    public User getUserByEmail(@PathVariable String email){
        return userRepository.findByEmail(email).orElseThrow(()->new BusinessException("요청하신 email에 해당하는 user가 없습니다.", HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("User Not Found", HttpStatus.NOT_FOUND));
        userRepository.delete(user);
        //return ResponseEntity.ok(user);
        return ResponseEntity.ok(id+" User가 삭제되었습니다.");
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User userDetail) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessException("User Not Found", HttpStatus.NOT_FOUND));
        //수정하려는 값을 저장
        user.setName(userDetail.getName());
        user.setEmail(userDetail.getEmail());
        User updatedUser = userRepository.save(user);
        return updatedUser;
    }
}
