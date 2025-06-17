package it.epicode.u5w3d2pratica.service;

import it.epicode.u5w3d2pratica.dto.UserDto;
import it.epicode.u5w3d2pratica.enumeration.Role;
import it.epicode.u5w3d2pratica.exception.NotFoundException;
import it.epicode.u5w3d2pratica.model.User;
import it.epicode.u5w3d2pratica.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User saveUser(UserDto userDto){
        User user = new User();
        user.setNome(userDto.getNome());
        user.setCognome(userDto.getCognome());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRole(Role.USER);

        return userRepository.save(user);
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User getUser(int id) throws NotFoundException {
        return userRepository.findById(id).orElseThrow(()-> new NotFoundException("User con id "
                + id + " non trovato"));
    }

    public User updateUser(int id, UserDto userDto) throws NotFoundException {
        User userDaAggiornare = getUser(id);
        userDaAggiornare.setNome(userDto.getNome());
        userDaAggiornare.setCognome(userDto.getCognome());
        userDaAggiornare.setEmail(userDto.getEmail());
        if (!passwordEncoder.matches(userDto.getPassword(), userDaAggiornare.getPassword())){
            userDaAggiornare.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        return userRepository.save(userDaAggiornare);
    }

    public void deleteUser(int id) throws NotFoundException {
        User userDaEliminare = getUser(id);

        userRepository.delete(userDaEliminare);
    }
}
