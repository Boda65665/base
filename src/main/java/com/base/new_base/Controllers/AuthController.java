package com.base.new_base.Controllers;


import com.base.new_base.DTO.TemporaryUserDTO;
import com.base.new_base.DTO.UserDTO;
import com.base.new_base.Entiti.Role;
import com.base.new_base.Forms.EditEmailForm;
import com.base.new_base.Forms.RestorePasswordForm;
import com.base.new_base.JWT.jwtProvider;
import com.base.new_base.Mail.GmailSender;
import com.base.new_base.Service.User.TemporaryUserService;
import com.base.new_base.Service.User.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RequestMapping("/auth")

@Controller
public class AuthController {
    final GmailSender gmailSender = new GmailSender();
    final AuthenticationManager manager;
    final PasswordEncoder passwordEncoder;
    final UserService userService;
    final TemporaryUserService temporaryUserService;
    final jwtProvider provider;
    private String generateSecretKeyForConfirmationEmail(String email){
        return DigestUtils
                .md5Hex(passwordEncoder.encode(email)).toUpperCase();
    }

    @Autowired
    public AuthController(AuthenticationManager manager, PasswordEncoder passwordEncoder, UserService userService, TemporaryUserService temporaryUserService, jwtProvider provider) {
        this.manager = manager;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.temporaryUserService = temporaryUserService;
        this.provider = provider;
    }

    @GetMapping("/reg")
    public String regGet(Model model) {
        TemporaryUserDTO new_user = new TemporaryUserDTO();
        model.addAttribute("user", new_user);
        return "auth/reg";
    }


    @PostMapping("/reg")
    public String regPost(Model model, HttpServletResponse response, @ModelAttribute("user") @Valid TemporaryUserDTO userDTO, BindingResult bindingResult) throws Exception {
        if (bindingResult.hasErrors()) {
            return "auth/reg";
        }
        if (temporaryUserService.findByEmail(userDTO.getEmail()) != null || userService.findByEmail(userDTO.getEmail())!=null) {
            model.addAttribute("user_already_created", "User with so email already created");
            return "redirect:/auth/login";
        } else {

            userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            userDTO.setRole(Role.UNVERIFIED);
            //мы хэшируем email, захэшированый password encoder-ом(с использованием соли).
            String secret = generateSecretKeyForConfirmationEmail(userDTO.getEmail());
            userDTO.setKeyForConfirmEmail(secret);
            temporaryUserService.save(userDTO);
            provider.generateToken(userDTO.getEmail(),response);


            gmailSender.sendEmail("Confirmation your email", "Что бы подтвердить почту перейдите по  этой ссылке: http://localhost:8080/auth/confirmEmail?keyConfirm=" + secret, userDTO.getEmail());


        }
        return "redirect:/email_confirmation";
    }

    @GetMapping("/login")
    public String loginGet(Model model) {
        UserDTO userDTO = new UserDTO();
        model.addAttribute("user", userDTO);
        return "auth/login";
    }

