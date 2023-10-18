package com.basic.myspringboot.service;

import com.basic.myspringboot.dto.UserReqDTO;
import com.basic.myspringboot.dto.UserReqForm;
import com.basic.myspringboot.dto.UserResDTO;
import com.basic.myspringboot.entity.User;
import com.basic.myspringboot.exception.BusinessException;
import com.basic.myspringboot.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
//static import
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor //final 쓴 변수의 생성자를 만들어줌
@Transactional  
public class UserService {  //비즈니스 로직을 보통 서비스에 넣어줌
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    
    // Constructor Injection
//    public UserService(UserRepository userRepository, ModelMapper modelMapper) { //autowired 안쓰고 생성자 만들어도 됨
//        this.userRepository = userRepository;
//        this.modelMapper = modelMapper;
//    }
    public UserResDTO saveUser(UserReqDTO userReqDTO) {
        //reqDTO => entity 매핑
        User user = modelMapper.map(userReqDTO, User.class);
        //DB에 저장
        User savedUser = userRepository.save(user);
        //entity => reqDTO 매핑
        return modelMapper.map(savedUser, UserResDTO.class);
    }
    @Transactional(readOnly = true)     //조회메서드인 경우, 성능에 도움이 됨.
    public UserResDTO getUserById(Long id){
        User userEntity = userRepository.findById(id) //Optional<User>
                .orElseThrow(() -> new BusinessException(id+" User Not Found", HttpStatus.NOT_FOUND));
        //Entity를 resDTO로
        UserResDTO userResDTO = modelMapper.map(userEntity,UserResDTO.class);
        return userResDTO;
    }
    @Transactional(readOnly = true)
    public List<UserResDTO> getUsers(){
        List<User> userList = userRepository.findAll();
        //List<User> => List<UserResDTO>
        List<UserResDTO> userResDTOList = userList.stream()  //stream<User>
                //map(Function의 추상메서드는 apply(T t))
                .map(user -> modelMapper.map(user, UserResDTO.class))    //Stream<UserResDTO>
                .collect(toList());//List<UserResDTO>
        return userResDTOList;
    }

    public UserResDTO updateUser(String email, UserReqDTO userReqDTO) {
        User existUser = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new BusinessException(email + " User Not Found", HttpStatus.NOT_FOUND));
        //Dirty Checking 변경감지 - setter method 호출해도 update query가 실행된다.
        existUser.setName(userReqDTO.getName());
        return modelMapper.map(existUser, UserResDTO.class);
    }
    public void updateUserForm(UserReqForm userReqForm){
        User existUser = userRepository.findById(userReqForm.getId())
                .orElseThrow(() ->
                        new BusinessException(userReqForm.getId() + " User Not Found", HttpStatus.NOT_FOUND));
        existUser.setName(userReqForm.getName());
    }

    public void deleteUser(Long id) {
        User user = userRepository.findById(id) //Optional<User>
                .orElseThrow(() ->
                        new BusinessException(id + " User Not Found", HttpStatus.NOT_FOUND));
        userRepository.delete(user);
    }
}
