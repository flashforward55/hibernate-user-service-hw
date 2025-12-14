package mate.academy.service.impl;

import java.util.List;
import java.util.Optional;
import mate.academy.dao.CinemaHallDao;
import mate.academy.exception.AuthenticationException;
import mate.academy.exception.RegistrationException;
import mate.academy.lib.Inject;
import mate.academy.lib.Service;
import mate.academy.model.CinemaHall;
import mate.academy.model.User;
import mate.academy.security.AuthenticationService;
import mate.academy.service.CinemaHallService;
import mate.academy.service.UserService;
import mate.academy.util.HashUtil;

@Service
public class CinemaHallServiceImpl implements CinemaHallService {
    @Inject
    private CinemaHallDao cinemaHallDao;

    @Override
    public CinemaHall add(CinemaHall cinemaHall) {
        return cinemaHallDao.add(cinemaHall);
    }

    @Override
    public CinemaHall get(Long id) {
        return cinemaHallDao.get(id).get();
    }

    @Override
    public List<CinemaHall> getAll() {
        return cinemaHallDao.getAll();
    }

    @Service
    public static class AuthenticationServiceImpl implements AuthenticationService {
        @Inject
        private UserService userService;

        @Override
        public User login(String email, String password) throws AuthenticationException {
            Optional<User> userOptional = userService.findByEmail(email);
            if (userOptional.isPresent()) {
                User user = userOptional.get();
                String hashedPassword = HashUtil.hashPassword(password, user.getSalt());
                if (hashedPassword.equals(user.getPassword())) {
                    return user;
                }
            }
            throw new AuthenticationException("Incorrect email or password!");
        }

        @Override
        public User register(String email, String password) throws RegistrationException {
            if (userService.findByEmail(email).isPresent()) {
                throw new RegistrationException("User with this email is already registered!");
            }
            User user = new User();
            user.setEmail(email);
            user.setPassword(password);
            return userService.add(user);
        }
    }
}