    @PostMapping("/login")
    public String loginPost(Model model, @ModelAttribute("user") UserDTO userDTO, HttpServletResponse response) {
        try {

            manager.authenticate(new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword()));
            provider.generateToken(userDTO.getEmail(),response);
            return "redirect:/home/";


        } catch (AuthenticationException ee) {

            model.addAttribute("not_correct_ps", "Email or password not corrected");
            return "auth/login";
        }


    }

    @GetMapping("/email_confirmation")
    public String emailConfirmationTemplate(HttpServletRequest rq) {
        System.out.println(provider.getRole(provider.resolveToken(rq)));
        return "/auth/email_confirm/send_email_msg";
    }

    @GetMapping("/email_confirm/{secretKey}")
    public String emailConfirmation(@PathVariable(value = "secretKey") String secretKey, HttpServletResponse response) {
        if (secretKey == null || temporaryUserService.findByKey(secretKey) == null)
            return "redirect:/email_confirmation";
        TemporaryUserDTO temporaryUserDTO = temporaryUserService.findByKey(secretKey);
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail(temporaryUserDTO.getEmail());
        userDTO.setLogin(temporaryUserDTO.getLogin());
        userDTO.setPassword(temporaryUserDTO.getPassword());
        userDTO.setRole(Role.USER);
        userService.save(userDTO);
        temporaryUserService.deleteByID(temporaryUserDTO.getId());
        provider.generateToken(userDTO.getEmail(),response);

        return "redirect:/email_confirmation_confirm";

    }

    @GetMapping("/edit_email")
    public String emailConfirmConfigGet(Model model) {
        EditEmailForm editEmailForm = new EditEmailForm();
        model.addAttribute("user", editEmailForm);

        return "/auth/email_confirm/edit_email";
    }

    @PostMapping("/edit_email")
    public String emailConfirmConfigPost(Model model, HttpServletResponse response, @ModelAttribute("user") @Valid EditEmailForm editEmailForm, BindingResult bindingResult,@CookieValue("JWT") String token) {
        if (bindingResult.hasFieldErrors("email") || bindingResult.hasFieldErrors("password")) {
            return "/auth/email_confirm/edit_email";
        }
        if (temporaryUserService.findByEmail(editEmailForm.getEmail()) != null || userService.findByEmail(editEmailForm.getEmail())!=null) {
            model.addAttribute("user_already_created", "User with so email already created");
            return "/auth/email_confirm/edit_email";
        } else {
            String email = provider.getUsername(token);

            TemporaryUserDTO userDTO = temporaryUserService.findByEmail(email);
            String ps = userDTO.getPassword();

            if (passwordEncoder.matches(editEmailForm.getPassword(),ps)) {
                userDTO.setEmail(editEmailForm.getEmail());

                temporaryUserService.update(userDTO);
                System.out.println("2");
                provider.generateToken(editEmailForm.getEmail(),response);
                return "redirect:/email_confirmation";

            }
            else {
                model.addAttribute("ps_error","Неверный пароль");
                return "/auth/email_confirm/edit_email";
            }



        }


    }
    @PostMapping("/restoring_password")
    public String restoringPasswordEmailFormPost(Model model,HttpServletRequest request)throws Exception{
        String email = request.getParameter("email");
        System.out.println(email);
        //мы хэшируем email, захэшированый password encoder-ом(с использованием соли).
        String secret = generateSecretKeyForConfirmationEmail(email);
        if(userService.findByEmail(email)!=null) {

            UserDTO userDTO = userService.findByEmail(email);
            userDTO.setKeyForConfirmEmail(secret);
            userService.update(userDTO);
        }
        else if (temporaryUserService.findByEmail(email)!=null){
            TemporaryUserDTO userDTO = temporaryUserService.findByEmail(email);
            userDTO.setKeyForConfirmEmail(secret);
            temporaryUserService.update(userDTO);
        }
        else {
            model.addAttribute("err_not_found","Пользователь с такой почтой не найден");
            return "redirect:/auth/restoring_password";
        }
        gmailSender.sendEmail("Востановление пароля","ссылка для востановление: http://localhost:8080/auth/restore_password/"+secret,email);
        return "/auth/restore_password/send_email_msg";
    }
    @GetMapping("/restoring_password")
    public String restoringPasswordEmailFormGet(){
        return "/auth/restore_password/email_form";
    }
    @GetMapping("/restore_password/{key}")
    public String restorePasswordGet(@PathVariable("key") String secret_key,Model model){
        if (userService.findByKey(secret_key)==null && temporaryUserService.findByKey(secret_key)==null) return "redirect:/auth/login";

        RestorePasswordForm restorePasswordForm = new RestorePasswordForm();
        model.addAttribute("restorePasswordForm",restorePasswordForm);
        return "/auth/restore_password/restoring_password_form";
    }
    @PostMapping("/restore_password/{key}")
    public String restorePasswordPost(@PathVariable("key") String secret_key,@ModelAttribute("restorePasswordForm") @Valid RestorePasswordForm restorePasswordForm,BindingResult bindingResult){

        if (bindingResult.hasErrors()){
            return "/auth/restore_password/restoring_password_form";
        }
        if (userService.findByKey(secret_key)!=null) {

            UserDTO userDTO = userService.findByKey(secret_key);
            String newPassword = restorePasswordForm.getPassword();
            userDTO.setPassword(passwordEncoder.encode(newPassword));
            String secret = generateSecretKeyForConfirmationEmail(userDTO.getEmail());
            userDTO.setKeyForConfirmEmail(secret);
            userService.update(userDTO);
        }
        else if (temporaryUserService.findByKey(secret_key)!=null)  {
            TemporaryUserDTO userDTO = temporaryUserService.findByKey(secret_key);
            String newPassword = restorePasswordForm.getPassword();
            userDTO.setPassword(passwordEncoder.encode(newPassword));
            String secret = generateSecretKeyForConfirmationEmail(userDTO.getEmail());
            userDTO.setKeyForConfirmEmail(secret);
            temporaryUserService.update(userDTO);
        }
        return "redirect:/auth/login";



   }
   @GetMapping("/logout")
    public String logout(HttpServletResponse response,HttpServletRequest request){
        provider.deleteCookie(request,response,"JWT");
        return "redirect:/auth/login";
   }
}
